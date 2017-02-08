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
package redhawk.driver.domain.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import CF.FileManager;
import redhawk.driver.base.impl.RedhawkFileSystemImpl;
import redhawk.driver.domain.RedhawkFileManager;
import redhawk.driver.xml.ScaXmlProcessor;
import redhawk.driver.xml.model.sca.sad.Softwareassembly;
import redhawk.driver.xml.model.sca.scd.Softwarecomponent;
import redhawk.driver.xml.model.sca.spd.Softpkg;

public class RedhawkFileManagerImpl extends RedhawkFileSystemImpl implements RedhawkFileManager {

	private FileManager fileManager;
	private static final String ALL_WAVEFORMS_REGEX = ".*.sad.xml";
	private static final String ALL_COMPONENTS_REGEX = ".*.scd.xml";
	private static final String ALL_SPDS_REGEX = ".*.spd.xml";
	
	private static Logger logger = Logger.getLogger(RedhawkFileManager.class.getName());
	
	public RedhawkFileManagerImpl(FileManager fileManager){
		super((CF.FileSystem) fileManager);
		this.fileManager = fileManager;
	}
	
	@Override
	public List<String> getWaveformFileNames() {
		return findFiles(ALL_WAVEFORMS_REGEX);
	}
	
	public Map<String, Softwareassembly> getWaveforms() {
		Map<String, Softwareassembly> waveforms = getWaveformFileNames().parallelStream().collect(Collectors.toMap(Function.identity(), e -> getWaveform(e)));
		waveforms.values().remove(null);
		return waveforms;
	}	
	
	@Override
	public Softwareassembly getWaveform(String waveformLocation) {
		try {
			return ScaXmlProcessor.unmarshal(new ByteArrayInputStream(getFile(waveformLocation)), Softwareassembly.class);
		} catch (JAXBException | SAXException | IOException e) {
			logger.log(Level.SEVERE, "An exception occurred while marshalling waveform: " + waveformLocation, e);
		}
		
		return null;
	}		
	
	
	@Override
	public List<String> getComponentFileNames() {
		return findFiles(ALL_COMPONENTS_REGEX);
	}

	@Override
	public Softwarecomponent getComponent(String componentLocation) {
		try {
			return ScaXmlProcessor.unmarshal(new ByteArrayInputStream(getFile(componentLocation)), Softwarecomponent.class);
		} catch (JAXBException | SAXException | IOException e) {
			logger.log(Level.SEVERE, "An exception occurred while marshalling component: " + componentLocation, e);
		}
		
		return null;
	}	
	
	public Map<String, Softwarecomponent> getComponents() {
		Map<String, Softwarecomponent> components = getComponentFileNames().parallelStream().collect(Collectors.toMap(Function.identity(), e -> getComponent(e)));
		components.values().remove(null);
		return components;
	}	

	public List<String> getSoftwarePackageDependenyFileNames() {
		return findFilesInDirectory("/deps", ALL_SPDS_REGEX);
	}

	public Softpkg getSoftwarePackageDependency(String spdLocation) {
		try {
			return ScaXmlProcessor.unmarshal(new ByteArrayInputStream(getFile(spdLocation)), Softpkg.class);
		} catch (JAXBException | SAXException | IOException e) {
			logger.log(Level.SEVERE, "An exception occurred while marshalling component: " + spdLocation, e);
		}
		
		return null;
	}	
	
	public Map<String, Softpkg> getSoftwarePackageDependencies() {
		Map<String, Softpkg> spds = getSoftwarePackageDependenyFileNames().parallelStream().collect(Collectors.toMap(Function.identity(), e -> getSoftwarePackageDependency(e)));
		spds.values().remove(null);
		return spds;
	}	
	
	
	public FileManager getCorbaObject(){
		return fileManager;
	}
	
}