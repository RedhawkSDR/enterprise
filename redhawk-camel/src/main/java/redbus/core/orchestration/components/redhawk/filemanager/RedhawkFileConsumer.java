package redbus.core.orchestration.components.redhawk.filemanager;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Processor;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.file.GenericFileConsumer;
import org.apache.camel.component.file.GenericFileOperationFailedException;

import redbus.core.orchestration.components.redhawk.RedhawkComponent;
import redbus.core.orchestration.components.redhawk.endpoints.RedhawkFileEndpoint;
import CF.DeviceManager;
import CF.DomainManager;
import CF.File;
import CF.FileException;
import CF.FileSystem;
import CF.InvalidFileName;

public class RedhawkFileConsumer extends GenericFileConsumer<RedhawkFileContainer>{

    private String endpointPath;
    
    private RedhawkFileOperations operations;
    private RedhawkFileEndpoint endpoint; 
    
    public RedhawkFileConsumer(RedhawkFileEndpoint endpoint, Processor processor, final RedhawkFileOperations operations) {
        super(endpoint, processor, operations);
        this.operations = operations;
        this.endpoint = endpoint;
        this.endpointPath = endpoint.getConfiguration().getDirectory();
    }

    
    @Override
    protected void doStart() throws Exception {
        log.trace("INVOKING DO START");
        
        String domainName = ((RedhawkComponent) endpoint.getComponent()).getDomainName();
        DomainManager domainManager = ((RedhawkComponent) endpoint.getComponent()).getRedhawkDriver().getDomain(domainName).getCorbaObj();
        
        if(endpoint.getFileManagerType() != null && endpoint.getFileManagerType().equalsIgnoreCase("DEVICE")){
            String devManagerName = endpoint.getDeviceManagerName();
            
            for(DeviceManager devManager : domainManager.deviceManagers()){
                if(devManagerName.equalsIgnoreCase(devManager.label())){
                    FileSystem fileManager = (FileSystem) devManager.fileSys();
                    operations.setFileManager(fileManager);
                    break;
                }
            }
            
            if(operations.getFileManager() == null){
                throw new IllegalArgumentException("Device Manager: " + devManagerName + " was not found.");
            }
            
        } else {
            FileSystem fileManager = (FileSystem) domainManager.fileMgr();
            operations.setFileManager(fileManager);
        }
        
        // auto create starting directory if needed
        if (!operations.existsFile(endpointPath) && !operations.isDirectory(endpointPath)) {
            if (endpoint.isAutoCreate()) {
                log.trace("Creating non existing starting directory: {}", endpointPath);
                boolean absolute = endpoint.isAbsolute(endpointPath);
                boolean created = operations.buildDirectory(endpointPath, absolute);
                if (!created) {
                    log.warn("Cannot auto create starting directory: {}" + endpointPath);
                }
            } else if (endpoint.isStartingDirectoryMustExist()) {
                throw new FileNotFoundException("Starting directory does not exist: " + endpointPath);
            }
        }                  
        
        super.doStart();
    }
    
    
    @Override
    protected void doStop() throws Exception {
        super.doStop();
    }


    protected boolean pollDirectory(String directoryName, List<GenericFile<RedhawkFileContainer>> fileList, int depth) {
        
            log.trace("pollDirectory from fileName: "+ directoryName);
        
//            if(endpoint.getRedhawkIntegration().getRedhawkConnection().isStarted()){
            
                depth++;
        
                if (!operations.existsFile(directoryName) || !operations.isDirectory(directoryName)){
                    log.warn("Cannot poll as directory does not exists or its not a directory: "+directoryName);
                    if (getEndpoint().isDirectoryMustExist()) {
                        throw new GenericFileOperationFailedException("Directory does not exist: " + directoryName);
                    }
                    return true;
                }
        
                log.trace("Polling directory: "+directoryName);
                
                
                
                List<RedhawkFileContainer> files = operations.listFiles(directoryName);
                if (files.size() == 0) {
                    // no files in this directory to poll
                    log.trace("No files found in directory: "+directoryName);
                    return true;
                } else {
                    // we found some files
                    if (log.isTraceEnabled()) {
                        log.trace("Found "+files.size()+" in directory: "+directoryName);
                    }
                }
        
                for (RedhawkFileContainer file : files) {
                    // check if we can continue polling in files
                    if (!canPollMoreFiles(fileList)) {
                        return false;
                    }
        
                    // trace log as Windows/Unix can have different views what the file is?
                    if (log.isTraceEnabled()) {
                        log.trace("Found file: {} [isAbsolute: {}, isDirectory: {}, isFile: {}, isHidden: {}]",
                                new Object[]{file, true, operations.isDirectory(file.getFilePath()) , !operations.isDirectory(file.getFilePath()), false});
                    }
        
                    
                    
                    if (operations.isDirectory(file.getFilePath())) {
                        if (endpoint.isRecursive() && depth < endpoint.getMaxDepth()) {
                            // recursive scan and add the sub files and folders
                            String subDirectory = file.getFilePath();
                            boolean canPollMore = pollDirectory(subDirectory, fileList, depth);
                            if (!canPollMore) {
                                return false;
                            }
                        }
                    } else {
                        // creates a generic file
                        
                        GenericFile<RedhawkFileContainer> gf = asGenericFile(endpointPath, file, getEndpoint().getCharset());
                        // Windows can report false to a file on a share so regard it always as a file (if its not a directory)
                        List<RedhawkFileContainer> rhFiles = new ArrayList<RedhawkFileContainer>();
                        if (isValidFile(gf, false, rhFiles) && depth >= endpoint.getMinDepth()) {
//                            if (isInProgress(gf)) {
//                                log.error("Skipping as file is already in progress: {}", gf.getFileName());
//                            } else {
                                log.error("Adding valid file: {}", file);
                                // matched file so add
                                fileList.add(gf);
//                            }
                        }
                    }
                }
        
                return true;
//            } else {
//                log.warn("Not connected to redhawk... Cannot poll directory");
//                return true;
//            }
    }

    /**
     * Creates a new GenericFile<File> based on the given file.
     *
     * @param endpointPath the starting directory the endpoint was configured with
     * @param file the source file
     * @return wrapped as a GenericFile
     */
    public GenericFile<RedhawkFileContainer> asGenericFile(String endpointPath, RedhawkFileContainer file, String charset) {
        GenericFile<RedhawkFileContainer> answer = new GenericFile<RedhawkFileContainer>();
        // use file specific binding
        answer.setBinding(new RedhawkFileBinding());

        answer.setCharset(charset);
        answer.setEndpointPath(endpointPath);
        answer.setFile(file);
        
        String fileName = file.getFilePath().substring(file.getFilePath().lastIndexOf("/")+1, file.getFilePath().length());
        answer.setFileNameOnly(fileName);
        
        File actualFile = null;
        try {
            actualFile = file.getFile();
            answer.setFileLength(actualFile.sizeOf());
        } catch (FileException e) {
            e.printStackTrace();
        } catch (InvalidFileName e) {
            e.printStackTrace();
        } finally{
            try {
                if(actualFile != null){
                    actualFile.close();
                }
            } catch (FileException e) {
                e.printStackTrace();
            }
        }
        
        
        answer.setDirectory(operations.isDirectory(file.getFilePath()) );
        // must use FileUtil.isAbsolute to have consistent check for whether the file is
        // absolute or not. As windows do not consider \ paths as absolute where as all
        // other OS platforms will consider \ as absolute. The logic in Camel mandates
        // that we align this for all OS. That is why we must use FileUtil.isAbsolute
        // to return a consistent answer for all OS platforms.
        answer.setAbsolute(true);
        answer.setAbsoluteFilePath(file.getFilePath());
        answer.setLastModified(operations.getLastModifiedDate(file.getFilePath()));

        // the file name should be the relative path
        answer.setFileName(fileName);

        // use file as body as we have converters if needed as stream
        answer.setBody(file);
        return answer;
    }

    @Override
    public RedhawkFileEndpoint getEndpoint() {
        return (RedhawkFileEndpoint) super.getEndpoint();
    }


    @Override
    protected boolean isMatched(GenericFile<RedhawkFileContainer> file, String doneFileName, List<RedhawkFileContainer> files) {
        return false;
            //super.isMatched(file, isDirectory, files);
    }

    
    
    
}
