package redhawk.driver.connectionmanager.impl;

public abstract class RedhawkEndpoint {

	private EndpointType type;
			
	private String resourceId;
		
	public RedhawkEndpoint(EndpointType type, String resourceId){
		this.type = type;
		this.resourceId = resourceId;
	}

	public EndpointType getType() {
		return type;
	}

	public void setType(EndpointType type) {
		this.type = type;
	}
	
	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
}
