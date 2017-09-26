package redhawk.driver.allocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

public class AllocationFactoryTest {
	private Map<String, Object> defaultTunerAllocation = new HashMap<>();
	
	private String allocationId = UUID.randomUUID().toString();
	
	@Before
	public void setup() {
		defaultTunerAllocation.put(AllocationFactory.SAMPLE_RATE_TOLERANCE, 0.0);
		defaultTunerAllocation.put(AllocationFactory.GROUP_ID, "");
		defaultTunerAllocation.put(AllocationFactory.TUNER_TYPE, "RX_DIGITIZER");
		defaultTunerAllocation.put(AllocationFactory.BANDWIDTH, 0.0);
		defaultTunerAllocation.put(AllocationFactory.RF_FLOW_ID, "");
		defaultTunerAllocation.put(AllocationFactory.ALLOCATION_ID, allocationId);
		defaultTunerAllocation.put(AllocationFactory.SAMPLE_RATE, 1.0);
		defaultTunerAllocation.put(AllocationFactory.DEVICE_CONTROL, true);
		defaultTunerAllocation.put(AllocationFactory.CENTER_FREQUENCY, 0.0);	
		defaultTunerAllocation.put(AllocationFactory.BANDWIDTH_TOLERANCE, 0.0);
	}
	
	@Test
	public void testCreateAllocationTuner() {
		Map<String, Object> allocation = AllocationFactory.createTunerAllocation();
		
		/*
		 * Test to make sure the appropriate default allocation is set up 
		 */
		allocation.put(AllocationFactory.ALLOCATION_ID, allocationId);
		
		assertEquals(defaultTunerAllocation, allocation);
		
		/*
		 * Test to make sure users can create custom allocations
		 */
		allocationId = UUID.randomUUID().toString();
		allocation = AllocationFactory.createTunerAllocation(10.0, "test", "FOO_BAR", 12.0, "flowId", 
				4.0, allocationId, false, 7.0, 7.0);
	
		Map<String, Object> customAllocation = defaultTunerAllocation; 

		customAllocation.put(AllocationFactory.SAMPLE_RATE_TOLERANCE, 4.0);
		customAllocation.put(AllocationFactory.GROUP_ID, "test");
		customAllocation.put(AllocationFactory.TUNER_TYPE, "FOO_BAR");
		customAllocation.put(AllocationFactory.BANDWIDTH, 12.0);
		customAllocation.put(AllocationFactory.RF_FLOW_ID, "flowId");
		customAllocation.put(AllocationFactory.ALLOCATION_ID, allocationId);
		customAllocation.put(AllocationFactory.SAMPLE_RATE, 10.0);
		customAllocation.put(AllocationFactory.DEVICE_CONTROL, false);
		customAllocation.put(AllocationFactory.CENTER_FREQUENCY, 7.0);	
		customAllocation.put(AllocationFactory.BANDWIDTH_TOLERANCE, 7.0);
		
		assertEquals(customAllocation, allocation);
	}
	
	@Test
	public void testCreateTunerGenericListenerAllocation() {
		Map<String, Object> obj = AllocationFactory.createTunerGenericListenerAllocation();
		Boolean deviceControl = (Boolean) obj.get(AllocationFactory.DEVICE_CONTROL);
		assertTrue(!deviceControl);
	}
	
	@Test
	public void testCreateTunerListenerAllocation() {
		Map<String, Object> obj = AllocationFactory.createTunerListenerAllocation("hello", "world");
		
		//Make sure two properties are set 
		assertEquals(2, obj.size());
		
		//Make sure appropriate keys are available
		assertTrue(obj.containsKey(AllocationFactory.LISTENER_ALLOCATION_ID));
		assertTrue(obj.containsKey(AllocationFactory.LISTENER_EXISTING_ALLOCATION_ID));	
	}
}
