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

import net.sf.jsptest.assertion.ExpectedAssertionFailure;

/**
 * @author Lasse Koskela
 */
public class TestHtmlTestCasePageAssertions extends AbstractHtmlTestCaseTestCase {

    protected void appendOutput(StringBuffer h) {
        h.append("<html><head>");
        h.append("<title>PageTitle</title>");
        h.append("</head><body>");
        h.append("before div");
        h.append("<!-- this is inside a comment -->");
        h.append("<div>");
        h.append("  inside div");
        h.append("</div>");
        h.append("after div");
        h.append("</body></html>");
    }

    public void testPageShouldHaveTitle() throws Exception {
        testcase.page().shouldHaveTitle("PageTitle");
        new ExpectedAssertionFailure(testcase) {

            public void run() {
                page().shouldHaveTitle("Not the same");
            }
        };
    }

    public void testPageShouldContain() throws Exception {
        testcase.page().shouldContain("before div");
        testcase.page().shouldContain("inside div");
        testcase.page().shouldContain("after div");
        new ExpectedAssertionFailure(testcase) {

            protected void run() throws Exception {
                page().shouldContain("no such text on the page");
            }
        };
    }

    public void testPageShouldNotContain() throws Exception {
        testcase.page().shouldNotContain("no such text on the page");
        new ExpectedAssertionFailure(testcase) {

            protected void run() throws Exception {
                page().shouldNotContain("before div");
            }
        };
    }

    public void testPageDoesNotContainTextInsideComments() throws Exception {
        testcase.page().shouldNotContain("inside a comment");
        new ExpectedAssertionFailure(testcase) {

            protected void run() throws Exception {
                page().shouldContain("inside a comment");
            }
        };
    }

    public void testPageShouldNotContainItsTitle() throws Exception {
        testcase.page().shouldNotContain("PageTitle");
        new ExpectedAssertionFailure(testcase) {

            protected void run() throws Exception {
                page().shouldContain("PageTitle");
            }
        };
    }
}
