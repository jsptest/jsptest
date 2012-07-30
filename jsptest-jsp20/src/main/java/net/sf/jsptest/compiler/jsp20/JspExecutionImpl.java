package net.sf.jsptest.compiler.jsp20;

import net.sf.jsptest.compiler.api.JspExecution;

/**
 * @author Lasse Koskela
 */
public class JspExecutionImpl implements JspExecution {

    private final String renderedOutput;

    public JspExecutionImpl(String output) {
        this.renderedOutput = output;
    }

    public String getRenderedResponse() {
        return renderedOutput;
    }
}
