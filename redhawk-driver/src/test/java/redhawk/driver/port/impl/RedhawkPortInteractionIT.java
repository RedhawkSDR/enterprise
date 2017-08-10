package redhawk.driver.port.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.domain.RedhawkFileManager;
import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.ApplicationReleaseException;
import redhawk.driver.exceptions.ApplicationStopException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.port.RedhawkPort;
import redhawk.testutils.RedhawkTestBase;

public class RedhawkPortInteractionIT extends RedhawkTestBase{
	private static RedhawkApplication app; 
	
	@BeforeClass
	public static void setup() throws MultipleResourceException, ApplicationCreationException, CORBAException {
		//Launch App
		app = driver.getDomain().createApplication("portTest", new File("src/test/resources/waveforms/PortListenerTest/PortListenerTest.sad.xml"));
	}

	@Test
	public void connectAndSend() throws ApplicationStopException, ApplicationReleaseException {
		String[] portNames = {"dataOctet_out", "dataFloat_out", "dataShort_out", "dataDouble_out", "dataUshort_out"};
		
		for(String portName : portNames) {
			try {
				// Get port
				RedhawkPort port = app.getComponentByName("DataConverter_1.*").getPort(portName);

				// Send To Port
				String providesPort = portName.substring(0, portName.indexOf("_"));
				RedhawkPort sendToPort = app.getComponentByName("DataConverter_2.*").getPort(providesPort);

				// Listen to data on port
				GenericPortListener pl = new GenericPortListener(sendToPort);

				port.connect(pl);
				//Active SRI should be empty because no data has been sent
				assertTrue(sendToPort.getActiveSRIs().isEmpty());
				
				if (!app.isStarted())
					app.start();
				
				while (pl.getMessagesReceived() < 10) {
					// Loop can't be empty
					Thread.sleep(1);
					
					//Send data out
					if(pl.receivedData)
						sendToPort.send(pl.getPacket());
				}

				port.disconnect();
				assertEquals("Should have 10 messages", new Integer(10), pl.getMessagesReceived());
				
				//ActiveSRI should be non empty because data has been sent
				assertTrue(!sendToPort.getActiveSRIs().isEmpty());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				fail("Test failure " + e.getMessage());
			} finally {
				//Release app
				if (app != null)
					app.release();
				
				//Relaunch app for next test 
				try {
					app = driver.getDomain().createApplication("testPorts", "/waveforms/PortListenerTest/PortListenerTest.sad.xml");
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
