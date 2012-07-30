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
public class TestHtmlTestCase extends AbstractFakeJspCompilerTestCase {

    private HtmlTestCase testcase;

    protected void appendOutput(StringBuffer h) {
        h.append("<html><body>");
        h.append("before form");
        h.append("<!-- this is inside a comment -->");
        h.append("<form name='form_name' action='act.do'>");
        h.append("  inside form");
        h.append("  <input type='text' name='field' value='hello'>");
        h.append("</form>");
        h.append("after form");
        h.append("</body></html>");
    }

    protected void setUp() throws Exception {
        super.setUp();
        testcase = new HtmlTestCase() {
        };
        testcase.get("/index.jsp");
    }

    public void testPageAssertions() throws Exception {
        testcase.page().shouldContain("before form");
        testcase.page().shouldContain("inside form");
        testcase.page().shouldContain("after form");
        try {
            testcase.page().shouldContain("no such text on the page");
            throw new RuntimeException("Test should've failed");
        } catch (AssertionFailedError expected) {
        }
    }

    public void testPageDoesNotContainTextInsideComments() throws Exception {
        try {
            testcase.page().shouldContain("inside a comment");
            throw new RuntimeException("Test should've failed");
        } catch (AssertionFailedError expected) {
        }
    }

    public void testUnnamedFormShouldHaveField() throws Exception {
        testcase.form().shouldHaveField("field");
        try {
            testcase.form().shouldHaveField("nosuchfield");
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
