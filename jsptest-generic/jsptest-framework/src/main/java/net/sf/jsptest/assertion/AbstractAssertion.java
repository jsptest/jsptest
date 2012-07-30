package net.sf.jsptest.assertion;

import junit.framework.Assert;

/**
 * Base class providing common assertion methods for concrete subclasses.
 * 
 * @author Lasse Koskela
 */
public abstract class AbstractAssertion {

    /**
     * Assert that the given substring (needle) is present in the given string (haystack).
     * 
     * @param message
     *            The optional failure message.
     * @param haystack
     *            The string to find the substring from.
     * @param needle
     *            The substring to find from the haystack.
     */
    protected void assertContains(String message, String haystack, String needle) {
        Assert.assertTrue(message, contains(haystack, needle));
    }

    /**
     * Assert that the given substring (needle) is present in the given string (haystack).
     * 
     * @param haystack
     *            The string to find the substring from.
     * @param needle
     *            The substring to find from the haystack.
     */
    protected void assertContains(String haystack, String needle) {
        String message = "Expected text <" + needle + "> was not found from <" + haystack + ">";
        assertContains(message, haystack, needle);
    }

    /**
     * Assert that the given substring (needle) is <i>not</i> present in the given string
     * (haystack).
     * 
     * @param message
     *            The optional failure message.
     * @param haystack
     *            The string to find the substring from.
     * @param needle
     *            The substring to find from the haystack.
     */
    protected void assertDoesNotContain(String message, String haystack, String needle) {
        Assert.assertFalse(message, contains(haystack, needle));
    }

    /**
     * Assert that the given substring (needle) is <i>not</i> present in the given string
     * (haystack).
     * 
     * @param haystack
     *            The string to find the substring from.
     * @param needle
     *            The substring to find from the haystack.
     */
    protected void assertDoesNotContain(String haystack, String needle) {
        assertDoesNotContain("Expected text <" + needle + "> not to be found from <" + haystack
                + ">", haystack, needle);
    }

    private boolean contains(String haystack, String needle) {
        return haystack.indexOf(needle) > -1;
    }
}
