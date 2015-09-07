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
    
    DataTypes(String poaTieClass, String operationsClass){
        this.poaTieClass = poaTieClass;
        this.operationsClass = operationsClass;
    }
    
}
