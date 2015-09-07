package redhawk.rest.model;

import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import redhawk.driver.xml.model.sca.prf.AccessType;
import redhawk.driver.xml.model.sca.prf.Action;
import redhawk.driver.xml.model.sca.prf.ConfigurationKind;
import redhawk.driver.xml.model.sca.prf.Enumerations;
import redhawk.driver.xml.model.sca.prf.IsCommandLine;
import redhawk.driver.xml.model.sca.prf.IsComplex;
import redhawk.driver.xml.model.sca.prf.IsOptional;
import redhawk.driver.xml.model.sca.prf.Kind;
import redhawk.driver.xml.model.sca.prf.PropertyValueType;
import redhawk.driver.xml.model.sca.prf.Range;



@XmlRootElement(name="property")
@XmlAccessorType(XmlAccessType.FIELD)
public class FullProperty {

    private static Logger logger = Logger.getLogger(FullProperty.class.getName());

    protected String type;
    protected String description;
    protected String id;
    protected AccessType mode;
    protected String name;
    protected String value;
    protected String units;
    protected Range range;
    protected Enumerations enumerations;
    protected List<Kind> kinds;
    protected Action action;
    protected IsComplex complex;
    protected IsCommandLine commandline;
    protected IsOptional optional;
    protected PropertyValueType propertyValueType;
    protected String dataType;
    protected List<Object> values;
    protected List<ConfigurationKind> configurationKinds;
    protected List<FullProperty> attributes;
    protected List<FullProperty> structs;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
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
	public Enumerations getEnumerations() {
		return enumerations;
	}
	public void setEnumerations(Enumerations enumerations) {
		this.enumerations = enumerations;
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
	public IsCommandLine getCommandline() {
		return commandline;
	}
	public void setCommandline(IsCommandLine commandline) {
		this.commandline = commandline;
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
	public List<ConfigurationKind> getConfigurationKinds() {
		return configurationKinds;
	}
	public void setConfigurationKinds(List<ConfigurationKind> configurationKinds) {
		this.configurationKinds = configurationKinds;
	}
	public List<FullProperty> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<FullProperty> attributes) {
		this.attributes = attributes;
	}
	public List<FullProperty> getStructs() {
		return structs;
	}
	public void setStructs(List<FullProperty> structs) {
		this.structs = structs;
	}
    
    
    
    
	
}

