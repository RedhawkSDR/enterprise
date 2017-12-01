package redhawk.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.component.RedhawkComponent;
import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.rest.model.Component;
import redhawk.rest.model.FetchMode;
import redhawk.testutils.RedhawkTestBase;

public class RedhawkRestComponentIT extends RedhawkTestBase{
	private static RedhawkManager manager = new RedhawkManager();
	
	private static RedhawkApplication basicApplication;

	private static String appName = "basicApp";
	
	private static String nameServer; 
	
	@BeforeClass
	public static void setup(){
		//Launch app 
		try {
			nameServer = domainHost+":"+domainPort;
			
			basicApplication = driver.getDomain().createApplication(appName, 
					new File("../redhawk-driver/src/test/resources/waveforms/rh/testWaveform.sad.xml"));
		} catch (MultipleResourceException | ApplicationCreationException | CORBAException e) {
			e.printStackTrace();
			fail("Test is not setup properly unable to launch application "+e.getMessage());
		}
	}
	
	@Test
	public void testControlComponent() {
		String componentName = basicApplication.getComponents().get(0).getName();
		String componentLocation = domainName+'/'+appName+'/'+componentName; 
		manager.controlComponent(nameServer, "stop", componentLocation);
		
		try {
			RedhawkComponent comp = driver.getComponent(componentLocation);
			assertEquals("Component should be stopped", false, comp.started());
			
			manager.controlComponent(nameServer, "start", componentLocation);
			assertEquals("Component should be started", true, comp.started());
		} catch (ResourceNotFoundException | MultipleResourceException | CORBAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testComponentFields() {
		try {
			List<Component> components = manager.getAll(nameServer, "component", domainName+"/"+appName, FetchMode.EAGER);
			
			//Make sure components are present 
			assertTrue(components.size()>0);
			
			/*
			 * Make sure fields are being filled in
			 */
			for(Component comp : components) {
				assertNotNull(comp.getProcessId());
				assertNotNull(comp.getDeviceIdentifier());
				assertNotNull(comp.getImplementation());
			}
		} catch (Exception e) {
			fail("Failure running test "+e.getMessage());
		}
	}
}
