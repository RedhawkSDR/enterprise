package redhawk.mock;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.junit.Test;

import com.google.common.io.Files;

import redhawk.RedhawkTestBase;
import redhawk.driver.domain.RedhawkFileManager;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.testutils.NodeBooterProxy;

public class NodeBooterProxyTestMT extends RedhawkTestBase{
	private String sdrRoot = "/var/redhawk/sdr";
	
	private String deviceManagerHome = this.sdrRoot+"/dev";
	
	/*
	 * Launches a DeviceManager using the NodeBooter script. 
	 */
	@Test
	public void testLaunchingADeviceManager() throws IOException, InterruptedException, ConnectionException, MultipleResourceException, CORBAException{
		/*
		 * Place Dcd in it's proper directory 
		 */
		File file = new File("src/test/resources/node/SimulatorNode");
		
		File nodeDir = new File(deviceManagerHome+"/nodes/SimulatorNode");
		
		/*
		 * Copy Nodes directory over  
		 */
		FileUtils.copyDirectory(file, nodeDir, FileFilterUtils.suffixFileFilter(".dcd.xml"));				
		
		/*
		 * Use the NodeBooter proxy to launch your device manager 
		 */
		NodeBooterProxy proxy = new NodeBooterProxy(); 
		
		Process process = proxy.launchDeviceManager("/var/redhawk/sdr/dev/nodes/SimulatorNode/DeviceManager.dcd.xml");	
		
		Thread.sleep(10000l);
		
		process.destroy();
		
		FileUtils.deleteDirectory(nodeDir);
	}
}
