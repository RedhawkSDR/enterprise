package redhawk.mock;

import java.io.File;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import CF.FileException;
import CF.FileOperations;
import CF.OctetSequenceHolder;
import CF.FilePackage.IOException;
import CF.FilePackage.InvalidFilePointer;

public class MyDcDFile implements FileOperations{
	private static Logger logger = Logger.getLogger(MyDcDFile.class.getName());
	
	private File dcdFileRep; 
	
	private String fileName; 
	
	private StringBuffer buffer;
	
	private int filePointer;

	public MyDcDFile(File file) throws java.io.IOException{
		this.dcdFileRep = file;
		this.fileName = file.getName();
		buffer = new StringBuffer(FileUtils.readFileToString(this.dcdFileRep));
	}

	@Override
	public String fileName() {
		// TODO Auto-generated method stub
		return this.fileName;
	}

	@Override
	public int filePointer() {
		// TODO Auto-generated method stub
		return filePointer;
	}

	@Override
	public void read(OctetSequenceHolder data, int length) throws IOException {
	    if (filePointer == 0) {
			try {
				data.value = buffer.toString().getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			filePointer++;
	    } else {
	    	logger.error("No bueno");
	    	data.value = new byte[0];
	    }
	}

	@Override
	public void write(byte[] data) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int sizeOf() throws FileException {
		// TODO Auto-generated method stub
		return buffer.toString().length();
	}

	@Override
	public void close() throws FileException {
		// TODO Auto-generated method stub
		filePointer = 0;
	}

	@Override
	public void setFilePointer(int filePointer) throws InvalidFilePointer, FileException {
		// TODO Auto-generated method stub
		this.filePointer = filePointer;
	}

}
