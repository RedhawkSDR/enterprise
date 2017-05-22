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
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosEventChannelAdmin.AlreadyConnected;
import org.omg.CosEventChannelAdmin.TypeError;
import org.omg.CosEventComm.Disconnected;
import org.omg.CosEventComm.PushConsumer;
import org.omg.CosEventComm.PushConsumerPOATie;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;

import CF.DataType;
import CF.EventChannelManager;
import CF.EventRegistrantIteratorHolder;
import CF.PropertiesHelper;
import CF.EventChannelManagerPackage.ChannelDoesNotExist;
import CF.EventChannelManagerPackage.EventChannelReg;
import CF.EventChannelManagerPackage.EventRegistrant;
import CF.EventChannelManagerPackage.EventRegistrantListHolder;
import CF.EventChannelManagerPackage.EventRegistration;
import CF.EventChannelManagerPackage.InvalidChannelName;
import CF.EventChannelManagerPackage.OperationFailed;
import CF.EventChannelManagerPackage.OperationNotAllowed;
import CF.EventChannelManagerPackage.RegistrationAlreadyExists;
import CF.EventChannelManagerPackage.RegistrationDoesNotExist;
import CF.EventChannelManagerPackage.ServiceUnavailable;
import redhawk.driver.eventchannel.RedhawkEventChannel;
import redhawk.driver.eventchannel.listeners.EventChannelListener;
import redhawk.driver.exceptions.EventChannelException;

public class RedhawkEventChannelImpl implements RedhawkEventChannel {
	private Logger log = Logger.getLogger(RedhawkEventChannelImpl.class.getName());
	
	private String eventChannelName;
	private ORB orb;
	private EventChannelManager eventChannelManager;
	private EventChannelReg registration;
	
	/**
	 * 
	 * @param eventChannelManager
	 * @param eventChannelName
	 * @param orb
	 */
	public RedhawkEventChannelImpl(EventChannelManager eventChannelManager, String eventChannelName, ORB orb) {
		this.eventChannelName = eventChannelName;
		this.orb = orb;
		this.eventChannelManager = eventChannelManager;
	}
	
	
	public String getName() {
		return eventChannelName;
	}
	
	public <T> void subscribe(EventChannelListener<T> listener) throws EventChannelException {
		this.subscribe(listener, null);
    }
	
	public <T> void subscribe(EventChannelListener<T> listener, String subscriptionId) throws EventChannelException{
    	try {
    		POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
    		rootPOA.the_POAManager().activate();
    		PushConsumerPOATie tie = new PushConsumerPOATie(listener);
    		PushConsumer pipeline = tie._this(orb);
    		register(subscriptionId);
    		registration.channel.for_consumers().obtain_push_supplier().connect_push_consumer(pipeline);
    	} catch(InvalidName | AdapterInactive | TypeError e){
    		throw new EventChannelException("A CORBA Exception has occured:", e);
    	} catch (AlreadyConnected e) {
    		throw new EventChannelException("Already Connected to the event Channel:", e); 		
		} 
	}
	
	private void register(String s) throws EventChannelException {
		if(registration == null) {
			String subscriptionId;
			
			if(s!=null){
				subscriptionId = s;
			}else{
		        subscriptionId = UUID.randomUUID().toString();				
			}
	        EventRegistration er = new EventRegistration(eventChannelName, subscriptionId);
	        try {
				registration = eventChannelManager.registerResource(er);
			} catch (InvalidChannelName | RegistrationAlreadyExists | OperationFailed | OperationNotAllowed | ServiceUnavailable e) {
				throw new EventChannelException("A CORBA Exception has occured:", e);
			}
		}
	}
	
	
	public void publish(String messageId, Map<String, java.lang.Object> message) throws EventChannelException {
		register(null);
		
		 List<CF.DataType> headers = new ArrayList<CF.DataType>();
         for (String key : message.keySet()) {
        	 Any prop;
        	 if(message.get(key) != null && message.get(key) instanceof Any){
        		 prop = (Any) message.get(key);  
        	 } else {
        		 String value = message.get(key) + "";
        		 prop = ORB.init().create_any();
        		 prop.insert_string(value);
        	 }
        	 
        	 CF.DataType dataType = new DataType();
        	 dataType.id = key;
        	 dataType.value = prop;
             headers.add(dataType);
         }
		
         Any messageHeaders = ORB.init().create_any();
         PropertiesHelper.insert(messageHeaders, headers.toArray(new CF.DataType[headers.size()]));
         
         DataType headerDataType = new DataType("message", messageHeaders);
         CF.DataType[] redhawkMessage = new DataType[1];
         redhawkMessage[0] = headerDataType;
         
         Any messageToSend = ORB.init().create_any();
         PropertiesHelper.insert(messageToSend, redhawkMessage);

         CF.DataType[] messageEncaps = new DataType[1];
         DataType messageWrapper = new DataType(messageId, messageToSend);
         messageEncaps[0] = messageWrapper;
         Any messageWrapperToSend = ORB.init().create_any();
         PropertiesHelper.insert(messageWrapperToSend, messageEncaps);		
		
		try {
			registration.channel.for_suppliers().obtain_push_consumer().push(messageWrapperToSend);
		} catch (Disconnected e) {
			e.printStackTrace();
		}
        
	}

		
	public void unsubscribe() throws EventChannelException {
		try {
			eventChannelManager.unregister(registration.reg);
		} catch (ChannelDoesNotExist e) {
			throw new EventChannelException("Event Channel Does not exist:", e);
		} catch (RegistrationDoesNotExist e) {
			throw new EventChannelException("Event Channel Registration Does Not Exist:", e);
		} catch (ServiceUnavailable e) {
			throw new EventChannelException("CORBA Exception:", e);
		}
	}
	
	public void unsubscribe(RedhawkEventRegistrant registrant) throws EventChannelException{
		try {
			eventChannelManager.unregister(registrant.getEventRegistration());
		} catch (ChannelDoesNotExist e) {
			throw new EventChannelException("Event Channel Does not exist:", e);
		} catch (RegistrationDoesNotExist e) {
			throw new EventChannelException("Event Channel Registration Does Not Exist:", e);
		} catch (ServiceUnavailable e) {
			throw new EventChannelException("CORBA Exception:", e);
		}
	}
	
	public List<RedhawkEventRegistrant> getRegistrants(Integer registrants){
		List<RedhawkEventRegistrant> eventRegistrants = new ArrayList<>();
		
		EventRegistrantListHolder holder = new EventRegistrantListHolder(); 
		EventRegistrantIteratorHolder iter = new EventRegistrantIteratorHolder();
		eventChannelManager.listRegistrants(this.eventChannelName, registrants, holder, iter);
		
		//TODO: Figure out how to do this with Stream API
		//eventRegistrants.addAll(Arrays.stream(holder.value).map(e -> ""+e.reg_id).collect(Collectors.toList());
		
		for(EventRegistrant registrant : holder.value){
			eventRegistrants.add(new RedhawkEventRegistrant(registrant.reg_id, registrant.channel_name, registrant));
		}
		
		return eventRegistrants;
	}
	
}