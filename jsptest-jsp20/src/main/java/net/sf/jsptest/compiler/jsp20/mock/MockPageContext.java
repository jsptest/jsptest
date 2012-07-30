package net.sf.jsptest.compiler.jsp20.mock;

import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.VariableResolver;
import org.apache.commons.el.ExpressionEvaluatorImpl;
import org.apache.commons.el.VariableResolverImpl;

/**
 * @author Lasse Koskela
 */
public class MockPageContext extends PageContext {

    private static final String VALID_SCOPES_DESCRIPTION = "Valid scopes are PageContext.PAGE_SCOPE, "
            + "PageContext.REQUEST_SCOPE, PageContext.SESSION_SCOPE"
            + " and PageContext.APPLICATION_SCOPE";
    private JspWriter out = new MockJspWriter();
    private HttpSession session;
    private ServletContext servletContext;
    private ServletRequest request;
    private ServletResponse response;
    private ServletConfig servletConfig;
    private Map pageScopeAttributes;
    private Map applicationScopeAttributes;
    private Object page;
    private ExpressionEvaluator expressionEvaluator;

    public MockPageContext() {
        this(new MockHttpServletRequest());
    }

    public MockPageContext(ServletRequest servletRequest) {
        // TODO: should the constructor invoke initialize(...) instead?
        this.request = servletRequest;
        this.response = new MockHttpServletResponse();
        this.session = new MockHttpSession();
        this.pageScopeAttributes = new HashMap();
        this.applicationScopeAttributes = new HashMap();
        this.servletContext = new MockServletContext();
        this.servletConfig = new MockServletConfig(servletContext);
        this.expressionEvaluator = new ExpressionEvaluatorImpl();
    }

    public Object findAttribute(String name) {
        Object value = null;
        value = getAttribute(name, PageContext.PAGE_SCOPE);
        if (value == null) {
            value = getAttribute(name, PageContext.REQUEST_SCOPE);
        }
        if (value == null) {
            value = getAttribute(name, PageContext.SESSION_SCOPE);
        }
        if (value == null) {
            value = getAttribute(name, PageContext.APPLICATION_SCOPE);
        }
        return value;
    }

    public Object getAttribute(String name) {
        return getAttribute(name, PageContext.PAGE_SCOPE);
    }

    public Object getAttribute(String name, int scope) {
        switch (scope) {
        case PageContext.PAGE_SCOPE:
            return pageScopeAttributes.get(name);
        case PageContext.REQUEST_SCOPE:
            return getRequest().getAttribute(name);
        case PageContext.SESSION_SCOPE:
            return getSession().getAttribute(name);
        case PageContext.APPLICATION_SCOPE:
            return applicationScopeAttributes.get(name);
        }
        throw invalidScope(scope);
    }

    private RuntimeException invalidScope(int scope) {
        throw new RuntimeException(VALID_SCOPES_DESCRIPTION);
    }

    public Enumeration getAttributeNamesInScope(int scope) {
        switch (scope) {
        case PageContext.PAGE_SCOPE:
            return asEnumeration(pageScopeAttributes.keySet());
        case PageContext.REQUEST_SCOPE:
            return getRequest().getAttributeNames();
        case PageContext.SESSION_SCOPE:
            return getSession().getAttributeNames();
        case PageContext.APPLICATION_SCOPE:
            return asEnumeration(applicationScopeAttributes.keySet());
        }
        throw invalidScope(scope);
    }

    private Enumeration asEnumeration(Collection collection) {
        return new Vector(collection).elements();
    }

    public int getAttributesScope(String name) {
        if (name == null) {
            throw new NullPointerException("getAttributesScope(String) doesn't accept null input");
        }
        if (getAttribute(name, PageContext.PAGE_SCOPE) != null) {
            return PageContext.PAGE_SCOPE;
        }
        if (getAttribute(name, PageContext.REQUEST_SCOPE) != null) {
            return PageContext.REQUEST_SCOPE;
        }
        if (getAttribute(name, PageContext.SESSION_SCOPE) != null) {
            return PageContext.SESSION_SCOPE;
        }
        if (getAttribute(name, PageContext.APPLICATION_SCOPE) != null) {
            return PageContext.APPLICATION_SCOPE;
        }
        return 0;
    }

    public Exception getException() {
        // TODO: this is only relevant for an "exception page" type of JSP
        return null;
    }

    public JspWriter getOut() {
        return out;
    }

    public Object getPage() {
        // TODO: in a Servlet environment, this would be an instance of
        // javax.servlet.Servlet
        return page;
    }

    public ServletRequest getRequest() {
        return request;
    }

    public ServletResponse getResponse() {
        return response;
    }

    public ServletConfig getServletConfig() {
        return servletConfig;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public HttpSession getSession() {
        return session;
    }

    public void handlePageException(Exception e) {
        logPageException(e);
        throw new RuntimeException(e);
    }

    public void handlePageException(Throwable e) {
        logPageException(e);
        throw new RuntimeException(e);
    }

    private void logPageException(Throwable e) {
        System.err.println("PAGE EXCEPTION:");
        e.printStackTrace();
    }

    public void forward(String path) throws ServletException, IOException {
        throw new RuntimeException("Unsupported operation: forward(String)");
    }

    public void include(String path) throws ServletException, IOException {
        throw new RuntimeException("Unsupported operation: include(String)");
    }

    public void include(String reference, boolean b) throws ServletException, IOException {
        throw new RuntimeException("Unsupported operation: include(String, boolean)");
    }

    public void initialize(Servlet servlet, ServletRequest request, ServletResponse response,
            String errorPageURL, boolean needsSession, int bufferSize, boolean autoFlush)
            throws IOException, IllegalStateException, IllegalArgumentException {
        this.page = servlet;
        this.request = request;
        this.response = response;
    }

    public void release() {
        applicationScopeAttributes.clear();
        pageScopeAttributes.clear();
    }

    public void removeAttribute(String name) {
        removeAttribute(name, PageContext.REQUEST_SCOPE);
    }

    public void removeAttribute(String name, int scope) {
        switch (scope) {
        case PageContext.REQUEST_SCOPE:
            getRequest().removeAttribute(name);
            break;
        case PageContext.SESSION_SCOPE:
            getSession().removeAttribute(name);
            break;
        case PageContext.PAGE_SCOPE:
            pageScopeAttributes.remove(name);
            break;
        case PageContext.APPLICATION_SCOPE:
            applicationScopeAttributes.remove(name);
            break;
        default:
            throw invalidScope(scope);
        }
    }

    public void setAttribute(String name, Object value) {
        setAttribute(name, value, PageContext.REQUEST_SCOPE);
    }

    public void setAttribute(String name, Object value, int scope) {
        switch (scope) {
        case PageContext.REQUEST_SCOPE:
            getRequest().setAttribute(name, value);
            break;
        case PageContext.SESSION_SCOPE:
            getSession().setAttribute(name, value);
            break;
        case PageContext.PAGE_SCOPE:
            pageScopeAttributes.put(name, value);
            break;
        case PageContext.APPLICATION_SCOPE:
            applicationScopeAttributes.put(name, value);
            break;
        default:
            throw invalidScope(scope);
        }
    }

    public ExpressionEvaluator getExpressionEvaluator() {
        return expressionEvaluator;
    }

    public VariableResolver getVariableResolver() {
        return new VariableResolverImpl(this);
    }

    public void setRequest(ServletRequest servletRequest) {
        this.request = servletRequest;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public void setSession(HttpSession httpSession) {
        this.session = httpSession;
    }

    public void setJspWriter(MockJspWriter jspWriter) {
        this.out = jspWriter;
    }
}
