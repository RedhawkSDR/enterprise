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

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.junit.Test;

import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.testutils.NodeBooterProxy;
import redhawk.testutils.RedhawkTestUtils;
import redhawk.testutils.StreamGobbler;

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
	
	@Test
	public void testBuildSh() throws IOException, InterruptedException{
		//Run the component before deploying it
		//Process p = RedhawkTestUtils.runBuildSh("src/test/resources/components/MessageProducer/", "build.sh");
		
		String workingDirectory = "src/test/resources/components/MessageProducer/";
		
		ProcessBuilder builder = new ProcessBuilder();
		builder.directory(new File(workingDirectory));
		builder.command("sh", "-c", "build.sh");
		
		Process process = builder.start();
		StreamGobbler streamGobller = new StreamGobbler(process.getInputStream(), System.out::println);

		Executors.newSingleThreadExecutor().submit(streamGobller);
		
		process.waitFor();

	}
}
