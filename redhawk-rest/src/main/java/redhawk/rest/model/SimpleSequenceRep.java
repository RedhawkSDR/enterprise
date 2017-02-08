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
import javax.xml.bind.annotation.XmlSeeAlso;

import redhawk.driver.xml.model.sca.prf.Action;
import redhawk.driver.xml.model.sca.prf.Enumerations;
import redhawk.driver.xml.model.sca.prf.IsCommandLine;
import redhawk.driver.xml.model.sca.prf.IsComplex;
import redhawk.driver.xml.model.sca.prf.IsOptional;
import redhawk.driver.xml.model.sca.prf.Kind;
import redhawk.driver.xml.model.sca.prf.PropertyValueType;
import redhawk.driver.xml.model.sca.prf.Range;



@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SimpleSequenceRep extends Property {

    private static Logger logger = Logger.getLogger(SimpleSequenceRep.class.getName());

    protected String units;
    protected Range range;
    protected List<Kind> kinds;
    protected Action action;
    protected IsComplex complex;
    protected IsOptional optional;
    protected PropertyValueType propertyValueType;
    protected String dataType;
    
    protected List<Object> values;
    
	public String getUnits() {
		return units;
	}
	public void setUnits(String units) {
		this.units = units;
	}
	public Range getRange() {
		return range;
	}
	public void setRange(Range range) {
		this.range = range;
	}
	public List<Kind> getKinds() {
		return kinds;
	}
	public void setKinds(List<Kind> kinds) {
		this.kinds = kinds;
	}
	public Action getAction() {
		return action;
	}
	public void setAction(Action action) {
		this.action = action;
	}
	public IsComplex getComplex() {
		return complex;
	}
	public void setComplex(IsComplex complex) {
		this.complex = complex;
	}
	public IsOptional getOptional() {
		return optional;
	}
	public void setOptional(IsOptional optional) {
		this.optional = optional;
	}
	public PropertyValueType getPropertyValueType() {
		return propertyValueType;
	}
	public void setPropertyValueType(PropertyValueType propertyValueType) {
		this.propertyValueType = propertyValueType;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public List<Object> getValues() {
		return values;
	}
	public void setValues(List<Object> values) {
		this.values = values;
	}
    
    
    
    
}

