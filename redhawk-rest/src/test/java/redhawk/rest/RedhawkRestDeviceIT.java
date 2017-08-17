package redhawk.rest;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import redhawk.rest.model.Device;
import redhawk.rest.model.DeviceManager;
import redhawk.rest.model.FetchMode;
import redhawk.testutils.RedhawkTestBase;

public class RedhawkRestDeviceIT extends RedhawkTestBase{
	private static RedhawkManager manager = new RedhawkManager();
	
	private static DeviceManager devManager; 
	
	private static String nameServer; 
	
	
	@BeforeClass
	public static void setup() {
		nameServer = domainHost+":"+domainPort;
		
		try {
			for(Object restDevMgr : manager.getAll(nameServer, "devicemanager", domainName, FetchMode.EAGER)) {
				String label = ((DeviceManager)restDevMgr).getLabel();
				System.out.println("Label: "+label);
				if(label.contains("DevMgr"))
					devManager = (DeviceManager) restDevMgr;
			}
		} catch (Exception e) {
			fail("Unable to run test can't get device manager "+e.getMessage());
		}
	}
	
	@Test
	public void testDeviceFields() {
		/*
		 * Get the GPP and test whether the new fields are being 
		 * set 
		 */
		try {
			//TODO: Unable to get DeviceManager by Identifier create a bug ticket.
			Device device = manager.get(nameServer, "device", domainName+"/"+devManager.getLabel()+"/GPP.*");
			
			assertNotNull(device.getAdminState());
			assertNotNull(device.getUsageState());
			assertNotNull(device.getOperationState());
			assertNotNull(device.getImplementation());
		} catch (Exception e) {
			fail("Test failure "+e.getMessage());
		}
	}
}
