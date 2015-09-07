package redhawk.driver.domain.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import redhawk.driver.RedhawkDriver;
import redhawk.driver.domain.RedhawkFileManager;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.xml.model.sca.sad.Softwareassembly;

public class RedhawkFileManagerImplTestIT {
	private RedhawkFileManager fileManager; 
	
	private List<String> defaultRHWaveforms = new ArrayList<>();

	@Before
	public void setup() throws ConnectionException, ResourceNotFoundException, CORBAException{
		fileManager = new RedhawkDriver().getDomain("REDHAWK_DEV").getFileManager(); 
		
		String baseLocation = "/waveforms/rh/";
		defaultRHWaveforms.add(baseLocation+"basic_components_demo/basic_components_demo.sad.xml");
		defaultRHWaveforms.add(baseLocation+"FM_mono_demo/FM_mono_demo.sad.xml");
		defaultRHWaveforms.add(baseLocation+"FM_RBDS_demo/FM_RBDS_demo.sad.xml");
		defaultRHWaveforms.add(baseLocation+"short_file_to_float_file/short_file_to_float_file.sad.xml");
		defaultRHWaveforms.add(baseLocation+"socket_loopback_demo/socket_loopback_demo.sad.xml");		
		defaultRHWaveforms.add(baseLocation+"vita49_loopback_demo/vita49_loopback_demo.sad.xml");			
	}
	
	@Test
	public void testFileManagerGetDemoWaveforms(){
		List<String> waveforms = fileManager.getWaveformFileNames();
		
		for(String waveform : defaultRHWaveforms){
			assertEquals(waveform+" should be in the default Waveform List but isn't "+waveforms, true, waveforms.contains(waveform));
		}
	}
	
	@Test
	public void testGetWaveforms(){
		Map<String, Softwareassembly> waveforms = fileManager.getWaveforms();
		
		//Make sure the appropriate number of default ones are there 
		assertEquals(6, waveforms.size());
		
		for(String waveform : waveforms.keySet()){
			assertNotNull(fileManager.getWaveform(waveform));
		}
	}
	
	@Test
	public void getDefaultComponents(){
		List<String> componentFileNames = fileManager.getComponentFileNames();
		
		//TODO: Bug in getComponentFileNames returns a non componet 'mgr/domainManager.scd.xml
		assertEquals("There should be 21 default components with RH", 22, componentFileNames.size());
		
		for(String componentFileName : componentFileNames){
			assertNotNull(fileManager.getComponent(componentFileName));
		}
	}
	
	@Test
	public void testGetCorbaObject(){
		assertNotNull(fileManager.getCorbaObject());
	}
}
