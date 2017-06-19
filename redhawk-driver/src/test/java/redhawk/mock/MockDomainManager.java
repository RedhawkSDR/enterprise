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
package redhawk.mock;

import org.omg.CORBA.Context;
import org.omg.CORBA.ContextList;
import org.omg.CORBA.ExceptionList;
import org.omg.CORBA.IntHolder;
import org.omg.CORBA.InterfaceDef;
import org.omg.CORBA.NVList;
import org.omg.CORBA.NamedValue;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.Policy;
import org.omg.CORBA.PolicyListHolder;
import org.omg.CORBA.Request;
import org.omg.CORBA.SetOverrideType;

import CF.AllocationManager;
import CF.Application;
import CF.ApplicationFactory;
import CF.ConnectionManager;
import CF.DataType;
import CF.Device;
import CF.DeviceAssignmentType;
import CF.DeviceManager;
import CF.DomainManager;
import CF.EventChannelManager;
import CF.FileManager;
import CF.InvalidFileName;
import CF.InvalidObjectReference;
import CF.InvalidProfile;
import CF.LogEvent;
import CF.PropertiesHolder;
import CF.UnknownIdentifier;
import CF.UnknownProperties;
import CF.ApplicationFactoryPackage.CreateApplicationError;
import CF.ApplicationFactoryPackage.CreateApplicationInsufficientCapacityError;
import CF.ApplicationFactoryPackage.CreateApplicationRequestError;
import CF.ApplicationFactoryPackage.InvalidInitConfiguration;
import CF.DomainManagerPackage.AlreadyConnected;
import CF.DomainManagerPackage.ApplicationAlreadyInstalled;
import CF.DomainManagerPackage.ApplicationInstallationError;
import CF.DomainManagerPackage.ApplicationUninstallationError;
import CF.DomainManagerPackage.DeviceManagerNotRegistered;
import CF.DomainManagerPackage.InvalidEventChannelName;
import CF.DomainManagerPackage.InvalidIdentifier;
import CF.DomainManagerPackage.NotConnected;
import CF.DomainManagerPackage.RegisterError;
import CF.DomainManagerPackage.UnregisterError;
import CF.PropertyEmitterPackage.AlreadyInitialized;
import CF.PropertySetPackage.InvalidConfiguration;
import CF.PropertySetPackage.PartialConfiguration;

public class MockDomainManager implements DomainManager {

	@Override
	public String domainManagerProfile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeviceManager[] deviceManagers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Application[] applications() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApplicationFactory[] applicationFactories() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileManager fileMgr() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AllocationManager allocationMgr() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConnectionManager connectionMgr() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EventChannelManager eventChannelMgr() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String identifier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DomainManager[] remoteDomainManagers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerDevice(Device registeringDevice, DeviceManager registeredDeviceMgr)
			throws InvalidObjectReference, InvalidProfile, DeviceManagerNotRegistered, RegisterError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerDeviceManager(DeviceManager deviceMgr)
			throws InvalidObjectReference, InvalidProfile, RegisterError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unregisterDeviceManager(DeviceManager deviceMgr) throws InvalidObjectReference, UnregisterError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unregisterDevice(Device unregisteringDevice) throws InvalidObjectReference, UnregisterError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Application createApplication(String profileFileName, String name, DataType[] initConfiguration,
			DeviceAssignmentType[] deviceAssignments)
					throws InvalidProfile, InvalidFileName, CreateApplicationError, CreateApplicationRequestError,
					CreateApplicationInsufficientCapacityError, InvalidInitConfiguration {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void installApplication(String profileFileName)
			throws InvalidProfile, InvalidFileName, ApplicationInstallationError, ApplicationAlreadyInstalled {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void uninstallApplication(String applicationId) throws InvalidIdentifier, ApplicationUninstallationError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerService(Object registeringService, DeviceManager registeredDeviceMgr, String name)
			throws InvalidObjectReference, DeviceManagerNotRegistered, RegisterError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unregisterService(Object unregisteringService, String name)
			throws InvalidObjectReference, UnregisterError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerWithEventChannel(Object registeringObject, String registeringId, String eventChannelName)
			throws InvalidObjectReference, InvalidEventChannelName, AlreadyConnected {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unregisterFromEventChannel(String unregisteringId, String eventChannelName)
			throws InvalidEventChannelName, NotConnected {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerRemoteDomainManager(DomainManager registeringDomainManager)
			throws InvalidObjectReference, RegisterError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unregisterRemoteDomainManager(DomainManager unregisteringDomainManager)
			throws InvalidObjectReference, UnregisterError {
		// TODO Auto-generated method stub
		
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
	public void unregisterPropertyListener(String id) throws CF.InvalidIdentifier {
		// TODO Auto-generated method stub
		
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

	//@Override
	public InterfaceDef _get_interface() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object _get_interface_def() {
		// TODO Auto-generated method stub
		return null;
	}

	//@Override
	public String _repository_id() {
		// TODO Auto-generated method stub
		return null;
	}

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
	public org.omg.CORBA.DomainManager[] _get_domain_managers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object _set_policy_override(Policy[] policies, SetOverrideType set_add) {
		// TODO Auto-generated method stub
		return null;
	}

	//@Override
	public Object _set_policy_overrides(Policy[] policies, SetOverrideType set_add) {
		// TODO Auto-generated method stub
		return null;
	}

	//@Override
	public Policy _get_client_policy(int type) {
		// TODO Auto-generated method stub
		return null;
	}

	//@Override
	public Policy[] _get_policy_overrides(int[] types) {
		// TODO Auto-generated method stub
		return null;
	}

	//@Override
	public boolean _validate_connection(PolicyListHolder inconsistent_policies) {
		// TODO Auto-generated method stub
		return false;
	}

	//@Override
	public Object _get_component() {
		// TODO Auto-generated method stub
		return null;
	}

	//@Override
	public ORB _get_orb() {
		// TODO Auto-generated method stub
		return null;
	}

}
