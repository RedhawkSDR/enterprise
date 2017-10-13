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
package redhawk.camel.components;

import static org.apache.camel.util.ObjectHelper.isNotEmpty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.component.file.GenericFileConfiguration;
import org.apache.camel.component.file.GenericFileDefaultSorter;
import org.apache.camel.impl.DefaultComponent;
import org.apache.camel.util.CastUtils;
import org.apache.camel.util.EndpointHelper;
import org.apache.camel.util.ObjectHelper;
import org.apache.camel.util.StringHelper;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redhawk.camel.components.endpoints.RedhawkDataEndpoint;
import redhawk.camel.components.endpoints.RedhawkDataXmlEndpoint;
import redhawk.camel.components.endpoints.RedhawkEventChannelEndpoint;
import redhawk.camel.components.endpoints.RedhawkFileEndpoint;
import redhawk.camel.components.endpoints.RedhawkFileReaderEndpoint;
import redhawk.camel.components.filereader.RedhawkFileReader;
import redhawk.driver.RedhawkDriver;
import redhawk.driver.connectionmanager.impl.RedhawkEndpoint;

/**
 * Represents the component that manages {@link RedhawkEndpoint}.
 */
public class RedhawkComponent extends DefaultComponent {

    protected static Logger logger = LoggerFactory.getLogger(RedhawkComponent.class);
    
    private static final String DATA_SERVICE = "data";
    private static final String DATA_XML_SERVICE = "dataxml";
    private static final String FILE_READER_SERVICE = "file-reader";
    private static final String FILE_MANAGER_SERVICE = "file-manager";
    private static final String EVENT_CHANNEL_SERVICE = "event-channel";
    private List<Endpoint> createdEndpoints = new ArrayList<Endpoint>();

    private Timer connectionMonitorTimer;
    private RedhawkComponentConnectionMonitor connectionMonitor;
    
    private RedhawkDriver redhawkDriver;
    private String domainName;
    
    private boolean newRedhawkDriverInstance = false;
    
    public RedhawkComponent(){
        connectionMonitor = new RedhawkComponentConnectionMonitor(this);
        connectionMonitorTimer = new Timer("CamelRedhawkConnectionMonior");
    }
    
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {

        String[] configParams = remaining.split(":");

        String service = configParams[0];

        if(redhawkDriver == null){
	        if(configParams.length > 3 && configParams.length < 6 ) {
	        	if(configParams.length == 5){
	        		if(configParams[4].startsWith("/")){
	        			parameters.put("directory", configParams[4]);
			        }
			    }
			} else {
				throw new IllegalArgumentException("Invalid URI Syntax:");
			}        
	        
	        String host = configParams[1];
	        String port = configParams[2];
	        String domain = configParams[3];
	        this.domainName = normalizeDomainName(domain);
	        redhawkDriver = new RedhawkDriver(host, Integer.parseInt(port));
	        newRedhawkDriverInstance = true;
        } else {
	        if(configParams.length >= 1 && configParams.length < 3 ) {
	        	if(configParams.length == 2){
	        		if(configParams[1].startsWith("/")){
	        			parameters.put("directory", configParams[1]);
			        }
			    }
			} else {
				//TODO: Figure out what was trying to be accomplished here other than 
				//throw new IllegalArgumentException("Invalid URI Syntax:");
			}           	
        }
        
        Endpoint endpoint = null;
        if(DATA_XML_SERVICE.equalsIgnoreCase(service)){
        	
        	if(parameters.get("serviceName") == null || (parameters.get("serviceName")+"").trim().length() == 0){
        		throw new IllegalArgumentException("No Value Specified for Service Name");
        	}
        	
            endpoint = new RedhawkDataXmlEndpoint(uri, this);
            setProperties(endpoint, parameters);
        } else if(FILE_MANAGER_SERVICE.equalsIgnoreCase(service)){
            
            String fileManagerType = parameters.get("fileManagerType")+"";
            
            if("DEVICE".equalsIgnoreCase(fileManagerType)){
                Validate.notEmpty((String) parameters.get("deviceManagerName"),"Device Manager Name cannot be empty");
            }
            
            RedhawkFileEndpoint redhawkEndpoint = new RedhawkFileEndpoint(uri, this);
            
            Object dirLocation = parameters.get("directory");
            if(dirLocation != null){
                String directory = dirLocation+"";
                
                // the starting directory must be a static (not containing dynamic expressions)
                if (StringHelper.hasStartToken(directory, "simple")) {
                    throw new IllegalArgumentException("Invalid directory: " + directory
                                                       + ". Dynamic expressions with ${ } placeholders is not allowed."
                                                       + " Use the fileName option to set the dynamic expression.");
                }
                
                redhawkEndpoint.setDirectoryName(directory);
            }
            

            GenericFileConfiguration config = new GenericFileConfiguration();
            config.setDirectory(redhawkEndpoint.getDirectoryName());
            redhawkEndpoint.setConfiguration(config);
            
            // sort by using file language
            String sortBy = getAndRemoveParameter(parameters, "sortBy", String.class);
            if (isNotEmpty(sortBy) && !EndpointHelper.isReferenceParameter(sortBy)) {
                // we support nested sort groups so they should be chained
                String[] groups = sortBy.split(";");
                Iterator<String> it = CastUtils.cast(ObjectHelper.createIterator(groups));
                Comparator<Exchange> comparator = createSortByComparator(it);
                redhawkEndpoint.setSortBy(comparator);
            }
            setProperties(redhawkEndpoint.getConfiguration(), parameters);
            setProperties(redhawkEndpoint, parameters);

            endpoint = redhawkEndpoint;            

        } else if(FILE_READER_SERVICE.equalsIgnoreCase(service)){
              RedhawkFileReader processor = new RedhawkFileReader(this);
              processor.setFilePath(parameters.get("scaFilePath")+"");
              endpoint = new RedhawkFileReaderEndpoint(uri, this, processor);
        } else if(EVENT_CHANNEL_SERVICE.equalsIgnoreCase(service)){
            String[] validDataTypeNames = {"messages", "properties", "logEvent"};
            Object dataTypeName = parameters.get("dataTypeName");
            if (dataTypeName == null) {
            	parameters.put("dataTypeName", "messages");
            } else if (Arrays.asList(validDataTypeNames).contains(dataTypeName)) {
            	parameters.put("dataTypeName", dataTypeName.toString());
            } else {
            	throw new IllegalArgumentException(String.format("dataTypeName %s not one of the valid types in %s", dataTypeName, Arrays.toString(validDataTypeNames)));
            }
            
            Validate.notNull(parameters.get("eventChannelName"), "No Value Specified for Event Channel Name");
            Validate.notEmpty(parameters.get("eventChannelName")+"", "No Value Specified for Event Channel Name");
            endpoint = new RedhawkEventChannelEndpoint(uri, this);
            setProperties(endpoint, parameters);
        } else if(DATA_SERVICE.equalsIgnoreCase(service)){
            endpoint = new RedhawkDataEndpoint(uri, this);
            setProperties(endpoint, parameters);
        } else {
            throw new IllegalArgumentException("Service specified by: " + service + " does not exist");
        }
        
        createdEndpoints.add(endpoint);
        
        return endpoint;
    }


    
    @Override
    protected void doStart() throws Exception {
        super.doStart();
        connectionMonitorTimer.scheduleAtFixedRate(connectionMonitor, 5000, 5000);
    }

    @Override
    protected void doStop() throws Exception {
    	connectionMonitorTimer.cancel();
    	
    	//Should always disconnect to clean up threads
    	redhawkDriver.disconnect();
    	
        super.doStop();
    }

    protected Comparator<Exchange> createSortByComparator(Iterator<String> it) {
        if (!it.hasNext()) {
            return null;
        }

        String group = it.next();

        boolean reverse = group.startsWith("reverse:");
        String reminder = reverse ? ifStartsWithReturnRemainder("reverse:", group) : group;

        boolean ignoreCase = reminder.startsWith("ignoreCase:");
        reminder = ignoreCase ? ifStartsWithReturnRemainder("ignoreCase:", reminder) : reminder;

        ObjectHelper.notEmpty(reminder, "sortBy expression", this);

        // recursive add nested sorters
        return GenericFileDefaultSorter.sortByFileLanguage(getCamelContext(), 
            reminder, reverse, ignoreCase, createSortByComparator(it));
    }

    private String normalizeDomainName(String domainNameToNormalize){
		if(domainNameToNormalize.contains("/")){
			return domainNameToNormalize.substring(domainNameToNormalize.indexOf("/")+1, domainNameToNormalize.length());
		} else {
			return domainNameToNormalize;
		}
    }

    public List<Endpoint> getCreatedEndpoints() {
        return createdEndpoints;
    }

	public RedhawkDriver getRedhawkDriver() {
		return redhawkDriver;
	}

	public void setRedhawkDriver(RedhawkDriver redhawkDriver) {
		this.redhawkDriver = redhawkDriver;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

}
