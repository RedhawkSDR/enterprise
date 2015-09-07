package redhawk.driver.xml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.util.EventReaderDelegate;

/**
 * Filter adding default namespace declaration to root element.
 */
public class NamespaceAddingEventReader extends EventReaderDelegate {
    private final XMLEventFactory factory = XMLEventFactory.newInstance();
    private final String namespaceURI;

    private int startElementCount = 0;

    public NamespaceAddingEventReader(XMLEventReader reader, String namespaceURI) {
        super(reader);
        this.namespaceURI = namespaceURI;
    }

    /**
     * Duplicate event with additional namespace declaration.
     * @param startElement
     * @return event with namespace
     */
    private StartElement withNamespace(StartElement startElement) {
        List<Object> namespaces = new ArrayList<Object>();
        namespaces.add(factory.createNamespace(namespaceURI));
        Iterator<?> originalNamespaces = startElement.getNamespaces();
        while (originalNamespaces.hasNext()) {
            namespaces.add(originalNamespaces.next());
        }
        
        return factory.createStartElement(
                new QName(namespaceURI, startElement.getName().getLocalPart()),
                startElement.getAttributes(),
                namespaces.iterator());
    }

    @Override
    public XMLEvent nextEvent() throws XMLStreamException {
        XMLEvent event = super.nextEvent();
        if (event.isStartElement()) {
            return withNamespace(event.asStartElement());
        }
        return event;
    }

    @Override
    public XMLEvent peek() throws XMLStreamException {
        XMLEvent event = super.peek();
        if (startElementCount == 0 && event.isStartElement()) {
            return withNamespace(event.asStartElement());
        } else {
            return event;
        }
    }
}