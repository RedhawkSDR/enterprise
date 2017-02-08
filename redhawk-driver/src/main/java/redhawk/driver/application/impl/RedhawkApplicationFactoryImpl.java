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
package redhawk.driver.application.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import CF.ApplicationFactory;
import CF.ApplicationFactoryHelper;
import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.application.RedhawkApplicationFactory;
import redhawk.driver.base.impl.CorbaBackedObject;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.ResourceNotFoundException;

public class RedhawkApplicationFactoryImpl extends CorbaBackedObject<ApplicationFactory> implements RedhawkApplicationFactory {

    private RedhawkDomainManager domainManager;
    private String identifier;
    
    private Map<String, RedhawkApplication> applicationInstances = new ConcurrentHashMap<String, RedhawkApplication>();
    
	public RedhawkApplicationFactoryImpl(RedhawkDomainManager domainManager, String applicationFactoryIor, String identifier){
        super(applicationFactoryIor, domainManager.getDriver().getOrb());
        this.domainManager = domainManager;
        this.identifier = identifier;
    }
    
	public Map<String, RedhawkApplication> getApplicationInstances() {
		return applicationInstances;
	}
	
    public ApplicationFactory getCorbaObj(){
        return getCorbaObject();
    }
    
    public String getName(){
        return getCorbaObject().name();
    }
    
    public String getIdentifier(){
        return getCorbaObject().identifier();
    }
    
    public RedhawkDomainManager getRedhawkDomainManager(){
        return domainManager;
    }

    public String getSoftwareProfile() {
        return getCorbaObject().softwareProfile();
    }    
    
    public void release(){
    	getCorbaObject()._release();
    	applicationInstances.clear();
    }
    
//    public RedhawkApplication createApplication(String instanceName, Map<String, Object> initialConfiguration, List<RedhawkDeviceAssignment> deviceAssignments ) throws ApplicationCreationException {
//    	try {
//			try {
//				return domainManager.getApplicationByName(instanceName);
//			} catch (ResourceNotFoundException e) {
//				//ignore
//			}
//		} catch (MultipleResourceException e1) {
//			throw new ApplicationCreationException(e1);
//		}
//    	
//    	//convert initialConfiguration
//    	//convert DeviceAssignments
//    	DataType[] initConfig = new DataType[]{};
//    	DeviceAssignmentType[] deviceAssignmentTypes = new DeviceAssignmentType[]{};
//    	
//    	Application applicationInstance;
//		try {
//			applicationInstance = getCorbaObject().create(instanceName, initConfig, deviceAssignmentTypes);
//			RedhawkApplication appToReturn = new RedhawkApplicationImpl(domainManager, getOrb().object_to_string(applicationInstance), applicationInstance.identifier());
//			applicationInstances.put(instanceName, appToReturn);
//			return appToReturn;
//		} catch (CreateApplicationError | CreateApplicationRequestError | CreateApplicationInsufficientCapacityError | InvalidInitConfiguration e) {
//			throw new ApplicationCreationException(e);
//		}
//    }

	@Override
	protected ApplicationFactory locateCorbaObject() throws ResourceNotFoundException {
		String ior;
		try {
			ior = ((RedhawkApplicationFactoryImpl) domainManager.getApplicationFactoryByIdentifier(identifier)).getIor();
			return (ApplicationFactory) getOrb().string_to_object(ior);
		} catch (MultipleResourceException e) {
			throw new ResourceNotFoundException(e);
		}
	}

	@Override
	public Class<?> getHelperClass() {
		return ApplicationFactoryHelper.class;
	}
    

}