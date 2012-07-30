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
import net.sf.jsptest.utils.XML;
import org.w3c.dom.Element;

/**
 * @author Lasse Koskela
 */
public class TestHtmlTestCaseElementAssertions extends AbstractHtmlTestCaseTestCase {

    protected void appendOutput(StringBuffer h) {
        h.append("<html><body>");
        h.append("  <p>this is p1</p>");
        h.append("  <div id='div1'>");
        h.append("    <p class='p2'>this is p2</p>");
        h.append("  </div>");
        h.append("  <div id='div2'>");
        h.append("    <p id='p3'>this is p3</p>");
        h.append("  </div>");
        h.append("</body></html>");
    }

    public void testPageShouldContainElement() throws Exception {
        testcase.page().shouldContainElement("//P[@CLASS='p2']");
        try {
            testcase.page().shouldContainElement("//p[@class='no_such_element']");
            throw new RuntimeException("Expected assertion to fail but it didn't.");
        } catch (AssertionFailedError expected) {
        }
    }

    public void testMissingElementGivesAProperFailureMessage() throws Exception {
        String xpath = "//P[@CLASS='no_such_element']";
        try {
            testcase.element(xpath);
            throw new RuntimeException("Expected assertion to fail but it didn't.");
        } catch (AssertionFailedError expected) {
            assertEquals("No element found with XPath expression " + xpath, expected.getMessage());
        }
    }

    public void testAssertingAgainstNonElementXPathFails() throws Exception {
        String xpath = "//P[@CLASS='p1']/text()";
        try {
            testcase.element(xpath);
            throw new RuntimeException("Expected assertion to fail but it didn't.");
        } catch (AssertionFailedError expected) {
        }
    }

    public void testElementShouldContain() throws Exception {
        testcase.element("//P[@CLASS='p2']").shouldContain("this is p2");
        try {
            testcase.element("//P[@CLASS='p1']").shouldContain("it doesn't contain this");
            throw new RuntimeException("Expected assertion to fail but it didn't.");
        } catch (AssertionFailedError expected) {
        }
    }

    public void testElementShouldNotContain() throws Exception {
        testcase.element("//P[@CLASS='p2']").shouldNotContain("bad things");
        try {
            testcase.element("//P[@CLASS='p2']").shouldNotContain("p2");
            throw new RuntimeException("Expected assertion to fail but it didn't.");
        } catch (AssertionFailedError expected) {
        }
    }

    public void testHasAccessToUnderlyingDomElement() throws Exception {
        Element element = testcase.element("//P[@CLASS='p2']").getElement();
        assertNotNull(element);
        assertEquals("this is p2", XML.textContentOf(element));
    }
}
