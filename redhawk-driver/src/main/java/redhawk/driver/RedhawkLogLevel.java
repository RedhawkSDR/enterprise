package redhawk.driver;

import java.util.HashMap;
import java.util.Map;

public enum RedhawkLogLevel {
	OFF(60000),
	
	FATAL(50000),
	
	ERROR(40000),
	
	WARN(30000),
	
	INFO(20000),
	
	DEBUG(10000),
	
	TRACE(5000),
	
	ALL(0);
	
	private Integer value;
	
	private static final Map<Integer, RedhawkLogLevel> REHDAWKLOGLEVELS = new HashMap<>();
	
	static {
		for(RedhawkLogLevel value : values()){
			REHDAWKLOGLEVELS.put(value.getValue(), value);
		}
	}
	
	RedhawkLogLevel(Integer t){
		this.value = t;
	}
	
	public Integer getValue(){
		return value;
	}
	
	public static RedhawkLogLevel reverseLookup(Integer value){
		RedhawkLogLevel level = REHDAWKLOGLEVELS.get(value);
		if(level==null){
			throw new IllegalArgumentException("Unknown log level "+value);
		}
		
		return level;
	}
}
