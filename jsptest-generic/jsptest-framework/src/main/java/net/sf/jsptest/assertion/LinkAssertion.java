package net.sf.jsptest.assertion;

import org.w3c.dom.Element;

/**
 * Provides assertion methods related to HTML anchors/links.
 * 
 * @author Lasse Koskela
 */
public class LinkAssertion extends DOMAssertion {

    /**
     * @param context
     *            The link element to serve as the context for subsequent assertion methods.
     */
    public LinkAssertion(Element context) {
        this.context = context;
    }

    /**
     * Assert that the selected link has the exact given label. Used in the following way:
     * <code>page().shouldHaveLink().withText("click here");</code>
     * 
     * @param labelText
     *            The expected label (has to be an exact match).
     */
    public void withText(String labelText) {
        shouldContainElement("Link with text '" + labelText + "' was not found from "
                + getContextAsString(), "//A[text()='" + labelText + "']");
    }

    /**
     * Assert that the selected link's label contains the given text. Used in the following way:
     * <code>page().shouldHaveLink().withPartialText("click");</code>
     * 
     * @param substringOfLabelText
     *            The substring that the label is expected to contain.
     */
    public void withPartialText(String substringOfLabelText) {
        shouldContainElement("Link with partial text '" + substringOfLabelText
                + "' was not found from " + getContextAsString(), "//A[fn:contains(text(), '"
                + substringOfLabelText + "')]");
    }

    /**
     * Assert that the selected link has the exact given name (that is, the "name" attribute). Used
     * in the following way: <code>page().shouldHaveLink().withName("link_confirm_purchase");</code>
     * 
     * @param name
     *            The expected value of the "name" attribute of the link element.
     */
    public void withName(String name) {
        shouldContainElement("Link with name '" + name + "' was not found from "
                + getContextAsString(), "//A[@NAME='" + name + "']");
    }

    /**
     * Assert that the selected link has the exact given ID. Used in the following way:
     * <code>page().shouldHaveLink().withId("link_confirm_purchase");</code>
     * 
     * @param id
     *            The expected value of the "id" attribute of the link element.
     */
    public void withId(String id) {
        shouldContainElement(
                "Link with ID '" + id + "' was not found from " + getContextAsString(), "//A[@ID='"
                        + id + "']");
    }

    /**
     * Assert that the selected link points to the given URL.
     * 
     * @param url
     *            The expected URL to compare to the link element's "href" attribute.
     */
    public void withHref(String url) {
        shouldContainElement("Link pointing to " + url + " was not found from "
                + getContextAsString(), "//A[@HREF='" + url + "']");
    }

    /**
     * Assert that the selected link has been styled with the given CSS class.
     * 
     * @param cssClass
     *            The expected value of the link element's "class" attribute.
     */
    public void withClass(String cssClass) {
        shouldContainElement("Link with CSS class '" + cssClass + "' was not found from "
                + getContextAsString(), "//A[@CLASS='" + cssClass + "']");
    }

    /**
     * Assert that the selected link wraps an image by the given ID.
     * 
     * @param id
     *            The expected ID of the wrapped image.
     */
    public void withImageId(String id) {
        shouldContainElement("Image link with image ID '" + id + "' was not found from "
                + getContextAsString(), "//A/IMG[@ID='" + id + "']");
    }

    /**
     * Assert that the selected link wraps an image by the given title (that is, the "title"
     * attribute of the <tt>img</tt> element).
     * 
     * @param title
     *            The expected title of the wrapped image.
     */
    public void withImageTitle(String title) {
        shouldContainElement("Image link with image titled '" + title + "' was not found from "
                + getContextAsString(), "//A/IMG[@TITLE='" + title + "']");
    }

    /**
     * Assert that the selected link wraps an image by the given name (that is, the "name" attribute
     * of the <tt>img</tt> element).
     * 
     * @param name
     *            The expected name of the wrapped image.
     */
    public void withImageName(String name) {
        shouldContainElement("Image link with image named '" + name + "' was not found from "
                + getContextAsString(), "//A/IMG[@NAME='" + name + "']");
    }

    /**
     * Assert that the selected link wraps an image at the given URL.
     * 
     * @param url
     *            The expected URL ("src" attribute) of the wrapped image.
     */
    public void withImageSrc(String url) {
        shouldContainElement("Image link with image URL '" + url + "' was not found from "
                + getContextAsString(), "//A/IMG[@SRC='" + url + "']");
    }

    /**
     * Assert that the selected link wraps an image by the given file name.
     * 
     * @param filename
     *            The expected file name of the wrapped image. Can be just the base name (e.g.
     *            "apple.gif") or a partial path to the image (e.g. "images/press/chairman.jpg").
     */
    public void withImageFileName(String filename) {
        shouldContainElement("Image link with the image URL ending with '" + filename
                + "' was not found from " + getContextAsString(), "//A/IMG[fn:ends-with(@SRC, '"
                + filename + "')]");
    }
}
