package redbus.core.orchestration.components.redhawk.filereader;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redbus.core.orchestration.components.redhawk.RedhawkComponent;
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