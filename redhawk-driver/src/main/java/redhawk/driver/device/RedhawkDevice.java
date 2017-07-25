/*
 * This file is protected by Copyright. Please refer to the COPYRIGHT file
 * distributed with this source distribution.
 *
 * This file is part of REDHAWK __REDHAWK_PROJECT__.
 *
 * REDHAWK __REDHAWK_PROJECT__ is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * REDHAWK __REDHAWK_PROJECT__ is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package redhawk.driver.device;

import java.util.Map;

import CF.Device;
import CF.LifeCyclePackage.ReleaseError;
import CF.ResourcePackage.StartError;
import CF.ResourcePackage.StopError;
import redhawk.driver.base.PortBackedObject;
import redhawk.driver.devicemanager.RedhawkDeviceManager;
import redhawk.driver.exceptions.ConnectionException;

public interface RedhawkDevice extends PortBackedObject {
	/**
	 * @return CORBA object representing a Device. 
	 */
	Device getCorbaObj();

	/**
	 * @return Device Manager for a device. 
	 */
	RedhawkDeviceManager getDeviceManager();
	
	/**
	 * Get the name of a Device. 
	 * 
	 * @return
	 */
	String getName();

	/**
	 * Get the identifier for a Device. 
	 * @return
	 */
	String getIdentifier();
	
	/**
	 * Start a device. 
	 * @throws StartError
	 */
	void start() throws StartError;
	
	/**
	 * Checks if a Device is started.  
	 * @return
	 */
	boolean started();
	
	/**
	 * Stop a device. 
	 * @throws StopError
	 */
	void stop() throws StopError;
	
	/**
	 * Release a device
	 * @throws ConnectionException 
	 * @throws ReleaseError 
	 */
	void release() throws ReleaseError, ConnectionException;

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
	
	/**
	 * Deallocates a multiple resources on the device.
	 * @param allocation
	 * 	{@link java.util.Map} with the allocations you'd like to get rid of. 
	 */
	//TODO: Test this method so you can better document. 
	void deallocate(Map<String, Object> allocation);
	
	
	/**
	 * Returns the adminState of a Device 
	 * @return
	 */
	AdminState adminState();
	
	/**
	 * Sets the adminState of a Device
	 * @param state
	 */
	void adminState(AdminState state);
}