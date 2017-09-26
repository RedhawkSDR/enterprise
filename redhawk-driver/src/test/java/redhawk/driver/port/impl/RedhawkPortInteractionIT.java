package redhawk.driver.port.impl;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.domain.RedhawkFileManager;
import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.ApplicationReleaseException;
import redhawk.driver.exceptions.ApplicationStopException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.PortException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.port.RedhawkPort;
import redhawk.testutils.RedhawkTestBase;

public class RedhawkPortInteractionIT extends RedhawkTestBase {
	private static RedhawkApplication app;
	
	private static String applicationName = "portTest";

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@BeforeClass
	public static void setup() throws MultipleResourceException, ApplicationCreationException, CORBAException {
		// Launch App
		app = driver.getDomain().createApplication(applicationName,
				new File("src/test/resources/waveforms/PortListenerTest/PortListenerTest.sad.xml"));
	}
	
	@Before
	public void setupRedhawkPortInteraction() throws MultipleResourceException, ApplicationCreationException, CORBAException {
		app = driver.getDomain().createApplication(applicationName, "/waveforms/PortListenerTest/PortListenerTest.sad.xml");
	}
	
	@After
	public void cleanup() throws ApplicationReleaseException {
		if(app!=null) {
			app.release();
			app = null;
		}
	}

	@Test
	public void testConnect() {
		try {
			RedhawkPort port = app.getComponentByName("SigGen.*").getPort("dataFloat_out");
			RedhawkPort portToConnectTo = app.getComponentByName("DataConverter_1.*").getPort("dataFloat");

			// Remove any existing connections
			for (String connection : port.getConnectionIds()) {
				port.disconnect(connection);
			}

			// Reconnect
			port.connect(portToConnectTo);

			// Make sure a connection now exists
			assertTrue(!port.getConnectionIds().isEmpty());
			
			//Test connecting the inverse connection see if helper does it for you
			
			// Remove any existing connections
			for (String connection : port.getConnectionIds()) {
				port.disconnect(connection);
			}
			
			portToConnectTo.connect(port);
			
			// Make sure a connection now exists
			assertTrue(!port.getConnectionIds().isEmpty());
		} catch (ResourceNotFoundException | MultipleResourceException | PortException e) {
			e.printStackTrace();
			fail("Unable to run test " + e.getMessage());
		}
	}

	@Test
	public void testConnectFailure() throws PortException {
		RedhawkPort port, portToConnectTo;
		try {
			port = app.getComponentByName("SigGen.*").getPort("dataFloat_out");
			portToConnectTo = app.getComponentByName("DataConverter_1.*").getPort("dataShort");

			// Remove any existing connections
			try {
				for (String connection : port.getConnectionIds()) {
					port.disconnect(connection);
				}
			} catch (PortException e) {
				fail("Unable to run test "+e.getMessage());
			}

			// Reconnect
			thrown.expect(PortException.class);
			thrown.expectMessage("Cannot connect ports of the same type or w/ non matching interfaces(repId).");
			port.connect(portToConnectTo);
		} catch (ResourceNotFoundException | MultipleResourceException e) {
			fail("Unable to run test "+e.getMessage());
		}
	}
	
	@Test
	public void testConnectFailurePortsOfSameType() throws PortException {
		RedhawkPort port, portToConnectTo;
		try {
			port = app.getComponentByName("SigGen.*").getPort("dataFloat_out");
			portToConnectTo = app.getComponentByName("SigGen.*").getPort("dataShort_out");

			// Remove any existing connections
			try {
				for (String connection : port.getConnectionIds()) {
					port.disconnect(connection);
				}
			} catch (PortException e) {
				fail("Unable to run test "+e.getMessage());
			}

			// Reconnect
			thrown.expect(PortException.class);
			thrown.expectMessage("Cannot connect ports of the same type or w/ non matching interfaces(repId).");
			port.connect(portToConnectTo);
		} catch (ResourceNotFoundException | MultipleResourceException e) {
			fail("Unable to run test "+e.getMessage());
		}		
	}

	@Test
	public void testListenAndSend() throws ApplicationStopException, ApplicationReleaseException {
		String[] portNames = { "dataOctet_out", "dataFloat_out", "dataShort_out", "dataDouble_out", "dataUshort_out" };

		for (String portName : portNames) {
			try {
				// Get port
				RedhawkPort port = app.getComponentByName("DataConverter_1.*").getPort(portName);

				// Send To Port
				String providesPort = portName.substring(0, portName.indexOf("_"));
				RedhawkPort sendToPort = app.getComponentByName("DataConverter_2.*").getPort(providesPort);

				// Listen to data on port
				GenericPortListener pl = new GenericPortListener(sendToPort);

				System.out.println("Made it here");
				port.listen(pl);
				System.out.println("Made it to listen");				
				// Active SRI should be empty because no data has been sent
				assertTrue(sendToPort.getActiveSRIs().isEmpty());

				if (!app.isStarted()) {
					System.out.println("Starting app");
					app.start();
				}else {
					System.out.println("App already started");
				}

				while (pl.getMessagesReceived() < 10) {
					// Loop can't be empty
					Thread.sleep(1);

					// Send data out
					if (pl.receivedData) {
						sendToPort.send(pl.getPacket());
					}
				}

				port.disconnect();
				assertTrue("Should have greater than or equal to 10 messages", 10 <= pl.getMessagesReceived());

				// ActiveSRI should be non empty because data has been sent
				assertTrue(!sendToPort.getActiveSRIs().isEmpty());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				fail("Test failure " + e.getMessage());
			} finally {
				// Release app
				if (app != null)
					app.release();

				// Relaunch app for next test
				try {
					app = driver.getDomain().createApplication("testPorts",
							"/waveforms/PortListenerTest/PortListenerTest.sad.xml");
				} catch (MultipleResourceException | ApplicationCreationException | CORBAException e) {
					fail("Broken test logic should be able to re deploy from that location.");
				}
			}
		}
	}

	@AfterClass
	public static void cleanupWaveform() throws ConnectionException, ResourceNotFoundException, CORBAException {
		try {
			RedhawkFileManager manager = driver.getDomain(domainName).getFileManager();
			manager.removeDirectory("/waveforms/PortListenerTest");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Clean this up eventaully");
		}
	}
}
