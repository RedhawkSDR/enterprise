package redhawk.driver.allocationmanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import redhawk.driver.allocationmanager.AllocationInfo;
import redhawk.driver.device.RedhawkDevice;
import redhawk.driver.devicemanager.RedhawkDeviceManager;
import redhawk.driver.exceptions.AllocationException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.EventChannelCreationException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.testutils.RedhawkTestBase;

public class RedhawkAllocationManagerIT extends RedhawkTestBase{
	private static RedhawkAllocationManager allocMgr;
	
	private static RedhawkDeviceManager deviceManager;
	
	private final String allocationType = "FRONTEND::tuner_allocation";
	
	private static File nodeDir;
	
	private static Process devMgrProcess; 
	
	@BeforeClass
	public static void setupAllocationManager(){
		try {
			/*
			 * Place Dcd in it's proper directory 
			 */
			File file = new File("src/test/resources/node/SimulatorNode");
			
			nodeDir = new File(deviceManagerHome+"/nodes/SimulatorNode");

			allocMgr = driver.getDomain().getAllocationManager();
			
			/*
			 * Copy Nodes directory over  
			 */
			FileUtils.copyDirectory(file, nodeDir, FileFilterUtils.suffixFileFilter(".dcd.xml"));	
		
			
			devMgrProcess = proxy.launchDeviceManager("/var/redhawk/sdr/dev/nodes/SimulatorNode/DeviceManager.dcd.xml");
			
			//Could use EventChannel to know when it's available
			Thread.sleep(10000l);
			deviceManager = driver.getDeviceManager("REDHAWK_DEV/Simulator.*");
		} catch (MultipleResourceException | CORBAException | IOException | InterruptedException | ResourceNotFoundException e) {
			// TODO Auto-generated catch block
			fail("Issue setting up test"+e.getMessage());
		}
	}
	
	@Test
	public void testGetCorbaObj(){
		//Test to make sure you can access corba obj from Domain Manager
		assertNotNull(allocMgr.getCorbaObj());
	}
	
	@Test
	public void testListDevices(){
		List<RedhawkDevice> devices = allocMgr.listDevices();
		
		for(RedhawkDevice dev : devices){
			System.out.println(dev);
		}
		
		assertEquals("Should be a simulator and GPP available", 2, devices.size());
	}
	
	@Test
	public void testAllocateAndDeallocateADevice(){
		//Allocate a device
		this.allocateDevice();
		
		//List the allocated devices
		this.getAllocations();
		
		//Deallocate Device
		this.deallocate();
	}
	
	public void deallocate(){
		List<AllocationInfo> allocation = allocMgr.getAllocations();
		
		Integer allocNum = allocation.size();
		
		assertTrue("Needs to be an allocation to deallocate ", allocNum>0);
		String allocationId = allocMgr.getAllocations().get(0).getAllocationId();
		
		
		allocMgr.deallocate(allocationId);
		assertEquals("Should be less allocations than original amount", allocNum-1, allocMgr.getAllocations().size());	
	}
	
	public void allocateDevice(){
		//Get simulator deviceId
		try {
			RedhawkDevice device = driver.getDeviceManager("REDHAWK_DEV/Simulator.*").getDevices().get(0);
			
			String identifier = device.getIdentifier();
			
			allocMgr.allocate(identifier, allocationType, this.getAllocationProperties());
		} catch (ResourceNotFoundException | MultipleResourceException | CORBAException | AllocationException e) {
			fail("Issue allocating device "+e.getMessage());
		}
	}
	
	public void getAllocations(){
		List<AllocationInfo> allocations = allocMgr.getAllocations();
		
		//Check to make sure no null objects
		for(AllocationInfo info : allocations){
			try {
				nullObjectCheck(info);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	private Map<String, Object> getAllocationProperties(){
		Map<String, Object> newAlloc = new HashMap<>();
		
		String allocId = "myTestAllocationId";
		newAlloc.put("FRONTEND::tuner_allocation::allocation_id", allocId);
		newAlloc.put("FRONTEND::tuner_allocation::tuner_type", "RX_DIGITIZER");
		newAlloc.put("FRONTEND::tuner_allocation::center_frequency", 101100000d);//101.1e6
		newAlloc.put("FRONTEND::tuner_allocation::sample_rate", 256000d);//256e3
		newAlloc.put("FRONTEND::tuner_allocation::bandwidth_tolerance", 20.0);
		newAlloc.put("FRONTEND::tuner_allocation::sample_rate_tolerance", 20.0);		
	
		return newAlloc;
	}
	
	
	private void nullObjectCheck(Object obj) throws IllegalArgumentException, IllegalAccessException{
		for(Field f : obj.getClass().getFields()){
			f.setAccessible(true);
			if(f.get(obj)==null)
				fail("All fields in this object "+obj.getClass().getName()+" Should be set.");
		}
	}
	
	@AfterClass
	public static void cleanupAllocationManager() throws IOException, MultipleResourceException, EventChannelCreationException, CORBAException{
		deviceManager.shutdown();
		
		//Remove directory for node
		FileUtils.deleteDirectory(nodeDir);

		devMgrProcess.destroy();		
	}
}
