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
package redhawk.camel.components.dataxml;

import java.util.ArrayList;
import java.util.List;

import BULKIO.PortStatistics;

    public class LinkStatistics {
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
        public LinkStatistics() {
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
