package redbus.core.orchestration.components.redhawk.dataxml;

import java.util.UUID;

import org.apache.camel.CamelException;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.Servant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import CF.DeviceManager;
import CF.PortSupplier;
import CF.PortSupplierHelper;
import CF.PortSupplierPOATie;
import CF.LifeCyclePackage.InitializeError;
import redbus.core.orchestration.components.redhawk.RedhawkComponent;
import redbus.core.orchestration.components.redhawk.endpoints.RedhawkDataXmlEndpoint;
import redhawk.driver.RedhawkDriver;
import redhawk.driver.devicemanager.RedhawkDeviceManager;
import redhawk.driver.domain.RedhawkDomainManager;
@Deprecated
public class RedhawkDataXmlProducer extends DefaultProducer {
    private static final transient Logger logger = LoggerFactory.getLogger(RedhawkDataXmlProducer.class);

    private PortSupplier pipeline;
    private DataXmlOutputImpl service;
    private RedhawkDataXmlEndpoint endpoint;
    
    private RedhawkDeviceManager currentDeviceManager;
    
    public RedhawkDataXmlProducer(RedhawkDataXmlEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    public void process(Exchange exchange) throws Exception {
        if(currentDeviceManager != null){
            Object streamId = exchange.getProperty("RedhawkStreamId");
            
            if(streamId == null){
                streamId = UUID.randomUUID().toString();
            }
            
            if(service != null){
                service.pushPacket(exchange.getIn().getBody()+"", true, streamId+"");    
            }
        } else {
            throw new CamelException("Not Connected to REDHAWK endpoint: " + endpoint.getEndpointUri());
        }
    }

    
    @Override
    protected void doStart() throws Exception {
        super.doStart();
        
        RedhawkDriver driver = ((RedhawkComponent) endpoint.getComponent()).getRedhawkDriver();
        String domainName = ((RedhawkComponent) endpoint.getComponent()).getDomainName();
        RedhawkDomainManager domainManager = driver.getDomain(domainName);
        
        
        if(endpoint.getDeviceManagerName() != null){
        	currentDeviceManager = domainManager.createDeviceManager(endpoint.getDeviceManagerName(), "redbus.base", true);
        } else {
        	if(domainManager.getDeviceManagers().size() > 0){
        		currentDeviceManager = domainManager.getDeviceManagers().get(0);
        	} else {
        		currentDeviceManager = domainManager.createDeviceManager("REDBUS", "redbus.base", true);
        	}
        }
        
        
        DeviceManager deviceManager = currentDeviceManager.getCorbaObject();
        
        if(deviceManager != null && !deviceManager._non_existent()){
            POA rootpoa = POAHelper.narrow(driver.getOrb().resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();
            PortSupplierImpl rhPortSupplier = new PortSupplierImpl(rootpoa);
            PortSupplierPOATie tie = new PortSupplierPOATie(rhPortSupplier, rootpoa);
            tie._this(driver.getOrb());
            CF.PortSupplier portSupplier = PortSupplierHelper.narrow(rootpoa.servant_to_reference((Servant)tie));
            service = rhPortSupplier.getPort_dataXML_out();
            pipeline = portSupplier;
            deviceManager.registerService(pipeline, endpoint.getServiceName());

            try {
                rhPortSupplier.initialize();
            } catch (InitializeError e) {
                e.printStackTrace();
            }     
        } else {
            throw new CamelException("No Device Managers were found for this domain");
        }        
        
    }

    
    @Override
    protected void doStop() throws Exception {
        super.doStop();
        DeviceManager deviceManager = currentDeviceManager.getCorbaObject();
        if(deviceManager != null && !deviceManager._non_existent()){
            deviceManager.unregisterService(pipeline, endpoint.getServiceName());
        } else {
            throw new CamelException("No Device Managers were found for this domain");
        }        
    }
    
    
}
