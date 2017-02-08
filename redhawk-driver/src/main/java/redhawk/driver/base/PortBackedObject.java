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
package redhawk.driver.base;

import java.util.List;
import java.util.Map;

import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.port.RedhawkPort;
import redhawk.driver.xml.model.sca.prf.Properties;
import redhawk.driver.xml.model.sca.scd.Softwarecomponent;
import redhawk.driver.xml.model.sca.spd.Softpkg;

public interface PortBackedObject extends QueryableResource {
	Map<String, RedhawkPort> ports() throws ResourceNotFoundException;
    List<RedhawkPort> getPorts() throws ResourceNotFoundException;
    RedhawkPort getPort(String portName) throws ResourceNotFoundException, MultipleResourceException;
	Softwarecomponent getSoftwareComponent() throws ResourceNotFoundException;
	Softpkg getComponentAssembly() throws ResourceNotFoundException;
	Properties getPropertyConfiguration() throws ResourceNotFoundException;
}
