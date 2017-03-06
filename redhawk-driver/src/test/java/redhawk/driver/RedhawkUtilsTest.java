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
