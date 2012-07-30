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
public class PageAssertionTest extends HtmlTestCase {

    protected String getWebRoot() {
        return "src/test/resources/websrc";
    }

    public void testPageShouldContain() throws Exception {
        get("/html/simple.jsp");
        page().shouldContain("Hello from Jasper");
        try {
            page().shouldContain("no such content");
            throw new RuntimeException();
        } catch (AssertionFailedError expected) {
        }
    }
}
