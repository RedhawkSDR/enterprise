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
package redhawk.websocket.sockets;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.jetty.websocket.api.Session;

import com.google.gson.Gson;

import redhawk.driver.Redhawk;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.eventchannel.RedhawkEventChannel;
import redhawk.driver.eventchannel.listeners.MessageListener;
import redhawk.driver.eventchannel.listeners.PropertyChange;
import redhawk.driver.eventchannel.listeners.PropertyChangeListener;
import redhawk.driver.exceptions.EventChannelException;
import redhawk.websocket.eventchannel.listeners.ObjectEventChannelListener;
import redhawk.websocket.model.DomainManagementModel;
import redhawk.websocket.model.MessageType;
import redhawk.websocket.utils.EventChannelConverter;

public class RedhawkEventChannelWebSocket extends RedhawkEventAdminWebSocket {

    private static Logger logger = Logger.getLogger(RedhawkEventChannelWebSocket.class.getName());
    private String eventChannelName;
    private Gson gson;
    private RedhawkDomainManager domainManager;
    private MessageType messageType;
    private RedhawkEventChannel eventChannel;
    
    public RedhawkEventChannelWebSocket(boolean newDriverInstance, Redhawk redhawkConnection, RedhawkDomainManager domainManager, String eventChannelName, MessageType message, String path) {
        super(newDriverInstance, redhawkConnection, path);
        logger.info("Constructing new instance of RedhawkEventChannelWebSocket");
        this.eventChannelName = eventChannelName;
        this.gson = new Gson();
        this.domainManager = domainManager;
        this.messageType = message;
        logger.fine("MessageTpye value: "+this.messageType);
    }

    /**
     * Subscribes to event channel and sends a message to session corresponding to incoming messageType. 
     */
    @Override
    public void onWebSocketConnect(Session session) {
        super.onWebSocketConnect(session);

        try {
        	eventChannel = domainManager.getEventChannelManager().getEventChannel(eventChannelName);

        	switch(messageType){
        	case PROPERTY_CHANGE:
                eventChannel.subscribe(new PropertyChangeListener() {
                    @Override
                    public void onMessage(PropertyChange message) {
                        try {
                        	logger.fine("Got PropertyChange Message "+eventChannelName);
                            session.getRemote().sendString(gson.toJson(EventChannelConverter.convertToPropertyChangeModel(message)));
                        } catch (IOException e) {
                            logger.severe(e.getMessage());
                        }
                    }
                });
                break;
        	case STANDARD_EVENT:
        		eventChannel.subscribe(new ObjectEventChannelListener() {
                    @Override
                    public void onMessage(Object message) {
                        try {
                        	logger.fine("Got StandardEvent Message "+eventChannelName);
                        	if(message instanceof DomainManagementModel){
                                session.getRemote().sendString(gson.toJson(message));                        		
                        	}else{
                        		logger.severe("Unknown message type!!!");
                        	}
                        } catch (IOException e) {
                        	logger.severe(e.getMessage());
                        }
                    }
                });
                break;
        	default:
        		eventChannel.subscribe(new MessageListener() {
                    @Override
                    public void onMessage(Map<String, Object> message) {
                        try {
                        	logger.fine("Got non PropertyChange Message "+message);
                            session.getRemote().sendString(gson.toJson(message));
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
        	if(eventChannel != null) {
        		eventChannel.unsubscribe();
        	}
		} catch (EventChannelException e) {
			logger.severe(e.getMessage());
		}
    }
}
