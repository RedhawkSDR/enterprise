package redhawk.driver.device;

import java.util.Map;

import CF.Device;
import CF.ResourcePackage.StartError;
import CF.ResourcePackage.StopError;
import redhawk.driver.base.PortBackedObject;
import redhawk.driver.devicemanager.RedhawkDeviceManager;

public interface RedhawkDevice extends PortBackedObject {
	Device getCorbaObj();
	RedhawkDeviceManager getDeviceManager();
	String getName();
	String getIdentifier();
	void start() throws StartError;
	boolean started();
	void stop() throws StopError;

	/**
	 * Allocates a single resources on the device
	 * 
	 * @param type
	 *            the struct sequence id to use for the allocation map, e.g., FRONTEND::tuner_allocation for a frontend tuner
	 * @param allocation
	 *            the map of allocation properties to request
	 * @return the allocationId of a successful allocation, or null if the allocation was unsuccessful
	 */
	boolean allocate(String type, Map<String, Object> allocation);
	//boolean allocate(RedhawkStructSequence allocation);  //need to be able to create RedhawkStructSequence without a backing corba object

	/**
	 * Deallocates a single resources on the device
	 * 
	 * @param type
	 *            the struct sequence id to use for the allocation map, e.g., FRONTEND::tuner_allocation for a frontend tuner
	 * @param allocation
	 *            the map of allocation properties to deallocate
	 */
	void deallocate(String type, Map<String, Object> allocation);
	//void deallocate(RedhawkStructSequence allocation);

	/**
	 * Allocates a single frontend resources on the device. Convenience method to more easily allocate devices.
	 * 
	 * @param allocation
	 *            the map of allocation properties to request. if an allocationId is provided, it will be used, otherwise a UUID will be
	 *            generated for the allocatinId
	 * @return the allocationId of a successful allocation, or null if the allocation was unsuccessful
	 */
	String allocate(Map<String, Object> allocation);

	// List<String> allocate(List<Map<String, Object>> allocations);

	/**
	 * Deallocates a single resources on the device. Convenience method to more easily deallocate devices.
	 * 
	 * @param allocationId
	 *            the id of a current allocation that is to be deallocated
	 */
	void deallocate(String allocationId);
	
	void deallocate(Map<String, Object> allocation);

	// void deallocate(List<String> allocationIds);	
	
	
}