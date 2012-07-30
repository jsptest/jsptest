package net.sf.jsptest.compiler.jsp20;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import junit.framework.TestCase;
import net.sf.jsptest.compiler.api.JspExecution;
import net.sf.jsptest.compiler.jsp20.mock.MockJspWriter;

/**
 * @author Lasse Koskela
 */
public class TestJspImpl extends TestCase {

    public static class FakeServlet extends javax.servlet.http.HttpServlet {

        protected void service(HttpServletRequest req, HttpServletResponse resp)
                throws ServletException, IOException {
            JspFactory _jspxFactory = JspFactory.getDefaultFactory();
            resp.setContentType("text/html");
            PageContext pageContext = _jspxFactory.getPageContext(this, req, resp, null, true,
                    8192, true);
            JspWriter out = pageContext.getOut();
            // this is the way Jasper's JspServlet obtains the session
            HttpSession session = pageContext.getSession();
            out.println("httpMethod=" + req.getMethod());
            Enumeration names = req.getAttributeNames();
            while (names.hasMoreElements()) {
                String name = (String) names.nextElement();
                out.print(name);
                out.print("=");
                out.println(req.getAttribute(name));
            }
            Enumeration names2 = session.getAttributeNames();
            while (names2.hasMoreElements()) {
                String name = (String) names2.nextElement();
                out.print(name);
                out.print("=");
                out.println(session.getAttribute(name));
            }
            Enumeration names3 = req.getParameterNames();
            while (names3.hasMoreElements()) {
                String name = (String) names3.nextElement();
                out.print(name);
                out.print("=");
                out.println(req.getParameter(name));
            }
            out.println();
            out.flush();
            out.close();
        }
    }

    protected String responseOutput;
    private JspImpl jspImpl;
    private Map requestParameters;
    private Map requestAttributes;
    private Map sessionAttributes;
    private MockJspWriter mockJspWriter;

    protected void setUp() throws Exception {
        requestParameters = new HashMap();
        requestAttributes = new HashMap();
        sessionAttributes = new HashMap();
        requestAttributes.put("REQATTR", "REQVALUE");
        sessionAttributes.put("SESATTR", "SESVALUE");
        requestParameters.put("REQPARM", new String[] {"PARAMVAL"});
        jspImpl = new JspImpl(FakeServlet.class) {

            protected MockJspWriter configureJspFactory(ServletContext httpContext,
                    HttpServletRequest httpRequest, HttpSession httpSession) {
                mockJspWriter = super.configureJspFactory(httpContext, httpRequest, httpSession);
                return mockJspWriter;
            }
        };
    }

    public void testHttpMethodIsPassedToTheServletInstance() throws Exception {
        simulateRequest("GET");
        assertOutputContains("httpMethod=GET");
        simulateRequest("POST");
        assertOutputContains("httpMethod=POST");
    }

    public void testRequestParametersArePassedToTheServletInstance() throws Exception {
        simulateRequest();
        assertOutputContains("REQPARM=PARAMVAL");
    }
    
    public void testRequestAttributesArePassedToTheServletInstance() throws Exception {
        simulateRequest();
        assertOutputContains("REQATTR=REQVALUE");
    }

    public void testSessionAttributesArePassedToTheServletInstance() throws Exception {
        simulateRequest();
        assertOutputContains("SESATTR=SESVALUE");
    }

    private void simulateRequest() {
        simulateRequest("GET");
    }

    private void simulateRequest(String method) {
        JspExecution execution = jspImpl.request(method, requestAttributes, sessionAttributes, requestParameters);
        responseOutput = execution.getRenderedResponse();
    }

    private void assertOutputContains(String string) throws IOException {
        String output = mockJspWriter.getContents();
        assertTrue("Output did not contain '" + string + "':\n[" + output + "]", output
                .indexOf(string) != -1);
    }
}
