package net.sf.jsptest.utils;

import junit.framework.TestCase;

/**
 * @author Lasse Koskela
 */
public class StringsTest extends TestCase {

    public void testReplacingNonexistentNeedle() throws Exception {
        assertEquals("no such thing here", Strings.replace("no such thing here", "needle", "foo"));
    }

    public void testReplacingOneInstance() throws Exception {
        assertEquals("Macs are cool", Strings.replace("what are cool", "what", "Macs"));
    }

    public void testReplacingMultipleInstances() throws Exception {
        assertEquals("Macs and Macs are cool", Strings.replace("what and what are cool", "what",
                "Macs"));
    }

    public void testReplacingWithTheSameValue() throws Exception {
        assertEquals("abcabcabc", Strings.replace("abcabcabc", "bc", "bc"));
    }

    public void testReplacingWithSomethingThatContainsTheSameValue() throws Exception {
        assertEquals("a_bc_a_bc_a_bc_", Strings.replace("abcabcabc", "bc", "_bc_"));
    }

    public void testReplacingEmptyStrings() throws Exception {
        assertEquals("", Strings.replace("", "", ""));
    }
}
