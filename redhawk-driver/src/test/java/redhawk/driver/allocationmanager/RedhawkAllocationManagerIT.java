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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import redhawk.driver.device.RedhawkDevice;
import redhawk.driver.exceptions.AllocationException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.testutils.RedhawkDeviceTestBase;

public class RedhawkAllocationManagerIT extends RedhawkDeviceTestBase{
	private static RedhawkAllocationManager allocMgr;
		
	private final String allocationType = "FRONTEND::tuner_allocation";
	
	@BeforeClass
	public static void setupAllocationManager(){
		try {
			allocMgr = driver.getDomain().getAllocationManager();
		} catch (MultipleResourceException | CORBAException e) {
			fail("Unable to get allocation manager "+e.getMessage());
		}
	}
	
	@Test
	public void testGetCorbaObj(){
		//Test to make sure you can access corba obj from Domain Manager
		assertNotNull(allocMgr.getCorbaObj());
	}
	
	@Test
	public void testListDevices(){
		//List Devices that the allocation manager is aware of. 
		List<RedhawkDevice> devices = allocMgr.listDevices();
		
		for(RedhawkDevice dev : devices){
			System.out.println(dev);
		}
		
		assertEquals("Should be a simulator and GPP available", 2, devices.size());
	}
	
	@Test
	public void testAllocateAndDeallocateADevice(){
		//Allocate a device
		this.allocateDevice();
		
		//List the allocated devices
		this.getAllocations();
		
		//Deallocate Device
		this.deallocate();
	}
	
	public void deallocate(){
		//Use allocation manager to get a list of Allocations 
		List<AllocationInfo> allocation = allocMgr.getAllocations();
		
		Integer allocNum = allocation.size();
		
		assertTrue("Needs to be an allocation to deallocate ", allocNum>0);
		String allocationId = allocMgr.getAllocations().get(0).getAllocationId();
		
		//Deallocate a specific allocation
		allocMgr.deallocate(allocationId);
		assertEquals("Should be less allocations than original amount", allocNum-1, allocMgr.getAllocations().size());	
	}
	
	public void allocateDevice(){
		try {
			//Get device from Device Manager
			RedhawkDevice device = driver.getDeviceManager("REDHAWK_DEV/Simulator.*").getDevices().get(0);
			
			String identifier = device.getIdentifier();
			
			//Perform allocation
			allocMgr.allocate(identifier, allocationType, this.getAllocationProperties());
		} catch (ResourceNotFoundException | MultipleResourceException | CORBAException | AllocationException e) {
			fail("Issue allocating device "+e.getMessage());
		}
	}
	
	public void getAllocations(){
		List<AllocationInfo> allocations = allocMgr.getAllocations();
		
		//Check to make sure no null objects
		for(AllocationInfo info : allocations){
			try {
				nullObjectCheck(info);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	private Map<String, Object> getAllocationProperties(){
		Map<String, Object> newAlloc = new HashMap<>();
		
		String allocId = "myTestAllocationId";
		newAlloc.put("FRONTEND::tuner_allocation::allocation_id", allocId);
		newAlloc.put("FRONTEND::tuner_allocation::tuner_type", "RX_DIGITIZER");
		newAlloc.put("FRONTEND::tuner_allocation::center_frequency", 101100000d);//101.1e6
		newAlloc.put("FRONTEND::tuner_allocation::sample_rate", 256000d);//256e3
		newAlloc.put("FRONTEND::tuner_allocation::bandwidth_tolerance", 20.0);
		newAlloc.put("FRONTEND::tuner_allocation::sample_rate_tolerance", 20.0);		
	
		return newAlloc;
	}
	
	
	private void nullObjectCheck(Object obj) throws IllegalArgumentException, IllegalAccessException{
		for(Field f : obj.getClass().getFields()){
			f.setAccessible(true);
			if(f.get(obj)==null)
				fail("All fields in this object "+obj.getClass().getName()+" Should be set.");
		}
	}
}
