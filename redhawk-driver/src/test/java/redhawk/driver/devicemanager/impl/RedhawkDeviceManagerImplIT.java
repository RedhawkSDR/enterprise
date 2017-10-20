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
package redhawk.driver.devicemanager.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import redhawk.driver.devicemanager.RedhawkDeviceManager;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.testutils.RedhawkTestBase;

public class RedhawkDeviceManagerImplIT extends RedhawkTestBase {
	@Test
	public void test() throws MultipleResourceException, CORBAException, Exception {
		// Create a device manager
		RedhawkDeviceManager manager = driver.getDomain().createDeviceManager("anotherDeviceManager",
				"/var/redhawk/sdr/dev/", false);

		Thread.sleep(10000l);
		manager.shutdown();
	}

	@Test
	public void testShutdownOfDeviceManager() {
		// Create a device manager
		RedhawkDeviceManager manager;
		RedhawkDomainManager domMgr = null;
		try {
			domMgr = driver.getDomain();
			manager = domMgr.createDeviceManager("anotherDeviceManager", "/var/redhawk/sdr/dev/", false);

			System.out.println(domMgr.getDriverRegisteredDeviceManagers());
			manager.shutdown();

			domMgr.unRegisterAllDriverRegisteredDeviceManagers();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testDeviceMethods() {
		/*
		 * Generic test for additional device methods
		 */
		try {
			RedhawkDeviceManager devMgr = driver.getDomain().getDeviceManagers().get(0);

			assertNotNull(devMgr.deviceConfigurationProfile());
			assertNotNull(devMgr.getComponentImplemantation());
		} catch (MultipleResourceException | CORBAException e) {
			fail("Test failure "+e.getMessage());
		}
	}
	
	@Test
	public void testGetDeviceManagerProperties() {
		try {
			RedhawkDeviceManager devMgr = driver.getDomain().getDeviceManagers().get(0);

			assertNotNull(devMgr.getProperties());
		} catch (MultipleResourceException | CORBAException e) {
			fail("Test failure "+e.getMessage());
		}
	}
}
