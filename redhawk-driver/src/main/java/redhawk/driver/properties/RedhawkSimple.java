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
    

    
    public RedhawkSimple(ORB orb, String parentIOR, DataType type, Object value) {
    	this.orb = orb;
    	this.parentIOR = parentIOR;
    	this.corbaProperty = type;
    	this.simpleId = type.id;
    	this.value = value;
    }
    
    /**
     * 
     * @param orb
     * @param parentObject
     * @param simpleId
     * @param value
     */
    @Deprecated
    public RedhawkSimple(ORB orb, String parentObject, String simpleId, Object value){
        this.orb = orb;
        this.parentIOR = parentObject;
        this.simpleId = simpleId;
        this.value = value;
    }
    
    public <T> void setValue(T value) throws Exception {
        Any any = AnyUtils.toAny(value, corbaProperty.value.type());
    	this.value = value;  
        reconfigure(simpleId, any);
    }
    
    @Override
	public <T> T getValue(Boolean requery) {
    	if(requery) {
    		PropertiesHolder ph = RedhawkUtils.getPropertyFromCORBAObject(this.orb, this.parentIOR, this.simpleId);
    		
    		/*
    		 * If PH is not equal to null will just use property value already set. This could 
    		 * be because the kindtype is `allocation` and action is `eq`
    		 */
    		if(ph!=null)
    			value = AnyUtils.convertAny(ph.value[0].value);
    	}
    	
    	return (T)value;
	} 

	@Override
	public String toString() {
		return String.format("RedhawkSimple [simpleId=%s, value=%s]", simpleId, value);
	}   
}
