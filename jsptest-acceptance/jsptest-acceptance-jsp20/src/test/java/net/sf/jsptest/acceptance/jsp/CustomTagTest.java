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
 * @author Lasse Koskela
 */
public class CustomTagTest extends JspTestCase {

    protected String getWebRoot() {
        return "src/test/resources/websrc";
    }

    public void testRenderingJspUsingCustomTaglibs() throws Exception {
        get("/taglibs/custom-taglib.jsp");
        output().shouldContain("This content is coming from CustomTag");
    }

    public void testTaglibAttributes() throws Exception {
        get("/taglibs/custom-taglib.jsp");
        output().shouldContain("timezone GMT");
        output().shouldContain("timezone EST");
    }

    public void testRequestMethods() throws Exception {
        // GET
        get("/taglibs/custom-taglib.jsp");
        output().shouldContain("HTTP request method: GET");
        // POST
        post("/taglibs/custom-taglib.jsp");
        output().shouldContain("HTTP request method: POST");
    }

    public void testRequestAndSessionAttributes() throws Exception {
        // GET
        setRequestAttribute("Rn1", "Rv1");
        setRequestAttribute("Rn2", "Rv2");
        setSessionAttribute("Sn1", "Sv1");
        setSessionAttribute("Sn2", "Sv2");
        get("/taglibs/custom-taglib.jsp");
        output().shouldContain("request attribute: Rn1=Rv1");
        output().shouldContain("request attribute: Rn2=Rv2");
        output().shouldContain("session attribute: Sn1=Sv1");
        output().shouldContain("session attribute: Sn2=Sv2");
        // POST
        setRequestAttribute("Rn1", "Rv1");
        setRequestAttribute("Rn2", "Rv2");
        setSessionAttribute("Sn1", "Sv1");
        setSessionAttribute("Sn2", "Sv2");
        post("/taglibs/custom-taglib.jsp");
        output().shouldContain("request attribute: Rn1=Rv1");
        output().shouldContain("request attribute: Rn2=Rv2");
        output().shouldContain("session attribute: Sn1=Sv1");
        output().shouldContain("session attribute: Sn2=Sv2");
    }
}
