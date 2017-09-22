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
import java.util.concurrent.CopyOnWriteArrayList;

import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.TypeCode;
import org.ossie.properties.AnyUtils;

import CF.DataType;
import CF.PropertiesHelper;
import CF.PropertiesHolder;
import redhawk.driver.RedhawkUtils;

/**
 * Wrapper class for interacting with Struct Sequences. 
 */
public class RedhawkStructSequence extends RedhawkProperty {

    private String sequenceId;
    //TODO: Why do we need copy on write??
    private List<Any> corbaSeq = new CopyOnWriteArrayList<Any>();
    private List<Map<String, Object>> sequence = new CopyOnWriteArrayList<Map<String, Object>>();
    private Map<String, TypeCode> keyToTypeCode = new HashMap<>();
    
    public RedhawkStructSequence(ORB orb, String parentObject, String id, Any[] propertyValue) {
        this.sequenceId = id;
        this.parentIOR = parentObject;
        this.orb = orb;
        
        /*
         * Loop through Any's and convert to RedhawkStruct objects
         * CORBA should be :
         * DataType[DataType[]]
         */
        for(int i = 0; i < propertyValue.length; i++){
            DataType[] structArray = (DataType[]) AnyUtils.convertAny(propertyValue[i]);
        	
            //Add any to corba sequence
            corbaSeq.add(propertyValue[i]);
        	
        	/*
        	 * Convert DataType[] to java representation of a struct 
        	 * Map<String, Object>()
        	 */
        	Map<String, Object> structRep = new HashMap<>();
        	for(DataType dt : structArray) {
                /*
                 * Need a key to typeCode cache to be able to easily manipulate 
                 * incoming maps from user
                 */
        		keyToTypeCode.put(dt.id, dt.value.type());
        		this.dataTypeToJavaObjectConverter(structRep, dt);
        	}
        	
        	//Add Struct Rep to sequence
        	sequence.add(structRep);
        }
    }
    
    public DataType[] getDataTypeArray() {
		Any[] newSequence = corbaSeq.toArray(new Any[corbaSeq.size()]);
        Any newAny = AnyUtils.toAny(newSequence, TCKind.tk_any);
        DataType[] result = new DataType[1];
		result[0] = new DataType(sequenceId, newAny);
		return result;
    }
    
    public void addStructsToSequence(List<Map<String,Object>> elementsToAdd) throws Exception {
		//TODO: Add checks to make sure user is adding appropriate key value pairs
    	
    	/*
		 * Loop through provided new elements and add them to current 
		 * struct. 
		 */
    	for(Map<String,Object> elements : elementsToAdd){
	    	List<DataType> dataTypesBuilder = new ArrayList<DataType>();
	        
	    	Any any = createAnyFromMap(elements);
            dataTypesBuilder.add(new DataType(this.sequenceId, any));
	        
	        Any structToInsert = orb.create_any();
	        DataType[] ss2I = dataTypesBuilder.toArray(new DataType[dataTypesBuilder.size()]);	        
	        PropertiesHelper.insert(structToInsert, ss2I);
	        
	        for(DataType typeArray : ss2I) {
	        	Object propertyValue = AnyUtils.convertAny(typeArray.value);
	        	DataType[] type2Array = (DataType[]) propertyValue; 
	        	corbaSeq.add(typeArray.value);
	        	sequence.add(dataTypeArrayToMap(type2Array));
	        }
    	}
    	
        reconfigureStructSequence();   
    }
    
    /**
     * Uses keyToTypeCode map to assist in converting 
     * Map to an Any. 
     * 
     * @param map
     * @return
     */
    private Any createAnyFromMap(Map<String, Object> map) {
		List<DataType> dataTypesToInsert = new ArrayList<DataType>();

		for (Map.Entry<String, Object> entry : map.entrySet()) {
			DataType dt = new DataType();
			dt.id = entry.getKey();
			dt.value = AnyUtils.toAny(entry.getValue(), keyToTypeCode.get(entry.getKey()));
			dataTypesToInsert.add(dt);
		}

		Any anyObject = orb.create_any();
		PropertiesHelper.insert(anyObject, dataTypesToInsert.toArray(new DataType[dataTypesToInsert.size()]));
		return anyObject;
    }
    
    public void addStructToSequence(Map<String,Object> elementToAdd) throws Exception {
    	List<Map<String, Object>> elementsToAdd = new ArrayList<>();
    	elementsToAdd.add(elementToAdd);
    	
    	this.addStructsToSequence(elementsToAdd);
    }
    
    @Deprecated
    public List<Map<String, Object>> getStructs(){
        return sequence;
    }
    
    public Map<String, Object> findStructWithPropertyThatMatches(String property, Object valueToMatch){
        for(Map<String, Object> struct : sequence){
            Object propVal = struct.get(property);
            if(propVal != null && propVal.equals(valueToMatch)){
                return struct;
            }
        }
        return null;
    }
    
    public void removeAllStructs() throws Exception {
        sequence.clear();
        corbaSeq.clear();
        reconfigureStructSequence();
    }
    
    
    public void removeStructsWithPropertyThatMatches(String property, List<Object> valuesToMatch) throws Exception {
        for(Map<String, Object> struct : sequence){
            Object propVal = struct.get(property);
            
            for(Object obj : valuesToMatch){
	            if(propVal != null && propVal.equals(obj)){
	                int structIndex = sequence.indexOf(struct);
	                corbaSeq.remove(structIndex);
	                sequence.remove(structIndex);
	                break;
	            }
            }
        }
        
        reconfigureStructSequence();
        
    }
    
    
    public void removeStructWithPropertyThatMatches(String property, Object valueToMatch) throws Exception {
        for(Map<String, Object> struct : sequence){
            Object propVal = struct.get(property);
            if(propVal != null && propVal.equals(valueToMatch)){
                int structIndex = sequence.indexOf(struct);
                corbaSeq.remove(structIndex);
                sequence.remove(structIndex);
                reconfigureStructSequence();
            }
        }
    }
    
    public Map<String, Object> getStructByPropertyAndValue(String propertyId, Object valueToMatch){
        for(Map<String, Object> struct : sequence){
            Object propVal = struct.get(propertyId);
            if(propVal != null && propVal.equals(valueToMatch)){
                return struct;
            }
        }
        
        return null;
    }
    
    
    public List<Map<String, Object>> getStructsByPropertyAndValue(String propertyId, Object valueToMatch){
        List<Map<String, Object>> structsToReturn = new ArrayList<Map<String, Object>>();
        for(Map<String, Object> struct : sequence){
            Object propVal = struct.get(propertyId);
            if(propVal != null && propVal.equals(valueToMatch)){
                structsToReturn.add(struct);
            }
        }
        
        return structsToReturn;
    }    
    
    //TODO: Get rid of these I believe this use case is invalid
    protected DataType[] updateStruct(RedhawkStruct structToUpdate, String propertyId, Object value) throws Exception {
        Map<String, Object> valuesToUpdate = new HashMap<String, Object>();
        valuesToUpdate.put(propertyId, value);
        return updateStruct(structToUpdate, valuesToUpdate);
    }
    
    
    protected DataType[] updateStruct(RedhawkStruct structToUpdate, Map<String, Object> valuesToUpdate) throws Exception {
        int structIndex = sequence.indexOf(structToUpdate);
        Any struct = corbaSeq.get(structIndex);
        
        DataType[] actualStruct = (DataType[]) AnyUtils.convertAny(struct);
        boolean modified = false;
        for(DataType type : actualStruct){
            for(String key : valuesToUpdate.keySet()){
                if(type.id.toLowerCase().matches(key.toLowerCase())){
                    type.value = AnyUtils.toAny(valuesToUpdate.get(key), type.value.type().kind());
                    modified = true;
                }
            }
        }     
        
        RedhawkStruct newStruct = null;
        if(modified){
            Any updatedStruct = orb.create_any();
            PropertiesHelper.insert(updatedStruct, actualStruct);
            corbaSeq.remove(structIndex);
            corbaSeq.add(structIndex, updatedStruct);
            
            //updated struct should become a DataType[] 
        	Object propertyValue = AnyUtils.convertAny(updatedStruct);
        	DataType[] type2Array = (DataType[]) propertyValue; 
        	
            sequence.remove(structIndex);
            sequence.add(structIndex, this.dataTypeArrayToMap(type2Array));
        }
        
        
        reconfigureStructSequence();
        return (DataType[]) AnyUtils.convertAny(corbaSeq.get(structIndex));
    }
    
    
    
    /**
     * Returns a list of maps
     * @return
     */
    @Deprecated
    public List<Map<String, Object>> toListOfMaps(){
    	return this.getValue(false);
    }
    
	@Override
	public <T> void setValue(T value) throws Exception {
		try {
			List<Map<String, Object>> obj = (List<Map<String, Object>>) value;
			
			//Clear out old values from corbaSeq & sequence then reuse add logic 
			corbaSeq.clear();
			sequence.clear();
			
			this.addStructsToSequence(obj);
		}catch(ClassCastException ex) {
			throw new Exception("Expecting a value castable to List<Map<String, Object>>");
		}
	}

	@Override
	public <T> T getValue(Boolean requery) {
		if(requery) {
			//Query for sequence
			PropertiesHolder ph = RedhawkUtils.getPropertyFromCORBAObject(this.orb, this.parentIOR, this.sequenceId);
			
			//Reinitialize list for latest values
			corbaSeq.clear();
			sequence.clear();
			for(DataType property : ph.value) {
		    	Any[] propertyValue = (Any[])AnyUtils.convertAny(property.value);
		        
		        for(int i = 0; i < propertyValue.length; i++){
		            DataType[] structArray = (DataType[]) AnyUtils.convertAny(propertyValue[i]);
		        	
		            //Add any to corba sequence
		            corbaSeq.add(propertyValue[i]);
		        	
		        	/*
		        	 * Convert DataType[] to java representation of a struct 
		        	 * Map<String, Object>()
		        	 */
		        	Map<String, Object> structRep = new HashMap<>();
		        	for(DataType dt : structArray) {
		        		this.dataTypeToJavaObjectConverter(structRep, dt);
		        	}
		        	
		        	//Add Struct Rep to sequence
		        	sequence.add(structRep);
		        }
			}
		}
		
		return (T) sequence;
	}
	
	/**
	 * Helper method for converting Struct(DataType[]) to Java representation 
	 * of a struct Map<String, Object>()
	 * 
	 * @param array
	 * 
	 * @return
	 */
	private Map<String, Object> dataTypeArrayToMap(DataType[] array){
    	Map<String, Object> structRep = new HashMap<>();
    	for(DataType dt : array) {
    		this.dataTypeToJavaObjectConverter(structRep, dt);
    	}
    	
    	return structRep;
	}
    
    private void reconfigureStructSequence() throws Exception {
        Any[] newSequence = corbaSeq.toArray(new Any[corbaSeq.size()]);
        Any newAny = AnyUtils.toAny(newSequence, TCKind.tk_any);
        reconfigure(sequenceId, newAny);
    }
	
	@Override
	public String toString(){
    	StringBuilder build = new StringBuilder();
    	build.append("RedhawkStructSequence: [sequenceId ="+this.sequenceId+"\n");
    	build.append("Structs: \n");
    	build.append(sequence.toString());
    	
    	return build.toString();
    }
}
