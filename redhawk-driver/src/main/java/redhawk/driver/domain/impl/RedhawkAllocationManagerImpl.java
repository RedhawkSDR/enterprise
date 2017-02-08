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
package redhawk.driver.domain.impl;

import java.util.logging.Logger;

import CF.AllocationManager;
import CF.AllocationManagerPackage.DeviceLocationType;
import redhawk.driver.domain.RedhawkAllocationManager;

public class RedhawkAllocationManagerImpl implements RedhawkAllocationManager  {

	private static Logger logger = Logger.getLogger(RedhawkAllocationManagerImpl.class.getName());

	private AllocationManager allocationManager;
	
	public RedhawkAllocationManagerImpl(AllocationManager allocationManager){
		this.allocationManager = allocationManager;
		
	}
	
	public void getLocalDevices() {
		for(DeviceLocationType d : allocationManager.localDevices()){
//			d.dev;
//			d.devMgr;
//			d.pools;
//			RedhawkDevice dd = new RedhawkDeviceImpl(d.devMgr, deviceIor, identifier)
		}
		
		
	}
	
	
	
	
//	  /* The readonly AllocationManager attribute allDevices contains all devices in all Domains that can be seen by any Allocation Manager seen by the local Allocation Manager */
//	  CF.AllocationManagerPackage.DeviceLocationType[] allDevices ();
//
//	  /* The readonly AllocationManager attribute authorizedDevices contains all devices after policy is applied by any Allocation Manager seen by the local Allocation Manager */
//	  CF.AllocationManagerPackage.DeviceLocationType[] authorizedDevices ();
//
//	  /* The readonly AllocationManager attribute localDevices contains all devices that are located within the local Domain */
//	  CF.AllocationManagerPackage.DeviceLocationType[] localDevices ();
//
//	  /* Lists up to 'count' devices within the given scope (local or all Domains). If there are more remaining, the out iterator can be used to fetch additional allocations. */
//	  void listDevices (CF.AllocationManagerPackage.DeviceScopeType deviceScope, int count, CF.AllocationManagerPackage.DeviceLocationSequenceHolder devices, CF.DeviceLocationIteratorHolder dl);
//
//	  /* Allocates a set of depenedencies */
//	  CF.AllocationManagerPackage.AllocationResponseType[] allocate (CF.AllocationManagerPackage.AllocationRequestType[] requests) throws CF.AllocationManagerPackage.AllocationError;
//
//	  /* Allocates a set of dependencies only inside the local Domain */
//	  CF.AllocationManagerPackage.AllocationResponseType[] allocateLocal (CF.AllocationManagerPackage.AllocationRequestType[] requests, String domainName) throws CF.AllocationManagerPackage.AllocationError;
//
//	  /* Deallocates a set of allocations */
//	  void deallocate (String[] allocationIDs) throws CF.AllocationManagerPackage.InvalidAllocationId;
//
//	  /* Returns all current allocations on all Domains with an optional list of selected allocations to return */
//	  CF.AllocationManagerPackage.AllocationStatusType[] allocations (String[] allocationIDs) throws CF.AllocationManagerPackage.InvalidAllocationId;
//
//	  /* Returns all current allocations that were made through the Allocation Manager that have not been deallocated with an optional list of selected allocations to return */
//	  CF.AllocationManagerPackage.AllocationStatusType[] localAllocations (String[] allocationIDs) throws CF.AllocationManagerPackage.InvalidAllocationId;
//
//	  /* Lists up to 'count' current allocations within the given scope (local or all Domains). If there are more remaining, the out iterator can be used to fetch additional allocations. */
//	  void listAllocations (CF.AllocationManagerPackage.AllocationScopeType allocScope, int how_many, CF.AllocationManagerPackage.AllocationStatusSequenceHolder allocs, CF.AllocationStatusIteratorHolder ai);	
	
	
	
	
	
	
}