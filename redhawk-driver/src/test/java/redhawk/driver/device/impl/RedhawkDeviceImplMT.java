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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import redhawk.driver.devicemanager.RedhawkDeviceManager;
import redhawk.driver.domain.RedhawkFileManager;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.EventChannelCreationException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.testutils.RedhawkDeviceTestBase;
import redhawk.testutils.RedhawkTestBase;

/*
 * This is currently a manual test(MT) until I figure out how to automatically launch a node from the REDHAWK Driver. 
 * 
 * Test relies on the SimulatorNode in src/test/resources being available from the DeviceManager on your domain. 
 *TODO: Make this work with mvn clean install -P localIT
 */
public class RedhawkDeviceImplMT extends RedhawkDeviceTestBase {
	@Test
	public void testAllocate() throws MultipleResourceException, CORBAException, ResourceNotFoundException {
		//Get Device Manager
		RedhawkDeviceManager deviceManager = driver.getDomain().getDeviceManagerByName("SimulatorNode");
		
		//Get Device you want 
		RedhawkDeviceImpl device = (RedhawkDeviceImpl) deviceManager.getDeviceByName("FmRdsSimulator.*");
		
		//Create Allocation Map 
		Map<String, Object> newAlloc = new HashMap<>();
				
		//Section 7.1 REDHAWK Manual for these properties
		/*Example tuner status. 
		 * {
		 * FRONTEND::tuner_status::rf_flow_id=, 
		 * FRONTEND::tuner_status::bandwidth=2280000.0, 
		 * FRONTEND::tuner_status::stream_id=MyStreamID, 
		 * FRONTEND::tuner_status::center_frequency=1.011E8, 
		 * FRONTEND::tuner_status::group_id=, 
		 * FRONTEND::tuner_status::gain=0.0, 
		 * FRONTEND::tuner_status::enabled=true, 
		 * FRONTEND::tuner_status::sample_rate=285000.0, 
		 * FRONTEND::tuner_status::allocation_id_csv=default:55d211b1-c35c-44a8-837c-96a7470089f0, 
		 * FRONTEND::tuner_status::tuner_type=RX_DIGITIZER
		 * }
		 * 
		 * params from IDE:
		 * 
		*/
		String allocId = "myTestAllocationId";
		newAlloc.put("FRONTEND::tuner_allocation::allocation_id", allocId);
		newAlloc.put("FRONTEND::tuner_allocation::tuner_type", "RX_DIGITIZER");
		newAlloc.put("FRONTEND::tuner_allocation::center_frequency", 101100000d);//101.1e6
		newAlloc.put("FRONTEND::tuner_allocation::sample_rate", 256000d);//256e3
		newAlloc.put("FRONTEND::tuner_allocation::bandwidth_tolerance", 20.0);
		newAlloc.put("FRONTEND::tuner_allocation::sample_rate_tolerance", 20.0);
		
		//Allocate Device
		device.allocate(newAlloc);
		
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
		RedhawkDeviceManager deviceManager = driver.getDomain().getDeviceManagerByName("SimulatorNode");
		
		//Get Device you want 
		RedhawkDeviceImpl device = (RedhawkDeviceImpl) deviceManager.getDeviceByName("FmRdsSimulator.*");
		
		//Create Allocation Map 
		Map<String, Object> newAlloc = new HashMap<>();
				
		//Section 7.1 REDHAWK Manual for these properties
		/*Example tuner status. 
		 * {
		 * FRONTEND::tuner_status::rf_flow_id=, 
		 * FRONTEND::tuner_status::bandwidth=2280000.0, 
		 * FRONTEND::tuner_status::stream_id=MyStreamID, 
		 * FRONTEND::tuner_status::center_frequency=1.011E8, 
		 * FRONTEND::tuner_status::group_id=, 
		 * FRONTEND::tuner_status::gain=0.0, 
		 * FRONTEND::tuner_status::enabled=true, 
		 * FRONTEND::tuner_status::sample_rate=285000.0, 
		 * FRONTEND::tuner_status::allocation_id_csv=default:55d211b1-c35c-44a8-837c-96a7470089f0, 
		 * FRONTEND::tuner_status::tuner_type=RX_DIGITIZER
		 * }
		 * 
		 * params from IDE:
		 * 
		*/
		String allocId = "myTestAllocationId";
		newAlloc.put("FRONTEND::tuner_allocation::allocation_id", allocId);
		newAlloc.put("FRONTEND::tuner_allocation::tuner_type", "RX_DIGITIZER");
		newAlloc.put("FRONTEND::tuner_allocation::center_frequency", 101100000);//101.1e6
		newAlloc.put("FRONTEND::tuner_allocation::sample_rate", 256000);//256e3
		newAlloc.put("FRONTEND::tuner_allocation::bandwidth_tolerance", 20);
		newAlloc.put("FRONTEND::tuner_allocation::sample_rate_tolerance", 20);
		
		//Allocate Device
		device.allocate(newAlloc);
		
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
			
			System.out.println(device.getStatus());
		} catch (MultipleResourceException | ResourceNotFoundException | CORBAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	//TODO: Add tests for managing a devices lifecycle
}
