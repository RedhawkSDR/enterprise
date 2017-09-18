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
import java.util.UUID;

import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.PortException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.port.RedhawkPort;
import redhawk.driver.xml.model.sca.prf.Properties;
import redhawk.driver.xml.model.sca.scd.Softwarecomponent;
import redhawk.driver.xml.model.sca.spd.Softpkg;

public interface PortBackedObject extends QueryableResource {
	/**
	 * @return {@link java.util.Map} with port name as the key and
	 *         {@link RedhawkPort} as the value.
	 * @throws ResourceNotFoundException
	 */
	Map<String, RedhawkPort> ports() throws ResourceNotFoundException;

	/**
	 * All the ports available for this object.
	 * 
	 * @return
	 * @throws ResourceNotFoundException
	 */
	List<RedhawkPort> getPorts() throws ResourceNotFoundException;

	/**
	 * @param portName
	 *            Name of the port to retrieve.
	 * @return A RedhawkPort object.
	 * @throws ResourceNotFoundException
	 * @throws MultipleResourceException
	 */
	RedhawkPort getPort(String portName) throws ResourceNotFoundException, MultipleResourceException;
	
	/**
	 * Connect to another Portbacked object.
	 * 
	 * @param resource
	 */
	default void connect(PortBackedObject resource) throws PortException {
		// No Id passed create one
		connect(resource, "rhdriver-" + UUID.randomUUID().toString());
	}

	/**
	 * Connect to another Portbacked object and specify the connectionId
	 * 
	 * @param port
	 * @throws PortException
	 */
	default void connect(PortBackedObject resource, String connectionId) throws PortException {
		try {
			Boolean foundPortMatch = false;
			String usesPortName = null, providesPortName = null;

			for (RedhawkPort port : this.getPorts()) {
				String portRepId = port.getRepId();
				String portType = port.getType();

				for (RedhawkPort connectToPort : resource.getPorts()) {
					String connectPortId = connectToPort.getRepId();
					String connectPortType = connectToPort.getType();
					//If port types aren't equal and interface(repId) make a connection
					if(!connectPortType.equals(portType) && portRepId.equals(connectPortId)) {						
						//If first match found fill in variables for connect
						if(!foundPortMatch) {
							if(portType.equals(RedhawkPort.PORT_TYPE_USES)) {
								usesPortName = port.getName();
								providesPortName = connectToPort.getName();
							}else {
								usesPortName = connectToPort.getName();
								providesPortName = port.getName();
							}
							
							foundPortMatch = true;
						}else {
							//Multiple matches user needs to specify which ports to connect
							throw new PortException("Multiple ports match with these components specify port names to match");
						}
					}
				}
			}
			
    		//Connect the matched ports
    		if(usesPortName!=null && providesPortName!=null) {
    			connect(resource, connectionId, usesPortName, providesPortName);
    		}else {
    			throw new PortException("No matching ports between these two components");
    		}
		} catch (ResourceNotFoundException ex) {
			throw new PortException("Unable to connect ports", ex);
		}
	}

	/**
	 * Connect to another PortbackedObject object by specifying connectionId,
	 * and usesPortName and providesPortName. 
	 * 
	 * @param port
	 * @param connectionId
	 * @param usesPortName
	 * @param providesPortName
	 * @throws PortException
	 */
	default void connect(PortBackedObject resource, String connectionId, String usesPortName, String providesPortName)
			throws PortException {
		RedhawkPort usesPort = null, providesPort = null;

		try {
			usesPort = this.getPort(usesPortName);
			providesPort = resource.getPort(providesPortName);
		} catch (ResourceNotFoundException e) {
			// Try to see if Component has the providesPortName instead
			try {
				providesPort = this.getPort(providesPortName);
				usesPort = resource.getPort(usesPortName);
			} catch (ResourceNotFoundException | MultipleResourceException e1) {
				throw new PortException("Unable to find connection between these two components using port "
						+ "names provided(uses: " + usesPortName + ", provides: " + providesPortName + ")", e1);
			}
		} catch (MultipleResourceException e) {
			throw new PortException("Multiple ports found with " + usesPortName, e);
		}

		// Connect the matched ports
		if (usesPort != null && providesPort != null) {
			usesPort.connect(providesPort, connectionId);
		} else {
			throw new PortException("No matching ports between these two components");
		}
	}
}
