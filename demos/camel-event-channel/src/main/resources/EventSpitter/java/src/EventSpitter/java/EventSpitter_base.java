package EventSpitter.java;

import java.util.Properties;

import org.apache.log4j.Logger;

import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.InvalidName;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;

import CF.InvalidObjectReference;

import org.ossie.component.*;
import org.ossie.properties.*;

import org.ossie.events.MessageSupplierPort;

/**
 * This is the component code. This file contains all the access points
 * you need to use to be able to access all input and output ports,
 * respond to incoming data, and perform general component housekeeping
 *
 * Source: EventSpitter.spd.xml
 *
 * @generated
 */

public abstract class EventSpitter_base extends Component {
    /**
     * @generated
     */
    public final static Logger logger = Logger.getLogger(EventSpitter_base.class.getName());

    /**
     * The property simpSeq
     * If the meaning of this property isn't clear, a description should be added.
     *
     * @generated
     */
    public final StringSequenceProperty simpSeq =
        new StringSequenceProperty(
            "simpSeq", //id
            null, //name
            StringSequenceProperty.asList("hello","there","bill","bobaggins"), //default value
            Mode.READWRITE, //mode
            Action.EXTERNAL, //action
            new Kind[] {Kind.PROPERTY}
            );
    
    /**
     * The property properties
     * If the meaning of this property isn't clear, a description should be added.
     *
     * @generated
     */
    /**
     * The structure for property properties
     * 
     * @generated
     */
    public static class properties_struct extends StructDef {
        public final StringProperty foo =
            new StringProperty(
                "foo", //id
                null, //name
                null, //default value
                Mode.READWRITE, //mode
                Action.EXTERNAL, //action
                new Kind[] {Kind.CONFIGURE}
                );
        public final FloatProperty bar =
            new FloatProperty(
                "bar", //id
                null, //name
                null, //default value
                Mode.READWRITE, //mode
                Action.EXTERNAL, //action
                new Kind[] {Kind.CONFIGURE}
                );
    
        /**
         * @generated
         */
        public properties_struct(String foo, Float bar) {
            this();
            this.foo.setValue(foo);
            this.bar.setValue(bar);
        }
    
        /**
         * @generated
         */
        public properties_struct() {
            addElement(this.foo);
            addElement(this.bar);
        }
    
        public String getId() {
            return "properties";
        }
    };
    
    public final StructProperty<properties_struct> properties =
        new StructProperty<properties_struct>(
            "properties", //id
            null, //name
            properties_struct.class, //type
            new properties_struct(), //default value
            Mode.READWRITE, //mode
            new Kind[] {Kind.PROPERTY} //kind
            );
    
    // Uses/outputs
    /**
     * If the meaning of this port isn't clear, a description should be added.
     *
     * @generated
     */
    public MessageSupplierPort port_messages;

    /**
     * @generated
     */
    public EventSpitter_base()
    {
        super();

        setLogger( logger, EventSpitter_base.class.getName() );


        // Properties
        addProperty(simpSeq);

        addProperty(properties);


        // Uses/outputs
        this.port_messages = new MessageSupplierPort("messages");
        this.addPort("messages", this.port_messages);
    }

    public void start() throws CF.ResourcePackage.StartError
    {
        super.start();
    }

    public void stop() throws CF.ResourcePackage.StopError
    {
        super.stop();
    }


    /**
     * The main function of your component.  If no args are provided, then the
     * CORBA object is not bound to an SCA Domain or NamingService and can
     * be run as a standard Java application.
     * 
     * @param args
     * @generated
     */
    public static void main(String[] args) 
    {
        final Properties orbProps = new Properties();
        EventSpitter.configureOrb(orbProps);

        try {
            Component.start_component(EventSpitter.class, args, orbProps);
        } catch (InvalidObjectReference e) {
            e.printStackTrace();
        } catch (NotFound e) {
            e.printStackTrace();
        } catch (CannotProceed e) {
            e.printStackTrace();
        } catch (InvalidName e) {
            e.printStackTrace();
        } catch (ServantNotActive e) {
            e.printStackTrace();
        } catch (WrongPolicy e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
