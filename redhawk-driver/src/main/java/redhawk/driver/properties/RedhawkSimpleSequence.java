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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.TypeCodePackage.BadKind;
import org.ossie.properties.AnyUtils;

/**
 * Wrapper class for interacting with Simple Sequences.  
 *
 */
public class RedhawkSimpleSequence extends RedhawkProperty {

    private String simpleSeqId;
    private List<Object> values; 
    private TypeCode tcKind;
    
    public RedhawkSimpleSequence(ORB orb, String parentObject, String simpleSeqId, Object[] values, TypeCode typeCode){
        this.orb = orb;
        this.parentIOR = parentObject;
        this.simpleSeqId = simpleSeqId;
        if(typeCode.kind() == TCKind.tk_alias) {
        	try {
				this.tcKind = typeCode.content_type();
			} catch (BadKind e) {
				this.tcKind = typeCode;
			}
        }else {
            this.tcKind = typeCode;        	
        }
        
        this.values = new ArrayList<Object>();
        for(Object obj : values){
        	this.values.add(obj);
        }
    }
    
	@Override
	public <T> void setValue(T value) throws Exception {
		values = Arrays.asList(this.convertToObjectArray(value));
		
    	Any any = AnyUtils.toAnySequence(values.toArray(), tcKind);
        reconfigure(simpleSeqId, any);
	}
    
    public void addValue(Object value) throws Exception {
    	values.add(value);
        Any any = AnyUtils.toAnySequence(values.toArray(), tcKind);
    	
    
    	reconfigure(simpleSeqId, any);
    }
    
    public void clearAllValues() throws Exception{
    	values.clear();
    	Any any = AnyUtils.toAnySequence(values.toArray(), tcKind);
        reconfigure(simpleSeqId, any);
    }
    
    public void addValues(Object[] valuesToAdd) throws Exception{
        for(Object obj : valuesToAdd){
        	this.values.add(obj);
        }
        Any any = AnyUtils.toAnySequence(values.toArray(), tcKind);
        reconfigure(simpleSeqId, any);
    }
    
    public void removeValue(Object valueToRemove) throws Exception{
   		values.remove(valueToRemove);
   		Any any = AnyUtils.toAnySequence(values.toArray(), tcKind);
        reconfigure(simpleSeqId, any);
    }
    
    public List<Object> getValues(){
    	return values;
    }
    
    //TODO: Possibly clean this up to not do implicit boxing
    //https://stackoverflow.com/questions/16427319/cast-object-to-array
    private static Object[] convertToObjectArray(Object array) {
        Class ofArray = array.getClass().getComponentType();
        if (ofArray.isPrimitive()) {
            List ar = new ArrayList();
            int length = Array.getLength(array);
            for (int i = 0; i < length; i++) {
                ar.add(Array.get(array, i));
            }
            return ar.toArray();
        }
        else {
            return (Object[]) array;
        }
    }

	@Override
	public String toString() {
		return "RedhawkSimpleSequence [simpleSeqId=" + simpleSeqId + ", values=" + values + ", tcKind=" + tcKind + "]";
	}
}
