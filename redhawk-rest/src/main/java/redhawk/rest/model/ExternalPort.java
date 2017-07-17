package redhawk.rest.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)//TODO: Upgrade this to @JsonInclude(Include.NON_NULL)
public class ExternalPort extends Port{
	private String externalname; 
	
	private String componentRefId;
	
	private String description;
	
	public ExternalPort(){}
	
	public ExternalPort(Port port){
		//See port properties
		this.setName(port.getName());
		this.setType(port.getType());
		this.setRepId(port.getRepId());
		this.setConnectionIds(port.getConnectionIds());
		this.setState(port.getState());
	}
	
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
}
