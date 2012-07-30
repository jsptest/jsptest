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
package net.sf.jsptest;

import junit.framework.AssertionFailedError;

/**
 * @author Lasse Koskela
 */
public class TestHtmlTestCaseFormAssertions extends AbstractHtmlTestCaseTestCase {

    protected void appendOutput(StringBuffer h) {
        h.append("<html><body>");
        h.append("<form name='form_name' action='act.do'>");
        h.append("  <input type='text' name='field' value='hello'>");
        h.append("  <input type='submit' name='submit_name' value='submit_value'>");
        h.append("</form>");
        h.append("</body></html>");
    }

    public void testUnnamedFormShouldHaveField() throws Exception {
        testcase.form().shouldHaveField("field");
        try {
            testcase.form().shouldHaveField("nosuchfield");
            throw new RuntimeException("Test should've failed");
        } catch (AssertionFailedError expected) {
        }
    }

    public void testUnnamedFormShouldHaveButton() throws Exception {
        testcase.form().shouldHaveSubmitButton();
        testcase.form().shouldHaveSubmitButton("submit_value");
        testcase.form().shouldHaveSubmitButton("submit_name");
        try {
            testcase.form().shouldHaveSubmitButton("nosuchbutton");
            throw new RuntimeException("Test should've failed");
        } catch (AssertionFailedError expected) {
        }
    }

    public void testNamedFormShouldHaveField() throws Exception {
        testcase.form("form_name").shouldHaveField("field");
        try {
            testcase.form("form_name").shouldHaveField("nosuchfield");
            throw new RuntimeException("Test should've failed");
        } catch (AssertionFailedError expected) {
        }
        try {
            testcase.form("no_such_form");
            throw new RuntimeException("Test should've failed");
        } catch (AssertionFailedError expected) {
        }
    }
}
