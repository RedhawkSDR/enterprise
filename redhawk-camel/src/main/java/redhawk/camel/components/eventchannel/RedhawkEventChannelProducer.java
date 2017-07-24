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
package redhawk.camel.components.eventchannel;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultProducer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omg.CORBA.Any;

import redhawk.camel.components.RedhawkComponent;
import redhawk.camel.components.endpoints.RedhawkEventChannelEndpoint;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.eventchannel.RedhawkEventChannel;
import redhawk.driver.eventchannel.RedhawkEventChannelManager;
import redhawk.driver.exceptions.ResourceNotFoundException;

public class RedhawkEventChannelProducer extends DefaultProducer {

	private final RedhawkEventChannelEndpoint endpoint;

	private static Log logger = LogFactory.getLog(RedhawkEventChannelProducer.class);

	private String domainName;
	private RedhawkEventChannel eventChannel;

	public RedhawkEventChannelProducer(RedhawkEventChannelEndpoint endpoint) {
		super(endpoint);
		this.endpoint = endpoint;
		domainName = ((RedhawkComponent) endpoint.getComponent()).getDomainName();
	}

	public void process(Exchange exchange) throws Exception {
		String eventChannelName = endpoint.getEventChannelName();
		Object rhObject = exchange.getIn().getHeader("originalRedhawkObject");
		RedhawkDomainManager domainManager = ((RedhawkComponent) endpoint.getComponent()).getRedhawkDriver().getDomain(domainName);
		Message inMessage = exchange.getIn();
		RedhawkEventChannelManager ecm = domainManager.getEventChannelManager();
		
		if (eventChannel == null) {
			try {
				eventChannel = ecm.getEventChannel(eventChannelName);
			} catch (ResourceNotFoundException e) {
				// suppress exception the first time if the event channel doesn't exist, because we will try to create it
				logger.info(String.format("EventChannel does not exist: %s, creating it.", eventChannelName));
				// let these two calls throw exceptions if it can't create or get the event channel this time
				ecm.createEventChannel(eventChannelName);  
				eventChannel = ecm.getEventChannel(eventChannelName);
			}
		}
		

		if (rhObject != null && rhObject instanceof Any) {
			Map<String, Object> message = inMessage.getHeaders();
			message.put("body", rhObject);
			eventChannel.publish(endpoint.getMessageId(), message);
		} else {
			Map<String, Object> message = inMessage.getHeaders();
			message.put("body", inMessage.getBody());
			eventChannel.publish(endpoint.getMessageId(), message);
		}
	}

	@Override
	protected void doStart() throws Exception {
		super.doStart();
	}

	@Override
	protected void doStop() throws Exception {
		super.doStop();
		RedhawkEventChannel ec = eventChannel;
		eventChannel = null;
		if (ec != null) {
			ec.unsubscribe();
		}
	}

}
