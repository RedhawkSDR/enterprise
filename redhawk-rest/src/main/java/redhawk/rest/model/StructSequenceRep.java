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
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import redhawk.driver.xml.model.sca.prf.ConfigurationKind;



@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class StructSequenceRep extends Property {

    private static Logger logger = Logger.getLogger(StructSequenceRep.class.getName());

    protected List<ConfigurationKind> configurationKinds;
    protected List<Property> structs;
    
    
	public List<Property> getStructs() {
		return structs;
	}
	public void setStructs(List<Property> structs) {
		this.structs = structs;
	}
	public List<ConfigurationKind> getConfigurationKinds() {
		return configurationKinds;
	}
	public void setConfigurationKinds(List<ConfigurationKind> configurationKinds) {
		this.configurationKinds = configurationKinds;
	}
    
    
    
    
    
}

