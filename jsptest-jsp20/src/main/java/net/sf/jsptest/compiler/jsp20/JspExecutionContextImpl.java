package net.sf.jsptest.compiler.jsp20;

import javax.servlet.ServletConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.jsp.PageContext;

import net.sf.jsptest.compiler.api.Jsp;
import net.sf.jsptest.compiler.api.JspExecutionContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JspExecutionContextImpl implements JspExecutionContext {
	private static final Log log = LogFactory.getLog(JspExecutionContextImpl.class);
	private final ServletConfig servletConfig;
	private final ServletRequest request;
	private final ServletResponse response;
	private final PageContext pageContext;
	
	public JspExecutionContextImpl(ServletConfig servletConfig,
			PageContext pageContext, ServletRequest request,
			ServletResponse response) {
		super();
		this.servletConfig = servletConfig;
		this.pageContext = pageContext;
		this.request = request;
		this.response = response;
	}

	public void initializeAndInvoke(Class jspClass, Jsp jsp) {
		try {
            log.debug("Instantiating Servlet: " + jspClass.getName());
            HttpServlet servlet = (HttpServlet) jspClass.newInstance();
            log.debug("Initializing Servlet: " + jspClass.getName());
            servlet.init(servletConfig);
            log.debug("Invoking Servlet: " + jspClass.getName());
            servlet.service(request, response);
            if (pageContext.getException() != null) {
                log.debug("An exception was stored into PageContext. Rethrowing it...");
                throw new RuntimeException(pageContext.getException());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
	}
}
