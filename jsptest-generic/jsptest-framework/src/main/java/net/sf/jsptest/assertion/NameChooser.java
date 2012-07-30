package net.sf.jsptest.assertion;

import org.w3c.dom.Element;

/**
 * @author Lasse Koskela
 */
public class NameChooser implements ElementChooser {

    private final String name;

    public NameChooser(String name) {
        this.name = name;
    }

    public boolean accept(Element element) {
        return name.equals(element.getAttribute("NAME"));
    }
}
