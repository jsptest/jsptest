package net.sf.jsptest.assertion;

import org.w3c.dom.Element;

/**
 * An interface for building a variety of element selection rules.
 * 
 * @author Lasse Koskela
 */
public interface ElementChooser {

    /**
     * Indicates whether this particular <tt>ElementChooser</tt> accepts the given
     * <tt>Element</tt>.
     * 
     * @param element
     *            The candidate <tt>Element</tt>.
     * @return <tt>true</tt> if this <tt>ElementChooser</tt> accepts the given <tt>Element</tt>,
     *         <tt>false</tt> otherwise.
     */
    boolean accept(Element element);
}
