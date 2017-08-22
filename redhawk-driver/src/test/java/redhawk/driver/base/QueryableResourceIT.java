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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Map;

import javax.xml.stream.FactoryConfigurationError;

import org.junit.Test;

import redhawk.driver.device.RedhawkDevice;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.properties.RedhawkProperty;
import redhawk.testutils.RedhawkTestBase;

public class QueryableResourceIT extends RedhawkTestBase{
	@Test
	public void testQueryDeviceProperties() {
		try {
			RedhawkDevice device = driver.getDomain().getDeviceByName("GPP.*");
			
			String[] propNames = new String[] {"device_kind", "loadAverage"};
			Map<String, RedhawkProperty> prop = device.getProperty(propNames);
			
			assertTrue("Properties should not be empty", !prop.isEmpty());
			assertEquals("Should be 2 elements", 2, prop.size());
		} catch (MultipleResourceException | CORBAException | FactoryConfigurationError e) {
			e.printStackTrace();
			fail("Test failure "+e.getMessage());
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
}