package redhawk.driver.device.impl;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import redhawk.RedhawkTestBase;
import redhawk.driver.device.impl.RedhawkDeviceImpl;
import redhawk.driver.devicemanager.RedhawkDeviceManager;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;

/*
 * This is currently a manual test until I figure out how to automatically launch a node from the REDHAWK Driver. 
 * 
 * Test relies on the SimulatorNode in src/test/resources being available from the DeviceManager on your domain. 
 */
@Ignore("Works need to make dynamic so it can run create it's own devicemanager")
public class RedhawkDeviceImplTestIT extends RedhawkTestBase {
	@Test
	public void test() throws MultipleResourceException, CORBAException, ResourceNotFoundException {
		//Get Device Manager
		RedhawkDeviceManager deviceManager = driver.getDomain().getDeviceManagerByName("SimulatorNode");
		
		//Get Device you want 
		RedhawkDeviceImpl device = (RedhawkDeviceImpl) deviceManager.getDeviceByName("FmRdsSimulator.*");
		
		//Create Allocation Map 
		Map<String, Object> newAlloc = new HashMap<>();
				
		//Section 7.1 REDHAWK Manual for these properties
		/*Example tuner status. 
		 * {
		 * FRONTEND::tuner_status::rf_flow_id=, 
		 * FRONTEND::tuner_status::bandwidth=2280000.0, 
		 * FRONTEND::tuner_status::stream_id=MyStreamID, 
		 * FRONTEND::tuner_status::center_frequency=1.011E8, 
		 * FRONTEND::tuner_status::group_id=, 
		 * FRONTEND::tuner_status::gain=0.0, 
		 * FRONTEND::tuner_status::enabled=true, 
		 * FRONTEND::tuner_status::sample_rate=285000.0, 
		 * FRONTEND::tuner_status::allocation_id_csv=default:55d211b1-c35c-44a8-837c-96a7470089f0, 
		 * FRONTEND::tuner_status::tuner_type=RX_DIGITIZER
		 * }
		*/
		String allocId = "myTestAllocationId";
		newAlloc.put("FRONTEND::tuner_allocation::allocation_id", allocId);
		newAlloc.put("FRONTEND::tuner_allocation::tuner_type", "RX_DIGITIZER");
		newAlloc.put("FRONTEND::tuner_allocation::center_frequency", 101100000d);//101.1e6
		newAlloc.put("FRONTEND::tuner_allocation::sample_rate", 256000d);//256e3
		newAlloc.put("FRONTEND::tuner_allocation::bandwidth_tolerance", 20.0);
		newAlloc.put("FRONTEND::tuner_allocation::sample_rate_tolerance", 20.0);
		
		//Allocate Device
		device.allocate(newAlloc);
		
		//Should now have a used tuner
		assertEquals(false, device.getUsedTuners().isEmpty());
		
		//Should now be no unused tuners
		assertEquals(true, device.getUnusedTuners().isEmpty());
		
		//Check to make sure center frequency is correct 
		assertEquals(101100000d, device.getUsedTuners().get(0).get("FRONTEND::tuner_status::center_frequency"));
		
		//Deallocate Device
		device.deallocate(allocId);
		
		//Reverse the last test
		assertEquals(true, device.getUsedTuners().isEmpty()); //TODO: Fix this you should be able to call this and get an empty list
		
		//Should now be no unused tuners
		assertEquals(false, device.getUnusedTuners().isEmpty());
	}
}
