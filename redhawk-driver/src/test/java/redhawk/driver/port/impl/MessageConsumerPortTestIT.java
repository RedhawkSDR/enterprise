package redhawk.driver.port.impl;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
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
import redhawk.RedhawkTestBase;
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

public class MessageConsumerPortTestIT extends RedhawkTestBase{	
	private RedhawkFileSystem rhFS;
	
	private RedhawkApplication rhApplication;
	
	@Before
	public void setup() throws ConnectionException, MultipleResourceException, CORBAException, FileNotFoundException, IOException, ApplicationCreationException, ApplicationStartException{
		//TODO: Make this a helper method
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
		rhFS = driver.getDomain().getFileManager();
		
		//Deploy example component
		this.writeJavaComponentToCF("src/test/resources/components/MessageProducer", rhFS);	
		
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
	
	/*
	 * @Test
	public void testSteveConsumer() throws InvalidName, ResourceNotFoundException, MultipleResourceException, CORBAException, ServantAlreadyActive, WrongPolicy, AdapterInactive{
		MessageConsumerPort msgConsumerPort = new MessageConsumerPort("tnav-consumer");
		msgConsumerPort.registerMessage("test_msg", test_msg_struct.class, new SteveMsgHandler());	
		POA rootPOA = POAHelper.narrow(driver.getOrb().resolve_initial_references("RootPOA"));	
		
		rootPOA.the_POAManager().activate();
		rootPOA.activate_object(msgConsumerPort);
		
		RedhawkPort port = driver.getDomain().getApplicationByName("MSPw.*").getComponentByName("MSP_1.*").getPort("msg_out");
		
		try{
			CF.Port messageConsumerPort = CF.PortHelper.narrow(port.getCorbaObject());
			messageConsumerPort.connectPort(msgConsumerPort._this(), "test-message-processing");

			Thread.sleep(30000l);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public class SteveMsgHandler implements MessageListener<test_msg_struct>{
		public SteveMsgHandler(){}
		
		@Override
		public void messageReceived(String messageId, test_msg_struct messageData) {
			// TODO Auto-generated method stub
			logger.info("messageReceived called: (jacorb callback) " + messageId + " data " + messageData.get_msg());			
		}
	}
	
	*/
	
	public void writeJavaComponentToCF(String directory, RedhawkFileSystem rhFS) throws FileNotFoundException, IOException{
		//Write all the xml files at the root level to CF
		File directoryObj = new File(directory);
		
		//Find all xml files
		File[] xmlFiles = directoryObj.listFiles((d, name) -> name.endsWith(".xml"));
		String rootPath = null;
		
		//Write all the .xml files
		for(File file : xmlFiles){
			String fileName = file.getName();
			String absoluteParentDir = file.getParent();
			rootPath = absoluteParentDir.substring(absoluteParentDir.lastIndexOf('/')+1, absoluteParentDir.length());
			
			String destFile = "/components/"+rootPath+"/"+fileName;
			rhFS.writeFile(new FileInputStream(file), destFile);
		}
		
		/*
		 * Write the jar and startJava.sh to it's appropriate location
		 */
		String[] extensions = new String[]{"jar", "sh"};
		
		Collection<File> moreFiles = FileUtils.listFiles(new File(directory), extensions, true);
		
		for(File file : moreFiles){
			String destFile = "/components/"+file.getAbsolutePath().substring(file.getAbsolutePath().indexOf(rootPath));
			
			//Ignore build.sh 
			if(!destFile.endsWith("build.sh")){
				rhFS.writeFile(new FileInputStream(file), destFile);
			}
		}
	}
	
	@After
	public void cleanup() throws IOException, ApplicationReleaseException{
		if(rhApplication!=null)
			rhApplication.release();
		
		rhFS.removeDirectory("/waveforms/MPWaveform");
		rhFS.removeDirectory("/components/MessageProducer");
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
