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
package redhawk.driver.thread;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.component.RedhawkComponent;
import redhawk.driver.device.RedhawkDevice;
import redhawk.driver.devicemanager.RedhawkDeviceManager;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ComponentStartException;
import redhawk.driver.exceptions.ComponentStopException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.logging.RedhawkLogLevel;
import redhawk.driver.port.RedhawkPort;
import redhawk.driver.port.impl.GenericPortListener;
import redhawk.testutils.RedhawkTestBase;

/**
 * Class for testing number of threads being leftover by driver methods to make
 * sure I'm cleaning up when I can it's users responsibility to call disconnect
 * when fully done w/ orb.
 */
public class CORBAThreadIT extends RedhawkTestBase {
	private static String appName = "myApp";

	private RedhawkApplication app;

	private RedhawkDomainManager dom;

	@BeforeClass
	public static void setupCORBAThreadResources() {
		try {
			// Create application
			driver.getDomain(domainName).createApplication(appName,
					"/waveforms/rh/basic_components_demo/basic_components_demo.sad.xml");
		} catch (ApplicationCreationException | CORBAException | ResourceNotFoundException e) {
			fail("Unable to launch application " + e.getMessage());
		}
	}

	@Before
	public void resetORB() throws MultipleResourceException, ResourceNotFoundException, CORBAException {
		// Get application
		dom = driver.getDomain(domainName);
		app = dom.getApplicationByName(appName);
	}

	@Test
	public void testDeviceManagerAndDeviceTC() throws InterruptedException {
		Integer originalTC = this.getThreadCount();

		try {
			RedhawkDeviceManager devMgr = dom.getDeviceManagers().get(0);
			RedhawkDevice dev = devMgr.getDevices().get(0);

			dev.started();
		} finally {
			driver.disconnect();
			Thread.sleep(1000);
			assertTrue("Making sure disconnect cleans all this up", originalTC >= this.getThreadCount());
		}
	}

	@Test
	public void testApplicationTC() throws InterruptedException {
		Integer originalTC = this.getThreadCount();
		try {
			// Interacting with any calls that may have something to do with
			// CORBA
			app.isAware();
			app.isStarted();
			app.getPorts();
			app.getComponents();

			// TC count should still be same as original
			assertEquals("Thread count should not have moved up", originalTC, this.getThreadCount());
		} catch (ResourceNotFoundException e) {
			fail("Test failure " + e.getMessage());
		} finally {
			driver.disconnect();
			Thread.sleep(1000l);

			// Make sure disconnect cleaned everything up
			assertTrue("Thread count should be same as original after", originalTC >= this.getThreadCount());
		}
	}

	@Test
	public void testComponentTC() throws InterruptedException {
		Integer originalTC = this.getThreadCount();
		List<RedhawkComponent> comps = app.getComponents();

		for (RedhawkComponent comp : comps) {
			try {
				comp.start();
				comp.started();
				comp.stop();
				RedhawkLogLevel level = comp.getLogLevel();
				comp.setLogLevel(level);
			} catch (ComponentStartException | ComponentStopException e) {
				fail("Unable to call component methods" + e.getMessage());
			}
		}

		// Sleeping to allow timeout
		Thread.sleep(1000l);
		assertTrue("Thread count should be back to normal and idle threads should be gone", originalTC>=
				this.getThreadCount());
	}

	@Test
	public void testPortTC() throws InterruptedException {
		Integer originalTC = this.getThreadCount();
		List<RedhawkComponent> comps = app.getComponents();

		try {
			// Components
			for (RedhawkComponent comp : comps) {
				// Ports
				for (RedhawkPort port : comp.getPorts()) {
					GenericPortListener pl = new GenericPortListener();

					if (!port.getType().equals(RedhawkPort.PORT_TYPE_PROVIDES)) {
						port.connect(pl);
						port.disconnect();
					}
				}
			}
			// TODO: Connecting a port with Jacorb will minimally create 9 threads it
			// appears.

			// Thread.sleep(5000l);
			// System.out.println("Latest TC: "+this.getThreadCount());
			// System.out.println(this.crunchifyGenerateThreadDump());
		} catch (Exception e) {
			fail("Test failure connecting/disconnecting from port " + e.getMessage());
		} finally {
			driver.disconnect();
			Thread.sleep(1000);

			assertTrue("Making sure disconnect cleans all this up", originalTC >= this.getThreadCount());
		}
	}

	@Test
	public void testTCDomainNonExplicit() throws InterruptedException {
		Integer originalTC = this.getThreadCount();
		// TODO: Why does server.timeout only take expected affect when creating an app
		logger.info("Original TC: " + originalTC);
		Boolean jacORB = Boolean.valueOf(System.getProperty("jacorb", "false"));

		try {
			RedhawkDomainManager domain = driver.getDomain();
			Thread.sleep(1000l);

			// Check number of threads after you retrieve a Domain, make sure
			// it's the expected numbers
			if (jacORB) {
				// ClientMessageReceptor contains thread for connections
				// to the DomainManager CORBA object
				assertTrue("Expected 1 additional thread for DomainManager object", new Integer(originalTC + 1)>=
						this.getThreadCount());
			} else {
				// SunORB leaves one thread in Idle state outside of DomainManager
				assertTrue("Expected two additional thread for DomainManager object", new Integer(originalTC + 2)>=
						this.getThreadCount());
			}

			// Ensure you can still do stuff with CORBA object
			assertEquals(domain.getCorbaObj().name(), domainName);
		} catch (MultipleResourceException | CORBAException | InterruptedException e) {
			fail("Unable to run test " + e.getMessage());
		} finally {
			driver.disconnect();
			Thread.sleep(1000l);

			// Make sure disconnect cleaned everything up
			assertTrue("Thread count should be same as original after", originalTC >= this.getThreadCount());
		}
	}

	@Test
	public void testTCDomain() throws InterruptedException {
		Integer originalTC = this.getThreadCount();
		Boolean jacORB = Boolean.valueOf(System.getProperty("jacorb", "false"));
		try {
			RedhawkDomainManager domain = driver.getDomain(domainName);
			Thread.sleep(1000l);

			// Check number of threads after you retrieve a Domain, make sure
			// it's the expected numbers
			if (jacORB) {
				// ClientMessageReceptor contains thread for connections
				// to the DomainManager CORBA object
				assertTrue("Expected one additional thread for DomainManager object", originalTC>=
						this.getThreadCount());
			} else {
				// SunORB leaves one thread in Idle state outside of DomainManager
				assertTrue("Expected one additional thread for DomainManager object", new Integer(originalTC + 2)>=
						this.getThreadCount());
			}

			// Ensure you can still do stuff with CORBA object
			assertEquals(domain.getCorbaObj().name(), domainName);
			// System.out.println(this.crunchifyGenerateThreadDump());
		} catch (ResourceNotFoundException | CORBAException | InterruptedException e) {
			fail("Unable to run test " + e.getMessage());
		} finally {
			driver.disconnect();
			Thread.sleep(1000l);

			// Make sure disconnect cleaned everything up
			assertTrue("Thread count should be same as original after", originalTC >= this.getThreadCount());
		}
	}

	private static Integer getThreadCount() {
		return ManagementFactory.getThreadMXBean().getThreadCount();
	}

	/*
	 * Code from crunchify
	 * https://cdn.crunchify.com/wp-content/uploads/2013/07/Generate-Java-Thread-
	 * Dump-Programmatically.png
	 */
	public static String crunchifyGenerateThreadDump() {
		final StringBuilder dump = new StringBuilder();
		final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
		final ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(threadMXBean.getAllThreadIds(), 100);

		for (ThreadInfo threadInfo : threadInfos) {
			dump.append('"');
			dump.append(threadInfo.getThreadName());
			dump.append("\" ");
			final Thread.State state = threadInfo.getThreadState();

			dump.append("\n java.lang.Thread.State: ");
			dump.append(state);

			final StackTraceElement[] stackTraceElements = threadInfo.getStackTrace();

			for (final StackTraceElement stackTraceElement : stackTraceElements) {
				dump.append("\n \t at");
				dump.append(stackTraceElement);
			}

			dump.append("\n\n");
		}

		return dump.toString();
	}
}
