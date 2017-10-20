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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.xml.sax.SAXException;

import redhawk.driver.devicemanager.RedhawkDeviceManager;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.properties.RedhawkSimple;
import redhawk.driver.xml.ScaXmlProcessor;
import redhawk.driver.xml.model.sca.dcd.Deviceconfiguration;
import redhawk.driver.xml.model.sca.dmd.Domainmanagerconfiguration;
import redhawk.driver.xml.model.sca.prf.Properties;
import redhawk.driver.xml.model.sca.spd.Softpkg;
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
			
			//TODO: Check with CF team make sure this is a safe assumption
			assertTrue(!devMgr.getProperties().isEmpty());
		} catch (MultipleResourceException | CORBAException e) {
			fail("Test failure "+e.getMessage());
		}
	}
	
	@Test
	public void testGetDeviceManagerPropertyConfiguration() {
		try {
			RedhawkDeviceManager devMgr = driver.getDomain().getDeviceManagers().get(0);
			
			RedhawkSimple dcdURI = devMgr.getProperty("DCD_FILE");
			Deviceconfiguration dcd = unMarshall(devMgr.getFileSystem().getFile(dcdURI.getValue()), Deviceconfiguration.class);
			String spdURI = dcd.getDevicemanagersoftpkg().getLocalfile().getName();
			System.out.println(spdURI);
			Softpkg spd = unMarshall(devMgr.getFileSystem().getFile(spdURI), Softpkg.class);
			String prf = spd.getPropertyfile().getLocalfile().getName();
			System.out.println(prf);
			
			//TODO: Clean this up
			String prfURI = devMgr.getFileSystem().findFiles(prf).get(0);
			Properties properties = unMarshall(devMgr.getFileSystem().getFile(prfURI), Properties.class);
			
			System.out.println(properties);
		} catch (MultipleResourceException | CORBAException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private <T> T unMarshall(byte[] fileInBytes, Class clazz) throws IOException {
		try {
			return (T) ScaXmlProcessor.unmarshal(new ByteArrayInputStream(fileInBytes), clazz);
		} catch (JAXBException | SAXException e) {
			throw new IOException(e);
		} 
	}
}
