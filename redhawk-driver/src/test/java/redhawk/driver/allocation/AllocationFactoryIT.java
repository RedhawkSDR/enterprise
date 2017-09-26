package redhawk.driver.allocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import redhawk.driver.allocationmanager.RedhawkAllocationManager;
import redhawk.driver.device.RedhawkDevice;
import redhawk.driver.exceptions.AllocationException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.testutils.RedhawkDeviceTestBase;

public class AllocationFactoryIT extends RedhawkDeviceTestBase{
	private final String tunerAllocationType = "FRONTEND::tuner_allocation";

	private final String listenerAllocationType = "FRONTEND::listener_allocation";

	@Test
	public void testTunerAllocateDevice() {
		try {
			RedhawkAllocationManager allocManager = driver.getDomain(domainName).getAllocationManager();
			RedhawkDevice device = deviceManager.getDevices().get(0);
			
			String deviceId = device.getIdentifier();
			Map<String, Object> allocation = AllocationFactory.createTunerAllocation();
			allocation.put(AllocationFactory.CENTER_FREQUENCY, 101100000d);
			allocation.put(AllocationFactory.SAMPLE_RATE, 256000d);
			allocation.put(AllocationFactory.BANDWIDTH_TOLERANCE, 20.0);
			allocation.put(AllocationFactory.SAMPLE_RATE_TOLERANCE, 20.0);

			assertTrue(allocManager.getAllocations().isEmpty());
			allocManager.allocate(deviceId, tunerAllocationType, allocation);
			
			assertTrue(!allocManager.getAllocations().isEmpty());
			
			String allocId = allocManager.getAllocations().get(0).getAllocationId();			
			
			//Add a listener allocation 
			//String listenerAllocationId = "listenerAlloc";
			//ap<String, Object> listenerAllocation = AllocationFactory.createTunerListenerAllocation(allocId, listenerAllocationId);
			
			//allocManager.allocate(deviceId, listenerAllocationType, listenerAllocation);
			//assertEquals(2, allocManager.getAllocations().size());
			
			//Deallocate 
			allocManager.deallocate(allocId);
			assertTrue(allocManager.getAllocations().isEmpty());
		} catch (ResourceNotFoundException | CORBAException | AllocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void test() throws ResourceNotFoundException, CORBAException, AllocationException {
		RedhawkAllocationManager allocManager = driver.getDomain(domainName).getAllocationManager();
		RedhawkDevice device = deviceManager.getDevices().get(0);
		
		String deviceId = device.getIdentifier();
		
		System.out.println(allocManager.getAllocations());
		if(!allocManager.getAllocations().isEmpty()) {
			String allocId = allocManager.getAllocations().get(0).getAllocationId();
			Map<String, Object> listenerAllocation = AllocationFactory.createTunerListenerAllocation(allocId, "FOO_BAR");

			allocManager.allocate(deviceId, listenerAllocationType, listenerAllocation);
			System.out.println(allocManager.getAllocations());
			System.out.println("Alloc Id: "+allocId);
			allocManager.deallocate(allocId);
		}else {
			System.out.println("No allocation anymore");
		}
	}
}
