package net.sf.jsptest.utils;

import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import junit.framework.TestCase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

/**
 * @author Lasse Koskela
 */
public class XMLTest extends TestCase {

    public void testCollectingTextContentForEmptyElement() throws Exception {
        Element element = parse("<root></root>");
        assertEquals("", XML.textContentOf(element));
    }

    public void testCollectingTextContentForNonEmptyElement() throws Exception {
        Element element = parse("<parent>text</parent>");
        assertEquals("text", XML.textContentOf(element));
    }

    public void testCollectingTextContentElementWithChildren() throws Exception {
        Element element = parse("<parent>parent<child>child</child></parent>");
        assertEquals("parentchild", XML.textContentOf(element));
    }

    public void testWhitespaceBetweenTextNodesIsRemoved() throws Exception {
        Element element = parse("<parent>\n  parent\n  <child>\n    child\n  </child>\n</parent>");
        assertEquals("parentchild", XML.textContentOf(element));
    }

    public void testCommentsAreIgnoredWhenCollectingTextContent() throws Exception {
        Element element = parse("<p>before comment<!-- ignore -->After comment</p>");
        assertEquals("before commentAfter comment", XML.textContentOf(element));
    }

    private Element parse(String xmlSnippet) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder parser = dbf.newDocumentBuilder();
        String xml = "<?xml version='1.0'?>\n" + xmlSnippet;
        Document doc = parser.parse(new InputSource(new StringReader(xml)));
        return doc.getDocumentElement();
    }
}
