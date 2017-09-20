package allPropsComponent.java;


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


/**
 * This is the component code. This file contains all the access points
 * you need to use to be able to access all input and output ports,
 * respond to incoming data, and perform general component housekeeping
 *
 * Source: AllPropsComponent.spd.xml
 *
 * @generated
 */

public abstract class AllPropsComponent_base extends Component {
    /**
     * @generated
     */
    public final static Logger logger = Logger.getLogger(AllPropsComponent_base.class.getName());

    /**
     * The property hello
     * If the meaning of this property isn't clear, a description should be added.
     *
     * @generated
     */
    public final StringProperty hello =
        new StringProperty(
            "hello", //id
            null, //name
            "World", //default value
            Mode.READWRITE, //mode
            Action.EXTERNAL, //action
            new Kind[] {Kind.PROPERTY}
            );
    
    /**
     * The property examples
     * If the meaning of this property isn't clear, a description should be added.
     *
     * @generated
     */
    public final StringSequenceProperty examples =
        new StringSequenceProperty(
            "examples", //id
            null, //name
            StringSequenceProperty.asList("Foo","Bar","Hello","World"), //default value
            Mode.READWRITE, //mode
            Action.EXTERNAL, //action
            new Kind[] {Kind.PROPERTY}
            );
    
    /**
     * The property cartoon_character
     * If the meaning of this property isn't clear, a description should be added.
     *
     * @generated
     */
    /**
     * The structure for property cartoon_character
     * 
     * @generated
     */
    public static class cartoon_character_struct extends StructDef {
        public final DoubleProperty age =
            new DoubleProperty(
                "age", //id
                null, //name
                10.0, //default value
                Mode.READWRITE, //mode
                Action.EXTERNAL, //action
                new Kind[] {Kind.CONFIGURE}
                );
        public final StringProperty name =
            new StringProperty(
                "name", //id
                null, //name
                "SpongeBob", //default value
                Mode.READWRITE, //mode
                Action.EXTERNAL, //action
                new Kind[] {Kind.CONFIGURE}
                );
        public final StringProperty friend =
            new StringProperty(
                "friend", //id
                null, //name
                "Patrick", //default value
                Mode.READWRITE, //mode
                Action.EXTERNAL, //action
                new Kind[] {Kind.CONFIGURE}
                );
    
        /**
         * @generated
         */
        public cartoon_character_struct(Double age, String name, String friend) {
            this();
            this.age.setValue(age);
            this.name.setValue(name);
            this.friend.setValue(friend);
        }
    
        /**
         * @generated
         */
        public void set_age(Double age) {
            this.age.setValue(age);
        }
        public Double get_age() {
            return this.age.getValue();
        }
        public void set_name(String name) {
            this.name.setValue(name);
        }
        public String get_name() {
            return this.name.getValue();
        }
        public void set_friend(String friend) {
            this.friend.setValue(friend);
        }
        public String get_friend() {
            return this.friend.getValue();
        }
    
        /**
         * @generated
         */
        public cartoon_character_struct() {
            addElement(this.age);
            addElement(this.name);
            addElement(this.friend);
        }
    
        public String getId() {
            return "cartoon_character";
        }
    };
    
    public final StructProperty<cartoon_character_struct> cartoon_character =
        new StructProperty<cartoon_character_struct>(
            "cartoon_character", //id
            null, //name
            cartoon_character_struct.class, //type
            new cartoon_character_struct(), //default value
            Mode.READWRITE, //mode
            new Kind[] {Kind.PROPERTY} //kind
            );
    
    /**
     * The property main_characters
     * If the meaning of this property isn't clear, a description should be added.
     *
     * @generated
     */
    /**
     * The structure for property matrix
     * 
     * @generated
     */
    public static class matrix_struct extends StructDef {
        public final StringProperty actor_name =
            new StringProperty(
                "actor_name", //id
                null, //name
                "Keannu Reaves", //default value
                Mode.READWRITE, //mode
                Action.EXTERNAL, //action
                new Kind[] {Kind.CONFIGURE}
                );
    
        /**
         * @generated
         */
        public matrix_struct(String actor_name) {
            this();
            this.actor_name.setValue(actor_name);
        }
    
        /**
         * @generated
         */
        public void set_actor_name(String actor_name) {
            this.actor_name.setValue(actor_name);
        }
        public String get_actor_name() {
            return this.actor_name.getValue();
        }
    
        /**
         * @generated
         */
        public matrix_struct() {
            addElement(this.actor_name);
        }
    
        public String getId() {
            return "matrix";
        }
    };
    
    public final StructSequenceProperty<matrix_struct> main_characters =
        new StructSequenceProperty<matrix_struct> (
            "main_characters", //id
            null, //name
            matrix_struct.class, //type
            StructSequenceProperty.asList(
                new matrix_struct("Keannu Reaves"),
                            new matrix_struct("Laurence Fishburne"),
                            new matrix_struct("Carrie-Anne Moss")
                ), //defaultValue
            Mode.READWRITE, //mode
            new Kind[] { Kind.PROPERTY } //kind
        );
    
    /**
     * @generated
     */
    public AllPropsComponent_base()
    {
        super();

        setLogger( logger, AllPropsComponent_base.class.getName() );


        // Properties
        addProperty(hello);

        addProperty(examples);

        addProperty(cartoon_character);

        addProperty(main_characters);

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
        AllPropsComponent.configureOrb(orbProps);

        try {
            Component.start_component(AllPropsComponent.class, args, orbProps);
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
