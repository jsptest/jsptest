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

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import net.sf.jsptest.utils.StreamConsumer;

/**
 * @author Lasse Koskela
 */
public class CommandLineJavac implements JavaCompiler {

    private static final String SEPARATOR = System.getProperty("path.separator");

    public boolean compile(String srcFile, String destDir, String[] classpath) throws Exception {
        srcFile = new File(srcFile).getCanonicalPath();
        String cp = join(classpath);
        String[] command = buildCommandLine(srcFile, destDir, cp);
        return execute(command);
    }

    protected String[] buildCommandLine(String pathToJavaSource, String outputDirectory,
            String classpathString) {
        return new String[] { "javac", "-classpath", classpathString, "-d", outputDirectory,
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

    protected boolean execute(String[] commandLine) throws IOException, InterruptedException {
        Process p = Runtime.getRuntime().exec(commandLine);
        String processOutput = readOutput(p);
        boolean success = (p.waitFor() == 0);
        if (!success) {
            System.err.println(processOutput);
        }
        return success;
    }

    private String readOutput(final Process p) throws IOException {
        try {
            StringWriter output = new StringWriter();
            final PrintWriter ps = new PrintWriter(output);
            Thread stderrThread = new Thread(new StreamConsumer("STDERR", p.getErrorStream(), ps));
            Thread stdoutThread = new Thread(new StreamConsumer("STDOUT", p.getInputStream(), ps));
            p.getOutputStream().close();
            stderrThread.start();
            stdoutThread.start();
            stderrThread.join();
            stdoutThread.join();
            return output.toString();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            return sw.toString();
        }
    }

    public boolean isAvailable() {
        try {
            Process p = Runtime.getRuntime().exec("javac");
            String s = readOutput(p);
            return s.indexOf("Usage: javac") != -1;
        } catch (Exception e) {
            return false;
        }
    }
}
