package redhawk.driver.port;

import java.util.HashMap;
import java.util.Map;

public enum PortState {
	IDLE(0),
	
	ACTIVE(1),
	
	BUSY(2);
	
	private Integer value;
	
	private static final Map<Integer, PortState> REDHAWKPORTSTATES = new HashMap<>();
	
	static{
		for(PortState value : values()){
			REDHAWKPORTSTATES.put(value.getValue(), value);
		}
	}
	
	PortState(Integer i){
		this.value = i;
	}
	
	public Integer getValue(){
		return value;
	}
	
	public static PortState reverseLookup(Integer value){
		PortState state = REDHAWKPORTSTATES.get(value);
		if(state==null){
			throw new IllegalArgumentException("Unknown Port state "+value);
		}
		
		return state;
	}
}

