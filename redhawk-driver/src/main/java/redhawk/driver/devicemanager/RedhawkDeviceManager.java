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
package redhawk.driver.devicemanager;

import java.util.List;

import CF.DeviceManager;
import redhawk.driver.base.QueryableResource;
import redhawk.driver.device.RedhawkDevice;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.exceptions.ServiceRegistrationException;
import redhawk.driver.xml.model.sca.prf.Properties;

public interface RedhawkDeviceManager extends QueryableResource {
	/**
	 * @return list of devices for a Redhawk Device Manager. 
	 */
    List<RedhawkDevice> getDevices();
    
    /**
     * Helper method to retrieve a Redhawk Device by name. 
     * @param name
     * 	Name of Redhawk Device. 
     * @return
     * @throws ResourceNotFoundException
     */
    RedhawkDevice getDeviceByName(String name) throws ResourceNotFoundException;
    
    /**
     * Helper method to retrieve a Redhawk Device by identifier. 
     * @param identifier
     * 	identifier for REDHAWK Device. 
     * @return
     * @throws ResourceNotFoundException
     */
    RedhawkDevice getDeviceByIdentifier(String identifier) throws ResourceNotFoundException;
    
    /**
     * @return Name of Redhawk Device Manager. 
     */
    String getName();
    
    /** 
     * @return Unique identifier for the object..
     * @deprecated Use getIdentifier
     */
    @Deprecated
    String getUniqueIdentifier();
    
    /**
     * 
     * @return Identifier for the DeviceManager
     */
    String getIdentifier();
    
    /**
     * @return Domain Manager for this Redhawk Device Manager.
     */
    RedhawkDomainManager getDomainManager();
    
    /**
     * @return {@link java.util.List} of Service for the Redhawk Device Manager. 
     */
    List<RedhawkService> getServices();
    
    /**
     * Retrieve a Service by name. 
     * @param name
     * 	Name of the Service to retrieve. 
     * @return
     * 	A Redhawk Service represented by the name provided. 
     * @throws MultipleResourceException
     * @throws ResourceNotFoundException
     */
    RedhawkService getServiceByName(String name) throws MultipleResourceException, ResourceNotFoundException;

    /**
     * @return POJO representing the File System of a Device Manager. 
     */
    RedhawkDeviceManagerFileSystem getFileSystem();
    
    /**
     * @return CORBA object for a DeviceManager. 
     */
    DeviceManager getCorbaObject();
    
    
    /**
     * Get the Properties Resource File for a DeviceManger
     * @return
     */
    Properties getPropertyConfiguration() throws ResourceNotFoundException;
    
    /**
     * @return POJO representing the File System of a Device Manager. 
     */
    //TODO: This and the getFileSystem method may be redundant. 
    RedhawkDeviceManagerFileSystem getDeviceManagerFileSystem();

    /**
     * Service Name to register. 
     * @param serviceName
     * 	Name of Service you'd like to register. 
     * @param objectToRegister
     * 	object representing the service. 
     * @param poaTieClass
     * 	POA for the Service. 
     * @param operationsInterface
     * 	Operations Interface for the service. 
     * @throws ServiceRegistrationException
     */
    void registerService(String serviceName, java.lang.Object objectToRegister, Class poaTieClass, Class operationsInterface) throws ServiceRegistrationException;
    
    /**
     * @param serviceName
     * 	ServiceName to unregister.
     * @throws MultipleResourceException
     * @throws ResourceNotFoundException
     */
    void unregisterService(String serviceName) throws MultipleResourceException, ResourceNotFoundException;
    
    /**
     * Shutdown Device Manager. 
     */
    void shutdown();
    
    /**
     * The deviceConfigurationProfile attribute contains the DeviceManager's profile, 
     * a profile element with a file reference to the DeviceManager's Device Configuration Descriptor (DCD) 
     * profile.
     * @return
     */
    String deviceConfigurationProfile();
    
    /**
     * returns the SPD implementation ID that the DeviceManager interface used to create a device
     * @return
     * @throws MultipleResourceException 
     */
    String getComponentImplemantation() throws MultipleResourceException;
}
