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
package redhawk.websocket.eventchannel.listeners;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.omg.CORBA.Any;
import org.omg.CORBA.TypeCodePackage.BadKind;

import StandardEvent.DomainManagementObjectAddedEventTypeHelper;
import StandardEvent.DomainManagementObjectRemovedEventTypeHelper;
import redhawk.driver.eventchannel.listeners.EventChannelListener;
import redhawk.websocket.utils.EventChannelConverter;
/**
 * Generic EventChannelListener to handle events that are not handled by the other Listeners(PropertyChange, Log, Message). Once a 
 * new message type is well defined it can break out from here into it's own class. 
 */
public abstract class ObjectEventChannelListener extends EventChannelListener<Object>{
    private static Logger logger = Logger.getLogger(ObjectEventChannelListener.class.getName());
    
    /**
     * Processes Any data incoming from port and turns it into known POJO if possible otherwise just returns the Any. 
     */
	@Override
	protected Object processMessage(Any data) {
		String type = "";
		Object object; 
		try {
			type = data.type().id();
		} catch (BadKind e) {
			logger.log(Level.SEVERE, "Unable to get type from Any", e.getCause());
		}
		
		if(type.equals(DomainManagementObjectAddedEventTypeHelper.id())){
			object = EventChannelConverter.convertDomainManagementObjectToModel(DomainManagementObjectAddedEventTypeHelper.extract(data));
		}else if(type.equals(DomainManagementObjectRemovedEventTypeHelper.id())){
			object = EventChannelConverter.convertDomainManagementObjectToModel(DomainManagementObjectRemovedEventTypeHelper.extract(data));
		}else{
			logger.log(Level.SEVERE, "Received unknown event type in Listener. Need to add code to handle. "+type);
			object = data;
		}
		
		return object;
	}
}
