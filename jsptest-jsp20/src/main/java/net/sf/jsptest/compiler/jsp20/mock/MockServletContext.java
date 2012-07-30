package net.sf.jsptest.compiler.jsp20.mock;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * @author Lasse Koskela
 */
public class MockServletContext implements ServletContext {

    public Set getResourcePaths(String reference) {
        return null;
    }

    public Object getAttribute(String s) {
        return null;
    }

    public Enumeration getAttributeNames() {
        return null;
    }

    public ServletContext getContext(String s) {
        return null;
    }

    public String getInitParameter(String s) {
        return null;
    }

    public Enumeration getInitParameterNames() {
        return null;
    }

    public int getMajorVersion() {
        return 0;
    }

    public String getMimeType(String s) {
        return null;
    }

    public int getMinorVersion() {
        return 0;
    }

    public RequestDispatcher getNamedDispatcher(String s) {
        return null;
    }

    public String getRealPath(String s) {
        return null;
    }

    public RequestDispatcher getRequestDispatcher(String s) {
        return null;
    }

    public URL getResource(String s) throws MalformedURLException {
        return null;
    }

    public InputStream getResourceAsStream(String s) {
        return null;
    }

    public Set getResourcePaths() {
        return null;
    }

    public String getServerInfo() {
        return null;
    }

    public Servlet getServlet(String s) throws ServletException {
        return null;
    }

    public String getServletContextName() {
        return null;
    }

    public Enumeration getServletNames() {
        return null;
    }

    public Enumeration getServlets() {
        return null;
    }

    public void log(Exception e, String s) {
    }

    public void log(String s) {
    }

    public void log(String s, Throwable throwable) {
    }

    public void removeAttribute(String s) {
    }

    public void setAttribute(String s, Object o) {
    }

    public String getContextPath() {
        return null;
    }
}
