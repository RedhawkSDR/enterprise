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
