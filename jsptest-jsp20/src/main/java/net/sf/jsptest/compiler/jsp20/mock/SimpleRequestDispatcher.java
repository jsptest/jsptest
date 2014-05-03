package net.sf.jsptest.compiler.jsp20.mock;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import net.sf.jsptest.compiler.api.Jsp;
import net.sf.jsptest.compiler.api.JspCompilationContext;
import net.sf.jsptest.compiler.api.JspExecutionContext;

public class SimpleRequestDispatcher implements RequestDispatcher {
	private String path;
	private JspCompilationContext compilationContext;
	private JspExecutionContext executionContext;

	public SimpleRequestDispatcher(String path, JspCompilationContext compilationContext, JspExecutionContext executionContext) {
		this.path = path;
		this.compilationContext = compilationContext;
		this.executionContext = executionContext;
	}

	public void forward(ServletRequest request, ServletResponse response)
			throws ServletException, IOException {
		Jsp jsp = compilationContext.getCompiler().compile(path, compilationContext.getSubstituteTaglibs());
		jsp.include(executionContext);
	}

	public void include(ServletRequest request, ServletResponse response)
			throws ServletException, IOException {
		Jsp jsp = compilationContext.getCompiler().compile(path, compilationContext.getSubstituteTaglibs());
		jsp.include(executionContext);
	}

}
