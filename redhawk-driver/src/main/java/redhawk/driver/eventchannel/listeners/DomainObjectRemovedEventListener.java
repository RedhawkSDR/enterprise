package redhawk.driver.eventchannel.listeners;

import org.omg.CORBA.Any;

import StandardEvent.DomainManagementObjectRemovedEventType;
import StandardEvent.DomainManagementObjectRemovedEventTypeHelper;


public abstract class DomainObjectRemovedEventListener extends EventChannelListener<DomainManagementObjectRemovedEventType> {

	@Override
	protected DomainManagementObjectRemovedEventType processMessage(Any data) {
		return DomainManagementObjectRemovedEventTypeHelper.extract(data);
	}

}