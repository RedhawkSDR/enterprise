package redhawk.driver.base;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import redhawk.driver.RedhawkDriver;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.ResourceNotFoundException;

public class RedhawkFileSystemTestIT {
	private RedhawkFileSystem fileSystem; 	
	
	@Before
	public void setup() throws ConnectionException, ResourceNotFoundException, CORBAException{
		RedhawkDriver driver = new RedhawkDriver(); 
		fileSystem = driver.getDomain("REDHAWK_DEV").getFileManager();
	}
	
	@Test
	public void testFileSystemInteration() throws FileNotFoundException, IOException{
		assertEquals("There should be waveforms in $SDRROOT/dom/waveforms", false, fileSystem.findFilesInDirectory("/waveforms", ".*").isEmpty());	
		assertEquals("There should be directories in $SDRROOT", false, fileSystem.findDirectories(".*").isEmpty());	
		
		
		//Write a waveform File to the waveforms directory
		fileSystem.writeFile(new FileInputStream("src/test/resources/waveforms/rh/testWaveform.sad.xml"), "/waveforms/testWaveform/testWaveform.sad.xml");
		assertEquals("Waveform file should now exist", true, fileSystem.getFile("/waveforms/testWaveform/testWaveform.sad.xml")!=null);
		
		//Clean up after yourself
		fileSystem.removeDirectory("/waveforms/testWaveform");
	}
}
