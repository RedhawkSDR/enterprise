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
package redhawk.driver.component.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.component.RedhawkComponent;
import redhawk.driver.domain.RedhawkFileManager;
import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.ApplicationReleaseException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ComponentStartException;
import redhawk.driver.exceptions.ComponentStopException;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.PortException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.port.RedhawkPort;
import redhawk.driver.properties.RedhawkProperty;
import redhawk.driver.properties.RedhawkSimple;
import redhawk.testutils.RedhawkTestBase;

public class RedhawkComponentImplIT extends RedhawkTestBase {
	private String applicationName = "myTestApplication";

	private RedhawkApplication application;

	private List<RedhawkComponent> components;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setup() throws ResourceNotFoundException, ApplicationCreationException, CORBAException, MultipleResourceException {
		driver.getDomain("REDHAWK_DEV").createApplication(applicationName, new File("src/test/resources/waveforms/rh/testWaveform.sad.xml"));
		application = driver.getApplication("REDHAWK_DEV/" + applicationName);
		assertNotNull(application);
		components = application.getComponents();
	}

	@Test
	public void testComponentManagementLifecycle() throws ComponentStartException, ComponentStopException {
		for (RedhawkComponent component : components) {
			component.start();
			assertEquals("Component should be started", true, component.started());
			component.stop();
			assertEquals("Component should be stopped.", false, component.started());
		}
	}

	@Test
	public void testAccessToComponentPorts() throws ResourceNotFoundException, MultipleResourceException {
		for (RedhawkComponent component : components) {
			for (RedhawkPort port : component.getPorts()) {
				assertNotNull(component.getPort(port.getName()));
			}
		}
	}

	@Test
	public void testComponentConnetionHelper2Failure() throws PortException {
		try {
			RedhawkComponent comp = application.getComponentByName("SigGen.*");
			RedhawkComponent connectComp = application.getComponentByName("HardLimit.*");

			// Remove any existing connections
			RedhawkPort port = comp.getPort("dataFloat_out");

			// Disconnect port if connected
			try {

				for (String id : port.getConnectionIds()) {
					port.disconnect(id);
				}
				
				assertTrue(port.getConnectionIds().isEmpty());
			} catch (PortException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Connect the port again
			thrown.expect(PortException.class);
			comp.connect(connectComp, "aConnection", "Foo", "Bar");
		} catch (MultipleResourceException | ResourceNotFoundException e) {
			fail("Unable to run tests " + e.getMessage());
		}
	}

	@Test
	public void testComponentConnectionFailure() throws PortException, ApplicationReleaseException {
		RedhawkApplication failureApp = null;
		/*
		 * Test connecting SigGen_Sine to DataConverter without specifying a port
		 */
		try {
			failureApp = driver.getDomain().createApplication("portTest",
					new File("src/test/resources/waveforms/PortListenerTest/PortListenerTest.sad.xml"));

			// Get components to connect
			RedhawkComponent component = failureApp.getComponentByName("SigGen.*");
			RedhawkComponent componentToConnect = failureApp.getComponentByName("DataConverter_1.*");

			thrown.expect(PortException.class);
			thrown.expectMessage("Multiple ports match with these components specify port names to match");
			component.connect(componentToConnect);
		} catch (MultipleResourceException | ApplicationCreationException | CORBAException
				| ResourceNotFoundException e) {
			fail("Unable to run tests " + e.getMessage());
		} finally {
			if (failureApp != null)
				failureApp.release();

			try {
				RedhawkFileManager manager = driver.getDomain(domainName).getFileManager();
				manager.removeDirectory("/waveforms/PortListenerTest");
			} catch (IOException | ConnectionException | ResourceNotFoundException | CORBAException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.err.println("Clean this up eventaully");
			}
		}
	}

	// TODO: Make this a test
	@Test
	public void snippets() throws Exception {
		// Get your component
		RedhawkComponent component = application.getComponentByName("SigGen.*");
		
		//Set the desired property on the compenent
		component.setProperty("sample_rate", 1000);
		
		//Confirm property set
		RedhawkSimple simple = component.getProperty("sample_rate");
		assertEquals(new Double(1000), simple.getValue());

		// Stop a component
		component.stop();

		// Start a component
		component.start();

		// Check if a component is started
		if (!component.started())
			component.start();
	}

	@Test
	public void testAccessToComponentProperties() throws ResourceNotFoundException, MultipleResourceException {
		for (RedhawkComponent component : components) {
			for (String propertyName : component.getProperties().keySet()) {
				assertNotNull(component.getProperty(propertyName));
			}
		}
	}

	@Test
	public void testComponentProcessId() {
		for (RedhawkComponent component : components) {
			assertNotNull(component.getProcessId());
		}
	}

	@Test
	public void testComponentImplementation() {
		for (RedhawkComponent component : components) {
			assertNotNull(component.getComponentImplementation());
		}
	}

	@Test
	public void testComponentDevice() {
		for (RedhawkComponent component : components) {
			assertNotNull(component.getComponentDevice());
		}
	}

	@After
	public void shutdown() throws ApplicationReleaseException, ConnectionException, ResourceNotFoundException,
			IOException, CORBAException {
		// Release application and clean it up from $SDRROOT
		application.release();
		driver.getDomain("REDHAWK_DEV").getFileManager().removeDirectory("/waveforms/testWaveform");
	}
}
