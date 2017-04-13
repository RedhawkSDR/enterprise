package redhawk.driver.devicemanager.impl;

import org.junit.Test;

import redhawk.driver.devicemanager.RedhawkDeviceManager;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.testutils.RedhawkTestBase;

public class RedhawkDeviceManagerImplTestIT extends RedhawkTestBase{
	@Test
	public void test() throws MultipleResourceException, CORBAException, Exception{
		//Create a device manager
		RedhawkDeviceManager manager = driver.getDomain().createDeviceManager("anotherDeviceManager", "/var/redhawk/sdr/dev/", false);
		
		Thread.sleep(10000l);
		manager.shutdown();
	}
}
