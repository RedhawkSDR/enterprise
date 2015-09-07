package redhawk.driver.eventchannel.listeners;

import java.util.Map;

import org.omg.CORBA.Any;

public class PropertyChange {

	private String sourceId;
	private String sourceName;
	private Map<String, Object> properties;
	private Any corbaAny;
	
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
	public void setCorbaAny(Any corbaAny) {
		this.corbaAny = corbaAny;
	}
	public Any getCorbaAny(){
		return corbaAny;
	}
	
}
