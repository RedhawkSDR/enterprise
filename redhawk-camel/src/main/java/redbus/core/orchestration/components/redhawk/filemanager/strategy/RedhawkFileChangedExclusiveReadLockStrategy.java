package redbus.core.orchestration.components.redhawk.filemanager.strategy;


import org.apache.camel.Exchange;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.file.GenericFileOperations;
import org.apache.camel.util.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redbus.core.orchestration.components.redhawk.filemanager.RedhawkFileContainer;
import redbus.core.orchestration.components.redhawk.filemanager.RedhawkFileOperations;

/**
 * Acquires exclusive read lock to the given file by checking whether the file is being
 * changed by scanning the file at different intervals (to detect changes).
 */
public class RedhawkFileChangedExclusiveReadLockStrategy extends RedhawkMarkerFileExclusiveReadLockStrategy {
    private static final transient Logger LOG = LoggerFactory.getLogger(RedhawkFileChangedExclusiveReadLockStrategy.class);
    private long timeout;
    private long checkInterval = 1000;

    public boolean acquireExclusiveReadLock(GenericFileOperations<RedhawkFileContainer> operations, GenericFile<RedhawkFileContainer> file, Exchange exchange) throws Exception {
        // must call super
        if (!super.acquireExclusiveReadLock(operations, file, exchange)) {
            return false;
        }

        
        String target = file.getAbsoluteFilePath();
        boolean exclusive = false;

        LOG.trace("Waiting for exclusive read lock to file: {}", file);

        long lastModified = Long.MIN_VALUE;
        long length = Long.MIN_VALUE;
        StopWatch watch = new StopWatch();

        while (!exclusive) {
            // timeout check
            if (timeout > 0) {
                long delta = watch.taken();
                if (delta > timeout) {
                    LOG.warn("Cannot acquire read lock within " + timeout + " millis. Will skip the file: " + file);
                    // we could not get the lock within the timeout period, so return false
                    return false;
                }
            }

            long newLastModified = ((RedhawkFileOperations)operations).getLastModifiedDate(target);
            
            long newLength = ((RedhawkFileOperations)operations).getFileLength(target);

            LOG.trace("Previous last modified: {}, new last modified: {}", lastModified, newLastModified);
            LOG.trace("Previous length: {}, new length: {}", length, newLength);

            if (newLastModified == lastModified && newLength == length && length != 0) {
                // We consider that zero-length files are files in progress
                LOG.trace("Read lock acquired.");
                exclusive = true;
            } else {
                // set new base file change information
                lastModified = newLastModified;
                length = newLength;

                boolean interrupted = sleep();
                if (interrupted) {
                    // we were interrupted while sleeping, we are likely being shutdown so return false
                    return false;
                }
            }
        }

        return exclusive;
    }

    private boolean sleep() {
        LOG.trace("Exclusive read lock not granted. Sleeping for {} millis.", checkInterval);
        try {
            Thread.sleep(checkInterval);
            return false;
        } catch (InterruptedException e) {
            LOG.debug("Sleep interrupted while waiting for exclusive read lock, so breaking out");
            return true;
        }
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public long getCheckInterval() {
        return checkInterval;
    }

    public void setCheckInterval(long checkInterval) {
        this.checkInterval = checkInterval;
    }

}
