/*
 * This file is protected by Copyright. Please refer to the COPYRIGHT file
 * distributed with this source distribution.
 *
 * This file is part of REDHAWK __REDHAWK_PROJECT__.
 *
 * REDHAWK __REDHAWK_PROJECT__ is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * REDHAWK __REDHAWK_PROJECT__ is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package redhawk.websocket.jsr356.sockets;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.OnMessage;
import javax.websocket.Session;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import CF.PortPackage.InvalidPort;
import redhawk.driver.bulkio.Packet;
import redhawk.driver.exceptions.PortException;
import redhawk.driver.port.PortListener;
import redhawk.driver.port.RedhawkPort;
import redhawk.websocket.jsr356.WebSocketDriverManager;
import redhawk.websocket.jsr356.processor.ProcessorObject;
import redhawk.websocket.jsr356.processor.WebSocketProcessor;
import redhawk.websocket.jsr356.utils.ByteBufferUtil;

public class RedhawkBulkIoWebSocket {

    private static Logger logger = Logger.getLogger(RedhawkBulkIoWebSocket.class.getName());

    
    private RedhawkPort port;
    private Gson gson;
    private Map<String, Session> sessions = new ConcurrentHashMap<>(); 
    
    public RedhawkBulkIoWebSocket(RedhawkPort port) {
        logger.info("Constructing new instance of RedhawkBulkIoWebSocket");
        this.port = port;
        this.gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE).create();
    }
    
    public void addSession(Session session) {
    	sessions.put(session.getId(), session);
    }
    
    /**
     * Connects to a REDHAWK port and sends data in json or binary 
     */
    public void connect() {
        try {
            port.connect(new PortListener() {
                @Override
                public void onReceive(Packet packet) {
                	sessions.values().parallelStream().forEach(s -> processForSession(s, packet));
                }
            });
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }
    
    
    private void processForSession(Session session, Packet packet){
    	List<ProcessorObject> processingChain = (List<ProcessorObject>) session.getUserProperties().get("processingChain");
    	boolean firstSend = (boolean) session.getUserProperties().get("firstSend");
    	boolean alwaysSendSri = (boolean) session.getUserProperties().get("alwaysSendSri");
    	boolean binary = (boolean) session.getUserProperties().get("binary");
    	
        for (ProcessorObject processor : processingChain) {
            WebSocketProcessor wsProcessor = WebSocketDriverManager.webSocketProcessors.get(processor.getProcessorName().toLowerCase());
            if (wsProcessor != null) {
               packet = wsProcessor.process(packet, processor.getProcessorConfiguration());
            } else {
                logger.warning("Web Socket Processor with name: " + processor.getProcessorName() + " does not exist in the Registry");
            }
        }

        if (firstSend) {
            sendSRI(packet, session);
            session.getUserProperties().put("firstSend", false);
        } else if (alwaysSendSri || packet.isNewSri()) {
            sendSRI(packet, session);
        }

        if (binary) {
            sendBinary(packet, session);
        } else {
            sendText(packet, session);
        }
    }
    
    
    public void removeSession(Session session) {
        Session sess = sessions.get(session.getId());
        if(sess != null) {
        	sessions.remove(session.getId());
        }
        
        if(sessions.isEmpty()){
        	try {
        		port.disconnect();
        	} catch (PortException e) {
        		logger.severe(e.getMessage());
        	} finally {
//        		if (this.isNewDriverInstance()) {
//        			this.getRedhawk().disconnect();
//        		}
        	}
        }
    }

    /**
     * Used to handle in incoming text for management of Websocket Processors. 
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        logger.fine("onMessage() Invoked......" + message);
    	List<ProcessorObject> processingChain = (List<ProcessorObject>) session.getUserProperties().get("processingChain");
        
        if (message.equals("clearProcessors")) {
        	processingChain.clear();
        } else if (message.startsWith("removeProcessor")) {
            String[] split = message.trim().split(":");
            if (split.length == 2) {
                removeProcessor(session, split[1]);
            }
        } else if (message.startsWith("processors:")) {
            logger.fine("ADDING PROCESSORS");
            message = message.replaceAll("processors\\:", "");
            addOrUpdateProcessors(session, message);
        } else if (message.equals("getProcessors")) {
            try {
                session.getBasicRemote().sendText(gson.toJson(processingChain));
            } catch (IOException e) {
                logger.severe(e.getMessage());
            }
        } else if (message.equals("listAvailableProcessors")) {
            try {
            	if(WebSocketDriverManager.webSocketProcessors !=null)
            		logger.fine("Available processors: "+WebSocketDriverManager.webSocketProcessors.keySet());
            	session.getBasicRemote().sendText(gson.toJson(WebSocketDriverManager.webSocketProcessors.keySet()));
            } catch (IOException e) {
                logger.severe(e.getMessage());
            }
        }
        
        
        session.getUserProperties().put("processingChain", processingChain);
    }
    
    /**
     * Sends SRI as json to remote endpoint(web portion of code)
     * @param packet
     */
    private void sendSRI(Packet packet, Session session) {
        try {
        	session.getBasicRemote().sendText(gson.toJson(packet));
        } catch (IOException e) {
            logger.severe(e.getMessage());
        } catch (NullPointerException npe) {
            logger.warning("Received a null packet, not sending.");
        }
    }

    /**
     * Sends binary data to remote endpoint(web portion of code).
     * @param packet
     */
    private void sendBinary(Packet packet, Session session) {
        try {
            ByteBuffer data = ByteBufferUtil.createByteArray(packet.getData());
            if (data != null) {
            	session.getBasicRemote().sendBinary(data);
            }
        } catch (IOException e) {
            logger.severe(e.getMessage());
        } catch (NullPointerException npe) {
            logger.warning("Received a null packet, not sending.");
        }
    }
    
    /**
     * Sends json response to remote endpoint(web portion of code)
     * @param packet
     */
    private void sendText(Packet packet, Session session) {
        try {
        	session.getBasicRemote().sendText(gson.toJson(packet.getData()));
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }  catch (NullPointerException npe) {
            logger.warning("Received a null packet, not sending.");
        }
    }
    
    /**
     * Method to remove a processor from the processing chain based upon name. 
     * @param processorName
     */
    private void removeProcessor(Session session, String processorName) {
    	List<ProcessorObject> processingChain = (List<ProcessorObject>) session.getUserProperties().get("processingChain");

        for (int i = 0; i < processingChain.size(); i++) {
            ProcessorObject p = processingChain.get(i);
            if (processorName.equalsIgnoreCase(p.getProcessorName())) {
            	processingChain.remove(i);
            }
        }
    }

    /**
     * Method to add or update processing chain.
     * @param data
     */
    private void addOrUpdateProcessors(Session session, String data) {
        try {
        	List<ProcessorObject> processingChain = (List<ProcessorObject>) session.getUserProperties().get("processingChain");

            logger.info("IN addorupdateprocessors()");
            logger.info("DATA IS: " + data);

            ProcessorObject[] pobjects = gson.fromJson(data, ProcessorObject[].class);

            for (ProcessorObject pobj : pobjects) {

                if (WebSocketDriverManager.webSocketProcessors.get(pobj.getProcessorName().toLowerCase()) != null && !processingChain.contains(pobj)) {
                    logger.info("addOrUpdateProcessors: ADDING PROCESSOR");
                    int index = processingChain.indexOf(pobj);
                    if(index!=-1){
                    	logger.fine("Updating processor by that name");
                    	processingChain.add(index, pobj);
                    }else{
                    	processingChain.add(pobj);                    	
                    }
                    break;
                }

                for (int i = 0; i < processingChain.size(); i++) {
                    ProcessorObject p = processingChain.get(i);
                    if (pobj.getProcessorName().equalsIgnoreCase(p.getProcessorName())) {
                    	processingChain.remove(i);
                    	processingChain.add(i, pobj);
                    }
                }
            }
            
            session.getUserProperties().put("processingChain", processingChain);

        } catch (JsonSyntaxException e) {
            logger.log(Level.FINE, "Message is not a processor object", e);
        } catch (Exception e) {
            logger.log(Level.FINE, "Caught a general exception in redhawk web socket: ", e);
        }
    }
    
    /**
     * Method to return the processing chain. 
     * @return
     */
    public List<ProcessorObject> getProcessorChain(Session session) {
		return (List<ProcessorObject>) session.getUserProperties().get("processingChain");
	}
}
