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

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.InvalidPayloadException;
import org.apache.camel.WrappedFile;
import org.apache.camel.component.file.GenericFileEndpoint;
import org.apache.camel.component.file.GenericFileExist;
import org.apache.camel.component.file.GenericFileOperationFailedException;
import org.apache.camel.component.file.GenericFileOperations;
import org.apache.camel.util.IOHelper;
import org.apache.camel.util.ObjectHelper;
import org.ossie.properties.AnyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import CF.DataType;
import CF.File;
import CF.FileException;
import CF.FileSystem;
import CF.InvalidFileName;
import CF.FilePackage.IOException;
import CF.FileSystemPackage.FileInformationType;
import redhawk.camel.components.endpoints.RedhawkFileEndpoint;

/**
 * Implementation of GenericFileOperations interfaces for CF.FileSystem
 *
 */
public class RedhawkFileOperations implements GenericFileOperations<RedhawkFileContainer>{
    private static final transient Logger logger = LoggerFactory.getLogger(RedhawkFileOperations.class);

    private FileSystem fileManager;
    private RedhawkFileEndpoint endpoint;
    
    public RedhawkFileOperations() {
    }

    public RedhawkFileOperations(RedhawkFileEndpoint endpoint) {
        this.endpoint = endpoint;
    }


    public boolean deleteFile(String name) throws GenericFileOperationFailedException {
        name = RedhawkFileUtil.hasLeadingSeparator(name) ? name : "/"+name;
        
        try{
            if(fileManager.exists(name)){
            	logger.debug("Removing lock file!!!");
                fileManager.remove(name);
                return true;
            } else {
                throw new GenericFileOperationFailedException("File does not exist");
            }
        } catch(FileException fe){
            throw new GenericFileOperationFailedException("A FileException has occured", fe);
        } catch(InvalidFileName ifn){
            throw new GenericFileOperationFailedException("FileName is invalid", ifn);
        }
    }

    public boolean renameFile(String from, String to) throws GenericFileOperationFailedException {
        
        from = RedhawkFileUtil.hasLeadingSeparator(from) ? from : "/"+from;
        to = RedhawkFileUtil.hasLeadingSeparator(to) ? to : "/"+to;
        logger.debug("From: "+from+" To: "+to);
        try {
            if(fileManager.exists(from)){
                try {
                    String toDirectory = to.substring(0,to.lastIndexOf("/"));
                    logger.debug("Inside of rename making this dir: "+toDirectory);
                    fileManager.mkdir(toDirectory);
                    fileManager.move(from, to);
                    return true;
                } catch(FileException fe){
                    if(endpoint.isCopyAndDeleteOnRenameFail()){
                        try {
                            fileManager.copy(from, to);
                            fileManager.remove(from);
                            return true;
                        } catch (FileException e) {
                            throw new GenericFileOperationFailedException("A FileException has occured", e);
                        }    
                    } else {
                        throw new GenericFileOperationFailedException("A FileException has occured", fe);
                    }
                }
            } else {
                throw new GenericFileOperationFailedException("File: "+from + " does not exist");    
            }
        } catch (InvalidFileName e) {
            throw new GenericFileOperationFailedException("FileName is invalid", e);
        }
        
    }

    public boolean existsFile(String name) throws GenericFileOperationFailedException {
        try{
            return fileManager.exists(name);
        } catch (InvalidFileName ifn){
            return false;    
        }
    }

    
    public long getFileLength(String filePath) {
        
        try{
            for(FileInformationType type : fileManager.list(filePath)){
                return type.size;
            }
        } catch(FileException e){
            throw new GenericFileOperationFailedException("FileException: " +filePath);    
        } catch (InvalidFileName e) {
            throw new GenericFileOperationFailedException("InvalidFileName: " +filePath);
        }
        
        throw new GenericFileOperationFailedException("Could Not find size for file: " +filePath);
    }
    
    public long getLastModifiedDate(String filePath) {
        
        try{
            for(FileInformationType type : fileManager.list(filePath)){
                System.out.println(type.name);
                
                for(DataType prop : type.fileProperties){
                    if(prop.id.equals("MODIFIED_TIME")){
//                        long date = prop.value.extract_longlong();
                        long date = ((BigInteger) AnyUtils.convertAny(prop.value)).longValue();
                        return date*1000;
                    }
                }
            }
        } catch(FileException e){
            throw new GenericFileOperationFailedException("FileException: " +filePath);    
        } catch (InvalidFileName e) {
            throw new GenericFileOperationFailedException("InvalidFileName: " +filePath);
        }
        
        throw new GenericFileOperationFailedException("Could Not find modified date for file: " +filePath);
    }
    
    public boolean isDirectory(String filePath) {
        char lastChar = filePath.charAt(filePath.length()-1);
        
        if(lastChar == '/'){
            filePath = filePath.substring(0, filePath.length()-1);
        }
        
        char firstChar = filePath.charAt(0);

        /*
         * Shouldn't be necessary since we're already starting 
         * at dom
         * if(firstChar != '/'){
           filePath = "/"+filePath;
        }
        */
        
        boolean exists = false;
        
        try {
                FileInformationType[] types = fileManager.list(filePath);
                for(FileInformationType type : types){
                    if(type.kind.value() == 1){
                        exists = true;
                    } 
                }
        } catch(InvalidFileName ifn){
            // do nothing
        } catch (FileException e) {
            // do nothing
        }
        
        return exists;
    }
    


    public void setEndpoint(GenericFileEndpoint<RedhawkFileContainer> arg0) {
        this.endpoint = (RedhawkFileEndpoint) endpoint;    
    }

    
    public boolean buildDirectory(String directory, boolean absolute) throws GenericFileOperationFailedException {
        ObjectHelper.notNull(endpoint, "endpoint");

        logger.debug("DIRECTORY NAME: " + directory);
        
        // always create endpoint defined directory
        if (endpoint.isAutoCreate() && !existsFile(endpoint.getDirectoryName())) {
            logger.trace("Building starting directory: {}" + endpoint.getDirectoryName());
            try {
            	logger.debug("Makings directory "+endpoint.getDirectoryName());
                fileManager.mkdir(endpoint.getDirectoryName());
            } catch (InvalidFileName e) {
                throw new GenericFileOperationFailedException("Unable to create directory: " + endpoint.getDirectoryName()+". InvalidFileName: " + e);
            } catch (FileException e) {
                throw new GenericFileOperationFailedException("Unable to create directory: " + endpoint.getDirectoryName()+". FileException: " + e);
            }
        }

        if (ObjectHelper.isEmpty(directory)) {
            // no directory to build so return true to indicate ok
            return true;
        }

//        File endpointPath = fileManager.open(endpoint.getDirectoryName(), false);
//        File target = fileManager.open(directory, false);

//        File path;
//        if (absolute) {
//            // absolute path
//            path = target;
//        } 
//        else if (endpointPath.fileName().equals(target.fileName())) {
//            // its just the root of the endpoint path
//            path = endpointPath;
//        } else {
//            // relative after the endpoint path
//            String afterRoot = ObjectHelper.after(directory, endpointPath.fileName() + java.io.File.separator);
//            if (ObjectHelper.isNotEmpty(afterRoot)) {
//                // dir is under the root path
//                path = fileManager.open(afterRoot+endpoint.getFilePath(), false);
//            } else {
//                // dir is relative to the root path
//                path = fileManager.open(directory+endpoint.getFilePath(), false);
//            }
//        }

        // We need to make sure that this is thread-safe and only one thread tries to create the path directory at the same time.
        synchronized (this) {
        	//BUG IS HERE!!!!
            logger.debug("Making it down here for this dir "+directory);
        	if (isDirectory(directory)) {
                // the directory already exists
                return true;
            } else {
            	logger.info("Making it doewn into here for "+directory);
                if (logger.isTraceEnabled()) {
                    logger.trace("Building directory: {} "+ directory);
                }
                try {
                	fileManager.mkdir(directory);
                } catch (InvalidFileName e) {
                    throw new GenericFileOperationFailedException("InvalidFileName: ", e);
                } catch (FileException e) {
                    throw new GenericFileOperationFailedException("FileException: ", e);
                }
                return true;
            }
        }
    }

    public List<RedhawkFileContainer> listFiles() throws GenericFileOperationFailedException {
        return null;
    }

    
    public boolean canPollMoreFiles(List<?> fileList) {
        // at this point we should not limit if we are not eager
        if (!endpoint.isEagerMaxMessagesPerPoll()) {
            return true;
        }

        if (endpoint.getMaxMessagesPerPoll() <= 0) {
            return true;
        }

        // then only poll if we haven't reached the max limit
        return fileList.size() < endpoint.getMaxMessagesPerPoll();
    }
    
    public List<RedhawkFileContainer> listFiles(String path) throws GenericFileOperationFailedException {
        
        List<RedhawkFileContainer> files = new ArrayList<RedhawkFileContainer>();
        path = path.charAt(path.length()-1) == java.io.File.separatorChar ? path : path+java.io.File.separator;
        
        logger.debug("Listing files at directory: " + path);
        
        try{
            for(FileInformationType type : fileManager.list(path)){
                logger.debug("LISTING FILE NAME: "+type.name);
                
                if(!canPollMoreFiles(files)){
                    return files;
                }
                
                if(isDirectory(path+type.name)){
                    files.add(new RedhawkFileContainer(path+type.name+java.io.File.separatorChar, fileManager, true));
                } else if(endpoint.isNoop()){
                    files.add(new RedhawkFileContainer(path+type.name, fileManager, true));
                } else {
                    try{
                        File file = fileManager.open(path+type.name, false);
                        files.add(new RedhawkFileContainer(path+type.name, fileManager, false));
                        file.close();
                    } catch(FileException fe){
                        logger.error("Skipping:" + path+type.name + " because it can not be opened in read/write mode. This is necessary to move the file to the move directory. Please check the file permissions or enable the noop property.");
                    }
                }
            }
        } catch(FileException e){
            throw new GenericFileOperationFailedException("FileException: ", e);
        } catch (InvalidFileName e) {
            throw new GenericFileOperationFailedException("InvalidFileName: ", e);
        }
        
        return files;
    }

    public void changeCurrentDirectory(String path) throws GenericFileOperationFailedException {
    }

    public void changeToParentDirectory() throws GenericFileOperationFailedException {
    }

    public String getCurrentDirectory() throws GenericFileOperationFailedException {
        return null;
    }
    
    public boolean retrieveFile(String name, Exchange exchange) throws GenericFileOperationFailedException {
        return true;
    }
    
    

    public boolean storeFile(String fileName, Exchange exchange) throws GenericFileOperationFailedException {
        ObjectHelper.notNull(endpoint, "endpoint");

        File file;
        try {
            file = fileManager.create(fileName);
        } catch (InvalidFileName e1) {
            throw new GenericFileOperationFailedException("Unable to create new file. InvalidFileName: " + e1);
        } catch (FileException e1) {
            throw new GenericFileOperationFailedException("Unable to create new file. FileException: " + e1);
        }       
        

        // if an existing file already exists what should we do?
        if (existsFile(fileName)) {
            if (endpoint.getFileExist() == GenericFileExist.Ignore) {
                // ignore but indicate that the file was written
                logger.trace("An existing file already exists: {}. Ignore and do not override it." + fileName);
                return true;
            } else if (endpoint.getFileExist() == GenericFileExist.Fail) {
                throw new GenericFileOperationFailedException("File already exist: " + file + ". Cannot write new file.");
            }
        }

        // we can write the file by 3 different techniques
        // 1. write file to file
        // 2. rename a file from a local work path
        // 3. write stream to file
        try {

            // is there an explicit charset configured we must write the file as
            String charset = endpoint.getCharset();

            // we can optimize and use file based if no charset must be used, and the input body is a file
            java.io.File source = null;
            if (charset == null) {
                // if no charset, then we can try using file directly (optimized)
                Object body = exchange.getIn().getBody();
                if (body instanceof WrappedFile) {
                    body = ((WrappedFile<?>) body).getFile();
                }
                if (body instanceof java.io.File) {
                    source = (java.io.File) body;
                }
            }

            if (source != null) {
                // okay we know the body is a file type

               if (source.exists()) {
                    // no there is no local work file so use file to file copy if the source exists
                    writeFileByFile(source, file);
                    // try to keep last modified timestamp if configured to do so
                    keepLastModified(exchange, file);
                    return true;
                }
            }

            if (charset != null) {
                // charset configured so we must use a reader so we can write with encoding
                Reader in = exchange.getIn().getBody(Reader.class);
                if (in == null) {
                    // okay no direct reader conversion, so use an input stream (which a lot can be converted as)
                    InputStream is = exchange.getIn().getMandatoryBody(InputStream.class);
                    in = new InputStreamReader(is);
                }
                // buffer the reader
                in = IOHelper.buffered(in);
                writeFileByReaderWithCharset(in, file, charset);
            } else {
                // fallback and use stream based
                InputStream in = exchange.getIn().getMandatoryBody(InputStream.class);
                writeFileByStream(in, file);
            }
            // try to keep last modified timestamp if configured to do so
            keepLastModified(exchange, file);
            return true;
        } catch (IOException e) {
            throw new GenericFileOperationFailedException("Cannot store file: " + file, e);
        } catch (InvalidPayloadException e) {
            throw new GenericFileOperationFailedException("Cannot store file: " + file, e);
        }
    }

    private void keepLastModified(Exchange exchange, File file) {
        if (endpoint.isKeepLastModified()) {
//            Long last;
//            Date date = exchange.getIn().getHeader(Exchange.FILE_LAST_MODIFIED, Date.class);
//            if (date != null) {
//                last = date.getTime();
//            } else {
//                // fallback and try a long
//                last = exchange.getIn().getHeader(Exchange.FILE_LAST_MODIFIED, Long.class);
//            }
//            if (last != null) {
//                boolean result = file.setLastModified(last);
//                if (LOG.isTraceEnabled()) {
//                    LOG.trace("Keeping last modified timestamp: {} on file: {} with result: {}", new Object[]{last, file, result});
//                }
//            }
        }
    }

//    private boolean writeFileByLocalWorkPath(File source, File file) throws IOException {
//        logger.trace("Using local work file being renamed from: "+source.fileName()+" to: "+file.fileName());
//        return renameFile(source.fileName(), file.fileName());
//    }

    private void writeFileByFile(java.io.File source, File target) {
        
        FileReader reader = null;
        try {
        	/*
        	 * Write files out to 1024 chunks to make sure you're 
        	 * not writing files out too big. 
        	 * 
        	 *TODO: Use a Java lib for this...
        	 */
        	FileInputStream inputStream = new FileInputStream(source);
        	Long fileSize = source.length();
        	byte[] buffer = new byte[1024];
        	int total = 0; 
        	int nRead = 0; 
        	while((nRead = inputStream.read(buffer)) != -1){
        		if(nRead<1024){
        			byte[] smallerBuffer = new byte[nRead];
        			System.arraycopy(buffer, 0, smallerBuffer, 0, nRead);
        			target.write(smallerBuffer);
        		}else{
            		target.write(buffer);        			
        		}
        		total+=nRead;
        	}
        	logger.debug("Chunks wrote "+total);
        } catch (FileNotFoundException e) {
            throw new GenericFileOperationFailedException("FileNotFoundException:", e);
        } catch (java.io.IOException e) {
            throw new GenericFileOperationFailedException("java.io.IOException:", e);
        } catch (IOException e) {
            throw new GenericFileOperationFailedException("IOException:", e);
        } finally{
            try {
                target.close();
                if(reader != null){
                    reader.close();
                }
            } catch (FileException e) {
                throw new GenericFileOperationFailedException("FileException:", e);
            } catch (java.io.IOException e) {
                throw new GenericFileOperationFailedException("java.io.IOException:", e);
            }
            
        }
    }
    
    

    private void writeFileByStream(InputStream in, File target) throws IOException {
        try {
            
            logger.debug("Using InputStream to write file: "+target.fileName());
            int size = endpoint.getBufferSize();
            byte[] buffer = new byte[size];
            ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                if (bytesRead < size) {
                    byteBuffer.limit(bytesRead);
                }
                target.write(byteBuffer.array());
                byteBuffer.clear();
            }
        } catch (java.io.IOException e) {
            throw new GenericFileOperationFailedException("java.io.IOException:", e);
        } finally {
            try {
                target.close();
            } catch (FileException e) {
                throw new GenericFileOperationFailedException("FileException:", e);
            }
        }
    }
    
    
    
    private void writeFileByReaderWithCharset(Reader in, File target, String charset) throws IOException {

        try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                
                int b;
                while( (b = in.read()) != -1){
                    bos.write(b);    
                }
                
                target.write(bos.toByteArray());
        } catch (FileNotFoundException e) {
            throw new GenericFileOperationFailedException("FileNotFoundException:", e);
        } catch (java.io.IOException e) {
            throw new GenericFileOperationFailedException("java.io.IOException:", e);
        } catch (IOException e) {
            throw new GenericFileOperationFailedException("IOException:", e);
        } finally{
            try {
                target.close();
                if(in != null){
                    in.close();
                }
            } catch (FileException e) {
                throw new GenericFileOperationFailedException("FileException:", e);
            } catch (java.io.IOException e) {
                throw new GenericFileOperationFailedException("java.io.IOException:", e);
            }
        }        
    }
    
    
    
//
//    /**
//     * Creates and prepares the output file channel. Will position itself in correct position if the file is writable
//     * eg. it should append or override any existing content.
//     */
//    private FileChannel prepareOutputFileChannel(java.io.File target, FileChannel out) throws IOException {
//        if (endpoint.getFileExist() == GenericFileExist.Append) {
//            out = new RandomAccessFile(target, "rw").getChannel();
//            out = out.position(out.size());
//        } else {
//            // will override
//            out = new FileOutputStream(target).getChannel();
//        }
//        return out;
//    }    

    public FileSystem getFileManager() {
        return fileManager;
    }

    public void setFileManager(FileSystem fileManager) {
        this.fileManager = fileManager;
    }

    public boolean createNewFile(String lockFileName) {
        File file = null;
        try {
        	logger.debug("Creating a lock lock file "+lockFileName);
            file = fileManager.create(lockFileName);
            return true;
        } catch (InvalidFileName e) {
            throw new GenericFileOperationFailedException("InvalidFileName:", e);
        } catch (FileException e) {
            throw new GenericFileOperationFailedException("FileException:", e);
        } finally{
            try {
                if(file != null){
                    file.close();
                }
            } catch (FileException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void releaseRetreivedFileResources(Exchange exchange) throws GenericFileOperationFailedException {
        logger.trace("RELEASING FILE RESOURCES");
    }
    
}
