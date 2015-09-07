package redhawk.driver.eventchannel.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.omg.CORBA.ORB;

import CF.EventChannelInfoIteratorHolder;
import CF.EventChannelManager;
import CF.EventChannelManagerPackage.ChannelAlreadyExists;
import CF.EventChannelManagerPackage.ChannelDoesNotExist;
import CF.EventChannelManagerPackage.EventChannelInfoListHolder;
import CF.EventChannelManagerPackage.OperationFailed;
import CF.EventChannelManagerPackage.OperationNotAllowed;
import CF.EventChannelManagerPackage.RegistrationsExists;
import CF.EventChannelManagerPackage.ServiceUnavailable;
import redhawk.driver.eventchannel.RedhawkEventChannel;
import redhawk.driver.eventchannel.RedhawkEventChannelManager;
import redhawk.driver.exceptions.EventChannelCreationException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;

public class RedhawkEventChannelManagerImpl implements RedhawkEventChannelManager  {

	private static Logger logger = Logger.getLogger(RedhawkEventChannelManagerImpl.class.getName());
	private EventChannelManager eventChannelManager;
    private ORB orb;
	
    public RedhawkEventChannelManagerImpl(ORB orb, EventChannelManager eventChannelManager){
    	this.orb = orb;
        this.eventChannelManager = eventChannelManager;
    }
    
    public void createEventChannel(String channelName) throws EventChannelCreationException {
    	try {
			eventChannelManager.create(channelName);
		} catch (ChannelAlreadyExists | OperationNotAllowed | OperationFailed | ServiceUnavailable e) {
			throw new EventChannelCreationException(e);
		}
    }
    
    public RedhawkEventChannel getEventChannel(String eventChannelName) throws MultipleResourceException, ResourceNotFoundException {
    	List<RedhawkEventChannel> channels = getEventChannels().stream().filter(e -> { return e.getName().matches(eventChannelName);}).collect(Collectors.toList());
    	if(channels.size() > 1){
    		throw new MultipleResourceException("Multiple event channels exist for the name: " + eventChannelName);
    	} else if(channels.size() == 1){
    		return channels.get(0);
    	} else {
    		throw new ResourceNotFoundException("Could not find the event channel with a name: " + eventChannelName);
    	}
    }
    
    public List<RedhawkEventChannel> getEventChannels() {
    	List<RedhawkEventChannel> eventChannels = new ArrayList<>();
    	EventChannelInfoListHolder h = new EventChannelInfoListHolder();
    	EventChannelInfoIteratorHolder a = new EventChannelInfoIteratorHolder();
    	eventChannelManager.listChannels(1000000, h, a);
   		eventChannels.addAll(Arrays.stream(h.value).map(e -> new RedhawkEventChannelImpl(eventChannelManager, e.channel_name, orb)).collect(Collectors.toList()));
    	return eventChannels;
    }
    
    public Map<String, RedhawkEventChannel> eventChannels() {
    	return getEventChannels().stream().collect(Collectors.toMap(e -> e.getName(), Function.identity()));
    }

	@Override
	public void releaseEventChannel(String channelName) throws EventChannelCreationException {
		try {
			eventChannelManager.release(channelName);
		} catch (ChannelDoesNotExist | RegistrationsExists
				| OperationNotAllowed | OperationFailed | ServiceUnavailable e) {
			throw new EventChannelCreationException(e);
		}
	}
		
}