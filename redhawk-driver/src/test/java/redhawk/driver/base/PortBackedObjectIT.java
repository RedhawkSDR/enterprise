package redhawk.driver.base;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.component.RedhawkComponent;
import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.PortException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.port.RedhawkPort;
import redhawk.testutils.RedhawkTestBase;

public class PortBackedObjectIT extends RedhawkTestBase{
	private static String appName1 = "myApp", appName2 = "myApp2", appName3 = "myApp3";
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	static RedhawkApplication basicComponentDemo, externalPortExample, externalPortExample2; 
	
	@BeforeClass
	public static void setupPortBasedObjectIT() {
		try {
			basicComponentDemo = driver.getDomain().createApplication(appName1, "/waveforms/rh/basic_components_demo/basic_components_demo.sad.xml");
			externalPortExample = driver.getDomain().createApplication(appName2,new File("src/test/resources/waveforms/ExternalPropPortExample/ExternalPropPortExample.sad.xml"));		
			externalPortExample2 = driver.getDomain().createApplication(appName3, "/waveforms/ExternalPropPortExample/ExternalPropPortExample.sad.xml");
		} catch (MultipleResourceException | ApplicationCreationException | CORBAException e) {
			fail("Unable to setup test "+e.getMessage());
		}
	}
	
	/*
	 * Test using the connection method between two components 
	 */
	@Test
	public void testComponentToComponentConnection() {
		try {
			RedhawkComponent comp = basicComponentDemo.getComponentByName("SigGen_sine.*");
			RedhawkComponent connectComp = basicComponentDemo.getComponentByName("HardLimit.*");

			// Remove any existing connections
			RedhawkPort port = comp.getPort("dataFloat_out");

			// Disconnect port if connected
			for (String id : port.getConnectionIds()) {
				port.disconnect(id);
			}

			assertTrue(port.getConnectionIds().isEmpty());

			// Connect the port again
			comp.connect(connectComp);

			// Components connect
			assertTrue(!port.getConnectionIds().isEmpty());

			// Try connecting the other way
			for (String id : port.getConnectionIds()) {
				port.disconnect(id);
			}

			// Connect the port again
			connectComp.connect(comp);

			// Components connect
			assertTrue(!port.getConnectionIds().isEmpty());
		} catch (MultipleResourceException | ResourceNotFoundException | PortException e) {
			fail("Unable to run tests " + e.getMessage());
		}		
	}
	
	@Test
	public void testApplicationToComponentConnection() throws PortException {
		try {
			String connectionId = "newConnection";
			
			RedhawkComponent comp = basicComponentDemo.getComponentByName("SigGen_sine.*");
			
			//Connect SigGen_sine to Data Converter in the other application
			externalPortExample.connect(comp, connectionId, "dataConverterFloat_in", "dataFloat_out");
		
			//Look at connection Ids make sure connection name is present
			List<String> connectionIds = comp.getPort("dataFloat_out").getConnectionIds();
		
			assertTrue(connectionIds.contains(connectionId));
		} catch (MultipleResourceException | ResourceNotFoundException e) {
			fail("Unable to run tests " + e.getMessage());
		}
	}
	
	@Test
	public void testApplicationToApplicationConnection() throws PortException {
		String connectionId = "aConnection";
			
		externalPortExample.connect(externalPortExample2, connectionId, "sigGenPort", "dataConverterFloat_in");
		
		try {
			List<String> connectionIds = externalPortExample.getPort("sigGenPort").getConnectionIds();
			
			assertTrue(connectionIds.contains(connectionId));
		} catch (ResourceNotFoundException | MultipleResourceException e) {
			fail("Unable to run tests " + e.getMessage());
		}
	}

	//Try connecting two ports that don't exist
	@Test
	public void testComponentConnectionFailure() throws PortException {
		try {
			RedhawkComponent comp = basicComponentDemo.getComponentByName("SigGen_sine.*");
			RedhawkComponent connectComp = basicComponentDemo.getComponentByName("HardLimit.*");

			// Remove any existing connections
			RedhawkPort port = comp.getPort("dataFloat_out");

			// Disconnect port if connected
			for (String id : port.getConnectionIds()) {
				port.disconnect(id);
			}
				
			assertTrue(port.getConnectionIds().isEmpty());

			// Connect the port again
			thrown.expect(PortException.class);
			comp.connect(connectComp, "aConnection", "Foo", "Bar");
		} catch (MultipleResourceException | ResourceNotFoundException e) {
			fail("Unable to run tests " + e.getMessage());
		}
	}
	
	//Try connecting two ports that exists but don't match
	@Test
	public void testComponentConnectionFailure2() throws PortException {
		try {
			RedhawkComponent comp = basicComponentDemo.getComponentByName("SigGen_sine.*");
			RedhawkComponent connectComp = basicComponentDemo.getComponentByName("sum.*");

			// Connect the port again
			thrown.expect(PortException.class);
			comp.connect(connectComp, "aConnection", "dataShort_out", "a");
		} catch (MultipleResourceException | ResourceNotFoundException e) {
			fail("Unable to run tests " + e.getMessage());
		}
	}
	
	@Test
	public void testApplicationConnectionFailure() throws PortException {
		thrown.expect(PortException.class);
		thrown.expectMessage("Multiple ports match with these components specify port names to match");
		externalPortExample.connect(externalPortExample2);
	}
	
	@AfterClass
	public static void cleanupStuff() {
		try {
			driver.getDomain().getFileManager().removeDirectory("/waveforms/ExternalPropPortExample");
		} catch (ConnectionException | MultipleResourceException | IOException | CORBAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
