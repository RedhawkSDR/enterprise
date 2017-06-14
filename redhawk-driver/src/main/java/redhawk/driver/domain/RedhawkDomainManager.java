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

import java.io.File;
import java.util.List;
import java.util.Map;

import redhawk.driver.Redhawk;
import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.application.impl.RedhawkApplicationFactoryImpl;
import redhawk.driver.base.QueryableResource;
import redhawk.driver.device.RedhawkDevice;
import redhawk.driver.devicemanager.DeviceManagerInturruptedCallback;
import redhawk.driver.devicemanager.RedhawkDeviceManager;
import redhawk.driver.devicemanager.RedhawkService;
import redhawk.driver.domain.impl.RedhawkAllocationManagerImpl;
import redhawk.driver.eventchannel.RedhawkEventChannelManager;
import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.xml.model.sca.dmd.Domainmanagerconfiguration;
import redhawk.driver.xml.model.sca.prf.Properties;
import redhawk.driver.xml.model.sca.sad.Softwareassembly;
import redhawk.driver.xml.model.sca.spd.Softpkg;
import CF.DomainManager;

/**
 * POJO representing a REDHAWK Domain Manager. 
 */
public interface RedhawkDomainManager extends QueryableResource {
	/**
	 * Returns the name of this {@link RedhawkDomainManager} 
	 * 
	 * @return The name of the REDHAWK domain.  This value comes from the name attribute in the DomainManager.dcd.xml file
	 */
    String getName();
    
    /**
     * Returns the Identifier for this {@link RedhawkDomainManager} 
     * 
     * @return The unique identifier of the REDHAWK domain.  The value comes from the id attribute in the DomainManager.dcd.xml file.
     * @throws ConnectionException
     */
    String getIdentifier();
    
    /**
     * Returns the {@link RedhawkDeviceManager} for this object
     * @return
     */
    Map<String, RedhawkDeviceManager> deviceManagers();
    
    /**
     * Return the applications available for this object. 
     * @return
     */
    Map<String, RedhawkApplication> applications();
    
    /**
     * Returns the devices availble for this object.
     * @return
     */
	Map<String, RedhawkDevice> devices();
    
    /**
     * @return The remote DomainManager Object Reference from the ORB.
     */
    DomainManager getCorbaObj();
   
    /**
     * Returns the {@link RedhawkFileManager} for this object. 
     * 
     * @return A POJO representing the {@link CF.FileManager} object. 
     * @throws ConnectionException
     */
    RedhawkFileManager getFileManager() throws ConnectionException;
    
    /**
     * Returns the {@link redhawk.driver.RedhawkDriver} used to create this {@link redhawk.driver.domain.RedhawkDomainManager}
     * @return
     */
    Redhawk getDriver();

    /**
     * Returns a list of {@link redhawk.driver.devicemanager.RedhawkDeviceManager}
     * @return
     */
    List<RedhawkDeviceManager> getDeviceManagers();
    
    /**
     * Returns a {@link redhawk.driver.devicemanager.RedhawkDeviceManager} 
     * @param name
     * 	Name of the DeviceManager you'd like to access. 
     * @return
     */
    List<RedhawkDeviceManager> getDeviceManagersByName(String name);

    /**
     * Returns the {@link RedhawkDeviceManager} at the specified identifier. 
     * 
 	 * @param identifier a {@link java.util.regex.Pattern#sum regular expression} specifying the DCE UUID of the Device Manager to get
 	 * @return a device manager with the specified identifier, or <code>null</code> if one could not be found.  If multiple device managers match, then an arbitrary one is returned.
     * @throws ResourceNotFoundException 
 	 */
    RedhawkDeviceManager getDeviceManagerByIdentifier(String identifier) throws ResourceNotFoundException;
    
    /**
     * Returns the {@link RedhawkDeviceManager} with the specified name. 
     * 
     * @param name a {@link java.util.regex.Pattern#sum regular expression} specifying the name of the Device Manager to get
     * @return a device manager with the specified name, or <code>null</code> if one could not be found.
     * @throws MultipleResourceException if multiple device managers match
     * @throws ResourceNotFoundException 
     */
    RedhawkDeviceManager getDeviceManagerByName(String name) throws MultipleResourceException, ResourceNotFoundException;

    /**
     * Returns a list of {@link redhawk.driver.device.RedhakwDevice}
     * @return
     */
    List<RedhawkDevice> getDevices();
    
    /**
     * Returns a list of {@link redhawk.driver.device.RedhakwDevice} with the given name regex. 
     * @param deviceName
     * 	Name of device you'd like to access. 
     * @return
     */
 	List<RedhawkDevice> getDevicesByName(String deviceName);
 	
 	/**
 	 * Returns a {@link redhawk.driver.device.RedhakwDevice} with the specified name. 
 	 * @param deviceName
 	 * 	Device name your searching for. 
 	 * @return
 	 * @throws MultipleResourceException
 	 */
 	RedhawkDevice getDeviceByName(String deviceName) throws MultipleResourceException;
 	
 	/**
 	 * Returns a {@link redhawk.driver.device.RedhakwDevice} by it's identifier
 	 * @param identifier
 	 * 	Identifier for RedhawkDevice you'd like to access. 
 	 * @return
 	 */
 	RedhawkDevice getDeviceByIdentifier(String identifier);
 	
 	/**
 	 * Returns a list of POJOs {@link RedhawkService} 
 	 * @return
 	 */
	List<RedhawkService> getServices();
	
	/**
	 * Returns a list {@link RedhawkService} that match the provided serviceName
	 * @param serviceName
	 * 	Name of the service 
	 * @return
	 */
	List<RedhawkService> getServicesByName(String serviceName);
	
	/**
	 * Returns a {@link RedhawkService} given a serviceName
	 * @param serviceName
	 * 	Name of the service 
	 * @return
	 * @throws MultipleResourceException
	 */
	RedhawkService getServiceByName(String serviceName) throws MultipleResourceException;
	
	/**
	 * Creates an {@link RedhawkApplication} based on a provided {@link java.io.File}
	 * @param instanceName
	 * 	Name of the application
	 * @param sadFile
	 * 	File representation of the Software Assembly Descriptor
	 * @return
	 * @throws ApplicationCreationException
	 */
	RedhawkApplication createApplication(String instanceName, File sadFile) throws ApplicationCreationException;
	
	/**
	 * Creates an {@link RedhawkApplication} based on a provided {@link Softwareassembly} 
	 * @param instanceName
	 * 	Name of the application
	 * @param softwareAssembly
	 * @return
	 * @throws ApplicationCreationException
	 */
	RedhawkApplication createApplication(String instanceName, Softwareassembly softwareAssembly) throws ApplicationCreationException;
	
	/**
	 * Creates an {@link RedhawkApplication} based on a provided sadFileDestination. 
	 * @param instanceName
	 * 	Name of the application
	 * @param sadFileDestinatio
	 * 	String representing the the location of your SAD file in your REDHAWK Domain.  
	 * 	Example: '/waveforms/rh/basic_components_demo/basic_components_demo.sad.xml'
	 * @return
	 * @throws ApplicationCreationException
	 */
	RedhawkApplication createApplication(String instanceName, String sadFileDestination) throws ApplicationCreationException;

	/**
	 * Returns the {@link RedhawkApplication} launched in this Domain. 
	 * @return
	 */
    List<RedhawkApplication> getApplications();
    
    /**
     * Utility method to retrieve a list {@link RedhawkApplication} by regex 
     * @param name
     * @return
     */
    List<RedhawkApplication> getApplicationsByName(String name);
    
    /**
     * Method to retrieve a {@link RedhawkApplication} by it's application name. 
     * @param name
     * 	Name of the application
     * @return
     * @throws MultipleResourceException
     * @throws ResourceNotFoundException
     */
    RedhawkApplication getApplicationByName(String name) throws MultipleResourceException, ResourceNotFoundException;
    
    /**
     * Method to retrieve a {@link RedhawkApplication} by it's application identifier. 
     * 
     * @param identifier
     * @return
     * @throws ResourceNotFoundException
     */
    RedhawkApplication getApplicationByIdentifier(String identifier) throws ResourceNotFoundException;
   
    /**
     * Method to create a POJO representing a RedhawkDeviceManager. 
     * @param deviceManagerName
     * 	Device manager name. 
     * @param fileSystemRoot
     * 	location of the root for the devicemanager. 
     * @param durable
     * 	whether or not the devicemanager is durable or not. 
     * @return
     * @throws Exception
     */
    RedhawkDeviceManager createDeviceManager(String deviceManagerName, String fileSystemRoot, boolean durable) throws Exception;
    
    /**
     * Method to create a POJO representing a RedhawkDeviceManager.
     * 
     * @param deviceManagerName
     * 	Name of the deviceManager
     * @param fileSystemRoot
     * 	Root of the deviceManager
     * @param durable
     * 	Whether or not the deviceManager created is durable.
     * @param callback
     * 	Callback for the deviceManager to use. 
     * @return
     * @throws Exception
     */
    RedhawkDeviceManager createDeviceManager(String deviceManagerName, String fileSystemRoot, boolean durable, DeviceManagerInturruptedCallback callback) throws Exception;
    
    /**
     * 
     * @return
     */
    Map<String, RedhawkDeviceManager> getDriverRegisteredDeviceManagers();
    
    /**
     * Unregisters all DeviceManagers 
     */
    void unRegisterAllDriverRegisteredDeviceManagers();
    
    /**
     * Returns  a connectionManager.
     * @return
     */
	RedhawkConnectionManager getConnectionManager();
	
	/**
	 * Returns a pojo representing an Allocation Manaager. 
	 * @return
	 */
    RedhawkAllocationManager getAllocationManager();
    
    /**
     * Returns a POJO representing a EventChannelManager
     * @return
     */
    RedhawkEventChannelManager getEventChannelManager();
    
    /**
     * Returns a POJO representing an RedhawkApplicationFactory
     * @param identifier
     * @return
     * @throws ResourceNotFoundException
     * @throws MultipleResourceException
     */
	RedhawkApplicationFactoryImpl getApplicationFactoryByIdentifier(String identifier) throws ResourceNotFoundException, MultipleResourceException;
    
	/**
	 * Returns a Domainmanagerconfiguration object. 
	 * @return
	 */
	Domainmanagerconfiguration getDomainManagerConfiguration();
	
	/**
	 * Returns an object representing the Domain Manager Assembly.
	 */
	Softpkg getDomainManagerAssembly() throws ResourceNotFoundException;
	
	/**
	 * Returns an object representing property configuration. 
	 * @return
	 * @throws ResourceNotFoundException
	 */
	Properties getPropertyConfiguration() throws ResourceNotFoundException;
}