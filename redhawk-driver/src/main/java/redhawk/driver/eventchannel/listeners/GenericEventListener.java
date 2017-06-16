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

import java.util.logging.Logger;

import org.omg.CORBA.Any;
import org.omg.CORBA.TypeCodePackage.BadKind;

import redhawk.driver.RedhawkUtils;

/**
 * Server as a catch all listener for all the different event listeners that are available
 *
 */
public abstract class GenericEventListener extends EventChannelListener<Object>{
	Logger logger = Logger.getLogger(GenericEventListener.class.getName());
	
	@Override
	protected Object processMessage(Any data) {
		try {
			String id = data.type().id();
			if(EventTypes.AbnormalComponentTerminationEventType.getType().equals(id)){
				return AbnormalComponentEventListener.getAbnormalComponentTerminationEventType(data);
			}else if(EventTypes.DomainManagementObjectAddedEventType.getType().equals(id)){
				return DomainObjectAddedEventListener.getDomainManagerObjectAddedEventType(data);
			}else if(EventTypes.DomainManagementObjectRemovedEventType.getType().equals(id)){
				return DomainObjectRemovedEventListener.getDomainManagementObjectRemovedEventType(data);
			}else if(EventTypes.LogEvent.getType().equals(id)){
				return LogEventListener.getLogEvent(data);
			}else if(EventTypes.PropertySetChangeEventType.getType().equals(id)){
				return PropertyChangeListener.getPropertyChange(data);
			}else if(EventTypes.StateChangeEventType.getType().equals(id)){
				return StateChangeEventListener.getStateChangeEventType(data);
			}else if(EventTypes.ResourceStateChangeEventType.getType().equals(id)){
				return ResourceStateChangeEventListener.getResourceStateChangeEventType(data);
			}else{
				logger.warning("Unknown type assuming this id: "+id+" Is a Map of properties");
				return RedhawkUtils.convertPropertiesAnyToMap(data);
			}
		} catch (BadKind e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.severe("Issue getting id "+e.getMessage());
			return null;
		}
	}
}
