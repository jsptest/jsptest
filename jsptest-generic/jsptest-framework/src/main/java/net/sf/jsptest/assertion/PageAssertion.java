package net.sf.jsptest.assertion;

import junit.framework.Assert;
import org.jaxen.JaxenException;
import org.jaxen.dom.DOMXPath;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Provides assertion methods related to an HTML page.
 * 
 * @author Lasse Koskela
 */
public class PageAssertion extends DOMAssertion {

    private Element headContext;

    /**
     * @param content
     *            The DOM tree to interpret as an HTML page.
     */
    public PageAssertion(Document content) {
        try {
            context = (Element) new DOMXPath("/HTML/BODY").selectSingleNode(content);
            headContext = (Element) new DOMXPath("/HTML/HEAD").selectSingleNode(content);
        } catch (JaxenException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Assert that the page should have the specified title.
     * 
     * @param expectedTitle
     *            The expected title.
     */
    public void shouldHaveTitle(String expectedTitle) {
        try {
            String title = new DOMXPath("TITLE/text()").stringValueOf(headContext);
            Assert.assertEquals(expectedTitle, title);
        } catch (JaxenException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns a handle for making assertions related to link elements.
     */
    public LinkAssertion shouldHaveLink() {
        return new LinkAssertion(context);
    }
}
