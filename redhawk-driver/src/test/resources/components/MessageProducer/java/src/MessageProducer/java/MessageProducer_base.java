package MessageProducer.java;


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
 * Source: MessageProducer.spd.xml
 *
 * @generated
 */

public abstract class MessageProducer_base extends Component {
    /**
     * @generated
     */
    public final static Logger logger = Logger.getLogger(MessageProducer_base.class.getName());

    /**
     * The property myMessage
     * My message that I'm outputting
     *
     * @generated
     */
    /**
     * The structure for property myMessage
     * 
     * @generated
     */
    public static class myMessage_struct extends StructDef {
        public final StringProperty text =
            new StringProperty(
                "text", //id
                null, //name
                null, //default value
                Mode.READWRITE, //mode
                Action.EXTERNAL, //action
                new Kind[] {Kind.CONFIGURE}
                );
        public final LongLongProperty timestamp =
            new LongLongProperty(
                "timestamp", //id
                null, //name
                null, //default value
                Mode.READWRITE, //mode
                Action.EXTERNAL, //action
                new Kind[] {Kind.PROPERTY}
                );
    
        /**
         * @generated
         */
        public myMessage_struct(String text, Long timestamp) {
            this();
            this.text.setValue(text);
            this.timestamp.setValue(timestamp);
        }
    
        /**
         * @generated
         */
        public void set_text(String text) {
            this.text.setValue(text);
        }
        public String get_text() {
            return this.text.getValue();
        }
        public void set_timestamp(Long timestamp) {
            this.timestamp.setValue(timestamp);
        }
        public Long get_timestamp() {
            return this.timestamp.getValue();
        }
    
        /**
         * @generated
         */
        public myMessage_struct() {
            addElement(this.text);
            addElement(this.timestamp);
        }
    
        public String getId() {
            return "myMessage";
        }
    };
    
    public final StructProperty<myMessage_struct> myMessage =
        new StructProperty<myMessage_struct>(
            "myMessage", //id
            null, //name
            myMessage_struct.class, //type
            new myMessage_struct(), //default value
            Mode.READWRITE, //mode
            new Kind[] {Kind.MESSAGE} //kind
            );
    
    // Uses/outputs
    /**
     * If the meaning of this port isn't clear, a description should be added.
     *
     * @generated
     */
    public MessageSupplierPort port_message_out;

    /**
     * @generated
     */
    public MessageProducer_base()
    {
        super();

        setLogger( logger, MessageProducer_base.class.getName() );


        // Properties
        addProperty(myMessage);


        // Uses/outputs
        this.port_message_out = new MessageSupplierPort("message_out");
        this.addPort("message_out", this.port_message_out);
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
        MessageProducer.configureOrb(orbProps);

        try {
            Component.start_component(MessageProducer.class, args, orbProps);
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
