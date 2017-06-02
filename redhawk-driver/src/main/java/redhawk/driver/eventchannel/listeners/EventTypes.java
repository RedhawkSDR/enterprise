package redhawk.driver.eventchannel.listeners;

import StandardEvent.AbnormalComponentTerminationEventTypeHelper;
import StandardEvent.DomainManagementObjectAddedEventTypeHelper;
import StandardEvent.DomainManagementObjectRemovedEventTypeHelper;
import CF.LogEventHelper;
import ExtendedEvent.PropertySetChangeEventTypeHelper;
import StandardEvent.StateChangeEventTypeHelper;
import ExtendedEvent.ResourceStateChangeEventTypeHelper;
/**
 * Enum for all EventType messages you may encounter on
 * an EventChannel
 */
public enum EventTypes {
	AbnormalComponentTerminationEventType(AbnormalComponentTerminationEventTypeHelper.id()),
	DomainManagementObjectAddedEventType(DomainManagementObjectAddedEventTypeHelper.id()),
	DomainManagementObjectRemovedEventType(DomainManagementObjectRemovedEventTypeHelper.id()),
	LogEvent(LogEventHelper.id()),
	PropertySetChangeEventType(PropertySetChangeEventTypeHelper.id()),
	ResourceStateChangeEventType(ResourceStateChangeEventTypeHelper.id()),
	StateChangeEventType(StateChangeEventTypeHelper.id());
	
	private String type;
	
	EventTypes(String type){
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
