package redhawk.websocket.sockets;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jetty.websocket.api.Session;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import CF.PortPackage.InvalidPort;
import redhawk.driver.Redhawk;
import redhawk.driver.bulkio.Packet;
import redhawk.driver.exceptions.PortException;
import redhawk.driver.port.PortListener;
import redhawk.driver.port.RedhawkPort;
import redhawk.websocket.ProcessorObject;
import redhawk.websocket.WebSocketProcessor;
import redhawk.websocket.utils.ByteBufferUtil;

public class RedhawkBulkIoWebSocket extends RedhawkEventAdminWebSocket {

    private static Logger logger = Logger.getLogger(RedhawkBulkIoWebSocket.class.getName());
    private RedhawkPort port;
    private boolean binary;
    private Gson gson;
    private boolean firstSend = true;
    private boolean alwaysSendSri = false;
    private List<ProcessorObject> processorChain = new CopyOnWriteArrayList<ProcessorObject>();
	private Map<String, WebSocketProcessor> webSocketProcessorServices;

    public RedhawkBulkIoWebSocket(boolean newDriverInstance, Redhawk redhawkConnection, RedhawkPort port, boolean binary, boolean alwaysSendSri, Map<String, WebSocketProcessor> webSocketProcessorServices, String path) {
    	super(newDriverInstance, redhawkConnection, path);
        logger.info("Constructing new instance of RedhawkBulkIoWebSocket");
        this.port = port;
        this.binary = binary;
        this.alwaysSendSri = alwaysSendSri;
        this.gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE).create();
        this.webSocketProcessorServices = webSocketProcessorServices;
    }
    
    /**
     * Connects to a REDHAWK port and sends data in json or binary 
     */
    @Override
    public void onWebSocketConnect(Session session) {
        super.onWebSocketConnect(session);

        try {
            port.connect(new PortListener() {
                @Override
                public void onReceive(Packet packet) {

                    for (ProcessorObject processor : processorChain) {
                        WebSocketProcessor wsProcessor = webSocketProcessorServices.get(processor.getProcessorName().toLowerCase());
                        if (wsProcessor != null) {
                            packet = wsProcessor.process(packet, processor.getProcessorConfiguration());
                        } else {
                            logger.warning("Web Socket Processor with name: " + processor.getProcessorName() + " does not exist in the Service Registry");
                        }
                    }

                    if (firstSend) {
                        sendSRI(packet);
                        firstSend = false;
                    } else if (alwaysSendSri || packet.isNewSri()) {
                        sendSRI(packet);
                    }

                    if (binary) {
                        sendBinary(packet);
                    } else {
                        sendText(packet);
                    }
                }
            });
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
    
    /**
     * Properly disconnects from port at close.  
     */
    @Override
    public void onWebSocketClose(int closeCode, String message) {
        super.onWebSocketClose(closeCode, message);
        try {
            port.disconnect();
        } catch (PortException e) {
        	logger.log(Level.SEVERE, e.getMessage());
        } catch (InvalidPort e) {
        	logger.log(Level.SEVERE, e.getMessage());
        } finally {

            if (this.isNewDriverInstance()) {
                this.getRedhawk().disconnect();
            }
        }
    }

    @Override
    public void onWebSocketBinary(byte[] data, int offset, int length) {
        logger.severe("onWebSocketBinary() invoked......" + data);
    }
    
    /**
     * Used to handle in incoming text for management of Websocket Processors. 
     */
    @Override
    public void onWebSocketText(String data) {

        logger.fine("onMessage() Invoked......" + data);

        if (data.equals("clearProcessors")) {
            processorChain.clear();
        } else if (data.startsWith("removeProcessor")) {
            String[] split = data.trim().split(":");
            if (split.length == 2) {
                removeProcessor(split[1]);
            }
        } else if (data.startsWith("processors:")) {
            logger.fine("ADDING PROCESSORS");
            data = data.replaceAll("processors\\:", "");
            addOrUpdateProcessors(data);
        } else if (data.equals("getProcessors")) {
            try {
                getRemote().sendString(gson.toJson(processorChain));
            } catch (IOException e) {
                logger.severe(e.getMessage());
            }
        } else if (data.equals("listAvailableProcessors")) {
            try {
            	if(webSocketProcessorServices!=null)
            		logger.fine("Available processors: "+webSocketProcessorServices.keySet());
                getRemote().sendString(gson.toJson(webSocketProcessorServices.keySet()));
            } catch (IOException e) {
            	logger.severe(e.getMessage());
            }
        }

    }
    
    /**
     * Sends SRI as json to remote endpoint(web portion of code)
     * @param packet
     */
    private void sendSRI(Packet packet) {
        try {
            getRemote().sendString(gson.toJson(packet));
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
    private void sendBinary(Packet packet) {
        try {
            ByteBuffer data = ByteBufferUtil.createByteArray(packet.getData());
            if (data != null) {
                getRemote().sendBytes(data);
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
    private void sendText(Packet packet) {
        try {
            getRemote().sendString(gson.toJson(packet.getData()));
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
    private void removeProcessor(String processorName) {
        for (int i = 0; i < processorChain.size(); i++) {
            ProcessorObject p = processorChain.get(i);
            if (processorName.equalsIgnoreCase(p.getProcessorName())) {
                processorChain.remove(i);
            }
        }
    }

    /**
     * Method to add or update processing chain.
     * @param data
     */
    private void addOrUpdateProcessors(String data) {
        try {
            logger.fine("IN addorupdateprocessors()");

            logger.fine("DATA IS: " + data);

            ProcessorObject[] pobjects = gson.fromJson(data, ProcessorObject[].class);
            logger.fine("Created objects from data: "+pobjects.length);
            logger.fine("Current processor chain: "+processorChain);
            logger.fine("WebSocketProcessorServices??? "+webSocketProcessorServices);
            for (ProcessorObject pobj : pobjects) {
                if (!webSocketProcessorServices.containsKey(pobj.getProcessorName().toLowerCase()) && !processorChain.contains(pobj)) {
            		logger.fine("addOrUpdateProcessors: ADDING PROCESSOR");
                    int index = processorChain.indexOf(pobj);
                    if(index!=-1){
                    	logger.fine("Updating processor by that name");
                    	processorChain.add(index, pobj);
                    }else{
                        processorChain.add(pobj);                    	
                    }
                    break;
                }

                for (int i = 0; i < processorChain.size(); i++) {
                    ProcessorObject p = processorChain.get(i);
                    if (pobj.getProcessorName().equalsIgnoreCase(p.getProcessorName())) {
                        processorChain.remove(i);
                        processorChain.add(i, pobj);
                    }
                }
            }
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
    public List<ProcessorObject> getProcessorChain() {
		return processorChain;
	}
}
