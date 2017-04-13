package redhawk.testutils;

import java.io.IOException;
import java.util.concurrent.Executors;

public class NodeBooterProxy {
	private ProcessBuilder builder; 
	
	public NodeBooterProxy(){
		builder = new ProcessBuilder();
	}
	
	/**
	 * Creates a DeviceManager given a DeviceManager location.
	 * 
	 * @param dcdFileLocation
	 * 	Location of DcD file.
	 * @return
	 * 	Process for 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public Process launchDeviceManager(String dcdFileLocation) throws IOException, InterruptedException{
		builder.command("sh", "-c", "nodeBooter -d "+dcdFileLocation);
		Process process = builder.start();
		
		StreamGobbler streamGobller = new StreamGobbler(process.getInputStream(), System.out::println);
		
		Executors.newSingleThreadExecutor().submit(streamGobller);

		return process;
		
		//int exitCode = process.waitFor();
		
		//return exitCode;
	}
	
}
