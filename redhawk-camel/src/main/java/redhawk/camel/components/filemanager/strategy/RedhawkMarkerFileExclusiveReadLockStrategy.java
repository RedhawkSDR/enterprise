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
package redhawk.camel.components.filemanager.strategy;

import java.io.File;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.component.file.FileComponent;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.file.GenericFileEndpoint;
import org.apache.camel.component.file.GenericFileExclusiveReadLockStrategy;
import org.apache.camel.component.file.GenericFileOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redhawk.camel.components.filemanager.RedhawkFileContainer;
import redhawk.camel.components.filemanager.RedhawkFileOperations;

public class RedhawkMarkerFileExclusiveReadLockStrategy implements GenericFileExclusiveReadLockStrategy<RedhawkFileContainer>{

    private static final transient Logger LOG = LoggerFactory.getLogger(RedhawkMarkerFileExclusiveReadLockStrategy.class);
    
    public void prepareOnStartup(GenericFileOperations<RedhawkFileContainer> operations, GenericFileEndpoint<RedhawkFileContainer> endpoint) {
        String dir = endpoint.getConfiguration().getDirectory();
        LOG.debug("Prepare on startup by deleting orphaned lock files from: "+ dir);
        deleteLockFiles(dir, endpoint.isRecursive(), operations);
    }

    public boolean acquireExclusiveReadLock(GenericFileOperations<RedhawkFileContainer> operations, GenericFile<RedhawkFileContainer> file, Exchange exchange) throws Exception {
        String lockFileName = getLockFileName(file);
        LOG.info("Locking the file: "+file.getFileName()+" using the lock file name: "+lockFileName);

        // create a plain file as marker filer for locking (do not use FileLock)
        boolean acquired = ((RedhawkFileOperations)operations).createNewFile(lockFileName);

        return acquired;
    }
    
    @Override
    public void releaseExclusiveReadLockOnAbort(GenericFileOperations<RedhawkFileContainer> operations, GenericFile<RedhawkFileContainer> file, Exchange exchange) throws Exception {
        doReleaseExclusiveReadLock(operations, file, exchange);
    }

    @Override
    public void releaseExclusiveReadLockOnRollback(GenericFileOperations<RedhawkFileContainer> operations, GenericFile<RedhawkFileContainer> file, Exchange exchange) throws Exception {
        doReleaseExclusiveReadLock(operations, file, exchange);
    }

    @Override
    public void releaseExclusiveReadLockOnCommit(GenericFileOperations<RedhawkFileContainer> operations, GenericFile<RedhawkFileContainer> file, Exchange exchange) throws Exception {
        doReleaseExclusiveReadLock(operations, file, exchange);
    }

    protected void doReleaseExclusiveReadLock(GenericFileOperations<RedhawkFileContainer> operations, GenericFile<RedhawkFileContainer> file, Exchange exchange) throws Exception {
        String lockFileName = getLockFileName(file);

        LOG.info("Unlocking file: "+ lockFileName);
        
        boolean deleted = operations.deleteFile(lockFileName);
        LOG.info("Lock file: "+lockFileName+" was deleted: "+deleted);
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
	public void setDeleteOrphanLockFiles(boolean arg0) {
		// TODO Auto-generated method stub
		
	}
    
    
}
