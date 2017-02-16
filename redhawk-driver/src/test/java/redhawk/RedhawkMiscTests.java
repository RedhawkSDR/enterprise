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
package redhawk;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.component.RedhawkComponent;
import redhawk.driver.device.RedhawkDevice;
import redhawk.driver.devicemanager.RedhawkDeviceManager;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.port.RedhawkPort;

@Ignore("Don't run during install lots of good stuff in here though...")
public class RedhawkMiscTests extends RedhawkBaseTest {
	private String domainName = "REDHAWK_DEV";
	
	private String applicationName = "";
	@Test
	public void getDomainManagers() throws Exception {
		System.out.println("Domains:");
		for (Map.Entry<String, RedhawkDomainManager> d : driver.getDomains().entrySet())
			System.out.println("\t" + d);
	}
	@Test
	public void getApplications() throws Exception {
		System.out.println("Applications:");
		for (RedhawkApplication app : driver.getDomain("REDHAWK_DEV").getApplications())
			System.out.println("\t" + app);
	}

	@Test
	public void getComponents() throws Exception {
		RedhawkDomainManager d = driver.getDomain(domainName);
		RedhawkApplication p = d
				.getApplicationByName(applicationName);
		System.out.println("Components:");
		for (RedhawkComponent c : p.getComponents())
			System.out.println("\t" + c);
	}

	@Test
	public void getComponentPorts() throws Exception {
		System.out.println("Component Ports:");
		RedhawkDomainManager d = driver.getDomain(domainName);
		RedhawkApplication p = d
				.getApplicationByName(applicationName);
		RedhawkComponent c = p.getComponentByName("MWPB_Tasker.*");
		for (RedhawkPort port : c.getPorts())
			System.out.println("\t" + port);
	}

	@Test
	public void getExternalPorts() throws Exception {
		RedhawkDomainManager d = driver.getDomain(domainName);
		RedhawkApplication p = d
				.getApplicationByName(applicationName);
		System.out.println("ExternalPorts:");
		for (RedhawkPort port : p.getExternalPorts())
			System.out.println("\t" + port);
	}

	@Test
	public void getDeviceManagers() throws Exception {
		RedhawkDomainManager d = driver.getDomain(domainName);
		System.out.println("DeviceManagers:");
		for (RedhawkDeviceManager dev : d.getDeviceManagers()) {
			String deviceManagerName = null;
			String uniq = null;
			try {
				deviceManagerName = dev.getName();
				uniq = dev.getUniqueIdentifier();
			} catch (Exception e) {
				// a reference to a device manager is returned, but that doesn't
				// mean it actually exists anymore
			}
			System.out.println(String.format("\t%s [%s]", deviceManagerName,
					uniq));
		}
	}

	@Test
	public void getDeviceManagerByName() throws Exception {
		System.out.println("DeviceManager (byName):");
		RedhawkDomainManager d = driver.getDomain(domainName);
		List<RedhawkDeviceManager> dms = d
				.getDeviceManagersByName("dynamic_gpp_node.*");
		for (RedhawkDeviceManager dm : dms)
			System.out.println("\t" + dm);
	}

	@Test
	public void getDeviceManagerById() throws Exception {
		System.out.println("DeviceManager (byId):");
		RedhawkDomainManager d = driver.getDomain(domainName);
		RedhawkDeviceManager dm = d.getDeviceManagerByIdentifier("");
		Assert.assertNull(dm);
		System.out.println("\t" + dm);
	}

	@Test
	public void getDeviceByName() throws Exception {
		System.out.println("Device (byName): ");
		RedhawkDomainManager d = driver.getDomain(domainName);
		RedhawkDeviceManager dm = d
				.getDeviceManagerByName("dynamic_gpp_node.*");
		RedhawkDevice dv = dm.getDeviceByName("dynamicGPP.*");
		System.out.println("\t" + dv);
	}

	@Test
	public void getDeviceById() throws Exception {
		System.out.println("Device (byId): ");
		RedhawkDomainManager d = driver.getDomain(domainName);
		RedhawkDeviceManager dm = d
				.getDeviceManagerByName("dynamic_gpp_node.*");
		RedhawkDevice dv = dm
				.getDeviceByIdentifier("dynamic_gpp_node:dynamicGPP_1");
		System.out.println("\t" + dv);
	}

	@Test
	public void getDevicePorts() throws Exception {
		System.out.println("Device Ports: ");
		RedhawkDomainManager d = driver.getDomain(domainName);
		RedhawkDeviceManager dm = d
				.getDeviceManagerByName("dynamic_gpp_node.*");
		RedhawkDevice dv = dm
				.getDeviceByIdentifier("dynamic_gpp_node:dynamicGPP_1");
		List<RedhawkPort> ps = dv.getPorts();
		for (RedhawkPort p : ps)
			System.out.println("\t" + p);
	}

	@Test
	public void getDevicePortByName() throws Exception {
		System.out.println("Device Port (byName): ");
		RedhawkDomainManager d = driver.getDomain(domainName);
		RedhawkDeviceManager dm = d
				.getDeviceManagerByName("dynamic_gpp_node.*");
		RedhawkDevice dv = dm
				.getDeviceByIdentifier("dynamic_gpp_node:dynamicGPP_1");
		RedhawkPort p = dv.getPort("propEvent");
		System.out.println("\t" + p);
	}

	@Test
	public void getComponentPortByName() throws Exception {
		System.out.println("Component Port (byName): ");
		RedhawkDomainManager d = driver.getDomain(domainName);
		RedhawkApplication ap = d.getApplicationByName(applicationName);
		RedhawkComponent c = ap.getComponentByName("MWPB_Tasker.*");
		RedhawkPort p = c.getPort("DomainManager_out");
		System.out.println("\t" + p);
	}
	
}