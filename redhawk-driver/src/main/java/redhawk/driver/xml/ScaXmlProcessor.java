package redhawk.driver.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

public class ScaXmlProcessor {
	private static Logger logger = Logger.getLogger(ScaXmlProcessor.class.getName());
	
	public static <T> T unmarshal(InputStream xml, Class<T> schemaClass) throws JAXBException, SAXException {
		
		RedbusRedhawkScaXmlType type = RedbusRedhawkScaXmlType.getBySchemaClass(schemaClass);
		String schemaFileName = type.getSchemaFile();
		String schemaContextPath = schemaClass.getPackage().getName();
		JAXBContext context = JAXBContextCacheUtil.getInstance().getContext(schemaContextPath);
		SchemaFactory sf = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		Schema schema = sf.newSchema(new StreamSource(ScaXmlProcessor.class.getClassLoader().getResourceAsStream(schemaFileName)));
		
		try {
			 XMLInputFactory factory = XMLInputFactory.newInstance();
			 factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
			 XMLEventReader eventReader = factory.createXMLEventReader(xml);
	         XMLEventReader e = new NamespaceAddingEventReader(eventReader, type.getXmlns());

	         Unmarshaller unmarshaller = context.createUnmarshaller();
	         unmarshaller.setSchema(schema);
	         JAXBElement<T> element = unmarshaller.unmarshal(e, schemaClass);
	         return element.getValue();
			
		} catch(Exception e){
			logger.severe(e.getMessage());
			throw new SAXException(e);
		}
		
	}
	
	
	public static <T> void marshalToFile(T scaObject, String componentName, String fileLocation, boolean includeNamespace) throws XMLStreamException, FactoryConfigurationError, JAXBException, IOException {
		String xmlFileExtension = RedbusRedhawkScaXmlType.getBySchemaClass(scaObject.getClass()).getXmlFileExtension();
		String marshalledXmlFileName = componentName + "." + xmlFileExtension;
		File prfFileDirectory = new File(fileLocation);
		prfFileDirectory.mkdirs();

		File xmlFile = new File(fileLocation + File.separator + marshalledXmlFileName);

//        XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(new FileWriter(xmlFile));
//        if (!includeNamespace) {
//        	writer.setNamespaceContext(NoNamesWriter.emptyNamespaceContext);
//        }		
		
		JAXBContext context = JAXBContextCacheUtil.getInstance().getContext(scaObject.getClass().getPackage().getName());
		Marshaller marshaller = context.createMarshaller();
		
		marshaller.marshal(scaObject, IndentingXMLStreamWriter.filter(new FileWriter(xmlFile)));
	}
	
	public static <T> String marshalToString(T scaObject, boolean includeNamespace) throws XMLStreamException, FactoryConfigurationError, JAXBException {
        StringWriter stringWriter = new StringWriter();
        XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(stringWriter);
        if (!includeNamespace) {
        	writer.setNamespaceContext(new NamespaceContext() {
        		@Override
        		public Iterator<String> getPrefixes(String namespaceURI) {
        			return null;
        		}

        		@Override
        		public String getPrefix(String namespaceURI) {
        			return "";
        		}

        		@Override
        		public String getNamespaceURI(String prefix) {
        			return null;
        		}
        	});
        }

        JAXBContext context = JAXBContextCacheUtil.getInstance().getContext(scaObject.getClass().getPackage().getName());
        Marshaller marshaller = context.createMarshaller();
        IndentingXMLStreamWriter sw = new IndentingXMLStreamWriter(writer);
        marshaller.marshal(scaObject, sw);
        return stringWriter.toString();
	}
}
