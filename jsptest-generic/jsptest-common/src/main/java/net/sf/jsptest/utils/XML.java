/*
 * Copyright 2008 Lasse Koskela.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.jsptest.utils;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The <tt>XML</tt> class provides utility methods for handling XML related tasks.
 * 
 * @author Lasse Koskela
 */
public class XML {

    private static final String APACHE_INDENTATION = "{http://xml.apache.org/xslt}indent-amount";

    public static String toString(Node xml) {
        try {
            TransformerFactory f = TransformerFactory.newInstance();
            Transformer t = f.newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.setOutputProperty(APACHE_INDENTATION, "2");
            OutputStream out = new ByteArrayOutputStream();
            t.transform(new DOMSource(xml), new StreamResult(out));
            return out.toString();
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    public static String textContentOf(Element element) {
        StringBuffer textContent = new StringBuffer(100);
        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            appendTextContentOf(children.item(i), textContent);
        }
        return textContent.toString();
    }

    private static void appendTextContentOf(Node from, StringBuffer to) {
        if (from.getNodeType() == Node.ELEMENT_NODE) {
            to.append(textContentOf((Element) from));
        } else if (from.getNodeType() != Node.COMMENT_NODE) {
            to.append(from.getNodeValue().trim());
        }
    }
}
