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
package redhawk.connector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedServiceFactory;

import redhawk.driver.Redhawk;
import redhawk.driver.RedhawkDriver;
import redhawk.driver.domain.RedhawkDomainManager;

public class RedhawkConnector implements ManagedServiceFactory {

	private static Logger logger = Logger.getLogger(RedhawkConnector.class.getName());

	private BundleContext bundleContext;
	private Map<String, ServiceRegistration<Redhawk>> redhawkRegistrationEntries = new HashMap<>();
	private Map<String, RedhawkDomainManager> redhawkDomainManagers = new HashMap<>();
	private String name = "redhawk.datasource.factory";

	private static final String CONNECTION_NAME_PROPERTY = "connectionName";
	private static final String HOST_NAME_PROPERTY = "host";
	private static final String PORT_NAME_PROPERTY = "port";
	private static final String DOMAIN_MANAGER_PROPERTY = "domainManager";
	private static final String DEVICE_MANAGER_NAME_PROPERTY = "deviceManagerName";
	private static final String DEVICE_MANAGER_FS_ROOT_PROPERTY = "deviceManagerFileSystemRoot";

	public void updated(String pid, Dictionary properties) throws ConfigurationException {

		String connectionName = dynamicPropertyConversion(properties, CONNECTION_NAME_PROPERTY, String.class);
		if (isEmpty(connectionName)) {
			throw new ConfigurationException(CONNECTION_NAME_PROPERTY, "Connection Name is Required");
		}

		String host = dynamicPropertyConversion(properties, HOST_NAME_PROPERTY, String.class);

		if (isEmpty(host)) {
			throw new ConfigurationException(HOST_NAME_PROPERTY, "Host Name is Required");
		}

		Long port = null;

		try {
			port = dynamicPropertyConversion(properties, PORT_NAME_PROPERTY, Long.class);
		} catch (ClassCastException e) {
			try {
				port = Long.parseLong((String) properties.get(PORT_NAME_PROPERTY));
			} catch (NumberFormatException e1) {
				logger.severe(e.getMessage());
			}
		}

		if (port == null) {
			throw new ConfigurationException(PORT_NAME_PROPERTY, "Port is Required");
		}

		try {
			logger.fine("Checking for a pre-existing REDHAWK connection with the connectionName of: " + connectionName);
			ServiceReference<?>[] existingRHConnection = bundleContext.getAllServiceReferences(Redhawk.class.getName(),
					null);

			if (existingRHConnection != null) {
				String servicePid = (String) existingRHConnection[0].getProperty("service.pid");
				String curConnectionName = (String) existingRHConnection[0].getProperty(CONNECTION_NAME_PROPERTY);

				if (!pid.equalsIgnoreCase(servicePid) && connectionName.equalsIgnoreCase(curConnectionName)) {
					throw new ConfigurationException(CONNECTION_NAME_PROPERTY,
							"A redhawk connection with this name already exists");
				}
				
				String curHost = dynamicPropertyConversion(existingRHConnection[0].getProperty(HOST_NAME_PROPERTY), String.class);
				long curPort = dynamicPropertyConversion(existingRHConnection[0].getProperty(PORT_NAME_PROPERTY), Long.class);

				if (!pid.equalsIgnoreCase(servicePid) && curHost.equalsIgnoreCase(host) && curPort == port) {
					throw new ConfigurationException(CONNECTION_NAME_PROPERTY,
							"A connection has already been established with this REDHAWK Domain from this Karaf Instance");
				}
			}
		} catch (InvalidSyntaxException e) {
			logger.log(Level.SEVERE, "An InvalidSyntaxException has occurred", e);
			throw new ConfigurationException("InvalidSyntaxException", e.getMessage());
		}

		if (connectionName.equalsIgnoreCase("redhawk")) {
			logger.warning(
					"Connection Name is redhawk. This will conflict with the REDHAWK Camel Component if it is installed. "
							+ "Appending host name to the connectionName for uniqueness");
			connectionName = connectionName + "-" + host.replaceAll("\\.", "_");
			properties.put(CONNECTION_NAME_PROPERTY, connectionName);
		}

		// remove old if updating.
		if (redhawkRegistrationEntries.get(pid) != null) {
			deleted(pid);
		}

		// Domain Manager is optional. If its specified, connect and register a
		// device manager.
		String domainManagerName = dynamicPropertyConversion(properties.get(DOMAIN_MANAGER_PROPERTY), String.class);
		String deviceManagerName = dynamicPropertyConversion(properties.get(DEVICE_MANAGER_NAME_PROPERTY), String.class);

		Redhawk redhawkDriver = new RedhawkDriver(host, port.intValue());

		logger.fine("domainManagerName : " + domainManagerName);
		if (!domainManagerName.isEmpty() && !isEmpty(deviceManagerName)) {
			try {
				RedhawkDomainManager domMgr = redhawkDriver.getDomain(domainManagerName);
				String deviceManagerFileSystemRoot = (String) properties.get(DEVICE_MANAGER_FS_ROOT_PROPERTY);

				if (isEmpty(deviceManagerFileSystemRoot)) {
					deviceManagerFileSystemRoot = System.getProperty("karaf.base");
				}

				domMgr.createDeviceManager(deviceManagerName, deviceManagerFileSystemRoot, true);
				redhawkDomainManagers.put(pid, domMgr);
			} catch (Exception e) {
				logger.log(Level.SEVERE,
						"There was a problem attempting to create the device manager.  The device manager will not be created at this time. Please update your configuration file.",
						e);
				return;
			}
		}

		ServiceRegistration<Redhawk> registration = (ServiceRegistration<Redhawk>) bundleContext
				.registerService(Redhawk.class.getName(), redhawkDriver, properties);
		redhawkRegistrationEntries.put(pid, registration);
	}

	public void deleted(String pid) {
		// DomainManager connections are optional, so only remove if we actually
		// have one
		RedhawkDomainManager dom = redhawkDomainManagers.get(pid);
		if (dom != null) {
			dom.unRegisterAllDriverRegisteredDeviceManagers();
			redhawkDomainManagers.remove(pid);
		}

		ServiceRegistration<Redhawk> sreg = redhawkRegistrationEntries.get(pid);
		if (sreg != null && sreg.getReference() != null) {
			// Clean up the service registry so that nobody can get the service
			// while we're shutting it down
			Redhawk redhawk = bundleContext.getService(sreg.getReference());
			if (redhawk != null) {
				redhawk.disconnect();
			}

			bundleContext.ungetService(sreg.getReference());
			sreg.unregister();
			redhawkRegistrationEntries.remove(pid);
		}
	}

	public void shutdown() {
		for (String pid : redhawkRegistrationEntries.keySet()) {
			deleted(pid);
		}
		redhawkRegistrationEntries.clear();
		redhawkDomainManagers.clear();
	}

	public String getName() {
		return name;
	}

	public BundleContext getBundleContext() {
		return bundleContext;
	}

	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public void setName(String name) {
		this.name = name;
	}

	private boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}

	private <T> T dynamicPropertyConversion(Dictionary properties, String name, Class aClass) throws ConfigurationException {
		try{
			return this.dynamicPropertyConversion(properties.get(name), aClass);
		}catch(ConfigurationException ex){
			throw new ConfigurationException(name, "Unable to get property "+properties.get(name)+" w/ key "+name+", likely bad formatting.", ex.getCause());
		}
	}
	
	private <T> T dynamicPropertyConversion(Object property, Class aClass) throws ConfigurationException {
		Method meth;
		
		if(property!=null){
			try {
				if(!aClass.isInstance(new String())){
					meth = aClass.getMethod("valueOf", String.class);
					return (T) meth.invoke(property, property.toString());				
				}else{
					return (T) property.toString();
				}
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				throw new ConfigurationException(name, "Unable to get property "+property+" likely bad formatting.", e.getCause());
			}			
		}else{
			logger.warning("Received Null for one of the properties being checked");
			return null;
		}
	}

}