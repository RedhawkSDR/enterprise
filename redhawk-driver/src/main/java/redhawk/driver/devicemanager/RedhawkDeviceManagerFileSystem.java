package redhawk.driver.devicemanager;

import CF.FileSystem;
import redhawk.driver.base.RedhawkFileSystem;

public interface RedhawkDeviceManagerFileSystem extends RedhawkFileSystem {

	FileSystem getCorbaObject();	
	
}
