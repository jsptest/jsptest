package net.sf.jsptest.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import junit.framework.TestCase;

/**
 * @author Lasse Koskela
 */
public class StreamConsumerTest extends TestCase {

    private StringWriter log;
    private String name;

    protected void setUp() throws Exception {
        super.setUp();
        log = new StringWriter();
        name = "name";
    }

    public void testConsumesAllAvailableInput() throws Exception {
        InputStream source = new ByteArrayInputStream("abc".getBytes());
        assertEquals(3, source.available());
        runStreamConsumerOn(source);
        assertEquals(0, source.available());
    }

    public void testWritesNameAndFullContentAfterReadingEverything() throws Exception {
        runStreamConsumerOn(new ByteArrayInputStream("abc".getBytes()));
        assertEquals(name + ":\nabc\n", log.toString());
    }

    private void runStreamConsumerOn(InputStream source) {
        new StreamConsumer(name, source, new PrintWriter(log)).run();
    }
}
