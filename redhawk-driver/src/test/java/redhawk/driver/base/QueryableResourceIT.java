package redhawk.driver.base;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.component.RedhawkComponent;
import redhawk.driver.device.RedhawkDevice;
import redhawk.driver.devicemanager.RedhawkDeviceManager;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.properties.RedhawkProperty;
import redhawk.driver.properties.RedhawkSimple;
import redhawk.driver.properties.RedhawkSimpleSequence;
import redhawk.testutils.RedhawkTestBase;

public class QueryableResourceIT extends RedhawkTestBase{
	private static String appName = "myApp", allPropsWave = "allPropsWave";
	
	static RedhawkApplication basicComponentDemo, allPropsWaveform;
	
	static RedhawkComponent component; 
	
	@BeforeClass
	public static void setupQueryableResourceIT() {
		try {
			basicComponentDemo = driver.getDomain(domainName).createApplication(appName, "/waveforms/rh/basic_components_demo/basic_components_demo.sad.xml");
			allPropsWaveform = driver.getDomain(domainName).createApplication(allPropsWave, new File("src/test/resources/waveforms/AllPropsWaveform/AllPropsWaveform.sad.xml"));
			
			component = allPropsWaveform.getComponents().get(0);
			
			assertNotNull(basicComponentDemo);
			assertNotNull(allPropsWaveform);
		} catch (ApplicationCreationException | CORBAException | ResourceNotFoundException e) {
			fail("Unable to setup test "+e.getMessage());
		}		
	}
	
	@Test
	public void testSetRedhawkSimple() throws Exception {
		try {
			//Set simple property on a DomainManager
			RedhawkDomainManager manager = driver.getDomain();
			
			manager.setPropertyValue("COMPONENT_BINDING_TIMEOUT", 120);
			//TODO: Get rid of this casting nonsense
			assertEquals(new Long(120), ((RedhawkSimple)manager.getProperty("COMPONENT_BINDING_TIMEOUT")).getValue());
			
			//Set simple property on a DeviceManager
			//System.out.println(manager.getDevice);
			RedhawkDeviceManager deviceManager = manager.getDeviceManagerByName("DevMgr.*");
			deviceManager.setPropertyValue("CLIENT_WAIT_TIME", 12000);
			assertEquals(new Long(12000), ((RedhawkSimple)deviceManager.getProperty("CLIENT_WAIT_TIME")).getValue());
			
			//Set simple property on a Device
			//TODO: Probably need to explicitly get device by name
			RedhawkDevice device = deviceManager.getDevices().get(0);
			device.setPropertyValue("reserved_capacity_per_component", .05);
			assertEquals(new Float(.05), ((RedhawkSimple)device.getProperty("reserved_capacity_per_component")).getValue());			
			
			//Set simple property on a Application
			basicComponentDemo.setPropertyValue("sample_rate", 14000);
			assertEquals(new Double(14000), ((RedhawkSimple)basicComponentDemo.getProperty("sample_rate")).getValue());
			
			//Set simple property on a Component
			RedhawkComponent component = basicComponentDemo.getComponentByName("SigGen_sine.*");
			component.setPropertyValue("magnitude", 75);
			assertEquals(new Double(75), ((RedhawkSimple)component.getProperty("magnitude")).getValue());
		} catch (MultipleResourceException | CORBAException e) {
			e.printStackTrace();
			fail("Unable to run test "+e.getMessage());
		}
	}
	
	@Test
	public void testSetRedhawkSimpleSequence() throws Exception {
		String[] simpleSequence = new String[] {"please", "excuse", "my", "dear", "aunt", "sally"};
		component.setPropertyValue("examples", simpleSequence);
		
		RedhawkSimpleSequence seq = component.getProperty("examples");
		assertArrayEquals(simpleSequence, seq.getValues().toArray());
	}
	
	//TODO: Look into this
	@Test
	public void testSetStruct() throws Exception {
		try {
			RedhawkDomainManager manager = driver.getDomain();
			
			Map<String, Object> struct = new HashMap<>();
			struct.put("client_wait_times::devices", 70000);
			struct.put("client_wait_times::services", 70000);
			struct.put("client_wait_times::managers", 70000);

			manager.setPropertyValue("client_wait_times", struct);
		} catch (MultipleResourceException | CORBAException e) {
			fail("Unable to run test "+ e.getMessage());
		}

	}
	
	@AfterClass
	public static void cleanupQueryableResourceIT() {
		try {
			driver.getDomain().getFileManager().removeDirectory("/waveforms/AllPropsWaveform");
		} catch (ConnectionException | MultipleResourceException | IOException | CORBAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}
}
