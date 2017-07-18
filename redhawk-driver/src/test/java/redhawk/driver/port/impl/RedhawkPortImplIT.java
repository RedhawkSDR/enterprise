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
package redhawk.driver.port.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.component.RedhawkComponent;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.PortException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.port.RedhawkPort;
import redhawk.testutils.RedhawkTestBase;

public class RedhawkPortImplIT extends RedhawkTestBase{
	private static RedhawkApplication application; 
	
	@BeforeClass
	public static void setupRedhawkUsesPort(){
		try {
			application = driver.getDomain().createApplication("myApp", "/waveforms/rh/basic_components_demo/basic_components_demo.sad.xml");
		} catch (Exception e) {
			e.printStackTrace();
			fail("Issue at startup "+e.getMessage());
		}
	}
	
	@Test
	public void testUsesPortStatistics(){
		try {
			RedhawkComponent comp = driver.getComponent("REDHAWK_DEV/myApp/HardLimit.*");
			
			//Checks to make sure you're able to retrieve Uses Port Statistics
			assertNotNull(comp.getPort("dataFloat_out").getPortStatistics());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Issue w/ test "+e.getMessage());
		}
	}
	
	@Test
	public void testProvidesPortStatistics(){
		RedhawkComponent comp;
		try {
			comp = driver.getComponent("REDHAWK_DEV/myApp/HardLimit.*");
		
			//Checks to make sure you're able to retrieve Provides Port Statistics
			assertNotNull(comp.getPort("dataFloat_in").getPortStatistics());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Issue w/ test "+e.getMessage());
		}
	}
	
	@Test
	public void testGetActiveSRIs(){
		try{
			//Start app so SRI is present 
			application.start();
			RedhawkPort port = driver.getPort("REDHAWK_DEV/myApp/HardLimit.*/dataFloat_in");
			assertNotNull(port.getActiveSRIs());
		}catch(Exception ex){
			ex.printStackTrace();
			fail("Test failure "+ex.getMessage());
		}
		
		//Make sure Uses Port actually throws Exception
		try {
			RedhawkPort port = driver.getPort("REDHAWK_DEV/myApp/HardLimit.*/dataFloat_out");
			port.getActiveSRIs();
			fail("Exception should've been thrown Uses port does not have activeSRIs");
		} catch (ResourceNotFoundException | MultipleResourceException | CORBAException e) {
			e.printStackTrace();
			fail("Test failure "+e.getMessage());
		} catch (PortException e) {
			assertTrue("Expected exception thrown", true);
		}

	}
	
	@Test
	public void getPortState(){
		RedhawkPort port;
		try {
			port = driver.getPort("REDHAWK_DEV/myApp/HardLimit.*/dataFloat_in");
		
			//Checks to make sure port State is not null
			assertNotNull(port.getPortState());
		} catch (ResourceNotFoundException | MultipleResourceException | CORBAException | PortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Test failure "+e.getMessage());
		} 
		
		try {
			port = driver.getPort("REDHAWK_DEV/myApp/HardLimit.*/dataFloat_out");
			port.getPortState();
			fail("Exception should've been thrown Uses port does not have state");
		} catch (ResourceNotFoundException | MultipleResourceException | CORBAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PortException e) {
			assertTrue("Expected exception thrown", true);
		}
		
	}
	
	@Test
	public void getPortConnections(){
		RedhawkComponent comp = null;
		try {
			comp = driver.getComponent("REDHAWK_DEV/myApp/HardLimit.*");

			RedhawkPort port = comp.getPort("dataFloat_out");
			assertTrue("Should be atleast 1 connection id", !port.getConnectionIds().isEmpty());
		} catch (ResourceNotFoundException | MultipleResourceException | CORBAException | PortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Test failure "+e.getMessage());
		}
		
		try {
			RedhawkPort port = comp.getPort("dataFloat_in");
			port.getConnectionIds();
			fail("Exception should've been thrown Provides port does not have connections");
		} catch (ResourceNotFoundException | MultipleResourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Test failure "+e.getMessage());
		} catch (PortException e) {
			assertTrue("Expected exception thrown", true);
		}
	}
	
	@Test
	public void testGetPortConnections(){
		RedhawkComponent comp = null;
		try {
			comp = driver.getComponent("REDHAWK_DEV/myApp/HardLimit.*");

			RedhawkPort port = comp.getPort("dataFloat_out");
			assertTrue("Should be atleast 1 connection id", !port.getConnectionIds().isEmpty());
			
			for(String connectionId : port.getConnectionIds()){
				port.disconnect(connectionId);
			}
			
			assertTrue("Should no longer be any connections", port.getConnectionIds().isEmpty());
		} catch (ResourceNotFoundException | MultipleResourceException | CORBAException | PortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Test failure "+e.getMessage());
		}
	}
}
