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
package redhawk.driver.connectionmanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.junit.BeforeClass;
import org.junit.Test;

import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.connectionmanager.impl.ConnectionInfo;
import redhawk.driver.connectionmanager.impl.EndpointType;
import redhawk.driver.connectionmanager.impl.RedhawkEndpoint;
import redhawk.driver.connectionmanager.impl.RedhawkEventChannelEndpoint;
import redhawk.driver.connectionmanager.impl.RedhawkPortEndpoint;
import redhawk.driver.device.RedhawkDevice;
import redhawk.driver.eventchannel.RedhawkEventChannel;
import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.port.RedhawkPort;
import redhawk.testutils.RedhawkDeviceTestBase;

public class RedhawkConnectionManagerIT extends RedhawkDeviceTestBase{
	private static RedhawkConnectionManager connectionManager;
	
	/*
	 * Set up launches any necessary devices
	 */
	@BeforeClass
	public static void setupConnectionMgr(){
		try {
			connectionManager = driver.getDomain().getConnectionManager();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Issue setting up test "+e.getMessage());
		}
	}
	
	@Test
	public void testAccessCorbaObject(){
		assertNotNull(connectionManager.getCorbaObj());
	}
	
	@Test
	public void testConnectDeviceToApplication(){
		try {
			//Create application
			String appName = "FM_RBDS_demo";
			RedhawkApplication app = driver.getDomain().createApplication(appName, "/waveforms/rh/FM_RBDS_demo/FM_RBDS_demo.sad.xml");
			
			//Create Endpoints
			String connectionId = "myConnection";
			RedhawkEndpoint providesEndpoint = this.createRedhawkEndpoint(app.getPort("tunerFloat_in"), app.getIdentifier(), EndpointType.Application);
			
			RedhawkDevice dev = driver.getDevice("REDHAWK_DEV/Simulator.*/FmRds.*");
			RedhawkEndpoint usesEndpoint = this.createRedhawkEndpoint(dev.getPort("dataFloat_out"), dev.getIdentifier(), EndpointType.Device);
			
			//Number of connections prior to creating one
			Integer connectionNumber = connectionManager.getConnections().size();
			assertEquals("Should be zero connections currently", new Integer(0), connectionNumber);
			connectionManager.connect(usesEndpoint, providesEndpoint, UUID.randomUUID().toString(), connectionId);
		
			//Connection size should have gone up 1
			assertEquals("Should be 1 connection", 1, connectionManager.getConnections().size());
			assertEquals("Number of connection should be "+connectionNumber+1, connectionNumber+1, connectionManager.getConnections().size());		
		
			//Test list connections
			this.listConnections();
			
			//Test disconnect
			this.disconnect();
		} catch (MultipleResourceException | ApplicationCreationException | CORBAException | ResourceNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Unable to run test "+e.getMessage());
		}
	}
	
	@Test
	public void testConnectApplictionToApplication() throws Exception{
		String appName = "FM_RBDS_demo";
		String connMgrTestAppName = "connMgrTest";
		String connectionId = "myConnection";
		RedhawkApplication providesApp = null, usesApp = null;
		
		try {
			//Create Applications
			providesApp = driver.getDomain().createApplication(appName, "/waveforms/rh/FM_RBDS_demo/FM_RBDS_demo.sad.xml");
			usesApp = driver.getDomain().createApplication(connMgrTestAppName, new File("src/test/resources/waveforms/ConnectionManagerTest/ConnectionManagerTest.sad.xml"));
		
			//Create Endpoints 
			RedhawkEndpoint usesEndpoint = this.createRedhawkEndpoint(usesApp.getPort("floatOutput"), usesApp.getIdentifier(), EndpointType.Application);
			RedhawkEndpoint providesEndpoint = this.createRedhawkEndpoint(providesApp.getPort("tunerFloat_in"), providesApp.getIdentifier(), EndpointType.Application);
		
			//Number of connections prior to creating one
			Integer connectionNumber = connectionManager.getConnections().size();
			assertEquals("Should be zero connections currently", new Integer(0), connectionNumber);
			connectionManager.connect(usesEndpoint, providesEndpoint, UUID.randomUUID().toString(), connectionId);
			
			//Connection size should have gone up 1
			assertEquals("Should be 1 connection", 1, connectionManager.getConnections().size());
			assertEquals("Number of connection should be "+connectionNumber+1, connectionNumber+1, connectionManager.getConnections().size());
			
			this.listConnections();
			
			//Test disconnect
			this.disconnect();
		} catch (MultipleResourceException | ApplicationCreationException | CORBAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if(providesApp!=null)
				providesApp.release();
			
			if(usesApp!=null){
				usesApp.release();
				driver.getDomain().getFileManager().removeDirectory("/waveforms/ConnectionManagerTest");
			}
		}
	}
	
	@Test
	public void testConnectApplictionToEventChannel() throws Exception{
		String connMgrTestAppName = "connMgrTest";
		String connectionId = "myConnection";
		String channelName = "ConnMgrTest";
		RedhawkApplication usesApp = null;
		RedhawkEventChannel eventChannel = null;
		
		try {
			//Create Application and Event Channel
			usesApp = driver.getDomain().createApplication(connMgrTestAppName, new File("src/test/resources/waveforms/ConnectionManagerTest/ConnectionManagerTest.sad.xml"));
			driver.getDomain().getEventChannelManager().createEventChannel(channelName);
			eventChannel = driver.getDomain().getEventChannelManager().getEventChannel(channelName);
			
			//Create Endpoints
			RedhawkEndpoint usesEndpoint = this.createRedhawkEndpoint(usesApp.getPort("messageOut"), usesApp.getIdentifier(), EndpointType.Application);
			RedhawkEndpoint providesEndpoint = new RedhawkEventChannelEndpoint(EndpointType.EventChannel, eventChannel.getName(), eventChannel);
		
			//Number of connections prior to creating one
			Integer connectionNumber = connectionManager.getConnections().size();
			assertEquals("Should be zero connections currently", new Integer(0), connectionNumber);
			connectionManager.connect(usesEndpoint, providesEndpoint, UUID.randomUUID().toString(), connectionId);
			
			//Connection size should have gone up 1
			assertEquals("Should be 1 connection", 1, connectionManager.getConnections().size());
			assertEquals("Number of connection should be "+connectionNumber+1, connectionNumber+1, connectionManager.getConnections().size());
			
			this.listConnections();
			
			//Test disconnect
			this.disconnect();
		} catch (MultipleResourceException | ApplicationCreationException | CORBAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if(eventChannel!=null){
				driver.getDomain().getEventChannelManager().releaseEventChannel(eventChannel.getName());
			}
			
			if(usesApp!=null){
				usesApp.release();
				driver.getDomain().getFileManager().removeDirectory("/waveforms/ConnectionManagerTest");
			}
		}
	}
	
	public void listConnections(){
		for(ConnectionInfo info : connectionManager.getConnections()){
			System.out.println(info);
		}
	}
	
	
	public void disconnect(){
		for(ConnectionInfo info : connectionManager.getConnections()){
			connectionManager.disconnect(info.getConnectionRecordId());
		}
		assertTrue("Should no longer be any connections", connectionManager.getConnections().isEmpty());
	}
	
	private RedhawkPortEndpoint createRedhawkEndpoint(RedhawkPort port, String resourceId, EndpointType type){
		return new RedhawkPortEndpoint(type, resourceId, port);
	}
}
