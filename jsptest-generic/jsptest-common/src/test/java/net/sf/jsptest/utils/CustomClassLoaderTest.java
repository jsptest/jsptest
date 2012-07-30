package net.sf.jsptest.utils;

import java.io.File;
import junit.framework.TestCase;

/**
 * @author Lasse Koskela
 */
public class CustomClassLoaderTest extends TestCase {

    private File baseDir;
    protected String definedName;
    protected byte[] definedData;
    private String fullyQualifiedClassName;

    protected void setUp() throws Exception {
        super.setUp();
        fullyQualifiedClassName = "pkg.Fake";
        baseDir = new File(System.getProperty("java.io.tmpdir"), "basedir");
        File packageDir = new File(baseDir, "pkg");
        packageDir.mkdirs();
        File classFile = new File(packageDir, "Fake.class");
        IO.write("Fake", classFile);
    }

    public void testClassesAreLoadedFromTheGivenBaseDirectory() throws Exception {
        CustomClassLoader cl = new CustomClassLoader(baseDir) {

            // override to avoid actual class loading
            protected Class defineClass(String name, byte[] data) throws ClassFormatError {
                definedName = name;
                definedData = data;
                return CustomClassLoaderTest.class;
            }
        };
        verifyClassLoading(cl);
    }

    public void testBaseDirectoryCanBeGivenAsAbsolutePathName() throws Exception {
        CustomClassLoader cl = new CustomClassLoader(baseDir.getAbsolutePath()) {

            // override to avoid actual class loading
            protected Class defineClass(String name, byte[] data) throws ClassFormatError {
                definedName = name;
                definedData = data;
                return CustomClassLoaderTest.class;
            }
        };
        verifyClassLoading(cl);
    }

    private void verifyClassLoading(CustomClassLoader classLoader) throws ClassNotFoundException {
        classLoader.loadClass(fullyQualifiedClassName);
        assertEquals(fullyQualifiedClassName, definedName);
        assertEquals("Fake", new String(definedData));
    }
}
