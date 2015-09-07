package redbus.core.orchestration.components.redhawk.dataxml;


import java.util.Hashtable;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.Servant;
import org.omg.PortableServer.POAPackage.ObjectNotActive;
import org.omg.PortableServer.POAPackage.ServantAlreadyActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

import CF.PortSupplierPOA;
import CF.LifeCyclePackage.InitializeError;
import CF.PortSupplierPackage.UnknownPort;

public class PortSupplierImpl implements RedhawkOperations {

    private static Log logger = LogFactory.getLog(PortSupplierImpl.class);
    
    protected org.omg.CORBA.ORB orb = null;
    /**
     * The POA object that this object should use for any CORBA objects it
     * creates
     */
    protected POA poa = null;
    /** Map of input ports for this resource */
    protected Hashtable<String, Object> portObjects;
    /** Map of input ports for this resource */
    protected Hashtable<String, Servant> portServants;
    /** Map of input ports for this resource */
    protected Hashtable<String, org.omg.CORBA.Object> ports;

    protected CF.PortSupplier portSupplier;

    private DataXmlOutputImpl port_dataXML_out;

    
    
    public void addPort(String name, Object object) {
        this.portObjects.put(name, object);
        this.portServants.put(name, (Servant)object);
    }

    
    public PortSupplierImpl(POA rootpoa) {
        this.ports = new Hashtable<String, org.omg.CORBA.Object>();
        this.portServants = new Hashtable<String, Servant>();
        this.portObjects = new Hashtable<String, Object>();
        
        this.poa = rootpoa;
        
        // Uses/output
        this.port_dataXML_out = new DataXmlOutputImpl("dataXML_out");
        this.addPort("dataXML_out", port_dataXML_out);
    }
    
    
    /**
     * {@inheritDoc}
     */
    public void initialize() throws InitializeError {

        this.ports.clear();
        for (Map.Entry<String, Servant> me : this.portServants.entrySet()) {
            org.omg.CORBA.Object obj = activateObject(me.getValue());
            this.ports.put(me.getKey(), obj);
        }
    }

    public Object getPortObject(final String name) throws UnknownPort {
        // the mapping of ports assumes that port names are unique to the
        // component
        // the Ports_var maps are kept different (they could be made into one)
        // because it's less confusing this way

        Object p = this.portObjects.get(name);
        if (p != null) {
            return p;
        }

        throw new UnknownPort("Unknown port: " + name);
    }

    public org.omg.CORBA.Object getPort(final String name) throws UnknownPort {
        // the mapping of ports assumes that port names are unique to the
        // component
        // the Ports_var maps are kept different (they could be made into one)
        // because it's less confusing this way

        logger.info(this.ports);
        
        
        org.omg.CORBA.Object p = this.ports.get(name);
        if (p != null) {
            return p;
        }

        throw new UnknownPort("Unknown port: " + name);
    }

    /**
     * This returns the CORBA ORB used by the component
     * 
     * @return the ORB in use
     */
    protected org.omg.CORBA.ORB getOrb() {
        return this.orb;
    }

    /**
     * This returns the RootPOA manager for the ORB in use
     * 
     * @return the POA manager
     */
    protected POA getPoa() {
        return this.poa;
    }

    /**
     * This function explicitly activates the given Servant.
     * 
     * @param s the Servant to activate
     * @return the activated CORBA Object
     */
    protected org.omg.CORBA.Object activateObject(final Servant s) {
        try {
            final byte[] oid = this.poa.activate_object(s);
            return this.poa.id_to_reference(oid);
        } catch (final ServantAlreadyActive e) {
            // PASS
        } catch (final WrongPolicy e) {
            // PASS
        } catch (final ObjectNotActive e) {
            // PASS
        }
        return null;
    }


    public DataXmlOutputImpl getPort_dataXML_out() {
        return port_dataXML_out;
    }
    
}
