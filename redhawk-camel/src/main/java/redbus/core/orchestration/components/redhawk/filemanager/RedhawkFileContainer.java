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
package redbus.core.orchestration.components.redhawk.filemanager;

import CF.File;
import CF.FileException;
import CF.FileSystem;
import CF.InvalidFileName;

public class RedhawkFileContainer {

    private String filePath;
    private FileSystem fileManager;
    private boolean readOnly;
    
    
    public RedhawkFileContainer(String filePath, FileSystem fileManager, boolean readOnly) {
        super();
        this.filePath = filePath;
        this.fileManager = fileManager;
        this.readOnly = readOnly;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public FileSystem getFileManager() {
        return fileManager;
    }

    public void setFileManager(FileSystem fileManager) {
        this.fileManager = fileManager;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public File getFile() throws InvalidFileName, FileException {
        return fileManager.open(filePath, readOnly);
    }
    
    
    
    
}
