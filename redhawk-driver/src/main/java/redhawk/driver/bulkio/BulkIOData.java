package redhawk.driver.bulkio;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import BULKIO.PortStatistics;
import BULKIO.PortUsageType;
import BULKIO.PrecisionUTCTime;
import BULKIO.StreamSRI;
import BULKIO.dataCharOperations;
import BULKIO.dataDoubleOperations;
import BULKIO.dataFileOperations;
import BULKIO.dataFloatOperations;
import BULKIO.dataLongLongOperations;
import BULKIO.dataLongOperations;
import BULKIO.dataOctetOperations;
import BULKIO.dataShortOperations;
import BULKIO.dataUlongLongOperations;
import BULKIO.dataUlongOperations;
import BULKIO.dataUshortOperations;
import BULKIO.dataXMLOperations;
import redhawk.driver.RedhawkUtils;
import redhawk.driver.port.PortListener;

public class BulkIOData<T> implements dataCharOperations, dataFloatOperations, dataDoubleOperations, dataFileOperations, dataXMLOperations,
										dataLongOperations, dataLongLongOperations, dataOctetOperations, dataShortOperations, dataUlongOperations, dataUlongLongOperations, dataUshortOperations {

    private static Logger logger = Logger.getLogger(BulkIOData.class.getName());
    private Map<String, StreamSRI> sriMap = new ConcurrentHashMap<String, StreamSRI>();
    private Set<String> updatedSRI = new HashSet<String>();
    
    private LinkedBlockingQueue dataQueue;
    private LinkedBlockingQueue<StreamSRI> sriQueue;
    
    private PortListener<T> portListener;
	
    private Thread dataProcessingThread;
    private Thread sriProcessingThread;
    private boolean processData = true;
    
    
    public BulkIOData(PortListener<T> portListener){
    	this.portListener = portListener;
    	dataQueue = new LinkedBlockingQueue(portListener.getMaxQueueSize());
    	sriQueue = new LinkedBlockingQueue<StreamSRI>(portListener.getMaxQueueSize());    	
    	
    	dataProcessingThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(processData){
					try {
						Object[] obj = (Object[]) dataQueue.take();
						genericPushPacket((Object) obj[0], (PrecisionUTCTime) obj[1], (Boolean) obj[2], (String) obj[3]);
					} catch (InterruptedException e) {
						logger.log(Level.SEVERE, "Interrupted exception on processing thread in BulkIoData", e);
					} catch (Exception e){
						logger.log(Level.SEVERE, "Exception caught", e);
					}
				}
			}
		});
    	
    	
    	sriProcessingThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(processData){
					
					try {
						StreamSRI streamSRI = sriQueue.take();
						
						synchronized (sriMap) {
							if(sriMap.get(streamSRI.streamID) != null){
								
								if(!RedhawkUtils.compareStreamSRI(streamSRI, sriMap.get(streamSRI.streamID))){
									//add to list of updated streams
									updatedSRI.add(streamSRI.streamID);
								}
							}
							
							sriMap.put(streamSRI.streamID, streamSRI);
						}					
						
					} catch (InterruptedException e) {
						logger.log(Level.SEVERE, "Interrupted exception on processing thread in BulkIoData", e);
					} catch (Exception e){
						logger.log(Level.SEVERE, "Exception caught", e);
					}
				}
			}
		});
    	
    	Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				processData = false;
			}
		}));
    	
    	sriProcessingThread.start();
    	dataProcessingThread.start();
    	
    }
    
    
    public void disconnect(){
    	processData = false;
    }
    
	@Override
	public PortUsageType state() {
		return null;
	}

	@Override
	public PortStatistics statistics() {
		return null;
	}

	@Override
	public StreamSRI[] activeSRIs() {
		synchronized(sriMap){
			return sriMap.values().toArray(new StreamSRI[sriMap.values().size()]);
		}
	}

	@Override
	public void pushSRI(StreamSRI streamSRI) {
		sriQueue.offer(streamSRI);
	}

	@Override
	public void pushPacket(float[] body, PrecisionUTCTime time, boolean endOfStream, String streamId) {
		if(!dataQueue.offer(new Object[]{body, time, endOfStream, streamId})){
			dataQueue.clear();
			logger.warning("QUEUE Threashold Exceeded. Flushing Queue");
		}
	}

	@Override
	public void pushPacket(char[] body, PrecisionUTCTime time, boolean endOfStream, String streamId) {
		if(!dataQueue.offer(new Object[]{body, time, endOfStream, streamId})){
			dataQueue.clear();
			logger.warning("QUEUE Threashold Exceeded. Flushing Queue");
		}
	}

	@Override
	public void pushPacket(short[] body, PrecisionUTCTime time, boolean endOfStream, String streamId) {
		if(!dataQueue.offer(new Object[]{body, time, endOfStream, streamId})){
			dataQueue.clear();
			logger.warning("QUEUE Threashold Exceeded. Flushing Queue");
		}
	}

	@Override
	public void pushPacket(byte[] body, PrecisionUTCTime time, boolean endOfStream, String streamId) {
		if(!dataQueue.offer(new Object[]{body, time, endOfStream, streamId})){
			dataQueue.clear();
			logger.warning("QUEUE Threashold Exceeded. Flushing Queue");
		}
	}

	@Override
	public void pushPacket(long[] body, PrecisionUTCTime time, boolean endOfStream, String streamId) {
		if(!dataQueue.offer(new Object[]{body, time, endOfStream, streamId})){
			dataQueue.clear();
			logger.warning("QUEUE Threashold Exceeded. Flushing Queue");
		}
	}

	@Override
	public void pushPacket(int[] body, PrecisionUTCTime time, boolean endOfStream, String streamId) {
		if(!dataQueue.offer(new Object[]{body, time, endOfStream, streamId})){
			dataQueue.clear();
			logger.warning("QUEUE Threashold Exceeded. Flushing Queue");
		}
	}

	@Override
	public void pushPacket(String body, PrecisionUTCTime time, boolean endOfStream, String streamId) {
		if(!dataQueue.offer(new Object[]{body, time, endOfStream, streamId})){
			dataQueue.clear();
			logger.warning("QUEUE Threashold Exceeded. Flushing Queue");
		}
	}

	@Override
	public void pushPacket(double[] body, PrecisionUTCTime time, boolean endOfStream, String streamId) {
		if(!dataQueue.offer(new Object[]{body, time, endOfStream, streamId})){
			dataQueue.clear();
			logger.warning("QUEUE Threashold Exceeded. Flushing Queue");
		}
	}
	
	@Override
	public void pushPacket(String body, boolean endOfStream, String streamId) {
		if(!dataQueue.offer(new Object[]{body, new PrecisionUTCTime(), endOfStream, streamId})){
			dataQueue.clear();
			logger.warning("QUEUE Threashold Exceeded. Flushing Queue");
		}
	}		
	
	private void genericPushPacket( Object body, PrecisionUTCTime time, boolean endOfStream, String streamId) {
        try {
        	StreamSRI streamSRI; 
        	boolean sriUpdate = false;
            synchronized(sriMap){
            	if(updatedSRI.contains(streamId)){
            		updatedSRI.remove(streamId);
            		sriUpdate = true;
            	}
                streamSRI = sriMap.get(streamId);
            }
            
            if(streamSRI == null){
            	logger.fine("STREAM SRI is NULL");
            	logger.warning("Could not find the SRI that matches the streamId of the Packet:["+streamId+"]");
            } else {
	            Packet<T> packet = new Packet<T>(streamSRI, time, (T) body, endOfStream, streamId);
				
	            if(sriUpdate){
					logger.fine("New SRI From SERVER" + sriUpdate);
				}	            
	            
	            packet.setNewSri(sriUpdate);
	            portListener.onReceive(packet);
            }
        } catch (Throwable t) {
            logger.severe("THROWABLE CAUGHT : " + t.getMessage());
        }    
        
        if(endOfStream){
            synchronized(sriMap){
                sriMap.remove(streamId);
            }
        }  
	}


	
}
