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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.ossie.component.UsesPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import BULKIO.PortStatistics;
import BULKIO.PortUsageType;
import BULKIO.StreamSRI;
import BULKIO.UsesPortStatistics;
import BULKIO.dataXMLHelper;
import BULKIO.dataXMLOperations;
import CF.DataType;
import ExtendedCF.UsesConnection;

public class DataXmlOutputImpl extends UsesPort<dataXMLOperations> {

    private static final transient Logger logger = LoggerFactory.getLogger(DataXmlOutputImpl.class);

    
    /**
     * @generated
     */
    protected String dataOut;

    /**
     * @generated
     */
    protected boolean refreshSRI;
    
    /**
     * Map of connection Ids to port objects
     * @generated
     */
    protected Map<String, dataXMLOperations> outConnections = new HashMap<String, dataXMLOperations>();

    /**
     * Map of connection ID to statistics
     * @generated
     */
    protected Map<String, LinkStatistics> stats;

    /**
     * Map of stream IDs to streamSRI's
     * @generated
     */
    protected Map<String, StreamSRI> currentSRIs;

    /**
     * @generated
     */
    public DataXmlOutputImpl(String portName) 
    {
        super(portName);

        this.outConnections = new HashMap<String, dataXMLOperations>();
        this.stats = new HashMap<String, LinkStatistics>();
        this.currentSRIs = new HashMap<String, StreamSRI>();

        //begin-user-code
        //end-user-code
    }

    /**
     * @generated
     */
    public PortUsageType state() {
        PortUsageType state = PortUsageType.IDLE;

        if (this.outConnections.size() > 0) {
            state = PortUsageType.ACTIVE;
        }

        //begin-user-code
        //end-user-code

        return state;
    }

    /**
     * @generated
     */
    public void enableStats(final boolean enable)
    {
        for (String connId : outConnections.keySet()) {
            stats.get(connId).enableStats(enable);
        }
    };

    /**
     * @generated
     */
    public UsesPortStatistics[] statistics() {
        UsesPortStatistics[] portStats = new UsesPortStatistics[this.outConnections.size()];
        int i = 0;
        
        synchronized (this.updatingPortsLock) {
            for (String connId : this.outConnections.keySet()) {
                portStats[i] = new UsesPortStatistics(connId, this.stats.get(connId).retrieve());
            }
        }
        
        return portStats;
    }

    /**
     * @generated
     */
    public StreamSRI[] activeSRIs() 
    {
        return this.currentSRIs.values().toArray(new StreamSRI[0]);
    }

    /**
     * @generated
     */
    public String getName() {
        return this.name;
    }
 
    /**
     * pushSRI
     *     description: send out SRI describing the data payload
     *
     *  H: structure of type BULKIO::StreamSRI with the SRI for this stream
     *    hversion
     *    xstart: start time of the stream
     *    xdelta: delta between two samples
     *    xunits: unit types from Platinum specification
     *    subsize: 0 if the data is one-dimensional
     *    ystart
     *    ydelta
     *    yunits: unit types from Platinum specification
     *    mode: 0-scalar, 1-complex
     *    streamID: stream identifier
     *    sequence<CF::DataType> keywords: unconstrained sequence of key-value pairs for additional description
     * @generated
     */
    public void pushSRI(StreamSRI header) 
    {
        // Header cannot be null
        if (header == null) return;
        // Header cannot have null keywords
        if (header.keywords == null) header.keywords = new DataType[0];

        synchronized(this.updatingPortsLock) {    // don't want to process while command information is coming in
            if (this.active) {
                //begin-user-code
                //end-user-code

                for (dataXMLOperations p : this.outConnections.values()) {
                    try {
                        p.pushSRI(header);
                    } catch(Exception e) {
                        System.out.println("Call to pushSRI by BULKIO_dataXMLOutPort failed");
                    }
                }
            }

            //begin-user-code
            //end-user-code
            
            this.currentSRIs.put(header.streamID, header);
            this.refreshSRI = false;

            //begin-user-code
            //end-user-code
        }    // don't want to process while command information is coming in

        return;
    }
        
    /**
     * @generated
     */
    public void pushPacket(String stringData, boolean endOfStream, String streamID) 
    {
        
        if (this.refreshSRI && this.currentSRIs.containsKey(streamID)) {
            this.pushSRI(this.currentSRIs.get(streamID));
        }
        
        synchronized(this.updatingPortsLock) {    // don't want to process while command information is coming in
            this.dataOut = stringData;
            if (this.active) {
                //begin-user-code
                //end-user-code
                
                for (Entry<String, dataXMLOperations> p : this.outConnections.entrySet()) {
                    try {
                        p.getValue().pushPacket(this.dataOut, endOfStream, streamID);
                        this.stats.get(p.getKey()).update(this.dataOut.length(), 0, endOfStream, streamID);
                    } catch(Exception e) {
                        logger.error("Call to pushPacket by BULKIO_dataXMLOutPort failed: "+ e.getMessage());
                    }
                }

                //begin-user-code
                //end-user-code
            } else {
                logger.warn("PORT NOT ACTIVE");
            }
        }    // don't want to process while command information is coming in
        
        return;
    }

    protected dataXMLOperations narrow(org.omg.CORBA.Object connection)  
    {
        dataXMLOperations ops = dataXMLHelper.narrow(connection);
        
        //begin-user-code 
        //end-user-code 
        
        return ops; 
    } 

    /**
     * @generated
     */
    public void connectPort(final org.omg.CORBA.Object connection, final String connectionId) throws CF.PortPackage.InvalidPort, CF.PortPackage.OccupiedPort
    {

        synchronized (this.updatingPortsLock) {
            super.connectPort(connection, connectionId);
            final dataXMLOperations port = dataXMLHelper.narrow(connection);
            this.outConnections.put(connectionId, port);
            this.active = true;
            this.stats.put(connectionId, new LinkStatistics());
            this.refreshSRI = true;
        
            //begin-user-code
            //end-user-code
        }
    }

    /**
     * @generated
     */
    public void disconnectPort(String connectionId) {
        synchronized (this.updatingPortsLock) {
            super.disconnectPort(connectionId);
            dataXMLOperations port = this.outConnections.remove(connectionId);
            this.stats.remove(connectionId);
            this.active = (this.outConnections.size() != 0);

            //begin-user-code
            //end-user-code
        }
    }

    /**
     * @generated
     */
    public UsesConnection[] connections() {
        final UsesConnection[] connList = new UsesConnection[this.outConnections.size()];
        int i = 0;
        synchronized (this.updatingPortsLock) {
            for (Entry<String, dataXMLOperations> ent : this.outConnections.entrySet()) {
                connList[i++] = new UsesConnection(ent.getKey(), (org.omg.CORBA.Object) ent.getValue());
            }
        }
        return connList;
    }

    /**
     * @generated
     */
    public class statPoint implements Cloneable {
        /** @generated */
        int elements;
        /** @generated */
        float queueSize;
        /** @generated */
        double secs;
    }
    
    /**
     * @generated
     */
    public class linkStatistics {
        /** @generated */
        protected double bitSize;
        /** @generated */
        protected PortStatistics runningStats;
        /** @generated */
        protected StatPoint[] receivedStatistics;
        /** @generated */
        protected List<String> activeStreamIDs;
        /** @generated */
        protected final int historyWindow;
        /** @generated */
        protected int receivedStatistics_idx;
        /** @generated */
        protected boolean enabled;

        /**
         * @generated
         */
        public linkStatistics() {
            this.enabled = true;
            this.bitSize = 1.0 * 8.0;
            this.historyWindow = 10;
            this.receivedStatistics_idx = 0;
            this.receivedStatistics = new StatPoint[historyWindow];
            this.activeStreamIDs = new ArrayList<String>();
            this.runningStats = new PortStatistics();
            this.runningStats.elementsPerSecond = -1.0f;
            this.runningStats.bitsPerSecond = -1.0f;
            this.runningStats.callsPerSecond = -1.0f;
            this.runningStats.averageQueueDepth = -1.0f;
            this.runningStats.streamIDs = new String[0];
            this.runningStats.timeSinceLastCall = -1.0f;
            for (int i = 0; i < historyWindow; ++i) {
                this.receivedStatistics[i] = new StatPoint();
            }
        }

        /**
         * @generated
         */
        public void setBitSize(double bitSize) {
            this.bitSize = bitSize;
        }

        /**
         * @generated
         */
        public void enableStats(boolean enable) {
            this.enabled = enable;
        }

        /**
         * @generated
         */
        public void update(int elementsReceived, float queueSize, boolean EOS, String streamID) {
            if (!this.enabled) {
                return;
            }
            long millis = System.currentTimeMillis();
            this.receivedStatistics[this.receivedStatistics_idx].elements = elementsReceived;
            this.receivedStatistics[this.receivedStatistics_idx].queueSize = queueSize;
            this.receivedStatistics[this.receivedStatistics_idx++].secs = millis / 1000.0;
            this.receivedStatistics_idx = this.receivedStatistics_idx % this.historyWindow;
            if (!EOS) {
                if (!this.activeStreamIDs.contains(streamID)) {
                    this.activeStreamIDs.add(streamID);
                }
            } else {
                this.activeStreamIDs.remove(streamID);
            }
        }

        /**
         * @generated
         */
        public PortStatistics retrieve() {
            if (!this.enabled) {
                return null;
            }
            long millis = System.currentTimeMillis();
            double secs = millis / 1000.0;
            int idx = (this.receivedStatistics_idx == 0) ? (this.historyWindow - 1) : (this.receivedStatistics_idx - 1) % this.historyWindow;
            double front_sec = this.receivedStatistics[idx].secs;
            double totalTime = secs - this.receivedStatistics[this.receivedStatistics_idx].secs;
            double totalData = 0;
            float queueSize = 0;
            int startIdx = (this.receivedStatistics_idx + 1) % this.historyWindow;
            for (int i = startIdx; i != receivedStatistics_idx; ) {
                totalData += this.receivedStatistics[i].elements;
                queueSize += this.receivedStatistics[i].queueSize;
                i = (i + 1) % this.historyWindow;
            }
            int receivedSize = receivedStatistics.length;
            synchronized (this.runningStats) {
                this.runningStats.timeSinceLastCall = (float)(secs - front_sec);
                this.runningStats.bitsPerSecond = (float)((totalData * this.bitSize) / totalTime);
                this.runningStats.elementsPerSecond = (float)(totalData / totalTime);
                this.runningStats.averageQueueDepth = (float)(queueSize / receivedSize);
                this.runningStats.callsPerSecond = (float)((receivedSize - 1) / totalTime);
                this.runningStats.streamIDs = this.activeStreamIDs.toArray(new String[0]);
            }
            return runningStats;
        }
    }

}
