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
package redhawk.driver.base.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
import CF.PortPackage.InvalidPort;
import CF.PortPackage.OccupiedPort;
import CF.PortSupplierPackage.UnknownPort;
import redhawk.driver.base.PortBackedObject;
import redhawk.driver.base.RedhawkFileSystem;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.PortException;
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
    
	public void connect(PortBackedObject resource) throws PortException{
		//No Id passed create one 
		connect(resource, "rhdriver-"+UUID.randomUUID().toString());
	}
    
	public void connect(PortBackedObject resource, String connectionId) throws PortException {
    	try {
    		Boolean foundPortMatch = false;
    		RedhawkPort usesPort = null, providesPort = null;
    		
        	//Get Ports available to for this object 
    		for(RedhawkPort port : this.getPorts()) {
				String portRepId = port.getRepId();
				String portType = port.getType();
				
				for(RedhawkPort connectToPort : resource.getPorts()) {
					String connectPortId = connectToPort.getRepId();
					String connectPortType = connectToPort.getType();
					
					//If port types aren't equal and interface(repId) make a connection
					if(!connectPortType.equals(portType) && portRepId.equals(connectPortId)) {						
						//If first match found fill in variables for connect
						if(!foundPortMatch) {
							if(portType.equals(RedhawkPort.PORT_TYPE_USES)) {
								usesPort = port;
								providesPort = connectToPort;
							}else {
								usesPort = connectToPort;
								providesPort = port;
							}
							
							foundPortMatch = true;
						}else {
							throw new PortException("Multiple ports match with these components specify port names to match");
						}
					}
				}
			}
    		
    		//Connect the matched ports
    		if(usesPort!=null && providesPort!=null) {
        		usesPort.connect(providesPort);    			
    		}else {
    			throw new PortException("No matching ports between these two components");
    		}
    	} catch (ResourceNotFoundException e) {
			throw new PortException("Unable to connect ports", e);
		}
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
