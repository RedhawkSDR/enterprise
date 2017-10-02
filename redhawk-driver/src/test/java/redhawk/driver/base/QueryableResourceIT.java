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
package redhawk.driver.base;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.component.RedhawkComponent;
import redhawk.driver.device.RedhawkDevice;
import redhawk.driver.devicemanager.RedhawkDeviceManager;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.properties.RedhawkProperty;
import redhawk.driver.properties.RedhawkPropertyTestBase;
import redhawk.driver.properties.RedhawkSimple;
import redhawk.driver.properties.RedhawkSimpleSequence;
import redhawk.driver.properties.RedhawkStruct;
import redhawk.driver.properties.RedhawkStructSequence;

public class QueryableResourceIT extends RedhawkPropertyTestBase{	
	static RedhawkApplication basicComponentDemo;
	
	@BeforeClass
	public static void setupQueryableResourceIT() {
		try {
			basicComponentDemo = driver.getDomain(domainName).createApplication(appName, "/waveforms/rh/basic_components_demo/basic_components_demo.sad.xml");
						
			assertNotNull(basicComponentDemo);
		} catch (ApplicationCreationException | CORBAException | ResourceNotFoundException e) {
			fail("Unable to setup test "+e.getMessage());
		}		
	}
	
	/*
	 * Test getting a property by it's optional(name) ensure that mapping from name to id is being done. 
	 */
	@Test
	public void testGetPropertyByName() {
		List<RedhawkDeviceManager> deviceManagers = manager.getDeviceManagers();
	
		for(RedhawkDeviceManager devManager : deviceManagers) {
			RedhawkDevice device = devManager.getDevices().get(0);
			RedhawkProperty prop = device.getProperty("device_kind");
			
			//Making sure device_kind defaults to id
			assertNotNull(prop);
		}
	}
	
	@Test
	public void testSetRedhawkSimple() throws Exception {
		try {
			//Set simple property on a DomainManager
			RedhawkDomainManager manager = driver.getDomain();
			
			manager.setProperty("COMPONENT_BINDING_TIMEOUT", 120);
			RedhawkSimple simple = manager.getProperty("COMPONENT_BINDING_TIMEOUT");
			assertEquals(new Long(120), simple.getValue());
			
			//Set simple property on a DeviceManager
			//System.out.println(manager.getDevice);
			RedhawkDeviceManager deviceManager = manager.getDeviceManagerByName("DevMgr.*");
			deviceManager.setProperty("CLIENT_WAIT_TIME", 12000);
			simple = deviceManager.getProperty("CLIENT_WAIT_TIME");
			assertEquals(new Long(12000), simple.getValue());
			
			//Set simple property on a Device
			//TODO: Probably need to explicitly get device by name
			RedhawkDevice device = deviceManager.getDevices().get(0);
			device.setProperty("reserved_capacity_per_component", .05);
			simple = device.getProperty("reserved_capacity_per_component");
			assertEquals(new Float(.05), simple.getValue());			
			
			//Set simple property on a Application
			basicComponentDemo.setProperty("sample_rate", 14000);
			simple = basicComponentDemo.getProperty("sample_rate");
			assertEquals(new Double(14000), simple.getValue());
			
			//Set simple property on a Component
			RedhawkComponent component = basicComponentDemo.getComponentByName("SigGen_sine.*");
			component.setProperty("magnitude", 75);
			simple = component.getProperty("magnitude");
			assertEquals(new Double(75), simple.getValue());
		} catch (MultipleResourceException | CORBAException e) {
			e.printStackTrace();
			fail("Unable to run test "+e.getMessage());
		}
	}
	
	@Test
	public void testQueryDevicePropertiesKnown() {
		RedhawkDevice device;
		try {
			device = driver.getDomain().getDeviceByName("GPP.*");
		
			String[] propNames = new String[] {"utilization", "nic_interfaces"};
			
			Map<String, RedhawkProperty> prop = device.getProperty(propNames);

			assertTrue("Properties should not be empty", !prop.isEmpty());
			assertEquals("Should be 2 elements", 2, prop.size());
		} catch (MultipleResourceException | CORBAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testSetRedhawkSimpleSequence() throws Exception {
		String[] simpleSequence = new String[] {"please", "excuse", "my", "dear", "aunt", "sally"};
		component.setProperty("examples", simpleSequence);
		
		RedhawkSimpleSequence seq = component.getProperty("examples");
		assertArrayEquals(simpleSequence, seq.getValues().toArray());
	}
	
	@Test
	public void testSetRedhawkStruct() throws Exception {
		RedhawkStruct struct = component.getProperty("cartoon_character");
		
		Map<String, Object> structValue = struct.getValue();
		structValue.put("age", new Double(12));
		structValue.put("friend", "Mr. Krabs");
		
		
		component.setProperty("cartoon_character", structValue);
		
		struct = component.getProperty("cartoon_character");
		assertEquals(structValue, struct.getValue());
	}
	
	@Test
	public void testSetRedhawkStructSequence() throws Exception {
		RedhawkStructSequence structSeq = component.getProperty("main_characters");
		
		List<Map<String, Object>> value = structSeq.getValue();
		Map<String, Object> obj = new HashMap<>();
		obj.put("actor_name", "Johnny Depp");
		obj.put("actor_country", "Clifford");
		value.add(obj);
		
		component.setProperty("main_characters", value);
		structSeq = component.getProperty("main_characters");
		assertEquals(value, structSeq.getValue());
	}
}
