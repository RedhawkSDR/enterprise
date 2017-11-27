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
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;
import org.ossie.properties.AnyUtils;

import CF.DataType;
import CF.PropertySet;
import CF.PropertySetHelper;
import CF.PropertySetPackage.InvalidConfiguration;
import CF.PropertySetPackage.PartialConfiguration;

public abstract class RedhawkProperty {
	private static Logger logger = Logger.getLogger(RedhawkProperty.class.getName());
	
	protected String parentIOR;
	
	protected ORB orb;

	/**
	 * CORBA representation of property so you can always get back to originals typeCode
	 * or other information you may need. 
	 * 
	 */
	protected DataType corbaProperty; 
	
	/**
	 * Reset the property value for the specified Id.
	 * 
	 * @param propertyId
	 * @param propValue
	 * @throws Exception
	 */
	public void reconfigure(String propertyId, Any propValue) throws Exception {
		DataType[] propertiesToConfigure = new DataType[] { new DataType(propertyId, propValue) };

		try {
			PropertySet properties = PropertySetHelper.narrow(orb.string_to_object(parentIOR));
			properties.configure(propertiesToConfigure);
		} catch (InvalidConfiguration e) {
			throw new InvalidConfiguration();
		} catch (PartialConfiguration e) {
			throw new PartialConfiguration();
		}
	}
	
	public abstract <T> void setValue(T value) throws Exception;
	
    public <T> T getValue() {
        return getValue(true);
    }

    /**
     * Whether of not to query actual corba object again for property value 
     * or to use the one already stored in object. 
     * 
     * @param requery
     * @return
     */
	public abstract <T> T getValue(Boolean requery); 
	
	//TODO: Probably move this over to REHDAWKUtils 
	protected void dataTypeToJavaObjectConverter(Map<String, Object> struct, DataType type) {
		Object propertyValue = AnyUtils.convertAny(type.value);
		if(propertyValue instanceof Object[]) {
    		List<Object> tempList = new ArrayList<>();
    		Object[] tempArray = (Object[]) propertyValue;
    		
    		if(tempArray.length>0) {
    			if(tempArray[0] instanceof DataType) {
        			//Use recursion
        			dataTypeToJavaObjectConverter(struct, (DataType)tempArray[0]);
        		}else {
            		for(Object obj : tempArray) {
            			tempList.add(obj);
            		}
            		
            		struct.put(type.id, tempList);    			
        		}	
    		}else {
    			logger.log(Level.WARNING, "No property found at key "+type.id+" struct="+struct);
    			//TODO: Figure out appropriate way to handle this
    			//struct.put(type.id, type.value);
    		}
    	}else if(propertyValue instanceof Object) {
    		struct.put(type.id, propertyValue);
    	}else {
    		struct.put(type.id, propertyValue);
<<<<<<< HEAD
=======
    		//logger.severe("Not handing Struct types of "+propertyValue.getClass());
>>>>>>> RI-111
    	}		
	}
}
