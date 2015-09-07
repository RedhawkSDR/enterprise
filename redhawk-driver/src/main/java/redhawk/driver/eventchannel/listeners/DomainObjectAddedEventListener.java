package redhawk.driver.eventchannel.listeners;

import org.omg.CORBA.Any;

import CF.LogEvent;
import CF.LogEventHelper;
import StandardEvent.DomainManagementObjectAddedEventType;
import StandardEvent.DomainManagementObjectAddedEventTypeHelper;


public abstract class DomainObjectAddedEventListener extends EventChannelListener<DomainManagementObjectAddedEventType> {

	@Override
	protected DomainManagementObjectAddedEventType processMessage(Any data) {
		return DomainManagementObjectAddedEventTypeHelper.extract(data);
	}

}