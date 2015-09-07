package redbus.core.orchestration.components.redhawk.filemanager.strategy;

import org.apache.camel.Exchange;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.file.GenericFileOperations;
import org.apache.camel.component.file.strategy.GenericFileRenameExclusiveReadLockStrategy;

import redbus.core.orchestration.components.redhawk.filemanager.RedhawkFileContainer;

public class RedhawkFileRenameExclusiveReadLockStrategy extends GenericFileRenameExclusiveReadLockStrategy<RedhawkFileContainer> {
    
    private RedhawkMarkerFileExclusiveReadLockStrategy marker = new RedhawkMarkerFileExclusiveReadLockStrategy();

    @Override
    public boolean acquireExclusiveReadLock(GenericFileOperations<RedhawkFileContainer> operations, GenericFile<RedhawkFileContainer> file, Exchange exchange) throws Exception {
        // must call marker first
        if (!marker.acquireExclusiveReadLock(operations, file, exchange)) {
            return false;
        }

        return super.acquireExclusiveReadLock(operations, file, exchange);
    }
}
