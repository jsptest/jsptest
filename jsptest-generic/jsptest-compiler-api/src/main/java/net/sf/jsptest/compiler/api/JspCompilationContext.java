package net.sf.jsptest.compiler.api;

import java.util.Map;

public class JspCompilationContext {
	
	private final JspCompiler compiler;
	private final Map substituteTaglib;
	
	public JspCompilationContext(JspCompiler compiler, Map substituteTaglib) {
		super();
		this.compiler = compiler;
		this.substituteTaglib = substituteTaglib;
	}

	public JspCompiler getCompiler() {
		return this.compiler;
	}

	public Map getSubstituteTaglibs() {
		return this.substituteTaglib;
	}
}
