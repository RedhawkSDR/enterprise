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

import java.io.File;
import java.util.Map;
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

public class RedhawkDriverTestIT {
	private Logger logger = Logger.getLogger(RedhawkDriverTestIT.class.getName());
	
	private String domainName;
	
	private String hostName; 
	
	private int domainPort;
	
	private RedhawkDriver driver; 
	
	private static RedhawkDriver rhDriver; 
	
	private static String sampleApp = "sampleApp";
	
	@BeforeClass
	public static void setupApp() throws ResourceNotFoundException, ApplicationCreationException, CORBAException{
		rhDriver = new RedhawkDriver("localhost", 2809);
		rhDriver.getDomain("REDHAWK_DEV").createApplication(sampleApp, "/waveforms/rh/basic_components_demo/basic_components_demo.sad.xml");
	}
	
	@Before
	public void setup(){
		domainName = "REDHAWK_DEV";
		hostName = "localhost";
		domainPort = 2809;
	}
	
	@Test
	public void testDefaultConstructor() throws CORBAException, ResourceNotFoundException, MultipleResourceException{
		driver = new RedhawkDriver();
		
		this.basicDriverTests(driver);
	}
	
	@Test
	public void testOneArgConstructor() throws CORBAException, ResourceNotFoundException, MultipleResourceException{
		driver = new RedhawkDriver(hostName);
		
		this.basicDriverTests(driver);
	}
	
	@Test
	public void testTwoArgConstructor() throws CORBAException, ResourceNotFoundException, MultipleResourceException{
		driver = new RedhawkDriver(hostName, domainPort);
		
		this.basicDriverTests(driver);
	}
	
	@Test
	public void testGetDeviceManager() throws ResourceNotFoundException, CORBAException, MultipleResourceException{
		//START SNIPPET: rhdrivergetdevicemanager
		driver = new RedhawkDriver();
		
		String deviceManagerName = driver.getDomain("REDHAWK_DEV").getDeviceManagers().get(0).getName();
		String pathForDevManager = "REDHAWK_DEV/"+deviceManagerName;
		logger.info(pathForDevManager);
		assertNotNull(driver.getDeviceManager(pathForDevManager));
		//END SNIPPET: rhdrivergetdevicemanager
	}
	
	@Test
	public void testGetDevice() throws ResourceNotFoundException, CORBAException, MultipleResourceException{
		//START SNIPPET: rhdrivergetDevice
		driver = new RedhawkDriver();
		RedhawkDeviceManager devManager = driver.getDomain("REDHAWK_DEV").getDeviceManagers().get(0);
		RedhawkDevice device = devManager.getDevices().get(0);
		String pathForDevice = domainName+File.separator+devManager.getName()+File.separator+device.getName();
		logger.info(pathForDevice);
		assertNotNull(driver.getDevice(pathForDevice));
		//END SNIPPET: rhdrivergetDevice
	}
	
	@Test
	public void testHelperMethods() throws MultipleResourceException, CORBAException{
		//START SNIPPET: rhdriverhelpers
		RedhawkDriver driver = new RedhawkDriver(); 
		
		//Use these utility methods if you only have one REDHAWK Domain/Redhawk Device Manager/Redhawk Device
		RedhawkDomainManager domainManager = driver.getDomain();
		
		RedhawkDeviceManager deviceManager = driver.getDeviceManager();
		
		RedhawkDevice device = driver.getDevice();
		//END SNIPPET: rhdriverhelpers
		
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
		assertEquals(hostName, driver.getHostName());
		assertEquals(domainPort, driver.getPort());
		assertNotNull(driver.getOrb());
		
		Map<String, RedhawkDomainManager> domainMap = driver.getDomains();
		
		//Ensure expected Number of domains returned and correct domain name 
		assertEquals(1, domainMap.size());
		assertEquals(domainName, domainMap.keySet().iterator().next());
		
		//Get a specific domain
		//START SNIPPET: rhdrivergetdomain
		RedhawkDomainManager domain = driver.getDomain(domainName);
		//END SNIPPET: rhdrivergetdomain
		assertNotNull(domain);
		assertEquals(domainName, domain.getName());
		
		//Get a specific application
		//START SNIPPET: rhdrivergetapplication
		RedhawkApplication application = driver.getApplication(domainName+"/"+sampleApp);
		//END SNIPPET: rhdrivergetapplication
		assertNotNull(application);
		assertEquals(sampleApp, application.getName());
		
		//Get a specific component
		//START SNIPPET: rhdrivergetcomponent
		RedhawkComponent component = driver.getComponent(domainName+"/"+sampleApp+"/SigGen_sine.*");
		//END SNIPPET: rhdrivergetcomponent
		assertNotNull(component);
		
		//Get a specific port
		//START SNIPPET: rhdrivergetport
		RedhawkPort port = driver.getPort(domainName+"/"+sampleApp+"/SigGen_sine.*/dataFloat_out");
		//END SNIPPET: rhdrivergetport
		assertNotNull(port);		
	}
	
	@After
	public void shutdown(){
		if(driver!=null)
			driver.disconnect();
		
		//TODO: Ask John why getOrb initializes an Orb??? 
		//assertEquals(null, driver.getOrb());
	}
	
	@AfterClass
	public static void tearDownApp() throws MultipleResourceException, ResourceNotFoundException, ApplicationReleaseException, CORBAException{
		rhDriver.getDomain("REDHAWK_DEV").getApplicationByName(sampleApp).release();
	}
}
