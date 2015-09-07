package redbus.core.orchestration.components.redhawk.filemanager.strategy;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.component.file.FileComponent;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.file.GenericFileEndpoint;
import org.apache.camel.component.file.GenericFileExclusiveReadLockStrategy;
import org.apache.camel.component.file.GenericFileOperations;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redbus.core.orchestration.components.redhawk.filemanager.RedhawkFileContainer;
import redbus.core.orchestration.components.redhawk.filemanager.RedhawkFileOperations;
import CF.File;

public class RedhawkMarkerFileExclusiveReadLockStrategy implements GenericFileExclusiveReadLockStrategy<RedhawkFileContainer>{

    private static final transient Log LOG = LogFactory.getLog(RedhawkMarkerFileExclusiveReadLockStrategy.class);
    
    public void prepareOnStartup(GenericFileOperations<RedhawkFileContainer> operations, GenericFileEndpoint<RedhawkFileContainer> endpoint) {
        String dir = endpoint.getConfiguration().getDirectory();
        LOG.debug("Prepare on startup by deleting orphaned lock files from: "+ dir);
        deleteLockFiles(dir, endpoint.isRecursive(), operations);
    }

    public boolean acquireExclusiveReadLock(GenericFileOperations<RedhawkFileContainer> operations, GenericFile<RedhawkFileContainer> file, Exchange exchange) throws Exception {
        String lockFileName = getLockFileName(file);
        LOG.trace("Locking the file: "+file.getFileName()+" using the lock file name: "+lockFileName);

        // create a plain file as marker filer for locking (do not use FileLock)
        boolean acquired = ((RedhawkFileOperations)operations).createNewFile(lockFileName);

        return acquired;
    }

    public void releaseExclusiveReadLock(GenericFileOperations<RedhawkFileContainer> operations, GenericFile<RedhawkFileContainer> file, Exchange exchange) throws Exception {
        String lockFileName = getLockFileName(file);

        LOG.trace("Unlocking file: "+ lockFileName);
        
        boolean deleted = operations.deleteFile(lockFileName);
        LOG.trace("Lock file: "+lockFileName+" was deleted: "+deleted);
    }

    public void setTimeout(long timeout) {
        // noop
    }

    public void setCheckInterval(long checkInterval) {
        // noop
    }

    private void deleteLockFiles(String dir, boolean recursive, GenericFileOperations<RedhawkFileContainer> operations) {
        
        List<RedhawkFileContainer> files = operations.listFiles(dir);
        if (files.isEmpty()) {
            return;
        }

        for (RedhawkFileContainer file : files) {
            String fileName = file.getFilePath().substring(file.getFilePath().lastIndexOf("/"), file.getFilePath().length());
            
            if (fileName.startsWith(".")) {
                // files starting with dot should be skipped
                continue;
            } else if (fileName.endsWith(FileComponent.DEFAULT_LOCK_FILE_POSTFIX)) {
                LOG.warn("Deleting orphaned lock file: " + file.getFilePath());
                operations.deleteFile(file.getFilePath());
            } else if (recursive && ((RedhawkFileOperations)operations).isDirectory(file.getFilePath())) {
                deleteLockFiles(file.getFilePath(), true,operations);
            }
        }
    }

    private static String getLockFileName(GenericFile<RedhawkFileContainer> file) {
        return file.getAbsoluteFilePath() + FileComponent.DEFAULT_LOCK_FILE_POSTFIX;
    }
    
    //TODO: Implement the methods below....
    @Override
    public void setReadLockLoggingLevel(LoggingLevel readLockLoggingLevel) {
    	// noop - we don't even catch the exception to log in the acquireExclusiveReadLock method
    }

	@Override
	public void setMarkerFiler(boolean markerFile) {
		// noop - we don't use a marker file
	}

	@Override
	public void releaseExclusiveReadLockOnAbort(
			GenericFileOperations<RedhawkFileContainer> arg0,
			GenericFile<RedhawkFileContainer> arg1, Exchange arg2)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void releaseExclusiveReadLockOnCommit(
			GenericFileOperations<RedhawkFileContainer> arg0,
			GenericFile<RedhawkFileContainer> arg1, Exchange arg2)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void releaseExclusiveReadLockOnRollback(
			GenericFileOperations<RedhawkFileContainer> arg0,
			GenericFile<RedhawkFileContainer> arg1, Exchange arg2)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDeleteOrphanLockFiles(boolean arg0) {
		// TODO Auto-generated method stub
		
	}
    
    
}
