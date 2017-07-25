package redhawk.rest;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Level;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.ApplicationReleaseException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.rest.model.Application;
import redhawk.rest.model.ExternalPort;
import redhawk.rest.model.PortStatisticsContainer;
import redhawk.rest.model.SRIContainer;
import redhawk.rest.utils.TestUtils;
import redhawk.testutils.RedhawkTestBase;

public class RedhawkRestApplicationIT extends RedhawkTestBase{
	private static RedhawkManager manager = new RedhawkManager();
	
	private static RedhawkApplication externalApplication, basicApplication;

	private static String applicationName = "ExternalPropsApp";
	
	private static String noExternalPropsPortsApp = "basicApp";
	
	private static String nameServer; 

	@BeforeClass
	public static void setup(){
		//Launch app 
		try {
			nameServer = domainHost+":"+domainPort;
			externalApplication = driver.getDomain().createApplication(applicationName, 
					new File("../redhawk-driver/src/test/resources/waveforms/ExternalPropPortExample/ExternalPropPortExample.sad.xml"));
			basicApplication = driver.getDomain().createApplication(noExternalPropsPortsApp, 
					new File("../redhawk-driver/src/test/resources/waveforms/rh/testWaveform.sad.xml"));
		} catch (MultipleResourceException | ApplicationCreationException | CORBAException e) {
			e.printStackTrace();
			fail("Test is not setup properly unable to launch application "+e.getMessage());
		}
	}
	
	@Test
	public void testApplicationFields() {
		try {
			Application application = manager.get(nameServer, "application", domainName+"/"+basicApplication.getName());
			
			String xml = TestUtils.getStringFromJAXB(application);
			logger.fine(xml);
			assertNotNull(application.isAware());
		} catch (Exception e) {
			fail("Test failure: "+e.getMessage());
		}
	}
	
	@Test
	public void testGetApplicationPort(){
		String applicationPath = domainName + "/" + externalApplication.getIdentifier() + "/hardLimitPort";
		
		try {
			ExternalPort port = manager.get(nameServer, "applicationport", applicationPath.split("/"));
		
			assertNotNull(port);
			logger.info(TestUtils.getStringFromJAXB(port));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Test is failing "+e.getMessage());
		}
	}
	
	@Test
	public void testGetApplicationPortStatistics(){ 		
		try {
			String portPath = domainName + "/" + externalApplication.getIdentifier() + "/hardLimitPort";
			logger.info("Application path: "+portPath);
			PortStatisticsContainer container = manager.getRhPortStatistics(nameServer, "applicationport", portPath);
		
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
		try {
			String portPath = domainName + "/" + externalApplication.getIdentifier() + "/hardLimitPort_in";
			externalApplication.start();
			Thread.sleep(1000l);
			logger.info("Application path: "+portPath);
			SRIContainer container = manager.getSRI(nameServer, "applicationport", portPath);
		
			assertNotNull(container);

			String xml = TestUtils.getStringFromJAXB(container);
			logger.info(xml);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Test failure "+e.getMessage());
		}
	}
	
	@Test
	public void testDisconnectionPortConnection(){
		try {
			String portPath = domainName + "/" + externalApplication.getIdentifier() + "/hardLimitPort";
			logger.info("Port path: "+portPath);
			ExternalPort port = manager.get(nameServer, "applicationport", portPath.split("/"));
			
			//At beginning of test port should be there
			assertTrue(!port.getConnectionIds().isEmpty());
			
			manager.disconnectConnectionById(nameServer, "applicationport", portPath, port.getConnectionIds().get(0));
			
			//Should now be empty
			port = manager.get(nameServer, "applicationport", portPath.split("/"));
			assertTrue(port.getConnectionIds().isEmpty());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Test failure "+e.getMessage());
		}		
	}
	
	@AfterClass
	public static void cleanupManager(){
		if(externalApplication!=null && basicApplication!=null){
			try {
				externalApplication.release();
				basicApplication.release();
				
				driver.getDomain().getFileManager().removeDirectory("/waveforms/ExternalPropPortExample");
				driver.getDomain().getFileManager().removeDirectory("/waveforms/testWaveform");			
			} catch (ApplicationReleaseException | ConnectionException | MultipleResourceException | IOException | CORBAException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
	}
}
