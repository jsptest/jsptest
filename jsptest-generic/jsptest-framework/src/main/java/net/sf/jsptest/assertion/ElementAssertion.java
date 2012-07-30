package net.sf.jsptest.assertion;

import junit.framework.Assert;
import org.jaxen.JaxenException;
import org.jaxen.dom.DOMXPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Collection of HTML-related assertion methods specifically applicable to "element" nodes in a DOM
 * tree.
 * 
 * @author Lasse Koskela
 */
public class ElementAssertion extends DOMAssertion {

    /**
     * @param content
     *            The <tt>Document</tt> serving as the context for the assertion.
     * @param xpathExpression
     *            The XPath expression that identifies the element, which the subsequent assertion
     *            methods should be applied to.
     */
    public ElementAssertion(Document content, String xpathExpression) {
        this.context = content.getDocumentElement();
        try {
            DOMXPath xpath = new DOMXPath(xpathExpression);
            Object matchingNode = xpath.selectSingleNode(context);
            if (matchingNode == null) {
                Assert.fail("No element found with XPath expression " + xpathExpression);
            } else if (Element.class.isAssignableFrom(matchingNode.getClass())) {
                context = (Element) matchingNode;
            }
        } catch (JaxenException e) {
            throw new RuntimeException(e);
        }
    }
}
