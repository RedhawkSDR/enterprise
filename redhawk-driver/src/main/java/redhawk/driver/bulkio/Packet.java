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
package redhawk.driver.bulkio;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.ossie.properties.AnyUtils;

import BULKIO.PrecisionUTCTime;
import BULKIO.StreamSRI;
import BULKIO.TCM_CPU;
import BULKIO.TCS_VALID;
import CF.DataType;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Packet<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	public boolean endOfStream;
	public String streamId;
	
	/**
	 * Version of the StreamSRI header. 1.0 Default. (long)
	 */
	public int hversion = 1;
	
	/**
	 * Start time of the stream (double)
	 */	
	public double xstart = 0.0;
	
	/**
	 * Delta between two values in the payload vector.(double)
	 */
	public double xdelta = 0.0;
	
	/**
	 * Unit types; common codes defined in IDL(i.e. 0=None, 1=time, 2=delay, 3=frequency)[short]
	 */
	public short xunits = 1;
	
	/**
	 * 0 if the data is one dimensional; >0 if two dimensional. This applies to framed data;
	 * ydelta describes the frame length (long)
	 */
	public int subsize = 0;
	
	/**
	 * Start of second dimension(double)
	 */
	public double ystart = 0.0;
	
	/**
	 * Delta between two samples of second dimension(double)
	 */
	public double ydelta = 0.0;
	
	/**
	 * Unique streams can be delivered over the same Port, where each stream is identified 
	 * by a unique String.
	 */
	public short yunits = 0;
	
	/**
	 * 0-Scalar, 1-Complex. Complex data is passed as interleaved I/Q values in the 
	 * sequence
	 */
	public short mode = 0;
	
	/**
	 * Flag to determine whether the receiving Port should exhibit back pressure.
	 */
	public boolean blocking = false;
	
	private T data;
	
	public Map<String, Object> keywords = new HashMap<String, Object>();
	
	private StreamSRI streamSri;
	
	private boolean newSri;
	public short tcmode;
	public short tcstatus;
	public double tfsec;
	public double toff;
	public double twsec;
	private PrecisionUTCTime time;
	
	/**
	 * Default constructor uses defaults for all values and will 
	 * initialize a PrecisonUTCTime object to the time the constructor 
	 * was called. 
	 */
	public Packet() {
		tcmode = TCM_CPU.value;
		tcstatus = TCS_VALID.value; 
		toff = 0.0; 
		
		/**
		 * Code ripped from BULKIO::PrecisionUTCTime create() method 
		 */
		long currentTime = System.currentTimeMillis(); 
		twsec = currentTime/1000; 
		tfsec = (currentTime%1000)/1000.0; 
		
		time = new PrecisionUTCTime(tcmode, tcstatus, toff, twsec, tfsec);
		
		/*
		 * Defaulting streamId to a random UUID 
		 */
		streamId = UUID.randomUUID().toString();
	}
	
	public Packet(StreamSRI sri, PrecisionUTCTime time, T data, boolean endOfStream, String streamId){
		this.blocking = sri.blocking;
		this.hversion = sri.hversion;
		this.mode = sri.mode;
		this.subsize = sri.subsize;
		this.xdelta = sri.xdelta;
		this.xstart = sri.xstart;
		this.xunits = sri.xunits;
		this.ydelta = sri.ydelta;
		this.ystart = sri.ystart;
		this.yunits = sri.yunits;
		this.endOfStream = endOfStream;
		this.streamId = streamId;
		
		this.time = time;
		
		if(time != null){
			this.tcmode = time.tcmode;
			this.tcstatus = time.tcstatus;
			this.tfsec = time.tfsec;
			this.toff = time.toff;
			this.twsec = time.twsec;
		}
		
		if(sri.keywords != null){
			for(DataType type : sri.keywords){
				keywords.put(type.id, AnyUtils.convertAny(type.value));
			}
		}
		
		this.streamSri = sri;
		this.data = data;
	}
	
	public void decimateTo(int sampleSize){

		if(data.getClass().isArray()){
			
			int bodyLength = 0;
	        if (data != null) {
	            bodyLength = Array.getLength(data);
	        }
			
            int sampleRate = bodyLength / sampleSize;
            
            if(data.getClass().equals(double[].class)){
            	subsize = sampleSize;
            	decimateDouble(sampleRate);
            } else if(data.getClass().equals(long[].class)){
            	subsize = sampleSize;
            	decimateLong(sampleRate);
            } else if(data.getClass().equals(short[].class)){
            	subsize = sampleSize;
            	decimateShort(sampleRate);
            } else if(data.getClass().equals(int[].class)){
            	subsize = sampleSize;
            	decimateInt(sampleRate);
            } else if(data.getClass().equals(float[].class)){
            	subsize = sampleSize;
            	decimateFloat(sampleRate);
            } else {
            	return;
            }
		}
	}
	
	
	public void thinTo(int sampleSize){

		if(data.getClass().isArray()){
			int bodyLength = 0;
	        if (data != null) {
	            bodyLength = Array.getLength(data);
	        }
        	
            int sampleRate = bodyLength / sampleSize;
            
            if(data.getClass().equals(double[].class)){
            	subsize = sampleSize;
            	thinDouble(sampleRate);
            } else if(data.getClass().equals(long[].class)){
            	subsize = sampleSize;
            	thinLong(sampleRate);
            } else if(data.getClass().equals(short[].class)){
            	subsize = sampleSize;
            	thinShort(sampleRate);
            } else if(data.getClass().equals(int[].class)){
            	subsize = sampleSize;
            	thinInt(sampleRate);
            } else if(data.getClass().equals(float[].class)){
            	subsize = sampleSize;
            	thinFloat(sampleRate);
            } else {
            	return;
            }
		}
	}
	
	
	
	
	private void thinDouble(int size){
        List<Double> sampledList = new ArrayList<Double>();
        double[] d = ((double[]) data);
        
        int sizeCnt = 1;
        for(int i = 0; i < d.length; i+=2) {
        	if(sizeCnt == size){
	        	sampledList.add(d[i]);
		    	sampledList.add(d[i+1]);
			    sizeCnt = 1;
        	} else {
        		sizeCnt++;
        	}
        }		
		
        data = (T) toPrimitive(sampledList.toArray(new Double[sampledList.size()]));
	}
	
	private void thinLong(int size){
        List<Long> sampledList = new ArrayList<Long>();
        long[] d = ((long[]) data);
        
        int sizeCnt = 1;
        for(int i = 0; i < d.length; i++) {
        	if(sizeCnt == size){
	        	sampledList.add(d[i]);
			    
			    if(mode == 1 && (i+1) < d.length){
			    	sampledList.add(d[i+1]);
			    }
			    sizeCnt = 1;
        	} else {
        		sizeCnt++;
        	}
        }		
		
        data = (T) toPrimitive(sampledList.toArray(new Long[sampledList.size()]));
	}
	
	
	private void thinInt(int size){
        List<Integer> sampledList = new ArrayList<Integer>();
        int[] d = ((int[]) data);
        
        int sizeCnt = 1;
        for(int i = 0; i < d.length; i++) {
        	if(sizeCnt == size){
	        	sampledList.add(d[i]);
			    
			    if(mode == 1 && (i+1) < d.length){
			    	sampledList.add(d[i+1]);
			    }
			    sizeCnt = 1;
        	} else {
        		sizeCnt++;
        	}
        }			
		
        data = (T) toPrimitive(sampledList.toArray(new Integer[sampledList.size()]));
	}
	private void thinShort(int size){
        List<Short> sampledList = new ArrayList<Short>();
        short[] d = ((short[]) data);
        
        int sizeCnt = 1;
        for(int i = 0; i < d.length; i++) {
        	if(sizeCnt == size){
	        	sampledList.add(d[i]);
			    
			    if(mode == 1 && (i+1) < d.length){
			    	sampledList.add(d[i+1]);
			    }
			    sizeCnt = 1;
        	} else {
        		sizeCnt++;
        	}
        }			
		
        data = (T) toPrimitive(sampledList.toArray(new Short[sampledList.size()]));
	}
	
	
	private void thinFloat(int size){
        List<Float> sampledList = new ArrayList<Float>();
        float[] d = ((float[]) data);
        
        int sizeCnt = 1;
        for(int i = 0; i < d.length; i++) {
        	if(sizeCnt == size){
	        	sampledList.add(d[i]);
			    
			    if(mode == 1 && (i+1) < d.length){
			    	sampledList.add(d[i+1]);
			    }
			    sizeCnt = 1;
        	} else {
        		sizeCnt++;
        	}
        }			
		
        data = (T) toPrimitive(sampledList.toArray(new Float[sampledList.size()]));
	}
	
	
    public static int[] toPrimitive(Integer[] array) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return new int[0];
        }
        final int[] result = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i].intValue();
        }
        return result;
    }
	
	
    public static short[] toPrimitive(Short[] array) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return new short[0];
        }
        final short[] result = new short[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i].shortValue();
        }
        return result;
    }
	
    public static float[] toPrimitive(Float[] array) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return new float[0];
        }
        final float[] result = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i].floatValue();
        }
        return result;
    }
	
    public static double[] toPrimitive(Double[] array) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return new double[0];
        }
        final double[] result = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i].doubleValue();
        }
        return result;
    }
	
    public static long[] toPrimitive(Long[] array) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return new long[0];
        }
        final long[] result = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i].longValue();
        }
        return result;
    }
	
	
	

	private void decimateInt(int size){
        List<Integer> innerlist = new ArrayList<Integer>();
        List<Integer> sampledList = new ArrayList<Integer>();

        int[] d = ((int[]) data);
        
        for(int i = 0; i < d.length; i++){
        	
        	innerlist.add(d[i]);
        	
        	if(mode == 1){
        		
        	}
        	
        	
			if( innerlist.size() == size){
			    Collections.sort(innerlist);
			    sampledList.add(innerlist.get(innerlist.size() - 1));
			    innerlist.clear();                    
			}
        }		
		
        data = (T) toPrimitive(sampledList.toArray(new Integer[sampledList.size()]));
	}
	
	private void decimateFloat(int size){
        List<Float> innerlist = new ArrayList<Float>();
        List<Float> sampledList = new ArrayList<Float>();

        float[] d = ((float[]) data);
        
        for(int i = 0; i < d.length; i++){
        	innerlist.add(d[i]);
        	
			if( innerlist.size() == size){
			    Collections.sort(innerlist);
			    sampledList.add(innerlist.get(innerlist.size() - 1));
			    innerlist.clear();                    
			}
        }		
		
        data = (T) toPrimitive(sampledList.toArray(new Float[sampledList.size()]));
	}	
	
	private void decimateShort(int size){
        List<Short> innerlist = new ArrayList<Short>();
        List<Short> sampledList = new ArrayList<Short>();

        short[] d = ((short[]) data);
        
        for(int i = 0; i < d.length; i++){
        	innerlist.add(d[i]);
        	
			if( innerlist.size() == size){
			    Collections.sort(innerlist);
			    sampledList.add(innerlist.get(innerlist.size() - 1));
			    innerlist.clear();                    
			}
        }		
		
        data = (T) toPrimitive(sampledList.toArray(new Short[sampledList.size()]));
	}
	
	private void decimateDouble(int size){
        List<Double> innerlist = new ArrayList<Double>();
        List<Double> sampledList = new ArrayList<Double>();

        double[] d = ((double[]) data);
        
        for(int i = 0; i < d.length; i++){
        	innerlist.add(d[i]);
        	
			if( innerlist.size() == size){
			    Collections.sort(innerlist);
			    sampledList.add(innerlist.get(innerlist.size() - 1));
			    innerlist.clear();                    
			}
        }		
		
        data = (T) toPrimitive(sampledList.toArray(new Double[sampledList.size()]));
	}
	
	private void decimateLong(int size){
        List<Long> innerlist = new ArrayList<Long>();
        List<Long> sampledList = new ArrayList<Long>();

        long[] d = ((long[]) data);
        
        for(int i = 0; i < d.length; i++){
        	innerlist.add(d[i]);
        	
			if( innerlist.size() == size){
			    Collections.sort(innerlist);
			    sampledList.add(innerlist.get(innerlist.size() - 1));
			    innerlist.clear();                    
			}
        }		
		
        data = (T) toPrimitive(sampledList.toArray(new Long[sampledList.size()]));
	}
	
	public Map<String, Object> getSriAsMap(){
		Map<String, Object> sriMap = new HashMap<>();
		sriMap.put("endOfStream",endOfStream);
		sriMap.put("streamId",streamId);
		sriMap.put("hversion",hversion);
		sriMap.put("xstart",xstart);
		sriMap.put("xdelta",xdelta);
		sriMap.put("xunits",xunits);
		sriMap.put("subsize",subsize);
		sriMap.put("ystart",ystart);
		sriMap.put("ydelta",ydelta);
		sriMap.put("yunits",yunits);
		sriMap.put("mode",mode);
		sriMap.put("blocking",blocking);
		sriMap.put("newSri",newSri);
		sriMap.put("tcmode",tcmode);
		sriMap.put("tcstatus",tcstatus);
		sriMap.put("tfsec",tfsec);
		sriMap.put("toff",toff);
		sriMap.put("twsec",twsec);	
		return sriMap;
	}
	
	public int getHversion() {
		return hversion;
	}
	public void setHversion(int hversion) {
		this.hversion = hversion;
	}
	public double getXstart() {
		return xstart;
	}
	public void setXstart(double xstart) {
		this.xstart = xstart;
	}
	public double getXdelta() {
		return xdelta;
	}
	public void setXdelta(double xdelta) {
		this.xdelta = xdelta;
	}
	public short getXunits() {
		return xunits;
	}
	public void setXunits(short xunits) {
		this.xunits = xunits;
	}
	public int getSubsize() {
		return subsize;
	}
	public void setSubsize(int subsize) {
		this.subsize = subsize;
	}
	public double getYstart() {
		return ystart;
	}
	public void setYstart(double ystart) {
		this.ystart = ystart;
	}
	public double getYdelta() {
		return ydelta;
	}
	public void setYdelta(double ydelta) {
		this.ydelta = ydelta;
	}
	public short getYunits() {
		return yunits;
	}
	public void setYunits(short yunits) {
		this.yunits = yunits;
	}
	public short getMode() {
		return mode;
	}
	public void setMode(short mode) {
		this.mode = mode;
	}
	public boolean isBlocking() {
		return blocking;
	}
	public void setBlocking(boolean blocking) {
		this.blocking = blocking;
	}
	public Map<String, Object> getKeywords() {
		return keywords;
	}
	public void setKeywords(Map<String, Object> keywords) {
		this.keywords = keywords;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}

	public boolean isEndOfStream() {
		return endOfStream;
	}

	public void setEndOfStream(boolean endOfStream) {
		this.endOfStream = endOfStream;
	}

	public String getStreamId() {
		return streamId;
	}

	public void setStreamId(String streamId) {
		this.streamId = streamId;
	}

	public StreamSRI getStreamSri() {
		return streamSri;
	}

	public void setStreamSri(StreamSRI streamSri) {
		this.streamSri = streamSri;
	}

	public boolean isNewSri() {
		return newSri;
	}

	public void setNewSri(boolean newSri) {
		this.newSri = newSri;
	}

	public short getTcmode() {
		return tcmode;
	}

	public short getTcstatus() {
		return tcstatus;
	}

	public double getTfsec() {
		return tfsec;
	}

	public double getToff() {
		return toff;
	}

	public double getTwsec() {
		return twsec;
	}

	public void setTcmode(short tcmode) {
		this.tcmode = tcmode;
	}

	public void setTcstatus(short tcstatus) {
		this.tcstatus = tcstatus;
	}

	public void setTfsec(double tfsec) {
		this.tfsec = tfsec;
	}

	public void setToff(double toff) {
		this.toff = toff;
	}

	public void setTwsec(double twsec) {
		this.twsec = twsec;
	}

	public PrecisionUTCTime getTime() {
		return time;
	}

	public void setTime(PrecisionUTCTime time) {
		this.time = time;
	}


}
