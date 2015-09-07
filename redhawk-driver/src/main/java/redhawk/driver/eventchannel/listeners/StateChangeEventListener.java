package redhawk.driver.eventchannel.listeners;

import org.omg.CORBA.Any;

import StandardEvent.StateChangeEventType;
import StandardEvent.StateChangeEventTypeHelper;


public abstract class StateChangeEventListener extends EventChannelListener<StateChangeEventType> {

	@Override
	protected StateChangeEventType processMessage(Any data) {
		return StateChangeEventTypeHelper.extract(data);
	}

}