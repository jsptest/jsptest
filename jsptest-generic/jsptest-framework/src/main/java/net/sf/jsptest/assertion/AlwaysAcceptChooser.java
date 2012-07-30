package net.sf.jsptest.assertion;

import org.w3c.dom.Element;

/**
 * An <tt>ElementChooser</tt> that always accepts any offered <tt>Element</tt>.
 * 
 * @author Lasse Koskela
 */
public class AlwaysAcceptChooser implements ElementChooser {

    public boolean accept(Element element) {
        return true;
    }
}
