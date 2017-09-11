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
package redhawk.driver.logging;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.component.RedhawkComponent;
import redhawk.driver.device.RedhawkDevice;
import redhawk.driver.devicemanager.RedhawkDeviceManager;
import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.ApplicationReleaseException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.testutils.RedhawkDeviceTestBase;

public class RedhawkLoggingIT extends RedhawkDeviceTestBase {
	@Test
	public void testDomainLogLevels() throws MultipleResourceException, CORBAException {
		try {
			// Expecting Domain log level to be at INFO
			assertEquals(RedhawkLogLevel.INFO, driver.getDomain().getLogLevel());

			// Set log level to something else
			driver.getDomain().setLogLevel(RedhawkLogLevel.ALL);
			assertEquals(RedhawkLogLevel.ALL, driver.getDomain().getLogLevel());
		} catch (MultipleResourceException | CORBAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// Reset defaults
			driver.getDomain().setLogLevel(RedhawkLogLevel.INFO);
		}
	}


	@Test
	public void testApplicationLogLevels() throws ApplicationReleaseException {
		String appName = "myApp";
		RedhawkApplication application = null;

		try {
			application = driver.getDomain().createApplication(appName,
					"/waveforms/rh/basic_components_demo/basic_components_demo.sad.xml");

			assertEquals(RedhawkLogLevel.INFO, application.getLogLevel());

			// Change log level 
			application.setLogLevel(RedhawkLogLevel.DEBUG);
			assertEquals(RedhawkLogLevel.DEBUG, application.getLogLevel());
		} catch (MultipleResourceException | ApplicationCreationException | CORBAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Uanble to run test because " + e.getMessage());
		} finally {
			// Reset defaults and release
			if (application != null) {
				application.setLogLevel(RedhawkLogLevel.INFO);
				application.release();
			}
		}
	}

	@Test
	public void testComponentLogLevels() throws ApplicationReleaseException {
		String appName = "myApp";
		RedhawkApplication application = null;
		RedhawkComponent component = null;
		try {
			application = driver.getDomain().createApplication(appName,
					"/waveforms/rh/basic_components_demo/basic_components_demo.sad.xml");
			component = application.getComponents().get(0);

			assertEquals(RedhawkLogLevel.INFO, component.getLogLevel());

			// Change log level 
			component.setLogLevel(RedhawkLogLevel.ALL);
			assertEquals(RedhawkLogLevel.ALL, component.getLogLevel());
		} catch (MultipleResourceException | ApplicationCreationException | CORBAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Uanble to run test because " + e.getMessage());
		} finally {
			if (component != null) {
				component.setLogLevel(RedhawkLogLevel.INFO);
			}
			if (application != null) {
				application.release();
			}
		}
	}

	@Test
	public void testDeviceLogLevels() throws IOException {
		RedhawkDeviceManager deviceManager = null;
		RedhawkDevice dev = null;
		
		try {
			deviceManager = driver.getDeviceManager("REDHAWK_DEV/Simulator.*");

			dev = deviceManager.getDevices().get(0);
			assertEquals(RedhawkLogLevel.INFO, dev.getLogLevel());

			// Change log level 
			dev.setLogLevel(RedhawkLogLevel.TRACE);
			assertEquals(RedhawkLogLevel.TRACE, dev.getLogLevel());
		} catch (ResourceNotFoundException | MultipleResourceException
				| CORBAException ex) {
			ex.printStackTrace();
			fail("Failure running test " + ex.getMessage());
		}finally {
			logger.info("Reseting log level");
			dev.setLogLevel(RedhawkLogLevel.INFO);
		}
	}
	
	@Test
	public void testDeviceManagerLog(){
		try {
			RedhawkDeviceManager devManager = driver.getDomain().getDeviceManagers().get(0);
			
			try{
				devManager.getLogLevel();
				fail("Should have thrown an UnsupportedOperationException");
			}catch(UnsupportedOperationException ex){
			}
			
			try{
				devManager.setLogLevel(RedhawkLogLevel.ALL);
				fail("Should have thrown an UnsupportedOperationException");
			}catch(UnsupportedOperationException ex){
			}
		} catch (MultipleResourceException | CORBAException e) {
			e.printStackTrace();
			fail("Unable to run test "+e.getMessage());
		}
	}

}
