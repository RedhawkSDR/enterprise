package redhawk.websocket.model;


/**
 * POJO for representing DomainManagementObject.*EventType
 */
public class DomainManagementModel {
	private String producerId; 
	
	private String sourceId; 
	
	private String sourceIOR;
	
	private SourceCategory sourceCategory;
	
	private String sourceName;
	
	private DomainManagementAction action; 
	
	public DomainManagementModel(){
		
	}

	public DomainManagementAction getAction() {
		return action;
	}

	public void setAction(DomainManagementAction action) {
		this.action = action;
	}

	public String getProducerId() {
		return producerId;
	}

	public void setProducerId(String producerId) {
		this.producerId = producerId;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getSourceIOR() {
		return sourceIOR;
	}

	public void setSourceIOR(String sourceIOR) {
		this.sourceIOR = sourceIOR;
	}

	public SourceCategory getSourceCategory() {
		return sourceCategory;
	}

	public void setSourceCategory(SourceCategory sourceCategory) {
		this.sourceCategory = sourceCategory;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	
	
	
}
