package redhawk.driver.eventchannel.listeners;

import org.omg.CORBA.Any;
import org.omg.CosEventComm.Disconnected;
import org.omg.CosEventComm.PushConsumerOperations;

public abstract class EventChannelListener<TParsedClass> implements PushConsumerOperations {

	public void disconnect_push_consumer() {
		//no need to implement according to RH team.
	}

	public void push(Any data) throws Disconnected {
		TParsedClass message = processMessage(data);
		onMessage(message);
	}
	
	protected abstract TParsedClass processMessage(Any data);

	public abstract void onMessage(TParsedClass message);
	
}
