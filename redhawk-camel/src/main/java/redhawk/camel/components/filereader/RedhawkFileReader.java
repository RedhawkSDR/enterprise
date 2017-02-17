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
package redhawk.camel.components.filereader;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redhawk.camel.components.RedhawkComponent;
import redhawk.driver.domain.RedhawkDomainManager;

@Deprecated
public class RedhawkFileReader implements Processor {

    private String filePath;
    private RedhawkComponent component;
    
    
    @SuppressWarnings("unused")
    private static Log logger = LogFactory.getLog(RedhawkFileReader.class);

    
    public RedhawkFileReader(RedhawkComponent component){
    	this.component = component;
    }
    
    
    public void process(Exchange exchange) throws Exception {

        String fileLocationFromHeader = exchange.getIn().getHeader("scaFilePath")+"";
        
        if(!fileLocationFromHeader.equalsIgnoreCase("null")){
            filePath = fileLocationFromHeader;
        }
        
        if(filePath.equalsIgnoreCase("null")){
            throw new IllegalArgumentException("scaFilePath was not specified.");
        }
        
        String domainName = component.getDomainName();
		RedhawkDomainManager domainManager = component.getRedhawkDriver().getDomain(domainName);
		byte[] fileInBytes = domainManager.getFileManager().getFile(filePath);
		
		String scaFileName = filePath.substring(filePath.lastIndexOf(java.io.File.separatorChar)+1, filePath.length());
		exchange.getIn().setHeader("scaFileName", scaFileName);
		exchange.getIn().setBody(fileInBytes);
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}