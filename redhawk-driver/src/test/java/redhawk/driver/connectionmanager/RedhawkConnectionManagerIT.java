package redhawk.driver.connectionmanager;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.UUID;

import org.junit.BeforeClass;
import org.junit.Test;

import redhawk.driver.RedhawkDriver;
import redhawk.driver.base.impl.EndpointType;
import redhawk.driver.base.impl.RedhawkEndpoint;
import redhawk.driver.connectionmanager.impl.ConnectionInfo;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.port.RedhawkPort;

public class RedhawkConnectionManagerIT {
	private static RedhawkConnectionManager connectionManager; 
	
	private static RedhawkDriver driver; 
	
	@BeforeClass
	public static void setupConnectionMgr(){
		try {
			driver = new RedhawkDriver();
			connectionManager = driver.getDomain().getConnectionManager();
		} catch (MultipleResourceException | CORBAException e) {
			fail("Issue setting up test "+e.getMessage());
		}
	}
	
	@Test
	public void testAccessCorbaObject(){
		assertNotNull(connectionManager.getCorbaObj());
	}
	
	@Test
	public void testListConnections(){
		for(ConnectionInfo info : connectionManager.getConnections()){
			System.out.println(info);
		}
	}
	
	@Test
	public void testDisconnect(){
		for(ConnectionInfo info : connectionManager.getConnections()){
			connectionManager.disconnect(info.getConnectionRecordId());
		}
		assertTrue("Should no longer be any connections", connectionManager.getConnections().isEmpty());
	}
	
	@Test
	public void sandbox(){
		try {
			RedhawkPort usesPort = driver.getDevice("REDHAWK_DEV/Simulator.*/FmRds.*").getPort("dataFloat_out");
			RedhawkPort providesPort = driver.getApplication("REDHAWK_DEV/rh.FM.*").getPort("tunerFloat_in");
			
			RedhawkEndpoint usesEndpoint = new RedhawkEndpoint(EndpointType.Device, driver.getDevice("REDHAWK_DEV/Simulator.*/FmRds.*").getIdentifier(), usesPort);
			RedhawkEndpoint providesEndpoint = new RedhawkEndpoint(EndpointType.Application, driver.getApplication("REDHAWK_DEV/rh.FM.*").getIdentifier(), providesPort);
			System.out.println(providesPort);
			System.out.println(providesPort.getType());
			System.out.println(providesPort.getRepId());			
			System.out.println(usesPort);
			System.out.println(usesPort.getType());
			System.out.println(usesPort.getRepId());			
			driver.getApplication("REDHAWK_DEV/rh.FM.*").getAssembly().getExternalports().getPorts();
			connectionManager.connect(usesEndpoint, providesEndpoint, UUID.randomUUID().toString(), UUID.randomUUID().toString());
		} catch (ResourceNotFoundException | MultipleResourceException | IOException | CORBAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
