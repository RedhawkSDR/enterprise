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
package redhawk.driver.allocationmanager.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import CF.AllocationManager;
import CF.AllocationManagerHelper;
import CF.DataType;
import CF.Device;
import CF.DeviceLocationIteratorHolder;
import CF.DeviceManager;
import CF.AllocationManagerPackage.AllocationError;
import CF.AllocationManagerPackage.AllocationRequestType;
import CF.AllocationManagerPackage.AllocationStatusType;
import CF.AllocationManagerPackage.DeviceLocationSequenceHolder;
import CF.AllocationManagerPackage.DeviceLocationType;
import CF.AllocationManagerPackage.DeviceScopeType;
import CF.AllocationManagerPackage.InvalidAllocationId;
import redhawk.driver.RedhawkUtils;
import redhawk.driver.allocationmanager.AllocationInfo;
import redhawk.driver.allocationmanager.RedhawkAllocationManager;
import redhawk.driver.base.impl.CorbaBackedObject;
import redhawk.driver.device.RedhawkDevice;
import redhawk.driver.device.impl.RedhawkDeviceImpl;
import redhawk.driver.devicemanager.RedhawkDeviceManager;
import redhawk.driver.devicemanager.impl.RedhawkDeviceManagerImpl;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.exceptions.AllocationException;
import redhawk.driver.exceptions.ResourceNotFoundException;

public class RedhawkAllocationManagerImpl extends CorbaBackedObject<AllocationManager> implements RedhawkAllocationManager{

	private static Logger logger = Logger.getLogger(RedhawkAllocationManagerImpl.class.getName());

	private AllocationManager allocationManager;
	
	private RedhawkDomainManager domainManager;
	
	
	public RedhawkAllocationManagerImpl(RedhawkDomainManager domMgr, AllocationManager allocationMgr){
		super(domMgr.getDriver().getOrb().object_to_string(allocationMgr), domMgr.getDriver().getOrb());
		domainManager = domMgr;
		this.allocationManager = allocationMgr;
	}

	@Override
	public List<AllocationInfo> getAllocations() {
		List<AllocationInfo> allocations = new ArrayList<>();
		try {
			AllocationStatusType[] allocationStatus = allocationManager.allocations(new String[0]);
		
			for(AllocationStatusType allocStatus : allocationStatus){
				AllocationInfo alloc = new AllocationInfo();
				
				alloc.setAllocatedDeviceId(allocStatus.allocatedDevice.identifier());
				alloc.setAllocationId(allocStatus.allocationID);
				alloc.setDeviceManagerId(allocStatus.allocationDeviceManager.identifier());
				alloc.setSourceId(allocStatus.sourceID);
				alloc.setRequestingDomain(allocStatus.requestingDomain);
				alloc.setAllocationProperties(RedhawkUtils.convertDataTypeArrayToMap(allocStatus.allocationProperties));
				allocations.add(alloc);
			}
		} catch (InvalidAllocationId e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return allocations;
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
		List<String> allocations = new ArrayList<>(); 
		allocations.add(allocationId);
		
		this.deallocate(allocations);
	}
	
	@Override
	public void deallocate(List<String> allocationIds) {
		// TODO Auto-generated method stub
		try {
			allocationManager.deallocate(allocationIds.toArray(new String[allocationIds.size()]));
		} catch (InvalidAllocationId e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public List<RedhawkDevice> listDevices() {
		List<RedhawkDevice> devices = new ArrayList<>();
		DeviceLocationSequenceHolder holder = new DeviceLocationSequenceHolder();
		DeviceLocationIteratorHolder iterHold = new DeviceLocationIteratorHolder();
		Map<String, RedhawkDeviceManager> devMgrs = new HashMap<>();
		
		//TODO: Shouldn't need to put in a number????
		allocationManager.listDevices(DeviceScopeType.ALL_DEVICES, 1000, holder, iterHold);
		
		
		
		for(DeviceLocationType location : holder.value){
			//TODO: Should I really need to create a devManager object
			Device dev = location.dev;
			DeviceManager devMgr = location.devMgr;
			RedhawkDeviceManager rhDevMgr = devMgrs.get(devMgr.identifier());
			
			//If not in method scoped cache create
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
		String ior = this.getIor();
		return AllocationManagerHelper.narrow(getOrb().string_to_object(ior));
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
}