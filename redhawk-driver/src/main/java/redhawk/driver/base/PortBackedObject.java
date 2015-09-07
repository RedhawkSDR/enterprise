package redhawk.driver.base;

import java.util.List;
import java.util.Map;

import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.port.RedhawkPort;
import redhawk.driver.xml.model.sca.prf.Properties;
import redhawk.driver.xml.model.sca.scd.Softwarecomponent;
import redhawk.driver.xml.model.sca.spd.Softpkg;

public interface PortBackedObject extends QueryableResource {
	Map<String, RedhawkPort> ports() throws ResourceNotFoundException;
    List<RedhawkPort> getPorts() throws ResourceNotFoundException;
    RedhawkPort getPort(String portName) throws ResourceNotFoundException, MultipleResourceException;
	Softwarecomponent getSoftwareComponent() throws ResourceNotFoundException;
	Softpkg getComponentAssembly() throws ResourceNotFoundException;
	Properties getPropertyConfiguration() throws ResourceNotFoundException;
}
