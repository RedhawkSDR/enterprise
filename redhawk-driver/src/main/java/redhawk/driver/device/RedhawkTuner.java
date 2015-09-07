package redhawk.driver.device;

import java.util.Map;

//import redhawk.RedhawkFrontendDevice;

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
