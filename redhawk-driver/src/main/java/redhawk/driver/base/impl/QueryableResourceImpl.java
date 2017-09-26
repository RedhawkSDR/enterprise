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
package redhawk.driver.base.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;
import org.omg.CORBA.TypeCode;
import org.ossie.properties.AnyUtils;

import CF.DataType;
import CF.PropertiesHolder;
import CF.PropertySetHelper;
import CF.PropertySetOperations;
import CF.UnknownProperties;
import redhawk.driver.base.QueryableResource;
import redhawk.driver.properties.RedhawkProperty;
import redhawk.driver.properties.RedhawkSimple;
import redhawk.driver.properties.RedhawkSimpleSequence;
import redhawk.driver.properties.RedhawkStruct;
import redhawk.driver.properties.RedhawkStructSequence;

/**
 * Helper methods for retrieving Properties from CORBA backed REDHAWK objects. 
 *
 * @param <TParsedClass>
 */
public abstract class QueryableResourceImpl<TParsedClass> extends CorbaBackedObject<TParsedClass> implements QueryableResource{
    private static Logger logger = Logger.getLogger(QueryableResourceImpl.class.getName());
    
    /**
     * @param ior
     * 	Interoparable Object Reference for the Object your trying to get. 
     * @param orb
     * 	ORB that is aware of the object you're trying to retrieve. 
     */
    public QueryableResourceImpl(String ior, ORB orb) {
    	super(ior, orb);
	}
    
    /**
     * @return Properties related to the object.
     */
    //TODO: The key isn't very unique here will cause problems on anything 
    //That's not just a component. 
    public Map<String, RedhawkProperty> getProperties(){
        Map<String, RedhawkProperty> propMap = new HashMap<String, RedhawkProperty>();
        PropertiesHolder ph = query(null);

        for(DataType property : ph.value){
            propMap.put(property.id, getAndCast(property));
        }

        return propMap;
    }
    
    /**
     * Retrieve properties by name.
     *  
     * @param propertyNames names of the properties you'd like to retrieve. 
     * @return
     */
    public <T> T getProperty(String ... propertyNames){

        PropertiesHolder ph = query(propertyNames);
        
        if(ph != null) {
        	if(ph.value.length==1){
                for(DataType property : ph.value){
                    return (T)getAndCast(property);
                }        		
        	}else{
                Map<String, RedhawkProperty> propMap = new HashMap<String, RedhawkProperty>();        		
        		for(DataType property : ph.value){
                    propMap.put(property.id, getAndCast(property));
        		}
        		return (T) propMap;
        	}
        }
        
        return null;
    }
    
    public void setProperty(String propertyName, Object propertyValue) throws Exception {
    	RedhawkProperty property = this.getProperty(propertyName);
    
    	if(property instanceof RedhawkSimple) {
    		property.setValue(propertyValue);
    	}else if(property instanceof RedhawkSimpleSequence){
    		property.setValue(propertyValue);
    	}else if(property instanceof RedhawkStruct) {
    		property.setValue(propertyValue);
    	}else if(property instanceof RedhawkStructSequence){ 
    		property.setValue(propertyValue);
    	}else {
    		System.err.println("Unhandled property type "+property.getClass().toString());
    	}
    }
    
    //TODO: Properties currently need to redo this code put in one place 
    private PropertiesHolder query(String ... propertyNames){
        PropertiesHolder ph = new PropertiesHolder();
        ph.value = new DataType[]{};

        if(propertyNames != null){
            List<DataType> dataTypes = new ArrayList<DataType>();
            for(String propertyName : propertyNames){
                dataTypes.add(new DataType(propertyName, getOrb().create_any()));
            }
            
            ph.value = dataTypes.toArray(new DataType[dataTypes.size()]);
        }

        try {
        	PropertySetOperations properties = PropertySetHelper.narrow(getOrb().string_to_object(getIor()));
        	properties.query(ph);
        } catch (UnknownProperties e) {
            logger.log(Level.FINE, "Could not find property: " + propertyNames, e);
            return null;
        }        
     
        return ph;
    }
    
    
    /**
     * Return a DataType property object as a POJO
     * 
     * @param property
     * @return
     * 
     */
    private RedhawkProperty getAndCast(DataType property){
    	//TODO: Is this still necessary
    	ClassLoader cl = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
    	Object propertyValue = AnyUtils.convertAny(property.value);
		Thread.currentThread().setContextClassLoader(cl);
    	
		if(propertyValue instanceof Any[]){
            return new RedhawkStructSequence(getOrb(), getIor(), property.id, (Any[]) propertyValue);
        } else if(propertyValue instanceof DataType[]) {
            return new RedhawkStruct(getOrb(), getIor(), property, (DataType[]) propertyValue);
        } else if(propertyValue instanceof Object[]){
            return new RedhawkSimpleSequence(getOrb(), getIor(),  property.id, (Object[]) propertyValue, property);
        } else {
            return new RedhawkSimple(getOrb(), getIor(),  property, propertyValue);
        }
    }
    
    
}