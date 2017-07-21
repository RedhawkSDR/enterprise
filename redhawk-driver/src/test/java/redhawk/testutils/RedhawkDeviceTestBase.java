package redhawk.testutils;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import redhawk.driver.devicemanager.RedhawkDeviceManager;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.EventChannelCreationException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;

public class RedhawkDeviceTestBase extends RedhawkTestBase{
	public static RedhawkDeviceManager deviceManager; 
	
	public static File nodeDir;
	
	public static Process devMgrProcess;
	
	//Variable indicating whether dev manager for simulator is available
	public static Boolean devMgrStartedExternally = false;	
	
	@BeforeClass
	public static void setupDevice(){
		try{
			System.out.println("======================================");
			for(RedhawkDeviceManager devMgr : driver.getDomain(domainName).getDeviceManagers()){
				System.out.println("DevMgr: "+devMgr);
			}
			System.out.println("======================================");
			deviceManager = driver.getDeviceManager(domainName+"/Simulator.*");
			devMgrStartedExternally = true;
		}catch(Exception ex){
			logger.info("No simulator going to try to launch one myself");
		}
		
		try {
			/*
			 * If the device manager does not exist try to launch it.
			 */
			if(!devMgrStartedExternally){
				/*
				 * Place Dcd in it's proper directory 
				 */
				//TODO: Add a way to configure this from a file
				File file = new File("src/test/resources/node/SimulatorNode");
				
				nodeDir = new File(deviceManagerHome+"/nodes/SimulatorNode");
				
				/*
				 * Copy Nodes directory over  
				 */
				FileUtils.copyDirectory(file, nodeDir, FileFilterUtils.suffixFileFilter(".dcd.xml"));	
			
				
				devMgrProcess = proxy.launchDeviceManager("/var/redhawk/sdr/dev/nodes/SimulatorNode/DeviceManager.dcd.xml");
				
				//Could use EventChannel to know when it's available
				Thread.sleep(10000l);
				deviceManager = driver.getDeviceManager("REDHAWK_DEV/Simulator.*");	
			}
		} catch (MultipleResourceException | CORBAException | IOException | InterruptedException | ResourceNotFoundException e) {
			// TODO Auto-generated catch block
			fail("Issue setting up test"+e.getMessage());
		}
	}
	
	
	@AfterClass
	public static void cleanupDevice() throws IOException, MultipleResourceException, EventChannelCreationException, CORBAException{
		if(!devMgrStartedExternally){
			deviceManager.shutdown();
			
			//Remove directory for node
			FileUtils.deleteDirectory(nodeDir);

			devMgrProcess.destroy();	
		}	
	}
}
