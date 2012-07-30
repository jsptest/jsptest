package net.sf.jsptest.compiler.jsp21;

import java.util.Map;
import net.sf.jsptest.compiler.api.Jsp;
import net.sf.jsptest.compiler.api.JspCompiler;

/**
 * @author Lasse Koskela
 * @author Meinert Schwartau (scwar32)
 */
public class JspCompilerImpl implements JspCompiler {

    public Jsp compile(String path, Map taglibs) {
        throw new RuntimeException("Not implemented");
    }

    public void setOutputDirectory(String directory) {
    }

    public void setWebRoot(String directory) {
    }
}
