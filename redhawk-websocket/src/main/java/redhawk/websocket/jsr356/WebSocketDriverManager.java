package redhawk.websocket.jsr356;

import java.util.concurrent.ConcurrentHashMap;

import redhawk.driver.Redhawk;
import redhawk.websocket.jsr356.processor.WebSocketProcessor;
import redhawk.websocket.jsr356.sockets.RedhawkBulkIoWebSocket;

public class WebSocketDriverManager {

	public static ConcurrentHashMap<String, Redhawk> redhawkDrivers = new ConcurrentHashMap<>();
	
	public static ConcurrentHashMap<String, RedhawkBulkIoWebSocket> portConnections = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<String, WebSocketProcessor> webSocketProcessors = new ConcurrentHashMap<>();
	
	
}
