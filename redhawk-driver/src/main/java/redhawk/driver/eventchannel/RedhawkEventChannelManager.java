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
package redhawk.driver.eventchannel;

import java.util.List;
import java.util.Map;

import redhawk.driver.exceptions.EventChannelException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;

public interface RedhawkEventChannelManager {
	/**
	 * Create an Event Channel 
	 * @param channelName
	 * 	Name for the event channel. 
	 * @throws EventChannelCreationException
	 */
	void createEventChannel(String channelName) throws EventChannelException;
	
	/**
	 * Release an Event Channel whether registrants exist or not. 
	 *
	 * @param channelName
	 * 	Channel name to release. 
	 * @throws EventChannelCreationException
	 */
	void releaseEventChannel(String channelName) throws EventChannelException;

	/**
	 * Release an Event channel.
	 * 
	 * @param channelName
	 * 	Channel name to release
	 * @param deleteRegistrants
	 * 	Whether or not to delete registrants automatically. If false an exception will occur. 
	 */
	void releaseEventChanne(String channelName, Boolean deleteRegistrants) throws EventChannelException;
	
	/**
	 * Retrieve a managed Event Channel. 
	 * @param eventChannelName
	 * 	Name of Event Channel to retrieve. 
	 * @return
	 * @throws MultipleResourceException
	 * @throws ResourceNotFoundException
	 */
	RedhawkEventChannel getEventChannel(String eventChannelName) throws MultipleResourceException, ResourceNotFoundException;	    
	
	/**
	 * @return list of event channels. 
	 */
	List<RedhawkEventChannel> getEventChannels();
	
	/**
	 * @return Map of Event Channels. With 'Event Channel Name' -> 'Redhawk Event Channel'
	 */
	Map<String, RedhawkEventChannel> eventChannels();
}
