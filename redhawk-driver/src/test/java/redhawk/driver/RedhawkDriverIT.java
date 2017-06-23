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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.omg.CORBA.ORBPackage.InvalidName;

import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.component.RedhawkComponent;
import redhawk.driver.device.RedhawkDevice;
import redhawk.driver.devicemanager.RedhawkDeviceManager;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.ApplicationReleaseException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.port.RedhawkPort;
import redhawk.testutils.RedhawkTestBase;

public class RedhawkDriverIT extends RedhawkTestBase{
	private static Logger logger = Logger.getLogger(RedhawkDriverIT.class.getName());
		
	private static String sampleApp = "sampleApp";
	
	@BeforeClass
	public static void setupApp() throws ResourceNotFoundException, ApplicationCreationException, CORBAException{
		driver.getDomain("REDHAWK_DEV").createApplication(sampleApp, "/waveforms/rh/basic_components_demo/basic_components_demo.sad.xml");
	}

	@Test
	public void testTwoArgConstructor() throws CORBAException, ResourceNotFoundException, MultipleResourceException{		
		this.basicDriverTests(driver);
	}
	
	@Test
	public void testDisconnect(){
		try {
			driver.getDomain();
			driver.disconnect();
		} catch (MultipleResourceException | CORBAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetDomain(){
		try {
			RedhawkDomainManager domain = driver.getDomain(domainName);
			assertNotNull(domain);
		} catch (ResourceNotFoundException | CORBAException e) {
			fail("Unable to use driver to get domain "+e.getMessage());
		}
	}
	
	@Test
	public void testGetDeviceManager() throws ResourceNotFoundException, CORBAException, MultipleResourceException{		
		String deviceManagerName = driver.getDomain("REDHAWK_DEV").getDeviceManagers().get(0).getName();
		//Path to dev Manager
		String pathForDevManager = domainName+"/"+deviceManagerName;
		logger.info(pathForDevManager);
		
		RedhawkDeviceManager deviceManager = driver.getDeviceManager(pathForDevManager);
		assertNotNull(deviceManager);
	}
	
	@Test
	public void testGetDevice() throws ResourceNotFoundException, CORBAException, MultipleResourceException{
		RedhawkDeviceManager devManager = driver.getDomain("REDHAWK_DEV").getDeviceManagers().get(0);
		RedhawkDevice tDevice = devManager.getDevices().get(0);
		String deviceName = tDevice.getName();
		
		//Path to device
		String pathForDevice = domainName+File.separator+devManager.getName()+File.separator+deviceName;
		logger.info(pathForDevice);
		
		RedhawkDevice device = driver.getDevice(pathForDevice);
		assertNotNull(device);
	}
	
	@Test
	public void testHelperMethods() throws MultipleResourceException, CORBAException{		
		//Use these utility methods if you only have one REDHAWK Domain/Redhawk Device Manager/Redhawk Device
		RedhawkDomainManager domainManager = driver.getDomain();
		
		RedhawkDeviceManager deviceManager = driver.getDeviceManager();
		
		RedhawkDevice device = driver.getDevice();
		
		assertNotNull(domainManager);
		assertNotNull(deviceManager);
		assertNotNull(device);
		driver.disconnect();
	}
	
	/*
	 * Testing of the public RedhawkDriver methods occurs below
	 */
	private void basicDriverTests(RedhawkDriver driver) throws CORBAException, ResourceNotFoundException, MultipleResourceException{
		//Ensure default port and host and ORB are returned correctly
		assertEquals(domainHost, driver.getHostName());
		assertEquals(domainPort.intValue(), driver.getPort());
		//assertEquals(domainPort, driver.getPort());
		assertNotNull(driver.getOrb());
		
		Map<String, RedhawkDomainManager> domainMap = driver.getDomains();
		
		//Ensure expected Number of domains returned and correct domain name 
		assertEquals(1, domainMap.size());
		assertEquals(domainName, domainMap.keySet().iterator().next());
		
		//Get a specific domain
		RedhawkDomainManager domain = driver.getDomain(domainName);
		assertNotNull(domain);
		assertEquals(domainName, domain.getName());
		
		//Get a specific application
		RedhawkApplication application = driver.getApplication(domainName+"/"+sampleApp);
		assertNotNull(application);
		assertEquals(sampleApp, application.getName());
		
		//Get a specific component
		RedhawkComponent component = driver.getComponent(domainName+"/"+sampleApp+"/SigGen_sine.*");
		assertNotNull(component);
		
		//Get a specific port
		RedhawkPort port = driver.getPort(domainName+"/"+sampleApp+"/SigGen_sine.*/dataFloat_out");
		assertNotNull(port);		
	}
	
	@AfterClass
	public static void tearDownApp() throws MultipleResourceException, ResourceNotFoundException, ApplicationReleaseException, CORBAException{
		driver.getDomain("REDHAWK_DEV").getApplicationByName(sampleApp).release();
	}
}
