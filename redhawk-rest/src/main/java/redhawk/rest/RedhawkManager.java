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
package redhawk.rest;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redhawk.driver.Redhawk;
import redhawk.driver.RedhawkDriver;
import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.base.QueryableResource;
import redhawk.driver.component.RedhawkComponent;
import redhawk.driver.device.AdminState;
import redhawk.driver.device.RedhawkDevice;
import redhawk.driver.device.impl.RedhawkDeviceImpl;
import redhawk.driver.devicemanager.RedhawkDeviceManager;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.domain.impl.RedhawkDomainManagerImpl;
import redhawk.driver.eventchannel.impl.RedhawkEventRegistrant;
import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.ApplicationStartException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.EventChannelException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.port.RedhawkPort;
import redhawk.driver.port.RedhawkPortStatistics;
import redhawk.driver.port.RedhawkStreamSRI;
import redhawk.driver.properties.RedhawkProperty;
import redhawk.driver.properties.RedhawkSimple;
import redhawk.driver.properties.RedhawkSimpleSequence;
import redhawk.driver.properties.RedhawkStruct;
import redhawk.driver.properties.RedhawkStructSequence;
import redhawk.driver.xml.model.sca.prf.Properties;
import redhawk.driver.xml.model.sca.sad.Externalproperties;
import redhawk.driver.xml.model.sca.sad.Softwareassembly;
import redhawk.rest.model.FetchMode;
import redhawk.rest.model.FullProperty;
import redhawk.rest.model.PortStatisticsContainer;
import redhawk.rest.model.Property;
import redhawk.rest.model.PropertyContainer;
import redhawk.rest.model.RemoteDomainRegistrar;
import redhawk.rest.model.SRIContainer;
import redhawk.rest.model.TunerMode;
import redhawk.rest.model.WaveformInfo;

public class RedhawkManager {
	private static Logger logger = LoggerFactory.getLogger(RedhawkManager.class);

	private List<ServiceReference<Redhawk>> redhawkDriverServices;
	private Map<String, Redhawk> redhawkDrivers = new ConcurrentHashMap<String, Redhawk>();

	private DomainConverter converter = new DomainConverter();
	private BundleContext context;

	private Redhawk getDriverInstance(String nameServer) throws ResourceNotFoundException {
		Redhawk redhawk = null;
		if (redhawkDrivers.get(nameServer) != null) {
			return redhawkDrivers.get(nameServer);
		} else {
			if (nameServer.contains(":")) {
				String[] hostAndPort = nameServer.split(":");
				redhawk = new RedhawkDriver(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
				redhawkDrivers.put(nameServer, redhawk);
				return redhawk;
			} else {
				throw new ResourceNotFoundException(
						"You did not specify a valid host and port to the REDHAWK name server. An example of a valid url is: localhost:2809");
			}
		}
	}
	
	public <T> T get(String nameServer, String type, String... location) throws ResourceNotFoundException, Exception {
		Redhawk redhawk = null;
		try {
			redhawk = getDriverInstance(nameServer);
			return (T) converter.convert(type, internalGet(redhawk, type, location));	
		}finally{
			if(redhawk!=null)
				redhawk.disconnect();
		}
	}

	public void registerRemoteDomain(String nameServer, String type, String location, RemoteDomainRegistrar registerRequest) throws Exception {
		Redhawk redhawk;
		try {
			redhawk = getDriverInstance(nameServer);
			
			RedhawkDomainManager dom = internalGet(redhawk, type, location);
			
			dom.registerRemoteDomainManager(registerRequest.getDomainName(), registerRequest.getNameServerHost(), 
					registerRequest.getNameServerPort());
		} catch (Exception e) {
			throw new Exception("Unable to register remote domain manager "+e.getMessage());
		}
	}
	
	public void unregisterRemoteDomain(String nameServer, String type, String location, String remoteDomainName) throws Exception {
		Redhawk redhawk;
		try {
			redhawk = getDriverInstance(nameServer);
			
			RedhawkDomainManager dom = internalGet(redhawk, type, location);
			
			dom.unregisterRemoteDomainManager(remoteDomainName);
		} catch (Exception e) {
			throw new Exception("Unable to unregister remote domain manager "+e.getMessage());
		}
	}
	
	public <T> PortStatisticsContainer getRhPortStatistics(String nameServer, String type, String location)
			throws Exception {
		Redhawk redhawk = getDriverInstance(nameServer);
		String[] locationArray = location.split("/");
		T port = internalGet(redhawk, type, locationArray);
		List<RedhawkPortStatistics> stats = new ArrayList<>();

		if (port instanceof RedhawkPort) {
			RedhawkPort rhPort = (RedhawkPort) port;
			stats.addAll(rhPort.getPortStatistics());
		}
		return new PortStatisticsContainer(stats);
	}
	
	public <T> SRIContainer getSRI(String nameServer, String type, String location)
			throws Exception {
		Redhawk redhawk = getDriverInstance(nameServer);
		String[] locationArray = location.split("/");
		T port = internalGet(redhawk, type, locationArray);
		List<RedhawkStreamSRI> sri = new ArrayList<>();

		if (port instanceof RedhawkPort) {
			RedhawkPort rhPort = (RedhawkPort) port;
			sri.addAll(rhPort.getActiveSRIs());
		}
		
		return new SRIContainer(sri);
	}
	
	public <T> void disconnectConnectionById(String nameServer, String type, String portLocation, String connectionId) throws Exception {
		try {
			Redhawk redhawk = getDriverInstance(nameServer);
			
			String[] locationArray = portLocation.split("/");
			
			T port = internalGet(redhawk, type, locationArray);
			if(port instanceof RedhawkPort){
				RedhawkPort rhPort = (RedhawkPort) port;
				rhPort.disconnect(connectionId);
			}
		} catch (Exception e) {
			logger.error("Error disconnecting port "+e.getMessage());
			throw new Exception("Error disconnecting port", e);
		}
	}

	public void deleteEventChannel(String nameServer, String domainName, String eventChannelName) {
		Redhawk redhawk = null;
		boolean createdNewInstance = false;

		try {
			if (redhawkDrivers.get(nameServer) != null) {
				redhawk = redhawkDrivers.get(nameServer);
			} else {
				if (nameServer.contains(":")) {
					String[] hostAndPort = nameServer.split(":");
					redhawk = new RedhawkDriver(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
					createdNewInstance = true;
				} else {
					throw new ResourceNotFoundException(
							"You did not specify a valid host and port to the REDHAWK name server. An example of a valid url is: localhost:2809");
				}
			}

			logger.debug("Trying to delete this event channel: " + eventChannelName);
			redhawk.getDomain(domainName).getEventChannelManager().releaseEventChannel(eventChannelName);
		} catch (ConnectionException e1) {
			e1.printStackTrace();
		} catch (CORBAException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ResourceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EventChannelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (redhawk != null && createdNewInstance) {
				redhawk.disconnect();
			}
		}
	}

	public void createEventChannel(String nameServer, String domainName, String eventChannelName) {
		Redhawk redhawk = null;
		boolean createdNewInstance = false;

		try {
			if (redhawkDrivers.get(nameServer) != null) {
				redhawk = redhawkDrivers.get(nameServer);
			} else {
				if (nameServer.contains(":")) {
					String[] hostAndPort = nameServer.split(":");
					redhawk = new RedhawkDriver(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
					createdNewInstance = true;
				} else {
					throw new ResourceNotFoundException(
							"You did not specify a valid host and port to the REDHAWK name server. An example of a valid url is: localhost:2809");
				}
			}

			logger.debug("Trying to create this event channel: " + eventChannelName);
			redhawk.getDomain(domainName).getEventChannelManager().createEventChannel(eventChannelName);
		} catch (ConnectionException e1) {
			e1.printStackTrace();
		} catch (CORBAException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ResourceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EventChannelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (redhawk != null && createdNewInstance) {
				redhawk.disconnect();
			}
		}
	}
	
	public void unsubscribeFromEventChannel(String nameServer, String domainName, String eventChannelName, String repId) {
		Redhawk redhawk = null;
		boolean createdNewInstance = false;

		try {
			if (redhawkDrivers.get(nameServer) != null) {
				redhawk = redhawkDrivers.get(nameServer);
			} else {
				if (nameServer.contains(":")) {
					String[] hostAndPort = nameServer.split(":");
					redhawk = new RedhawkDriver(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
					createdNewInstance = true;
				} else {
					throw new ResourceNotFoundException(
							"You did not specify a valid host and port to the REDHAWK name server. An example of a valid url is: localhost:2809");
				}
			}

			logger.debug("Trying to create this event channel: " + eventChannelName);
			redhawk.getDomain(domainName).getEventChannelManager().getEventChannel(eventChannelName)
				.unsubscribe(new RedhawkEventRegistrant(repId, eventChannelName, null));
		} catch (ConnectionException e1) {
			e1.printStackTrace();
		} catch (CORBAException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ResourceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MultipleResourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EventChannelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (redhawk != null && createdNewInstance) {
				redhawk.disconnect();
			}
		}
	}

	public void createApplication(String nameServer, String domainName, String instanceName, WaveformInfo info)
			throws ResourceNotFoundException, ApplicationCreationException {
		Redhawk redhawk = null;
		boolean createdNewInstance = false;

		try {
			if (redhawkDrivers.get(nameServer) != null) {
				redhawk = redhawkDrivers.get(nameServer);
			} else {
				if (nameServer.contains(":")) {
					String[] hostAndPort = nameServer.split(":");
					redhawk = new RedhawkDriver(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
					createdNewInstance = true;
				} else {
					throw new ResourceNotFoundException(
							"You did not specify a valid host and port to the REDHAWK name server. An example of a valid url is: localhost:2809");
				}
			}

			logger.debug(
					"Trying to create this application: " + instanceName + " sadLocation: " + info.getSadLocation());
			RedhawkApplication application = redhawk.getDomain(domainName).createApplication(instanceName,
					info.getSadLocation());
			try {
				application.start();
			} catch (ApplicationStartException e) {
				throw new ApplicationCreationException("Unable to start the application", e);
			}

		} catch (ConnectionException e1) {
			e1.printStackTrace();
		} catch (CORBAException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			if (redhawk != null && createdNewInstance) {
				redhawk.disconnect();
			}
		}
	}

	// TODO: Release/Create/Contol have a lot of similar code make this simpler
	public void controlApplication(String nameServer, String domainName, String appId, String control)
			throws Exception {
		Redhawk redhawk = null;
		boolean createdNewInstance = false;

		try {
			if (redhawkDrivers.get(nameServer) != null) {
				redhawk = redhawkDrivers.get(nameServer);
			} else {
				if (nameServer.contains(":")) {
					String[] hostAndPort = nameServer.split(":");
					redhawk = new RedhawkDriver(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
					createdNewInstance = true;
				} else {
					throw new ResourceNotFoundException(
							"You did not specify a valid host and port to the REDHAWK name server. An example of a valid url is: localhost:2809");
				}
			}

			RedhawkApplication application;
			try {
				application = redhawk.getDomain(domainName).getApplicationByIdentifier(appId);
			} catch (ResourceNotFoundException ex) {
				logger.debug("Unable to application by Identifier: " + appId + " trying by Name");
				application = redhawk.getDomain(domainName).getApplicationByName(appId);
			}

			if (application == null) {
				throw new ResourceNotFoundException("Could not find application with an Identifier of: " + appId);
			}

			if (control.equalsIgnoreCase("stop")) {
				application.stop();
			} else if (control.equalsIgnoreCase("start")) {
				application.start();
			} else {
				throw new Exception(
						"Unknown control string " + control + " appropriate commands are 'start' or 'stop'");
			}
		} finally {
			if (redhawk != null && createdNewInstance) {
				redhawk.disconnect();
			}
		}
	}

	public void releaseApplication(String nameServer, String domainName, String appId) throws Exception {
		Redhawk redhawk = null;
		boolean createdNewInstance = false;

		try {
			if (redhawkDrivers.get(nameServer) != null) {
				redhawk = redhawkDrivers.get(nameServer);
			} else {
				if (nameServer.contains(":")) {
					String[] hostAndPort = nameServer.split(":");
					redhawk = new RedhawkDriver(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
					createdNewInstance = true;
				} else {
					throw new ResourceNotFoundException(
							"You did not specify a valid host and port to the REDHAWK name server. An example of a valid url is: localhost:2809");
				}
			}

			RedhawkApplication application;
			try {
				application = redhawk.getDomain(domainName).getApplicationByIdentifier(appId);
			} catch (ResourceNotFoundException ex) {
				logger.debug("Unable to application by Identifier: " + appId + " trying by Name");
				application = redhawk.getDomain(domainName).getApplicationByName(appId);
			}

			if (application == null) {
				throw new ResourceNotFoundException("Could not find application with an Identifier of: " + appId);
			}

			application.release();

		} finally {
			if (redhawk != null && createdNewInstance) {
				redhawk.disconnect();
			}
		}
	}

	public void shutdownDeviceManager(String nameServer, String deviceManagerLocation) throws Exception {
		// TODO: Refactor to clean this code up
		Redhawk redhawk = null;
		boolean createdNewInstance = false;

		try {
			if (redhawkDrivers.get(nameServer) != null) {
				redhawk = redhawkDrivers.get(nameServer);
			} else {
				if (nameServer.contains(":")) {
					String[] hostAndPort = nameServer.split(":");
					redhawk = new RedhawkDriver(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
					createdNewInstance = true;
				} else {
					throw new ResourceNotFoundException(
							"You did not specify a valid host and port to the REDHAWK name server. An example of a valid url is: localhost:2809");
				}
			}

			RedhawkDeviceManager deviceManager;
			try {
				redhawk.getDeviceManager(deviceManagerLocation).shutdown();
			} catch (ResourceNotFoundException | MultipleResourceException | CORBAException ex) {
				logger.debug("Issue retrieving DeviceManager at this location: " + deviceManagerLocation);
				throw new Exception("Unable to access DeviceManager", ex);
			}
		} finally {
			if (redhawk != null && createdNewInstance) {
				redhawk.disconnect();
			}
		}
	}

	public void allocateDevice(String nameServer, String deviceLocation, Map<String, Object> allocation)
			throws Exception {
		// TODO: Refactor to clean this code up
		Redhawk redhawk = null;
		boolean createdNewInstance = false;

		try {
			if (redhawkDrivers.get(nameServer) != null) {
				redhawk = redhawkDrivers.get(nameServer);
			} else {
				if (nameServer.contains(":")) {
					String[] hostAndPort = nameServer.split(":");
					redhawk = new RedhawkDriver(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
					createdNewInstance = true;
				} else {
					throw new ResourceNotFoundException(
							"You did not specify a valid host and port to the REDHAWK name server. An example of a valid url is: localhost:2809");
				}
			}

			RedhawkDeviceManager deviceManager;
			try {
				logger.info("Allocation is " + allocation);
				RedhawkDevice device = redhawk.getDevice(deviceLocation);

				device.allocate(allocation);
			} catch (ResourceNotFoundException | MultipleResourceException | CORBAException ex) {
				logger.debug("Issue allocating Device at this location: " + deviceLocation);
				throw new Exception("Unable allocate Device", ex);
			}
		} finally {
			if (redhawk != null && createdNewInstance) {
				redhawk.disconnect();
			}
		}
	}

	public void deallocateDevice(String nameServer, String deviceLocation, String allocationId) throws Exception {
		// TODO: Refactor to clean this code up
		Redhawk redhawk = null;
		boolean createdNewInstance = false;

		try {
			if (redhawkDrivers.get(nameServer) != null) {
				redhawk = redhawkDrivers.get(nameServer);
			} else {
				if (nameServer.contains(":")) {
					String[] hostAndPort = nameServer.split(":");
					redhawk = new RedhawkDriver(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
					createdNewInstance = true;
				} else {
					throw new ResourceNotFoundException(
							"You did not specify a valid host and port to the REDHAWK name server. An example of a valid url is: localhost:2809");
				}
			}

			try {
				RedhawkDevice device = redhawk.getDevice(deviceLocation);

				device.deallocate(allocationId);
			} catch (ResourceNotFoundException | MultipleResourceException | CORBAException ex) {
				logger.debug("Issue allocating Device at this location: " + deviceLocation);
				throw new Exception("Unable allocate Device", ex);
			}
		} finally {
			if (redhawk != null && createdNewInstance) {
				redhawk.disconnect();
			}
		}
	}
	
	public void setAdminState(String nameServer, String deviceLocation, AdminState state) throws Exception {
		// TODO: Refactor to clean this code up
		Redhawk redhawk = null;
		boolean createdNewInstance = false;

		try {
			if (redhawkDrivers.get(nameServer) != null) {
				redhawk = redhawkDrivers.get(nameServer);
			} else {
				if (nameServer.contains(":")) {
					String[] hostAndPort = nameServer.split(":");
					redhawk = new RedhawkDriver(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
					createdNewInstance = true;
				} else {
					throw new ResourceNotFoundException(
							"You did not specify a valid host and port to the REDHAWK name server. An example of a valid url is: localhost:2809");
				}
			}

			try {
				RedhawkDevice device = redhawk.getDevice(deviceLocation);

				device.adminState(state);
			} catch (ResourceNotFoundException | MultipleResourceException | CORBAException ex) {
				logger.debug("Issue allocating Device at this location: " + deviceLocation);
				throw new Exception("Unable allocate Device", ex);
			}
		} finally {
			if (redhawk != null && createdNewInstance) {
				redhawk.disconnect();
			}
		}
	}

	public List<Map<String, Object>> getTuners(String nameServer, String deviceLocation, TunerMode mode)
			throws Exception {
		// TODO: Refactor to clean this code up
		Redhawk redhawk = null;
		boolean createdNewInstance = false;

		try {
			if (redhawkDrivers.get(nameServer) != null) {
				redhawk = redhawkDrivers.get(nameServer);
			} else {
				if (nameServer.contains(":")) {
					String[] hostAndPort = nameServer.split(":");
					redhawk = new RedhawkDriver(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
					createdNewInstance = true;
				} else {
					throw new ResourceNotFoundException(
							"You did not specify a valid host and port to the REDHAWK name server. An example of a valid url is: localhost:2809");
				}
			}

			try {
				RedhawkDeviceImpl device = (RedhawkDeviceImpl) redhawk.getDevice(deviceLocation);

				switch (mode) {
				case ALL:
					return device.getAllTuners();
				case UNUSED:
					return device.getUnusedTuners();
				case USED:
					return device.getUsedTuners();
				default:
					return new ArrayList<>();
				}
			} catch (ResourceNotFoundException | MultipleResourceException | CORBAException ex) {
				logger.debug("Issue allocating Device at this location: " + deviceLocation);
				throw new Exception("Unable allocate Device", ex);
			}
		} finally {
			if (redhawk != null && createdNewInstance) {
				redhawk.disconnect();
			}
		}
	}

	public Map<String, Softwareassembly> getWaveforms(String nameServer, String domainName) throws Exception {
		Redhawk redhawk = null;
		boolean createdNewInstance = false;

		try {
			if (redhawkDrivers.get(nameServer) != null) {
				redhawk = redhawkDrivers.get(nameServer);
			} else {
				if (nameServer.contains(":")) {
					String[] hostAndPort = nameServer.split(":");
					redhawk = new RedhawkDriver(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
					createdNewInstance = true;
				} else {
					throw new ResourceNotFoundException(
							"You did not specify a valid host and port to the REDHAWK name server. An example of a valid url is: localhost:2809");
				}
			}

			return redhawk.getDomain(domainName).getFileManager().getWaveforms();

		} finally {
			if (redhawk != null && createdNewInstance) {
				redhawk.disconnect();
			}
		}
	}

	/**
	 * Returns the specified type object from the nameserver at the specified
	 * location
	 *
	 * @param nameServer
	 *            Location of the omniName Server
	 * @param type
	 *            Type of object to bring back (domain, application, component,
	 *            port, devicemanager, device, waveform)
	 * @param location
	 *            Name of the DOMAIN you're connecting too(i.e REDHAWK_DEV)
	 * @param fetchMode
	 *            Whether or not to return everything in the tree. Defaults to
	 *            FetchMode.EAGER
	 * @return Returns the model for the object type specified (i.e.
	 *         redhawk.rest.model.Domain)
	 * @throws ResourceNotFoundException
	 * @throws Exception
	 */
	public <T> List<T> getAll(String nameServer, String type, String location, FetchMode fetchMode)
			throws ResourceNotFoundException, Exception {
		Redhawk redhawk = null;

		try {
			if (redhawkDrivers.get(nameServer) != null) {
				redhawk = redhawkDrivers.get(nameServer);
			} else {
				if (nameServer.contains(":")) {
					String[] hostAndPort = nameServer.split(":");
					redhawk = new RedhawkDriver(hostAndPort[0], Integer.parseInt(hostAndPort[1]));
				} else {
					throw new ResourceNotFoundException(
							"You did not specify a valid host and port to the REDHAWK name server. An example of a valid url is: localhost:2809");
				}
			}

			return (List<T>) converter.convertAll(type, internalGetAll(redhawk, type, location), fetchMode);

		} finally {
			if (redhawk != null) {
				redhawk.disconnect();
			}
		}
	}

	/**
	 * Returns a PropertyContainer for the specified type at a specific location
	 *
	 * @param nameServer
	 * @param type
	 * @param location
	 * @return
	 * @throws Exception
	 */
	public PropertyContainer getProperties(String nameServer, String type, String... location) throws Exception {
		Redhawk redhawk = getDriverInstance(nameServer);

		// QueryableResource resource = internalGet(redhawk, type, location);
		// Object object = converter.convert(type, resource);

		Map<String, RedhawkProperty> properties = null;
		Properties propConfig = null;
		String assemblyControllerId = null;
		Externalproperties exProps = null;

		switch (type) {
		case "domain": {
			RedhawkDomainManager domain = internalGet(redhawk, type, location);
			properties = domain.getProperties();
			propConfig = domain.getPropertyConfiguration();
			break;
		}
		case "application": {
			RedhawkApplication application = internalGet(redhawk, type, location);
			properties = application.getProperties();

			// Get Component Refs for Assembly Controller and ExternalProps
			assemblyControllerId = application.getAssembly().getAssemblycontroller().getComponentinstantiationref()
					.getRefid();
			exProps = application.getAssembly().getExternalproperties();

			break;
		}
		case "devicemanager": {
			RedhawkDeviceManager devMgr = internalGet(redhawk, type, location);
			properties = devMgr.getProperties();
			break;
		}
		case "component": {
			RedhawkComponent comp = internalGet(redhawk, type, location);
			properties = comp.getProperties();
			propConfig = comp.getPropertyConfiguration();
			break;
		}
		case "device": {
			RedhawkDevice device = internalGet(redhawk, type, location);
			properties = device.getProperties();
			propConfig = device.getPropertyConfiguration();
			break;
		}
		}

		List<Property> propertyList = new ArrayList<>();
		if (type.equals("application")) {
			// Get all property objects from assembly controller
			RedhawkComponent acComp = internalGet(redhawk, "component",
					location[0] + "/" + assemblyControllerId + ".*");

			// Convert AC props
			propertyList = converter.convertProperties(acComp.getProperties(), acComp.getPropertyConfiguration());

			// Now Add any additional External Props
			if(exProps!=null){
			for (redhawk.driver.xml.model.sca.sad.Property prop : exProps.getProperties()) {
					Property exProp = this.getProperty(prop.getPropid(), nameServer, "component",
						location[0] + "/" + prop.getComprefid() + ".*");

					// TODO: Possibly add another field here for external propId
					exProp.setExternalId(prop.getExternalpropid());
					propertyList.add(exProp);
				}
			}
		} else {
			propertyList = converter.convertProperties(properties, propConfig);
		}
		return new PropertyContainer(propertyList);
	}

	public Property getProperty(String propertyId, String nameServer, String type, String... location)
			throws Exception {
		List<Property> properties = getProperties(nameServer, type, location).getProperties();
		List<Property> filteredProps = properties.parallelStream().filter(p -> {
			return p.getId().equalsIgnoreCase(propertyId);
		}).collect(Collectors.toList());

		if (filteredProps.size() > 0) {
			return filteredProps.get(0);
		} else {
			return null;
		}
	}

	public void setProperty(FullProperty prop, String nameServer, String type, String... location) throws Exception {
		Redhawk redhawk = getDriverInstance(nameServer);
		QueryableResource resource = internalGet(redhawk, type, location);
		internalSetProperty(resource, prop);
	}

	public void setProperties(List<FullProperty> properties, String nameServer, String type, String... location)
			throws Exception {
		Redhawk redhawk = getDriverInstance(nameServer);
		QueryableResource resource = internalGet(redhawk, type, location);
		properties.stream().forEach(property -> {
			try {
				internalSetProperty(resource, property);
			} catch (Exception e) {
				logger.error("Problem setting property: " + property.getId(), e);
			}
		});
	}
	
	private <T> T internalGet(Redhawk redhawk, String type, String... location)
			throws ResourceNotFoundException, Exception {
		switch (type) {
		case "domain":
			RedhawkDomainManager domain = null;
			try {
				domain = redhawk.getDomain(location[0]);
			}catch(Exception ex) {
				//Try get the domain as a remote domain
				logger.warn("Can't find domain name on local nameserver. Attempting to get remote domain. Note:"
						+" This will only work w/ REST if remote Domain was registered via the REDHAWK Driver.");
				RedhawkDomainManagerImpl impl = (RedhawkDomainManagerImpl) domain;
				if(impl.getDriverRegisteredRemoteDomainManager().containsKey(location[0])) {
					domain = impl;
				}else {
					throw new ResourceNotFoundException("Issue finding resource "+location[0]);
				}
			}
			return (T) domain;
		case "application":
			return (T) redhawk.getApplication(location[0]);
		case "applicationport":
			// Location array should be of this for <Domain Name>/<application>/<port>
			String appAddress = location[0]+"/"+location[1];
			String portName = location[2];
			return (T) redhawk.getApplication(appAddress).getPort(portName);
		case "devicemanager":
			return (T) redhawk.getDeviceManager(location[0]);
		case "eventchannel":
			String domainName = location[0];
			String eventChannelName = location[1];
			return (T) redhawk.getDomain(domainName).getEventChannelManager().getEventChannel(eventChannelName);
		case "component":
			return (T) redhawk.getComponent(Arrays.stream(location).collect(Collectors.joining("/")));
		case "port":
			return (T) redhawk.getPort(Arrays.stream(location).collect(Collectors.joining("/")));
		case "device":
			return (T) redhawk.getDevice(Arrays.stream(location).collect(Collectors.joining("/")));
		case "softwarecomponent":
			return (T) redhawk.getComponent(Arrays.stream(location).collect(Collectors.joining("/")))
					.getSoftwareComponent();
		}

		throw new ResourceNotFoundException("Could not locate object of type: " + type + " at: " + location);
	}

	private <T> List<T> internalGetAll(Redhawk redhawk, String type, String... location)
			throws ResourceNotFoundException, Exception {
		/*
		 * Convert Location to array list and run parallel stream on it
		 */
		switch (type) {
		case "domain":
			return (List<T>) redhawk.getDomains().values().stream().collect(Collectors.toList());
		case "application":
			return (List<T>) redhawk.getDomain(location[0]).getApplications();
		case "devicemanager":
			return (List<T>) redhawk.getDomain(location[0]).getDeviceManagers();
		case "component":
			return (List<T>) redhawk.getApplication(Arrays.stream(location).collect(Collectors.joining("/")))
					.getComponents();
		case "port":
			return (List<T>) redhawk.getComponent(Arrays.stream(location).collect(Collectors.joining("/"))).getPorts();
		case "deviceport":
			// TODO: Figure out why streams not working on deviceLocation
			// return (List<T>)
			// redhawk.getDevice(Arrays.stream(location).collect(Collectors.joining("/"))).getPorts();
			RedhawkDevice device = redhawk.getDevice(location[0]);
			return (List<T>) device.getPorts();
		case "applicationport":
			RedhawkApplication app = redhawk.getApplication(location[0]);
			return (List<T>) app.getPorts();
		case "eventchannel":
			return (List<T>) redhawk.getDomain(location[0]).getEventChannelManager().getEventChannels();
		case "device":
			return (List<T>) redhawk.getDeviceManager(Arrays.stream(location).collect(Collectors.joining("/")))
					.getDevices();
		// case "softwarecomponent": return (List<T>)
		// redhawk.getComponent(Arrays.stream(location).collect(Collectors.joining("/"))).getSoftwareComponent();
		}

		throw new ResourceNotFoundException("Could not locate object of type: " + type + " at: " + location);
	}

	private Object convertSimple(String type, String value) throws Exception {

		if (type.equals("java.lang.Integer")) {
			return Integer.parseInt(value);
		} else if (type.equals("java.math.BigInteger")) {
			return new BigInteger(value);
		} else if (type.equals("java.lang.Byte")) {
			return Byte.parseByte(value);
		} else if (type.equals("java.lang.Double")) {
			return Double.parseDouble(value);
		} else if (type.equals("java.lang.Float")) {
			return Float.parseFloat(value);
		} else if (type.equals("java.lang.String")) {
			return value;
		} else if (type.equals("java.lang.Long")) {
			return Long.parseLong(value);
		} else if (type.equals("java.lang.Character")) {
			return value.toCharArray()[0];
		} else if (type.equals("java.lang.Short")) {
			return Short.parseShort(value);
		} else if (type.equals("java.lang.Boolean")) {
			return Boolean.parseBoolean(value);
		}

		throw new IllegalArgumentException("Could not determine simple type for: " + type + " with value: " + value);
	}

	private void internalSetProperty(QueryableResource resource, FullProperty prop) throws Exception {

		switch (prop.getType()) {
		case "simple": {
			RedhawkSimple simple = resource.getProperty(prop.getId());

			if (simple != null) {
				simple.setValue(convertSimple(prop.getDataType(), prop.getValue()));
			}
			break;
		}
		case "simplesequence": {
			RedhawkSimpleSequence simpSeq = resource.getProperty(prop.getId());
			simpSeq.clearAllValues();
			simpSeq.addValues(convertSimpleSequenceValues(prop.getValues(), prop.getDataType()));
			break;
		}
		case "struct": {
			RedhawkStruct struct = resource.getProperty(prop.getId());
			Map<String, Object> values = new HashMap<String, Object>();
			prop.getAttributes().forEach(p -> {
				if (p.getType().equals("simple")) {
					try {
						values.put(p.getId(), convertSimple(p.getDataType(), p.getValue()));
					} catch (Exception e) {
						logger.error(String.format("Exception converting %s to %s: ", p.getValue(), prop.getDataType()),
								e);
					}
				} else if (p.getType().equals("simplesequence")) {
					values.put(p.getId(), convertSimpleSequenceValues(p.getValues(), p.getDataType()));
				}
			});
			struct.setValues(values);
			break;
		}
		case "structsequence": {
			RedhawkStructSequence structSequence = resource.getProperty(prop.getId());
			structSequence.removeAllStructs();

			List<Map<String, Object>> elements = prop.getStructs().stream().map(struct -> {
				Map<String, Object> values = new HashMap<String, Object>();
				struct.getAttributes().stream().forEach(v -> {
					try {
						if (v.getType().equals("simple")) {
							values.put(v.getId(), convertSimple(v.getDataType(), v.getValue()));
						} else if (v.getType().equals("simplesequence")) {
							values.put(v.getId(), convertSimpleSequenceValues(v.getValues(), v.getDataType()));
						}
					} catch (Exception e) {
						logger.error("EXCEPTION IN INTERNAL SET PROPERTY: ", e);
					}
				});
				return values;
			}).collect(Collectors.toList());
			structSequence.addStructsToSequence(elements);
			break;
		}
		}
	}

	public BundleContext getContext() {
		return context;
	}

	public void setContext(BundleContext context) {
		this.context = context;
	}

	public void bindRedhawk(ServiceReference<Redhawk> reference) {
		String connectionName = (String) reference.getProperty("connectionName");
		logger.debug("BINDING REDHAWK SERVICE: " + connectionName);
		if (connectionName != null) {
			try {
				Redhawk rh = context.getService(reference);
				redhawkDrivers.put(connectionName, rh);
			} catch (Exception ex) {
				logger.error("Error trying to get reference from the context ", ex);
			}
		}
	}

	public void unbindRedhawk(ServiceReference<Redhawk> reference) {
		if (reference != null && reference.getProperty("connectionName") != null) {
			String connectionName = (String) reference.getProperty("connectionName");
			logger.debug("UNBINDING REDHAWK SERVICE: " + connectionName);
			if (connectionName != null) {
				redhawkDrivers.remove(connectionName);
			}
		} else {
			logger.debug("Unable to unbind Null reference passed in or no connectionName present");
		}
	}

	public List<ServiceReference<Redhawk>> getRedhawkDriverServices() {
		return redhawkDriverServices;
	}

	public void setRedhawkDriverServices(List<ServiceReference<Redhawk>> redhawkDriverServices) {
		this.redhawkDriverServices = redhawkDriverServices;
	}

	/**
	 * 
	 * @param oldValues
	 *            values that may be converted
	 * @param dataType
	 *            oldValues should be converted to this type
	 * @return
	 */
	private Object[] convertSimpleSequenceValues(List<Object> oldValues, String dataType) {
		List<Object> newValues = oldValues.stream().map(objA -> {
			if (objA instanceof String) {
				Object objB = null;
				try {
					objB = convertSimple(dataType, (String) objA);
				} catch (Exception e) {
					logger.error(String.format("Exception converting %s to %s: ", objA, dataType), e);
				}
				return objB;
			} else {
				return objA;
			}
		}).collect(Collectors.toList());
		return newValues.toArray();
	}	
}