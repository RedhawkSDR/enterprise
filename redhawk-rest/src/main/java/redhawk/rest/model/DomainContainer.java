package redhawk.rest.model;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="domains")
@XmlAccessorType(XmlAccessType.FIELD)
public class DomainContainer {

    @XmlElement(name="domain")
    private List<Domain> domains;

    public DomainContainer(){    	
    }
    
    public DomainContainer(List<Domain> domains){
    	this.domains = domains;
    }
    
	public List<Domain> getDomains() {
		return domains;
	}

	public void setDomains(List<Domain> domains) {
		this.domains = domains;
	}

    
}