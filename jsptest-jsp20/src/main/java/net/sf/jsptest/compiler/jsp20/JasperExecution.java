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
package net.sf.jsptest.compiler.jsp20;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspFactory;
import junit.framework.TestCase;
import net.sf.jsptest.compiler.JspCompilationInfo;
import net.sf.jsptest.compiler.jsp20.mock.MockHttpServletRequest;
import net.sf.jsptest.compiler.jsp20.mock.MockHttpServletResponse;
import net.sf.jsptest.compiler.jsp20.mock.MockHttpSession;
import net.sf.jsptest.compiler.jsp20.mock.MockJspFactory;
import net.sf.jsptest.compiler.jsp20.mock.MockJspWriter;
import net.sf.jsptest.compiler.jsp20.mock.MockPageContext;
import net.sf.jsptest.compiler.jsp20.mock.MockServletConfig;
import net.sf.jsptest.compiler.jsp20.mock.MockServletContext;
import net.sf.jsptest.utils.CustomClassLoader;
import net.sf.jsptest.utils.IO;

/**
 * The <tt>JasperTestCase</tt> provides a facility for rendering JavaServer Pages outside a real
 * Servlet/JSP container.
 * <p>
 * It uses the <tt>JasperCompiler</tt> to compile JSP files and invokes the compiled Servlet class
 * using a custom <tt>ClassLoader</tt>, providing access to a <tt>WebResponse</tt> object
 * representing the rendered output by pointing HttpUnit to a temporary file on the local file
 * system.
 * 
 * @author Lasse Koskela
 * @author Meinert Schwartau (scwar32)
 */
public abstract class JasperExecution extends TestCase {

    private static Map mockTaglibs = new HashMap();
    private static JasperCompiler compiler = new JasperCompiler();
    private MockPageContext pageContext;
    private File responseFile;
    protected Map sessionAttributes;
    protected Map requestAttributes;

    public JasperExecution() {
        this.sessionAttributes = new HashMap();
        this.requestAttributes = new HashMap();
        mockTaglibs.clear();
        responseFile = null;
    }

    /**
     * Returns a handle to the rendered output written into a temporary file.
     */
    protected File getRenderedResponse() {
        return responseFile;
    }

    /**
     * Returns the root directory relative to which all request paths will be resolved. This root
     * directory is the one which contains your "WEB-INF". By default, it is the current working
     * directory. Override this method to point JspTest to your web root.
     */
    protected String getWebRoot() {
        return ".";
    }

    /**
     * Returns the directory where generated .class file(s) should be written. Unless overridden by
     * subclasses, the default is "$HOME/.jsptest/compiled" plus a unique subdirectory derived from
     * the current working directory.
     */
    protected String getClassOutputBaseDir() {
        File home = new File(System.getProperty("user.home"));
        File jspClassDir = new File(home, ".jsptest/compiled/"
                + createUniqueIdentifierForWorkspace());
        jspClassDir.mkdirs();
        return jspClassDir.getAbsolutePath();
    }

    private String createUniqueIdentifierForWorkspace() {
        return "workspace-" + new File(".").getAbsolutePath().hashCode();
    }

    /**
     * Simulate a HTTP GET request to the given path and return the rendered page.
     * 
     * @param path
     *            The resource path to query.
     * @throws Exception
     */
    protected void get(String path) throws Exception {
        request(path, "GET");
    }

    /**
     * Simulate a HTTP POST request to the given path and return the rendered page.
     * 
     * @param path
     *            The resource path to query.
     * @throws Exception
     */
    protected void post(String path) throws Exception {
        request(path, "POST");
    }

    /**
     * Overriding the <tt>request()</tt> method in <tt>JspTestCase</tt> in order to perform the
     * necessary compilation before starting the container.
     * 
     * @param path
     *            The path to the JSP file.
     * @param httpMethod
     *            The HTTP request method (GET, POST).
     */
    protected void request(String path, String httpMethod) throws Exception {
        require(path.startsWith("/"), "JSP paths must start with a slash ('/')");
        Class jspClass = compileToClass(path);
        responseFile = File.createTempFile("jsptest-response", ".txt");
        invokeServlet(httpMethod, jspClass);
    }

    private void require(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }

    protected void invokeServlet(String httpMethod, Class jspClass) throws InstantiationException,
            IllegalAccessException, ServletException, IOException {
        ServletContext servletContext = new MockServletContext();
        ServletConfig servletConfig = new MockServletConfig(servletContext);
        MockHttpSession httpSession = new MockHttpSession();
        httpSession.setAttributes(sessionAttributes);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setSession(httpSession);
        request.setMethod(httpMethod);
        request.setAttributes(requestAttributes);
        MockHttpServletResponse response = new MockHttpServletResponse(getRenderedResponse());
        MockJspWriter jspWriter = configureJspFactory(servletContext, request, httpSession);
        initializeAndInvokeJsp(jspClass, servletConfig, request, response);
        writeOutputToTempFile(jspWriter.getContents());
    }

    protected MockJspWriter configureJspFactory(ServletContext httpContext,
            HttpServletRequest httpRequest, HttpSession httpSession) {
        pageContext = new MockPageContext();
        pageContext.setRequest(httpRequest);
        pageContext.setServletContext(httpContext);
        pageContext.setSession(httpSession);
        MockJspWriter jspWriter = new MockJspWriter();
        pageContext.setJspWriter(jspWriter);
        JspFactory.setDefaultFactory(new MockJspFactory(pageContext));
        return jspWriter;
    }

    protected void initializeAndInvokeJsp(Class jspClass, ServletConfig servletConfig,
            HttpServletRequest request, HttpServletResponse response)
            throws InstantiationException, IllegalAccessException, ServletException, IOException {
        HttpServlet servlet = (HttpServlet) jspClass.newInstance();
        servlet.init(servletConfig);
        servlet.service(request, response);
        if (pageContext.getException() != null) {
            throw new RuntimeException(pageContext.getException());
        }
    }

    private Class compileToClass(String path) throws Exception, ClassNotFoundException {
        JspCompilationInfo compilation = getCompiler().compile(path, mockTaglibs);
        return loadJspClass(compilation.getClassName());
    }

    private JasperCompiler getCompiler() {
        compiler.setWebRoot(getWebRoot());
        compiler.setClassOutputBaseDir(getClassOutputBaseDir());
        return compiler;
    }

    private Class loadJspClass(String jspClassName) throws ClassNotFoundException {
        ClassLoader cl = new CustomClassLoader(getClassOutputBaseDir());
        return cl.loadClass(jspClassName);
    }

    protected File writeOutputToTempFile(String output) throws IOException {
        File temp = File.createTempFile("generated_html_", ".html");
        IO.write(output, temp);
        return temp;
    }

    protected void substituteTaglib(String namespace, Class klass) {
        mockTaglibs.put(namespace, klass);
    }
}
