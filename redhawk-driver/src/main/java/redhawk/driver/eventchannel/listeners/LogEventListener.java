package redhawk.driver.eventchannel.listeners;

import org.omg.CORBA.Any;

import CF.LogEvent;
import CF.LogEventHelper;


public abstract class LogEventListener extends EventChannelListener<LogEvent> {

	@Override
	protected LogEvent processMessage(Any data) {
		return LogEventHelper.extract(data);
	}

}