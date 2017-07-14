package redhawk.driver.port.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import BULKIO.StreamSRI;
import BULKIO.updateSRI;
import BULKIO.updateSRIHelper;
import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.component.RedhawkComponent;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.port.RedhawkPort;
import redhawk.driver.port.RedhawkPortStatistics;
import redhawk.testutils.RedhawkTestBase;

public class RedhawkPortImplIT extends RedhawkTestBase{
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
	
	@Test
	public void testGetActiveSRIs(){
		try{
			//Start app so SRI is present 
			application.start();
			RedhawkPort port = driver.getPort("REDHAWK_DEV/myApp/HardLimit.*/dataFloat_in");
			//Thread.sleep(5000l);//Give waveform a second to start
			updateSRI t = updateSRIHelper.narrow(port.getCorbaObject());
			for(StreamSRI sri : t.activeSRIs()){
				System.out.println(sri);
			}
			
			System.out.println("Stopped application");
			application.stop();
			t = updateSRIHelper.narrow(port.getCorbaObject());
			for(StreamSRI sri : t.activeSRIs()){
				System.out.println(sri);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			fail("Test failure "+ex.getMessage());
		}
	}
	
	@Test
	public void getPortState(){
		RedhawkPort port;
		try {
			port = driver.getPort("REDHAWK_DEV/myApp/HardLimit.*/dataFloat_in");
		
			//Checks to make sure port State is not null
			assertNotNull(port.getPortState());
		}catch(Exception ex){
			ex.printStackTrace();
			fail("Test failure "+ex.getMessage());
		}
	}
}
