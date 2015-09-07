package redhawk.driver.domain;

import java.util.List;
import java.util.Map;

import CF.FileManager;
import redhawk.driver.base.RedhawkFileSystem;
import redhawk.driver.xml.model.sca.sad.Softwareassembly;
import redhawk.driver.xml.model.sca.scd.Softwarecomponent;
import redhawk.driver.xml.model.sca.spd.Softpkg;


public interface RedhawkFileManager extends RedhawkFileSystem {

	
	List<String> getWaveformFileNames();
	Map<String, Softwareassembly> getWaveforms();
	Softwareassembly getWaveform(String waveformLocation);
	
	List<String> getComponentFileNames();
	Softwarecomponent getComponent(String componentLocation);	
	Map<String, Softwarecomponent> getComponents();
	
	
	Map<String, Softpkg> getSoftwarePackageDependencies();
	Softpkg getSoftwarePackageDependency(String spdLocation);
	List<String> getSoftwarePackageDependenyFileNames();
	FileManager getCorbaObject();
	
}
