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

import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;
import org.ossie.properties.AnyUtils;

import CF.DataType;
import CF.PropertiesHolder;
import redhawk.driver.RedhawkUtils;

/**
 * POJO wrapping interaction with Simple properties. 
 */
public class RedhawkSimple extends RedhawkProperty {
	/**
	 * Simple Id for Property
	 */
    private String simpleId;
    
    /**
     * Java representation of value 
     */
    private Object value; 
    

    
    public RedhawkSimple(ORB orb, String parentIOR, DataType type) {
    	this.orb = orb;
    	this.parentIOR = parentIOR;
    	this.corbaProperty = type;
    	this.simpleId = type.id;
    	this.value = AnyUtils.convertAny(type.value, type.value.type());
    }
    
    /**
     * 
     * @param orb
     * @param parentObject
     * @param simpleId
     * @param value
     */
    public RedhawkSimple(ORB orb, String parentObject, String simpleId, Object value){
        this.orb = orb;
        this.parentIOR = parentObject;
        this.simpleId = simpleId;
        this.value = value;
    }
    
    public <T> void setValue(T value) throws Exception {
        //Any any = createAny(value);
        //System.out.println("Created any code "+any.type());
        //System.out.println("Actual any code "+this.corbaProperty.value.type());
        Any any = AnyUtils.toAny(value, corbaProperty.value.type());
    	this.value = value;  
        reconfigure(simpleId, any);
    }
    
    @Override
	public Object getValue(Boolean requery) {
    	if(requery) {
    		PropertiesHolder ph = RedhawkUtils.getPropertyFromCORBAObject(this.orb, this.parentIOR, this.simpleId);
    		
    		//Should always only be one value in this structure
    		value = AnyUtils.convertAny(ph.value[0].value);
    	}
    	
    	return value;
	} 

	@Override
	public String toString() {
		return String.format("RedhawkSimple [simpleId=%s, value=%s]", simpleId, value);
	}   
}
