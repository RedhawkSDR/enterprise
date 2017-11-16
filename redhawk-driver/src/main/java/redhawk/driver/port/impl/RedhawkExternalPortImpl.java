package redhawk.driver.port.impl;

import java.util.List;

import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;

import CF.PortPackage.InvalidPort;
import redhawk.driver.bulkio.Packet;
import redhawk.driver.exceptions.PortException;
import redhawk.driver.port.PortListener;
import redhawk.driver.port.RedhawkPort;
import redhawk.driver.port.RedhawkPortStatistics;

/**
 *  
 *
 */
public class RedhawkExternalPortImpl implements RedhawkPort {
	/**
	 * Description for the external port. 
	 */
	private String description; 
	
	/**
	 * Name for the External port
	 */
	private String name;
	
	private RedhawkPortImpl port;
	
	private String componentReferenceId; 
	
	public RedhawkExternalPortImpl(RedhawkPortImpl impl){
		this.port = impl;
	}
	
	public RedhawkExternalPortImpl(RedhawkPortImpl impl, String description, String externalName, String componentRefId){
		this.port = impl; 
		this.description = description;
		this.name = externalName;
		this.componentReferenceId = componentRefId;
	}
	
	/**
	 * Description of External port
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 *
	 * @return
	 */
	public String getInternalName() {
		return port.getName();
	}

	@Override
	public void connect(PortListener<?> portListener) throws Exception {		
		port.connect(portListener);
	}

	@Override
	public void disconnect() throws PortException {
		port.disconnect();
	}
	
	@Override
	public void disconnect(String connectionId) throws PortException{
		port.disconnect(connectionId);
	}

	@Override
	public <T> void send(Packet<T> packet) throws Exception {
		port.send(packet);
	}

	@Override
	public List<RedhawkPortStatistics> getPortStatistics() {
		return port.getPortStatistics();
	}

	@Override
	public String getRepId() {
		return port.getRepId();
	}

	@Override
	public String getType() {
		return port.getType();
	}

	@Override
	public String getName() {
		return name;
	}
	
	public void setName(String externalName) {
		this.name = externalName;
	}

	@Override
	public Object getCorbaObject() {
		return port.getCorbaObject();
	}

	public RedhawkPortImpl getPort() {
		return port;
	}

	public void setPort(RedhawkPortImpl port) {
		this.port = port;
	}

	public String getComponentReferenceId() {
		return componentReferenceId;
	}

	public void setComponentReferenceId(String componentReferenceId) {
		this.componentReferenceId = componentReferenceId;
	}
	
	@Override
	public String toString() {
		return "RedhawkExternalPortImpl [description=" + description + ", externalName=" + name + ", port="
				+ port + "]";
	}

	@Override
	public void listen(PortListener<?> portListener) throws Exception {
		port.connect(portListener);
	}
}
