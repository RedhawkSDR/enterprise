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
import org.omg.CORBA.TCKind;
import org.ossie.properties.AnyUtils;

import CF.DataType;
import CF.PropertiesHelper;
import CF.PropertySet;
import CF.PropertySetHelper;
import CF.PropertySetPackage.InvalidConfiguration;
import CF.PropertySetPackage.PartialConfiguration;

//TODO: Make this an abstract class or something...
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
	
    public Object getValue() {
        return getValue(true);
    }

    /**
     * Whether of not to query actual corba object again for property value 
     * or to use the one already stored in object. 
     * 
     * @param requery
     * @return
     */
	public abstract Object getValue(Boolean requery); 
	
	//TODO: Why isn't this just using AnyUtils
	protected Any createAny(Object objectToCreate) {
		final Any any = orb.create_any();
		if (objectToCreate instanceof String) {
			any.insert_string((String) objectToCreate);
		} else if (objectToCreate instanceof Short) {
			any.insert_short((Short) objectToCreate);
		} else if (objectToCreate instanceof Boolean) {
			any.insert_boolean((Boolean) objectToCreate);
		} else if (objectToCreate instanceof Long) {
			any.insert_longlong((Long) objectToCreate);
		} else if (objectToCreate instanceof Character) {
			any.insert_char((Character) objectToCreate);
		} else if (objectToCreate instanceof Double) {
			any.insert_double((Double) objectToCreate);
		} else if (objectToCreate instanceof Float) {
			any.insert_float((Float) objectToCreate);
		} else if (objectToCreate instanceof Integer) {
			any.insert_long((Integer) objectToCreate);
		} else if (objectToCreate instanceof Byte) {
			any.insert_octet((Byte) objectToCreate);
		} else if (objectToCreate instanceof Map) {
			Map objMap = (Map) objectToCreate;
			List<DataType> dataTypesToInsert = new ArrayList<DataType>();

			for (Object key : objMap.keySet()) {
				DataType dt = new DataType();
				dt.id = key + "";
				dt.value = createAny(objMap.get(key));
				dataTypesToInsert.add(dt);
			}

			Any anyObject = orb.create_any();
			PropertiesHelper.insert(anyObject, dataTypesToInsert.toArray(new DataType[dataTypesToInsert.size()]));
			return anyObject;

		} else if (objectToCreate instanceof Object[]) {
			Object[] objects = (Object[]) objectToCreate;

			if (objects.length < 1) {
				logger.log(Level.FINE, "Empty array provided, returning empty any");
				return any;
			}

			// determining type based on first entry of array
        	org.omg.CORBA.TypeCode tcElement = orb.get_primitive_tc(getTCKind(objects[0]));
        	org.omg.CORBA.TypeCode typeCodeForSequence = orb.create_sequence_tc(objects.length, tcElement);

	        return AnyUtils.toAnySequence(objectToCreate, typeCodeForSequence);
		}

		return any;
	}
	
	protected Any createAny(Object objectToCreate, TCKind kind) {
		//Should use AnyUtils for everything that's not a collection
		final Any any = orb.create_any();
		if(objectToCreate instanceof Object[]) {
			Object[] objects = (Object[]) objectToCreate;

			if (objects.length < 1) {
				logger.log(Level.FINE, "Empty array provided, returning empty any");
				return any;
			}

			// determining type based on first entry of array
        	org.omg.CORBA.TypeCode tcElement = orb.get_primitive_tc(getTCKind(objects[0]));
        	org.omg.CORBA.TypeCode typeCodeForSequence = orb.create_sequence_tc(objects.length, tcElement);

	        return AnyUtils.toAnySequence(objectToCreate, typeCodeForSequence);			
		}else if(objectToCreate instanceof Map) {
			Map objMap = (Map) objectToCreate;
			List<DataType> dataTypesToInsert = new ArrayList<DataType>();

			for (Object key : objMap.keySet()) {
				DataType dt = new DataType();
				dt.id = key + "";
				dt.value = createAny(objMap.get(key));
				dataTypesToInsert.add(dt);
			}

			Any anyObject = orb.create_any();
			PropertiesHelper.insert(anyObject, dataTypesToInsert.toArray(new DataType[dataTypesToInsert.size()]));
			return anyObject;			
		}else {
			return AnyUtils.toAny(objectToCreate, kind);			
		}
	}

	protected TCKind getTCKind(Object objectToCreate) {

		if (objectToCreate instanceof String) {
			return TCKind.tk_string;
		} else if (objectToCreate instanceof Short) {
			return TCKind.tk_short;
		} else if (objectToCreate instanceof Boolean) {
			return TCKind.tk_boolean;
		} else if (objectToCreate instanceof Long) {
			return TCKind.tk_longlong;
		} else if (objectToCreate instanceof Character) {
			return TCKind.tk_char;
		} else if (objectToCreate instanceof Double) {
			return TCKind.tk_double;
		} else if (objectToCreate instanceof Float) {
			return TCKind.tk_float;
		} else if (objectToCreate instanceof Integer) {
			return TCKind.tk_long;
		} else if (objectToCreate instanceof Byte) {
			return TCKind.tk_octet;
		}
		return TCKind.tk_any; // TOOO, probably not a good idea
	}

}
