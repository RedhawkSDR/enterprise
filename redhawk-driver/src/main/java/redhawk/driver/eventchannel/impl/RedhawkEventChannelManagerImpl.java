/*
 * This file is protected by Copyright. Please refer to the COPYRIGHT file
 * distributed with this source distribution.
 *
 * This file is part of REDHAWK __REDHAWK_PROJECT__.
 *
 * REDHAWK __REDHAWK_PROJECT__ is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * REDHAWK __REDHAWK_PROJECT__ is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
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