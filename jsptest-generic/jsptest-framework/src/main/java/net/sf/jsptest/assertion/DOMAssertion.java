package net.sf.jsptest.assertion;

import java.util.List;
import junit.framework.Assert;
import net.sf.jsptest.utils.XML;
import org.jaxen.JaxenException;
import org.jaxen.dom.DOMXPath;
import org.w3c.dom.Element;

/**
 * Base class providing assertion methods related to the HTML DOM tree.
 * 
 * @author Lasse Koskela
 */
public abstract class DOMAssertion extends AbstractAssertion {

    protected Element context;

    /**
     * Returns the <tt>Element</tt> as the context of assertions.
     */
    public Element getElement() {
        return context;
    }

    /**
     * Assert that the selected DOM element contains the given text.
     * 
     * @param text
     *            The (partial) content that should be found.
     */
    public void shouldContain(String text) {
        assertContains(XML.textContentOf(context), text);
    }

    /**
     * Assert that the selected DOM element does not contain the given text.
     * 
     * @param text
     *            The (partial) content that should not be found.
     */
    public void shouldNotContain(String text) {
        assertDoesNotContain(XML.textContentOf(context), text);
    }

    /**
     * Assert that the selected DOM element contains the given element.
     * 
     * @param xpathExpression
     *            An XPath expression describing the expected child element.
     */
    public void shouldContainElement(String xpathExpression) {
        shouldContainElement("No matching nodes found for XPath: " + xpathExpression + " from\n"
                + getContextAsString(), xpathExpression);
    }

    /**
     * Assert that the selected DOM element contains the given element.
     * 
     * @param message
     *            The optional failure message.
     * @param xpathExpression
     *            An XPath expression describing the expected child element.
     */
    public void shouldContainElement(String message, String xpathExpression) {
        try {
            DOMXPath xpath = new DOMXPath(xpathExpression);
            List matchingNodes = xpath.selectNodes(context);
            Assert.assertFalse(message, matchingNodes.isEmpty());
        } catch (JaxenException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Renders the content of the selected element as a <tt>String</tt>.
     */
    protected String getContextAsString() {
        return XML.toString(context);
    }
}
