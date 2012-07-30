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
package net.sf.jsptest.compiler.java;

import java.lang.reflect.Method;

/**
 * This implementation uses the Java compiler from Sun which ships with the JDK since Java
 * 1.3. The code is inspired by the <tt>javac</tt> Ant task implementation.
 * 
 * @author Mathias Broekelmann
 */
public class SunJavaC implements JavaCompiler {

    private static final String SEPARATOR = System.getProperty("path.separator");

    public boolean compile(String pathToJavaSource, String outputDirectory, String[] classpath)
            throws Exception {
        String classpathString = join(classpath);
        String[] args = buildArgs(pathToJavaSource, outputDirectory, classpathString);
        return compile(args);
    }

    private boolean compile(String[] args) {
        try {
            Class compilerClass = getCompileClass();
            Method compilerMethod = resolveCompilerMethod(compilerClass);
            Object compilerInstance = compilerClass.newInstance();
            Object[] compilerArguments = new Object[] { args };
            int returnCode = ((Integer) compilerMethod.invoke(compilerInstance, compilerArguments))
                    .intValue();
            return returnCode == 0;
        } catch (Throwable e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    private String[] buildArgs(String pathToJavaSource, String outputDirectory,
            String classpathString) {
        return new String[] { "-classpath", classpathString, "-d", outputDirectory,
                pathToJavaSource };
    }

    private String join(String[] classpath) {
        StringBuffer s = new StringBuffer(5000);
        for (int i = 0; i < classpath.length; i++) {
            if (s.length() > 0) {
                s.append(SEPARATOR);
            }
            s.append(classpath[i]);
        }
        return s.toString();
    }

    public boolean isAvailable() {
        try {
            resolveCompilerMethod(getCompileClass());
            return true;
        } catch (Throwable ex) {
            return false;
        }
    }

    private Method resolveCompilerMethod(Class compilerClass) throws NoSuchMethodException {
        return compilerClass.getMethod("compile", new Class[] { (new String[0]).getClass() });
    }

    private Class getCompileClass() throws ClassNotFoundException {
        return Class.forName("com.sun.tools.javac.Main");
    }
}
