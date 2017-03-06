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
package redhawk.driver.devicemanager.impl;

import org.omg.CORBA.Context;
import org.omg.CORBA.ContextList;
import org.omg.CORBA.DomainManager;
import org.omg.CORBA.ExceptionList;
import org.omg.CORBA.InterfaceDef;
import org.omg.CORBA.NVList;
import org.omg.CORBA.NamedValue;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.Policy;
import org.omg.CORBA.PolicyListHolder;
import org.omg.CORBA.Request;
import org.omg.CORBA.SetOverrideType;

import CF.DataType;
import CF.Device;
import CF.DeviceManager;
import CF.FileSystem;
import CF.InvalidIdentifier;
import CF.InvalidObjectReference;
import CF.PropertiesHolder;
import CF.UnknownProperties;
import CF.DeviceManagerPackage.ServiceType;
import CF.PortSetPackage.PortInfoType;
import CF.PortSupplierPackage.UnknownPort;
import CF.PropertyEmitterPackage.AlreadyInitialized;
import CF.PropertySetPackage.InvalidConfiguration;
import CF.PropertySetPackage.PartialConfiguration;

//TODO" What is this class for????
public class DummyDeviceManagerForCleanupOnly implements DeviceManager {

	private String identifier;
	
	public DummyDeviceManagerForCleanupOnly(String identifier) {
		this.identifier = identifier;
	}
	
	@Override
	public String deviceConfigurationProfile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileSystem fileSys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String identifier() {
		return identifier;
	}

	@Override
	public String label() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Device[] registeredDevices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceType[] registeredServices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerDevice(Device registeringDevice) throws InvalidObjectReference {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unregisterDevice(Device registeredDevice) throws InvalidObjectReference {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerService(Object registeringService, String name) throws InvalidObjectReference {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unregisterService(Object unregisteringService, String name) throws InvalidObjectReference {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getComponentImplementationId(String componentInstantiationId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void configure(DataType[] configProperties) throws InvalidConfiguration, PartialConfiguration {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void query(PropertiesHolder configProperties) throws UnknownProperties {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getPort(String name) throws UnknownPort {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean _is_a(String repositoryIdentifier) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean _is_equivalent(Object other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean _non_existent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int _hash(int maximum) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object _duplicate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void _release() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public Object _get_interface_def() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*Methods no longer necessary.
	@Override
	public InterfaceDef _get_interface() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String _repository_id() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Object _set_policy_overrides(Policy[] policies, SetOverrideType set_add) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Policy _get_client_policy(int type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Policy[] _get_policy_overrides(int[] types) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean _validate_connection(PolicyListHolder inconsistent_policies) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object _get_component() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ORB _get_orb() {
		// TODO Auto-generated method stub
		return null;
	}
	*End of Methods no longer necessary
	*/

	@Override
	public Request _request(String operation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Request _create_request(Context ctx, String operation, NVList arg_list, NamedValue result) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Request _create_request(Context ctx, String operation, NVList arg_list, NamedValue result,
			ExceptionList exclist, ContextList ctxlist) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Policy _get_policy(int policy_type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DomainManager[] _get_domain_managers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object _set_policy_override(Policy[] policies, SetOverrideType set_add) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CF.DomainManager domMgr() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initializeProperties(DataType[] initialProperties)
			throws AlreadyInitialized, InvalidConfiguration, PartialConfiguration {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String registerPropertyListener(Object obj, String[] prop_ids, float interval)
			throws UnknownProperties, InvalidObjectReference {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void unregisterPropertyListener(String id) throws InvalidIdentifier {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PortInfoType[] getPortSet() {
		// TODO Auto-generated method stub
		return null;
	}

}
