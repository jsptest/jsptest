package net.sf.jsptest.assertion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import junit.framework.Assert;
import net.sf.jsptest.html.Form;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Provides form-related assertion methods.
 * 
 * @author Lasse Koskela
 */
public class FormAssertion {

    private final List forms;

    /**
     * @param document
     *            The context from where to select the form as the context for subsequent assertion
     *            methods.
     * @param chooser
     *            The <tt>ElementChooser</tt> to use for selecting the form.
     */
    public FormAssertion(Document document, ElementChooser chooser) {
        this.forms = new ArrayList();
        NodeList elements = document.getElementsByTagName("FORM");
        for (int i = 0; i < elements.getLength(); i++) {
            Element element = (Element) elements.item(i);
            if (chooser.accept(element)) {
                forms.add(new Form(element));
            }
        }
        if (forms.isEmpty()) {
            Assert.fail("Form not found.");
        }
    }

    /**
     * Assert that the form has a field by the given name.
     * 
     * @param name
     *            The name of the expected form field.
     */
    public void shouldHaveField(String name) {
        for (Iterator i = forms.iterator(); i.hasNext();) {
            Form form = (Form) i.next();
            if (form.hasInputField(name)) {
                return;
            }
        }
        Assert.fail("No form field '" + name + "' on page");
    }

    /**
     * Assert that the form has a submit button by the given name or label.
     * 
     * @param nameOrLabel
     *            The name or label of the expected submit button.
     */
    public void shouldHaveSubmitButton(String nameOrLabel) {
        for (Iterator i = forms.iterator(); i.hasNext();) {
            Form form = (Form) i.next();
            if (form.hasSubmitButton(nameOrLabel)) {
                return;
            }
        }
        Assert.fail("No form submit button '" + nameOrLabel + "' on page");
    }

    /**
     * Assert that the form has a submit button.
     */
    public void shouldHaveSubmitButton() {
        for (Iterator i = forms.iterator(); i.hasNext();) {
            Form form = (Form) i.next();
            if (form.hasSubmitButton()) {
                return;
            }
        }
        Assert.fail("No form submit button on page");
    }

    /**
     * Gives access to form field-specific assertions such as:
     * <code>form("name").field("name").shouldHaveValue("foo");</code>
     * 
     * @param fieldName
     *            The name of the field.
     */
    public FormFieldAssertion field(String fieldName) {
        shouldHaveField(fieldName);
        return new FormFieldAssertion(forms, fieldName);
    }
}
