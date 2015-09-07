package redhawk.rest.model;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="waveforms")
@XmlAccessorType(XmlAccessType.FIELD)
public class WaveformContainer {

    @XmlElement(name="waveforms")
    private List<WaveformInfo> waveforms;

    public WaveformContainer(){    	
    }
    
    public WaveformContainer(List<WaveformInfo> waveforms){
    	this.waveforms = waveforms;
    }
    
	public List<WaveformInfo> getDomains() {
		return waveforms;
	}

	public void setDomains(List<WaveformInfo> waveforms) {
		this.waveforms = waveforms;
	}

    
}