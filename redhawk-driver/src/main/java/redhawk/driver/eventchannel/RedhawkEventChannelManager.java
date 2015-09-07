package redhawk.driver.eventchannel;

import java.util.List;
import java.util.Map;

import redhawk.driver.exceptions.EventChannelCreationException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;

public interface RedhawkEventChannelManager {
	void createEventChannel(String channelName) throws EventChannelCreationException;
	void releaseEventChannel(String channelName) throws EventChannelCreationException;
	RedhawkEventChannel getEventChannel(String eventChannelName) throws MultipleResourceException, ResourceNotFoundException;	    
	List<RedhawkEventChannel> getEventChannels();
	Map<String, RedhawkEventChannel> eventChannels();
}
