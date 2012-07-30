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

package net.sf.jsptest.acceptance.jsp;

import net.sf.jsptest.JspTestCase;

/**
 * Unit testing JavaServer Pages has never been this easy!
 */
public class RequestAndSessionAttributeTest extends JspTestCase {

    protected String getWebRoot() {
        return "src/test/resources/websrc";
    }

    public void testRequestAttributes() throws Exception {
        setRequestAttribute("Rn1", "Rv1");
        setRequestAttribute("Rn2", "Rv2");
        get("/echo_attributes.jsp");
        output().shouldContain("request attribute: 'Rn1'='Rv1'");
        output().shouldContain("request attribute: 'Rn2'='Rv2'");
    }

    public void testSessionAttributes() throws Exception {
        setSessionAttribute("Sn1", "Sv1");
        setSessionAttribute("Sn2", "Sv2");
        get("/echo_attributes.jsp");
        output().shouldContain("session attribute: 'Sn1'='Sv1'");
        output().shouldContain("session attribute: 'Sn2'='Sv2'");
    }

    public void testRequestAndSessionAttributesCanBeObjects()
            throws Exception {
        setRequestAttribute("request", (Object) "1");
        setSessionAttribute("session", (Object) new Integer(2));
        get("/echo_attributes.jsp");
        output().shouldContain("request attribute: 'request'='1'");
        output().shouldContain("session attribute: 'session'='2'");
    }

    public void testSessionAndRequestAttributesDontOverrideEachOther()
            throws Exception {
        setRequestAttribute("name", "request");
        setSessionAttribute("name", "session");
        get("/echo_attributes.jsp");
        output().shouldContain("request attribute: 'name'='request'");
        output().shouldContain("session attribute: 'name'='session'");
    }
}
