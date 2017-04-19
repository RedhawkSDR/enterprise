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
package redhawk.driver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import redhawk.driver.xml.model.sca.sad.Softwareassembly;

public class RedhawkUtilsTest {	
	@Test
	public void testUnmarshalSadFile() throws IOException {
		InputStream data = new FileInputStream("src/test/resources/waveforms/rh/testWaveform.sad.xml");
		Softwareassembly sadObject = RedhawkUtils.unMarshalSadFile(data);
		assertNotNull(sadObject);
		assertEquals("testWaveform", sadObject.getName());
	}
	
	@Test(expected=IOException.class)
	public void testUnmarshalSadFileWithException() throws IOException {
		RedhawkUtils.unMarshalSadFile(null);
	}
}
