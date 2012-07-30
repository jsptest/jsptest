package net.sf.jsptest.compiler.api;

import java.util.Map;

/**
 * @author Lasse Koskela
 * @author Meinert Schwartau (scwar32)
 */
public interface JspCompiler {

    Jsp compile(String path, Map taglibs);

    void setWebRoot(String directory);

    void setOutputDirectory(String directory);
}
