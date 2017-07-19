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
package redhawk.rest;

import redhawk.driver.application.RedhawkApplication;
import redhawk.driver.component.RedhawkComponent;
import redhawk.driver.device.RedhawkDevice;
import redhawk.driver.devicemanager.RedhawkDeviceManager;
import redhawk.driver.domain.RedhawkDomainManager;
import redhawk.driver.eventchannel.RedhawkEventChannel;
import redhawk.driver.eventchannel.RedhawkEventChannelManager;
import redhawk.driver.eventchannel.impl.RedhawkEventRegistrant;
import redhawk.driver.exceptions.MultipleResourceException;
import redhawk.driver.exceptions.PortException;
import redhawk.driver.exceptions.ResourceNotFoundException;
import redhawk.driver.port.RedhawkPort;
import redhawk.driver.port.impl.RedhawkExternalPortImpl;
import redhawk.driver.properties.*;
import redhawk.driver.xml.model.sca.prf.Properties;
import redhawk.driver.xml.model.sca.prf.*;
import redhawk.rest.model.*;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DomainConverter {

	private static Logger logger = Logger.getLogger(DomainConverter.class.getName());

	protected Domain convertDomain(RedhawkDomainManager domainManager) {
		return convertDomain(domainManager, FetchMode.EAGER);
	}

	protected Domain convertDomain(RedhawkDomainManager domainManager, FetchMode fetchMode) {
		Domain domain = new Domain();
		domain.setIdentifier(domainManager.getIdentifier());
		domain.setName(domainManager.getName());

		if (fetchMode.equals(FetchMode.EAGER)) {

			try {
				domain.setProperties(
						convertProperties(domainManager.getProperties(), domainManager.getPropertyConfiguration()));
			} catch (ResourceNotFoundException e) {
				domain.setProperties(new ArrayList<>());
			}

			domain.setEventChannels(convertEventChannels(domainManager.getEventChannelManager().getEventChannels()));

			domain.setDeviceManagers(domainManager.getDeviceManagers().parallelStream().map(this::convertDeviceManager)
					.collect(Collectors.toList()));

			domain.setApplications(domainManager.getApplications().parallelStream().map(this::convertApplication)
					.collect(Collectors.toList()));
		}
		return domain;
	}

	protected List<Property> convertProperties(Map<String, RedhawkProperty> props, Properties propertyConfiguration) {
		List<Property> properties = new ArrayList<Property>();
		Map<String, RedhawkProperty> propertyClone = new HashMap<>();
		propertyClone.putAll(props);

		if (propertyConfiguration != null) {
			List<Object> stuff = propertyConfiguration.getSimplesAndSimplesequencesAndTests();
			for (Object o : stuff) {
				switch (o.getClass().getSimpleName()) {
				case "Simple": {
					Simple simp = (Simple) o;
					RedhawkProperty rhProp = props.get(simp.getId());
					if (propertyClone.get(simp.getId()) != null) {
						propertyClone.remove(simp.getId());
					}
					Property prop = convertSimple(simp.getId(), (RedhawkSimple) rhProp, simp);
					properties.add(prop);
					break;
				}
				case "SimpleSequence": {
					SimpleSequence simp = (SimpleSequence) o;
					RedhawkProperty rhProp = props.get(simp.getId());
					if (propertyClone.get(simp.getId()) != null) {
						propertyClone.remove(simp.getId());
					}
					Property prop = convertSimpleSequence(simp.getId(), (RedhawkSimpleSequence) rhProp, simp);
					properties.add(prop);
					break;
				}
				case "Struct": {
					Struct struct = (Struct) o;
					RedhawkProperty rhProp = props.get(struct.getId());
					if (propertyClone.get(struct.getId()) != null) {
						propertyClone.remove(struct.getId());
					}
					Property prop = convertStruct(struct.getId(), (RedhawkStruct) rhProp, struct);
					properties.add(prop);
					break;
				}
				case "StructSequence": {
					StructSequence structSequence = (StructSequence) o;
					RedhawkProperty rhProp = props.get(structSequence.getId());
					if (propertyClone.get(structSequence.getId()) != null) {
						propertyClone.remove(structSequence.getId());
					}
					Property prop = convertStructSequence(structSequence.getId(), (RedhawkStructSequence) rhProp,
							structSequence);
					properties.add(prop);
					break;
				}
				}
			}
		}

		return properties;
	}

	/**
	 * Utility method to convert a Property to
	 * 
	 * @param property
	 * @param o
	 * @return
	 */
	protected Property convertProperty(String propertyId, RedhawkProperty property, Object o) {
		Property prop = null; 
		switch (o.getClass().getSimpleName()) {
		case "Simple": {
			Simple simp = (Simple) o;
			
			//Make sure propertyId matched 
			if(simp.getId().equals(propertyId))
				prop = convertSimple(simp.getId(), (RedhawkSimple) property, simp);
			
			break;
		}
		case "SimpleSequence": {
			SimpleSequence simp = (SimpleSequence) o;
			
			if(simp.getId().equals(propertyId))
				prop = convertSimpleSequence(simp.getId(), (RedhawkSimpleSequence) property, simp);
			
			break;
		}
		case "Struct": {
			Struct struct = (Struct) o;
			
			if(struct.getId().equals(propertyId))
				prop = convertStruct(struct.getId(), (RedhawkStruct) property, struct);
			
			break;
		}
		case "StructSequence": {
			StructSequence structSequence = (StructSequence) o;
			
			if(structSequence.getId().equals(propertyId))
				prop = convertStructSequence(structSequence.getId(), (RedhawkStructSequence) property,
					structSequence);
			
			break;
		}
		default: {
			logger.warning("Unhandled Redhawk Property type " + o);
			break;
		}
		}
		
		return prop;
	}

	private Property convertStruct(String id, RedhawkStruct rhProp, Struct original) {
		StructRep destination = new StructRep();
		destination.setType("struct");
		destination.setDescription(original.getDescription());
		destination.setId(original.getId());
		destination.setMode(original.getMode());
		destination.setName(original.getName());

		List<Property> attributes = new ArrayList<>();

		for (Object o : original.getSimplesAndSimplesequences()) {
			switch (o.getClass().getSimpleName()) {
			case "Simple": {
				Simple simple = (Simple) o;
				SimpleRep s = convertSimple(simple.getId(), null, simple);
				if (rhProp != null && rhProp.size() > 0) {
					Object obj = rhProp.get(simple.getId());
					if (obj != null) {
						s.setValue(obj + "");
						s.setDataType(obj.getClass().getName());
					}
				}

				attributes.add(s);
				break;
			}
			case "SimpleSequence": {
				SimpleSequence simple = (SimpleSequence) o;
				SimpleSequenceRep s = convertSimpleSequence(simple.getId(), null, simple);
				if (rhProp != null && rhProp.size() > 0) {
					Object obj = rhProp.get(simple.getId());
					if (obj != null) {
						s.setValues(Arrays.asList(obj));
						s.setDataType(obj.getClass().getName());
					}
				}

				attributes.add(s);
				break;
			}
			}
		}

		destination.setAttributes(attributes);
		return destination;
	}

	private SimpleRep convertSimple(String propertyId, RedhawkSimple redhawkProperty, Simple original) {
		SimpleRep destination = new SimpleRep();
		destination.setType("simple");
		destination.setAction(original.getAction());
		destination.setCommandline(original.getCommandline());
		destination.setComplex(original.getComplex());
		destination.setDescription(original.getDescription());
		destination.setEnumerations(original.getEnumerations());
		destination.setId(original.getId());
		destination.setMode(original.getMode());
		destination.setName(original.getName());
		destination.setOptional(original.getOptional());
		destination.setRange(original.getRange());
		destination.setPropertyValueType(original.getType());
		destination.setUnits(original.getUnits());
		destination.setValue(original.getValue());

		if (redhawkProperty != null && redhawkProperty.getValue() != null) {
			destination.setId(propertyId);
			destination.setDataType(redhawkProperty.getValue().getClass().getName());
			destination.setValue(redhawkProperty.getValue() + "");
		}

		return destination;
	}

	private SimpleSequenceRep convertSimpleSequence(String propertyId, RedhawkSimpleSequence redhawkProperty,
			SimpleSequence original) {
		SimpleSequenceRep destination = new SimpleSequenceRep();
		destination.setType("simplesequence");
		destination.setDescription(original.getDescription());
		destination.setRange(original.getRange());
		destination.setPropertyValueType(original.getType());
		destination.setUnits(original.getUnits());
		destination.setAction(original.getAction());
		destination.setId(original.getId());
		destination.setMode(original.getMode());
		destination.setName(original.getName());
		destination.setOptional(original.getOptional());
		destination.setComplex(original.getComplex());
		destination.setId(propertyId);

		if (redhawkProperty != null && redhawkProperty.getValues() != null && redhawkProperty.getValues().size() > 0) {
			destination.setValues(redhawkProperty.getValues());
			destination.setDataType(redhawkProperty.getValues().get(0).getClass().getName());
		} else if (original.getValues() != null) {
			// Need to check for null can't just assume values are there...
			Values values = original.getValues();
			List<Object> objects = values.getValues().stream().map(s -> {
				return (Object) s;
			}).collect(Collectors.toList());
			destination.setValues(objects);
		}

		return destination;
	}

	private StructSequenceRep convertStructSequence(String propertyId, RedhawkStructSequence redhawkProperty,
			StructSequence original) {
		StructSequenceRep destination = new StructSequenceRep();
		destination.setType("structsequence");
		destination.setDescription(original.getDescription());
		destination.setId(original.getId());
		destination.setMode(original.getMode());
		destination.setName(original.getName());
		destination.setId(propertyId);

		List<Property> structs = new ArrayList<>();

		if (redhawkProperty != null && redhawkProperty.getStructs() != null) {
			for (RedhawkStruct struct : redhawkProperty.getStructs()) {
				Property s = convertStruct(original.getStruct().getId(), struct, original.getStruct());
				structs.add(s);
			}
			destination.setStructs(structs);
		}

		return destination;
	}

	public Object convertAll(String type, List<Object> list, FetchMode fetchMode) {
		switch (type) {
		case "domain": {
			return list.parallelStream().map(obj -> convertDomain((RedhawkDomainManager) obj, fetchMode))
					.collect(Collectors.toList());
		}
		case "application": {
			return list.parallelStream().map(obj -> convertApplication((RedhawkApplication) obj, fetchMode))
					.collect(Collectors.toList());
		}
		case "component": {
			return list.stream().map(obj -> convertComponent((RedhawkComponent) obj, fetchMode))
					.collect(Collectors.toList());
		}
		case "port": {
			return list.stream().map(obj -> convertPort((RedhawkPort) obj)).collect(Collectors.toList());
		}
		case "deviceport": {
			return list.stream().map(obj -> convertPort((RedhawkPort) obj)).collect(Collectors.toList());
		}
		case "applicationport": {
			return list.stream().map(obj -> convertExternalPort((RedhawkPort) obj)).collect(Collectors.toList());
		}
		case "devicemanager": {
			return list.stream().map(obj -> convertDeviceManager((RedhawkDeviceManager) obj, fetchMode))
					.collect(Collectors.toList());
		}
		case "device": {
			return list.stream().map(obj -> convertDevice((RedhawkDevice) obj, fetchMode)).collect(Collectors.toList());
		}
		case "eventchannel":{
			return list.stream().map(obj -> convertEventChannel((RedhawkEventChannel)obj)).collect(Collectors.toList());
		}
		case "softwarecomponent": {
			return list;
		}
		case "waveform": {
			return list;
		}
		}

		return null;
	}

	private Application convertApplication(RedhawkApplication obj) {
		return convertApplication(obj, FetchMode.EAGER);
	}

	private Application convertApplication(RedhawkApplication obj, FetchMode fetchMode) {
		Application app = new Application();
		app.setIdentifier(obj.getIdentifier());
		app.setName(obj.getName());
		app.setStarted(obj.isStarted());

		if (fetchMode.equals(FetchMode.EAGER)) {
			try {
				// Need to get the properties from the component
				RedhawkComponent comp = obj.getComponentByName(
						obj.getAssembly().getAssemblycontroller().getComponentinstantiationref().getRefid()+".*");

				List<Property> properties = this.convertProperties(comp.getProperties(),
						comp.getPropertyConfiguration());
				
				if(obj.getAssembly().getExternalproperties()!=null){
					for (redhawk.driver.xml.model.sca.sad.Property exProp : obj.getAssembly().getExternalproperties().getProperties()) {
						RedhawkComponent component = obj.getComponentByName(exProp.getComprefid()+".*");
						String externalPropertyId = exProp.getExternalpropid();
						String propId = exProp.getPropid();
						
						//Get the RedhawkProperty by it's Component propId
						RedhawkProperty prop = component.getProperty(propId);
						
						//Find prop configuration that matches then rollout
						for(Object rhProp : component.getPropertyConfiguration().getSimplesAndSimplesequencesAndTests()){
							Property myExProp = this.convertProperty(propId, prop, rhProp);
							
							if(exProp!=null){
								myExProp.setExternalId(externalPropertyId);
								properties.add(myExProp);
								break;
							}
						}
					}					
				}

				app.setProperties(properties);

				app.setExternalPorts(obj.getPorts().stream().map(this::convertExternalPort).collect(Collectors.toList()));
			} catch (IOException | MultipleResourceException | ResourceNotFoundException e) {
				logger.log(Level.WARNING, e.getMessage(), e);
			}

			app.setComponents(
					obj.getComponents().parallelStream().map(this::convertComponent).collect(Collectors.toList()));
		}
		return app;
	}

	private Component convertComponent(RedhawkComponent obj) {
		return convertComponent(obj, FetchMode.EAGER);

	}

	private Component convertComponent(RedhawkComponent obj, FetchMode fetchMode) {
		Component comp = new Component();
		comp.setName(obj.getName());
		comp.setStarted(obj.started());

		if (fetchMode.equals(FetchMode.EAGER)) {
			try {
				comp.setProperties(convertProperties(obj.getProperties(), obj.getPropertyConfiguration()));
			} catch (ResourceNotFoundException e) {
				logger.log(Level.WARNING, "Could not find prf file for component:" + obj.getName(), e);
				comp.setProperties(new ArrayList<>());
			}

			try {
				comp.setSoftwareComponent(obj.getSoftwareComponent());
			} catch (ResourceNotFoundException e) {
				logger.log(Level.WARNING, "Could not find scd file for component:" + obj.getName(), e);
			}

			try {
				comp.setPorts(obj.getPorts().parallelStream().map(this::convertPort).collect(Collectors.toList()));
			} catch (ResourceNotFoundException e) {
				logger.log(Level.WARNING, "Could not find scd file for component:" + obj.getName(), e);
			}
		}
		return comp;
	}

	private DeviceManager convertDeviceManager(RedhawkDeviceManager obj) {
		return convertDeviceManager(obj, FetchMode.EAGER);
	}

	private DeviceManager convertDeviceManager(RedhawkDeviceManager obj, FetchMode fetchMode) {
		DeviceManager mgr = new DeviceManager();
		mgr.setIdentifier(obj.getUniqueIdentifier());
		mgr.setLabel(obj.getName());

		if (fetchMode.equals(FetchMode.EAGER)) {
			mgr.setProperties(convertProperties(obj.getProperties(), null));

			mgr.setDevices(obj.getDevices().parallelStream().map(this::convertDevice).collect(Collectors.toList()));

			mgr.setServices(obj.getServices().stream().map(s -> {
				Service service = new Service();
				service.setName(s.getServiceName());
				return service;
			}).collect(Collectors.toList()));
		}
		return mgr;
	}

	private Device convertDevice(RedhawkDevice obj) {
		return convertDevice(obj, FetchMode.EAGER);
	}

	private Device convertDevice(RedhawkDevice obj, FetchMode fetchMode) {
		Device device = new Device();
		device.setIdentifier(obj.getIdentifier());
		device.setLabel(obj.getName());
		device.setStarted(obj.started());

		if (fetchMode.equals(FetchMode.EAGER)) {
			try {
				device.setProperties(convertProperties(obj.getProperties(), obj.getPropertyConfiguration()));
			} catch (ResourceNotFoundException e) {
				logger.log(Level.WARNING, "Could not find prf file for device: " + obj.getName(), e);
				device.setProperties(new ArrayList<Property>());
			}
		}

		return device;
	}
	
	private EventChannel convertEventChannel(RedhawkEventChannel obj){
		EventChannel channel = new EventChannel(); 
		
		channel.setName(obj.getName());
		
		//TODO: Registrant number is hidden prob should have a way to set it 
		List<String> registrants = new ArrayList<>();
		for(RedhawkEventRegistrant registrant : obj.getRegistrants(10000)){
			registrants.add(registrant.getRegistrationId());
		}
		
		channel.setRegistrantIds(registrants);
		return channel;
	}

	/**
	 * Helper method to convert RedhawkPort to Port
	 * 
	 * @param obj
	 * @return
	 */
	private Port convertPort(RedhawkPort obj) {
		Port p = new Port();
		p.setName(obj.getName());
		p.setRepId(obj.getRepId());
		p.setType(obj.getType());
		
		if(obj.getType().equalsIgnoreCase("provides")){
			try {
				p.setState(obj.getPortState().toString());
			} catch (PortException e) {
				logger.fine("Unable to get state of port "+e.getMessage());
			}
		}
		
		if(obj.getType().equalsIgnoreCase("uses")){
			try {
				p.setConnectionIds(obj.getConnectionIds());
			} catch (PortException e) {
				logger.fine("Unable to get connectionIds of port "+e.getMessage());
			}
		}

		return p;
	}
	
	//TODO: Clean this up!!!
	private ExternalPort convertExternalPort(RedhawkPort obj){
		Port port = this.convertPort(obj);
		return this.convertExternalPort((RedhawkExternalPortImpl)obj, port);
	}
	
	private ExternalPort convertExternalPort(RedhawkExternalPortImpl obj, Port port){
		ExternalPort p = new ExternalPort(port); 
		p.setExternalname(obj.getExternalName());
		p.setComponentRefId(obj.getComponentReferenceId());;
		p.setDescription(obj.getDescription());
		
		return p;
	}

	private List<EventChannel> convertEventChannels(List<RedhawkEventChannel> eventChannels){
		//TODO: Make this work 
		//eventChannels.stream().map(e -> convertEventChannel(e)).map(Collectors.toList());
		List<EventChannel> channels = new ArrayList<>();
		
		for(RedhawkEventChannel channel : eventChannels){
			channels.add(convertEventChannel(channel));
		}
		
		return channels;
	}
	
	@Deprecated
	private List<String> convertEventChannels(RedhawkDomainManager domainManager) {
		try {
			return domainManager.getEventChannelManager().getEventChannels().stream().map(e -> e.getName())
					.collect(Collectors.toList());
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Could not get Event Channels: ", e);
			return new ArrayList<String>();
		}
	}

	public Object convert(String type, Object object) throws ResourceNotFoundException, IOException {
		switch (type) {
		case "domain":
			return convertDomain((RedhawkDomainManager) object);
		case "application":
			return convertApplication((RedhawkApplication) object);
		case "component":
			return convertComponent((RedhawkComponent) object);
		case "port":
			return convertPort((RedhawkPort) object);
		case "applicationport":
			return convertExternalPort((RedhawkPort)object);
		case "devicemanager":
			return convertDeviceManager((RedhawkDeviceManager) object);
		case "device":
			return convertDevice((RedhawkDevice) object);
		case "eventchannel":
			return convertEventChannel((RedhawkEventChannel) object);
		case "softwarecomponent":
			return object;
		}

		return null;
	}

	public Object findPropertyInformationFromPrfFile(String id, Properties prop) {
		List<Object> objectList = prop.getSimplesAndSimplesequencesAndTests();

		for (Object obj : objectList) {
			switch (obj.getClass().getSimpleName()) {
			case "Simple":
				if (obj instanceof Simple) {
					Simple simple = (Simple) obj;
					if (simple.getId().equals(id)) {
						logger.fine("Matched on this propertyId " + id);
						return simple;
					}
				}
				break;
			case "SimpleSequence":
				if (obj instanceof SimpleSequence) {
					SimpleSequence simpleSequence = (SimpleSequence) obj;

					if (simpleSequence.getId().equals(id)) {
						logger.fine("Matched on this propertyId " + id);
						return simpleSequence;
					}
				}
				break;
			case "Struct":
				if (obj instanceof Struct) {
					Struct struct = (Struct) obj;

					if (struct.getId().equals(id)) {
						logger.fine("Matched on this propertyId " + id);
						return struct;
					}
				}
				break;
			case "StructSequence":
				if (obj instanceof StructSequence) {
					StructSequence structSequence = (StructSequence) obj;

					if (structSequence.getId().equals(id)) {
						logger.fine("Matched on this propertyId " + id);
						return structSequence;
					}
				}
				break;
			default:
				logger.severe("Undefined " + obj.getClass().getSimpleName() + " Property Id: " + id);
				break;
			}
		}

		logger.info("****Didn't find this property Id: " + id);
		return null;
	}

}