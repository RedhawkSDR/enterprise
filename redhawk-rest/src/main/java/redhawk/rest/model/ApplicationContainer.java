package redhawk.rest.model;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="applications")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicationContainer {

    @XmlElement(name="application")
    private List<Application> applications;

    public ApplicationContainer(){    	
    }
    
    public ApplicationContainer(List<Application> applications){
    	this.applications = applications;
    }
    
	public List<Application> getApplications() {
		return applications;
	}

	public void setApplications(List<Application> applications) {
		this.applications = applications;
	}

    
}