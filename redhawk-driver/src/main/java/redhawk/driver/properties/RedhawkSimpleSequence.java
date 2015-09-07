package redhawk.driver.properties;

import java.util.ArrayList;
import java.util.List;

import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;
import org.omg.CORBA.TypeCode;
import org.ossie.properties.AnyUtils;

public class RedhawkSimpleSequence extends RedhawkProperty {

    private String simpleSeqId;
    private List<Object> values; 
    private TypeCode tcKind;
    
    public RedhawkSimpleSequence(ORB orb, String parentObject, String simpleSeqId, Object[] values, TypeCode typeCode){
        this.orb = orb;
        this.parentObject = parentObject;
        this.simpleSeqId = simpleSeqId;
        this.tcKind = typeCode;
        
        this.values = new ArrayList<Object>();
        for(Object obj : values){
        	this.values.add(obj);
        }
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
        for(Object obj : values){
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
    
    
}
