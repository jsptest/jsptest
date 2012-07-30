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

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author Lasse Koskela
 */
public class CustomTag extends TagSupport {

    protected String timezone;

    public void setTz(String timezone) {
        this.timezone = timezone;
    }

    private HttpServletRequest getRequest() {
        return ((HttpServletRequest) pageContext.getRequest());
    }

    public int doStartTag() throws JspException {
        return TagSupport.EVAL_BODY_INCLUDE;
    }

    public int doAfterBody() throws JspException {
        try {
            JspWriter out = pageContext.getOut();
            out.println("<pre>");
            out.println("This content is coming from CustomTag for timezone " + timezone);
            printRequestMethod(out);
            printRequestAttributes(out);
            printSessionAttributes(out);
            out.println("</pre>");
        } catch (Exception e) {
            e.printStackTrace(System.err);
            throw new JspException(e);
        }
        return TagSupport.EVAL_PAGE;
    }

    private void printRequestMethod(JspWriter out) throws IOException {
        out.println("HTTP request method: " + getRequest().getMethod());
    }

    private void printRequestAttributes(JspWriter out) throws IOException {
        Enumeration attrNames = getRequest().getAttributeNames();
        while (attrNames.hasMoreElements()) {
            String name = (String) attrNames.nextElement();
            String value = String.valueOf(getRequest().getAttribute(name));
            out.println("HTTP request attribute: " + name + "=" + value);
        }
    }

    private void printSessionAttributes(JspWriter out) throws IOException {
        HttpSession session = getRequest().getSession(true);
        Enumeration attrNames = session.getAttributeNames();
        while (attrNames.hasMoreElements()) {
            String name = (String) attrNames.nextElement();
            String value = String.valueOf(session.getAttribute(name));
            out.println("HTTP session attribute: " + name + "=" + value);
        }
    }

    public int doEndTag() throws JspException {
        return TagSupport.EVAL_PAGE;
    }
}
