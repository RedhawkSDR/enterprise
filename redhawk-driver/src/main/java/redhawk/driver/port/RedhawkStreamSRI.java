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
package redhawk.driver.port;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import BULKIO.StreamSRI;

/**
 * XML for RedhawkStreamSRI object. 
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RedhawkStreamSRI {
	private int hversion; 
	
	private double xstart; 
	
	private double xdelta;
	
	private short xunits;
	
	private int subsize;
	
	private double ystart;
	
	private double ydelta;
	
	private short yunits;
	
	private short mode;
	
	private String streamID;
	
	private boolean blocking;
	
	private Object[] keywords;
	
	public RedhawkStreamSRI(){	
	}
	
	public RedhawkStreamSRI(StreamSRI sri){
		setHversion(sri.hversion);
		this.setXstart(sri.xstart);
		this.setXunits(sri.xunits);
		this.setSubsize(sri.subsize);
		this.setXdelta(sri.xdelta);
		this.setSubsize(sri.subsize);
		this.setYstart(sri.ystart);
		this.setYdelta(sri.ydelta);
		this.setYunits(sri.yunits);
		this.setMode(sri.mode);
		this.setStreamID(sri.streamID);
		this.setBlocking(sri.blocking);
		this.setKeywords(sri.keywords);
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

	public short getMode() {
		return mode;
	}

	public void setMode(short mode) {
		this.mode = mode;
	}

	public String getStreamID() {
		return streamID;
	}

	public void setStreamID(String streamID) {
		this.streamID = streamID;
	}

	public boolean isBlocking() {
		return blocking;
	}

	public void setBlocking(boolean blocking) {
		this.blocking = blocking;
	}

	public Object[] getKeywords() {
		return keywords;
	}

	public void setKeywords(Object[] keywords) {
		this.keywords = keywords;
	}

	public short getYunits() {
		return yunits;
	}

	public void setYunits(short yunits) {
		this.yunits = yunits;
	}

	@Override
	public String toString() {
		return "RedhawkStreamSRI [hversion=" + hversion + ", xstart=" + xstart + ", xdelta=" + xdelta + ", xunits="
				+ xunits + ", subsize=" + subsize + ", ystart=" + ystart + ", ydelta=" + ydelta + ", yunits=" + yunits
				+ ", mode=" + mode + ", streamID=" + streamID + ", blocking=" + blocking + ", keywords="
				+ Arrays.toString(keywords) + "]";
	}
}
