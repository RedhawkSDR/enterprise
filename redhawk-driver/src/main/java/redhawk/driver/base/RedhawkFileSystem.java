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

public interface RedhawkFileSystem {

	List<String> findFilesInDirectory(String directory, String pattern);
	List<String> findDirectoriesInDirectory(String directory, String pattern);
	List<String> findFiles(String pattern);
	List<String> findDirectories(String pattern);
	
	void removeFile(String filePath) throws IOException;
	void removeDirectory(String directoryPath) throws IOException; 
	byte[] getFile(String filePath) throws IOException;
	void writeFile(InputStream inputStream, String destFile) throws IOException;
}
