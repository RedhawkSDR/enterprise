package redhawk.rest.model;

import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import redhawk.driver.xml.model.sca.prf.AccessType;



@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(value={SimpleRep.class})
public class Property {

    private static Logger logger = Logger.getLogger(Property.class.getName());

    protected String type;
    protected String description;
    protected String id;
    protected AccessType mode;
    protected String name;

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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}

