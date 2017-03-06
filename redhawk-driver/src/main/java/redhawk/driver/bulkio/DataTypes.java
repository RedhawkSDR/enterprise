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

import BULKIO.dataCharOperations;
import BULKIO.dataCharPOATie;
import BULKIO.dataDoubleOperations;
import BULKIO.dataDoublePOATie;
import BULKIO.dataFileOperations;
import BULKIO.dataFilePOATie;
import BULKIO.dataFloatOperations;
import BULKIO.dataFloatPOATie;
import BULKIO.dataLongLongOperations;
import BULKIO.dataLongLongPOATie;
import BULKIO.dataLongOperations;
import BULKIO.dataLongPOATie;
import BULKIO.dataOctetOperations;
import BULKIO.dataOctetPOATie;
import BULKIO.dataSDDSOperations;
import BULKIO.dataSDDSPOATie;
import BULKIO.dataShortOperations;
import BULKIO.dataShortPOATie;
import BULKIO.dataUlongLongOperations;
import BULKIO.dataUlongLongPOATie;
import BULKIO.dataUlongOperations;
import BULKIO.dataUlongPOATie;
import BULKIO.dataUshortOperations;
import BULKIO.dataUshortPOATie;
import BULKIO.dataXMLOperations;
import BULKIO.dataXMLPOATie;

/**
 * Enum map POA -> Operation class. 
 *
 */
public enum DataTypes {

    DATA_CHAR(dataCharPOATie.class.getName(), dataCharOperations.class.getName()),
    DATA_DOUBLE(dataDoublePOATie.class.getName(), dataDoubleOperations.class.getName()),
    DATA_LONG(dataLongPOATie.class.getName(), dataLongOperations.class.getName()),
    DATA_OCTET(dataOctetPOATie.class.getName(), dataOctetOperations.class.getName()),
    DATA_SDDS(dataSDDSPOATie.class.getName(), dataSDDSOperations.class.getName()),
    DATA_SHORT(dataShortPOATie.class.getName(), dataShortOperations.class.getName()),
    DATA_ULONG(dataUlongPOATie.class.getName(), dataUlongOperations.class.getName()),
    DATA_USHORT(dataUshortPOATie.class.getName(), dataUshortOperations.class.getName()),
    DATA_XML(dataXMLPOATie.class.getName(), dataXMLOperations.class.getName()),
    DATA_FILE(dataFilePOATie.class.getName(), dataFileOperations.class.getName()),
    DATA_FLOAT(dataFloatPOATie.class.getName(), dataFloatOperations.class.getName()),
    DATA_ULONGLONG(dataUlongLongPOATie.class.getName(), dataUlongLongOperations.class.getName()),
    DATA_LONGLONG(dataLongLongPOATie.class.getName(), dataLongLongOperations.class.getName());
    
    public String poaTieClass;
    public String operationsClass;
    
    /**
     * 
     * @param poaTieClass
     * 	String class name of POA class
     * @param operationsClass
     * 	String class name of Operation class. 
     */
    DataTypes(String poaTieClass, String operationsClass){
        this.poaTieClass = poaTieClass;
        this.operationsClass = operationsClass;
    }
    
}
