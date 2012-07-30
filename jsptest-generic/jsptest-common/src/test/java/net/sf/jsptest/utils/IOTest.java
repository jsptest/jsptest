package net.sf.jsptest.utils;

import java.io.File;
import junit.framework.TestCase;

/**
 * @author Lasse Koskela
 */
public class IOTest extends TestCase {

    private File file;

    protected void setUp() throws Exception {
        file = File.createTempFile(getName(), ".txt");
    }

    protected void tearDown() throws Exception {
        file.delete();
    }

    public void testReadAndWriteToFile() throws Exception {
        String content = "content";
        IO.write(content, file);
        assertEquals(content, new String(IO.readToByteArray(file)));
    }
    
    public void testAppendingToFile() throws Exception {
    	IO.append("one", file);
    	IO.append("two", file);
    	assertEquals("onetwo", new String(IO.readToByteArray(file)));
    }
}
