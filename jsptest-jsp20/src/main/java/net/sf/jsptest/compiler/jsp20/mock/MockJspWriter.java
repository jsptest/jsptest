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
package net.sf.jsptest.compiler.jsp20.mock;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.jsp.JspWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Lasse Koskela
 */
public class MockJspWriter extends JspWriter {

    private Log log;
    private PrintWriter writer;
    private StringWriter stringWriter;

    public String getContents() {
        return stringWriter.toString();
    }

    public MockJspWriter() {
        super(1024, true);
        log = LogFactory.getLog(getClass());
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter, true);
    }

    public void newLine() throws IOException {
        writer.println();
    }

    public void print(boolean x) throws IOException {
        writer.print(x);
    }

    public void print(char x) throws IOException {
        writer.print(x);
    }

    public void print(int x) throws IOException {
        writer.print(x);
    }

    public void print(long x) throws IOException {
        writer.print(x);
    }

    public void print(float x) throws IOException {
        writer.print(x);
    }

    public void print(double x) throws IOException {
        writer.print(x);
    }

    public void print(char[] x) throws IOException {
        writer.print(x);
    }

    public void print(String x) throws IOException {
        writer.print(x);
    }

    public void print(Object x) throws IOException {
        writer.print(x);
    }

    public void println() throws IOException {
        writer.println();
    }

    public void println(boolean x) throws IOException {
        writer.println(x);
    }

    public void println(char x) throws IOException {
        writer.println(x);
    }

    public void println(int x) throws IOException {
        writer.println(x);
    }

    public void println(long x) throws IOException {
        writer.println(x);
    }

    public void println(float x) throws IOException {
        writer.println(x);
    }

    public void println(double x) throws IOException {
        writer.println(x);
    }

    public void println(char[] x) throws IOException {
        writer.println(x);
    }

    public void println(String x) throws IOException {
        writer.println(x);
    }

    public void println(Object x) throws IOException {
        writer.println(x);
    }

    public void clear() throws IOException {
        notImplemented("clear()");
    }

    public void clearBuffer() throws IOException {
        notImplemented("clearBuffer()");
    }

    public void flush() throws IOException {
        writer.flush();
    }

    public void close() throws IOException {
        writer.close();
    }

    public int getRemaining() {
        notImplemented("getRemaining()");
        return 0;
    }

    public void write(char[] x, int start, int length) throws IOException {
        writer.write(x, start, length);
    }

    private void notImplemented(String methodSignature) {
        log.error(getClass().getName() + "#" + methodSignature + " not implemented");
    }
}
