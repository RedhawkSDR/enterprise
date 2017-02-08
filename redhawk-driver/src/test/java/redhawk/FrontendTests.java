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

import org.junit.Ignore;
import org.junit.Test;

import redhawk.driver.Redhawk;
import redhawk.driver.RedhawkDriver;
import redhawk.driver.device.RedhawkDevice;
import redhawk.driver.devicemanager.RedhawkDeviceManager;
import redhawk.driver.domain.RedhawkDomainManager;

@Ignore("This would be a system test")
public class FrontendTests {
	@Test
	public void testme() throws Exception {

		// Connect to redhawk and list domain managers
		Redhawk rh = new RedhawkDriver("<insert IP address>");
		System.out.println("\nAll domain managers:");
		Map<String, RedhawkDomainManager> domainManagers = rh.getDomains();
		//TODO: Replacing this line with below someone should probably check it prior to removing the whole thing...
		//domainManagers.stream().map(x->x.getDomainName()).forEach(x -> System.out.println(x));
		for(String domainName : domainManagers.keySet())
			System.out.println(domainName);
		
		// Connect to our domain manager and list device managers
		RedhawkDomainManager pf = rh.getDomain("<insert domain>");
		System.out.println("\nAll device managers:");
		List<RedhawkDeviceManager> deviceManagers = pf.getDeviceManagers();
		deviceManagers.stream().map(x->x.getName()).forEach(x -> System.out.println(x));

		// Get our device manager and list devices
		RedhawkDeviceManager twslv = pf.getDeviceManagerByName("TWSLV_Node");
		System.out.println("\nAll devices:");
		List<RedhawkDevice> devices = twslv.getDevices();
		devices.stream().map(x->x.getName()).forEach(x -> System.out.println(x));

		// Get our device and list its properties
		RedhawkDevice dev = twslv.getDeviceByName("TWSLV_1");
		System.out.println("\n" + dev.getName() + " device properties:");
		dev.getProperties().forEach((x, y) -> System.out.println(String.format("  %s: %s", x, y)));

		// See if its a frontend device
		/*TODO: This functionality does not appear to be present anymore 
		 * 
		 * RedhawkFrontendDevice fdev = dev instanceof RedhawkFrontendDevice ? (RedhawkFrontendDevice) dev : null;
		System.out.println("\nIs it a frontend device? : " + (fdev == null ? "no" : "yes"));

		// Get all the tuners on the device
		System.out.println("\nAll Tuners:");
		fdev.getAllTuners().stream().map(x -> x.get("FRONTEND::tuner_status::rf_flow_id")).forEach(x -> System.out.println(x));

		// Get just the available tuners that aren't in use
		System.out.println("\nAvailable Tuners:");
		fdev.getUnusedTuners().stream().map(x -> x.get("FRONTEND::tuner_status::rf_flow_id")).forEach(x -> System.out.println(x));

		// Get the tuner status. One of them is for our allocation
		System.out.println("\nFrontend Tuner Statuses:");
		fdev.getAllTuners().stream().forEach(x -> System.out.println(x));
		
		// Tune up the FIRST available tuner from the list above. This is different than arbitrarily letting the device choose
		System.out.println("\nAllocating a random available frontend tuner:");
		if (fdev.getUnusedTuners().isEmpty()) {
			System.out.println("  No available tuners to allocate");
			return;
		}
		Map<String, Object> ourTuner = fdev.getUnusedTuners().get(0);
		String flow = ourTuner.get("FRONTEND::tuner_status::rf_flow_id").toString();
		Map<String, Object> newAlloc = new HashMap<String, Object>();
		// String allocId = "myTestAllocationId";
		// newAlloc.put("FRONTEND::tuner_allocation::allocation_id", allocId);
		newAlloc.put("FRONTEND::tuner_allocation::tuner_type", "RX_DIGITIZER");
		newAlloc.put("FRONTEND::tuner_allocation::center_frequency", 464300000d);
		newAlloc.put("FRONTEND::tuner_allocation::bandwidth", 7000000d);
		newAlloc.put("FRONTEND::tuner_allocation::bandwidth_tolerance", 100.0d);
		newAlloc.put("FRONTEND::tuner_allocation::sample_rate", 9000000d);
		newAlloc.put("FRONTEND::tuner_allocation::sample_rate_tolerance", 100.0d);
		newAlloc.put("FRONTEND::tuner_allocation::device_control", true);
		newAlloc.put("FRONTEND::tuner_allocation::group_id", "");
		newAlloc.put("FRONTEND::tuner_allocation::rf_flow_id", flow);
		String allocId = fdev.allocate(newAlloc);
		if (allocId == null) {
			System.out.println("  Tuner allocation failed for : " + flow);
			return;
		}
		System.out.println("allocid : " + allocId);
		String nflow=fdev.getTunerById(allocId).get("FRONTEND::tuner_status::rf_flow_id").toString();
		System.out.println(String.format("  Tuner allocated: %s [%s]", nflow, allocId));

		// Get the tuner status. One of them is for our allocation
		System.out.println("\nFrontend Tuner Statuses:");
		fdev.getAllTuners().stream().forEach(x -> System.out.println(x));

		// Deallocate the tuner
		System.out.println("\nDeallocating frontend tuner:");
		fdev.deallocate(allocId);
		System.out.println("  Tuner deallocated: " + allocId);

		// Get the tuner status. One of them is for our allocation
		System.out.println("\nFrontend Tuner Statuses:");
		fdev.getAllTuners().stream().forEach(x -> System.out.println(x));

		if (true)
			return;

		// Do some tuner operations to our tuner allocation
		FrontendTuner tuner = FrontendTunerHelper.narrow(dev.getCorbaObj());
		FrontendTunerOperations myTuner = (FrontendTunerOperations) tuner;
		myTuner.getTunerType(allocId);
		*/
	}
}
