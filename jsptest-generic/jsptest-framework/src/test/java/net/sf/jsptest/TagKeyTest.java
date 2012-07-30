package net.sf.jsptest;

import junit.framework.TestCase;

/**
 * @author Lasse Koskela
 */
public class TagKeyTest extends TestCase {

    public void testEqualsAndHashcode() throws Exception {
        // TODO: add EqualsTester to the class path and use that
        TagKey subject = new TagKey("prefix", "name");
        TagKey samePrefixSameName = new TagKey("prefix", "name");
        TagKey samePrefixDifferentName = new TagKey("prefix", "different");
        TagKey differentPrefixDifferentName = new TagKey("different", "different");
        TagKey differentPrefixSameName = new TagKey("different", "different");
        assertTrue(subject.equals(subject));
        assertEquals(subject.hashCode(), subject.hashCode());
        assertTrue(subject.equals(samePrefixSameName));
        assertEquals(subject.hashCode(), samePrefixSameName.hashCode());
        assertFalse(subject.equals(samePrefixDifferentName));
        assertFalse(subject.equals(differentPrefixSameName));
        assertFalse(subject.equals(differentPrefixDifferentName));
    }

    public void testToStringPrettyPrints() throws Exception {
        assertEquals("prefix:name", new TagKey("prefix", "name").toString());
    }
    
    public void testTagNameDefaultsToAsteriskIfNotSet() throws Exception {
        assertEquals(new TagKey("prefix", "*"), new TagKey("prefix"));
    }
}
