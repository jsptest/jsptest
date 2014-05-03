package net.sf.jsptest.compiler.api;

public interface JspExecutionContext {
	public void initializeAndInvoke(Class jspClass, Jsp jsp);
}
