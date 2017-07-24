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
package redhawk.driver.application.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import CF.Application;
import CF.ApplicationHelper;
import CF.DeviceAssignmentType;
import CF.ApplicationPackage.ComponentProcessIdType;
import CF.LifeCyclePackage.ReleaseError;
import CF.ResourcePackage.StartError;
import CF.ResourcePackage.StopError;
import redhawk.driver.RedhawkUtils;
import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.base.impl.QueryableResourceImpl;
import redhawk.driver.component.RedhawkComponent;
import redhawk.driver.component.impl.RedhawkComponentImpl;
import redhawk.driver.device.RedhawkDevice;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.exceptions.ApplicationReleaseException;
import redhawk.driver.exceptions.ApplicationStartException;
import redhawk.driver.exceptions.ApplicationStopException;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.logging.RedhawkLogLevel;
import redhawk.driver.port.RedhawkPort;
import redhawk.driver.port.impl.RedhawkExternalPortImpl;
import redhawk.driver.port.impl.RedhawkPortImpl;
import redhawk.driver.properties.RedhawkProperty;
import redhawk.driver.xml.model.sca.sad.Externalports;
import redhawk.driver.xml.model.sca.sad.Externalproperties;
import redhawk.driver.xml.model.sca.sad.Port;
import redhawk.driver.xml.model.sca.sad.Property;
import redhawk.driver.xml.model.sca.sad.Softwareassembly;

public class RedhawkApplicationImpl extends QueryableResourceImpl<Application> implements RedhawkApplication {

	private static Logger logger = Logger.getLogger(RedhawkApplicationImpl.class.getName());

	private RedhawkDomainManager domainManager;
	private String identifier;

	public RedhawkApplicationImpl(RedhawkDomainManager domainManager, String applicationIor, String identifier) {
		super(applicationIor, domainManager.getDriver().getOrb());
		this.domainManager = domainManager;
		this.identifier = identifier;
	}

	@Override
	protected Application locateCorbaObject() throws ResourceNotFoundException {
		logger.log(Level.FINE, "LOCATING CORBA OBJECT");
		String newIor = ((RedhawkApplicationImpl) domainManager.getApplicationByIdentifier(identifier)).getIor();
		return ApplicationHelper.narrow(getOrb().string_to_object(newIor));
	}

	public boolean isStarted() {
		return getCorbaObject().started();
	}

	public List<RedhawkComponent> getComponents() {
		return Arrays.stream(getCorbaObj().registeredComponents()).map(c -> new RedhawkComponentImpl(this, c))
				.collect(Collectors.toList());
	}

	public Map<String, RedhawkComponent> components() {
		return getComponents().stream().collect(Collectors.toMap(e -> e.getName(), Function.identity()));
	}

	@Override
	public List<RedhawkComponent> getComponentsByName(String name) {
		return getComponents().stream().filter(c -> {
			return c.getName().toLowerCase().matches(name.toLowerCase());
		}).collect(Collectors.toList());
	}

	public void release() throws ApplicationReleaseException {
		try {
			getCorbaObject().releaseObject();
		} catch (ReleaseError | ConnectionException e) {
			throw new ApplicationReleaseException(e);
		}
	}

	public RedhawkComponent getComponentByName(String componentName)
			throws MultipleResourceException, ResourceNotFoundException {
		List<RedhawkComponent> components = getComponentsByName(componentName);

		if (components.size() > 1) {
			throw new MultipleResourceException("More that one component found with the name of: " + componentName
					+ " use getComponentsByName instead");
		} else if (components.size() == 1) {
			return components.get(0);
		} else {
			throw new ResourceNotFoundException("Could not find the component with the name of: " + componentName);
		}
	}

	public Application getCorbaObj() {
		return getCorbaObject();
	}

	public String getName() {
		return getCorbaObject().name();
	}

	public String getIdentifier() {
		return getCorbaObject().identifier();
	}

	public RedhawkDomainManager getRedhawkDomainManager() {
		return domainManager;
	}

	public Softwareassembly getAssembly() throws IOException {
		try {
			return RedhawkUtils.unMarshalSadFile(
					new ByteArrayInputStream(domainManager.getFileManager().getFile(getCorbaObject().profile())));
		} catch (ConnectionException | IOException e) {
			throw new IOException(e);
		}
	}

	@Override
	public RedhawkPort getPort(String name) throws ResourceNotFoundException, IOException {
		for (Port port : getAssembly().getExternalports().getPorts()) {
			if (port.getExternalname().matches(name)) {
				String portName = port.getUsesidentifier() != null ? port.getUsesidentifier()
						: port.getProvidesidentifier();
				String compName = port.getComponentinstantiationref().getRefid();
				try {
					RedhawkPortImpl myPort = (RedhawkPortImpl) getComponentByName(compName + ".*").getPort(portName);

					return new RedhawkExternalPortImpl(myPort, port.getDescription(), port.getExternalname(), port.getComponentinstantiationref().getRefid());
				} catch (MultipleResourceException e) {
					logger.severe(e.getMessage());
				} catch (Exception e) {
					logger.severe(e.getMessage());
				}
			}
		}

		throw new ResourceNotFoundException("Could not find the external port specified by: " + name);
	}

	@Override
	public List<RedhawkPort> getPorts() throws IOException {
		List<RedhawkPort> externalPorts = new ArrayList<RedhawkPort>();
		Externalports extPorts = getAssembly().getExternalports();

		if (extPorts != null) {
			for (Port port : extPorts.getPorts()) {
				String portName = port.getUsesidentifier() != null ? port.getUsesidentifier()
						: port.getProvidesidentifier();
				String compName = port.getComponentinstantiationref().getRefid();
				try {
					RedhawkPort rhPort = getComponentByName(compName + ".*").getPort(portName);
					RedhawkExternalPortImpl exPort = new RedhawkExternalPortImpl((RedhawkPortImpl) rhPort);

					// Adding additional methods.
					exPort.setExternalName(port.getExternalname());
					exPort.setDescription(port.getDescription());
					exPort.setComponentReferenceId(port.getComponentinstantiationref().getRefid());
					
					externalPorts.add(exPort);
				} catch (MultipleResourceException e) {
					logger.severe(e.getMessage());
				} catch (Exception e) {
					logger.severe(e.getMessage());
				}
			}
		}

		return externalPorts;
	}

	/**
	 * At this level the only properties that will be returned are external.
	 */
	public Map<String, RedhawkProperty> getExternalProperties() {
		Map<String, RedhawkProperty> propMap = new HashMap<>();
		List<String> exPropIds = new ArrayList<>();
		
		// Only return properties that are external
		try {
			Externalproperties exProps = getAssembly().getExternalproperties();
			if(exProps!=null){
				for(Property prop : exProps.getProperties())
					exPropIds.add(prop.getExternalpropid());
				
				propMap = super.getProperty(exPropIds.toArray(new String[exPropIds.size()]));				
			}
		} catch (IOException e) {
			logger.severe(e.getMessage());
		}
		
		return propMap;
	}
	
	@Override
	public void start() throws ApplicationStartException {
		if (!isStarted()) {
			try {
				getCorbaObject().start();
			} catch (StartError | ConnectionException e) {
				throw new ApplicationStartException(e);
			}
		}
	}

	@Override
	public void stop() throws ApplicationStopException {
		if (isStarted()) {
			try {
				getCorbaObject().stop();
			} catch (StopError | ConnectionException e) {
				throw new ApplicationStopException(e);
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RedhawkApplicationImpl [name=").append(getName()).append(", identifier=")
				.append(getIdentifier()).append("]");
		return builder.toString();
	}

	@Override
	public Class<?> getHelperClass() {
		return ApplicationHelper.class;
	}

	@Override
	public RedhawkLogLevel getLogLevel() {
		return RedhawkLogLevel.reverseLookup(getCorbaObject().log_level());		
	}

	@Override
	public void setLogLevel(RedhawkLogLevel level) {
		getCorbaObject().log_level(level.getValue());
	}

	@Override
	public boolean isAware() {
		return this.getCorbaObject().aware();
	}

	@Override
	public Map<String, RedhawkDevice> getComponentDevices() {
		Map<String, RedhawkDevice> compToDevice = new HashMap<>(); 
		Map<String, RedhawkDevice> devCache = new HashMap<>(); 
		
		for(DeviceAssignmentType devAss : this.getCorbaObject().componentDevices()){
			if(devCache.containsKey(devAss.assignedDeviceId)){
				RedhawkDevice dev = domainManager.getDeviceByIdentifier(devAss.assignedDeviceId);
				
				//Add Device to component map
				compToDevice.put(devAss.componentId, dev);
				
				//Update cache
				devCache.put(devAss.assignedDeviceId, dev);
			}else{
				compToDevice.put(devAss.componentId, devCache.get(devAss.assignedDeviceId));
			}
		}
		
		return compToDevice;
	}

	@Override
	public Map<String, Integer> getComponentProcessIds() {
		Map<String, Integer> compToProcess = new HashMap<>();
		
		for(ComponentProcessIdType id : this.getCorbaObject().componentProcessIds()) {
			compToProcess.put(id.componentId, id.processId);
		}
		
		return compToProcess;
	}
}