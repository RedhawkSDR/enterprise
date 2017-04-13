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
package redhawk.camel.components.endpoints;

import java.io.ByteArrayOutputStream;

import org.apache.camel.Component;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.component.file.FileConsumer;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.file.GenericFileEndpoint;
import org.apache.camel.component.file.GenericFileExist;
import org.apache.camel.component.file.GenericFileProcessStrategy;
import org.apache.camel.processor.idempotent.MemoryIdempotentRepository;
import org.apache.camel.util.ObjectHelper;
import org.apache.log4j.Logger;

import CF.File;
import CF.FileException;
import CF.InvalidFileName;
import CF.OctetSequenceHolder;
import CF.FilePackage.IOException;
import redhawk.camel.components.filemanager.RedhawkFileConsumer;
import redhawk.camel.components.filemanager.RedhawkFileContainer;
import redhawk.camel.components.filemanager.RedhawkFileOperations;
import redhawk.camel.components.filemanager.RedhawkFileProducer;
import redhawk.camel.components.filemanager.RedhawkFileUtil;
import redhawk.camel.components.filemanager.strategy.RedhawkFileProcessStrategyFactory;

public class RedhawkFileEndpoint extends GenericFileEndpoint<RedhawkFileContainer>{
	private Logger logger = Logger.getLogger(RedhawkFileEndpoint.class);
	
    private RedhawkFileOperations operations = new RedhawkFileOperations(this);
    private boolean copyAndDeleteOnRenameFail = true;
    private String directoryName;
    
    private boolean whiteboardEnabled = false;

    private RedhawkFileConsumer consumer;
    private RedhawkFileProducer producer;
    
    private String fileManagerType;
    private String deviceManagerName;
    
    public RedhawkFileEndpoint() {
        // use marker file as default exclusive read locks
        this.readLock = "markerFile";
    }

    @Override
    protected GenericFileProcessStrategy<RedhawkFileContainer> createGenericFileStrategy() {
    	return RedhawkFileProcessStrategyFactory.createGenericFileProcessStrategy(getCamelContext(), getParamsAsMap());
    }

    public RedhawkFileEndpoint(String endpointUri, Component component) {
        super(endpointUri, component);
        // use marker file as default exclusive read locks
        this.readLock = "markerFile";
    }

    public RedhawkFileConsumer createConsumer(Processor processor) throws Exception {

        ObjectHelper.notNull(operations, "operations");
        ObjectHelper.notNull(directoryName, "directoryName");

        RedhawkFileConsumer result = newFileConsumer(processor, operations);

        if (isDelete() && getMove() != null) {
            throw new IllegalArgumentException("You cannot set both delete=true and move options");
        }

        // if noop=true then idempotent should also be configured
        if (isNoop() && idempotent == null) {
            log.info("Endpoint is configured with noop=true so forcing endpoint to be idempotent as well");
            setIdempotent(true);
        }

        // if idempotent and no repository set then create a default one
        if (idempotent != null && isIdempotent() && idempotentRepository == null) {
            log.info("Using default memory based idempotent repository with cache max size: " + DEFAULT_IDEMPOTENT_CACHE_SIZE);
            idempotentRepository = MemoryIdempotentRepository.memoryIdempotentRepository(DEFAULT_IDEMPOTENT_CACHE_SIZE);
        }

        // set max messages per poll
        result.setMaxMessagesPerPoll(getMaxMessagesPerPoll());
        result.setEagerLimitMaxMessagesPerPoll(isEagerMaxMessagesPerPoll());

        configureConsumer(result);
        consumer = result;
        return result;
    }

    public RedhawkFileProducer createProducer() throws Exception {

        // you cannot use temp prefix and file exists append
        if (getFileExist() == GenericFileExist.Append && getTempPrefix() != null) {
            throw new IllegalArgumentException("You cannot set both fileExist=Append and tempPrefix options");
        }
        
        producer = new RedhawkFileProducer(this, operations);
        return producer;
    }

    public Exchange createExchange(GenericFile<RedhawkFileContainer> file) {
        Exchange exchange = createExchange();
        if (file != null) {
            file.bindToExchange(exchange);
        }
        return exchange;
    }

    /**
     * Strategy to create a new {@link FileConsumer}
     *
     * @param processor  the given processor
     * @param operations file operations
     * @return the created consumer
     */
    protected RedhawkFileConsumer newFileConsumer(Processor processor, RedhawkFileOperations operations) {
        return new RedhawkFileConsumer(this, processor, operations);
    }


    @Override
    public String createDoneFileName(String fileName) {
        return super.createDoneFileName(fileName);
    }

    @Override
    public String getScheme() {
        return "file";
    }

    @Override
    protected String createEndpointUri() {
        return getDirectoryName().toString();
    }

    @Override
    public char getFileSeparator() {       
        return java.io.File.separatorChar;
    }

    @Override
    public boolean isAbsolute(String name) {
        // relative or absolute path?
        if (name.startsWith(java.io.File.separator)) {
            return true;
        } else {
            return false;
        }
    }

    
    @Override
    protected void doStart() throws Exception {
        super.doStart();
        
        
        if(producer != null && producer.isStopped()){
            producer.start();
        }
        
        if(consumer != null && consumer.isStopped()){
            consumer.start();
        }
    }

    @Override
    protected void doStop() throws Exception {
        super.doStop();
        
        if(producer != null && producer.isStarted()){
            producer.stop();
        }
        
        if(consumer != null && consumer.isStarted()){
            consumer.stop();
        }
    }
    
    @Override
    public void configureMessage(GenericFile<RedhawkFileContainer> file, Message message) {
        
        super.configureMessage(file, message);
        
        File f = null;
        ByteArrayOutputStream bos = null;
        try {
            f = ((RedhawkFileContainer) file.getFile()).getFile();
            OctetSequenceHolder holder = new OctetSequenceHolder();
            
            int fileSize;
            try {
                fileSize = f.sizeOf();
            } catch (FileException e) {
                throw new java.io.IOException("IOException on LoadContent");
            }
            
            int readSize = 1024*2000;
            
            bos = new ByteArrayOutputStream();
            
            for(int i=0; i < (fileSize/readSize)+1; i++){
                f.read(holder, readSize);
                bos.write(holder.value);
            }                
            
            message.setBody(bos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } catch (InvalidFileName e1) {
            e1.printStackTrace();
        } catch (FileException e1) {
            e1.printStackTrace();
        } finally{
            if(f != null){
                try {
                    f.close();
                } catch (FileException e) {
                    e.printStackTrace();
                }
            }
            
            if(bos != null){
                try {
                    bos.close();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean isCopyAndDeleteOnRenameFail() {
        return copyAndDeleteOnRenameFail;
    }

    public void setCopyAndDeleteOnRenameFail(boolean copyAndDeleteOnRenameFail) {
        this.copyAndDeleteOnRenameFail = copyAndDeleteOnRenameFail;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public void setDirectoryName(String directoryName) {
        directoryName = RedhawkFileUtil.hasLeadingSeparator(directoryName) ? directoryName : java.io.File.separator + directoryName;
        directoryName = RedhawkFileUtil.normalizePath(directoryName);
        this.directoryName = directoryName;
    }

    public String getFileManagerType() {
        return fileManagerType;
    }

    public void setFileManagerType(String fileManagerType) {
        this.fileManagerType = fileManagerType;
    }

    public String getDeviceManagerName() {
        return deviceManagerName;
    }

    public void setDeviceManagerName(String deviceManagerName) {
        this.deviceManagerName = deviceManagerName;
    }


    public boolean isWhiteboardEnabled() {
        return whiteboardEnabled;
    }

    public void setWhiteboardEnabled(boolean whiteboardEnabled) {
        this.whiteboardEnabled = whiteboardEnabled;
    }

    
}
