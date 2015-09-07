package redhawk.driver.eventchannel.listeners;

import java.util.HashMap;
import java.util.Map;

import org.omg.CORBA.Any;
import org.ossie.properties.AnyUtils;

import ExtendedEvent.PropertySetChangeEventType;
import ExtendedEvent.PropertySetChangeEventTypeHelper;


public abstract class PropertyChangeListener extends EventChannelListener<PropertyChange> {

	@Override
	protected PropertyChange processMessage(Any data) {

		PropertySetChangeEventType propChangeEvent = PropertySetChangeEventTypeHelper.extract(data);
		
		PropertyChange propChange = new PropertyChange();
		Map<String, Object> properties = new HashMap<String, Object>();
		propChange.setSourceId(propChangeEvent.sourceId);
		propChange.setSourceName(propChangeEvent.sourceName);
		propChange.setCorbaAny(data);
		for (CF.DataType prop : propChangeEvent.properties) {
			Object obj = AnyUtils.convertAny(prop.value);
			properties.put(prop.id, obj);
		}		
		
		propChange.setProperties(properties);
		
		return propChange;
	}


}
