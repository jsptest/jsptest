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
package net.sf.jsptest;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import net.sf.jsptest.compiler.dummy.FakeJspCompiler;

/**
 * @author Lasse Koskela
 */
public class TestJspTestCase extends TestCase {

    private String renderedOutput;
    private JspTestCase testcase;

    protected void setUp() throws Exception {
        super.setUp();
        renderedOutput = "there is a needle in this haystack";
        testcase = new JspTestCase() {

            protected String getWebRoot() {
                return "./websrc";
            }
        };
    }

    public void testCorrectWebRootIsPassedToJspCompiler() throws Exception {
        testcase.get("/index.jsp");
        assertEquals("./websrc", FakeJspCompiler.lastCompiledWebRoot());
    }

    public void testCorrectPathIsPassedToJspCompiler() throws Exception {
        String path = "/index.jsp";
        testcase.get(path);
        assertEquals(path, FakeJspCompiler.lastCompiledPath());
    }

    public void testAccessToRenderedOutput() throws Exception {
        FakeJspCompiler.appendOutput(renderedOutput);
        testcase.get("/index.jsp");
        testcase.output().shouldContain(renderedOutput);
    }

    public void testOutputShouldContain() throws Exception {
        testcase.get("/index.jsp");
        testcase.output().shouldContain("needle");
        try {
            testcase.output().shouldContain("No such content");
            throw new RuntimeException("Assertion should've failed");
        } catch (AssertionFailedError expected) {
        }
    }

    public void testOutputShouldNotContain() throws Exception {
        testcase.get("/index.jsp");
        testcase.output().shouldNotContain("no such content");
        try {
            testcase.output().shouldNotContain("needle");
            throw new RuntimeException("Assertion should've failed");
        } catch (AssertionFailedError expected) {
        }
    }

    public void testRenderingSameJspMultipleTimes() throws Exception {
        for (int i = 0; i < 5; i++) {
            testcase.get("/index.jsp");
            testcase.output().shouldContain("needle");
        }
    }

    public void testJspFileWithSpecialCharactersInName() throws Exception {
        testcase.get("/name$_test-file2.jsp");
        testcase.output().shouldContain("needle");
    }

    public void testJspFileInSubDirectory() throws Exception {
        testcase.get("/sub/dir/page_in_subdir.jsp");
        testcase.output().shouldContain("needle");
    }

    public void testJspPathNotStartingWithSlash() throws Exception {
        try {
            testcase.get("sub/dir/page_in_subdir.jsp");
            fail("JSP path not starting with a slash should raise an exception");
        } catch (AssertionFailedError pass) {
            throw pass;
        } catch (Throwable e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }
}
