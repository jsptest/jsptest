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

import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.jsp.JspEngineInfo;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.PageContext;

/**
 * @author Lasse Koskela
 */
public class MockJspFactory extends JspFactory {

    private PageContext pageContext;

    public MockJspFactory(PageContext pageContext) {
        this.pageContext = pageContext;
    }

    public PageContext getPageContext(Servlet servlet, ServletRequest request,
            ServletResponse response, String errorPageURL, boolean needsSession, int buffer,
            boolean autoflush) {
        return pageContext;
    }

    public void releasePageContext(PageContext pageContext) {
    }

    public JspEngineInfo getEngineInfo() {
        return new JspEngineInfo() {

            public String getSpecificationVersion() {
                return "2.0";
            }
        };
    }
}
