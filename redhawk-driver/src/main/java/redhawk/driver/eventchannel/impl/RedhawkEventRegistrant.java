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

import CF.EventChannelManagerPackage.EventRegistrant;
import CF.EventChannelManagerPackage.EventRegistration;

public class RedhawkEventRegistrant {
	private String registrationId;
	
	private String channelName;
	
	private EventRegistrant registrant;
	
	public RedhawkEventRegistrant(String id, String channelName, EventRegistrant r){
		this.registrationId = id;
		this.channelName = channelName;
		this.registrant = r;
	}

	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public EventRegistrant getRegistrant() {
		return registrant;
	}

	public void setRegistrant(EventRegistrant registrant) {
		this.registrant = registrant;
	}
	
	public EventRegistration getEventRegistration(){
		return new EventRegistration(this.channelName, this.registrationId);
	}
	
}
