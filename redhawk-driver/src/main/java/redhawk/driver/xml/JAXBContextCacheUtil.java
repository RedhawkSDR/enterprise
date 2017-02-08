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
package redhawk.driver.xml;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

public class JAXBContextCacheUtil {
	private static Logger logger = Logger.getLogger(JAXBContextCacheUtil.class.getName());
	
	private static JAXBContextCacheUtil instance = null; 
	
	private Map<String, JAXBContext> jaxbContextCache = new HashMap<>(); 
	
	protected JAXBContextCacheUtil(){
		
	}
	
	public static JAXBContextCacheUtil getInstance(){
		if(instance==null){
			instance = new JAXBContextCacheUtil(); 
		}
		
		return instance;
	}
	
	public JAXBContext getContext(String schemaContextPath) throws JAXBException{
		if(!jaxbContextCache.containsKey(schemaContextPath)){
			logger.fine("Creating new context");
			JAXBContext context = JAXBContext.newInstance(schemaContextPath, JAXBContextCacheUtil.class.getClassLoader());
			jaxbContextCache.put(schemaContextPath, context); 
			return context; 
		}else{
			logger.fine("Using context in cache"); 
			return jaxbContextCache.get(schemaContextPath);
		}
	}
	
	
}