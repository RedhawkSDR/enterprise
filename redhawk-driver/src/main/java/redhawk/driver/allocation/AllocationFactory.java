package redhawk.driver.allocation;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AllocationFactory {
	private static final String FRONTEND = "FRONTEND::";
	
	private static final String TUNER_ALLOCATION = "tuner_allocation::";
		
	private static final String LISTENER_ALLOCATION = "listener_allocation::";
	
	public static final String TUNER_ALLOCATION_TYPE = FRONTEND+"tuner_allocation";
	
	public static final String LISTENER_ALLOCATION_TYPE = FRONTEND+"listener_allocation";
	
	public static final String SAMPLE_RATE = FRONTEND+TUNER_ALLOCATION+"sample_rate";
	
	public static final String TUNER_TYPE = FRONTEND+TUNER_ALLOCATION+"tuner_type";
	
	public static final String BANDWIDTH = FRONTEND+TUNER_ALLOCATION+"bandwidth";
	
	public static final String RF_FLOW_ID = FRONTEND+TUNER_ALLOCATION+"rf_flow_id";
	
	public static final String ALLOCATION_ID = FRONTEND+TUNER_ALLOCATION+"allocation_id";
	
	public static final String DEVICE_CONTROL = FRONTEND+TUNER_ALLOCATION+"device_control";
	
	public static final String CENTER_FREQUENCY = FRONTEND+TUNER_ALLOCATION+"center_frequency";
	
	public static final String BANDWIDTH_TOLERANCE = FRONTEND+TUNER_ALLOCATION+"bandwidth_tolerance";
	
	public static final String SAMPLE_RATE_TOLERANCE = FRONTEND+TUNER_ALLOCATION+"sample_rate_tolerance";
	
	public static final String GROUP_ID = FRONTEND+TUNER_ALLOCATION+"group_id";
	
	public static final String LISTENER_EXISTING_ALLOCATION_ID = FRONTEND+LISTENER_ALLOCATION+"existing_allocation_id";
	
	public static final String LISTENER_ALLOCATION_ID = FRONTEND+LISTENER_ALLOCATION+"listener_allocation_id";
	
	/**
	 * Creates a default tuner allocation with the following properties
	 * 
	 * {
	 * 	FRONTEND::tuner_allocation::sample_rate_tolerance : 0.0, 
	 * 	FRONTEND::tuner_allocation::group_id : '',
	 * 	FRONTEND::tuner_allocation::tuner_type: 'RX_DIGITIZER',
	 * 	FRONTEND::tuner_allocation::bandwidth: 0.0,
	 * 	FRONTEND::tuner_allocation::rf_flow_id: '',
	 * 	FRONTEND::tuner_allocation::sample_rate: 1.0,
	 * 	FRONTEND::tuner_allocation::allocation_id: <UUID>,
	 * 	FRONTEND::tuner_allocation::device_control: true,
	 * 	FRONTEND::tuner_allocation::center_frequency: 0.0,
	 * 	FRONTEND::tuner_allocation::bandwidth_tolerance: 0.0
	 * }
	 * @return
	 */
	public static Map<String, Object> createTunerAllocation() {
		return createTunerAllocation(null, null, null, null, null, null, null, null, null,
				null);
	}
	
	/**
	 * Creates a default tuner allocation with the following properties
	 * {
	 * 	FRONTEND::tuner_allocation::sample_rate_tolerance : 0.0, 
	 * 	FRONTEND::tuner_allocation::group_id : '',
	 * 	FRONTEND::tuner_allocation::tuner_type: 'RX_DIGITIZER',
	 * 	FRONTEND::tuner_allocation::bandwidth: 0.0,
	 * 	FRONTEND::tuner_allocation::rf_flow_id: '',
	 * 	FRONTEND::tuner_allocation::sample_rate: 1.0,
	 * 	FRONTEND::tuner_allocation::allocation_id: <UUID>,
	 * 	FRONTEND::tuner_allocation::device_control: false,
	 * 	FRONTEND::tuner_allocation::center_frequency: 0.0,
	 * 	FRONTEND::tuner_allocation::bandwidth_tolerance: 0.0	 
	 * }
	 * @return
	 */
	public static Map<String, Object> createTunerGenericListenerAllocation(){
		return createTunerAllocation(null, null, null, null, null, null, null, false, null,
				null);		
	}
	
	/**
	 * Create a generic tuner listener allocation w/ specified properties instead of defaults. Enter null if you want a property to be it's default. 
	 * 
	 * @param sampleRate
	 * @param groupId
	 * @param tunerType
	 * @param bandwidth
	 * @param rfFlowId
	 * @param sampleRateTolerance
	 * @param allocationId
	 * @param centerFrequency
	 * @param bandwidthTolerance
	 * @return
	 */
	public static Map<String, Object> createTunerGenericListenerAllocation(
			Double sampleRate, String groupId, String tunerType, Double bandwidth,
			String rfFlowId, Double sampleRateTolerance, String allocationId,
			Double centerFrequency, Double bandwidthTolerance){
		return createTunerAllocation(sampleRate, groupId, tunerType, bandwidth, rfFlowId, sampleRateTolerance, allocationId, false, centerFrequency,
				bandwidthTolerance);		
	}
	
	/**
	 * Create a tuner allocation w/ specified properties instead of defaults. Enter null 
	 * if you want a property to be it's default.
	 *  
	 * @param sampleRate
	 * @param groupId
	 * @param tunerType
	 * @param bandwidth
	 * @param rfFlowId
	 * @param sampleRateTolerance
	 * @param allocationId
	 * @param deviceControl
	 * @param centerFrequency
	 * @param bandwidthTolerance
	 * @return
	 */
	public static Map<String, Object> createTunerAllocation(
			Double sampleRate, String groupId, String tunerType, Double bandwidth,
			String rfFlowId, Double sampleRateTolerance, String allocationId, Boolean deviceControl,
			Double centerFrequency, Double bandwidthTolerance){
		Map<String, Object> tunerAllocation = new HashMap<>();
		
		if(sampleRate!=null)
			tunerAllocation.put(SAMPLE_RATE, sampleRate);
		else
			tunerAllocation.put(SAMPLE_RATE, 1.0);
		
		if(groupId!=null) 
			tunerAllocation.put(GROUP_ID, groupId);
		else
			tunerAllocation.put(GROUP_ID, "");
		
		if(tunerType!=null)
			tunerAllocation.put(TUNER_TYPE, tunerType); 
		else
			tunerAllocation.put(TUNER_TYPE, "RX_DIGITIZER"); //TODO: make this an enum 

			
		if(bandwidth!=null)
			tunerAllocation.put(BANDWIDTH, bandwidth);
		else
			tunerAllocation.put(BANDWIDTH, 0.0);
		
		if(rfFlowId!=null)
			tunerAllocation.put(RF_FLOW_ID, rfFlowId);
		else
			tunerAllocation.put(RF_FLOW_ID, "");			
		
		if(sampleRateTolerance!=null)
			tunerAllocation.put(SAMPLE_RATE_TOLERANCE, sampleRateTolerance);
		else
			tunerAllocation.put(SAMPLE_RATE_TOLERANCE, 0.0);
		
		if(allocationId!=null)
			tunerAllocation.put(ALLOCATION_ID, allocationId);
		else
			tunerAllocation.put(ALLOCATION_ID, UUID.randomUUID().toString());

		if(deviceControl!=null)
			tunerAllocation.put(DEVICE_CONTROL, deviceControl);
		else
			tunerAllocation.put(DEVICE_CONTROL, true);

		if(centerFrequency!=null)
			tunerAllocation.put(CENTER_FREQUENCY, centerFrequency);
		else
			tunerAllocation.put(CENTER_FREQUENCY, 0.0);
		
		if(bandwidthTolerance!=null)
			tunerAllocation.put(BANDWIDTH_TOLERANCE, bandwidthTolerance);
		else
			tunerAllocation.put(BANDWIDTH_TOLERANCE, 0.0);
		
		return tunerAllocation;
	}
	
	/**
	 * Creates a data structure for listener allocation 
	 * 
	 * @param existingAllocationId
	 * @param listenerAllocationId
	 * @return
	 */
	public static Map<String, Object> createTunerListenerAllocation(String existingAllocationId, String listenerAllocationId){
		Map<String, Object> tunerAllocation = new HashMap<>();
		
		tunerAllocation.put(LISTENER_EXISTING_ALLOCATION_ID, existingAllocationId);
		tunerAllocation.put(LISTENER_ALLOCATION_ID, listenerAllocationId);
		
		return tunerAllocation;
	}
	
	
}
