package net.sf.jsptest;

import net.sf.jsptest.compiler.dummy.FakeJspCompiler;
import junit.framework.TestCase;

/**
 * @author Lasse Koskela
 */
public abstract class AbstractFakeJspCompilerTestCase extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
        StringBuffer output = new StringBuffer(2000);
        appendOutput(output);
        FakeJspCompiler.appendOutput(output.toString());
    }

    protected void tearDown() throws Exception {
        FakeJspCompiler.cleanOutput();
        super.tearDown();
    }

    protected abstract void appendOutput(StringBuffer buffer);
}
