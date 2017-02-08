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
package redhawk.driver.application;

import java.io.IOException;
import java.util.List;

import CF.Application;
import redhawk.driver.base.QueryableResource;
import redhawk.driver.component.RedhawkComponent;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.exceptions.ApplicationReleaseException;
import redhawk.driver.exceptions.ApplicationStartException;
import redhawk.driver.exceptions.ApplicationStopException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.port.RedhawkPort;
import redhawk.driver.xml.model.sca.sad.Softwareassembly;

public interface RedhawkApplication extends QueryableResource {
    List<RedhawkComponent> getComponents();
    List<RedhawkComponent> getComponentsByName(String name);
    RedhawkComponent getComponentByName(String name) throws MultipleResourceException, ResourceNotFoundException;
    String getName();
    String getIdentifier();
    RedhawkDomainManager getRedhawkDomainManager();
    Application getCorbaObj();
	Softwareassembly getAssembly() throws IOException;
    void start() throws ApplicationStartException;
    void stop() throws ApplicationStopException;
    void release() throws ApplicationReleaseException;
    RedhawkPort getExternalPort(String name) throws ResourceNotFoundException, IOException;
    List<RedhawkPort> getExternalPorts() throws IOException;
    boolean isStarted();
}