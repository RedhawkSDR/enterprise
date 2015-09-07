package redhawk.driver.eventchannel.listeners;

import org.omg.CORBA.Any;

import StandardEvent.AbnormalComponentTerminationEventType;
import StandardEvent.AbnormalComponentTerminationEventTypeHelper;


public abstract class AbnormalComponentEventListener extends EventChannelListener<AbnormalComponentTerminationEventType> {

	@Override
	protected AbnormalComponentTerminationEventType processMessage(Any data) {
		return AbnormalComponentTerminationEventTypeHelper.extract(data);
	}

}