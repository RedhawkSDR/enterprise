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
package redhawk.driver.port.impl;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;
import org.omg.PortableServer.POAPackage.ServantAlreadyActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;
import org.ossie.events.MessageConsumerPort;
import org.ossie.events.MessageListener;
import org.ossie.properties.Action;
import org.ossie.properties.Kind;
import org.ossie.properties.LongLongProperty;
import org.ossie.properties.Mode;
import org.ossie.properties.StringProperty;
import org.ossie.properties.StructDef;

import CF.PortPackage.InvalidPort;
import CF.PortPackage.OccupiedPort;
import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.base.RedhawkFileSystem;
import redhawk.driver.exceptions.ApplicationCreationException;
import redhawk.driver.exceptions.ApplicationReleaseException;
import redhawk.driver.exceptions.ApplicationStartException;
import redhawk.driver.exceptions.CORBAException;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.port.RedhawkPort;
import redhawk.testutils.RedhawkTestBase;

/*
 * User running this test must be in the REDHAWK group in order for this to work. 
 * Possibly add in a check for that. 
 * 
 * sudo /usr/sbin/usermod -a -G redhawk <user name>
 */
public class MessageConsumerPortIT extends RedhawkTestBase{	
	private RedhawkFileSystem rhFS;
	
	private RedhawkApplication rhApplication;
	
	@Before
	public void setup() throws ConnectionException, MultipleResourceException, CORBAException, FileNotFoundException, IOException, ApplicationCreationException, ApplicationStartException, InterruptedException{
		//TODO: Detect whether component is already installed if not then install it. Run 'build.sh install' install
		//Use FileSystem to create the appropriate directory structure for your components. 
		/*
		 * Java Component
		 * CompName
		 * |-prf.xml
		 * |-scd.xml
		 * |-spd.xml
		 * |java/
		 * |- .jar
		 * |- startJava.sh
		 */
		/*rhFS = driver.getDomain().getFileManager();
		
		//Run build.sh so component can have necessary files
		RedhawkTestUtils.runCommand("src/test/resources/components/MessageProducer/", "build.sh");

		//Deploy example component
		RedhawkTestUtils.writeJavaComponentToCF("src/test/resources/components/MessageProducer", rhFS);	
		*/
		rhFS = driver.getDomain().getFileManager();
		
		//Deploy application
		rhApplication = driver.getDomain().createApplication("myMessageProducer", new File("src/test/resources/waveforms/MPWaveform/MPWaveform.sad.xml"));
		rhApplication.start();
	}
	
	@Test
	public void messageConsumerTest() throws InterruptedException, InvalidName, ResourceNotFoundException, AdapterInactive, ServantAlreadyActive, WrongPolicy{
		MessageConsumerPort msgConsumerPort = new MessageConsumerPort("message_out");
		InMsgHandler msgHandler = new InMsgHandler();
		
		//Appropriate Message Id is important!!!!!
		msgConsumerPort.registerMessage("myMessage", myMessage_struct.class, msgHandler);	
		POA rootPOA = POAHelper.narrow(driver.getOrb().resolve_initial_references("RootPOA"));
		
		rootPOA.the_POAManager().activate();
		rootPOA.activate_object(msgConsumerPort);
		
		RedhawkPort messageConsumer = rhApplication.getComponents().get(0).getPorts().get(0);
		
		CF.Port messageConsumerPort = CF.PortHelper.narrow(messageConsumer.getCorbaObject());
	
		try {
			messageConsumerPort.connectPort(msgConsumerPort._this(), "test-message-processing");
			
			Thread.sleep(5000l);			
			assertTrue(msgHandler.getMessageCount()>0);
		} catch (InvalidPort e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OccupiedPort e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@After
	public void cleanup() throws IOException, ApplicationReleaseException, InterruptedException{
		if(rhApplication!=null)
			rhApplication.release();
		
		rhFS.removeDirectory("/waveforms/MPWaveform");
		
		//TODO: Clean up only if necessary
		//rhFS.removeDirectory("/components/MessageProducer");
		
		/*
		 * Clean up component dir
		 */
		//RedhawkTestUtils.runCommand("src/test/resources/components/MessageProducer/java", "make distclean");
	}
	
	public class InMsgHandler implements MessageListener<myMessage_struct> {
		private Integer messageCount; 
		
		InMsgHandler(){
			logger.info("Message handler created");
			messageCount=0;
		}
		
		@Override
		public void messageReceived(String messageId, myMessage_struct messageData) {
			logger.info("Able to receive message "+messageId+" data "+messageData);
			messageCount++;
		}
		
		public Integer getMessageCount(){
			return messageCount;
		}
	}
	
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
    

}
