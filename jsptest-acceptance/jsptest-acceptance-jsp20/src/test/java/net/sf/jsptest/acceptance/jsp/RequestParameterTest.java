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
public class RequestParameterTest extends JspTestCase {

    protected String getWebRoot() {
        return "src/test/resources/websrc";
    }

    public void testRequestParameters() throws Exception {
        setRequestParameter("Rn1", "Rv1");
        setRequestParameter("Rn2", "Rv2");
        setRequestParameter("Rn3", new String[] {"Rv3a", "Rv3b"});
        get("/echo_parameters.jsp");
        output().shouldContain("request parameter: 'Rn1'='Rv1'");
        output().shouldContain("request parameter: 'Rn2'='Rv2'");
        output().shouldContain("request parameter: 'Rn3'='Rv3a','Rv3b'");
    }
    
    public void testRequestParametersUsingEL() throws Exception {
    	setRequestParameter("Rn0", "Rv0");
    	get("/echo_parameters_el.jsp");
        output().shouldContain("request parameter: 'Rn0'='Rv0'");
    }
}
