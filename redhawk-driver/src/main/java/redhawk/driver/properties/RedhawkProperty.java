package redhawk.driver.properties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;

import CF.DataType;
import CF.PropertiesHelper;
import CF.PropertySet;
import CF.PropertySetHelper;
import CF.PropertySetPackage.InvalidConfiguration;
import CF.PropertySetPackage.PartialConfiguration;

public class RedhawkProperty {

    protected String parentObject;
    protected ORB orb;
    
    /**
     * Reset the property value for the specified Id.
     * 
     * @param propertyId
     * @param propValue
     * @throws Exception
     */
    protected void reconfigure(String propertyId, Any propValue) throws Exception {
        DataType[] propertiesToConfigure = new DataType[] { new DataType(propertyId, propValue) };

        try {
        	PropertySet properties = PropertySetHelper.narrow(orb.string_to_object(parentObject));
        	properties.configure(propertiesToConfigure);
        } catch (InvalidConfiguration e) {
            throw new InvalidConfiguration();
        } catch (PartialConfiguration e) {
            throw new PartialConfiguration();
        }
    }
    
    
    protected Any createAny(Object objectToCreate){
        final Any any = orb.create_any();
        if(objectToCreate instanceof String){
            any.insert_string((String) objectToCreate);
        } else if(objectToCreate instanceof Short){
            any.insert_short((Short) objectToCreate);
        } else if(objectToCreate instanceof Boolean){
            any.insert_boolean((Boolean) objectToCreate);
        } else if(objectToCreate instanceof Long){
            any.insert_longlong((Long) objectToCreate);
        } else if(objectToCreate instanceof Character){
            any.insert_char((Character) objectToCreate);
        } else if(objectToCreate instanceof Double){
            any.insert_double((Double) objectToCreate);
        } else if(objectToCreate instanceof Float){
            any.insert_float((Float) objectToCreate);
        } else if(objectToCreate instanceof Integer){
            any.insert_long((Integer) objectToCreate);
        } else if(objectToCreate instanceof Byte){
            any.insert_octet((Byte) objectToCreate);
        } else if(objectToCreate instanceof Map){
            Map objMap = (Map) objectToCreate;
            List<DataType> dataTypesToInsert = new ArrayList<DataType>();
            
            for(Object key : objMap.keySet()){
                DataType dt = new DataType();
                dt.id = key+"";
                dt.value = createAny(objMap.get(key));
                dataTypesToInsert.add(dt);
            }
            
            Any anyObject = orb.create_any();
            PropertiesHelper.insert(anyObject, dataTypesToInsert.toArray(new DataType[dataTypesToInsert.size()]));
            return anyObject;
            
        }
        
        return any;
    }
    
}
