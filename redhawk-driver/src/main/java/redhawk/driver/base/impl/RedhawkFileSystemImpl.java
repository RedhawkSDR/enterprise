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
package redhawk.driver.base.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import CF.File;
import CF.FileException;
import CF.FileSystem;
import CF.InvalidFileName;
import CF.OctetSequenceHolder;
import CF.FileSystemPackage.FileInformationType;
import CF.FileSystemPackage.FileType;
import redhawk.driver.base.RedhawkFileSystem;

public class RedhawkFileSystemImpl implements RedhawkFileSystem {

	private FileSystem fileSystem;
	
	public RedhawkFileSystemImpl(FileSystem fileSystem){
		this.fileSystem = fileSystem;
	}
	
	public List<String> findFilesInDirectory(String directory, String pattern){
		List<String> files = new ArrayList<String>();
		if(!directory.endsWith("/")){
			directory = directory + "/";
		}
		
		return internalFindFiles(files, directory, pattern);
	}
	
	public List<String> findDirectoriesInDirectory(String directory, String pattern){
		List<String> directories = new ArrayList<String>();
		if(!directory.endsWith("/")){
			directory = directory + "/";
		}
		
		return internalFindDirectories(directories, directory, pattern);
	}	
	
	
	public List<String> findFiles(String pattern){
		List<String> files = new ArrayList<String>();
		return internalFindFiles(files, "/", pattern);
	}
	
	public List<String> findDirectories(String pattern){
		List<String> directories = new ArrayList<String>();
		return internalFindDirectories(directories, "/", pattern);
	}
	
	protected List<String> internalFindFiles(List<String> files, String dir, String pattern) {
		try {
			for(FileInformationType info : fileSystem.list(dir)){
				if(info.kind.value() == FileType._DIRECTORY){
					String newDir = dir + info.name + "/";
					internalFindFiles(files, newDir, pattern);
				} else {
					if(info.name.matches(pattern)){
						files.add(dir+info.name);
					}
				}
			}
		} catch(FileException f){
//			f.printStackTrace();
		} catch (InvalidFileName e) {
//			e.printStackTrace();
		}
		
		return files;
	}
	
	
	public List<String> internalFindDirectories(List<String> directories, String dir, String pattern) {
		try {
			for(FileInformationType info : fileSystem.list(dir)){
				if(info.kind.value() == FileType._DIRECTORY){
					if(info.name.matches(pattern)){
						directories.add(dir+info.name);
					}
					String newDir = dir + info.name + "/";
					internalFindDirectories(directories, newDir, pattern);
				}
			}
		} catch(FileException f){
			f.printStackTrace();
		} catch (InvalidFileName e) {
			e.printStackTrace();
		}
		
		return directories;
	}
	
	
	public void removeFile(String filePath) throws IOException {
		try {
			fileSystem.remove(filePath);
		} catch (FileException | InvalidFileName e) {
			throw new IOException(e);
		}
	}
	
	public void removeDirectory(String directoryPath) throws IOException {
		try {
			//Must remove files in directory before actual directory is removed
			for(String file : this.findFilesInDirectory(directoryPath, ".*")){
				removeFile(file);
			}
			fileSystem.rmdir(directoryPath);
		} catch (FileException | InvalidFileName e) {
			throw new IOException("Unable to remove directory at this location: "+directoryPath);
		}
	}
	
	public byte[] getFile(String filePath) throws IOException {
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		File file = null;
		try {
			file = fileSystem.open(filePath, true);
			
	        OctetSequenceHolder holder = new OctetSequenceHolder();
	        
	        int fileSize = file.sizeOf();
	        int readSize = 1024*2000;
	        
	        for(int i=0; i < (fileSize/readSize)+1; i++){
	            file.read(holder, readSize);
	            bos.write(holder.value);
	        }
		} catch (FileException | InvalidFileName | CF.FilePackage.IOException | IOException e) {
			throw new IOException(e);
		} finally{
			bos.close();
			if(file != null){
				try {
					file.close();
				} catch (FileException e) {
					throw new IOException(e);
				}
			}
		}
		
        return bos.toByteArray();
	}
	
	public void writeFile(InputStream inputStream, String destFile) throws IOException {
		  
	      CF.File file;
	      try {
	    	  
	    	  //Check to see if File is already on disk 
	    	  if(fileSystem.exists(destFile)){
	    		  throw new IOException("Destination File: " + destFile + " already Exists");
	    	  }
	    	  
	    	  //Check to see if parent directory already exists
	    	  if(!fileSystem.exists(destFile.substring(0, destFile.lastIndexOf(java.io.File.separator)))) {
	    		  fileSystem.mkdir(destFile.substring(0, destFile.lastIndexOf(java.io.File.separator)));
	    	  }
	    	  
	    	  //Create a CF.File
	          file = fileSystem.create(destFile);
	      } catch (InvalidFileName e1) {
	          throw new IOException("Unable to create new file. InvalidFileName: " + e1);
	      } catch (FileException e1) {
	          throw new IOException("Unable to create new file. FileException: " + e1);
	      }

	      /*
	       * Write to CF.File
	       */
	      try {
	          int size = (int) inputStream.available();
	          byte[] buffer = new byte[size];
	          ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
	          int bytesRead;
	          while ((bytesRead = inputStream.read(buffer)) != -1) {
	              if (bytesRead < size) {
	                  byteBuffer.limit(bytesRead);
	              }
	              file.write(byteBuffer.array());
	              byteBuffer.clear();
	          }
	      } catch (IOException e) {
	          throw new IOException("Java IOException:", e);
	      } catch (CF.FilePackage.IOException e) {
	          throw new IOException("REDHAWK IOException:", e);
		} finally {
	          try {
	        	  inputStream.close();
	              file.close();
	          } catch (FileException e) {
	              throw new IOException("FileException:", e);
	          }
	      }
	  }
	
}