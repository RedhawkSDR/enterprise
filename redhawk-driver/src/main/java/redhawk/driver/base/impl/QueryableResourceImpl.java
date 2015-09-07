package redhawk.driver.base.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jacorb.orb.TypeCode;
import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;
import org.ossie.properties.AnyUtils;

import CF.DataType;
import CF.PropertiesHolder;
import CF.PropertySetHelper;
import CF.PropertySetOperations;
import CF.UnknownProperties;
import redhawk.driver.properties.RedhawkProperty;
import redhawk.driver.properties.RedhawkSimple;
import redhawk.driver.properties.RedhawkSimpleSequence;
import redhawk.driver.properties.RedhawkStruct;
import redhawk.driver.properties.RedhawkStructSequence;

public abstract class QueryableResourceImpl<TParsedClass> extends CorbaBackedObject<TParsedClass> {

    private static Logger logger = Logger.getLogger(QueryableResourceImpl.class.getName());
    
    public QueryableResourceImpl(String ior, ORB orb) {
    	super(ior, orb);
	}
    
    public Map<String, RedhawkProperty> getProperties(){
        Map<String, RedhawkProperty> propMap = new HashMap<String, RedhawkProperty>();
        PropertiesHolder ph = query(null);

        for(DataType property : ph.value){
            propMap.put(property.id, getAndCast(property));
        }

        return propMap;
    }
    
    public <T> T getProperty(String ... propertyNames){

        PropertiesHolder ph = query(propertyNames);
        
        if(ph != null) {
            for(DataType property : ph.value){
                return (T)getAndCast(property);
            }
        }
        
        return null;
    }
    
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
    
    
    private RedhawkProperty getAndCast(DataType property){
        Object propertyValue = AnyUtils.convertAny(property.value);
        if(propertyValue instanceof Any[]){
            return new RedhawkStructSequence(getOrb(), getIor(), property.id, (Any[]) propertyValue);
        } else if(propertyValue instanceof DataType[]) {
            return new RedhawkStruct(getOrb(), getIor(), property.id, (DataType[]) propertyValue, null);
        } else if(propertyValue instanceof Object[]){
            return new RedhawkSimpleSequence(getOrb(), getIor(),  property.id, (Object[]) propertyValue, TypeCode.originalType(property.value.type()));
        } else {
            return new RedhawkSimple(getOrb(), getIor(),  property.id, propertyValue);
        }
    }
    
    
}