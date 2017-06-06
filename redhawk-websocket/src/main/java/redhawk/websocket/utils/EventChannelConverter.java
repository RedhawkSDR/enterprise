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
package redhawk.websocket.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.omg.CORBA.Any;
import org.ossie.properties.AnyUtils;

import CF.DataType;
import ExtendedEvent.ResourceStateChangeEventType;
import StandardEvent.DomainManagementObjectAddedEventType;
import StandardEvent.DomainManagementObjectRemovedEventType;
import StandardEvent.SourceCategoryType;
import redhawk.driver.eventchannel.listeners.PropertyChange;
import redhawk.driver.properties.RedhawkStructSequence;
import redhawk.websocket.model.DomainManagementAction;
import redhawk.websocket.model.DomainManagementModel;
import redhawk.websocket.model.PropertyChangeModel;
import redhawk.websocket.model.ResourceState;
import redhawk.websocket.model.ResourceStateChangeModel;
import redhawk.websocket.model.SourceCategory;

public class EventChannelConverter {
	public static Object convertData(Object obj){
		if(obj instanceof DomainManagementObjectAddedEventType){
			return convertDomainManagementObjectToModel(obj);
		}else if(obj instanceof DomainManagementObjectRemovedEventType){
			return convertDomainManagementObjectToModel(obj);
		}else if(obj instanceof ResourceStateChangeEventType){
			return convertResourceStateChangeEventType((ResourceStateChangeEventType)obj);
		}else{
			return null;
		}
	}
	
	/**
	 * Turns a PropertyChange CORBA object into a PropertyChangeModel which can be serialized into JSON. 
	 * @param message
	 * @return
	 * 		Returns a POJO representing a PropertyChange on REDHAWK 
	 */
	public static PropertyChangeModel convertToPropertyChangeModel(PropertyChange message){
		PropertyChangeModel model = new PropertyChangeModel(); 
        model.setSourceId(message.getSourceId());
        model.setSourceName(message.getSourceName());
        Map<String, Object> properties = new HashMap<String, Object>();
        for(Entry<String, Object> entry : message.getProperties().entrySet()){
        	if(entry.getValue() instanceof DataType[]){
        		DataType[] dt = (DataType[]) entry.getValue();
        		Map<String, Object> test = new HashMap<>(); 
        		for(DataType t : dt){
        	        Object propertyValue = AnyUtils.convertAny(t.value);
        	        test.put(t.id, propertyValue);	
        		}
        		properties.put(entry.getKey(), test);
        	}else if(entry.getValue() instanceof Any[]){
        		Any[] anyObject = (Any[]) entry.getValue();
                RedhawkStructSequence sequence = new RedhawkStructSequence(null, "bs", entry.getKey(), anyObject);
                properties.put(entry.getKey(), sequence.toListOfMaps());
        	}else{
        		properties.put(entry.getKey(), entry.getValue());
        	}
        }
        model.setProperties(properties);
        
        return model;
	}
	
	/**
	 * Converts DomainManagementObjectAddedEventType and DomainManagementObjectRemovedEventType into DomainManagementModel. 
	 * 
	 * @param managementObject
	 * @return
	 * 		Returns a POJO representing a DomainManagementObjectRemovedEventType and DomainManagementObjectAddedEventType.
	 */
	public static DomainManagementModel convertDomainManagementObjectToModel(Object managementObject){
		DomainManagementModel model = new DomainManagementModel();
		
		if(managementObject instanceof DomainManagementObjectAddedEventType){
			DomainManagementObjectAddedEventType obj = (DomainManagementObjectAddedEventType) managementObject;
			model.setProducerId(obj.producerId);
			model.setSourceCategory(getSourceCategory(obj.sourceCategory.value()));
			model.setAction(DomainManagementAction.ADD);
			model.setSourceName(obj.sourceName);
			//At the frontend this is likely unnecessary
			//model.setSourceIOR(obj.sourceIOR.toString());
		}else if(managementObject instanceof DomainManagementObjectRemovedEventType){
			DomainManagementObjectRemovedEventType obj = (DomainManagementObjectRemovedEventType) managementObject;
			model.setProducerId(obj.producerId);
			model.setSourceCategory(getSourceCategory(obj.sourceCategory.value()));
			model.setAction(DomainManagementAction.REMOVE);
			model.setSourceName(obj.sourceName);			
		}else{
			throw new ClassCastException("This method is only for DomainManagementObjects.");
		}
		return model;
	}
	
	/**
	 * Converts ResourceStateChangeEventType to model 
	 */
	public static ResourceStateChangeModel convertResourceStateChangeEventType(ResourceStateChangeEventType object){
		ResourceStateChangeModel model = new ResourceStateChangeModel();
		
		model.setSourceId(object.sourceId);
		model.setSourceName(object.sourceName);
		model.setStateChangedFrom(ResourceState.getValue(object.stateChangeFrom.value()));
		model.setStateChangedTo(ResourceState.getValue(object.stateChangeTo.value()));
	
		return model;
	}
	
	
	/**
	 * Based on the int value of SourceCategory will return String representation of category. 
	 * @param value
	 * @return
	 * 		Returns Enum(Application, Application_Factory, Device, Device_Manager, Service, Unknown)
	 */
	public static SourceCategory getSourceCategory(int value){
		SourceCategory category;
		
		switch(value){
		case SourceCategoryType._APPLICATION:
			category = SourceCategory.APPLICATION;
			break;
		case SourceCategoryType._APPLICATION_FACTORY:
			category = SourceCategory.APPLICATION_FACTORY;
			break;
		case SourceCategoryType._DEVICE:
			category = SourceCategory.DEVICE;
			break;
		case SourceCategoryType._DEVICE_MANAGER:
			category = SourceCategory.DEVICE_MANAGER;
			break; 
		case SourceCategoryType._SERVICE:
			category = SourceCategory.SERVICE;
			break; 
		default:
			category = SourceCategory.UNKNOWN;
			break;
		}
		
		return category;
	}
}
