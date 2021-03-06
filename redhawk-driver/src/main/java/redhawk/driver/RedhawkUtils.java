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
package redhawk.driver;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;
import org.ossie.properties.AnyUtils;
import org.xml.sax.SAXException;

import BULKIO.StreamSRI;
import CF.DataType;
import CF.PropertiesHelper;
import CF.PropertiesHolder;
import CF.PropertySetHelper;
import CF.PropertySetOperations;
import CF.UnknownProperties;
import redhawk.driver.eventchannel.listeners.PropertyChange;
import redhawk.driver.properties.RedhawkStructSequence;
import redhawk.driver.xml.ScaXmlProcessor;
import redhawk.driver.xml.model.sca.prf.Properties;
import redhawk.driver.xml.model.sca.prf.Simple;
import redhawk.driver.xml.model.sca.prf.SimpleSequence;
import redhawk.driver.xml.model.sca.prf.Struct;
import redhawk.driver.xml.model.sca.prf.StructSequence;
import redhawk.driver.xml.model.sca.sad.Softwareassembly;

/**
 * Class for utility methods for doing things with REDHAWK. 
 */
public class RedhawkUtils {
	private static Logger logger = Logger.getLogger(RedhawkUtils.class.getName());
	
	/**
	 * Class name for struct
	 */
	public static final String structClassSimpleName = Struct.class.getSimpleName();
	
	public static final String structsequenceClassSimpleName = StructSequence.class.getSimpleName();
	
	public static final String simpleClassSimpleName = Simple.class.getSimpleName();
	
	public static final String simplesequenceClassSimpleName = SimpleSequence.class.getSimpleName();

	
	/**
	 * Pass in an {@link java.io.InputStream} for your Software Assembly Descriptor(SAD) file and get back a POJO 
	 * for that SAD file. 
	 * 
	 * @param inputStream
	 * 	Inputstream for you SAD file. 
	 * @return
	 * The {@link redhawk.driver.xml.model.sca.sad.Softwareassembly} referenced by your inputStream
	 * @throws IOException
	 */
	public static Softwareassembly unMarshalSadFile(InputStream inputStream) throws IOException {
		try {
			return ScaXmlProcessor.unmarshal(inputStream, Softwareassembly.class);
		} catch (JAXBException | SAXException e) {
			throw new IOException(e);
		}
	}	

	//Not used as far as i can tell..
//	
//	public static DataType[] convertMapToDataTypeArray(ORB orb, Map<String, Object> props) {
//		List<DataType> dataTypes = new ArrayList<DataType>();
//		for (Entry<String, Object> entry : props.entrySet()) {
//			dataTypes.add(new DataType(entry.getKey(), createAny(orb, entry.getValue())));
//		}
//		DataType[] inner = dataTypes.toArray(new DataType[dataTypes.size()]);
//		DataType[] result = new DataType[1];
//		result[0] = new DataType("FRONTEND::tuner_allocation", createAny(orb, inner));
//		
//		return result;
//	}
	
	/**
	 * Utility method for comparing {@link BULKIO.StreamSRI}
	 * 
	 * @param sri1
	 * 	A StreamSRI object
	 * @param sri2
	 * 	Another StreamSRI object to compare sri1 to.
	 * @return
	 */
	public static boolean compareStreamSRI(StreamSRI sri1, StreamSRI sri2){
		
		boolean streamComparison = sri1.streamID.equals(sri2.streamID);
		
		boolean sriData = (sri1.hversion == sri2.hversion) && (sri1.xstart == sri2.xstart)
				&& (sri1.xdelta == sri2.xdelta) && (sri1.xunits == sri2.xunits)
				&& (sri1.subsize == sri2.subsize) && (sri1.ystart == sri2.ystart)
				&& (sri1.ydelta == sri2.ydelta) && (sri1.yunits == sri2.yunits)
				&& (sri1.mode == sri2.mode) && (sri1.blocking == sri2.blocking);

		boolean keywordLength = sri1.keywords.length == sri2.keywords.length;

		boolean keywordCompare = true;

		boolean foundInKeywords = false;
		for(DataType d : sri1.keywords){
			String id = d.id;
			for(DataType p : sri2.keywords){
				if(id.equals(p.id)){
					keywordCompare = AnyUtils.compareAnys(d.value, p.value, "eq");
					foundInKeywords = true;
					break;
				}
			}
			
			if(!foundInKeywords){
				keywordCompare = false;
				break;
			}
		}

		if(keywordCompare) {
			foundInKeywords = false;
			for(DataType d : sri2.keywords){
				String id = d.id;
				for(DataType p : sri1.keywords){
					if(id.equals(p.id)){
						keywordCompare = AnyUtils.compareAnys(d.value, p.value, "eq");
						foundInKeywords = true;
						break;
					}
				}
	
				if(!foundInKeywords){
					keywordCompare = false;
					break;
				}
			}
		}
		
		
		return (streamComparison && sriData && keywordLength && keywordCompare);
	}
	
	/**
	 * Creates an {@link org.omg.CORBA.Any} from the ORB and object passed in.
	 * @param orb
	 * 	ORB object to use for creating the Any
	 * @param objectToCreate
	 * 	Object for creating the any. 
	 * @return
	 */
	public static Any createAny(ORB orb, Object objectToCreate){
		final Any any = orb.create_any();
		
		logger.log(Level.FINE, "Original Object "+objectToCreate);
        
		if(objectToCreate instanceof String){
            any.insert_string((String) objectToCreate);
        } else if(objectToCreate instanceof Short){
            any.insert_short((Short) objectToCreate);
        } else if(objectToCreate instanceof Boolean){
            any.insert_boolean((Boolean) objectToCreate);
        } else if(objectToCreate instanceof Long){
            any.insert_longlong((Long) objectToCreate);
        } else if(objectToCreate instanceof Character){
            any.insert_char((Character) objectToCreate);
        } else if(objectToCreate instanceof Double){
            any.insert_double((Double) objectToCreate);
        } else if(objectToCreate instanceof Float){
            any.insert_float((Float) objectToCreate);
        } else if(objectToCreate instanceof Integer){
            any.insert_long((Integer) objectToCreate);
        } else if(objectToCreate instanceof Byte){
            any.insert_octet((Byte) objectToCreate);
        } else if(objectToCreate instanceof Map){
            Map objMap = (Map) objectToCreate;
            List<DataType> dataTypesToInsert = new ArrayList<DataType>();
            
            for(Object key : objMap.keySet()){
                DataType dt = new DataType();
                dt.id = key+"";
                dt.value = createAny(orb, objMap.get(key));
                dataTypesToInsert.add(dt);
            }
            
            Any anyObject = orb.create_any();
            PropertiesHelper.insert(anyObject, dataTypesToInsert.toArray(new DataType[dataTypesToInsert.size()]));
            return anyObject;
            
        }

        return any;
    }
        
	/**
	 * Takes an {@link org.omg.CORBA.Any} and creates a Map with the 
	 * DataTypes id as the key and DataType value as the value. 
	 * 
	 * @param any
	 * @return
	 */
	public static Map<String, Object> convertPropertiesAnyToMap(Any any) {
		
		CF.DataType[] message = PropertiesHelper.extract(any);
		return convertDataTypeArrayToMap(message);
	}
	
	/**
	 * Helper method to convert a DataType[] to a Map
	 * @param dt
	 * @return
	 */
	public static Map<String, Object> convertDataTypeArrayToMap(DataType[] dt){
		Map<String, Object> mapObject = new HashMap<String, Object>();

		for (CF.DataType prop : dt) {
			CF.DataType[] attributes = PropertiesHelper.extract(prop.value);

			for (CF.DataType attr : attributes) {
				Object obj = AnyUtils.convertAny(attr.value);
				mapObject.put(attr.id, obj);
			}
		}

		return mapObject;
	}

	
//	- Simple
//	- Simple Seq
//	- Struct
//		- Simple
//		- SimpleSeq
//	- StructSeq
//		- Struct
//			- Simple
//			- SimpleSeq
	
	
	
	/**
	 * Utility method for creating a PropertyChange message into a Map<String, Object>  
	 * 
	 * @param message
	 * @return
	 */
	public static Map<String, Object> convertAny(PropertyChange message){

		Map<String, Object> properties = new HashMap<String, Object>();
        
        for(Entry<String, Object> entry : message.getProperties().entrySet()){
        	if(entry.getValue() instanceof DataType[]){
        		DataType[] dt = (DataType[]) entry.getValue();
        		Map<String, Object> test = new HashMap<>(); 
        		for(DataType t : dt){
        	        Object propertyValue = AnyUtils.convertAny(t.value);
        	        test.put(t.id, propertyValue);	
        		}
        		properties.put(entry.getKey(), test);
        	} else if(entry.getValue() instanceof Any[]){
        		Any[] anyObject = (Any[]) entry.getValue();
                RedhawkStructSequence sequence = new RedhawkStructSequence(null, "bs", entry.getKey(), anyObject);
                properties.put(entry.getKey(), sequence.toListOfMaps());
        	}else{
        		properties.put(entry.getKey(), entry.getValue());
        	}
        }
        
        return properties;
	}
	
	/**
	 * Constructs a map of all property names to their Id's. If the name is null, 
	 * then the id is used as the key & value for the map
	 * 
	 * @param props
	 * @return
	 */
	public static Map<String, List<String>> getPropertyNameToId(Properties props){
		Map<String, List<String>> propToNameMap = new HashMap<>();
		
		for(Object prop : props.getSimplesAndSimplesequencesAndTests()) {
			String simpleName = prop.getClass().getSimpleName();
			
			if(simpleName.equals(simpleClassSimpleName)) {
				Simple simple = getPropertyFromObject(prop);
				updatePropMap(propToNameMap, simple.getName(), simple.getId());
			}else if(simpleName.equals(simplesequenceClassSimpleName)) {
				SimpleSequence seq = getPropertyFromObject(prop);
				updatePropMap(propToNameMap, seq.getName(), seq.getId());
			}else if(simpleName.equals(structClassSimpleName)) {
				Struct struct = getPropertyFromObject(prop);
				updatePropMap(propToNameMap, struct.getName(), struct.getId());
			}else if(simpleName.equals(structsequenceClassSimpleName)) {
				StructSequence seq = getPropertyFromObject(prop);
				updatePropMap(propToNameMap, seq.getName(), seq.getId());
			}
		}
		
		return propToNameMap;
	}
	
	private static void updatePropMap(Map<String, List<String>> propMap, String name, String id) {
		//Logic to make sure no null keys
		if(name==null)
			name = id;
		
		if(propMap.containsKey(name)) {
			propMap.get(name).add(id);
		}else {
			List<String> list = new ArrayList<>();
			list.add(id);
			propMap.put(name, list);
		}
	}
	
	/**
	 * Helper method for querying property(s) based on it's IOR, name and the given orb. 
	 * @param orb
	 * @param ior
	 * @param propertyNames
	 * @return
	 */
	public static PropertiesHolder getPropertyFromCORBAObject(ORB orb, String ior, String... propertyNames) {
		PropertiesHolder ph = new PropertiesHolder();
		ph.value = new DataType[] {};

		if (propertyNames != null) {
			List<DataType> dataTypes = new ArrayList<DataType>();
			for (String propertyName : propertyNames) {
				dataTypes.add(new DataType(propertyName, orb.create_any()));
			}

			ph.value = dataTypes.toArray(new DataType[dataTypes.size()]);
		}

		try {
			PropertySetOperations properties = PropertySetHelper.narrow(orb.string_to_object(ior));
			properties.query(ph);
		} catch (UnknownProperties e) {
			logger.log(Level.FINE, "Could not find or query property: " + propertyNames, e);
			return null;
		}

		return ph;
	}

	public static <T> T getPropertyFromObject(Object prop) {
		String simpleName = prop.getClass().getSimpleName();

		if(simpleName.equals(simpleClassSimpleName)) {
			Simple simple = (Simple) prop;
			
			return (T) simple;
		}else if(simpleName.equals(simplesequenceClassSimpleName)) {
			SimpleSequence seq = (SimpleSequence) prop;
			
			return (T) seq;
		}else if(simpleName.equals(structClassSimpleName)) {
			Struct struct = (Struct) prop;
			
			return (T) struct;
		}else if(simpleName.equals(structsequenceClassSimpleName)) {
			StructSequence seq = (StructSequence) prop;
			
			return (T) seq;
		}else {
			throw new IllegalArgumentException("Object not Struct/Simple/SimpleSequence/StructSequence type, "+prop.getClass().getSimpleName());
		}
	}
}