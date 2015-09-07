package redhawk.websocket.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.omg.CORBA.Any;
import org.ossie.properties.AnyUtils;

import CF.DataType;
import StandardEvent.DomainManagementObjectAddedEventType;
import StandardEvent.DomainManagementObjectRemovedEventType;
import StandardEvent.SourceCategoryType;
import redhawk.driver.eventchannel.listeners.PropertyChange;
import redhawk.driver.properties.RedhawkStructSequence;
import redhawk.websocket.model.DomainManagementAction;
import redhawk.websocket.model.DomainManagementModel;
import redhawk.websocket.model.PropertyChangeModel;
import redhawk.websocket.model.SourceCategory;

public class EventChannelConverter {
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
			model.setSourceIOR(obj.sourceIOR.toString());
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
