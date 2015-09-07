package redhawk.driver.devicemanager.impl;

import java.util.List;

import CF.FileSystem;
import redhawk.driver.base.impl.RedhawkFileSystemImpl;
import redhawk.driver.devicemanager.RedhawkDeviceManagerFileSystem;

public class RedhawkDeviceManagerFileSystemImpl extends RedhawkFileSystemImpl implements RedhawkDeviceManagerFileSystem {

	
	private FileSystem fileSystem;
	
	public RedhawkDeviceManagerFileSystemImpl(FileSystem fileSystem){
		super(fileSystem);
		this.fileSystem = fileSystem;
	}
	
	
	public List<String> getDeviceManagers(){
		return findDirectoriesInDirectory("/nodes", "*");
	}
	
	public FileSystem getCorbaObject(){
		return fileSystem;
	}
	
}