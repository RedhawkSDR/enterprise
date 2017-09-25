package redhawk.driver.properties;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.component.RedhawkComponent;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.testutils.RedhawkTestBase;

/**
 * Base class so you don't need to worry about relaunching 
 * all props waveform in each property test
 *
 */
public class RedhawkPropertyTestBase extends RedhawkTestBase{
	public static String appName = "myApp";
	
	public static RedhawkApplication allPropsWaveform; 
	
	public static RedhawkComponent component;
	
	public static RedhawkDomainManager manager;	
	
	@BeforeClass
	public static void setupRedhawkPropertyTestBase() throws ResourceNotFoundException {
		try {
			manager = driver.getDomain(domainName);
			allPropsWaveform = manager.createApplication(appName, new File("src/test/resources/waveforms/AllPropsWaveform/AllPropsWaveform.sad.xml"));
			component = allPropsWaveform.getComponents().get(0);
		
			assertNotNull(allPropsWaveform);
			assertNotNull(component);
		} catch (ApplicationCreationException | CORBAException e) {
			e.printStackTrace();
			fail("Unable to setup test "+e.getMessage());
		}
	}
	
	@AfterClass
	public static void cleanupRedhawkPropertyTestBase() {
		try {
			driver.getDomain().getFileManager().removeDirectory("/waveforms/AllPropsWaveform");
		} catch (ConnectionException | MultipleResourceException | IOException | CORBAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}
}
