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
package redhawk.driver.base;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Helper class for interacting with File in a REDHAWK File System. 
 *
 */
public interface RedhawkFileSystem {
	/**
	 * Use a pattern to find all the files in a specific REDHAWK directory. 
	 * @param directory
	 * 	directory name to look in. 
	 * @param pattern
	 * 	Pattern for the files to match. 
	 * @return
	 */
	List<String> findFilesInDirectory(String directory, String pattern);
	
	/**
	 * Use a pattern to find all the directories in a REDHAWK directory. 
	 * @param directory
	 * 	directory name to look in for other directories. 
	 * @param pattern
	 * 	Pattern for the directories. 
	 * @return
	 */
	List<String> findDirectoriesInDirectory(String directory, String pattern);
	
	/**
	 * Find all files in $SDRROOT matching a specific pattern.  
	 * @param pattern
	 * 	pattern for files.
	 * @return
	 */
	List<String> findFiles(String pattern);
	
	/**
	 * Find all directories matching a specific pattern in $SDRROOT
	 * @param pattern
	 * 	pattern for 
	 * @return
	 */
	List<String> findDirectories(String pattern);
	
	/**
	 * Remove a file from $SDRROOT
	 * 
	 * @param filePath
	 * 	Path to file to be removed. 
	 * @throws IOException
	 */
	void removeFile(String filePath) throws IOException;
	
	/**
	 * Remove a directory from $SDRROOT
	 * 
	 * @param directoryPath
	 * 	path to directory. 
	 * @throws IOException
	 */
	void removeDirectory(String directoryPath) throws IOException; 
	
	/**
	 * Get a file at a specific Path in $SDRROOT.
	 * 
	 * @param filePath
	 * 	Path to file
	 * @return
	 * 	byte representation of a file. 
	 * @throws IOException
	 */
	byte[] getFile(String filePath) throws IOException;
	
	/**
	 * Write a file to $SDRROOT. 
	 * @param inputStream
	 * 	Stream representing file 
	 * @param destFile
	 * 	location in $SDRROOT for file to be stored. 
	 * @throws IOException
	 */
	void writeFile(InputStream inputStream, String destFile) throws IOException;
}
