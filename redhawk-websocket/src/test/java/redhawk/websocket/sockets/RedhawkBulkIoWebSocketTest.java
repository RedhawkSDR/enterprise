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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import CF.PortPackage.InvalidPort;
import redhawk.driver.exceptions.PortException;
import redhawk.driver.port.RedhawkPort;
import redhawk.websocket.WebSocketProcessor;

@Ignore("Ignore till updated to work again...")
public class RedhawkBulkIoWebSocketTest {
	private static Map<String, WebSocketProcessor> webSocketProcessorServices = new HashMap<>(); 
	
	private static RedhawkBulkIoWebSocket webSocket; 
	
	@BeforeClass
	public static void beforeClass(){ 
		WebSocketProcessor processor = Mockito.mock(WebSocketProcessor.class);
		Mockito.when(processor.getName()).thenReturn("myProcessorDecimator");
		webSocketProcessorServices.put("myprocessordecimator", processor);
		webSocket = new RedhawkBulkIoWebSocket(false, null, null, false, true, webSocketProcessorServices, "");
	}
	
	@Before
	public void setup(){
		assertTrue(webSocket.getProcessorChain().isEmpty());
		webSocket.onWebSocketText("processors: [{"
				+ "\"processorName\" : \"myProcessorDecimator\","
				+ "\"processorConfiguration\" : {}}]");	
		assertFalse(webSocket.getProcessorChain().isEmpty());
	}
	
	@Test
	public void testAddProcessingChain(){
		assertEquals("myProcessorDecimator", webSocket.getProcessorChain().get(0).getProcessorName());
	}
	
	@Test
	public void testRemoveProcessor(){
		//Remove processor 
		webSocket.onWebSocketText("removeProcessor:myProcessorDecimator");
		assertTrue(webSocket.getProcessorChain().isEmpty());
	}
	
	@Test
	public void testUpdatingAProcessor(){
		//Now I want to update that 
		webSocket.onWebSocketText("processors: [{"
				+ "\"processorName\" : \"myProcessorDecimator\","
				+ "\"processorConfiguration\" : {"
				+ "\"param1\":1, "
				+ "\"param2\": \"hello is it me your looking for\"}}]");
		assertFalse(webSocket.getProcessorChain().isEmpty());
		
		//Should only be an update not an additional one
		assertEquals(webSocket.getProcessorChain().size(), 1);
		Map<String, String> expectedConfig = new HashMap<>();
		expectedConfig.put("param1", "1");
		expectedConfig.put("param2", "hello is it me your looking for");
		assertEquals(expectedConfig, webSocket.getProcessorChain().get(0).getProcessorConfiguration());
	}
	
	@Test
	public void letsGetMoreCodeCoverage() throws InvalidPort, PortException{
		RedhawkPort port = Mockito.mock(RedhawkPort.class);
		Mockito.doThrow(PortException.class).when(port).disconnect();
		
		RedhawkBulkIoWebSocket socket = new RedhawkBulkIoWebSocket(false, null, port, false, false, null, null);
		socket.onWebSocketClose(1, "because");
		
		Mockito.doThrow(InvalidPort.class).when(port).disconnect();
		socket.onWebSocketClose(1, "because");
		
		socket.onWebSocketBinary(null, 0, 1);
		
		RedhawkBulkIoWebSocket spySocket = Mockito.spy(socket);
		Mockito.doThrow(IOException.class).when(spySocket).getRemote();
		spySocket.onWebSocketText("getProcessors");
		
		spySocket.onWebSocketText("listAvailableProcessors");
	}
	
	@After
	public void cleanup(){
		webSocket.onWebSocketText("clearProcessors");
	}
	
}
