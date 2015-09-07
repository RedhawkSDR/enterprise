package redhawk.driver.port;

import java.util.List;

import redhawk.driver.bulkio.Packet;
import redhawk.driver.exceptions.PortException;
import CF.PortPackage.InvalidPort;

public interface RedhawkPort {

	public static final String PORT_TYPE_USES = "uses";
	public static final String PORT_TYPE_PROVIDES = "provides";
	public static final String PORT_TYPE_BIDIRECTIONAL = "bidirectional";
	
    public void connect(PortListener<?> portListener) throws Exception;
    public void disconnect() throws InvalidPort, PortException;
    public <T> void send(Packet<T> packet) throws Exception;
    
    public List<RedhawkPortStatistics> getPortStatistics();
    public String getRepId();
    public String getType();
	public String getName();
	
	public org.omg.CORBA.Object getCorbaObject();

}
