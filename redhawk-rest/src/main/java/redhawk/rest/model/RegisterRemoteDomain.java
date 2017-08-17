package redhawk.rest.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RegisterRemoteDomain {
	private String domainName; 
	
	private Integer nameServerPort;
	
	private String nameServerHost;
	
	public RegisterRemoteDomain() {	
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public Integer getNameServerPort() {
		return nameServerPort;
	}

	public void setNameServerPort(Integer nameServerPort) {
		this.nameServerPort = nameServerPort;
	}

	public String getNameServerHost() {
		return nameServerHost;
	}

	public void setNameServerHost(String nameServerHost) {
		this.nameServerHost = nameServerHost;
	}
	
	
}
