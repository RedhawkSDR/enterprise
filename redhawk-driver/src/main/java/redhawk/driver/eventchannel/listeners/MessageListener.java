package redhawk.driver.eventchannel.listeners;

import java.util.Map;

import org.omg.CORBA.Any;

import redhawk.driver.RedhawkUtils;


public abstract class MessageListener extends EventChannelListener<Map<String,Object>> {

	@Override
	protected Map<String, Object> processMessage(Any data) {
		return RedhawkUtils.convertPropertiesAnyToMap(data);
	}
	
}