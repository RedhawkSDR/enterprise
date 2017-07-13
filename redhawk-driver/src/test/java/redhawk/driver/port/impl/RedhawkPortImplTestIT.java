package redhawk.driver.port.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.component.RedhawkComponent;
import redhawk.driver.port.RedhawkPortStatistics;
import redhawk.testutils.RedhawkTestBase;

public class RedhawkPortImplTestIT extends RedhawkTestBase{
	private static RedhawkApplication application; 
	
	@BeforeClass
	public static void setupRedhawkUsesPort(){
		try {
			application = driver.getDomain().createApplication("myApp", "/waveforms/rh/basic_components_demo/basic_components_demo.sad.xml");
		} catch (Exception e) {
			e.printStackTrace();
			fail("Issue at startup "+e.getMessage());
		}
	}
	
	@Test
	public void testUsesPortStatistics(){
		try {
			RedhawkComponent comp = driver.getComponent("REDHAWK_DEV/myApp/HardLimit.*");
			
			//Checks to make sure you're able to retrieve Uses Port Statistics
			assertNotNull(comp.getPort("dataFloat_out").getPortStatistics());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Issue w/ test "+e.getMessage());
		}
	}
	
	@Test
	public void testProvidesPortStatistics(){
		RedhawkComponent comp;
		try {
			comp = driver.getComponent("REDHAWK_DEV/myApp/HardLimit.*");
		
			//Checks to make sure you're able to retrieve Provides Port Statistics
			assertNotNull(comp.getPort("dataFloat_in").getPortStatistics());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Issue w/ test "+e.getMessage());
		}
	}
}
