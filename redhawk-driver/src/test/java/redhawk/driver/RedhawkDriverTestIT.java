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

import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.omg.CORBA.ORBPackage.InvalidName;

import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.component.RedhawkComponent;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.ApplicationReleaseException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.port.RedhawkPort;

public class RedhawkDriverTestIT {
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
	
	@After
	public void shutdown(){
		driver.disconnect();
		//TODO: Ask John why getOrb initializes an Orb??? 
		//assertEquals(null, driver.getOrb());
	}
	
	@AfterClass
	public static void tearDownApp() throws MultipleResourceException, ResourceNotFoundException, ApplicationReleaseException, CORBAException{
		rhDriver.getDomain("REDHAWK_DEV").getApplicationByName(sampleApp).release();
	}
}
