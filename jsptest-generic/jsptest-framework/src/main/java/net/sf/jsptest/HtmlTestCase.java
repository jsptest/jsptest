/*
 * Copyright 2007 Lasse Koskela.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.jsptest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import net.sf.jsptest.assertion.ElementAssertion;
import net.sf.jsptest.assertion.ElementChooser;
import net.sf.jsptest.assertion.FormAssertion;
import net.sf.jsptest.assertion.NameChooser;
import net.sf.jsptest.assertion.PageAssertion;
import net.sf.jsptest.utils.IO;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.tidy.Tidy;

/**
 * The <tt>HtmlTestCase</tt> class provides a facility for rendering a JSP and a set of assertion
 * methods for verifying that the rendered HTML matches our expectations.
 * 
 * @author Lasse Koskela
 */
public abstract class HtmlTestCase extends JspTestCase {

    private Document renderedDocument;
    private String renderedDocumentPath;
    private Tidy tidy;
    private DocumentBuilder documentBuilder;

    /**
     * Simulate an HTTP request to a JSP, parsing the rendered output as HTML.
     * 
     * @param path
     *            The path to the JSP to execute.
     * @param httpMethod
     *            "GET" or "POST".
     */
    protected void request(String path, String httpMethod) throws Exception {
        super.request(path, httpMethod);
        parseRenderedHtml();
    }

    /**
     * Returns the rendered HTML as an <tt>org.w3c.dom.Document</tt>.
     */
    protected Document getRenderedHtml() {
        return renderedDocument;
    }

    /**
     * Returns the path to the rendered (and pretty-printed) HTML document.
     */
    protected String getRenderedHtmlPath() {
        return renderedDocumentPath;
    }

    private void parseRenderedHtml() {
        try {
            File original = File.createTempFile("renderedHtml", ".html");
            IO.write(getRenderedResponse(), original);
            File tidyHtml = new File(original.getAbsolutePath() + ".tidy.html");
            parseRenderedOutput(original, tidyHtml);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void parseRenderedOutput(File html, File prettyPrinted) {
        try {
            configureTidy().parse(new FileInputStream(html), new FileOutputStream(prettyPrinted));
            DocumentBuilder db = configureDocumentBuilder();
            renderedDocument = db.parse(prettyPrinted);
            renderedDocumentPath = prettyPrinted.getAbsolutePath();
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage() + " (" + html.getAbsolutePath() + ")", e);
        }
    }

    private synchronized DocumentBuilder configureDocumentBuilder()
            throws ParserConfigurationException {
        if (documentBuilder == null) {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            documentBuilder = dbf.newDocumentBuilder();
        }
        return documentBuilder;
    }

    private synchronized Tidy configureTidy() {
        if (tidy == null) {
            tidy = new Tidy();
            tidy.setWraplen(Integer.MAX_VALUE);
            tidy.setMakeClean(true);
            tidy.setXmlOut(true);
            tidy.setTidyMark(false);
            tidy.setQuiet(true);
            tidy.setShowWarnings(false);
            tidy.setUpperCaseTags(true);
            tidy.setUpperCaseAttrs(true);
        }
        return tidy;
    }

    /**
     * Returns a handle for making assertions about the specified HTML form.
     */
    public FormAssertion form(String name) {
        return new FormAssertion(getRenderedHtml(), new NameChooser(name));
    }

    /**
     * Returns a handle for making assertions about an HTML form.
     */
    public FormAssertion form() {
        return new FormAssertion(getRenderedHtml(), new ElementChooser() {

            public boolean accept(Element element) {
                return true;
            }
        });
    }

    /**
     * Returns a handle for making assertions about the rendered HTML page.
     */
    public PageAssertion page() {
        return new PageAssertion(getRenderedHtml());
    }

    /**
     * Returns a handle for making assertions about the specified HTML element.
     */
    public ElementAssertion element(String xpathExpression) {
        return new ElementAssertion(getRenderedHtml(), xpathExpression);
    }
}
