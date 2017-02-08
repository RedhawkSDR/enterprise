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

public interface RedhawkDeviceManager extends QueryableResource {


    List<RedhawkDevice> getDevices();
    RedhawkDevice getDeviceByName(String name) throws ResourceNotFoundException;
    RedhawkDevice getDeviceByIdentifier(String identifier) throws ResourceNotFoundException;
    String getName();
    String getUniqueIdentifier();
    RedhawkDomainManager getDomainManager();
    List<RedhawkService> getServices();
    RedhawkService getServiceByName(String name) throws MultipleResourceException, ResourceNotFoundException;

    RedhawkDeviceManagerFileSystem getFileSystem();
    DeviceManager getCorbaObject();
    RedhawkDeviceManagerFileSystem getDeviceManagerFileSystem();

    void registerService(String serviceName, java.lang.Object objectToRegister, Class poaTieClass, Class operationsInterface) throws ServiceRegistrationException;
    void unregisterService(String serviceName) throws MultipleResourceException, ResourceNotFoundException;
    
    void shutdown();
}
