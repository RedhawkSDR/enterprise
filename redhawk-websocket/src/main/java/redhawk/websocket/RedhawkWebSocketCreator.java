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
package redhawk.websocket;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.osgi.framework.ServiceReference;

import redhawk.driver.Redhawk;
import redhawk.driver.RedhawkDriver;
import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.port.RedhawkPort;
import redhawk.websocket.model.MessageType;
import redhawk.websocket.sockets.RedhawkBulkIoWebSocket;
import redhawk.websocket.sockets.RedhawkEventChannelWebSocket;

/**
 * Class creates a WebSocket endpoint based on the params passed in on the servlet level by the
 * user. 
 * 
 * Sample endpoint:
 * 	ws://localhost/redhawk/localhost:2809/domains/REDHAWK_DEV/applications/websocket.*\/ports/out
 */
public class RedhawkWebSocketCreator implements WebSocketCreator {

    private static Logger logger = Logger.getLogger(RedhawkWebSocketCreator.class.getName());

    private List<ServiceReference<Redhawk>> redhawkDriverServices;
    private List<WebSocketProcessor> webSocketProcessorServices;
    private Map<String, WebSocketProcessor> webSocketProcessors;
    private Map<String, Redhawk> redhawkDrivers;

    /**
     * Whether to produce binary data or not
     */
    private Boolean binary;
    
    /**
     * Name of the domain taken from the path in the request uri
     */
    private String domainName;
    
    /**
     * Name of the nameServer
     */
    private String nameServer;
    
    /**
     * Returns the path in the request
     */
    private String path; 
    
	/**
     * Return the endpoint type used(i.e. applications, devicemanager, eventchannels)
     */
    private String rhEndpointType; 
    
    private Boolean latestPathVald; 
    
    private Boolean alwaysSendSri;
    
    private String[] pathArray;
    
    public RedhawkWebSocketCreator(List<ServiceReference<Redhawk>> redhawkDriverServices, List<WebSocketProcessor> webSocketProcessorServices, Map<String, WebSocketProcessor> webSocketProcessors, Map<String, Redhawk> redhawkDrivers) {
        this.redhawkDriverServices = redhawkDriverServices;
        this.webSocketProcessorServices = webSocketProcessorServices;
        this.webSocketProcessors = webSocketProcessors;
        this.redhawkDrivers = redhawkDrivers;
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
    	logger.fine("Attempting to Connect to the web socket");

        if (!this.manageRequestPath(req.getRequestPath(), req.getQueryString())) {
            logger.warning("Path is not valid: " + path);
            return null;
        }

        Redhawk redhawkConnection = null;

        logger.fine("NameServer: " + nameServer + " DomainName: " + domainName);

        boolean newDriverInstance = false;
        
        if (redhawkDrivers.get(nameServer) != null) {
        	redhawkConnection = redhawkDrivers.get(nameServer);
        } else {
            String[] hostAndPort = nameServer.split(":");
            redhawkConnection = new RedhawkDriver(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
            newDriverInstance = true;
        }

        RedhawkPort port = null;

        try {
            switch (pathArray[3]) {
                case "devicemanagers":
                    port = redhawkConnection.getDomain(domainName).getDeviceManagerByName(pathArray[4]).getDeviceByName(pathArray[6]).getPort(pathArray[8]);
                    return new RedhawkBulkIoWebSocket(newDriverInstance, redhawkConnection, port, binary, alwaysSendSri, webSocketProcessors, path);
                case "applications":
                    RedhawkApplication app = redhawkConnection.getDomain(domainName).getApplicationByName(pathArray[4]);
                    if (pathArray[5].equals("components")) {
                        port = app.getComponentByName(pathArray[6]).getPort(pathArray[8]);
                    } else if (pathArray[5].equals("ports")) {                        
                        port = app.getExternalPort(pathArray[6]);
                    }
                    return new RedhawkBulkIoWebSocket(newDriverInstance, redhawkConnection, port, binary, alwaysSendSri, webSocketProcessors, path);
                case "eventchannels":
                    for (String eventChannel : redhawkConnection.getDomain(domainName).getEventChannelManager().eventChannels().keySet()) {
                        logger.fine("EVENT CHANNEL: " + eventChannel);
                        if (eventChannel.equalsIgnoreCase(pathArray[4])) {
                            MessageType type = MessageType.UNKNOWN;
                        	//TODO There's a cleaner way to do this just ripping the string once
                            boolean propertyChanges = (req.getQueryString() + "").contains("messageType=propertyChanges");
                            boolean standardEvent = (req.getQueryString()+"").contains("messageType=standardEvent");
                            if(propertyChanges){
                            	type = MessageType.PROPERTY_CHANGE;
                            }else if(standardEvent){
                            	type = MessageType.STANDARD_EVENT;
                            }
                            logger.fine("MessageType is: "+type);
                            return new RedhawkEventChannelWebSocket(newDriverInstance, redhawkConnection, redhawkConnection.getDomain(domainName), eventChannel, type, path);
                        }
                    }
                    break;
            }
        } catch (NullPointerException npe) {
            logger.severe("Could not locate REDHAWK resource at: " + path);
            return null;
        } catch (ConnectionException e) {
            logger.log(Level.SEVERE, "Error connecting to REDHAWK", e);
            return null;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "A general exception has occured.", e);
            return null;
        }

        return null;
    }
    
    public Boolean manageRequestPath(String requestPath, String queryString){
        String path = requestPath.replaceFirst("/", "");
        
        binary=true;
        if (path.startsWith("redhawk/")) {
            path = path.replaceFirst("redhawk/", "");
        }
        logger.fine("PATH: " + path);
        logger.fine("Request Query String: "+queryString);

        alwaysSendSri = (queryString + "").contains("sriFrequency=always");

        if (requestPath.toString().endsWith(".json")) {
            binary = false;
            path = path.substring(0, path.indexOf(".json"));
        }

        pathArray = path.split("/");

        latestPathVald = false;
        if (pathArray.length == 9) {
        	latestPathVald = true;
        }
        if ((pathArray.length == 5) && pathArray[3].equals("eventchannels")) {
        	latestPathVald = true;
        }
        if ((pathArray.length == 7) && (pathArray[3].equals("applications")) && (pathArray[5].equals("ports"))) {
        	latestPathVald = true;
        }

        if (!latestPathVald) {
            logger.warning("Path is not valid: " + path);
            return false;
        }

        nameServer = pathArray[0];
        
        domainName = pathArray[2];
     
        return true;
    }

	public Boolean getBinary() {
		return binary;
	}

	public String getDomainName() {
		return domainName;
	}

	public String getNameServer() {
		return nameServer;
	}

	public String getPath() {
		return path;
	}

	public String getRhEndpointType() {
		return rhEndpointType;
	}
	
	public Boolean isLatestPathValid(){
		return this.latestPathVald;
	}
}
