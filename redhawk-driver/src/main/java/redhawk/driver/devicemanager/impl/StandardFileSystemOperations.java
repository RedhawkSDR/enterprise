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


