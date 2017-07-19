package redhawk.rest.utils;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class TestUtils {
	public static String getStringFromJAXB(Object obj) throws JAXBException{
        StringWriter stringWriter = new StringWriter();			
		try {
			JAXBContext jc = JAXBContext.newInstance(new Class[]{obj.getClass()});
			
			Marshaller m = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(obj, stringWriter);
			
		} catch (JAXBException e) {
			throw new JAXBException("Issue getting String ", e);
		}
		return stringWriter.toString();
	}
}
