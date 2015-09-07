package redhawk.driver.device.impl;

import java.util.ArrayList;
import java.util.Map;

//import redhawk.RedhawkFrontendDevice;
import redhawk.driver.device.RedhawkTuner;
import redhawk.driver.port.RedhawkPort;
import redhawk.driver.properties.RedhawkStruct;
import FRONTEND.DigitalTunerHelper;
import FRONTEND.DigitalTunerOperations;

//the point of this class is to encapsulate the allocation_id so its never exposed to the user
public class RedhawkTunerImpl implements RedhawkTuner {
	
//	protected RedhawkFrontendDevice parentDevice;
	protected RedhawkPort controlPort;
//	protected String allocId;
	
	protected String getAllocId(RedhawkStruct s) {
		String allocIdCsv = (String) s.toMap().get("FRONTEND::tuner_status::allocation_id_csv");
		if (allocIdCsv == null)
			return null;
		ArrayList<String> allocIds = new ArrayList<String>();
		if (allocIdCsv.isEmpty())
			return null;
		return allocIdCsv.split(",")[0].trim();
	}
//	
//	public RedhawkTunerImpl(RedhawkFrontendDevice parentDevice, Map<String, Object> status) {
//		this.parentDevice = parentDevice;
//		
//		String tuner_type = (String) status.get("FRONTEND::tuner_status::tuner_type");
//		try {
//			// Different types of control ports available: DigitalTuner_in, AnalogTuner_in, GPS_in, RFSource_in...
//			controlPort = parentDevice.getPortByName("DigitalTuner_in");
//			DigitalTunerOperations digitalTuner = DigitalTunerHelper.narrow(controlPort.getCorbaObject());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	protected <T> T getControlPort() throws Exception {
		//RedhawkPort port = parentDevice.getPortByName("DigitalTuner_in");
		throw new UnsupportedOperationException("This method is not yet implemented");
		// need to extract the appropriate control port type, analog, digital, gps, rfinfo, etc
	}
	
//	@Override
//	public RedhawkFrontendDevice getFrontendDevice() {
//		return parentDevice;
//	}

//	@Override
//	public void tune(Map<String, Object> properties) {
//		// parse out the properties and set all the values... bean style perhaps
//	}

//	@Override
//	public void deTune() {
//		parentDevice.deallocate(allocId);
//	}

	@Override
	public boolean isAgcEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double getBandwidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getCenterFrequency() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isDeviceControl() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public float getGain() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getGroupId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getOutputSampleRate() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getReferenceSource() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getRfFlowId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAgcEnabled(boolean enabled) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBandwidth(double bandwidth) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCenterFrequency(double frequency) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEnabled(boolean enabled) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGain(float gain) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOutputSampleRate(double sampleRate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setReferenceSource(int refSource) {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public String getAllocationId() {
//		return allocId;
//	}
}
