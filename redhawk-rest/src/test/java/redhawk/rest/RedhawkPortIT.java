package redhawk.rest;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.rest.model.Port;
import redhawk.rest.model.PortStatisticsContainer;
import redhawk.rest.model.SRIContainer;
import redhawk.rest.utils.TestUtils;
import redhawk.testutils.RedhawkTestBase;

public class RedhawkPortIT extends RedhawkTestBase {
	private static RedhawkManager manager = new RedhawkManager();

	private static String appName = "myApp";

	private static RedhawkApplication app;

	private static String nameServer;

	@BeforeClass
	public static void setup() {
		try {
			String sadLocation = "/waveforms/rh/basic_components_demo/basic_components_demo.sad.xml";
			nameServer = domainHost + ":" + domainPort;
			
			//driver.getDomain();
			app = driver.getDomain().createApplication(appName,
				 sadLocation);
		} catch (MultipleResourceException | CORBAException | ApplicationCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Unable to launch applications required for the test " + e.getMessage());
		}
	}
	
	@Test
	public void testGetPortStatistics(){
		String portPath = domainName + "/" + app.getIdentifier() + "/HardLimit.*/dataFloat_out";
		
		try {
			PortStatisticsContainer container = manager.getRhPortStatistics(nameServer, "port", portPath);
			
			assertNotNull(container);
			String xml = TestUtils.getStringFromJAXB(container);
			logger.info(xml);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Test failure "+e.getMessage());
		}

	}
	
	@Test
	public void testGetPortSRI(){
		String portPath = domainName + "/" + app.getIdentifier() + "/HardLimit.*/dataFloat_in";
		
		try {
			app.start();
			Thread.sleep(1000l);
			SRIContainer container = manager.getSRI(nameServer, "port", portPath);
			
			assertNotNull(container);
			String xml = TestUtils.getStringFromJAXB(container);
			logger.info(xml);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Test failure "+e.getMessage());
		}

	}

	@Test
	public void testDisconnectPort() {
		String portPath = domainName + "/" + app.getIdentifier() + "/HardLimit.*/dataFloat_out";

		Port port;
		try {
			port = manager.get(nameServer, "port", portPath);
			assertTrue("Expecting there to be a connection present ", !port.getConnectionIds().isEmpty());

			for (String connectionId : port.getConnectionIds())
				manager.disconnectConnectionById(nameServer, "port", portPath, connectionId);
		
			port = manager.get(nameServer, "port", portPath);
			assertTrue("Should no longer be connection ids", port.getConnectionIds().isEmpty());
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Failed test "+ex.getMessage());
		}
	}
	
	
}
