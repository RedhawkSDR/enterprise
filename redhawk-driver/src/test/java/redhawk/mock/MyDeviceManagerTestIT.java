package redhawk.mock;

import java.io.File;
import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import redhawk.RedhawkTestBase;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.MultipleResourceException;

@Ignore("Launch a deviceManager programmatically")
public class MyDeviceManagerTestIT extends RedhawkTestBase{
	@Test
	public void test() throws MultipleResourceException, CORBAException, Exception{
		String dcdFileLocation = "src/test/resources/node/SimulatorNode/DeviceManager.dcd.xml";
				
		MyRedhawkDeviceManager mgr = new MyRedhawkDeviceManager(
					new File(dcdFileLocation),
					"REHDAWK_DEV",
					"SimulatorNode",
					driver.getDomain().getCorbaObj(),
					driver.getOrb(),
					"/var/redhawk/sdr/dev/"
				);
		
		System.out.println(mgr);
		Thread.sleep(30000l);
		mgr.shutdown();
	}
}
