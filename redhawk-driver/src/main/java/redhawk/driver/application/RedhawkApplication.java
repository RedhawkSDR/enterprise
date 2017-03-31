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

/**
 * POJO representing a Redhawk Application
 */
public interface RedhawkApplication extends QueryableResource {
    /**
     * @return A {@link java.util.List} with {@link RedhawkComponent} objects. 
     */
	List<RedhawkComponent> getComponents();
	
	/**
	 * Get a list of components by a Regex
	 * @param name
	 * 	Regex for a component name. 
	 * @return
	 * 	A {@link java.util.List} with {@link RedhawkComponent} objects.
	 */
    List<RedhawkComponent> getComponentsByName(String name);
    
    /**
     * Get a specific {@link RedhawkComponent} by name. 
     * @param name
     * 	Name of the RedhawkComponent. 
     * @return
     * @throws MultipleResourceException
     * @throws ResourceNotFoundException
     */
    RedhawkComponent getComponentByName(String name) throws MultipleResourceException, ResourceNotFoundException;
    
    /** 
     * @return Name of the Redhawk Application
     */
    String getName();
    
    /**
     * 
     * @return Identifier for the Redhawk Application
     */
    String getIdentifier();
    
    /**
     * @return The Redhawk Domain Manager that this application is hosted by. 
     */
    RedhawkDomainManager getRedhawkDomainManager();
    
    /** 
     * @return The CORBA object representing this RedhawkApplication
     */
    Application getCorbaObj();
    
    /**
     * @return {@link Softwareassembly} for this object. 
     * @throws IOException
     */
	Softwareassembly getAssembly() throws IOException;
	
	/**
	 * Use this method to start your Redhawk Application. 
	 * 
	 * @throws ApplicationStartException
	 */
    void start() throws ApplicationStartException;
    
    /**
     * Use this method to stop your Redhawk Application. 
     * @throws ApplicationStopException
     */
    void stop() throws ApplicationStopException;
    
    /**
     * Use this method to release your Redhawk Application
     * @throws ApplicationReleaseException
     */
    void release() throws ApplicationReleaseException;
    
    /**
     * Use this method to get a specific ExternalPort for your application. 
     * @param name
     * 	Name of the External Port. 
     * @return
     * @throws ResourceNotFoundException
     * @throws IOException
     */
    RedhawkPort getPort(String name) throws ResourceNotFoundException, IOException;
    
    /**
     * Use this method to get ExternalPorts for your application.  
     * @return
     * @throws IOException
     */
    List<RedhawkPort> getPorts() throws IOException;
    
    /**
     * Utility method to figure out if an application is started or not. 
     * @return
     */
    boolean isStarted();
}