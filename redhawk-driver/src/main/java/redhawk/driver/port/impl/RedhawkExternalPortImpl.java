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
	private String externalName;
	
	private RedhawkPortImpl port;
	
	private String componentReferenceId; 
	
	public RedhawkExternalPortImpl(RedhawkPortImpl impl, String description, String externalName){
		this.port = impl; 
		this.description = description;
		this.externalName = externalName;
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
	public String getExternalName() {
		return externalName;
	}

	public void setExternalName(String externalName) {
		this.externalName = externalName;
	}

	@Override
	public void connect(PortListener<?> portListener) throws Exception {		
		port.connect(portListener);
	}

	@Override
	public void disconnect() throws InvalidPort, PortException {
		port.disconnect();
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
		return port.getName();
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

	public RedhawkExternalPortImpl(RedhawkPortImpl impl){
		this.port = impl;
	}
	
	@Override
	public String toString() {
		return "RedhawkExternalPortImpl [description=" + description + ", externalName=" + externalName + ", port="
				+ port + "]";
	}
}
