package redhawk.driver.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;
import org.ossie.properties.AnyUtils;

import CF.DataType;
import CF.PropertiesHelper;

public class RedhawkStruct extends RedhawkProperty implements Map<String, Object> {
	
	private static Logger logger = Logger.getLogger(RedhawkStruct.class.getName());
    private List<DataType> structProperties = new ArrayList<DataType>();
    private String structId;
    private RedhawkStructSequence structSequenceParent;
    
    public RedhawkStruct(ORB orb, String parentObject, String structId, DataType[] struct, RedhawkStructSequence structSequenceParent){
        this.orb = orb;
        this.parentObject = parentObject;
        this.structId = structId;
        this.structSequenceParent = structSequenceParent;
        
        for(DataType dataType : struct){
            structProperties.add(dataType);
        }
    }

    public DataType[] getDataTypeArray() {
    	return structProperties.toArray(new DataType[structProperties.size()]);
    }
    
    public <T> T getValue(String property){
        for(DataType type : structProperties){
            if(type.id.toLowerCase().matches(property.toLowerCase())){
                return (T) AnyUtils.convertAny(type.value);
            }
        }
        
        return null;
    }

    public <T> T getValue(String property, Class<T> clazz){
        for(DataType type : structProperties){
            if(type.id.toLowerCase().matches(property.toLowerCase())){
                return (T) AnyUtils.convertAny(type.value);
            }
        }
        
        return null;
    }
    
    
    public void setValues(Map<String, Object> valuesToChange) throws Exception {
        Map<String, Object> values = new HashMap<String, Object>();
        
        for(String key : valuesToChange.keySet()){
            for(DataType type : structProperties){
                if(type.id.toLowerCase().matches(key.toLowerCase())){
                    type.value = createAny(valuesToChange.get(key));
                    values.put(type.id, valuesToChange.get(key));
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
                type.value = createAny(value);
                
                //if part of struct sequence
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
    

    public Map<String,Object> toMap(){
        Map<String, Object> returnMap = new HashMap<String, Object>();
        for(DataType type : structProperties){
            returnMap.put(type.id, AnyUtils.convertAny(type.value));
        }
        
        return returnMap;
    }

	@Override
	public void clear() {
		throw new UnsupportedOperationException("Currently cannot clear all values in the struct");
	}

	@Override
	public boolean containsKey(Object key) {
		return getValue(key+"") != null;
	}

	@Override
	public boolean containsValue(Object value) {
        for(DataType type : structProperties){
        	if(AnyUtils.convertAny(type.value).equals(value)){
        		return true;
        	}
        }
		
		return false;
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		Set<java.util.Map.Entry<String, Object>> entrySet = new HashSet<>();
		
        for(final DataType type : structProperties){
        	Entry<String, Object> entry = new Entry<String, Object>() {
        		private DataType t = type;
        		
				@Override
				public String getKey() {
					return t.id;
				}

				@Override
				public Object getValue() {
					return AnyUtils.convertAny(t.value);
				}

				@Override
				public Object setValue(Object obj) {
					return t.value = createAny(obj);
				}
			};
        	
			entrySet.add(entry);
        }
        
        return entrySet;
		
	}

	@Override
	public Object get(Object key) {
		return getValue(key+"");
	}

	@Override
	public boolean isEmpty() {
		return structProperties.isEmpty();
	}

	@Override
	public Set<String> keySet() {
		Set<String> keys = new HashSet<>();
		for(DataType t : structProperties){
			keys.add(t.id);
		}
		
		return keys;
	}

	@Override
	public Object put(String key, Object value) {
		try {
			setValue(key, value);
			return value;
		} catch (Exception e) {
			logger.severe(e.getMessage());
			return null;
		}
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> values) {
		Map<String, Object> valuesToChange = new HashMap<>();
		
		for(String key : values.keySet()){
			valuesToChange.put(key, values.get(key));
		}
		
		try {
			setValues(valuesToChange);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}

	@Override
	public Object remove(Object arg0) {
		throw new UnsupportedOperationException("Remove is not supported..");
	}

	@Override
	public int size() {
		return toMap().size();
	}

	@Override
	public Collection<Object> values() {
		return toMap().values();
	}
}
