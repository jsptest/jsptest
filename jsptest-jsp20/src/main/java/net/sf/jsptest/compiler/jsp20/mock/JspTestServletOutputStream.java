package net.sf.jsptest.compiler.jsp20.mock;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletOutputStream;

/**
 * @author Lasse Koskela
 */
public class JspTestServletOutputStream extends ServletOutputStream {

    private final OutputStream stream;

    public JspTestServletOutputStream(OutputStream stream) {
        this.stream = stream;
    }

    public void write(int b) throws IOException {
        stream.write(b);
    }
}
