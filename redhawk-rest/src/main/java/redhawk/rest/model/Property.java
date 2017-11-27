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

import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import redhawk.driver.xml.model.sca.prf.AccessType;



@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(value={SimpleRep.class, SimpleSequenceRep.class, StructRep.class, StructSequenceRep.class})
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)//TODO: Upgrade this to @JsonInclude(Include.NON_NULL)
public class Property {

    private static Logger logger = Logger.getLogger(Property.class.getName());

    protected String type;
    protected String description;
    protected String id;
	protected String externalId;
    protected AccessType mode;
    protected String name;

	public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getExternalId() {
		return externalId;
	}
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public AccessType getMode() {
        return mode;
    }
    public void setMode(AccessType mode) {
        this.mode = mode;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}

