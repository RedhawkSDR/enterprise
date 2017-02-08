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

    void disconnect();
    ORB getOrb();

    String getHostName();
    int getPort();
    
	Map<String, RedhawkDomainManager> getDomains() throws CORBAException;
	RedhawkDomainManager getDomain(String domainName) throws ConnectionException, ResourceNotFoundException, CORBAException;
	
    RedhawkApplication getApplication(String applicationLocation) throws ResourceNotFoundException, CORBAException, MultipleResourceException;
    RedhawkComponent getComponent(String componentLocation) throws ResourceNotFoundException, CORBAException, MultipleResourceException;
    RedhawkPort getPort(String portLocation) throws ResourceNotFoundException, CORBAException, MultipleResourceException;
    RedhawkDevice getDevice(String deviceLocation) throws ResourceNotFoundException, CORBAException, MultipleResourceException;
    RedhawkDeviceManager getDeviceManager(String deviceManagerLocation) throws ResourceNotFoundException, CORBAException, MultipleResourceException;
    
}