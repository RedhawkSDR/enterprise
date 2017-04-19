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
package redhawk.testutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

import redhawk.driver.base.RedhawkFileSystem;

/**
 * Utility class for holding any reusable test functions from the RedhawkDriver
 * in other components. 
 */
public class RedhawkTestUtils {
	/**
	 * Use this method to write a Java component to your CF.FileSystem
	 * 
	 * @param directory
	 * 	Location of your component directory
	 * @param rhFS
	 * 	FileSystem object
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void writeJavaComponentToCF(String directory, RedhawkFileSystem rhFS) throws FileNotFoundException, IOException{
		//Write all the xml files at the root level to CF
		File directoryObj = new File(directory);
		
		//Find all xml files
		File[] xmlFiles = directoryObj.listFiles((d, name) -> name.endsWith(".xml"));
		String rootPath = null;
		
		//Write all the .xml files
		for(File file : xmlFiles){
			String fileName = file.getName();
			String absoluteParentDir = file.getParent();
			rootPath = absoluteParentDir.substring(absoluteParentDir.lastIndexOf('/')+1, absoluteParentDir.length());
			
			String destFile = "/components/"+rootPath+"/"+fileName;
			rhFS.writeFile(new FileInputStream(file), destFile);
		}
		
		/*
		 * Write the jar and startJava.sh to it's appropriate location
		 */
		String[] extensions = new String[]{"jar", "sh"};
		
		Collection<File> moreFiles = FileUtils.listFiles(new File(directory), extensions, true);
		
		for(File file : moreFiles){
			String destFile = "/components/"+file.getAbsolutePath().substring(file.getAbsolutePath().indexOf(rootPath));
			
			//Ignore build.sh 
			if(!destFile.endsWith("build.sh")){
				rhFS.writeFile(new FileInputStream(file), destFile);
			}
		}
	}
}
