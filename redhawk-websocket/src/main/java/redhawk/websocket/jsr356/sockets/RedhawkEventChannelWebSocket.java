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
import java.util.Map;
import java.util.logging.Logger;

import javax.websocket.Session;

import com.google.gson.Gson;

import redhawk.driver.Redhawk;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.eventchannel.RedhawkEventChannel;
import redhawk.driver.eventchannel.listeners.MessageListener;
import redhawk.driver.eventchannel.listeners.PropertyChange;
import redhawk.driver.eventchannel.listeners.PropertyChangeListener;
import redhawk.driver.exceptions.EventChannelException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.websocket.jsr356.model.MessageType;
import redhawk.websocket.jsr356.utils.EventChannelConverter;

public class RedhawkEventChannelWebSocket extends RedhawkEventAdminWebSocket {

    private static Logger logger = Logger.getLogger(RedhawkEventChannelWebSocket.class.getName());
    private Gson gson;
    private MessageType messageType;
    private RedhawkEventChannel eventChannel;
    
    public RedhawkEventChannelWebSocket(boolean newDriverInstance, Redhawk redhawkConnection, RedhawkDomainManager domainManager, String eventChannelName, MessageType message, String path) {
        super(newDriverInstance, redhawkConnection, path);
        logger.info("Constructing new instance of RedhawkEventChannelWebSocket");
        
        try {
			eventChannel = domainManager.getEventChannelManager().getEventChannel(eventChannelName);
		} catch (MultipleResourceException | ResourceNotFoundException e) {
			e.printStackTrace();
		}
        
        this.gson = new Gson();
        this.messageType = message;
        logger.fine("MessageTpye value: "+this.messageType);
    }

    /**
     * Subscribes to event channel and sends a message to session corresponding to incoming messageType. 
     */
    @Override
    public void onWebSocketConnect(final Session session) {
        super.onWebSocketConnect(session);

        try {
        	switch(messageType){
        	case PROPERTY_CHANGE:
        		eventChannel.subscribe(new PropertyChangeListener() {
                    @Override
                    public void onMessage(PropertyChange message) {
                        try {
                        	logger.fine("Got PropertyChange Message "+eventChannel.getName());
                            session.getBasicRemote().sendText(gson.toJson(EventChannelConverter.convertToPropertyChangeModel(message)));
                        } catch (IOException e) {
                            logger.severe(e.getMessage());
                        }
                    }
                });
                break;
        	case STANDARD_EVENT:
//        		eventChannel.subscribe(new ObjectEventChannelListener() {
//                    @Override
//                    public void onMessage(Object message) {
//                        try {
//                        	logger.debug("Got StandardEvent Message "+eventChannel.getName());
//                        	if(message instanceof DomainManagementModel){
//                                session.getBasicRemote().sendText(gson.toJson(message));                        		
//                        	}else{
//                        		logger.error("Unknown message type!!!");
//                        	}
//                        } catch (IOException e) {
//                            logger.error(e);
//                        }
//                    }
//                });
                break;
        	default:
        		eventChannel.subscribe(new MessageListener() {
                    @Override
                    public void onMessage(Map<String, Object> message) {
                        try {
                        	logger.fine("Got non PropertyChange Message "+message);
                            session.getBasicRemote().sendText(gson.toJson(message));
                        } catch (IOException e) {
                            logger.severe(e.getMessage());
                        }
                    }
                });
                break;
        	}
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    /**
     * Properly disconnects from REDHAWK DomainManager. 
     */
    @Override
    public void onWebSocketClose(int closeCode, String message) {
        super.onWebSocketClose(closeCode, message);

        try {
        	eventChannel.unsubscribe();
        } catch (EventChannelException e) {
            logger.severe("Exception unsubscribing from event channel");
        } finally {
            if (this.isNewDriverInstance()) {
                this.getRedhawk().disconnect();
            }
        }
    }
}
