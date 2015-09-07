package redhawk.driver.eventchannel;

import java.util.Map;

import redhawk.driver.eventchannel.listeners.EventChannelListener;
import redhawk.driver.exceptions.EventChannelException;

public interface RedhawkEventChannel {
	String getName();
	<T> void subscribe(EventChannelListener<T> listener) throws EventChannelException;
	void publish(String messageId, Map<String, java.lang.Object> message) throws EventChannelException;
	void unsubscribe() throws EventChannelException;
}