package redhawk.websocket.model;

public class ResourceStateChangeModel {
	private String sourceId; 
	
	private String sourceName;
	
	private ResourceState stateChangedFrom; 
	
	private ResourceState stateChangedTo;
	
	public ResourceStateChangeModel(){
		
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public ResourceState getStateChangedFrom() {
		return stateChangedFrom;
	}

	public void setStateChangedFrom(ResourceState stateChangedFrom) {
		this.stateChangedFrom = stateChangedFrom;
	}

	public ResourceState getStateChangedTo() {
		return stateChangedTo;
	}

	public void setStateChangedTo(ResourceState stateChangedTo) {
		this.stateChangedTo = stateChangedTo;
	}
	
	
}
