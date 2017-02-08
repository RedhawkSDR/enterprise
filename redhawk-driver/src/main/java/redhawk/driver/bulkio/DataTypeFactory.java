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
package redhawk.driver.bulkio;

import java.util.logging.Logger;

import BULKIO.PortStatistics;
import BULKIO.dataChar;
import BULKIO.dataCharHelper;
import BULKIO.dataDouble;
import BULKIO.dataDoubleHelper;
import BULKIO.dataFile;
import BULKIO.dataFileHelper;
import BULKIO.dataFloat;
import BULKIO.dataFloatHelper;
import BULKIO.dataLong;
import BULKIO.dataLongHelper;
import BULKIO.dataLongLong;
import BULKIO.dataLongLongHelper;
import BULKIO.dataOctet;
import BULKIO.dataOctetHelper;
import BULKIO.dataSDDS;
import BULKIO.dataSDDSHelper;
import BULKIO.dataShort;
import BULKIO.dataShortHelper;
import BULKIO.dataUlong;
import BULKIO.dataUlongHelper;
import BULKIO.dataUlongLong;
import BULKIO.dataUlongLongHelper;
import BULKIO.dataUshort;
import BULKIO.dataUshortHelper;
import BULKIO.dataXML;
import BULKIO.dataXMLHelper;

/**
 * Factory object for holding the narrowed instance 
 * of the Provides Port on a component. 
 *
 */
public class DataTypeFactory {
	private static Logger logger = Logger.getLogger(DataTypeFactory.class.getName());

	private dataChar dataCharObj =  null; 
	
	private dataDouble dataDoubleObj = null;
	
	private dataLong dataLongObj = null; 

	private dataOctet datOctetObj = null;
	
	private dataShort dataShortObj = null; 
	
	private dataUshort dataUShortObj = null; 
	
	private dataXML dataXMLObj = null; 
	
	private dataFile dataFileObj = null; 
	
	private dataFloat dataFloatObj = null; 
	
	private dataUlong dataULongObj = null; 
	
	private dataUlongLong dataUlongLongObj = null; 
	
	private dataLongLong dataLongLongObj = null;
	
	private dataSDDS dataSDDSObj = null; 

	private DataTypes operationsType = null; 
	
	public DataTypeFactory(org.omg.CORBA.Object port){
		/*
		 * Figure out which class it is 
		 */
		for(DataTypes type : DataTypes.values()){
			try{
				switch(type){
				case DATA_CHAR:
					dataCharObj = dataCharHelper.narrow(port);
					operationsType = type;
					break; 
				case DATA_DOUBLE: 
					dataDoubleObj = dataDoubleHelper.narrow(port); 
					operationsType = type;
					break; 
				case DATA_LONG:
					dataLongObj = dataLongHelper.narrow(port); 
					operationsType = type;
					break; 
				case DATA_OCTET:
					datOctetObj = dataOctetHelper.narrow(port); 
					operationsType = type;
					break; 
				case DATA_SHORT:
					dataShortObj = dataShortHelper.narrow(port);
					operationsType = type;
					break; 
				case DATA_ULONG:
					dataULongObj = dataUlongHelper.narrow(port);
					operationsType = type;
					break; 
				case DATA_USHORT: 
					dataUShortObj = dataUshortHelper.narrow(port); 
					operationsType = type;
					break; 
				case DATA_XML:
					dataXMLObj = dataXMLHelper.narrow(port);
					operationsType = type;
					break;
				case DATA_FILE:
					dataFileObj = dataFileHelper.narrow(port);
					operationsType = type;
					break; 
				case DATA_FLOAT:
					dataFloatObj = dataFloatHelper.narrow(port); 
					operationsType = type;
					break; 
				case DATA_ULONGLONG:
					dataUlongLongObj = dataUlongLongHelper.narrow(port);
					operationsType = type;
					break;
				case DATA_LONGLONG:
					dataLongLongObj = dataLongLongHelper.narrow(port);
					operationsType = type;
					break; 
				case DATA_SDDS:
					dataSDDSObj = dataSDDSHelper.narrow(port); 
					operationsType = type; 
					break; 
				default:
					logger.fine(operationsType+" does not have a push packet method");
				}
			}catch(Exception e){
				logger.fine(e.getMessage());
			}
			
			/*
			 * Breaking out of loop found operations type
			 */
			if(operationsType!=null){
				logger.fine("Found type for port "+operationsType);
				break;
			}
		}
	}

	public dataChar getDataCharObj() {
		return dataCharObj;
	}

	public dataDouble getDataDoubleObj() {
		return dataDoubleObj;
	}

	public dataLong getDataLongObj() {
		return dataLongObj;
	}

	public dataOctet getDatOctetObj() {
		return datOctetObj;
	}

	public dataShort getDataShortObj() {
		return dataShortObj;
	}

	public dataUshort getDataUShortObj() {
		return dataUShortObj;
	}

	public dataXML getDataXMLObj() {
		return dataXMLObj;
	}

	public dataFile getDataFileObj() {
		return dataFileObj;
	}

	public dataFloat getDataFloatObj() {
		return dataFloatObj;
	}

	public dataUlong getDataULongObj() {
		return dataULongObj;
	}

	public dataUlongLong getDataUlongLongObj() {
		return dataUlongLongObj;
	}

	public dataLongLong getDataLongLongObj() {
		return dataLongLongObj;
	}

	public DataTypes getOperationsType() {
		return operationsType;
	}
	
	public dataSDDS getDataSDDSObj() {
		return dataSDDSObj;
	}
	
	public PortStatistics getStatistics(){		
		switch(operationsType){
		case DATA_CHAR: return dataCharObj.statistics(); 
		case DATA_DOUBLE: return dataDoubleObj.statistics(); 
		case DATA_LONG: return dataLongObj.statistics(); 
		case DATA_OCTET: return datOctetObj.statistics(); 
		case DATA_SHORT: return dataShortObj.statistics(); 
		case DATA_ULONG: return dataULongObj.statistics(); 
		case DATA_USHORT: return dataUShortObj.statistics();  
		case DATA_XML: return dataXMLObj.statistics(); 
		case DATA_FILE: return dataFileObj.statistics(); 
		case DATA_FLOAT: return dataFloatObj.statistics(); 
		case DATA_ULONGLONG: return dataUlongLongObj.statistics(); 
		case DATA_LONGLONG: return dataLongLongObj.statistics(); 
		case DATA_SDDS: return dataSDDSObj.statistics(); 
		default:
			logger.severe("INVALID OPERATIONS TYPE!!!");
		}
		
		return null; 
	}
}
