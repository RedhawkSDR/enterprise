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
package redhawk.camel.components.filemanager;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Processor;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.file.GenericFileConsumer;
import org.apache.camel.component.file.GenericFileOperationFailedException;

import CF.DeviceManager;
import CF.DomainManager;
import CF.File;
import CF.FileException;
import CF.FileSystem;
import CF.InvalidFileName;
import redhawk.camel.components.RedhawkComponent;
import redhawk.camel.components.endpoints.RedhawkFileEndpoint;

/**
 * Extension of GenericFileConsumer giving you FileConsumer capabilities on a
 * CF.FileSyste
 *
 */
public class RedhawkFileConsumer extends GenericFileConsumer<RedhawkFileContainer>{

    private String endpointPath;
    
    private RedhawkFileOperations operations;
    private RedhawkFileEndpoint endpoint; 
    //private Set<String> extendedAttributes;

    
    public RedhawkFileConsumer(RedhawkFileEndpoint endpoint, Processor processor, final RedhawkFileOperations operations) {
        super(endpoint, processor, operations);
        this.operations = operations;
        this.endpoint = endpoint;
        this.endpointPath = endpoint.getConfiguration().getDirectory();
        
        /*
         * if (endpoint.getExtendedAttributes() != null) {
            this.extendedAttributes = new HashSet<>();

            for (String attribute : endpoint.getExtendedAttributes().split(",")) {
                extendedAttributes.add(attribute);
            }
        }
        */
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
                log.info("Creating non existing starting directory: {}", endpointPath);
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
        
		log.trace("pollDirectory from fileName: " + directoryName);

		depth++;

		/*
		 * Check to see if valid directory...
		 */
		if (!operations.isDirectory(directoryName)) {
			log.warn("Cannot poll as directory does not exists or its not a directory: " + directoryName);
			if (getEndpoint().isDirectoryMustExist()) {
				throw new GenericFileOperationFailedException("Directory does not exist: " + directoryName);
			}
			return true;
		}

		log.trace("Polling directory: " + directoryName);

		List<RedhawkFileContainer> files = operations.listFiles(directoryName);
		if (files.size() == 0) {
			// no files in this directory to poll
			log.trace("No files found in directory: " + directoryName);
			return true;
		} else {
			// we found some files
			if (log.isTraceEnabled()) {
				log.trace("Found " + files.size() + " in directory: " + directoryName);
			}
		}

		for (RedhawkFileContainer file : files) {
			// check if we can continue polling in files
			if (!canPollMoreFiles(fileList)) {
				return false;
			}

			// trace log as Windows/Unix can have different views what the file
			// is?
			if (log.isTraceEnabled()) {
				log.trace("Found file: {} [isAbsolute: {}, isDirectory: {}, isFile: {}, isHidden: {}]",
						new Object[] { file, true, operations.isDirectory(file.getFilePath()),
								!operations.isDirectory(file.getFilePath()), false });
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

				// Windows can report false to a file on a share so regard it
				// always as a file (if its not a directory)
				List<RedhawkFileContainer> rhFiles = new ArrayList<RedhawkFileContainer>();
				
				GenericFile<RedhawkFileContainer> gf = asGenericFile(endpointPath, file, getEndpoint().getCharset());

				if (isValidFile(gf, false, rhFiles) && depth >= endpoint.getMinDepth()) {
					log.trace("Adding valid file: {} "+file.getFilePath(), file);
                    // matched file so add
                    /*
                     * if (extendedAttributes != null) {
                        Path path = file.toPath();
                        Map<String, Object> allAttributes = new HashMap<>();
                        for (String attribute : extendedAttributes) {
                            try {
                                String prefix = null;
                                if (attribute.endsWith(":*")) {
                                    prefix = attribute.substring(0, attribute.length() - 1);
                                } else if (attribute.equals("*")) {
                                    prefix = "basic:";
                                }

                                if (ObjectHelper.isNotEmpty(prefix)) {
                                    Map<String, Object> attributes = Files.readAttributes(path, attribute);
                                    if (attributes != null) {
                                        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
                                            allAttributes.put(prefix + entry.getKey(), entry.getValue());
                                        }
                                    }
                                } else if (!attribute.contains(":")) {
                                    allAttributes.put("basic:" + attribute, Files.getAttribute(path, attribute));
                                } else {
                                    allAttributes.put(attribute, Files.getAttribute(path, attribute));
                                }
                            } catch (IOException e) {
                                if (log.isDebugEnabled()) {
                                    log.debug("Unable to read attribute {} on file {}", attribute, file, e);
                                }
                            }
                        }

                        gf.setExtendedAttributes(allAttributes);
                    }
                    */
					// if (isInProgress(gf)) {
					// log.error("Skipping as file is already in progress: {}",
					// gf.getFileName());
					// } else {
					// matched file so add
					fileList.add(gf);
					// }
				}
			}
		}

		return true;
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
