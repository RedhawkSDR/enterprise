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
package redhawk.driver;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.util.Map;
import java.util.logging.Logger;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import CF.Resource;
import redhawk.RedhawkTestBase;
import redhawk.driver.Redhawk;
import redhawk.driver.RedhawkDriver;
import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.component.RedhawkComponent;
import redhawk.driver.device.RedhawkDevice;
import redhawk.driver.devicemanager.RedhawkDeviceManager;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.port.RedhawkPort;

@Ignore("Some of this functionality would need to be in a system test. Other parts can be mocked...")
public class RedhawkDriverTest extends RedhawkTestBase {

	@Test
	public void testConstructorOne() {
		Redhawk redhawk = new RedhawkDriver();
		assertEquals(redhawk.getHostName(), "localhost");
		assertEquals(redhawk.getPort(), 2809);
	}
	
	@Test
	public void testConstructorTwo() {
		Redhawk redhawk = new RedhawkDriver("127.0.0.1");
		assertEquals(redhawk.getHostName(), "127.0.0.1");
		assertEquals(redhawk.getPort(), 2809);
	}

	@Test
	public void testConstructorThree() {
		Redhawk redhawk = new RedhawkDriver("192.168.1.1", 3333);
		assertEquals(redhawk.getHostName(), "192.168.1.1");
		assertEquals(redhawk.getPort(), 3333);
	}
	
	@Test
	public void testConstructorThreeWithOSGI() {
		System.setProperty("redbus.base","testValue");
		Redhawk redhawk = new RedhawkDriver("192.168.1.1", 3333);
		assertEquals(redhawk.getHostName(), "192.168.1.1");
		assertEquals(redhawk.getPort(), 3333);
		assertEquals(System.getProperty("jacorb.classloaderpolicy"), "forname");
	}
	
	@Test
	public void testToString() {
		System.out.println(redhawk.toString());
		String driverString = "RedhawkDriver [hostName=127.0.0.1, port=2809]";
		assertEquals(redhawk.toString(), driverString);
	}	
	
	@Test
	public void testGetDomains() throws CORBAException {
		Map<String, RedhawkDomainManager> domains = redhawk.getDomains();
		assertTrue(domains != null);
		assertTrue(domains.get("TEST_REDHAWK_DEV") != null);
	}
	
	@Test
	public void testGetDomain() throws ConnectionException, ResourceNotFoundException, CORBAException {
		RedhawkDomainManager domain = redhawk.getDomain("TEST_REDHAWK_DEV");
		assertTrue(domain != null);
	}
	
	@Test(expected=ResourceNotFoundException.class)
	public void testNotFoundDomain() throws ConnectionException, ResourceNotFoundException, CORBAException {
		redhawk.getDomain("TEST_REDHAWK_DEV_SDFSD");
	}	

	@Test
	public void testGetApplicationByExactName() throws ResourceNotFoundException, MultipleResourceException, CORBAException {
		RedhawkApplication application = redhawk.getApplication("REDHAWK_DEV/TestWF_014_165211993");
		assertTrue(application != null);
	}

	@Test
	public void testGetApplicationByRegexName() throws ResourceNotFoundException, MultipleResourceException, CORBAException {
		RedhawkApplication application = redhawk.getApplication("REDHAWK_DEV/TestWF.*");
		assertTrue(application != null);
	}

	@Test(expected=ResourceNotFoundException.class)
	public void testGetApplicationByNameNotFound() throws ResourceNotFoundException, MultipleResourceException, CORBAException {
		redhawk.getApplication("REDHAWK_DEV/DFSDSDF");
	}
	
	@Test(expected=ResourceNotFoundException.class)
	public void testGetApplicationWithUnknownDomain() throws ResourceNotFoundException, MultipleResourceException, CORBAException {
		redhawk.getApplication("REDHAWK/TestWF.*");
	}
	
	@Test(expected=ResourceNotFoundException.class)
	public void testGetApplicationWithInvalidUrl() throws ResourceNotFoundException, MultipleResourceException, CORBAException {
		redhawk.getApplication("TestWF.*");
	}
	
	@Test
	public void testGetApplicationById() throws ResourceNotFoundException, MultipleResourceException, CORBAException {
		RedhawkApplication application = redhawk.getApplication("REDHAWK_DEV/DCE:5df26659-7f17-4446-8b14-d013b2f0a890.*");
		assertTrue(application != null);
	}

	@Test
	public void testGetDeviceManager() throws ResourceNotFoundException, MultipleResourceException, CORBAException  {
		RedhawkDeviceManager dm = redhawk.getDeviceManager("REDHAWK_DEV/DevMgr_uwk.*");
		assertTrue(dm != null);
	}
	
	@Test
	public void testGetComponent() throws ResourceNotFoundException, MultipleResourceException, CORBAException  {
		RedhawkComponent cmp = redhawk.getComponent("REDHAWK_DEV/TestWF.*/psd_1.*");
		assertTrue(cmp != null);
	}
	
	@Test(expected=ResourceNotFoundException.class)
	public void testGetComponentWithBadUri() throws ResourceNotFoundException, MultipleResourceException, CORBAException  {
		RedhawkComponent cmp = redhawk.getComponent("REDHAWK_DEV/TestWF.*");
		assertTrue(cmp != null);
	}	
	
	
	@Test
	public void testGetDevice() throws ResourceNotFoundException, MultipleResourceException, CORBAException  {
		RedhawkDevice dev = redhawk.getDevice("REDHAWK_DEV/DevMgr_uwk.*/GPP.*");
		assertTrue(dev != null);
	}
	
	@Test(expected=ResourceNotFoundException.class)
	public void testGetDeviceWithWrongName() throws ResourceNotFoundException, MultipleResourceException, CORBAException  {
		RedhawkDevice dev = redhawk.getDevice("REDHAWK_DEV/DevMgr_uwk.*/DEVICE.*");
		assertTrue(dev != null);
	}	
	
	@Test
	public void testGetDevicePort() throws ResourceNotFoundException, MultipleResourceException, CORBAException  {
		RedhawkPort port = redhawk.getPort("REDHAWK_DEV/DevMgr.*/GPP.*/propEvent");
		assertTrue(port != null);
	}
	
	@Test(expected=ResourceNotFoundException.class)
	public void testGetDevicePortWithUnknownName() throws ResourceNotFoundException, MultipleResourceException, CORBAException  {
		RedhawkPort port = redhawk.getPort("REDHAWK_DEV/DevMgr.*/GPP.*/somePort");
		assertTrue(port != null);
	}	
	
	@Test(expected=ResourceNotFoundException.class)
	public void testGetDevicePortWithBadUri() throws ResourceNotFoundException, MultipleResourceException, CORBAException  {
		RedhawkPort port = redhawk.getPort("REDHAWK_DEV/DevMgr.*");
		assertTrue(port != null);
	}	
	
	@Test
	public void testGetComponentPort() throws ResourceNotFoundException, MultipleResourceException, CORBAException  {
		RedhawkPort port = redhawk.getPort("REDHAWK_DEV/TestWF.*/psd_1.*/psd_dataFloat_out");
		assertTrue(port != null);
	}
	
}