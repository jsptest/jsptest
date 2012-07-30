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
package net.sf.jsptest.compiler.jsp20.mock;

import java.util.Enumeration;
import java.util.Vector;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 * @author Lasse Koskela
 */
public class MockServletConfig implements ServletConfig {

    private ServletContext servletContext;

    public MockServletConfig(ServletContext context) {
        this.servletContext = context;
    }

    public String getServletName() {
        return "MyServlet";
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public String getInitParameter(String name) {
        return null;
    }

    public Enumeration getInitParameterNames() {
        return new Vector().elements();
    }
}
