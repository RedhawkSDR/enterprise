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

import org.omg.CosEventChannelAdmin.EventChannel;

import redhawk.driver.eventchannel.impl.RedhawkEventRegistrant;
import redhawk.driver.eventchannel.listeners.EventChannelListener;
import redhawk.driver.exceptions.EventChannelException;

public interface RedhawkEventChannel {
	/**
	 * @return Returns the name of the Event Channel.
	 */
	String getName();
	
	/**
	 * Subscribe to a specific EventChannel 
	 * @param listener
	 * 	Listener object handle logic for what to do when receiving an 
	 * 	event message. 
	 * @throws EventChannelException
	 */
	<T> void subscribe(EventChannelListener<T> listener) throws EventChannelException;
	
	/**
	 * Subscribe to a specific EventChannel with a specific Id
	 * @param listener
	 * 	Listener object handle logic for what to do when receiving an 
	 * 	event message. 	 
	 * @param id
	 * 	Id for the listener
	 * @throws EventChannelException
	 */
	<T> void subscribe(EventChannelListener<T> listener, String id) throws EventChannelException;

	
	/**
	 * Publish messages to an event channel 
	 * @param messageId
	 * 	Message Id for the message. 
	 * @param message
	 * 	content of the message.
	 * @throws EventChannelException
	 */
	void publish(String messageId, Map<String, java.lang.Object> message) throws EventChannelException;
	
	/**
	 * Unsubscribe event listener managed by this object 
	 * 
	 * @throws EventChannelException
	 */
	void unsubscribe() throws EventChannelException;
	
	/**
	 * Unsubscribe a particular registrant.
	 * 
	 * @param registrant
	 */
	void unsubscribe(RedhawkEventRegistrant registrant) throws EventChannelException;
	
	/**
	 * Retrieve the list of Registrants for the Event Channel
	 * 
	 * @param registrants
	 * 	Number of registrants to pull back
	 * @return
	 */
	List<RedhawkEventRegistrant> getRegistrants(Integer registrants);
	
	EventChannel getCorbaObj() throws EventChannelException;
}