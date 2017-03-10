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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import redhawk.RedhawkTestBase;
import redhawk.driver.RedhawkDriver;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.ResourceNotFoundException;

public class RedhawkFileSystemTestIT extends RedhawkTestBase{
	private RedhawkFileSystem fileSystem; 	
	
	@Before
	public void setup() throws ConnectionException, ResourceNotFoundException, CORBAException{
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
