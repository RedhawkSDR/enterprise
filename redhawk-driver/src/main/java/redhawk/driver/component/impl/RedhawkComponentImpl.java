package redhawk.driver.component.impl;

import java.util.logging.Logger;

import CF.ComponentType;
import CF.ComponentTypeHelper;
import CF.ComponentTypeHolder;
import CF.ResourceHelper;
import CF.ResourcePackage.StartError;
import CF.ResourcePackage.StopError;
import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.base.impl.PortBackedObjectImpl;
import redhawk.driver.component.RedhawkComponent;
import redhawk.driver.exceptions.ComponentStartException;
import redhawk.driver.exceptions.ComponentStopException;
import redhawk.driver.exceptions.ConnectionException;

public class RedhawkComponentImpl extends PortBackedObjectImpl<ComponentType> implements RedhawkComponent {

    private static Logger logger = Logger.getLogger(RedhawkComponentImpl.class.getName());
    
    private RedhawkApplication application;
    private ComponentType component;
    
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


    
}
