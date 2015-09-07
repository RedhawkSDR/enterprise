package redhawk.driver.devicemanager.impl;

import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import CF.FileException;
import CF.FileOperations;
import CF.OctetSequenceHolder;
import CF.FilePackage.IOException;
import CF.FilePackage.InvalidFilePointer;

public class DcdFile implements FileOperations {

	private static Logger logger = Logger.getLogger(DcdFile.class.getName());
	private String domainName;
	private String identifier;

	public static final String NL = "\n";
	
	private static final String TEXT_1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + NL
										+ "<!DOCTYPE deviceconfiguration PUBLIC \"-//JTRS//DTD SCA V2.2.2 DCD//EN\" \"deviceconfiguration.dtd\">" + NL 
										+ "<deviceconfiguration name=\""; 
	
	private static final String TEXT_2 = "\" id=\"";
	
	private static final String TEXT_3 = "\">" + NL 
									+ "    <devicemanagersoftpkg>"
			+ NL + "    \t<localfile name=\"/mgr/DeviceManager.spd.xml\">" + NL
			+ "    \t</localfile>" + NL + "    </devicemanagersoftpkg>";
	private static final String TEXT_9 = "    " + NL + "    <partitioning>";
	private static final String TEXT_15 = NL + "    </partitioning>" + NL
			+ "    <domainmanager>" + NL + "    \t<namingservice name=\"";
	private static final String TEXT_17 = "\">" + NL + "    \t</namingservice>" + NL
			+ "    </domainmanager>" + NL + "</deviceconfiguration>";

	private int filePointer;
	private String deviceManagerName;

	public byte[] generate() {
		final StringBuffer stringBuffer = new StringBuffer();

		stringBuffer.append(TEXT_1);

		String host = deviceManagerName;
		try {
			host = deviceManagerName+"_" + Inet4Address.getLocalHost().getCanonicalHostName();
			if ("localhost".equalsIgnoreCase(host)) {
				host = deviceManagerName;
			}
		} catch (UnknownHostException e1) {
			logger.log(Level.SEVERE, "UnknownHostException", e1);
		}

		stringBuffer.append(host);
		stringBuffer.append(TEXT_2);
		stringBuffer.append(identifier);
		stringBuffer.append(TEXT_3);
		stringBuffer.append(TEXT_9);
		stringBuffer.append(TEXT_15);
		String domainLocation = domainName+"/"+domainName;
		stringBuffer.append(domainLocation);
		stringBuffer.append(TEXT_17);

		try {
			return stringBuffer.toString().getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.log(Level.SEVERE, "An UnsupportedEncodingException has occurred.",e);
		}

		return null;

	}


	public DcdFile(String domainName, String deviceManagerName) {
		this.domainName = domainName;
		this.deviceManagerName = deviceManagerName;
	}

	@Override
	public String fileName() {
		return "DeviceManager.dcd.xml";
	}

	@Override
	public int filePointer() {
		return filePointer;
	}

	@Override
	public void read(OctetSequenceHolder data, int length) throws IOException {
	    if (filePointer == 0) {
			data.value = generate();
			filePointer++;
	    } else {
	    	data.value = new byte[0];
	    }
	}

	@Override
	public void write(byte[] data) throws IOException {

	}

	@Override
	public int sizeOf() throws FileException {
		return generate().length;
	}

	@Override
	public void close() throws FileException {
		filePointer = 0;
	}

	@Override
	public void setFilePointer(int filePointer) throws InvalidFilePointer, FileException {
		this.filePointer = filePointer;
	}


	public String getIdentifier() {
		return identifier;
	}


	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

}
