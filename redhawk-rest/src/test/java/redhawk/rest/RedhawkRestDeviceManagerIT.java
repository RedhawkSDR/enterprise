package redhawk.rest;

import static org.junit.Assert.fail;

import org.junit.Test;

import redhawk.rest.model.DeviceManagerContainer;
import redhawk.rest.model.FetchMode;
import redhawk.rest.utils.TestUtils;
import redhawk.testutils.RedhawkTestBase;

public class RedhawkRestDeviceManagerIT extends RedhawkTestBase{
	private static final String NAMESERVER = domainHost+":"+domainPort;
	
	private static RedhawkManager manager = new RedhawkManager(); 
	
	@Test
	public void testGetDeviceManager() {
		try {
			DeviceManagerContainer container = new DeviceManagerContainer(manager.getAll(NAMESERVER, "devicemanager", domainName, FetchMode.EAGER));
			System.out.println(TestUtils.getStringFromJAXB(container));
		} catch (Exception e) {
			fail("Test failure: "+e.getMessage());
		}
	}
}
