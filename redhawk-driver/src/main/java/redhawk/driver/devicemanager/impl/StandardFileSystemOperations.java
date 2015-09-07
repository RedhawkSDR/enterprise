package redhawk.driver.devicemanager.impl;

import java.util.logging.Logger;

import org.omg.CORBA.ORB;
import org.omg.PortableServer.POA;

import CF.File;
import CF.FileException;
import CF.InvalidFileName;

public class StandardFileSystemOperations extends JavaFileSystem {

    private static Logger logger = Logger.getLogger(StandardFileSystemOperations.class.getName());
	
	private File dcdFile;
	
    public StandardFileSystemOperations(ORB orb, POA poa, java.io.File root) {
        super(orb, poa, root);
    }
        
	@Override
	public File open(String fileName, boolean readOnly) throws InvalidFileName, FileException {
		if("DeviceManager.dcd.xml".equals(fileName) || "/DeviceManager.dcd.xml".equals(fileName)){
			return dcdFile;
		} else {
		    return super.open(fileName, readOnly);
		}
	}
	
    public File getDcdFile() {
        return dcdFile;
    }
	
    public void setDcdFile(File dcdFile) {
        this.dcdFile = dcdFile;
    }
	
}


