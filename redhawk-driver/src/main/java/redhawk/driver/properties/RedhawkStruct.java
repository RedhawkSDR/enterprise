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
package redhawk.driver.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;
import org.ossie.properties.AnyUtils;

import CF.DataType;
import CF.PropertiesHelper;
import CF.PropertiesHolder;
import redhawk.driver.RedhawkUtils;

/**
 * 
 * Wrapper class for interacting with Structs. Java representation of a Struct 
 * is a Map<String, Object> 
 */
public class RedhawkStruct extends RedhawkProperty {
	private static Logger logger = Logger.getLogger(RedhawkStruct.class.getName());
	
	/**
	 * Original list structure containing CF.DataType objects for this Struct
	 */
    private List<DataType> structProperties = new ArrayList<DataType>();
    
    /**
     * Map to represent Struct object
     */
    private Map<String, Object> struct = new HashMap<>();
    
    /**
     * Root Id for struct
     */
    private String structId;
    
    /**
     * Available when struct is part of a StructSequence 
     */
    private RedhawkStructSequence structSequenceParent;
    
    public RedhawkStruct(ORB orb, String ior, DataType corbaRepOfProperty, DataType[] propertyValue) {
        this.orb = orb;
        this.parentIOR = ior;
        this.structId = corbaRepOfProperty.id;
        this.corbaProperty = corbaRepOfProperty;
        
        for(DataType dataType : propertyValue){
            structProperties.add(dataType);
            
	    	this.dataTypeToJavaObjectConverter(struct, dataType);
        }	
    }
    
    public RedhawkStruct(ORB orb, String ior, DataType corbaRepOfProperty, DataType[] propertyValue, RedhawkStructSequence structSequenceParent) {
        this.orb = orb;
        this.parentIOR = ior;
        this.structId = corbaRepOfProperty.id;
        this.corbaProperty = corbaRepOfProperty;
        this.structSequenceParent = structSequenceParent;
        
        for(DataType dataType : propertyValue){
            structProperties.add(dataType);
            
	    	this.dataTypeToJavaObjectConverter(struct, dataType);
        }	
    }
    
    public DataType[] getDataTypeArray() {
    	return structProperties.toArray(new DataType[structProperties.size()]);
    }
    
	@Override
	public <T> void setValue(T value) throws Exception {
		try {
		Map<String, Object> obj = (Map<String, Object>) value;
		this.setValues(obj);
		}catch(ClassCastException ex) {
			throw new Exception("Expecting a value castable to Map<String, Object>");
		}
	}
	
	@Override
	public <T> T getValue(Boolean requery) {
		if(requery) {
			this.requeryStruct();
		}
		
		return (T) struct;
	}
	
	public <T> T getValue(String key) {
		return getValue(key, true);
	}
	
	public <T> T getValue(String key, Boolean requery) {
		if(requery) {
			this.requeryStruct();
		}
		
		return (T) struct.get(key);
	}
	
	private void requeryStruct() {
		PropertiesHolder ph = RedhawkUtils.getPropertyFromCORBAObject(this.orb, this.parentIOR, this.structId);
		
		for(DataType type : ph.value) { 
	    	DataType[] typeArray = (DataType[]) AnyUtils.convertAny(type.value);
			
	    	for(DataType innerType : typeArray) {
    	    	//This is updating values in the struct directly 
    			this.dataTypeToJavaObjectConverter(struct, innerType);  	    		
	    	}
		}
	}
	
	
	@Deprecated
    public void setValues(Map<String, Object> valuesToChange) throws Exception {
        for(String key : valuesToChange.keySet()){
            for(DataType type : structProperties){
                if(type.id.toLowerCase().matches(key.toLowerCase())){
                    type.value = AnyUtils.toAny(valuesToChange.get(key), type.value.type().kind());
                }
            }   
        }

        //if part of struct sequence
        if(structSequenceParent != null){
        	logger.fine("FOUND AN UPDATE REQUEST ON A STRUCT SEQUENCE");
            structSequenceParent.updateStruct(this, valuesToChange);
        } else {
            Any structToInsert = orb.create_any();
            PropertiesHelper.insert(structToInsert, structProperties.toArray(new DataType[structProperties.size()]));  
            reconfigure(structId, structToInsert);
        }
    }
    
    public void setValue(String propertyId, Object value) throws Exception {
        DataType[] updatedStruct = null;
        for(DataType type : structProperties){
            if(type.id.toLowerCase().matches(propertyId.toLowerCase())){
                type.value = AnyUtils.toAny(value, type.value.type());
                
                //if part of struct sequence
                //TODO: I don't believe this is possible 
                if(structSequenceParent != null){
                	logger.fine("FOUND AN UPDATE REQUEST ON A STRUCT SEQUENCE");
                    updatedStruct = structSequenceParent.updateStruct(this, propertyId, value);
                } else {
                    Any structToInsert = orb.create_any();
                    PropertiesHelper.insert(structToInsert, structProperties.toArray(new DataType[structProperties.size()]));  
                    reconfigure(structId, structToInsert);
                }
            }
        }    
        
        if(updatedStruct != null){
            structProperties.clear();
            for(DataType dataType : updatedStruct){
                structProperties.add(dataType);
            }
        }
    }
    
	@Override
	public String toString() {
		StringBuilder build = new StringBuilder();
		build.append("RedhawkStruct: [structId="+structId);
		build.append("\n");
		build.append("Struct Properties: \n");
		for(CF.DataType dt : structProperties){
			build.append("\tid: "+dt.id+" value: "+AnyUtils.convertAny(dt.value)+" \n");
		}
		build.append("]");
		return build.toString();
	}
}
