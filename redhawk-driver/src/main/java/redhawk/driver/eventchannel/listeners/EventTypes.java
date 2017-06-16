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
