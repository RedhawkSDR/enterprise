package redhawk.websocket.jsr356.model;

import java.util.Map;

/**
 * POJO for representing a REDHAWK PropertyChangeEvent. 
 *
 */
public class PropertyChangeModel {
	private String sourceId; 
	
	private String sourceName; 
	
	private Map<String, Object> properties; 
	
	public PropertyChangeModel(){
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

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
	
	@Override
	public String toString() {
		return "PropertyChangeModel [sourceId=" + sourceId + ", sourceName="
				+ sourceName + ", properties=" + properties + "]";
	}
}
