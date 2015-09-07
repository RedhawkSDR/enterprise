package redbus.core.orchestration.components.redhawk.endpoints;

import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.Processor;
import org.apache.camel.impl.ProcessorEndpoint;

import redhawk.driver.RedhawkDriver;

@Deprecated
public class RedhawkFileReaderEndpoint extends ProcessorEndpoint {
    private String scaFilePath;
    private RedhawkDriver redhawkDriver;
    
    public RedhawkFileReaderEndpoint() {
        super();
    }
    public RedhawkFileReaderEndpoint(String endpointUri, CamelContext context, Processor processor) {
        super(endpointUri, context, processor);
    }
    public RedhawkFileReaderEndpoint(String endpointUri, Component component, Processor processor) {
        super(endpointUri, component, processor);
    }
    public RedhawkFileReaderEndpoint(String endpointUri, Component component) {
        super(endpointUri, component);
    }
    public String getScaFilePath() {
        return scaFilePath;
    }
    public void setScaFilePath(String scaFilePath) {
        this.scaFilePath = scaFilePath;
    }
	public RedhawkDriver getRedhawkDriver() {
		return redhawkDriver;
	}
	public void setRedhawkDriver(RedhawkDriver redhawkDriver) {
		this.redhawkDriver = redhawkDriver;
	}
    

}
