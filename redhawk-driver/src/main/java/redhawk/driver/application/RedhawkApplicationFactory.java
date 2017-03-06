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

import java.util.Map;

import CF.ApplicationFactory;

/**
 * POJO representing a {@link CF.ApplicationFactory} object.  
 *
 */
public interface RedhawkApplicationFactory {
	/**
	 * @return Name of the ApplicationFactory. 
	 */
	String getName();
	
	/**
	 * @return Identifier for the ApplicationFactory. 
	 */
	String getIdentifier();
	
	/** 
	 * @return 
	 */
	String getSoftwareProfile();
	
	/**
	 * Get a {@link java.util.Map} representing the applications available for this factory. 
	 * @return {@link java.util.Map} with application name as the key and {@link RedhawkApplication} as the value. 
	 */
	Map<String, RedhawkApplication> getApplicationInstances();
//	RedhawkApplication createApplication(String instanceName, Map<String, Object> initialConfiguration, List<RedhawkDeviceAssignment> deviceAssignments ) throws ApplicationCreationException;
	
	/**
	 * @return The {@link CF.ApplicationFactory} for this object. 
	 */
	ApplicationFactory getCorbaObject();
	
	/**
	 * Release this application factory. 
	 */
	void release();
}
