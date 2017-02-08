/*
 * This file is protected by Copyright. Please refer to the COPYRIGHT file
 * distributed with this source distribution.
 *
 * This file is part of REDHAWK __REDHAWK_PROJECT__.
 *
 * REDHAWK __REDHAWK_PROJECT__ is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * REDHAWK __REDHAWK_PROJECT__ is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
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