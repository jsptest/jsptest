/*
 * Copyright 2007 Lasse Koskela.
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

package net.sf.jsptest.acceptance.html;

import junit.framework.AssertionFailedError;
import net.sf.jsptest.HtmlTestCase;

/**
 * @author Lasse Koskela
 */
public class FormAssertionTest extends HtmlTestCase {

    protected String getWebRoot() {
        return "src/test/resources/websrc";
    }

    public void testFormFieldPresent() throws Exception {
        get("/html/forms/simpleform.jsp");
        form().shouldHaveField("text_field_1");
        form().shouldHaveField("text_field_2");
        try {
            form().shouldHaveField("text_field_3");
            throw new RuntimeException("There is no such field!");
        } catch (AssertionFailedError expected) {
        }
    }

    public void testAssertingAgainstFormFieldValue() throws Exception {
        setRequestAttribute("j_username", "bob");
        get("/html/forms/simpleform.jsp");
        form("form_name").field("text_field_1")
                .shouldHaveValue("one");
        form("form_name").field("text_field_2")
                .shouldHaveValue("two");
        try {
            form("form_name").field("text_field_1").shouldHaveValue(
                    "three");
            throw new RuntimeException(
                    "The field value shouldn't match!");
        } catch (AssertionFailedError expected) {
        }
    }

    public void testFormHasNamedSubmitButton() throws Exception {
        get("/html/forms/simpleform.jsp");
        form("form_name").shouldHaveSubmitButton("GO");
        try {
            form("form_name").shouldHaveSubmitButton("NOSUCHBUTTON");
            throw new RuntimeException("There is no such button!");
        } catch (AssertionFailedError expected) {
        }
    }

    public void testFormHasUnnamedSubmitButton() throws Exception {
        get("/html/forms/simpleform.jsp");
        form("form_name").shouldHaveSubmitButton();
        try {
            form("form_without_buttons").shouldHaveSubmitButton();
            throw new RuntimeException("There is no submit buttons!");
        } catch (AssertionFailedError expected) {
        }
    }
}
