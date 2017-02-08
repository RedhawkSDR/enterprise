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
import org.ossie.properties.AnyUtils;

import CF.DataType;
import CF.PropertiesHelper;

public class RedhawkStructSequence extends RedhawkProperty {

    private String sequenceId;
    private List<Any> corbaSeq = new CopyOnWriteArrayList<Any>();
    protected List<RedhawkStruct> sequence = new CopyOnWriteArrayList<RedhawkStruct>();
    
    public RedhawkStructSequence(ORB orb, String parentObject, String id, Any[] propertyValue) {
        this.sequenceId = id;
        this.parentObject = parentObject;
        this.orb = orb;
        for(int i = 0; i < propertyValue.length; i++){
            DataType[] struct = (DataType[]) AnyUtils.convertAny(propertyValue[i]);
            RedhawkStruct rhStruct = new RedhawkStruct(orb, parentObject, null, struct, this);
            corbaSeq.add(propertyValue[i]);
            sequence.add(rhStruct);
        }
    }
    
    public String toString(){
        return sequence.toString();
    }
    
    
    public DataType[] getDataTypeArray() {
		Any[] newSequence = corbaSeq.toArray(new Any[corbaSeq.size()]);
        Any newAny = AnyUtils.toAny(newSequence, TCKind.tk_any);
        DataType[] result = new DataType[1];
		result[0] = new DataType(sequenceId, newAny);
		return result;
    }
    
    public void addStructsToSequence(List<Map<String,Object>> elementsToAdd) throws Exception {
        
    	for(Map<String,Object> elements : elementsToAdd){
	    	List<DataType> dataTypesBuilder = new ArrayList<DataType>();
	        for( Object key : elements.keySet()){
	            dataTypesBuilder.add(new DataType(key+"", createAny(elements.get(key))));
	        }
	        
	        Any structToInsert = orb.create_any();
	        DataType[] ss2I = dataTypesBuilder.toArray(new DataType[dataTypesBuilder.size()]);
	        PropertiesHelper.insert(structToInsert, ss2I);
	        corbaSeq.add(structToInsert);
	        sequence.add(new RedhawkStruct(orb, parentObject, null, ss2I, this));
    	}
    	
        reconfigureStructSequence();   
    }
    
    public void addStructToSequence(Map<String,Object> elementToAdd) throws Exception {
        List<DataType> dataTypesBuilder = new ArrayList<DataType>();
        Map element = (Map) elementToAdd;
        for( Object key : element.keySet()){
            dataTypesBuilder.add(new DataType(key+"", createAny(element.get(key))));
        }
        
        Any structToInsert = orb.create_any();
        DataType[] ss2I = dataTypesBuilder.toArray(new DataType[dataTypesBuilder.size()]);
        PropertiesHelper.insert(structToInsert, ss2I);
        corbaSeq.add(structToInsert);
        sequence.add(new RedhawkStruct(orb, parentObject, null, ss2I, this));
        reconfigureStructSequence();   
    }
    
    public List<RedhawkStruct> getStructs(){
        return sequence;
    }
    
    public RedhawkStruct findStructWithPropertyThatMatches(String property, Object valueToMatch){
        for(RedhawkStruct struct : sequence){
            Object propVal = struct.getValue(property);
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
        for(RedhawkStruct struct : sequence){
            Object propVal = struct.getValue(property);
            
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
        for(RedhawkStruct struct : sequence){
            Object propVal = struct.getValue(property);
            if(propVal != null && propVal.equals(valueToMatch)){
                int structIndex = sequence.indexOf(struct);
                corbaSeq.remove(structIndex);
                sequence.remove(structIndex);
                reconfigureStructSequence();
            }
        }
    }
    
    public RedhawkStruct getStructByPropertyAndValue(String propertyId, Object valueToMatch){
        for(RedhawkStruct struct : sequence){
            Object propVal = struct.getValue(propertyId);
            if(propVal != null && propVal.equals(valueToMatch)){
                return struct;
            }
        }
        
        return null;
    }
    
    
    public List<RedhawkStruct> getStructsByPropertyAndValue(String propertyId, Object valueToMatch){
        List<RedhawkStruct> structsToReturn = new ArrayList<RedhawkStruct>();
        for(RedhawkStruct struct : sequence){
            Object propVal = struct.getValue(propertyId);
            if(propVal != null && propVal.equals(valueToMatch)){
                structsToReturn.add(struct);
            }
        }
        
        return structsToReturn;
    }    
    
    
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
                    type.value = createAny(valuesToUpdate.get(key));
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
            sequence.remove(structIndex);
            sequence.add(structIndex, newStruct);
        }
        
        
        reconfigureStructSequence();
        return (DataType[]) AnyUtils.convertAny(corbaSeq.get(structIndex));
    }
    
    
    
    public List<Map<String, Object>> toListOfMaps(){
    	List<Map<String,Object>> listOfMaps = new ArrayList<Map<String,Object>>();
    	
    	for(RedhawkStruct struct : getStructs()){
    		listOfMaps.add(struct.toMap());
    	}
    	
    	return listOfMaps;
    }
    
    
    private void reconfigureStructSequence() throws Exception {
        Any[] newSequence = corbaSeq.toArray(new Any[corbaSeq.size()]);
        Any newAny = AnyUtils.toAny(newSequence, TCKind.tk_any);
        reconfigure(sequenceId, newAny);
    }
    
    
}
