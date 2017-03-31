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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import redhawk.driver.Redhawk;
import redhawk.driver.RedhawkDriver;
import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.component.RedhawkComponent;
import redhawk.driver.device.RedhawkDevice;
import redhawk.driver.devicemanager.RedhawkDeviceManager;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.port.RedhawkPort;
import redhawk.websocket.sockets.RedhawkBulkIoWebSocket;
import redhawk.websocket.sockets.RedhawkEventChannelWebSocket;

@Ignore("Clean this up so it actually works...")
public class RedhawkWebSocketCreatorTest {
	private static RedhawkWebSocketCreator creator; 
	
	private static Map<String, Redhawk> redhawkDrivers = new HashMap<>(); 
	
	private static Redhawk redhawk; 
	
	private static RedhawkDomainManager rhDomainManager; 
	
	private static RedhawkApplication rhApplication; 
	
	private static RedhawkPort rhPort; 
	
	private static RedhawkComponent rhComponent; 
	
	private static RedhawkDeviceManager rhDeviceManager; 
	
	private static RedhawkDevice rhDevice; 
	
	
	@Before
	public void setupClass() throws Exception{
		redhawk = Mockito.mock(RedhawkDriver.class);
		rhDomainManager = Mockito.mock(RedhawkDomainManager.class);
		rhApplication = Mockito.mock(RedhawkApplication.class);
		rhPort = Mockito.mock(RedhawkPort.class); 
		rhComponent = Mockito.mock(RedhawkComponent.class);
		rhDeviceManager = Mockito.mock(RedhawkDeviceManager.class);
		rhDevice = Mockito.mock(RedhawkDevice.class); 
		
		Set<String> eventChannels = new HashSet<>();
		eventChannels.add("IDM Channel");
		eventChannels.add("ODM Channel");
		
		
		Mockito.when(redhawk.getDomain("aDomain")).thenReturn(rhDomainManager);
//		Mockito.when(redhawk.getAlias()).thenReturn("anAlias");
		Mockito.when(rhDomainManager.getApplicationByName("applicationName")).thenReturn(rhApplication);
		Mockito.when(rhApplication.getPort("aExternalPort")).thenReturn(rhPort);
		Mockito.when(rhApplication.getComponentByName("componentName")).thenReturn(rhComponent);
		Mockito.when(rhComponent.getPort("aPort")).thenReturn(rhPort);
		//Mockito.when(rhDomainManager.getEventChannelManager().eventChannels().keySet()).thenReturn(eventChannels);
		Mockito.when(rhDomainManager.getDeviceManagerByName("aDeviceManagerName")).thenReturn(rhDeviceManager);
		Mockito.when(rhDeviceManager.getDeviceByName("aDeviceName")).thenReturn(rhDevice);
		Mockito.when(rhDevice.getPort("aDevicePort")).thenReturn(rhPort);
		
		redhawkDrivers.put("redhawk-connection", redhawk);
		
		creator = new RedhawkWebSocketCreator(null, null, null, redhawkDrivers);
	}
	
	@Test
	public void testProperHandlingOfIncomingJsonRequest(){
		String jsonPortRequest = "/redhawk/localhost:2809/domains/REDHAWK_DEV/applications/websocket-integration-test.*/components/SigGen.*/ports/out.json";
		
		creator.manageRequestPath(jsonPortRequest, null);
		
		assertEquals("This endpoint should come out as valid", true, creator.isLatestPathValid());
		
		assertEquals("localhost:2809", creator.getNameServer());
		
		assertEquals("REDHAWK_DEV", creator.getDomainName());
		
		assertEquals(false, creator.getBinary());
	}
	
	@Test
	public void testProperHandlingOfIncomingBinaryRequest(){
		String jsonPortRequest = "/redhawk/localhost:2809/domains/REDHAWK_DEV/applications/websocket-integration-test.*/components/SigGen.*/ports/out";
		
		creator.manageRequestPath(jsonPortRequest, null);
		
		assertEquals("This endpoint should come out as valid", true, creator.isLatestPathValid());
		
		assertEquals("localhost:2809", creator.getNameServer());
		
		assertEquals("REDHAWK_DEV", creator.getDomainName());
		
		assertEquals(true, creator.getBinary());
	}
	
	@Test
	public void testProperHandlingOfIncomingEventChannelRequest(){
		String jsonPortRequest = "/redhawk/localhost:2809/domains/REDHAWK_DEV/eventchannels/ODM_Channel?messageType=propertyChanges ";
		
		creator.manageRequestPath(jsonPortRequest, null);
		
		assertEquals("This endpoint should come out as valid", true, creator.isLatestPathValid());
		
		assertEquals("localhost:2809", creator.getNameServer());
		
		assertEquals("REDHAWK_DEV", creator.getDomainName());		
	}
	
	@Test
	public void testImproperPath(){
		String jsonPortRequest = "/tedtalk/localhost:2809/domains/REDHAWK_DEV/eventchannels/ODM_Channel?messageType=propertyChanges ";
		
		creator.manageRequestPath(jsonPortRequest, null);
		
		assertEquals(false, creator.isLatestPathValid());
	}
	
	@Test
	public void testImproperPathP2(){
		String jsonPortRequest = "/tedtalk/localhost:2809/domains/REDHAWK_DEV/eventchannels/ODM_Channel?messageType=propertyChanges ";
		ServletUpgradeRequest request = Mockito.mock(ServletUpgradeRequest.class);
		ServletUpgradeResponse response = Mockito.mock(ServletUpgradeResponse.class); 
		
		Mockito.when(request.getRequestPath()).thenReturn(jsonPortRequest);
		Mockito.when(request.getQueryString()).thenReturn(null);
		
		assertNull(creator.createWebSocket(request, response));
	}
	
	@Test
	public void testCreateWebSocket() throws Exception{
		String testUri = "/redhawk/anAlias/domains/aDomain/applications/applicationName/components/componentName/ports/aExternalPort";
		ServletUpgradeRequest request = Mockito.mock(ServletUpgradeRequest.class);
		ServletUpgradeResponse response = Mockito.mock(ServletUpgradeResponse.class); 
		
		Mockito.when(request.getRequestPath()).thenReturn(testUri);
		Mockito.when(request.getQueryString()).thenReturn(null);
		
		RedhawkBulkIoWebSocket bulkIoWebSocket = (RedhawkBulkIoWebSocket) creator.createWebSocket(request, response);
		assertNotNull("This should return a RedhawkBulkIoWebSocket object", bulkIoWebSocket);	
		assertEquals(RedhawkBulkIoWebSocket.class, bulkIoWebSocket.getClass());
	}
	
	@Test
	public void testCreateWebSocket2(){
		String testUri = "/redhawk/anAlias/domains/aDomain/eventchannels/ODM_Channel";
		
		ServletUpgradeRequest request = Mockito.mock(ServletUpgradeRequest.class);
		ServletUpgradeResponse response = Mockito.mock(ServletUpgradeResponse.class); 
		
		Mockito.when(request.getRequestPath()).thenReturn(testUri);
		Mockito.when(request.getQueryString()).thenReturn(null);
		
		RedhawkEventChannelWebSocket eventChannelWs = (RedhawkEventChannelWebSocket) creator.createWebSocket(request, response);
		assertNotNull("This should return a RedhawkEventChannelWebSocket object", eventChannelWs);	
		assertEquals(RedhawkEventChannelWebSocket.class, eventChannelWs.getClass());
	}
	
	@Test
	public void testCreateWebSocket3(){
		String testUri = "/redhawk/anAlias/domains/aDomain/devicemanagers/aDeviceManagerName/devices/aDeviceName/ports/aDevicePort";
		
		ServletUpgradeRequest request = Mockito.mock(ServletUpgradeRequest.class);
		ServletUpgradeResponse response = Mockito.mock(ServletUpgradeResponse.class); 
		
		Mockito.when(request.getRequestPath()).thenReturn(testUri);
		Mockito.when(request.getQueryString()).thenReturn(null);
		
		RedhawkBulkIoWebSocket bulkIoWebSocket = (RedhawkBulkIoWebSocket) creator.createWebSocket(request, response);
		assertNotNull("This should return a RedhawkBulkIoWebSocket object", bulkIoWebSocket);	
		assertEquals(RedhawkBulkIoWebSocket.class, bulkIoWebSocket.getClass());
	}
	
	@Test
	public void testCreateWebSocket4() throws Exception{
		String testUri = "/redhawk/anAlias/domains/aDomain/applications/applicationName/ports/aExternalPort";
		ServletUpgradeRequest request = Mockito.mock(ServletUpgradeRequest.class);
		ServletUpgradeResponse response = Mockito.mock(ServletUpgradeResponse.class); 
		
		Mockito.when(request.getRequestPath()).thenReturn(testUri);
		Mockito.when(request.getQueryString()).thenReturn(null);
		
		RedhawkBulkIoWebSocket bulkIoWebSocket = (RedhawkBulkIoWebSocket) creator.createWebSocket(request, response);
		assertNotNull("This should return a RedhawkBulkIoWebSocket object", bulkIoWebSocket);	
		assertEquals(RedhawkBulkIoWebSocket.class, bulkIoWebSocket.getClass());
	}
	
	@Test
	public void testNPE() throws Exception{
		String testUri = "/redhawk/anAlias/domains/aDomain/eventchannels/ODM_Channel";
		Mockito.when(redhawk.getDomain("aDomain")).thenThrow(NullPointerException.class);
		
		ServletUpgradeRequest request = Mockito.mock(ServletUpgradeRequest.class);
		ServletUpgradeResponse response = Mockito.mock(ServletUpgradeResponse.class); 
		
		Mockito.when(request.getRequestPath()).thenReturn(testUri);
		Mockito.when(request.getQueryString()).thenReturn(null);
		
		assertNull(creator.createWebSocket(request, response));
	}
	
	@Test
	public void testConnectionException() throws Exception{
		String testUri = "/redhawk/anAlias/domains/aDomain/eventchannels/ODM_Channel";
		Mockito.when(redhawk.getDomain("aDomain")).thenThrow(ConnectionException.class);
		
		ServletUpgradeRequest request = Mockito.mock(ServletUpgradeRequest.class);
		ServletUpgradeResponse response = Mockito.mock(ServletUpgradeResponse.class); 
		
		Mockito.when(request.getRequestPath()).thenReturn(testUri);
		Mockito.when(request.getQueryString()).thenReturn(null);
		
		assertNull(creator.createWebSocket(request, response));
	}
	
	@Test
	public void testException() throws Exception{
		String testUri = "/redhawk/anAlias/domains/aDomain/eventchannels/ODM_Channel";
		Mockito.when(redhawk.getDomain("aDomain")).thenThrow(Exception.class);
		
		ServletUpgradeRequest request = Mockito.mock(ServletUpgradeRequest.class);
		ServletUpgradeResponse response = Mockito.mock(ServletUpgradeResponse.class); 
		
		Mockito.when(request.getRequestPath()).thenReturn(testUri);
		Mockito.when(request.getQueryString()).thenReturn(null);
		
		assertNull(creator.createWebSocket(request, response));
	}
}
