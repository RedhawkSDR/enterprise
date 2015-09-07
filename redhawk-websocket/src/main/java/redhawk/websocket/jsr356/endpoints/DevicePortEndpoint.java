package redhawk.websocket.jsr356.endpoints;

import java.util.logging.Logger;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import redhawk.driver.Redhawk;
import redhawk.driver.RedhawkDriver;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.port.RedhawkPort;
import redhawk.websocket.jsr356.WebSocketDriverManager;
import redhawk.websocket.jsr356.sockets.RedhawkBulkIoWebSocket;


@ServerEndpoint("/{nameServer}/domains/{domainName}/devicemanagers/{deviceManagerName}/devices/{deviceName}/ports/{portName}")
public class DevicePortEndpoint {

    private static Logger logger = Logger.getLogger(DevicePortEndpoint.class.getName());

	@OnOpen
	public void onOpen(Session session, EndpointConfig c, @PathParam("nameServer") String nameServer, @PathParam("domainName") String domainName, 
		@PathParam("deviceManagerName") String deviceManagerName, @PathParam("deviceName") String deviceName, @PathParam("portName") String portName) {
        logger.info("Connected ... " + session.getId());
        logger.info("session.getRequestURI() : " + session.getRequestURI());
        logger.info("session.getQueryString() : " + session.getQueryString());
        session.getUserProperties().put("binary", session.getRequestURI().toString().endsWith(".json"));
        session.getUserProperties().put("alwaysSendSri", (session.getQueryString()+"").contains("sriFrequency=always"));

        Redhawk redhawk = WebSocketDriverManager.redhawkDrivers.get(nameServer);
        if (redhawk == null) {
            String[] hostAndPort = nameServer.split(":");
            if(hostAndPort.length == 2) {
            	redhawk = new RedhawkDriver(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
            } else if(hostAndPort.length == 1) {
            	redhawk = new RedhawkDriver(hostAndPort[0]);
            } else {
            	//throw exception
            }
            
            WebSocketDriverManager.redhawkDrivers.put(nameServer, redhawk);
        }
        
        String redhawkConnection = redhawk.getHostName()+":"+redhawk.getPort();
        String portUrl = domainName+"/"+deviceManagerName+"/"+deviceName+"/"+portName;
        RedhawkPort port;
		try {
			port = redhawk.getPort(portUrl);
			RedhawkBulkIoWebSocket websocket = WebSocketDriverManager.portConnections.get(redhawkConnection+":"+portUrl);
			
			if(websocket != null) {
				websocket.addSession(session);
			} else {
				websocket = new RedhawkBulkIoWebSocket(port);
				WebSocketDriverManager.portConnections.put(redhawkConnection+":"+portUrl, websocket);
			}
			
			session.getUserProperties().put("redhawkUri", redhawkConnection+":"+portUrl);
		} catch (ResourceNotFoundException | MultipleResourceException | CORBAException e) {
			e.printStackTrace();
		}
        
	}
	
	
	@OnClose
	public void onClose(Session session, CloseReason cr) {
		logger.info("CLOSING SESSION: " + session.getId());
		String rhUri = (String) session.getUserProperties().get("redhawkUri");
		RedhawkBulkIoWebSocket websocket = WebSocketDriverManager.portConnections.get(rhUri);
		websocket.removeSession(session);
	}
	
}
