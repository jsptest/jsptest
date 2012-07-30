/*
 * Copyright 2008 Lasse Koskela.
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
package net.sf.jsptest.compiler;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * The <tt>JspCompilationInfo</tt> class acts as a simple data struct, being passed around as a
 * blackboard of sorts where different compilation methods obtain prerequisite information (where to
 * find input) and add newly created information (where the output went).
 * 
 * @author Lasse Koskela
 * @author Meinert Schwartau (scwar32)
 */
public class JspCompilationInfo {

    private String classFileLocation;
    private String jspSourceLocation;
    private String jspClassName;
    private String javaSourceLocation;
    private String jspPath;
    private String classOutputDir;
    private String webRoot;
    private Map taglibs;
    private static Map compilationHistory = new HashMap();

    public JspCompilationInfo() {
        this.taglibs = new HashMap();
    }

    private String absolute(String path) {
        return new File(path).getAbsolutePath();
    }

    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append("{").append(getClass().getName());
        s.append("\n jsp path:   ").append(getJspPath());
        s.append(",\n web root:   ").append(getWebRoot());
        s.append(",\n jsp file:   ").append(getJspSource());
        s.append(",\n java file:  ").append(getJavaSource());
        s.append(",\n class file: ").append(getClassFile());
        s.append(",\n class name: ").append(getClassName());
        s.append(",\n output dir: ").append(getClassOutputDir());
        s.append("\n}");
        return s.toString();
    }

    public String getClassName() {
        return jspClassName;
    }

    public void setClassName(String jspClassName) {
        this.jspClassName = jspClassName;
    }

    public String getClassFile() {
        return classFileLocation;
    }

    public void setClassFile(String classFileLocation) {
        this.classFileLocation = absolute(classFileLocation);
    }

    public String getJspSource() {
        return jspSourceLocation;
    }

    public void setJspSource(String jspSourceLocation) {
        this.jspSourceLocation = absolute(jspSourceLocation);
    }

    public String getJavaSource() {
        return javaSourceLocation;
    }

    public void setJavaSource(String javaSourceLocation) {
        this.javaSourceLocation = absolute(javaSourceLocation);
    }

    public String getJspPath() {
        return jspPath;
    }

    public void setJspPath(String jspPath) {
        this.jspPath = jspPath;
    }

    public String getClassOutputDir() {
        return classOutputDir;
    }

    public void setClassOutputDir(String outputDir) {
        this.classOutputDir = absolute(outputDir);
    }

    public String getWebRoot() {
        return webRoot;
    }

    public void setWebRoot(String webRoot) {
        this.webRoot = webRoot;
    }

    public Map getTaglibs() {
        return taglibs;
    }

    public void setTaglibs(Map taglibs) {
        this.taglibs = new HashMap(taglibs);
    }

    public synchronized void compilationWasSuccessful() {
        compilationHistory.put(jspSourceLocation, new LastCompile(taglibs));
    }

    public synchronized boolean jspCompilationRequired() {
        // TODO: only avoid compilation if there is a matching MD5 for the .jsp
        // file in a cache or if the file is missing altogether.
        File jsp = new File(jspSourceLocation);
        File java = new File(javaSourceLocation);
        File clazz = new File(classFileLocation);
        return (doesNotExistOrIsTooOld(clazz) || doesNotExistOrIsTooOld(java)
                || jsp.lastModified() > java.lastModified() || taglibsHaveChangedSinceLastCompile());
    }

    private boolean taglibsHaveChangedSinceLastCompile() {
        LastCompile lastCompile = (LastCompile) compilationHistory.get(getJspSource());
        if (lastCompile == null) {
            return true;
        }
        return !lastCompile.getTaglibs().equals(getTaglibs());
    }

    private boolean doesNotExistOrIsTooOld(File file) {
        long expirationAge = 5 * 60 * 1000L;
        long expirationThreshold = System.currentTimeMillis() - expirationAge;
        return file.exists() == false || file.lastModified() < expirationThreshold;
    }

    /**
     * Represents the tag configuration of a prior compilation.
     * 
     * @author Meinert Schwartau (scwar32)
     */
    private static final class LastCompile {

        private final Map taglibs;

        public LastCompile(Map taglibs) {
            this.taglibs = taglibs;
        }

        public Map getTaglibs() {
            return taglibs;
        }
    }
}
