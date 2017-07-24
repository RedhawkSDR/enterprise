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
package redhawk.driver.component.impl;

import java.util.logging.Logger;

import CF.ComponentType;
import CF.ComponentTypeHelper;
import CF.ComponentTypeHolder;
import CF.Resource;
import CF.ResourceHelper;
import CF.ResourcePackage.StartError;
import CF.ResourcePackage.StopError;
import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.base.impl.PortBackedObjectImpl;
import redhawk.driver.component.RedhawkComponent;
import redhawk.driver.exceptions.ComponentStartException;
import redhawk.driver.exceptions.ComponentStopException;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.logging.RedhawkLogLevel;

public class RedhawkComponentImpl extends PortBackedObjectImpl<ComponentType> implements RedhawkComponent {

    private static Logger logger = Logger.getLogger(RedhawkComponentImpl.class.getName());
    
    private RedhawkApplication application;
    private ComponentType component;
    
    /**
     * 
     * @param application {@link RedhawkApplication} that this component belongs to. 
     * @param component {@link CF.ComponentType} for this component. 
     */
    public RedhawkComponentImpl(RedhawkApplication application, ComponentType component){
    	super(application.getRedhawkDomainManager().getDriver().getOrb().object_to_string(component.componentObject) , application.getRedhawkDomainManager().getDriver().getOrb(), application.getRedhawkDomainManager().getFileManager());
    	this.application = application;
    	this.component = component;
    }
    
	@Override
	protected ComponentType locateCorbaObject() {
		return component;
	}
    
    @Override
	public ComponentType getCorbaObject() throws ConnectionException {
		return component;
	}

	public String getName(){
        return component.identifier;
    }
    
    public ComponentType getCorbaObj(){
        return component;
    }
	
    /** 
     * @return Application that holds this component. 
     */
    public RedhawkApplication getRedhawkApplication(){
        return application;
    }
    
	@Override
	public void start() throws ComponentStartException {
		try {
			ResourceHelper.narrow(component.componentObject).start();
		} catch (StartError e) {
			throw new ComponentStartException(e);
		}
	}

	@Override
	public boolean started() {
		return ResourceHelper.narrow(component.componentObject).started();
	}

	@Override
	public void stop() throws ComponentStopException {
		try {
			ResourceHelper.narrow(component.componentObject).stop();
		} catch (StopError e) {
			throw new ComponentStopException(e);
		}
	}
	
    @Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RedhawkComponentImpl [name=").append(getName()).append("]");
		return builder.toString();
	}

	@Override
	public Class<?> getHelperClass() {
		return ComponentTypeHelper.class;
	}

	@Override
	public RedhawkLogLevel getLogLevel() {
		Resource resource = ResourceHelper.narrow(component.componentObject);
		return RedhawkLogLevel.reverseLookup(resource.log_level());
	}

	@Override
	public void setLogLevel(RedhawkLogLevel level) {
		Resource resource = ResourceHelper.narrow(component.componentObject);
		resource.log_level(level.getValue());
	}

	@Override
	public Integer getProcessId() {
		return application.getComponentProcessIds().get(component.identifier);
	}


    
}
