package redhawk.websocket;

import java.util.Map;

import redhawk.driver.bulkio.Packet;

public interface WebSocketProcessor {

	Packet process(Packet packet, Map<String, String> configuration);
	String getName();
	
}