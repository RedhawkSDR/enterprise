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
