package redhawk.driver.device;

import java.util.HashMap;
import java.util.Map;

public enum UsageState {
	IDLE(0),
	
	ACTIVE(1),
	
	BUSY(2);
	
	private Integer value; 

	private static final Map<Integer, UsageState> USAGESTATES = new HashMap<>();
	
	static {
		for(UsageState value : values()) {
			USAGESTATES.put(value.getValue(), value);
		}
	}
	
	UsageState(Integer value){
		this.value = value;
	}
	
	public Integer getValue(){
		return value;
	}
	
	public static UsageState reverseLookup(Integer value) {
		UsageState state = USAGESTATES.get(value);
		
		if(state==null) {
			throw new IllegalArgumentException("Unknown Usage State "+value);
		}
		
		return state;
	}
}
