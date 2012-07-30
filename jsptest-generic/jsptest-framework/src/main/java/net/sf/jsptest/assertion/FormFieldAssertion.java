package net.sf.jsptest.assertion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import junit.framework.Assert;
import net.sf.jsptest.html.Form;
import net.sf.jsptest.html.FormField;

/**
 * Provides form field-oriented assertion methods.
 * 
 * @author Lasse Koskela
 */
public class FormFieldAssertion {

    private final List fields;
    private final String fieldName;

    /**
     * @param forms
     *            The list of forms that should be considered the context for the subsequent
     *            assertion methods.
     * @param fieldName
     *            The name of the form field that should be considered the context for the
     *            subsequent assertion methods.
     */
    public FormFieldAssertion(List forms, String fieldName) {
        this.fieldName = fieldName;
        this.fields = new ArrayList();
        for (Iterator i = forms.iterator(); i.hasNext();) {
            Form form = (Form) i.next();
            if (form.hasInputField(fieldName)) {
                fields.add(form.getInputField(fieldName));
            }
        }
    }

    /**
     * Assert that the selected form field has the given value.
     * 
     * @param expectedValue
     *            The expected value.
     */
    public void shouldHaveValue(String expectedValue) {
        List actuals = new ArrayList();
        for (Iterator i = fields.iterator(); i.hasNext();) {
            FormField form = (FormField) i.next();
            if (expectedValue.equals(form.getValue())) {
                return;
            } else {
                actuals.add(form.getValue());
            }
        }
        Assert.fail("Field '" + fieldName + "' does not have value '" + expectedValue + "': "
                + actuals);
    }
}
