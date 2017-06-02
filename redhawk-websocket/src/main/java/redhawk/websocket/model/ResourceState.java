package redhawk.websocket.model;

public enum ResourceState {
	/**
	 * Resource is stopped
	 */
	STOPPED(0),
	
	/**
	 * Resource is started
	 */
	STARTED(1);
	
	private Integer numRep; 
		
	ResourceState(Integer num){
		this.numRep = num;
	}
	
	public static ResourceState getValue(Integer num){
		if(num.equals(1)){
			return STARTED;
		}else{
			return STOPPED;
		}
	}

	public Integer getNumRep() {
		return numRep;
	}
}
