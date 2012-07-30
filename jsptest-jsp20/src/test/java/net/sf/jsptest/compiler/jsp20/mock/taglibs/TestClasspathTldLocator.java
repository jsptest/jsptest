package net.sf.jsptest.compiler.jsp20.mock.taglibs;

import java.io.File;
import junit.framework.TestCase;

/**
 * @author Lasse Koskela
 */
public class TestClasspathTldLocator extends TestCase {

    private TldLocator locator;

    protected void setUp() throws Exception {
        super.setUp();
        locator = new ClasspathTldLocator();
    }

    public void testMissingFilesAreReportedAsNotFound() throws Exception {
        assertFalse(locator.find("nosuchfile.tld").wasFound());
    }

    public void testTldFilesAreFoundFromClasspath() throws Exception {
        TldLocation location = locator.find("exists.tld");
        assertTrue(location.wasFound());
        assertTrue(new File(location.toArray()[0]).exists());
        assertNull(location.toArray()[1]);
    }

    public void testValidLocationIfTldFilesAreFoundFromJarInClasspath() throws Exception {
        TldLocation location = locator.find("c.tld");
        assertTrue(location.wasFound());
        String[] array = location.toArray();
        assertEquals(2, array.length);
        assertFalse("location must not start with 'jar:' : " + array[0], array[0]
                .startsWith("jar:"));
        assertFalse("location must not end with '.tld' : " + array[0], array[0].endsWith(".tld"));
        assertEquals(array[1], "META-INF/c.tld");
    }
}
