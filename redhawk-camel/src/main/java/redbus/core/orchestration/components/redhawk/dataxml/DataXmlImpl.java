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
package redbus.core.orchestration.components.redhawk.dataxml;


import java.util.Date;
import java.util.UUID;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.spi.ExceptionHandler;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omg.CORBA.ORB;

import BULKIO.PortStatistics;
import BULKIO.PortUsageType;
import BULKIO.StreamSRI;
import BULKIO.dataXMLPOA;

public class DataXmlImpl extends dataXMLPOA {

    private static Log logger = LogFactory.getLog(DataXmlImpl.class);
    
    private ORB orb;
    
    private Endpoint endpoint;
    private Processor processor;
    private ExceptionHandler exceptionHandler;
    
    public DataXmlImpl(Processor processor, Endpoint endpoint, ExceptionHandler exceptionHandler) {
        this.endpoint = endpoint;
        this.processor = processor;
        this.exceptionHandler = exceptionHandler;
    }
    
    
    public ORB getOrb() {
        return orb;
    }

    public void setOrb(ORB orb) {
        this.orb = orb;
    }

    public void pushPacket(String body, boolean endOfStream, String streamId) {
        Exchange exchange = endpoint.createExchange();

        if(StringUtils.isBlank(streamId)){
           streamId = UUID.randomUUID().toString(); 
        }
        
        exchange.setProperty("RedhawkStreamId", streamId);
        
        // create a message body
        exchange.getIn().setBody(body);

        try {
            processor.process(exchange);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if (exchange.getException() != null) {
                exceptionHandler.handleException("Error processing exchange", exchange, exchange.getException());
            }
        }
        
    }

    public PortUsageType state() {
        return null;
    }

    public PortStatistics statistics() {
        return null;
    }

    public StreamSRI[] activeSRIs() {
        return null;
    }

    public void pushSRI(StreamSRI arg0) {
        
    }

}
