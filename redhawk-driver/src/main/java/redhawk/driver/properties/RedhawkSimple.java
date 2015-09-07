package redhawk.driver.properties;

import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;

public class RedhawkSimple extends RedhawkProperty {

    private String simpleId;
    private Object value; 
    
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
