package redhawk.driver.domain.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import redhawk.driver.device.RedhawkDevice;
import redhawk.driver.domain.RedhawkAllocationManager;
import redhawk.driver.exceptions.AllocationException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.testutils.RedhawkTestBase;

public class RedhawkAllocationManagerIT extends RedhawkTestBase{
	private static RedhawkAllocationManager allocMgr;
	
	private final String allocationType = "FRONTEND::tuner_allocation";
	
	@BeforeClass
	public static void setupAllocationManager(){
		try {
			allocMgr = driver.getDomain().getAllocationManager();
		} catch (MultipleResourceException | CORBAException e) {
			// TODO Auto-generated catch block
			fail("Issue accessing REDHAWK Domain "+e.getMessage());
		}
	}
	
	@Test
	public void testGetCorbaObj(){
		//Test to make sure you can access corba obj from Domain Manager
		assertNotNull(allocMgr.getCorbaObj());
	}
	
	@Test
	public void testListDevices(){
		List<RedhawkDevice> devices = allocMgr.listDevices();
		
		for(RedhawkDevice dev : devices){
			System.out.println(dev);
		}
		
		assertEquals("Should be a simulator and GPP available", 2, devices.size());
	}
	
	@Test
	public void testAllocateDevice(){
		//Get simulator deviceId
		try {
			RedhawkDevice device = driver.getDeviceManager("REDHAWK_DEV/Simulator.*").getDevices().get(0);
			
			String identifier = device.getIdentifier();
			
			allocMgr.allocate(identifier, allocationType, this.getAllocationProperties());
		} catch (ResourceNotFoundException | MultipleResourceException | CORBAException | AllocationException e) {
			fail("Issue allocating device "+e.getMessage());
		}
	}
	
	
	private Map<String, Object> getAllocationProperties(){
		Map<String, Object> newAlloc = new HashMap<>();
		
		String allocId = "myTestAllocationId";
		newAlloc.put("FRONTEND::tuner_allocation::allocation_id", allocId);
		newAlloc.put("FRONTEND::tuner_allocation::tuner_type", "RX_DIGITIZER");
		newAlloc.put("FRONTEND::tuner_allocation::center_frequency", 101100000d);//101.1e6
		newAlloc.put("FRONTEND::tuner_allocation::sample_rate", 256000d);//256e3
		newAlloc.put("FRONTEND::tuner_allocation::bandwidth_tolerance", 20.0);
		newAlloc.put("FRONTEND::tuner_allocation::sample_rate_tolerance", 20.0);		
	
		return newAlloc;
	}
	
	
}
