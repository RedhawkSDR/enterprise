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
package redhawk.driver;

import java.util.Map;

import org.omg.CORBA.ORB;

import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.component.RedhawkComponent;
import redhawk.driver.device.RedhawkDevice;
import redhawk.driver.devicemanager.RedhawkDeviceManager;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.port.RedhawkPort;

public interface Redhawk {
	/**
	 * Disconnects from Object Resource Broker(ORB)
	 */
    void disconnect();
    
    /**
     * Returns a POJO representing the ORB
     * @return
     */
    ORB getOrb();

    /**
     * Returns the host name for the Name Server 
     * @return
     */
    String getHostName();
    
    /**
     * Returns the port for the Name Server 
     * @return
     */
    int getPort();
    
    /**
     * Returns a Map containing the Redhawk Domain name as a key 
     * and a {@link redhawk.driver.domain.RedhawkDomainManager} for 
     * it's values. 
     * 
     * @return Map of all the Redhawk Domains found at the configured Name Server
     * @throws CORBAException
     * 	
     */
	Map<String, RedhawkDomainManager> getDomains() throws CORBAException;
	
	/**
	 * Returns the requested {@link redhawk.driver.domain.RedhawkDomainManager} 
	 * @param domainName Name of the Domain you'd like a RedhawkDomainManager for. 
	 * @return
	 * 	the RedhawkDomainManager you requested. 
	 * 
	 * @throws ConnectionException
	 * @throws ResourceNotFoundException
	 * @throws CORBAException
	 */
	RedhawkDomainManager getDomain(String domainName) throws ConnectionException, ResourceNotFoundException, CORBAException;
	
	/**
	 * Returns the application {@link redhawk.driver.application.RedhawkApplication} specified in the application location 
	 * @param applicationLocation
	 * 	Application location is a string with 'redhawk domain'/'application name' . An example is: 
	 * 	REDHAWK_DEV/helloRedhawk . Where 'REDHAWK_DEV' is the REDHAWK domain name and 'helloRedhawk' is the Application
	 * 	Name. 
	 * @return
	 * 	Requested application. 
	 * 
	 * @throws ResourceNotFoundException
	 * @throws CORBAException
	 * @throws MultipleResourceException
	 */
    RedhawkApplication getApplication(String applicationLocation) throws ResourceNotFoundException, CORBAException, MultipleResourceException;
    
    /**
     * Returns a component {@link redhawk.driver.component.RedhawkComponent} based on the componentLocation specified.
     * 
     * @param componentLocation
     * 	String representing the Location of the Component on your domain. String should come in this form 'redhawk domain'/'application name'/'component name'
     * 	An example string would look like this 'REDHAWK_DEV/helloRedhawk/SigGen_1'. 
     * @return
     * 	The component at the specified location. 
     * @throws ResourceNotFoundException
     * @throws CORBAException
     * @throws MultipleResourceException
     */
    RedhawkComponent getComponent(String componentLocation) throws ResourceNotFoundException, CORBAException, MultipleResourceException;
    
    /**
     * Returns a Port {@link redhawk.driver.port.RedhawkPort} based on the port location specified. 
     * 
     * @param portLocation
     * 	String representation of the location of the Port you'd like to retrieve. String should come in this form
     * 	'REDHAWK_DEV/helloRedhawk/SigGen_1/dataFloat_out'. 
     * @return
     * 	The port at the specified location. 
     * @throws ResourceNotFoundException
     * @throws CORBAException
     * @throws MultipleResourceException
     */
    RedhawkPort getPort(String portLocation) throws ResourceNotFoundException, CORBAException, MultipleResourceException;
    
    /**
     * Returns a Device {@link redhawk.driver.device.RedhawkDevice} based on the deviceLocation specified. 
     * @param deviceLocation
     * @return
     * 	The device at the specified location. 
     * @throws ResourceNotFoundException
     * @throws CORBAException
     * @throws MultipleResourceException
     */
    RedhawkDevice getDevice(String deviceLocation) throws ResourceNotFoundException, CORBAException, MultipleResourceException;
    
    /**
     * Returns a DeviceManager {@link redhawk.driver.devicemanager.RedhawkDeviceManager} at the deviceManagerLocation 
     * given. 
     * @param deviceManagerLocation
     * 	The location of the DeviceManager. 
     * @return
     * @throws ResourceNotFoundException
     * @throws CORBAException
     * @throws MultipleResourceException
     */
    RedhawkDeviceManager getDeviceManager(String deviceManagerLocation) throws ResourceNotFoundException, CORBAException, MultipleResourceException;
    
}