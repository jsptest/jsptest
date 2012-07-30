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

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import net.sf.jsptest.assertion.OutputAssertion;
import net.sf.jsptest.compiler.api.Jsp;
import net.sf.jsptest.compiler.api.JspCompiler;
import net.sf.jsptest.compiler.api.JspCompilerFactory;
import net.sf.jsptest.compiler.api.JspExecution;
import org.apache.log4j.Logger;

/**
 * An abstract base class to be extended by the user. The <tt>JspTestCase</tt> class provides a
 * facility for rendering a JSP and a set of assertion methods for verifying that the JSP under test
 * renders the expected kind of output.
 * 
 * @author Lasse Koskela
 * @author Meinert Schwartau (scwar32)
 * @author Ronaldo Webb
 */
public abstract class JspTestCase extends TestCase {

    private Logger log;
    private Map requestParameters;
    private Map requestAttributes;
    private Map sessionAttributes;
    private Map substituteTaglibs;
    private JspExecution execution;

    public JspTestCase() {
        log = Logger.getLogger(getClass());
    }

    /**
     * The standard JUnit <tt>setUp()</tt> method. <b>Remember to invoke <tt>super.setUp()</tt>
     * if you override this!</b>
     */
    protected void setUp() throws Exception {
        requestParameters = new HashMap();
        requestAttributes = new HashMap();
        sessionAttributes = new HashMap();
        substituteTaglibs = new HashMap();
    }

    /**
     * Override this method to tell the JSP compiler where the "web" files are located. Defaults to
     * the current working directory.
     */
    protected String getWebRoot() {
        return ".";
    }

    /**
     * Sets a session attribute for the current session.
     * 
     * @param attribute
     *            Name of the attribute.
     * @param value
     *            Value for the attribute.
     */
    protected void setSessionAttribute(String attribute, Object value) {
        sessionAttributes.put(attribute, value);
    }

    /**
     * Sets a request attribute for the next request.
     * 
     * @param attribute
     *            Name of the attribute.
     * @param value
     *            Value for the attribute.
     */
    protected void setRequestAttribute(String attribute, Object value) {
        requestAttributes.put(attribute, value);
    }
    
    /**
     * Sets a single value for a request parameter for the next request.
     * 
     * @param attribute
     *            Name of the attribute.
     * @param value
     *            Value for the attribute.
     */
    protected void setRequestParameter(String parameter, String value) {
        setRequestParameter(parameter, new String[] {value});
    }

    /**
     * Sets multiple values for a request parameter for the next request.
     * 
     * @param attribute
     *            Name of the attribute.
     * @param values
     *            Values for the attribute.
     */
    protected void setRequestParameter(String parameter, String[] values) {
        requestParameters.put(parameter, values);
    }

    /**
     * Simulate a HTTP GET request to the specified JSP file.
     * 
     * @param path
     *            The JSP file to render. The path should start with a "/" and is interpreted to be
     *            relative to the web root specified by <tt>getWebRoot</tt>.
     */
    protected void get(String path) throws Exception {
        request(path, "GET");
    }

    /**
     * Simulate a HTTP POST request to the specified JSP file.
     * 
     * @param path
     *            The JSP file to render. The path should start with a "/" and is interpreted to be
     *            relative to the web root specified by <tt>getWebRoot</tt>.
     */
    protected void post(String path) throws Exception {
        request(path, "POST");
    }

    /**
     * Simulate an HTTP request to a JSP.
     * 
     * @param path
     *            The path to the JSP to execute.
     * @param httpMethod
     *            "GET" or "POST".
     */
    protected void request(String path, String httpMethod) throws Exception {
        validatePath(path);
        JspCompiler compiler = JspCompilerFactory.newInstance();
        log.debug("Using compiler " + compiler.getClass().getName() + " and webroot "
                + new File(getWebRoot()).getAbsolutePath());
        compiler.setWebRoot(getWebRoot());
        compiler.setOutputDirectory(getOutputDirectory());
        Jsp jsp = compiler.compile(path, substituteTaglibs);
        log.debug("Simulating a request to " + path);
        execution = jsp.request(httpMethod, requestAttributes, sessionAttributes, requestParameters);
    }

    private void validatePath(String path) {
        if (!path.startsWith("/")) {
            throw new IllegalArgumentException("The JSP path must start with a \"/\"");
        }
    }

    private String getOutputDirectory() {
        return "target/jsptest";
    }

    /**
     * Returns the rendered output.
     */
    protected String getRenderedResponse() {
        return execution.getRenderedResponse();
    }

    /**
     * Returns a handle for making assertions about the rendered content.
     */
    public OutputAssertion output() {
        return new OutputAssertion(getRenderedResponse());
    }

    /**
     * Invoke this method to substitute the specified taglib with the given implementation.
     * 
     * @param name
     *            The name of the taglib to replace.
     * @param newImplementation
     *            The new (substitute) implementation to use.
     */
    protected void substituteTaglib(String name, Class newImplementation) {
        substituteTaglibs.put(new TagKey(name), newImplementation);
    }

    /**
     * Invoke this method to substitute the specified taglib with the given implementation.
     * 
     * @param name
     *            The name of the taglib to replace.
     * @param newImplementation
     *            The new (substitute) implementation to use.
     */
    protected void substituteTag(String prefix, String name, Class newImplementation) {
        substituteTaglibs.put(new TagKey(prefix, name), newImplementation);
    }
}
