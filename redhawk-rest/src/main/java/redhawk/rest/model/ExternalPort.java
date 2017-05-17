package redhawk.rest.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)//TODO: Upgrade this to @JsonInclude(Include.NON_NULL)
public class ExternalPort{ //TODO: Just extend port
	private String externalname; 
	
	private String componentRefId;
	
	private String description;

	private String name;
	private String type;
	private String repId;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getExternalname() {
		return externalname;
	}

	public void setExternalname(String externalname) {
		this.externalname = externalname;
	}

	public String getComponentRefId() {
		return componentRefId;
	}

	public void setComponentRefId(String componentRefId) {
		this.componentRefId = componentRefId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRepId() {
		return repId;
	}
	public void setRepId(String repId) {
		this.repId = repId;
	}
}
