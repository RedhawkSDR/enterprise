package redhawk.driver.eventchannel.listeners;

import org.omg.CORBA.Any;

import ExtendedEvent.ResourceStateChangeEventType;
import ExtendedEvent.ResourceStateChangeEventTypeHelper;

public abstract class ResourceStateChangeEventListener extends EventChannelListener<ResourceStateChangeEventType>{

	@Override
	protected ResourceStateChangeEventType processMessage(Any data) {
		// TODO Auto-generated method stub
		return getResourceStateChangeEventType(data);
	}
	
	protected static ResourceStateChangeEventType getResourceStateChangeEventType(Any data){
		return ResourceStateChangeEventTypeHelper.extract(data);
	}
}
