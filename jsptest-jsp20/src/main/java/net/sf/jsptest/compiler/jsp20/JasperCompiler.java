/*
 * Copyright 2007 Lasse Koskela.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.jsptest.compiler.jsp20;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.jsp.tagext.TagInfo;
import net.sf.jsptest.compiler.JspCompilationInfo;
import net.sf.jsptest.compiler.java.CommandLineJavac;
import net.sf.jsptest.compiler.java.Java6Compiler;
import net.sf.jsptest.compiler.java.JavaCompiler;
import net.sf.jsptest.compiler.java.SunJavaC;
import net.sf.jsptest.compiler.jsp20.mock.MockOptions;
import net.sf.jsptest.compiler.jsp20.mock.MockServletConfig;
import net.sf.jsptest.compiler.jsp20.mock.MockTagInfo;
import net.sf.jsptest.utils.Path;
import net.sf.jsptest.utils.Strings;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jasper.EmbeddedServletOptions;
import org.apache.jasper.JasperException;
import org.apache.jasper.JspCompilationContext;
import org.apache.jasper.Options;
import org.apache.jasper.compiler.AntCompiler;
import org.apache.jasper.compiler.Compiler;
import org.apache.jasper.compiler.JspRuntimeContext;
import org.apache.jasper.compiler.ServletWriter;
import org.apache.jasper.servlet.JspCServletContext;
import org.apache.jasper.servlet.JspServletWrapper;

/**
 * The <tt>JasperTestCase</tt> provides a facility for compiling JavaServer Pages outside a real
 * Servlet/JSP container.
 * <p>
 * It makes use of Jakarta Tomcat's Jasper JSP compiler to compile a JSP file into Java source code,
 * and then Sun's javac implementation to compile the Java source code into Java bytecode.
 * <p>
 * The resulting .class file is written under a "WEB-INF/classes" directory under the "web root"
 * defined by concrete subclasses through the implementation of <tt>getWebRoot()</tt>. If you
 * want the .class files to be generated somewhere else than under the web root, you can also
 * override <tt>getClassOutputBaseDir()</tt>, which specifies the root directory for the compiled
 * .class files.
 * <p>
 * The resulting Servlet class gets its package based on the <tt>getJspPackageName()</tt> method
 * which can be overridden if necessary. The default is "jsp" which means that, for example, a JSP
 * named "front_page.jsp" would eventually be translated into a class file
 * "[webroot]/WEB-INF/classes/jsp/front_page_jsp.class" where "jsp/" is the JSP package name and
 * "front_page_jsp.class" the normalized class name derived from the source JSP file's name.
 * 
 * @author Lasse Koskela
 * @author Meinert Schwartau (scwar32)
 */
public class JasperCompiler {

    private static final Log log = LogFactory.getLog(JasperCompiler.class);
    private static JavaCompiler COMPILER = determineJavaCompiler();
    private String webRoot;
    private String classOutputBaseDir;
    private String jspPackageName;

    public JasperCompiler() {
        webRoot = ".";
        classOutputBaseDir = ".";
    }

    /**
     * Sets the "web root", i.e. the root directory of your exploded J2EE web application. In other
     * words, this is the directory under which you should have a subdirectory named "WEB-INF".
     */
    public void setWebRoot(String webRoot) {
        this.webRoot = webRoot;
    }

    /**
     * Sets the directory where generated .class file(s) should be written..
     */
    public void setClassOutputBaseDir(String directory) {
        this.classOutputBaseDir = directory;
    }

    /**
     * Compile the specified JSP source file into bytecode.
     * 
     * @param path
     *            The path to the JSP source file to compile, given relative to the web root.
     * @param mockTaglibs
     *            Mapping of tag names to tag handler classes
     */
    public JspCompilationInfo compile(String path, Map mockTaglibs) throws Exception {
        JspCompilationInfo info = createJspCompilationInfo(path, mockTaglibs);
        if (info.jspCompilationRequired()) {
            compileJsp(info);
            compileJavaToBytecode(info);
        } else {
            log.debug("  No compilation needed for " + info.getJspSource());
        }
        return info;
    }

    /**
     * Sets the package name for the generated Java classes. The default package name is "jsp".
     */
    public void setJspPackageName(String packageName) {
        this.jspPackageName = packageName;
    }

    /**
     * Returns the package name for the generated Java class.
     */
    private String getJspPackageName() {
        if (jspPackageName != null) {
            return jspPackageName;
        } else {
            return "jsp";
        }
    }

    private JspCompilationInfo createJspCompilationInfo(String jsp, Map mockTaglibs) {
        JspCompilationInfo info = new JspCompilationInfo();
        info.setJspPath(jsp);
        info.setClassOutputDir(classOutputBaseDir);
        info.setJspSource(resolveJspSourceFile(jsp));
        info.setWebRoot(getWebRoot());
        info.setTaglibs(mockTaglibs);
        resolveJavaSourceFile(info);
        resolveClassFileLocation(info);
        resolveClassName(info);
        return info;
    }

    private String getWebRoot() {
        File root = new File(webRoot);
        if (root.exists() && root.isDirectory()) {
            return root.getAbsolutePath();
        } else {
            return resolveWebRootFromClassPath();
        }
    }

    private String resolveWebRootFromClassPath() {
        String path = webRoot;
        if (path.startsWith("./")) {
            path = path.substring(2);
        }
        URL url = getClass().getClassLoader().getResource(path);
        if (url == null) {
            return webRoot;
        }
        if (!url.toExternalForm().startsWith("file:")) {
            log.info("Web root referenced a non-filesystem resource: " + url);
            return webRoot;
        }
        return new File(url.toExternalForm().substring("file:".length())).getAbsolutePath();
    }

    private void compileJsp(JspCompilationInfo info) throws Exception {
        assertTrue("Source file " + new File(info.getJspSource()).getAbsolutePath()
                + " does not exist", new File(info.getJspSource()).exists());
        PrintWriter logWriter = new PrintWriter(new StringWriter());
        URL baseUrl = new File(info.getWebRoot()).toURL();
        ServletContext sContext = new JspCServletContext(logWriter, baseUrl);
        ServletConfig sConfig = new MockServletConfig(sContext);
        Options options = createOptions(sContext, sConfig, info);
        JspRuntimeContext rtContext = new JspRuntimeContext(sContext, options);
        JspServletWrapper sWrapper = makeWrapper(sContext, options, rtContext);
        JspCompilationContext cContext = createJspCompilationContext(info, sContext, options,
                rtContext, sWrapper, new StringWriter());
        logCompilation(info.getJspSource(), info.getClassOutputDir());
        compileJspToJava(sWrapper, cContext);
        File javaFile = new File(info.getJavaSource());
        assertTrue("Failed to generate .java source code to " + javaFile.getAbsolutePath(),
                javaFile.exists());
        info.compilationWasSuccessful();
    }

    private void compileJspToJava(JspServletWrapper jspServletWrapper,
            JspCompilationContext jspCompilationContext) throws FileNotFoundException,
            JasperException, Exception {
        Compiler compiler = new AntCompiler();
        compiler.init(jspCompilationContext, jspServletWrapper);
        compiler.compile();
    }

    private JspCompilationContext createJspCompilationContext(JspCompilationInfo info,
            ServletContext servletContext, Options options, JspRuntimeContext jspRuntimeContext,
            JspServletWrapper jspServletWrapper, StringWriter stringWriter) {
        boolean isErrorPage = false;
        JspCompilationContext cContext = new JspCompilationContext(info.getJspPath(), isErrorPage,
                options, servletContext, jspServletWrapper, jspRuntimeContext);
        cContext.getOutputDir(); // forces creation of the directory tree
        cContext.setServletJavaFileName(info.getJavaSource());
        cContext.setServletPackageName(getJspPackageName());
        cContext.setWriter(new ServletWriter(new PrintWriter(stringWriter)));
        createPathToGeneratedJavaSource(info);
        return cContext;
    }

    private void createPathToGeneratedJavaSource(JspCompilationInfo info) {
        new File(info.getJavaSource()).getParentFile().mkdirs();
    }

    private JspServletWrapper makeWrapper(ServletContext servletContext, Options options,
            JspRuntimeContext jspRuntimeContext) throws MalformedURLException, JasperException {
        TagInfo tagInfo = new MockTagInfo();
        String tagFilePath = "/";
        URL tagFileJarUrl = new File(".").toURL();
        JspServletWrapper wrapper = new JspServletWrapper(servletContext, options, tagFilePath,
                tagInfo, jspRuntimeContext, tagFileJarUrl);
        return wrapper;
    }

    private Options createOptions(ServletContext ctx, ServletConfig cfg, JspCompilationInfo info) {
        Options options = new EmbeddedServletOptions(cfg, ctx);
        return new MockOptions(options, ctx, info);
    }

    private void resolveJavaSourceFile(JspCompilationInfo info) {
        File dir = new File(info.getClassOutputDir());
        if (getJspPackageName().length() > 0) {
            dir = new File(dir, getJspPackageName().replace('.', '/'));
        }
        dir.mkdirs();
        String name = resolveJavaSourceFileName(info.getJspPath());
        info.setJavaSource(new File(dir, name).getPath());
    }

    private String resolveJavaSourceFileName(String jspPath) {
        String name = encodeSpecialCharacters(jspPath);
        if (name.startsWith("/")) {
            name = name.substring(1);
        }
        return name + ".java";
    }

    private String encodeSpecialCharacters(String name) {
        StringBuffer result = new StringBuffer();
        char[] chars = name.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '-') {
                result.append("_002d");
            } else if (chars[i] == '_') {
                result.append("_005f");
            } else if (chars[i] == '.') {
                result.append("_");
            } else {
                result.append(chars[i]);
            }
        }
        return result.toString();
    }

    private String resolveJspSourceFile(String jsp) {
        if (jsp.startsWith("/")) {
            jsp = jsp.substring(1);
        }
        return new File(getWebRoot(), jsp).getPath();
    }

    private void resolveClassName(JspCompilationInfo info) {
        String baseName = new File(info.getJavaSource()).getName();
        baseName = baseName.substring(0, baseName.indexOf("."));
        String packageName = getPackagePrefix() + getSubDirPackagePrefix(info);
        info.setClassName(packageName + baseName);
    }

    private String getPackagePrefix() {
        String packagePrefix = getJspPackageName();
        if (packagePrefix != null && packagePrefix.length() > 0) {
            packagePrefix = packagePrefix + ".";
        }
        return packagePrefix;
    }

    private String getSubDirPackagePrefix(JspCompilationInfo info) {
        String dirPrefix = info.getJspPath();
        if (dirPrefix.startsWith("/")) {
            dirPrefix = dirPrefix.substring(1);
        }
        int lastSlashIndex = dirPrefix.lastIndexOf("/");
        if (lastSlashIndex != -1) {
            dirPrefix = dirPrefix.substring(0, lastSlashIndex);
            dirPrefix = encodeSpecialCharacters(dirPrefix);
            dirPrefix = dirPrefix.replace('/', '.') + ".";
        } else {
            dirPrefix = "";
        }
        return dirPrefix;
    }

    private void compileJavaToBytecode(JspCompilationInfo info) throws Exception {
        File classFile = new File(info.getClassFile());
        classFile.delete();
        logCompilation(info.getJavaSource(), info.getClassOutputDir());
        boolean ok = javac()
                .compile(info.getJavaSource(), info.getClassOutputDir(), getClassPath());
        assertTrue("javac failed", ok);
        assertTrue("Failed to compile .java file to " + classFile.getAbsolutePath(), classFile
                .exists());
    }

    private String[] getClassPath() {
        Path cp = new Path();
        cp.addSystemProperty("java.class.path");
        cp.addContainer(javax.servlet.jsp.tagext.JspTag.class);
        cp.addContainer(javax.servlet.jsp.jstl.core.LoopTag.class);
        cp.addContainer(javax.servlet.http.HttpServlet.class);
        cp.addContainer(org.apache.taglibs.standard.Version.class);
        cp.addContainer(org.apache.jasper.JspC.class);
        cp.addContainer(org.apache.jasper.compiler.Compiler.class);
        cp.addContainer(org.apache.jasper.runtime.HttpJspBase.class);
        cp.add(new File("target", "test-classes").getAbsolutePath());
        cp.add(new File("target", "classes").getAbsolutePath());
        return cp.toStringArray();
    }

    private void resolveClassFileLocation(JspCompilationInfo info) {
        String file = Strings.replace(info.getJavaSource(), ".java", ".class");
        info.setClassFile(file);
    }

    private static void assertTrue(String errorMessage, boolean condition) {
        if (!condition) {
            throw new RuntimeException(errorMessage);
        }
    }

    private static JavaCompiler determineJavaCompiler() {
        List compilers = new ArrayList();
        // this doesn't work because with Maven we need to set the classpath
        // explicitly as the "current" classpath does not include our
        // dependencies
        compilers.add(new Java6Compiler());
        compilers.add(new SunJavaC());
        compilers.add(new CommandLineJavac());
        for (Iterator i = compilers.iterator(); i.hasNext();) {
            JavaCompiler compiler = (JavaCompiler) i.next();
            if (compiler.isAvailable()) {
                log.debug("Using JavaCompiler: " + compiler.getClass().getName());
                return compiler;
            }
        }
        throw new RuntimeException("No JavaCompiler implementation available on the system");
    }

    private static JavaCompiler javac() {
        return COMPILER;
    }

    private void logCompilation(String src, String dst) {
        log.debug("  Compiling " + src + " to " + dst);
    }
}
