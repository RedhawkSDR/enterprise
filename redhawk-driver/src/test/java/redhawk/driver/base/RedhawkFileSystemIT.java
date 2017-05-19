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
package redhawk.driver.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.testutils.RedhawkTestBase;

public class RedhawkFileSystemIT extends RedhawkTestBase{
	private RedhawkFileSystem domainFileSystem; 
	
	private RedhawkFileSystem deviceManagerFileSystem;
	
	@Before
	public void setup() throws ConnectionException, ResourceNotFoundException, CORBAException{
		domainFileSystem = driver.getDomain("REDHAWK_DEV").getFileManager();
		deviceManagerFileSystem = driver.getDomain("REDHAWK_DEV").getDeviceManagers().get(0).getDeviceManagerFileSystem();
	}
	
	@Test
	public void testFileSystemInteration() throws FileNotFoundException, IOException{
		assertEquals("There should be waveforms in $SDRROOT/dom/waveforms", false, domainFileSystem.findFilesInDirectory("/waveforms", ".*").isEmpty());	
		assertEquals("There should be directories in $SDRROOT", false, domainFileSystem.findDirectories(".*").isEmpty());	
		
		
		//Write a waveform File to the waveforms directory
		domainFileSystem.writeFile(new FileInputStream("src/test/resources/waveforms/rh/testWaveform.sad.xml"), "/waveforms/testWaveform/testWaveform.sad.xml");
		assertEquals("Waveform file should now exist", true, domainFileSystem.getFile("/waveforms/testWaveform/testWaveform.sad.xml")!=null);
		
		//Clean up after yourself
		domainFileSystem.removeDirectory("/waveforms/testWaveform");
	}
	
	@Test
	public void testFileSystemInteractionData() throws FileNotFoundException, IOException{
		File dataDir = new File("src/test/resources/data");
		for(File data : dataDir.listFiles()){
			domainFileSystem.writeFile(new FileInputStream(data), "/data/"+data.getName());
		}
		
		assertEquals("Should now be two files in the directory", 2, domainFileSystem.findFilesInDirectory("data", ".*").size());
		domainFileSystem.removeDirectory("/data");
		
		assertTrue("Should now be zero files in the directory", domainFileSystem.findFilesInDirectory("data", ".*").isEmpty());
	}
	
	@Test
	public void testDeviceManagerFileSystem(){
		List<String> directories = deviceManagerFileSystem.findDirectories("/nodes");
	
		for(String directory : directories){
			System.out.println(directory);
		}
	}
}
