/*
g * This file is protected by Copyright. Please refer to the COPYRIGHT file
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
package redhawk.driver.device.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import org.omg.CORBA.COMM_FAILURE;
import org.omg.CORBA.OBJECT_NOT_EXIST;
import org.omg.CORBA.TRANSIENT;

import CF.DataType;
import CF.Device;
import CF.DeviceHelper;
import CF.DevicePackage.AdminType;
import CF.DevicePackage.InsufficientCapacity;
import CF.DevicePackage.InvalidCapacity;
import CF.DevicePackage.InvalidState;
import CF.LifeCyclePackage.ReleaseError;
import CF.ResourcePackage.StartError;
import CF.ResourcePackage.StopError;
import redhawk.driver.RedhawkUtils;
import redhawk.driver.base.impl.PortBackedObjectImpl;
import redhawk.driver.device.AdminState;
import redhawk.driver.device.OperationalState;
import redhawk.driver.device.RedhawkDevice;
import redhawk.driver.devicemanager.RedhawkDeviceManager;
import redhawk.driver.exceptions.ConnectionException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.logging.RedhawkLogLevel;
import redhawk.driver.properties.RedhawkStruct;
import redhawk.driver.properties.RedhawkStructSequence;

/**
 * Wraps the CORBA {@link CFDevice} logic so it's easier to interact with 
 * as a user.  
 *
 */
public class RedhawkDeviceImpl extends PortBackedObjectImpl<Device> implements RedhawkDevice {

	private static Logger logger = Logger.getLogger(RedhawkDeviceImpl.class.getName());
    private RedhawkDeviceManager deviceManager;
    private String identifier;
    
    public RedhawkDeviceImpl(RedhawkDeviceManager deviceManager, String deviceIor, String identifier) {
        super(deviceIor, deviceManager.getDomainManager().getDriver().getOrb(), deviceManager.getFileSystem());
    	this.deviceManager = deviceManager;
    	this.identifier = identifier;
    }

	@Override
	protected Device locateCorbaObject() throws ResourceNotFoundException {
		String ior = ((RedhawkDeviceImpl) deviceManager.getDeviceByIdentifier(identifier)).getIor();
		return DeviceHelper.narrow(getOrb().string_to_object(ior));
	}

    public String getName(){
        return getCorbaObject().label();
    }

    public String getIdentifier(){
        return getCorbaObject().identifier();
    }
    
    public Device getCorbaObj(){
        return getCorbaObject();
    }
    
    public RedhawkDeviceManager getDeviceManager(){
        return deviceManager;    
    }


	
	
    @Override
    public void start() throws StartError {
    	getCorbaObject().start();
    }

    @Override
    public boolean started() {
        return getCorbaObject().started();
    }

    @Override
    public void stop() throws StopError {
    	getCorbaObject().stop();
    }

	@Override
	public void release() throws ReleaseError, ConnectionException {
		getCorbaObject().releaseObject();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RedhawkDeviceImpl [name=").append(getName())
				.append(", identifier=").append(getIdentifier())
				.append("]");
		return builder.toString();
	}

	@Override
	public Class<?> getHelperClass() {
		return CF.DeviceHelper.class;
	}

	@Override
	public void deallocate(String type, Map<String, Object> allocation) {
		Device d;
		try {
			DataType[] outer = new DataType[1];
			outer[0] = new DataType(type, RedhawkUtils.createAny(getOrb(), allocation));
			getCorbaObject().deallocateCapacity(outer);
		} catch (InvalidCapacity e) {
			e.printStackTrace();
		} catch (InvalidState e) {
			e.printStackTrace();
		} catch (ConnectionException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean allocate(String type, Map<String, Object> allocation) {
		try {
			DataType[] outer = new DataType[1];
			
			outer[0] = new DataType(type, RedhawkUtils.createAny(getOrb(), allocation));
			return getCorbaObject().allocateCapacity(outer);
		} catch (InvalidCapacity e) {
			logger.warning("Unable to allocate with provided data "+e.getMessage());
			logger.info("Going to switch all incoming integers to doubles and try again");
			//Try switching allocation to use Doubles 
			Map<String, Object> newAlloc = new HashMap<>();
			for(Map.Entry<String, Object> entry : allocation.entrySet()){
				if(entry.getValue() instanceof Integer){
					newAlloc.put(entry.getKey(), Double.parseDouble(entry.getValue().toString()));
				}else{
					newAlloc.put(entry.getKey(), entry.getValue());
				}
			}
			DataType[] outer = new DataType[1];

			outer[0] = new DataType(type, RedhawkUtils.createAny(getOrb(), newAlloc));
			try {
				return getCorbaObject().allocateCapacity(outer);
			} catch (InvalidCapacity | InvalidState | InsufficientCapacity | ConnectionException e1) {
				e1.printStackTrace();
			}
		} catch (InvalidState e) {
			e.printStackTrace();
		} catch (InsufficientCapacity e) {
			e.printStackTrace();
		} catch (ConnectionException e) {
			e.printStackTrace();
		} catch (COMM_FAILURE | TRANSIENT | OBJECT_NOT_EXIST e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	
	@Override
	public String allocate(Map<String, Object> allocation) {
		String allocId = (String) allocation.get("FRONTEND::tuner_allocation::allocation_id");
		if (allocId == null) {
			allocId = UUID.randomUUID().toString();
			allocation.put("FRONTEND::tuner_allocation::allocation_id", allocId);
		}
		return allocate("FRONTEND::tuner_allocation", allocation) ? allocId : null;
	}

	@Override
	public void deallocate(String allocationId) {
		Map<String, Object> allocation = new HashMap<String, Object>();
		allocation.put("FRONTEND::tuner_allocation::allocation_id", allocationId);
		deallocate("FRONTEND::tuner_allocation", allocation);
	}

	@Override
	public void deallocate(Map<String, Object> allocation) {
		deallocate("FRONTEND::tuner_allocation", allocation);
	}
	
	public String getAllocId(RedhawkStruct s) {
		List<String> allocIds = getAllocIds(s);
		
		if(allocIds.isEmpty()){
			return null;
		}
		return allocIds.get(0);
	}
	
	/**
	 * Checks the structs allocation_id_csv property to see if the 
	 * Device is allocated or not.
	 * 
	 * @param s
	 * @return
	 */
	public List<String> getAllocIds(RedhawkStruct s) {
		ArrayList<String> allocIds = new ArrayList<String>();
		String allocIdCsv = (String) s.toMap().get("FRONTEND::tuner_status::allocation_id_csv");
		
		if (allocIdCsv == null || allocIdCsv.isEmpty())
			return allocIds;
		
		for (String tunerAlloc : allocIdCsv.split(",")) {
			allocIds.add(tunerAlloc.trim());
		}
		
		return allocIds;
	}
	
	public List<String> getAllocIds(){
		List<String> allocIds = new ArrayList<String>();
		
		List<RedhawkStruct> structs = this.getStatus();
		for(RedhawkStruct struct : structs){
			allocIds.addAll(this.getAllocIds(struct));
		}
		
		return allocIds;
	}
	
	public List<Map<String, Object>> getAllTuners() {
//		return getStatus().stream().map(x -> x.toMap()).collect(Collectors.toList());
		
		List<Map<String,Object>> allTuners = new ArrayList<Map<String,Object>>();
		for(RedhawkStruct s : getStatus()){
			allTuners.add(s.toMap());
		}
		
		return allTuners;
	}

	protected List<RedhawkStruct> getStatus() {
		return ((RedhawkStructSequence) getProperty("FRONTEND::tuner_status")).getStructs();
	}

	public Map<String, Object> getTunerById(String allocId) {
		List<Map<String, Object>> tuners = new ArrayList<Map<String, Object>>();
		for (RedhawkStruct s : getStatus()) {
			// if (getAllocIds(s).contains(allocId))  //tuner matches if ANY allocation ids in the csv match
			if (getAllocIds(s).get(0).equals(allocId))  //tuner matches if FIRST allocation id in the csv matches
				tuners.add(s.toMap());
		}
		if (tuners.size() > 1) {
			throw new IllegalStateException("More than one tuner exist with allocation id: " + allocId);
		} else if (tuners.size() == 1) {
			return tuners.get(0);
		}

		return null;
	}

	public List<Map<String, Object>> getUnusedTuners() {
		List<Map<String, Object>> tuners = new ArrayList<Map<String,Object>>();
		for(RedhawkStruct s : getStatus()){
			List<String> allocIds = getAllocIds(s);
			if(allocIds == null || allocIds.isEmpty()){
				tuners.add(s.toMap());
			}
		}
		return tuners;
	}

	public List<Map<String, Object>> getUsedTuners() {
//		return getStatus().stream().filter(x -> !getAllocId(x).isEmpty()).map(x -> x.toMap()).collect(Collectors.toList());
		
		List<Map<String, Object>> tuners = new ArrayList<Map<String,Object>>();
		for(RedhawkStruct s : getStatus()){
			String allocId = getAllocId(s);
			if(allocId!=null && !allocId.isEmpty()){
				tuners.add(s.toMap());
			}
		}
		
		return tuners;
	}

	@Override
	public RedhawkLogLevel getLogLevel() {
		return RedhawkLogLevel.reverseLookup(getCorbaObject().log_level());
	}

	@Override
	public void setLogLevel(RedhawkLogLevel level) {
		getCorbaObject().log_level(level.getValue());		
	}

	@Override
	public AdminState adminState() {
		return AdminState.reverseLookup(getCorbaObject().adminState().value());
	}

	@Override
	public void adminState(AdminState state) {
		getCorbaObject().adminState(AdminType.from_int(state.getValue()));
	}

	@Override
	public OperationalState operationalState() {
		return OperationalState.reverseLookup(getCorbaObject().operationalState().value());
	}

	
//	RedhawkTuner getTuner(String allocId) {
//		Map<String, Object> tunerStatus = getTunerById(allocId);
//		return tunerStatus == null ? null : new RedhawkTunerImpl(this, tunerStatus);
//	}
	
//	RedhawkTuner tune(Map<String, Object> props) {
//		Map<String, Object> allocation = new HashMap<String, Object>();
//		for (String key : props.keySet())
//			allocation.put("FRONTEND::tuner_allocation::" + key, props.get(key));
//		String allocId = allocate(allocation);
//		if (allocId == null) return null; // allocate doesn't propocate the exceptions... consider having them flow through
//		return getTuner(allocId);
//	}	
	
	
}
