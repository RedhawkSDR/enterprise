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
package redhawk.driver.domain;

import java.util.List;
import java.util.Map;

import CF.FileManager;
import redhawk.driver.base.RedhawkFileSystem;
import redhawk.driver.xml.model.sca.sad.Softwareassembly;
import redhawk.driver.xml.model.sca.scd.Softwarecomponent;
import redhawk.driver.xml.model.sca.spd.Softpkg;

/**
 * Class that enables you to easily interact with the FileSystem 
 * for a Redhawk Domain. 
 *
 */
public interface RedhawkFileManager extends RedhawkFileSystem {
	/**
	 * @return A list of the waveform names for the RedhawkFileManager. 
	 */
	List<String> getWaveformFileNames();
	
	/**
	 * @return A map of the waveforms with the key being the waveform name and the value being the {@link Softwareassembly} for the waveform. 
	 */
	Map<String, Softwareassembly> getWaveforms();
	
	/**
	 * Retrieve the waveform at a specific location
	 * @param waveformLocation location of the waveform. 
	 * @return
	 * 	{@link Softwareassembly} for the waveform location. 
	 */
	Softwareassembly getWaveform(String waveformLocation);
	
	/**
	 * Gets all the component file names. 
	 * @return
	 * 	component file names. 
	 */
	List<String> getComponentFileNames();
	
	/**
	 * Get the Softwarecomponent by component location. 
	 * 
	 * @param componentLocation
	 * 	location of a component. 
	 * @return
	 */
	Softwarecomponent getComponent(String componentLocation);	
	
	/**
	 * @return a {@link java.util.Map} with component name as the key and {@link Softwarecomponent} as the value. 
	 */
	Map<String, Softwarecomponent> getComponents();
	
	/**
	 * @return a {@link java.util.Map} with dependency name as the key and {@link Softpkg} as the value.
	 */
	Map<String, Softpkg> getSoftwarePackageDependencies();
	
	/**
	 * Gets a specific {@link Softpkg}
	 * @param spdLocation
	 * 	location of the {@link Softpkg}
	 * @return
	 */
	Softpkg getSoftwarePackageDependency(String spdLocation);
	
	/**
	 * @return
	 * 	Returns the list of SoftwarePackageDependency File Names. 
	 */
	List<String> getSoftwarePackageDependenyFileNames();
	
	/** 
	 * @return
	 * Returns a {@link CF.FileManager} object.
	 */
	FileManager getCorbaObject();
}
