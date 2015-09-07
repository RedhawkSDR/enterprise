package redhawk.rest.model;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="devicemanagers")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeviceManagerContainer {

    @XmlElement(name="devicemanager")
    private List<DeviceManager> devicemanagers;

    public DeviceManagerContainer(){    	
    }
    
    public DeviceManagerContainer(List<DeviceManager> devicemanagers){
    	this.devicemanagers = devicemanagers;
    }
    
	public List<DeviceManager> getDeviceManagers() {
		return devicemanagers;
	}

	public void setDeviceManagers(List<DeviceManager> devicemanagers) {
		this.devicemanagers = devicemanagers;
	}

    
}