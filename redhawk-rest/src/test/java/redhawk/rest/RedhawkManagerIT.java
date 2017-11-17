package redhawk.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.ApplicationReleaseException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.rest.model.Application;
import redhawk.rest.model.ApplicationContainer;
import redhawk.rest.model.Domain;
import redhawk.rest.model.ExternalPort;
import redhawk.rest.model.FetchMode;
import redhawk.rest.model.Port;
import redhawk.rest.model.PropertyContainer;
import redhawk.rest.utils.TestUtils;
import redhawk.testutils.RedhawkTestBase;

public class RedhawkManagerIT extends RedhawkTestBase{
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Unable to launch applications required for the test "+e.getMessage());
		}
	}
	
	@Test
	public void testGetDomain(){
		try {
			List<Domain> domains = manager.getAll(domainHost+":2809", "domain", null, FetchMode.EAGER);
			assertNotNull(domains);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unable to get domains "+e.getMessage());
		}
	}
	
	@Test
	public void testApplicationsWithExternalPortsAndProperties(){
		try {
			List<Application> applications = manager.getAll(domainHost+":2809", "application", "REDHAWK_DEV", FetchMode.EAGER);
		
			assertEquals("Should be atleast 2 app", 2, applications.size());
			
			for(Application application : applications){
				if(application.getName().equals(applicationName)){
					this.externalApplicationAsserts(application);			
				}else{
					//Should be no external ports  
					assertEquals("No external properties", true, application.getExternalPorts().isEmpty());
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetApplicationWithIdvsName(){
		try{
			Application appFromName = manager.get(domainHost+":2809", "application", domainName+"/"+basicApplication.getIdentifier());
			Application appFromId = manager.get(domainHost+":2809", "application", domainName+"/"+basicApplication.getName());
			
			String xmlForAppFromName = TestUtils.getStringFromJAXB(appFromName);
			String xmlForAppFromId = TestUtils.getStringFromJAXB(appFromId);
			
			assertEquals("XML should be the same whether app from name and app from Id ", xmlForAppFromName, xmlForAppFromId);
		}catch(Exception ex){
			fail("Unable to retrieve application "+ex.getMessage());
		}
	}
	
	@Test
	public void testApplicationWithExternalPortsAndProperties(){
		try {
			Application application = manager.get(domainHost+":2809", "application", "REDHAWK_DEV/External.*");
		
			assertNotNull("Should be 1 app", application);
			this.externalApplicationAsserts(application);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetApplicationWithNoExternalProperties(){
		try {
			PropertyContainer container = manager.getProperties(domainHost+":2809", "application", "REDHAWK_DEV/"+noExternalPropsPortsApp);
			assertNotNull(container);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			fail("FAILED!!!!"+e.getMessage());
		}
	}
	
	@Test
	public void testGetAll(){
		try {
			List<Application> applications = manager.getAll(nameServer, "application", domainName, FetchMode.EAGER);
			
			assertTrue("Applications should have data", !applications.isEmpty());
		
			String xml = TestUtils.getStringFromJAXB(new ApplicationContainer(applications));
			logger.info(xml);
		} catch (Exception e) {
			fail("Test failure "+e.getLocalizedMessage());
		}
	}
	
	@Test
	@Ignore("Until you have this set up to work dynamically")
	public void testGetDevicePort() {
		try {
			//TODO: Do this dynamically 
			Port port = manager.get(nameServer, "deviceport", "");
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unabled to get device port "+e.getMessage());
		}
	}
	
	private void externalApplicationAsserts(Application application){
		assertEquals("Properties should be here", true, !application.getProperties().isEmpty());
		assertEquals("Should be four external ports", 5, application.getExternalPorts().size());
	
		//Make sure external ports contain the externalname
		List<ExternalPort> ports = application.getExternalPorts();
		for(ExternalPort port : ports){
			assertNotNull(port.getExternalname());
		}		
	}
	
	@AfterClass
	public static void cleanupManager(){
		if(externalApplication!=null){
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
