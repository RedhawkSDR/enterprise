package redbus.core.orchestration.components.redhawk.eventchannel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omg.CORBA.Any;

import CF.LogEvent;
import redbus.core.orchestration.components.redhawk.RedhawkComponent;
import redbus.core.orchestration.components.redhawk.endpoints.RedhawkEventChannelEndpoint;
import redhawk.driver.RedhawkUtils;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.eventchannel.RedhawkEventChannel;
import redhawk.driver.eventchannel.listeners.EventChannelListener;
import redhawk.driver.eventchannel.listeners.LogEventListener;
import redhawk.driver.eventchannel.listeners.PropertyChange;
import redhawk.driver.eventchannel.listeners.PropertyChangeListener;

public class RedhawkEventChannelConsumer extends DefaultConsumer {
    private final RedhawkEventChannelEndpoint endpoint;

    private static Log logger = LogFactory.getLog(RedhawkEventChannelConsumer.class);

    private static final String PROPERTY_SOURCE_ID = "PROPERTY_SOURCE_ID";
    private static final String PROPERTY_SOURCE_NAME = "PROPERTY_SOURCE_NAME";
    private static final String PROPERTY_ID = "PROPERTY_ID";
    private static final String PROPERTY_VALUE_KIND = "PROPERTY_VALUE_KIND";
    private static final String PROPERTY_VALUE = "PROPERTY_VALUE";

    private Processor processor;
    private RedhawkEventChannel eventChannel;
    private RedhawkDomainManager domainManager;
    private boolean consumerThreadRunFlag = true;

    private Thread eventChannelConsumerThread;

    public RedhawkEventChannelConsumer(RedhawkEventChannelEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
        this.endpoint = endpoint;
        this.processor = processor;
    }


    @Override
    protected void doStart() throws Exception {
        super.doStart();

        final EventChannelListener listener;

        if (endpoint.getDataTypeName().equalsIgnoreCase("messages")) {

            listener = new EventChannelListener<Any>() {

                @Override
                protected Any processMessage(Any data) {
                    return data;
                }

                @Override
                public void onMessage(Any message) {
                    Exchange exchange = endpoint.createExchange();
                    exchange.getIn().setHeader("originalRedhawkObject", message);

                    if (endpoint.isConvertAnysToMaps()) {
                        exchange.getIn().setBody(RedhawkUtils.convertPropertiesAnyToMap(message));
                    } else {
                        exchange.getIn().setBody(message);
                    }

                    try {
                        processor.process(exchange);
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }
                }
            };
        } else if (endpoint.getDataTypeName().equalsIgnoreCase("properties")) {
            listener = new PropertyChangeListener() {
                @Override
                public void onMessage(PropertyChange message) {
                    Exchange exchange = null;
                    exchange = endpoint.createExchange();

                    for (String propId : message.getProperties().keySet()) {
                        exchange = endpoint.createExchange();
                        exchange.setProperty(PROPERTY_SOURCE_ID, message.getSourceId());
                        exchange.setProperty(PROPERTY_SOURCE_NAME, message.getSourceName());
                        exchange.setProperty(PROPERTY_ID, propId);
                        exchange.setProperty(PROPERTY_VALUE_KIND, message.getProperties().get(propId).getClass().getName());
                        exchange.setProperty(PROPERTY_VALUE, message.getProperties().get(propId));

                        exchange.getIn().setHeader("originalRedhawkObject", message.getCorbaAny());
                        exchange.getIn().setBody(message.getProperties());

                        try {
                            if (exchange != null) {
                                processor.process(exchange);
                            }
                        } catch (Exception e) {
                            logger.error(e.getMessage());
                        } finally {
                            if (exchange.getException() != null) {
                                getExceptionHandler().handleException("Error processing exchange", exchange, exchange.getException());
                            }
                        }
                    }
                }
            };
        } else if (endpoint.getDataTypeName().equalsIgnoreCase("logEvent")) {
            listener = new LogEventListener() {
                @Override
                public void onMessage(LogEvent message) {
                    Exchange exchange = null;
                    exchange = endpoint.createExchange();

                    exchange.getIn().setHeader("originalRedhawkObject", message);
                    exchange.getIn().setBody(message);

                    try {
                        if (exchange != null) {
                            processor.process(exchange);
                        }
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    } finally {
                        if (exchange.getException() != null) {
                            getExceptionHandler().handleException("Error processing exchange", exchange, exchange.getException());
                        }
                    }
                }
            };

        } else {
            throw new IllegalArgumentException("Cannot find a listener for the specified data type: " + endpoint.getDataTypeName());
        }

        eventChannelConsumerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (consumerThreadRunFlag) {
                    try {
                        String domainName = ((RedhawkComponent) endpoint.getComponent()).getDomainName();
                        ((RedhawkComponent) endpoint.getComponent()).getRedhawkDriver().getDomain(domainName);
                        
                        if(domainManager == null) {
                        	domainManager = ((RedhawkComponent) endpoint.getComponent()).getRedhawkDriver().getDomain(domainName);
                        }

                        //TODO: this may be a heartbeat
                        domainManager.getEventChannelManager().getEventChannel(endpoint.getEventChannelName());
                        if(eventChannel == null) {
                        	eventChannel = domainManager.getEventChannelManager().getEventChannel(endpoint.getEventChannelName());
                        	eventChannel.subscribe(listener);
                        }
                        
                        
                        Thread.currentThread().sleep(5000);
                    } catch (Exception e) {
                        logger.error("Problem creating the consumer to the eventchannel. Will re-attempt in 10 seconds", e);
                        try {
                        	eventChannel = null;
                            Thread.currentThread().sleep(10000);
                        } catch (InterruptedException e1) {
                            logger.error(e);
                        }
                    }
                }
            }
        });

        eventChannelConsumerThread.start();

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                consumerThreadRunFlag = false;
            }
        }));

    }

    @Override
    protected void doStop() throws Exception {
        if (eventChannel != null) {
            eventChannel.unsubscribe();
        }

        consumerThreadRunFlag = false;
        super.doStop();
    }


}
