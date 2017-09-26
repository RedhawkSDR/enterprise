package redhawk.driver.allocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Map;

import org.junit.After;
import org.junit.Test;

import redhawk.driver.allocationmanager.AllocationInfo;
import redhawk.driver.allocationmanager.RedhawkAllocationManager;
import redhawk.driver.device.RedhawkDevice;
import redhawk.driver.device.impl.RedhawkDeviceImpl;
import redhawk.driver.exceptions.AllocationException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.testutils.RedhawkDeviceTestBase;

public class AllocationFactoryIT extends RedhawkDeviceTestBase{
	@Test
	public void testTunerAllocateDeviceWithAllocationManager() {
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
			allocManager.allocate(deviceId, AllocationFactory.TUNER_ALLOCATION_TYPE, allocation);
			
			assertTrue(!allocManager.getAllocations().isEmpty());
			
			//Id from allocation manager
			AllocationInfo allocInfo = allocManager.getAllocations().get(0);
			String allocManagerId = allocInfo.getAllocationId();
			
			//Ensure passed in allocation Id matched actual allocation 
			String allocationId = (String)allocation.get(AllocationFactory.ALLOCATION_ID);
			assertEquals(allocationId, allocInfo.getAllocationProperties().get(AllocationFactory.ALLOCATION_ID));
			
			//Deallocate 
			allocManager.deallocate(allocManagerId);
			assertTrue(allocManager.getAllocations().isEmpty());
		} catch (ResourceNotFoundException | CORBAException | AllocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testTunerListenerAllocation() {
		/*
		 * Do full allocation and listener allocation with allocation manager
		 */
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
			allocManager.allocate(deviceId, AllocationFactory.TUNER_ALLOCATION_TYPE, allocation);
			
			assertTrue(!allocManager.getAllocations().isEmpty());
			
			//Id from allocation manager
			AllocationInfo allocInfo = allocManager.getAllocations().get(0);
			String allocManagerId = allocInfo.getAllocationId();
			String allocationId = (String)allocation.get(AllocationFactory.ALLOCATION_ID);

			String listenerAllocId = "barney";
			Map<String, Object> listenerAllocation = AllocationFactory.createTunerListenerAllocation(allocationId, listenerAllocId);
			allocManager.allocate(deviceId, AllocationFactory.LISTENER_ALLOCATION_TYPE, listenerAllocation);
			
			assertEquals(2, allocManager.getAllocations().size());
		} catch (ResourceNotFoundException | CORBAException | AllocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testTunerListenerAllocation2() {
		try {
			/*
			 * Allocate device directly then use AllocationManager to create 
			 * Listener allocation
			 */
			RedhawkDevice device = deviceManager.getDevices().get(0);
			RedhawkAllocationManager allocManager = driver.getDomain(domainName).getAllocationManager();
			
			String deviceId = device.getIdentifier();
			Map<String, Object> allocation = AllocationFactory.createTunerAllocation();
			allocation.put(AllocationFactory.CENTER_FREQUENCY, 101100000d);
			allocation.put(AllocationFactory.SAMPLE_RATE, 256000d);
			allocation.put(AllocationFactory.BANDWIDTH_TOLERANCE, 20.0);
			allocation.put(AllocationFactory.SAMPLE_RATE_TOLERANCE, 20.0);
			
			String allocationId = device.allocate(allocation);
			String expectedAllocationId = (String) allocation.get(AllocationFactory.ALLOCATION_ID);
			
			//Ensure device has appropriate allocation id
			assertEquals(expectedAllocationId, allocationId);
			
			Map<String, Object> listenerAllocation = AllocationFactory.createTunerListenerAllocation(allocationId, "myListenerAllocation");
			allocManager.allocate(deviceId, AllocationFactory.LISTENER_ALLOCATION_TYPE, listenerAllocation);
			
			//Ensure listener allocation exists and is using passed down Id
			assertEquals(1, allocManager.getAllocations().size());
			
			Map<String, Object> allocProps = allocManager.getAllocations().get(0).getAllocationProperties();

			String listenerAllocationId = (String) allocProps.get("FRONTEND::listener_allocation::listener_allocation_id");
			assertEquals("myListenerAllocation", listenerAllocationId);
		} catch (ResourceNotFoundException | CORBAException | AllocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGenericTunerListenerAllocateDevice() {
		try {
			/*
			 * Create allocation
			 */
			RedhawkAllocationManager allocManager = driver.getDomain(domainName).getAllocationManager();
			RedhawkDevice device = deviceManager.getDevices().get(0);
			
			String deviceId = device.getIdentifier();
			Map<String, Object> allocation = AllocationFactory.createTunerAllocation();
			allocation.put(AllocationFactory.CENTER_FREQUENCY, 101100000d);
			allocation.put(AllocationFactory.SAMPLE_RATE, 256000d);
			allocation.put(AllocationFactory.BANDWIDTH_TOLERANCE, 20.0);
			allocation.put(AllocationFactory.SAMPLE_RATE_TOLERANCE, 20.0);
		
			allocManager.allocate(deviceId, AllocationFactory.TUNER_ALLOCATION_TYPE, allocation);

			//Get allocation Id for reference in listener allocation 
			assertTrue(!allocManager.getAllocations().isEmpty());
					
			AllocationInfo info = allocManager.getAllocations().get(0);
			
			//Create Listener Allocation 
			Map<String, Object> listenerAllocation = AllocationFactory.createTunerGenericListenerAllocation();
			listenerAllocation.put(AllocationFactory.CENTER_FREQUENCY, 101100000d);
			listenerAllocation.put(AllocationFactory.SAMPLE_RATE, 256000d);
			listenerAllocation.put(AllocationFactory.BANDWIDTH_TOLERANCE, 20.0);
			listenerAllocation.put(AllocationFactory.SAMPLE_RATE_TOLERANCE, 20.0);
			
			//Try allocating with Allocation manager
			/*System.out.println("Allocation With Allocation Manager ");
			allocManager.allocate(deviceId, tunerAllocationType, listenerAllocation);			

			info = allocManager.getAllocations().get(0);
			System.out.println("Latest allocation data from manager: ");
			for(AllocationInfo allocInfo : allocManager.getAllocations()) {
				for(Map.Entry<String, Object> entry : allocInfo.getAllocationProperties().entrySet()) {
					System.out.println("\tKey: "+entry.getKey()+" Value: "+entry.getValue());
				}
			}*/
			
			//Try allocating with Device
			RedhawkDeviceImpl impl = (RedhawkDeviceImpl) device;
			String deviceAllocId = impl.allocate(listenerAllocation);
			
			Map<String, Object> deviceStatus = impl.getUsedTuners().get(0);
			
			/*System.out.println("Device Status: ");
			for(Map.Entry<String, Object> entry : deviceStatus.entrySet()) {
				System.out.println("\t"+entry.getKey()+":"+entry.getValue());
			}*/
			String allocIds = (String) deviceStatus.get("FRONTEND::tuner_status::allocation_id_csv");
			assertTrue(allocIds.toString().contains(deviceAllocId));
		} catch (ResourceNotFoundException | CORBAException | AllocationException e) {
			fail("Unable to run test.");
		}
	}
	
	/*
	 * Clean up behind tests...
	 */
	@After
	public void cleanupAllocationFactory() throws ResourceNotFoundException, CORBAException {
		RedhawkAllocationManager allocManager = driver.getDomain(domainName).getAllocationManager();

		for(AllocationInfo alloc : allocManager.getAllocations()) {
			allocManager.deallocate(alloc.getAllocationId());
		}
		
		RedhawkDeviceImpl impl = (RedhawkDeviceImpl) deviceManager.getDevices().get(0);
		for(Map<String, Object> alloc : impl.getUsedTuners()) {
			String[] allocIds = ((String) alloc.get("FRONTEND::tuner_status::allocation_id_csv")).split(",");
			for(String allocId : allocIds) {
				impl.deallocate(allocId);				
			}
		}
	}
}