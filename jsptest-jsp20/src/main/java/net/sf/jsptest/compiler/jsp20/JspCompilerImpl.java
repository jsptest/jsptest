package net.sf.jsptest.compiler.jsp20;

import java.io.File;
import java.util.Map;
import net.sf.jsptest.compiler.JspCompilationInfo;
import net.sf.jsptest.compiler.api.Jsp;
import net.sf.jsptest.compiler.api.JspCompiler;
import net.sf.jsptest.utils.CustomClassLoader;

/**
 * @author Lasse Koskela
 * @author Meinert Schwartau (scwar32)
 */
public class JspCompilerImpl implements JspCompiler {

    private String outputDirectory = new File(System.getProperty("java.io.tmpdir"),
            "jsptest-classes").getAbsolutePath();
    private String webRoot;

    public void setOutputDirectory(String directory) {
        outputDirectory = directory;
    }

    protected String getOutputDirectory() {
        return outputDirectory;
    }

    public void setWebRoot(String directory) {
        this.webRoot = directory;
    }

    protected String getWebRoot() {
        return webRoot;
    }

    public Jsp compile(final String jspPath, Map taglibs) {
        try {
            JasperCompiler compiler = new JasperCompiler();
            compiler.setWebRoot(getWebRoot());
            compiler.setClassOutputBaseDir(getOutputDirectory());
            JspCompilationInfo info = compiler.compile(jspPath, taglibs);
            final Class servletClass = compileToClass(info);
            return new JspImpl(servletClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Class compileToClass(JspCompilationInfo compilation) throws Exception,
            ClassNotFoundException {
        return loadJspClass(compilation.getClassName());
    }

    private Class loadJspClass(String jspClassName) throws ClassNotFoundException {
        ClassLoader cl = new CustomClassLoader(getOutputDirectory());
        return cl.loadClass(jspClassName);
    }
}
