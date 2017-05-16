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

/**
 * POJO wrapping interaction with Simple properties. 
 */
public class RedhawkSimple extends RedhawkProperty {

    private String simpleId;
    private Object value; 
    
    /**
     * 
     * @param orb
     * @param parentObject
     * @param simpleId
     * @param value
     */
    public RedhawkSimple(ORB orb, String parentObject, String simpleId, Object value){
        this.orb = orb;
        this.parentObject = parentObject;
        this.simpleId = simpleId;
        this.value = value;
    }
    
    public void setValue(Object value) throws Exception {
        Any any = createAny(value);
        this.value = value;  
        reconfigure(simpleId, any);
    }
    

    public Object getValue() {
        return value;  
    }

	@Override
	public String toString() {
		return String.format("RedhawkSimple [simpleId=%s, value=%s]", simpleId, value);
	}
    
    
}
