package redhawk.rest.model;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FileInformationType {

	private String name 				= null;
	private String location				= "";
	private String kind 				= null;
	private long size 					= 0l;
	private List<SimpleRep> properties		= null;
	
	  
	  
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	
	public long getSize() {
		return size;
	}
	
	public void setSize(long size) {
		this.size = size;
	}
	
	public List<SimpleRep> getProperties() {
		return properties;
	}
	
	public void setProperties(List<SimpleRep> properties) {
		this.properties = properties;
	}

	  
	  
}
