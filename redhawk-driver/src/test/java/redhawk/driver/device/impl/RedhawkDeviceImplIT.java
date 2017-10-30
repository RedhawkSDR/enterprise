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
package redhawk.driver.device.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import redhawk.driver.allocation.AllocationFactory;
import redhawk.driver.device.AdminState;
import redhawk.driver.device.RedhawkDevice;
import redhawk.driver.devicemanager.RedhawkDeviceManager;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.port.RedhawkPort;
import redhawk.testutils.RedhawkDeviceTestBase;

public class RedhawkDeviceImplIT extends RedhawkDeviceTestBase {
	@Test
	public void testAllocate() throws MultipleResourceException, CORBAException, ResourceNotFoundException {
		//Get Device Manager
		RedhawkDeviceManager deviceManager = driver.getDomain().getDeviceManagerByName("Simulator.*");
		
		//Get Device you want 
		RedhawkDeviceImpl device = (RedhawkDeviceImpl) deviceManager.getDeviceByName("FmRdsSimulator.*");
		
				
		//Section 7.1 REDHAWK Manual for these properties
		//Create Allocation Map 
		String allocId = "myTestAllocationId";
		Map<String, Object> allocation = AllocationFactory.createTunerAllocation();
		allocation.put(AllocationFactory.CENTER_FREQUENCY, 101100000d);
		allocation.put(AllocationFactory.SAMPLE_RATE, 256000d);
		allocation.put(AllocationFactory.BANDWIDTH_TOLERANCE, 20.0);
		allocation.put(AllocationFactory.SAMPLE_RATE_TOLERANCE, 20.0);
		allocation.put(AllocationFactory.ALLOCATION_ID, allocId);
		
		//Allocate Device
		device.allocate(allocation);
		
		//Should now have a used tuner
		assertEquals(false, device.getUsedTuners().isEmpty());
		
		//Should now be no unused tuners
		assertEquals(true, device.getUnusedTuners().isEmpty());
		
		//Check to make sure center frequency is correct 
		assertEquals(101100000d, device.getUsedTuners().get(0).get("FRONTEND::tuner_status::center_frequency"));
		
		//Deallocate Device
		device.deallocate(allocId);
		
		//Reverse the last test
		assertEquals(true, device.getUsedTuners().isEmpty()); //TODO: Fix this you should be able to call this and get an empty list
		
		//Should now be no unused tuners
		assertEquals(false, device.getUnusedTuners().isEmpty());
	}
	
	@Test
	public void testAllocate2() throws MultipleResourceException, CORBAException, ResourceNotFoundException {
		//Get Device Manager
		RedhawkDeviceManager deviceManager = driver.getDomain().getDeviceManagerByName("Simulator.*");
		
		//Get Device you want 
		RedhawkDeviceImpl device = (RedhawkDeviceImpl) deviceManager.getDeviceByName("FmRdsSimulator.*");
						
		//Section 7.1 REDHAWK Manual for these properties
		//Create Allocation Map 
		String allocId = "myTestAllocationId";
		Map<String, Object> allocation = AllocationFactory.createTunerAllocation();
		allocation.put(AllocationFactory.CENTER_FREQUENCY, 101100000d);
		allocation.put(AllocationFactory.SAMPLE_RATE, 256000d);
		allocation.put(AllocationFactory.BANDWIDTH_TOLERANCE, 20.0);
		allocation.put(AllocationFactory.SAMPLE_RATE_TOLERANCE, 20.0);
		allocation.put(AllocationFactory.ALLOCATION_ID, allocId);
		
		//Allocate Device
		device.allocate(allocation);
		
		//Should now have an allocation Id
		assertEquals("Should now have one allocation.", 1, device.getAllocIds().size());
		
		//Should now have a used tuner
		assertEquals(false, device.getUsedTuners().isEmpty());
		
		//Should now be no unused tuners
		assertEquals(true, device.getUnusedTuners().isEmpty());
		
		//Check to make sure center frequency is correct 
		assertEquals(101100000d, device.getUsedTuners().get(0).get("FRONTEND::tuner_status::center_frequency"));
		
		//Deallocate Device
		device.deallocate(allocId);
		
		//Reverse the last test
		assertEquals(true, device.getUsedTuners().isEmpty()); //TODO: Fix this you should be able to call this and get an empty list
		
		//Should now be no unused tuners
		assertEquals(false, device.getUnusedTuners().isEmpty());
	}
	
	@Test
	public void testGetAllocIds(){
		//Get Device Manager
		RedhawkDeviceManager deviceManager;
		try {
			deviceManager = driver.getDomain().getDeviceManagerByName("Simulator.*");

			//Get Device you want 
			RedhawkDeviceImpl device = (RedhawkDeviceImpl) deviceManager.getDeviceByName("FmRdsSimulator.*");
			assertTrue("Allocations should be empty", device.getAllocIds().isEmpty());
		} catch (MultipleResourceException | ResourceNotFoundException | CORBAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	//TODO: Add tests for managing a devices lifecycle
	@Test
	public void testAdminStateOperationalStateAndUsageState() {
		try {
			deviceManager = driver.getDomain().getDeviceManagerByName("Simulator.*");

			//Get Device you want 
			RedhawkDevice device = deviceManager.getDeviceByName("FmRdsSimulator.*");					
			
			//Check retrieving state
			assertNotNull(device.adminState());
			assertEquals(AdminState.UNLOCKED, device.adminState());
			
			//Check setting state
			device.adminState(AdminState.LOCKED);
			assertEquals(AdminState.LOCKED, device.adminState());
			
			//Put back to normal
			device.adminState(AdminState.UNLOCKED);
			assertEquals(AdminState.UNLOCKED, device.adminState());
			
			assertNotNull(device.operationalState());
			
			assertNotNull(device.usageState());
		
			assertNotNull(device.getImplementation());
		} catch (MultipleResourceException | ResourceNotFoundException | CORBAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAccessingDevicePorts() {
		try {
			deviceManager = driver.getDomain().getDeviceManagers().get(0);
		
			RedhawkDevice device = deviceManager.getDevices().get(0);
			
			List<RedhawkPort> ports = device.getPorts();
			assertTrue(!ports.isEmpty());
			
			System.out.println("HELLO!!!!!!!!! "+device.getCorbaObj().softwareProfile());
			for(RedhawkPort port : ports) {
				assertNotNull(device.getPort(port.getName()));
			}
		} catch (MultipleResourceException | CORBAException | ResourceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
