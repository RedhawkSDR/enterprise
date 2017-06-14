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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.omg.CORBA.COMM_FAILURE;
import org.omg.CORBA.OBJECT_NOT_EXIST;
import org.omg.CORBA.TRANSIENT;
import org.xml.sax.SAXException;

import redhawk.driver.RedhawkDriver;
import redhawk.driver.RedhawkUtils;
import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.application.impl.RedhawkApplicationFactoryImpl;
import redhawk.driver.application.impl.RedhawkApplicationImpl;
import redhawk.driver.base.impl.QueryableResourceImpl;
import redhawk.driver.device.RedhawkDevice;
import redhawk.driver.devicemanager.DeviceManagerInturruptedCallback;
import redhawk.driver.devicemanager.RedhawkDeviceManager;
import redhawk.driver.devicemanager.RedhawkService;
import redhawk.driver.devicemanager.impl.DeviceManagerTemplate;
import redhawk.driver.devicemanager.impl.DurableRedhawkDeviceManagerImpl;
import redhawk.driver.devicemanager.impl.RedhawkDeviceManagerImpl;
import redhawk.driver.devicemanager.impl.RedhawkServiceImpl;
import redhawk.driver.domain.RedhawkAllocationManager;
import redhawk.driver.domain.RedhawkConnectionManager;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.domain.RedhawkFileManager;
import redhawk.driver.eventchannel.RedhawkEventChannelManager;
import redhawk.driver.eventchannel.impl.RedhawkEventChannelManagerImpl;
import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.xml.ScaXmlProcessor;
import redhawk.driver.xml.model.sca.dmd.Domainmanagerconfiguration;
import redhawk.driver.xml.model.sca.prf.Properties;
import redhawk.driver.xml.model.sca.sad.Softwareassembly;
import redhawk.driver.xml.model.sca.spd.Softpkg;
import CF.Application;
import CF.ApplicationFactory;
import CF.DataType;
import CF.DeviceAssignmentType;
import CF.DeviceManager;
import CF.DomainManager;
import CF.DomainManagerHelper;
import CF.InvalidFileName;
import CF.InvalidProfile;
import CF.ApplicationFactoryPackage.CreateApplicationError;
import CF.ApplicationFactoryPackage.CreateApplicationInsufficientCapacityError;
import CF.ApplicationFactoryPackage.CreateApplicationRequestError;
import CF.ApplicationFactoryPackage.InvalidInitConfiguration;
import CF.DomainManagerPackage.ApplicationInstallationError;

public class RedhawkDomainManagerImpl extends
		QueryableResourceImpl<DomainManager> implements RedhawkDomainManager {

	private static Logger logger = Logger
			.getLogger(RedhawkDomainManagerImpl.class.getName());
	private RedhawkDriver driver;
	private String domainName;
	private Map<String, RedhawkDeviceManager> driverRegisteredDeviceManagers = new HashMap<String, RedhawkDeviceManager>();

	public RedhawkDomainManagerImpl(RedhawkDriver driver,
			String domainManagerIor, String domainName) {
		super(domainManagerIor, driver.getOrb());
		this.driver = driver;
		this.domainName = domainName;
	}

	public RedhawkDeviceManager createDeviceManager(String deviceManagerName,
			String fileSystemRoot, boolean durable) throws Exception {
		return createDeviceManager(deviceManagerName, fileSystemRoot, durable,
				null);
	}

	@Override
	public RedhawkDeviceManager createDeviceManager(String deviceManagerName,
			String fileSystemRoot, boolean durable,
			DeviceManagerInturruptedCallback callback) throws Exception {
		final DeviceManagerTemplate devMgr = new DeviceManagerTemplate(
				domainName, getCorbaObj(), getOrb(), deviceManagerName,
				fileSystemRoot);

		RedhawkDeviceManager deviceManager;
		if (durable) {
			deviceManager = new DurableRedhawkDeviceManagerImpl(domainName,
					deviceManagerName, fileSystemRoot, this, getOrb()
							.object_to_string(devMgr.getCorbaDeviceManager()),
					devMgr.getCorbaDeviceManager().identifier(), callback);
		} else {
			deviceManager = new RedhawkDeviceManagerImpl(this, getOrb()
					.object_to_string(devMgr.getCorbaDeviceManager()), devMgr
					.getCorbaDeviceManager().identifier());
		}

		driverRegisteredDeviceManagers.put(
				domainName + ":" + deviceManagerName, deviceManager);
		return deviceManager;
	}

	public String getName() {
		try {
			return getCorbaObject().name();
		} catch (Throwable t) {
			return domainName;
		}
	}

	public String getIdentifier() {
		return getCorbaObject().identifier();
	}

	public RedhawkFileManager getFileManager() {
		return new RedhawkFileManagerImpl(getCorbaObject().fileMgr());
	}

	public Map<String, RedhawkApplication> applications() {
		Map<String, RedhawkApplication> apps = new HashMap<String, RedhawkApplication>();

		for (RedhawkApplication app : getApplications()) {
			try {
				apps.put(app.getName(), app);
			} catch (OBJECT_NOT_EXIST cf) {
				/*
				 * If something no longer exists ( for example a narrowband
				 * waveform that existed when getCorbaObject().applications()
				 * was called and dropped before application.name() was called )
				 * then we still need the rest of the items which could include
				 * what we want.
				 */
				logger.log(Level.FINE, "Item no longer exists", cf);
			}
		}

		return apps;
	}

	public List<RedhawkApplication> getApplications() {
		List<RedhawkApplication> applications = new ArrayList<RedhawkApplication>();
		
		for (Application application : getCorbaObject().applications()) {
			try {
				applications.add(new RedhawkApplicationImpl(this, getOrb()
						.object_to_string(application), application
						.identifier()));
			} catch (OBJECT_NOT_EXIST cf) {
				logger.log(Level.FINE, "Item no longer exists", cf);
			}
		}

		return applications;
	}

	public RedhawkApplication getApplicationByName(String name)
			throws MultipleResourceException, ResourceNotFoundException {
		List<RedhawkApplication> applications = getApplicationsByName(name);

		if (applications.size() > 1) {
			throw new MultipleResourceException(
					"More than one application matches the search criteria.  Use getApplicationsByName instead.");
		} else if (applications.size() == 1) {
			return applications.get(0);
		} else {
			throw new ResourceNotFoundException(
					"Could not find an application with the name: " + name);
		}
	}

	@Override
	public List<RedhawkApplication> getApplicationsByName(String name) {
		return getApplications()
				.stream()
				.filter(a -> {
					boolean matches = a.getName().toLowerCase()
							.matches(name.toLowerCase());
					boolean equals = a.getName().equalsIgnoreCase(name);
					logger.log(Level.FINE, "NAME: " + a.getName() + ":" + name
							+ " : " + matches);
					return equals || matches;
				}).collect(Collectors.toList());
	}

	public RedhawkApplication getApplicationByIdentifier(String identifier)
			throws ResourceNotFoundException {
		List<RedhawkApplication> applications = getApplications()
				.stream()
				.filter(a -> {
					return a.getIdentifier().toLowerCase()
							.matches(identifier.toLowerCase());
				}).collect(Collectors.toList());

		if (applications.size() > 0) {
			return applications.get(0);
		} else {
			throw new ResourceNotFoundException(
					"Could not find an application with the identifier: "
							+ identifier);
		}
	}

	public List<RedhawkDeviceManager> getDeviceManagers() {
		List<RedhawkDeviceManager> deviceManagers = new ArrayList<RedhawkDeviceManager>();
		for (DeviceManager deviceManager : getCorbaObject().deviceManagers()) {
			try {
				deviceManagers.add(new RedhawkDeviceManagerImpl(this, getOrb()
						.object_to_string(deviceManager), deviceManager
						.identifier()));
			} catch (TRANSIENT | OBJECT_NOT_EXIST t) {
				// Skip and don't add dev manager to the list.
			}
		}

		return deviceManagers;
	}

	public Map<String, RedhawkDeviceManager> deviceManagers() {
		Map<String, RedhawkDeviceManager> devMgrs = new HashMap<>();
		for (RedhawkDeviceManager mgr : getDeviceManagers()) {
			devMgrs.put(mgr.getName(), mgr);
		}

		return devMgrs;
	}

	@Override
	public List<RedhawkDeviceManager> getDeviceManagersByName(String name) {
		List<RedhawkDeviceManager> deviceManagers = new ArrayList<RedhawkDeviceManager>();
		for (DeviceManager deviceManager : getCorbaObject().deviceManagers()) {
			try {
				if (deviceManager.label().toLowerCase()
						.matches(name.toLowerCase()))
					deviceManagers.add(new RedhawkDeviceManagerImpl(this,
							getOrb().object_to_string(deviceManager),
							deviceManager.identifier()));
			} catch (COMM_FAILURE | TRANSIENT e) {
				// if a single device manager fails, don't give up on getting
				// the rest
			}
		}

		return deviceManagers;
	}

	public RedhawkDeviceManager getDeviceManagerByName(String name)
			throws MultipleResourceException, ResourceNotFoundException {
		List<RedhawkDeviceManager> deviceManagers = getDeviceManagersByName(name);
		if (deviceManagers.size() > 1) {
			throw new MultipleResourceException(
					"More than one device manager matches the search criteria.  Use getDeviceManagersByName instead.");
		} else if (deviceManagers.size() == 1) {
			return deviceManagers.get(0);
		} else {
			throw new ResourceNotFoundException(
					"Unable to find a device manager with the name: " + name);
		}
	}

	public RedhawkDeviceManager getDeviceManagerByIdentifier(String identifier)
			throws ResourceNotFoundException {
		for (DeviceManager deviceManager : getCorbaObject().deviceManagers()) {
			try {
				if (deviceManager.identifier().toLowerCase()
						.matches(identifier.toLowerCase()))
					return new RedhawkDeviceManagerImpl(this, getOrb()
							.object_to_string(deviceManager),
							deviceManager.identifier());
			} catch (COMM_FAILURE e) {
				// if a single device manager fails, don't give up on getting
				// the rest
			}
		}

		throw new ResourceNotFoundException(
				"Unable to find a device manager with the identifier: "
						+ identifier);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RedhawkDomainManagerImpl [domainName=")
				.append(domainName).append(", uniqueIdentifier=")
				.append(getIdentifier()).append("]");
		return builder.toString();
	}

	public List<RedhawkDevice> getDevices() {
		List<RedhawkDevice> devices = new ArrayList<RedhawkDevice>();
		for (RedhawkDeviceManager deviceManager : getDeviceManagers()) {
			for (RedhawkDevice device : deviceManager.getDevices()) {
				devices.add(device);
			}
		}

		return devices;
	}

	public Map<String, RedhawkDevice> devices() {
		Map<String, RedhawkDevice> devices = new HashMap<>();
		for (RedhawkDevice dev : getDevices()) {
			devices.put(dev.getName(), dev);
		}

		return devices;
	}

	@Override
	public List<RedhawkDevice> getDevicesByName(String deviceName) {
		List<RedhawkDevice> devices = new ArrayList<RedhawkDevice>();
		for (RedhawkDeviceManager deviceManager : getDeviceManagers()) {
			for (RedhawkDevice device : deviceManager.getDevices()) {
				if (device.getName().matches(deviceName)) {
					devices.add(device);
				}
			}
		}

		return devices;
	}

	@Override
	public RedhawkDevice getDeviceByName(String deviceName)
			throws MultipleResourceException {
		List<RedhawkDevice> devices = new ArrayList<RedhawkDevice>();
		for (RedhawkDeviceManager deviceManager : getDeviceManagers()) {
			for (RedhawkDevice device : deviceManager.getDevices()) {
				if (device.getName().matches(deviceName)) {
					devices.add(device);
				}
			}
		}

		if (devices.size() > 1) {
			throw new MultipleResourceException(
					"More than one device matches the search criteria.  Use getDevicesByName instead.");
		} else if (devices.size() == 1) {
			return devices.get(0);
		}

		return null;
	}

	@Override
	public RedhawkDevice getDeviceByIdentifier(String identifier) {
		for (RedhawkDeviceManager deviceManager : getDeviceManagers()) {
			for (RedhawkDevice device : deviceManager.getDevices()) {
				if (device.getIdentifier().matches(identifier)) {
					return device;
				}
			}
		}

		return null;
	}

	public void unRegisterAllDriverRegisteredDeviceManagers() {
		for (RedhawkDeviceManager deviceManager : driverRegisteredDeviceManagers
				.values()) {
			try {
				deviceManager.shutdown();
			} catch (OBJECT_NOT_EXIST e) {
				e.printStackTrace();
			}
		}

		driverRegisteredDeviceManagers.clear();
	}

	public RedhawkApplication createApplication(String instanceName,
			File sadFile) throws ApplicationCreationException {
		if (!sadFile.exists()) {
			throw new ApplicationCreationException(
					"The Sad file referenced by path: "
							+ sadFile.getAbsolutePath() + " does not exist");
		} else if (sadFile.isDirectory()) {
			throw new ApplicationCreationException(
					"The Sad file referenced by path: "
							+ sadFile.getAbsolutePath()
							+ " is a directory not a file");
		}

		try {
			Softwareassembly softwareAssembly = RedhawkUtils
					.unMarshalSadFile(new FileInputStream(sadFile));
			String sadFileDestination = File.separator + "waveforms"
					+ File.separator + softwareAssembly.getName()
					+ File.separator + softwareAssembly.getName() + ".sad.xml";
			// TODO: Should you be able to create a Application that's file is
			// already in SDR???
			getFileManager().writeFile(new FileInputStream(sadFile),
					sadFileDestination);
			return createApplication(instanceName, sadFileDestination);
		} catch (IOException e) {
			throw new ApplicationCreationException(e);
		}
	}

	public RedhawkApplication createApplication(String instanceName,
			Softwareassembly softwareAssembly)
			throws ApplicationCreationException {
		try {
			String sadFileDestination = File.separator + "waveforms"
					+ File.separator + softwareAssembly.getName()
					+ File.separator + softwareAssembly.getName() + ".sad.xml";

			JAXBContext context = JAXBContext
					.newInstance(Softwareassembly.class.getPackage().getName());
			Marshaller marshaller = context.createMarshaller();
			StringWriter stringWriter = new StringWriter();
			marshaller.marshal(softwareAssembly, stringWriter);

			ByteArrayInputStream sadInBytes = new ByteArrayInputStream(
					stringWriter.toString().getBytes());
			// TODO: Should you be able to create a Application that's file is
			// already in SDR???
			getFileManager().writeFile(sadInBytes, sadFileDestination);

			return createApplication(instanceName, sadFileDestination);
		} catch (IOException | JAXBException e) {
			throw new ApplicationCreationException(e);
		}
	}

	public RedhawkApplication createApplication(String instanceName,
			String sadFileDestination) throws ApplicationCreationException {
		DataType[] initConfig = new DataType[] {};
		DeviceAssignmentType[] deviceAssignmentTypes = new DeviceAssignmentType[] {};

		Application applicationInstance;
		try {
			applicationInstance = getCorbaObj().createApplication(
					sadFileDestination, instanceName, initConfig,
					deviceAssignmentTypes);
			RedhawkApplication appToReturn = new RedhawkApplicationImpl(this,
					getOrb().object_to_string(applicationInstance),
					applicationInstance.identifier());
			return appToReturn;
		} catch (CreateApplicationError | CreateApplicationRequestError
				| CreateApplicationInsufficientCapacityError
				| InvalidInitConfiguration | InvalidProfile | InvalidFileName | ApplicationInstallationError e) {
			throw new ApplicationCreationException(e);
		}
	}

	public RedhawkDriver getDriver() {
		return driver;
	}

	@Override
	public List<RedhawkService> getServices() {
		return getDeviceManagers()
				.stream()
				.map(d -> {
					return Arrays
							.stream(d.getCorbaObject().registeredServices())
							.map(s -> new RedhawkServiceImpl(d, s))
							.collect(Collectors.toList());
				}).flatMap(l -> l.stream()).collect(Collectors.toList());
	}

	@Override
	public List<RedhawkService> getServicesByName(String serviceName) {
		return getServices().stream().filter(s -> {
			return s.getServiceName().matches(serviceName);
		}).collect(Collectors.toList());
	}

	@Override
	public RedhawkService getServiceByName(String serviceName)
			throws MultipleResourceException {
		List<RedhawkService> services = getServicesByName(serviceName);

		if (services.size() > 1) {
			throw new MultipleResourceException(
					"More than one service matches the search criteria.  Use getServicesByName instead.");
		} else if (services.size() == 1) {
			return services.get(0);
		}

		return null;
	}

	@Override
	protected DomainManager locateCorbaObject()
			throws ResourceNotFoundException {
		try {
			String ior = ((RedhawkDomainManagerImpl) driver
					.getDomain(domainName)).getIor();
			return DomainManagerHelper.narrow(getOrb().string_to_object(ior));
		} catch (CORBAException e) {
			throw new ResourceNotFoundException(e);
		}
	}

	public Map<String, RedhawkDeviceManager> getDriverRegisteredDeviceManagers() {
		return driverRegisteredDeviceManagers;
	}

	@Override
	public DomainManager getCorbaObj() {
		DomainManager mgr;
		return super.getCorbaObject();
	}

	@Override
	public Class<?> getHelperClass() {
		return CF.DomainManagerHelper.class;
	}

	public RedhawkConnectionManager getConnectionManager() {
		return new RedhawkConnectionManagerImpl(getCorbaObj().connectionMgr());
	}

	public RedhawkAllocationManager getAllocationManager() {
		return new RedhawkAllocationManagerImpl(this, getCorbaObj().allocationMgr());
	}

	public RedhawkEventChannelManager getEventChannelManager() {
		return new RedhawkEventChannelManagerImpl(getOrb(), getCorbaObj()
				.eventChannelMgr());
	}

	@Override
	public RedhawkApplicationFactoryImpl getApplicationFactoryByIdentifier(
			String identifier) throws ResourceNotFoundException,
			MultipleResourceException {
		List<ApplicationFactory> factories = Arrays
				.stream(getCorbaObj().applicationFactories()).filter(af -> {
					return af.identifier().equalsIgnoreCase(identifier);
				}).collect(Collectors.toList());
		if (factories.size() > 1) {
			throw new MultipleResourceException(
					"Multiple Application Factories were found with the identifier: "
							+ identifier);
		} else if (factories.size() == 1) {
			return new RedhawkApplicationFactoryImpl(this, getOrb()
					.object_to_string(factories.get(0)), identifier);
		} else {
			throw new ResourceNotFoundException(
					"An application factory with the identifier of: "
							+ identifier + " was not found");
		}
	}

	public Domainmanagerconfiguration getDomainManagerConfiguration() {
		try {
			String softwareProfile = getCorbaObj().domainManagerProfile();
			byte[] dmdFileInBytes = getFileManager().getFile(softwareProfile);
			return ScaXmlProcessor.unmarshal(new ByteArrayInputStream(
					dmdFileInBytes), Domainmanagerconfiguration.class);
		} catch (JAXBException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	public Softpkg getDomainManagerAssembly() throws ResourceNotFoundException {
		try {
			String spdFileLocation = getDomainManagerConfiguration()
					.getDomainmanagersoftpkg().getLocalfile().getName();
			byte[] spdFileInBytes = getFileManager().getFile(spdFileLocation);
			return ScaXmlProcessor.unmarshal(new ByteArrayInputStream(
					spdFileInBytes), Softpkg.class);
		} catch (IOException | JAXBException | SAXException e) {
			throw new ResourceNotFoundException(e);
		}
	}

	public Properties getPropertyConfiguration()
			throws ResourceNotFoundException {
		try {
			String prfFileName = getDomainManagerAssembly().getPropertyfile()
					.getLocalfile().getName();
			String softwareProfile = getDomainManagerConfiguration()
					.getDomainmanagersoftpkg().getLocalfile().getName();
			String componentDirPath = softwareProfile.substring(0,
					softwareProfile.lastIndexOf("/"));
			byte[] prfFileInBytes = getFileManager().getFile(
					componentDirPath + "/" + prfFileName);
			return ScaXmlProcessor.unmarshal(new ByteArrayInputStream(
					prfFileInBytes), Properties.class);
		} catch (IOException | JAXBException | SAXException e) {
			throw new ResourceNotFoundException(e);
		}
	}

}
