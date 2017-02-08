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