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
