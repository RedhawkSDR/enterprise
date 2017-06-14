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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import org.omg.CORBA.ORB;

import CF.AllocationManager;
import CF.AllocationManagerHelper;
import CF.DataType;
import CF.Device;
import CF.DeviceLocationIteratorHolder;
import CF.DeviceManager;
import CF.AllocationManagerPackage.AllocationError;
import CF.AllocationManagerPackage.AllocationRequestType;
import CF.AllocationManagerPackage.DeviceLocationSequenceHolder;
import CF.AllocationManagerPackage.DeviceLocationType;
import CF.AllocationManagerPackage.DeviceScopeType;
import redhawk.driver.RedhawkDriver;
import redhawk.driver.RedhawkUtils;
import redhawk.driver.base.impl.CorbaBackedObject;
import redhawk.driver.device.RedhawkDevice;
import redhawk.driver.device.impl.RedhawkDeviceImpl;
import redhawk.driver.devicemanager.RedhawkDeviceManager;
import redhawk.driver.devicemanager.impl.RedhawkDeviceManagerImpl;
import redhawk.driver.domain.RedhawkAllocationManager;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.exceptions.AllocationException;
import redhawk.driver.exceptions.ResourceNotFoundException;

public class RedhawkAllocationManagerImpl extends CorbaBackedObject<AllocationManager> implements RedhawkAllocationManager{

	private static Logger logger = Logger.getLogger(RedhawkAllocationManagerImpl.class.getName());

	private AllocationManager allocationManager;
	
	private RedhawkDomainManager domainManager;
	
	private Map<String, RedhawkDeviceManager> devMgrs = new HashMap<>();
	
	private ORB orb;
	
	public RedhawkAllocationManagerImpl(RedhawkDomainManager domMgr, AllocationManager mgr){
		super(domMgr.getDriver().getOrb().object_to_string(mgr), domMgr.getDriver().getOrb());
		domainManager = domMgr;
		this.orb = domMgr.getDriver().getOrb();
		this.allocationManager = mgr;
	}
	
	public void getLocalDevices() {
		for(DeviceLocationType d : allocationManager.localDevices()){
//			d.dev;
//			d.devMgr;
//			d.pools;
//			RedhawkDevice dd = new RedhawkDeviceImpl(d.devMgr, deviceIor, identifier)
		}
		
		
	}

	@Override
	public List<Map<String, Object>> getAllocations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void allocate(String deviceId, String allocationType, Map<String, Object> allocation) throws AllocationException {
		DataType[] allocationProperties  = new DataType[1];
		Device[] devices = new Device[1];
		AllocationRequestType[] allocations = new AllocationRequestType[1];
		
		allocationProperties[0] = new DataType(allocationType, RedhawkUtils.createAny(getOrb(), allocation));
		devices[0] = domainManager.getDeviceByIdentifier(deviceId).getCorbaObj();
	
		AllocationRequestType type = new AllocationRequestType(); 
		type.allocationProperties = allocationProperties;
		type.requestedDevices = devices;
		type.devicePools = new String[0];
		
		//TODO: Do something other than UUID
		type.requestID = UUID.randomUUID().toString();
		type.sourceID = UUID.randomUUID().toString();
		
		allocations[0] = type;
		try {
			allocationManager.allocate(allocations);
		} catch (AllocationError e) {
			throw new AllocationException("Exception allocating device "+deviceId+" with these properties: "+allocation, e);
		}
	}

	@Override
	public void deallocate(String allocationId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dellocate(String[] allocationIds) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public List<RedhawkDevice> listDevices() {
		List<RedhawkDevice> devices = new ArrayList<>();
		DeviceLocationSequenceHolder holder = new DeviceLocationSequenceHolder();
		DeviceLocationIteratorHolder iterHold = new DeviceLocationIteratorHolder();
		
		//TODO: Shouldn't need to put in a number????
		allocationManager.listDevices(DeviceScopeType.ALL_DEVICES, 1000, holder, iterHold);
		
		for(DeviceLocationType location : holder.value){
			//TODO: Should I really need to create a devManager object
			Device dev = location.dev;
			DeviceManager devMgr = location.devMgr;
			RedhawkDeviceManager rhDevMgr = devMgrs.get(devMgr.identifier());
			
			//If not in cache create
			if(rhDevMgr==null){
				rhDevMgr = new RedhawkDeviceManagerImpl(domainManager, getOrb().object_to_string(devMgr), devMgr.identifier());
				devMgrs.put(devMgr.identifier(), rhDevMgr);
			}
			
			devices.add(new RedhawkDeviceImpl(rhDevMgr, getOrb().object_to_string(dev), dev.identifier()));
		}
		
		return devices;
	}

	@Override
	protected AllocationManager locateCorbaObject() throws ResourceNotFoundException {
		return null;
	}

	@Override
	public Class<?> getHelperClass() {
		// TODO Auto-generated method stub
		return AllocationManagerHelper.class;
	}

	@Override
	public AllocationManager getCorbaObj() {
		return getCorbaObject();
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