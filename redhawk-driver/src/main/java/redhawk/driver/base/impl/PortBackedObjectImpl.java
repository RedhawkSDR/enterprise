package redhawk.driver.base.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import org.omg.CORBA.ORB;
import org.xml.sax.SAXException;

import CF.ComponentType;
import CF.Resource;
import CF.ResourceHelper;
import CF.PortSupplierPackage.UnknownPort;
import redhawk.driver.base.PortBackedObject;
import redhawk.driver.base.RedhawkFileSystem;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.port.RedhawkPort;
import redhawk.driver.port.impl.RedhawkPortImpl;
import redhawk.driver.xml.ScaXmlProcessor;
import redhawk.driver.xml.model.sca.prf.Properties;
import redhawk.driver.xml.model.sca.scd.Provides;
import redhawk.driver.xml.model.sca.scd.Softwarecomponent;
import redhawk.driver.xml.model.sca.scd.Uses;
import redhawk.driver.xml.model.sca.spd.Softpkg;

public abstract class PortBackedObjectImpl<TParsedClass> extends QueryableResourceImpl<TParsedClass> implements PortBackedObject {

	private Logger logger = Logger.getLogger(PortBackedObjectImpl.class.getName());
	private RedhawkFileSystem fileSystem;
	
	protected PortBackedObjectImpl(String ior, ORB orb, RedhawkFileSystem fileSystem){
		super(ior, orb);
		this.fileSystem = fileSystem;
	}
	
    public Map<String, RedhawkPort> ports() throws ResourceNotFoundException {
    	return getPorts().stream().collect(Collectors.toMap(p -> p.getName(), Function.identity()));
    }
    
    public List<RedhawkPort> getPorts() throws ResourceNotFoundException {
    	logger.log(Level.FINE, "getPorts()");
    	List<RedhawkPort> rhPorts = new ArrayList<RedhawkPort>();
        
    	for(Object port : getSoftwareComponent().getComponentfeatures().getPorts().getProvidesAndUses()){
    		String name = (port instanceof Uses) ? ((Uses) port).getUsesname() : ((Provides) port).getProvidesname();
    		String repId = (port instanceof Uses) ? ((Uses) port).getRepid() : ((Provides) port).getRepid();
    		String type = (port instanceof Uses) ? RedhawkPort.PORT_TYPE_USES : RedhawkPort.PORT_TYPE_PROVIDES;
    		try {
    			rhPorts.add(new RedhawkPortImpl(getResource().getPort(name), getOrb(), repId, name, type));
    		} catch (UnknownPort e) {
    			logger.severe("IN HERE SEVERE");
			}
    	}
    	return rhPorts;
    }
    
    public RedhawkPort getPort(String portName) throws ResourceNotFoundException, MultipleResourceException {
        List<RedhawkPort> ports = getPorts().stream().filter(p -> p.getName().matches(portName)).collect(Collectors.toList());
        
        if(ports.size() > 1) {
        	throw new MultipleResourceException("Multiple Ports exist with the name: " + portName);
        } else if(ports.size() == 1) {
        	return ports.get(0);
        } else {
        	throw new ResourceNotFoundException("Unable to find the port with name: " + portName);
        }
    }
    
	public Softwarecomponent getSoftwareComponent() throws ResourceNotFoundException{
		try {
			String scdFileName = getComponentAssembly().getDescriptor().getLocalfile().getName();
			String componentDirPath = getSoftwareProfile().substring(0, getSoftwareProfile().lastIndexOf("/"));
			byte[] scdFileInBytes = fileSystem.getFile(componentDirPath+"/"+scdFileName);
			return unMarshall(scdFileInBytes, Softwarecomponent.class); 
		} catch(IOException e){
			throw new ResourceNotFoundException(e);
		}
	}
	
	public Softpkg getComponentAssembly() throws ResourceNotFoundException {
		try {
			byte[] spdFileInBytes = fileSystem.getFile(getSoftwareProfile());
			return unMarshall(spdFileInBytes, Softpkg.class);
		} catch(IOException e){
			throw new ResourceNotFoundException(e);
		}
	}
	
	public Properties getPropertyConfiguration() throws ResourceNotFoundException{
		try {
			String prfFileName = getComponentAssembly().getPropertyfile().getLocalfile().getName();
			String componentDirPath = getSoftwareProfile().substring(0, getSoftwareProfile().lastIndexOf("/"));
			byte[] prfFileInBytes = fileSystem.getFile(componentDirPath+"/"+prfFileName);
			return unMarshall(prfFileInBytes, Properties.class); 
		} catch(IOException e){
			throw new ResourceNotFoundException(e);
		}
	}
	
	private <T> T unMarshall(byte[] fileInBytes, Class clazz) throws IOException {
		try {
			return (T) ScaXmlProcessor.unmarshal(new ByteArrayInputStream(fileInBytes), clazz);
		} catch (JAXBException | SAXException e) {
			throw new IOException(e);
		} 
	}	
	
	private String getSoftwareProfile() {
		return getResource().softwareProfile();
	}
	
	private Resource getResource() {
		if(getCorbaObject() instanceof ComponentType) {
			return ResourceHelper.narrow(((ComponentType) getCorbaObject()).componentObject);
		} else {
			return ((Resource) getCorbaObject());
		}		
	}
	
	
}
