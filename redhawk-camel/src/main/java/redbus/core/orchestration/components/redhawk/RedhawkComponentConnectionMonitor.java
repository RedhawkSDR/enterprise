package redbus.core.orchestration.components.redhawk;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import org.apache.camel.Endpoint;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redbus.core.orchestration.components.redhawk.endpoints.RedhawkDataXmlEndpoint;
import CF.DeviceManager;

public class RedhawkComponentConnectionMonitor extends TimerTask {

    private RedhawkComponent component;
    private static Log logger = LogFactory.getLog(RedhawkComponentConnectionMonitor.class);
    
    private boolean timerDisabledEndpoints = false;
    private List<String> stoppedEndpoints = new ArrayList<String>();
    
    
    public RedhawkComponentConnectionMonitor(RedhawkComponent component){
        this.component = component;
    }
    
    @Override
    public void run() {

//        RedhawkConnection connection = component.getRedhawkIntegration().getRedhawkConnection();
//        
//        if(!connection.isConnected()){
//            for(Endpoint endpoint : component.getCreatedEndpoints()){
//                try {
//                    if(!timerDisabledEndpoints){
//                        logger.info("Not connected to REDHAWK stopping endpoint: " + endpoint.getEndpointUri());
//                        endpoint.stop();
//                        timerDisabledEndpoints = true;
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        } else {
//            if(timerDisabledEndpoints){
//                for(Endpoint endpoint : component.getCreatedEndpoints()){
//                    try {
//                        logger.info("Reconnected to REDHAWK starting endpoint: "+ endpoint.getEndpointUri());
//                        endpoint.start();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }                
//
//                timerDisabledEndpoints = false;
//            }
//            
//            if(connection.isConnected()){
//                for(Endpoint endpoint : component.getCreatedEndpoints()){
//                    if(endpoint instanceof RedhawkDataXmlEndpoint){
//                        
//                        RedhawkDataXmlEndpoint dataXmlEndpoint = (RedhawkDataXmlEndpoint) endpoint;
//                        
//                        String currentDeviceManager = dataXmlEndpoint.getDeviceManagerName();
//                        boolean found = false;
//                        
//                        logger.info("CHECKING HERE: " + currentDeviceManager);
//                        
//                        try {
//                                if(connection.isConnected() && connection.getDomainManager() != null && !connection.getDomainManager()._non_existent()){
//                                    if(connection.getDomainManager().deviceManagers() != null){
//                                        for(DeviceManager deviceManager : connection.getDomainManager().deviceManagers()){
//                                            if(deviceManager.label().equalsIgnoreCase(currentDeviceManager)){
//                                                found = true;
//                                                break;
//                                            }
//                                        }
//                                    }
//                                }
//                        } catch(Exception e){
//                            //logger.info("CAUGHT YA!!!!!");
//                        }
//                        
//                        if(!found){
//                            if(!dataXmlEndpoint.isStopped() && !dataXmlEndpoint.isStopping()){
//                                try {
//                                    logger.info("COULD NOT FIND DEVICE MANAGER.... STOPPING ENDPOINT================================");
//                                    endpoint.stop();
//                                    stoppedEndpoints.add(endpoint.getEndpointUri());
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        } else {
//                            if(!dataXmlEndpoint.isStarted() && !dataXmlEndpoint.isStarting() && stoppedEndpoints.contains(endpoint.getEndpointUri())){
//                                try {
//                                    logger.info("RESTARTING ENDPOINT================================");
//                                    endpoint.start();
//                                    stoppedEndpoints.remove(endpoint.getEndpointUri());
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }                        
//                        }
//                    }
//                }
//            }
//        }
    }
}