package net.sf.jsptest.html;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Representation of an HTML form.
 * 
 * @author Lasse Koskela
 */
public class Form {

    private Element element;
    private NodeList fields;

    /**
     * Build a <tt>Form</tt> from an HTML element.
     * 
     * @param element
     *            The HTML form element to represent.
     */
    public Form(Element element) {
        this.element = element;
        fields = element.getElementsByTagName("INPUT");
    }

    /**
     * Returns the name of the HTML form.
     */
    public String getName() {
        return element.getAttribute("NAME");
    }

    /**
     * Indicates whether the form has an input field by the given name.
     * 
     * @param name
     *            The name of the input field.
     */
    public boolean hasInputField(String name) {
        if (getInputField(name) != null) {
            return true;
        }
        return false;
    }

    /**
     * Returns the specified input field or <tt>null</tt> if no such field exists on the form.
     * 
     * @param name
     *            The name of the input field.
     */
    public FormField getInputField(String name) {
        for (int i = 0; i < fields.getLength(); i++) {
            FormField field = new FormField((Element) fields.item(i));
            if (name.equals(field.getName())) {
                return field;
            }
        }
        return null;
    }

    /**
     * Indicates whether the form has a submit button by the given name.
     * 
     * @param name
     *            The name of the submit button.
     */
    public boolean hasSubmitButton(String name) {
        NodeList elems = element.getElementsByTagName("INPUT");
        for (int i = 0; i < elems.getLength(); i++) {
            Element element = (Element) elems.item(i);
            if ("SUBMIT".equalsIgnoreCase(element.getAttribute("TYPE"))) {
                if (name.equals(element.getAttribute("VALUE"))) {
                    return true;
                }
                if (name.equals(element.getAttribute("NAME"))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Indicates whether the form has a submit button.
     */
    public boolean hasSubmitButton() {
        NodeList elems = element.getElementsByTagName("INPUT");
        for (int i = 0; i < elems.getLength(); i++) {
            Element element = (Element) elems.item(i);
            if ("SUBMIT".equalsIgnoreCase(element.getAttribute("TYPE"))) {
                return true;
            }
        }
        return false;
    }
}
