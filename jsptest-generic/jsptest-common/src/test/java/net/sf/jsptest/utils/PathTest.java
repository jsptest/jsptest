package net.sf.jsptest.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import junit.framework.TestCase;

/**
 * @author Lasse Koskela
 */
public class PathTest extends TestCase {

    private Path path;
    private File file1;
    private File file2;

    protected void setUp() throws Exception {
        super.setUp();
        path = new Path();
        file1 = File.createTempFile("test", ".tmp");
        file2 = File.createTempFile("test", ".tmp");
    }

    protected void tearDown() throws Exception {
        file1.delete();
        file2.delete();
        super.tearDown();
    }

    public void testEmptyPath() throws Exception {
        assertEquals(0, path.toStringArray().length);
    }

    public void testAddingFileObjects() throws Exception {
        path.add(file1);
        path.add(file2);
        pathShouldBe(file1.getAbsolutePath(), file2.getAbsolutePath());
    }

    public void testAddingObjects() throws Exception {
        path.add(file1.getAbsolutePath());
        path.add(file2.getAbsolutePath());
        pathShouldBe(file1.getAbsolutePath(), file2.getAbsolutePath());
    }

    public void testAddedEntriesDontHaveToBeExistingFiles() throws Exception {
        String nonExistingPath = "nosuch/file.txt";
        String nonExistingAbsolutePath = "/no/such/file.txt";
        path.add(nonExistingPath);
        path.add(nonExistingAbsolutePath);
        pathShouldBe(nonExistingPath, nonExistingAbsolutePath);
    }

    public void testAddingSystemProperties() throws Exception {
        System.setProperty("NO_SUCH_FILE", "no/such/file.txt");
        System.setProperty("EXISTING_FILE", "src/test/resources/PathTest.res");
        path.addSystemProperty("NO_SUCH_FILE");
        path.addSystemProperty("EXISTING_FILE");
        pathShouldBe(System.getProperty("NO_SUCH_FILE"), new File(System
                .getProperty("EXISTING_FILE")).getAbsolutePath());
    }

    public void testAddContainer() throws Exception {
        path.addContainer(junit.framework.Assert.class);
        assertEquals(1, path.toStringArray().length);
        assertTrue(path.toString().indexOf("junit") != -1);
    }

    public void testAddContainerWithClassLoadedByBootstrapClassLoader() throws Exception {
        path.addContainer(String.class);
        assertEquals(1, path.toStringArray().length);
    }

    private void pathShouldBe(String firstEntry, String secondEntry) {
        List expected = new ArrayList();
        expected.add(firstEntry);
        expected.add(secondEntry);
        assertEquals(expected, Arrays.asList(path.toStringArray()));
    }
}
