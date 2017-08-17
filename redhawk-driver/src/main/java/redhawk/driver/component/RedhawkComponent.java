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
package redhawk.driver.component;

import CF.ComponentType;
import redhawk.driver.base.PortBackedObject;
import redhawk.driver.device.RedhawkDevice;
import redhawk.driver.exceptions.ComponentStartException;
import redhawk.driver.exceptions.ComponentStopException;

public interface RedhawkComponent extends PortBackedObject {
    /**
     * @return Name of the component. 
     */
	String getName();
	
	/**
	 * @return CORBA object representing a REDHAWK Component. 
	 */
    ComponentType getCorbaObj();
    
    /**
     * Utility method for starting a component. 
     * @throws ComponentStartException
     */
    void start() throws ComponentStartException;
    
    /**
     * Utility method for checking if a component is started. 
     * @return
     */
    boolean started();
    
    /**
     * Utility method for stopping a component. 
     * @throws ComponentStopException
     */
    void stop() throws ComponentStopException;
    
    /**
     * Process Id for this component. 
     * @return
     */
    Integer getProcessId();
    
    /**
     * Returns the implementation type for the component. 
     * @return
     */
    String getComponentImplementation();
    
    /**
     * Helper method for getting the device that a component is running on. 
     * 
     * @return
     */
    RedhawkDevice getComponentDevice();
}
