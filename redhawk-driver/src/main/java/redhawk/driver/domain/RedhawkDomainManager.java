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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import CF.DomainManager;
import CF.Resource;
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
import redhawk.driver.xml.ScaXmlProcessor;
import redhawk.driver.xml.model.sca.dmd.Domainmanagerconfiguration;
import redhawk.driver.xml.model.sca.prf.Properties;
import redhawk.driver.xml.model.sca.sad.Softwareassembly;
import redhawk.driver.xml.model.sca.spd.Softpkg;

public interface RedhawkDomainManager extends QueryableResource {

	
    /**
     * @return The name of the REDHAWK domain.  This value comes from the name attribute in the DomainManager.dcd.xml file
     */
    String getName();
    
    /**
     * @return The unique identifier of the REDHAWK domain.  The value comes from the id attribute in the DomainManager.dcd.xml file.
     * @throws ConnectionException
     */
    String getIdentifier();
    Map<String, RedhawkDeviceManager> deviceManagers();
    Map<String, RedhawkApplication> applications();
	Map<String, RedhawkDevice> devices();


    
    /**
     * @return The remote DomainManager Object Reference from the ORB.
     */
    DomainManager getCorbaObj();
   
    RedhawkFileManager getFileManager() throws ConnectionException;
    
    Redhawk getDriver();

    List<RedhawkDeviceManager> getDeviceManagers();
    List<RedhawkDeviceManager> getDeviceManagersByName(String name);

    /**
 	 * @param identifier a {@link java.util.regex.Pattern#sum regular expression} specifying the DCE UUID of the Device Manager to get
 	 * @return a device manager with the specified identifier, or <code>null</code> if one could not be found.  If multiple device managers match, then an arbitrary one is returned.
     * @throws ResourceNotFoundException 
 	 */
    RedhawkDeviceManager getDeviceManagerByIdentifier(String identifier) throws ResourceNotFoundException;
    
    /**
     * @param name a {@link java.util.regex.Pattern#sum regular expression} specifying the name of the Device Manager to get
     * @return a device manager with the specified name, or <code>null</code> if one could not be found.
     * @throws MultipleResourceException if multiple device managers match
     * @throws ResourceNotFoundException 
     */
    RedhawkDeviceManager getDeviceManagerByName(String name) throws MultipleResourceException, ResourceNotFoundException;


    List<RedhawkDevice> getDevices();
 	List<RedhawkDevice> getDevicesByName(String deviceName);
 	RedhawkDevice getDeviceByName(String deviceName) throws MultipleResourceException;
 	RedhawkDevice getDeviceByIdentifier(String identifier);
 	
	List<RedhawkService> getServices();
	List<RedhawkService> getServicesByName(String serviceName);
	RedhawkService getServiceByName(String serviceName) throws MultipleResourceException;
	
	RedhawkApplication createApplication(String instanceName, File sadFile) throws ApplicationCreationException;
	RedhawkApplication createApplication(String instanceName, Softwareassembly softwareAssembly) throws ApplicationCreationException;
	RedhawkApplication createApplication(String instanceName, String sadFileDestination) throws ApplicationCreationException;

    List<RedhawkApplication> getApplications();
    List<RedhawkApplication> getApplicationsByName(String name);
    RedhawkApplication getApplicationByName(String name) throws MultipleResourceException, ResourceNotFoundException;
    RedhawkApplication getApplicationByIdentifier(String identifier) throws ResourceNotFoundException;
   

    RedhawkDeviceManager createDeviceManager(String deviceManagerName, String fileSystemRoot, boolean durable) throws Exception;
    RedhawkDeviceManager createDeviceManager(String deviceManagerName, String fileSystemRoot, boolean durable, DeviceManagerInturruptedCallback callback) throws Exception;
    
    Map<String, RedhawkDeviceManager> getDriverRegisteredDeviceManagers();
    void unRegisterAllDriverRegisteredDeviceManagers();
    
	RedhawkConnectionManager getConnectionManager();
    RedhawkAllocationManagerImpl getAllocationManager();
    RedhawkEventChannelManager getEventChannelManager();

	RedhawkApplicationFactoryImpl getApplicationFactoryByIdentifier(String identifier) throws ResourceNotFoundException, MultipleResourceException;
    
	
	
	
	Domainmanagerconfiguration getDomainManagerConfiguration();
	Softpkg getDomainManagerAssembly() throws ResourceNotFoundException;
	Properties getPropertyConfiguration() throws ResourceNotFoundException;
	
	
	
}