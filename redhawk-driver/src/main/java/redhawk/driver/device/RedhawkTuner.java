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
package redhawk.driver.device;

import java.util.Map;

//import redhawk.RedhawkFrontendDevice;

//TODO: Will need to test this to understand before you can document. 
public interface RedhawkTuner {
	//public void tune(Map<String, Object> properties);
	//public void deTune();
	//public Map<String, Object> getStatus();
	//public void listen(String listenerId);
//	public RedhawkFrontendDevice getFrontendDevice();
//	public void tune(Map<String, Object> properties);
//	public void deTune();
//	public String getAllocationId();
	
	public boolean isAgcEnabled();
	public double getBandwidth();
	public double getCenterFrequency();
	public boolean isDeviceControl();
	public boolean isEnabled();
	public float getGain();
	public String getGroupId();
	public double getOutputSampleRate();
	public int getReferenceSource();
	public String getRfFlowId();
	public Map<String, Object> getStatus();
	public String getType();
	public void setAgcEnabled(boolean enabled);
	public void setBandwidth(double bandwidth);
	public void setCenterFrequency(double frequency);
	public void setEnabled(boolean enabled);
	public void setGain(float gain);
	public void setOutputSampleRate(double sampleRate);
	public void setReferenceSource(int refSource);
}
