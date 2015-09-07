package redhawk.rest.model;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="devices")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeviceContainer {

    @XmlElement(name="device")
    private List<Device> devices;

    public DeviceContainer(){    	
    }
    
    public DeviceContainer(List<Device> devices){
    	this.devices = devices;
    }
    
	public List<Device> getDevices() {
		return devices;
	}

	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}

    
}