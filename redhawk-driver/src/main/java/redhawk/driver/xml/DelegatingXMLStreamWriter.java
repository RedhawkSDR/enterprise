package redhawk.driver.xml;

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class DelegatingXMLStreamWriter implements XMLStreamWriter {

	private XMLStreamWriter delegate;
	
	public DelegatingXMLStreamWriter(XMLStreamWriter writer){
		this.delegate = writer;
	}
	
	
	@Override
	public void close() throws XMLStreamException {
		delegate.close();
	}

	@Override
	public void flush() throws XMLStreamException {
		delegate.flush();		
	}

	@Override
	public String getPrefix(String arg0) throws XMLStreamException {
		return delegate.getPrefix(arg0);
	}

	@Override
	public Object getProperty(String arg0) throws IllegalArgumentException {
		return delegate.getProperty(arg0);
	}

	@Override
	public void setDefaultNamespace(String arg0) throws XMLStreamException {
		delegate.setDefaultNamespace(arg0);
	}

	@Override
	public void setNamespaceContext(NamespaceContext arg0) throws XMLStreamException {
		delegate.setNamespaceContext(arg0);
	}

	@Override
	public void setPrefix(String arg0, String arg1) throws XMLStreamException {
		delegate.setPrefix(arg0, arg1);
	}

	@Override
	public void writeAttribute(String arg0, String arg1) throws XMLStreamException {
		delegate.writeAttribute(arg0, arg1);
	}

	@Override
	public void writeAttribute(String arg0, String arg1, String arg2) throws XMLStreamException {
		delegate.writeAttribute(arg0, arg1, arg2);
	}

	@Override
	public void writeAttribute(String arg0, String arg1, String arg2, String arg3) throws XMLStreamException {
		delegate.writeAttribute(arg0, arg1, arg2, arg3);
	}

	@Override
	public void writeCData(String arg0) throws XMLStreamException {
		delegate.writeCData(arg0);
	}

	@Override
	public void writeCharacters(String arg0) throws XMLStreamException {
		delegate.writeCharacters(arg0);
	}

	@Override
	public void writeCharacters(char[] arg0, int arg1, int arg2) throws XMLStreamException {
		delegate.writeCharacters(arg0, arg1, arg2);
	}

	@Override
	public void writeComment(String arg0) throws XMLStreamException {
		delegate.writeComment(arg0);
	}

	@Override
	public void writeDTD(String arg0) throws XMLStreamException {
		delegate.writeDTD(arg0);		
	}

	@Override
	public void writeDefaultNamespace(String arg0) throws XMLStreamException {
		delegate.writeDefaultNamespace(arg0);
	}

	@Override
	public void writeEmptyElement(String arg0) throws XMLStreamException {
		delegate.writeEmptyElement(arg0);
	}

	@Override
	public void writeEmptyElement(String arg0, String arg1) throws XMLStreamException {
		delegate.writeEmptyElement(arg0, arg1);
	}

	@Override
	public void writeEmptyElement(String arg0, String arg1, String arg2) throws XMLStreamException {
		delegate.writeEmptyElement(arg0, arg1, arg2);
	}

	@Override
	public void writeEndDocument() throws XMLStreamException {
		delegate.writeEndDocument();
	}

	@Override
	public void writeEndElement() throws XMLStreamException {
		delegate.writeEndElement();
	}

	@Override
	public void writeEntityRef(String arg0) throws XMLStreamException {
		delegate.writeEntityRef(arg0);		
	}

	@Override
	public void writeNamespace(String arg0, String arg1) throws XMLStreamException {
		delegate.writeNamespace(arg0, arg1);		
	}

	@Override
	public void writeProcessingInstruction(String arg0) throws XMLStreamException {
		delegate.writeProcessingInstruction(arg0);		
	}

	@Override
	public void writeProcessingInstruction(String arg0, String arg1) throws XMLStreamException {
		delegate.writeProcessingInstruction(arg0, arg1);		
	}

	@Override
	public void writeStartDocument() throws XMLStreamException {
		delegate.writeStartDocument();
	}

	@Override
	public void writeStartDocument(String arg0) throws XMLStreamException {
		delegate.writeStartDocument(arg0);
	}

	@Override
	public void writeStartDocument(String arg0, String arg1) throws XMLStreamException {
		delegate.writeStartDocument(arg0, arg1);		
	}

	@Override
	public void writeStartElement(String arg0) throws XMLStreamException {
		delegate.writeStartElement(arg0);		
	}

	@Override
	public void writeStartElement(String arg0, String arg1) throws XMLStreamException {
		delegate.writeStartElement(arg0, arg1);		
	}

	@Override
	public void writeStartElement(String arg0, String arg1, String arg2) throws XMLStreamException {
		delegate.writeStartElement(arg0, arg1, arg2);
	}

	@Override
	public NamespaceContext getNamespaceContext() {
		return delegate.getNamespaceContext();
	}
	
	
}
