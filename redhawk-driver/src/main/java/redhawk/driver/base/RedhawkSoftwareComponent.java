package redhawk.driver.base;

import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.xml.model.sca.prf.Properties;
import redhawk.driver.xml.model.sca.scd.Softwarecomponent;
import redhawk.driver.xml.model.sca.spd.Softpkg;

public interface RedhawkSoftwareComponent extends PortBackedObject{
	/**
	 * @return The Softwarecomponent for this object.
	 * @throws ResourceNotFoundException
	 */
	Softwarecomponent getSoftwareComponent() throws ResourceNotFoundException;

	/**
	 * @return The Software Package for the object.
	 * @throws ResourceNotFoundException
	 */
	Softpkg getComponentAssembly() throws ResourceNotFoundException;

	/**
	 * 
	 * @return Properties related to this object.
	 * @throws ResourceNotFoundException
	 */
	Properties getPropertyConfiguration() throws ResourceNotFoundException;	
}
