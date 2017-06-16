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
package redhawk.driver.allocationmanager;

import java.util.List;
import java.util.Map;

import CF.AllocationManager;
import redhawk.driver.device.RedhawkDevice;
import redhawk.driver.exceptions.AllocationException;

/**
 * provides a single point for creating, inspecting, and delegating
 * allocations
 *
 */
public interface RedhawkAllocationManager {
	/**
	 * Get all allocations from a domain
	 * 
	 * @return
	 */
	List<AllocationInfo> getAllocations();
	
	/**
	 * Allocate a Device
	 * @param deviceId
	 * 	String representing the Device Id
	 * @param allocationType
	 * 	String representing the allocation type
	 * @param allocation
	 * 	Map representing the allocation
	 * @throws AllocationException
	 */
	void allocate(String deviceId, String allocationType, Map<String, Object> allocation) throws AllocationException;
	
	/**
	 * Deallocate a device by allocation Id
	 * @param allocationId
	 */
	void deallocate(String allocationId);
	
	/**
	 * Deallocate several devices by allocation Id
	 * @param allocationIds
	 */
	void deallocate(List<String> allocationIds);
	
	/**
	 * List devices available in a Domain
	 * @return
	 */
	List<RedhawkDevice> listDevices();
	
	/**
	 * CorbaObj representing the AllocationManager
	 * @return
	 */
	AllocationManager getCorbaObj();
}
