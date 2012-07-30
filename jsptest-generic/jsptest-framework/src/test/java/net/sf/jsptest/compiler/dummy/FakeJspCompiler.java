package net.sf.jsptest.compiler.dummy;

import java.util.Map;
import net.sf.jsptest.compiler.api.Jsp;
import net.sf.jsptest.compiler.api.JspCompiler;
import net.sf.jsptest.compiler.api.JspExecution;

/**
 * @author Lasse Koskela
 * @author Meinert Schwartau (scwar32)
 * @author Ronaldo Webb
 */
public class FakeJspCompiler implements JspCompiler {

    private static StringBuffer fakedOutput = new StringBuffer(2000);
    private static String lastCompiledPath;
    private static String lastCompiledWebRoot;
    private String webRoot;

    public void setWebRoot(String directory) {
        this.webRoot = directory;
    }

    protected String getWebRoot() {
        return webRoot;
    }

    public static void cleanOutput() {
        fakedOutput.setLength(0);
    }

    public static void appendOutput(String content) {
        fakedOutput.append(content);
    }

    public Jsp compile(String path, Map taglibs) {
        lastCompiledWebRoot = getWebRoot();
        lastCompiledPath = path;
        return new Jsp() {

            public JspExecution request(String httpMethod, Map requestAttributes,
                    Map sessionAttributes, Map requestParameters) {
                return new JspExecution() {

                    public String getRenderedResponse() {
                        return fakedOutput.toString();
                    }
                };
            }
        };
    }

    public static String lastCompiledPath() {
        return lastCompiledPath;
    }

    public static String lastCompiledWebRoot() {
        return lastCompiledWebRoot;
    }

    public void setOutputDirectory(String directory) {
    }
}
