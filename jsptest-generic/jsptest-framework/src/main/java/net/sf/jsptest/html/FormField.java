package net.sf.jsptest.html;

import org.w3c.dom.Element;

/**
 * Representation of a form input field.
 * 
 * @author Lasse Koskela
 */
public class FormField {

    private Element element;

    /**
     * Build an input field from the given HTML element.
     * 
     * @param element
     *            The HTML input element.
     */
    public FormField(Element element) {
        this.element = element;
    }

    /**
     * Returns the name of the field.
     */
    public String getName() {
        return element.getAttribute("NAME");
    }

    /**
     * Returns the value of the field.
     */
    public String getValue() {
        return element.getAttribute("VALUE");
    }
}
