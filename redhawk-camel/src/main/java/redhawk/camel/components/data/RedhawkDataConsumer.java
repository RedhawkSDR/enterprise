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
package redhawk.camel.components.data;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redhawk.camel.components.RedhawkComponent;
import redhawk.camel.components.endpoints.RedhawkDataEndpoint;
import redhawk.driver.bulkio.Packet;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.port.PortListener;
import redhawk.driver.port.RedhawkPort;

public class RedhawkDataConsumer extends DefaultConsumer {

    private static Log logger = LogFactory.getLog(RedhawkDataConsumer.class);

    private RedhawkPort port;
    private RedhawkDataEndpoint endpoint;
    private Processor processor;

    private boolean connected = false;
    private boolean forceStop = false;

    public RedhawkDataConsumer(RedhawkDataEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
        this.endpoint = endpoint;
        this.processor = processor;
    }


    @Override
    protected void doStart() throws Exception {
        super.doStart();
        logger.debug("Starting connection: \n"
                + "\tWaveform: " + endpoint.getWaveformName() + "\n"
                + "\tComponent: " + endpoint.getComponentName() + "\n"
                + "\tPort: " + endpoint.getPortName() + "\n"
                + "\tType: " + endpoint.getPortType() + "\n");
        Thread checkerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isStarted() && !forceStop) {
                    checkConnected();
                    try {
                        Thread.currentThread().sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        checkerThread.start();
    }


    private void checkConnected() {
        try {

            String domainName = ((RedhawkComponent) endpoint.getComponent()).getDomainName();
            logger.debug("Looking up domain manager: " + domainName);
            RedhawkDomainManager domainManager = ((RedhawkComponent) endpoint.getComponent()).getRedhawkDriver().getDomain(domainName);
            logger.debug("Found domain manager. Looking up port...");
            port = domainManager.getApplicationByName(endpoint.getWaveformName()).getComponentByName(endpoint.getComponentName()).getPort(endpoint.getPortName());
            logger.debug("Found the port.");
            if (!connected) {

                logger.debug("Attempting to connect to the port...");

                PortListener portListener = new PortListener() {
                    @Override
                    public void onReceive(Packet packet) {

                        if (endpoint.getDecimateTo() > 0 && packet.getData().getClass().isArray()) {
                            packet.decimateTo(endpoint.getDecimateTo());
                        }

                        Exchange exchange = endpoint.createExchange();

                        exchange.getIn().setHeader("streamId", packet.streamId);

                        if (packet.getTime() != null) {
                            exchange.getIn().setHeader("tcmode", packet.tcmode);
                            exchange.getIn().setHeader("tcstatus", packet.tcstatus);
                            exchange.getIn().setHeader("tcfsec", packet.tfsec);
                            exchange.getIn().setHeader("toff", packet.toff);
                            exchange.getIn().setHeader("twsec", packet.twsec);
                        }

                        exchange.getIn().setHeader("eos", packet.endOfStream);
                        exchange.getIn().setHeader("blocking", packet.blocking);
                        exchange.getIn().setHeader("hversion", packet.hversion);
                        exchange.getIn().setHeader("mode", packet.mode);
                        exchange.getIn().setHeader("subsize", packet.subsize);
                        exchange.getIn().setHeader("xdelta", packet.xdelta);
                        exchange.getIn().setHeader("xstart", packet.xstart);
                        exchange.getIn().setHeader("xunits", packet.xunits);
                        exchange.getIn().setHeader("ydelta", packet.ydelta);
                        exchange.getIn().setHeader("ystart", packet.ystart);
                        exchange.getIn().setHeader("yunits", packet.yunits);

                        exchange.getIn().setBody(packet);

                        try {
                            processor.process(exchange);
                        } catch (Exception e) {
                            logger.error("EXCEPTION: " + e.getMessage());
                        }
                    }
                };

                portListener.setMaxQueueSize(endpoint.getBufferSize());
                port.connect(portListener);

                connected = true;
                logger.debug("Connected to the port");
            }

        } catch (ConnectionException e) {
            logger.error("Problem connecting to REDHAWK: " + e.getMessage());
            connected = false;
        } catch (MultipleResourceException m) {
            logger.error("Found multiple resources within REDHAWK that could have the same port: " + m.getMessage());
            connected = false;
        } catch (NullPointerException e) {
            logger.error("Could not locate one of the REDHAWK elements: " + e.getMessage());
            connected = false;
        } catch (Exception e1) {
            logger.error("An Exception has occurred: " + e1.getMessage());
            connected = false;
        }
    }


    @Override
    protected void doStop() throws Exception {
        super.doStop();
        forceStop = true;
        try {
            if (port != null) {
                port.disconnect();
            }
        } catch (Exception e) {
            logger.error("Could Not Disconnect Port: " + e.getMessage());
        }
    }


}