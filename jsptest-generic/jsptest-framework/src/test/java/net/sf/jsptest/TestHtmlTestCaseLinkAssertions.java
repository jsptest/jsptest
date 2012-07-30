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

import net.sf.jsptest.assertion.ExpectedAssertionFailure;

/**
 * @author Lasse Koskela
 */
public class TestHtmlTestCaseLinkAssertions extends AbstractHtmlTestCaseTestCase {

    private static final String LINK_HREF = "http://target.com/page.html";
    private static final String LINK_ID = "linkId";
    private static final String LINK_NAME = "linkName";
    private static final String LINK_TEXT = "linkText";
    private static final String LINK_CLASS = ".linkClass";
    private static final String IMAGE_ID = "imageId";
    private static final String IMAGE_NAME = "imageName";
    private static final String IMAGE_TITLE = "imageTitle";
    private static final String IMAGE_FILENAME = "filename.jpg";
    private static final String IMAGE_SRC = "./images/" + IMAGE_FILENAME;

    protected void appendOutput(StringBuffer h) {
        h.append("<html><head>");
        h.append("<title>PageTitle</title>");
        h.append("</head><body>");
        h.append("<a href='" + LINK_HREF + "'>Link with href</a>");
        h.append("<a id='" + LINK_ID + "'>Link with ID</a>");
        h.append("<a name='" + LINK_NAME + "'>Link with name</a>");
        h.append("<a class='" + LINK_CLASS + "'>Link with class</a>");
        h.append("<a href='foo'>" + LINK_TEXT + "</a>");
        h.append("<a name='foo'><img id='" + IMAGE_ID + "' name='" + IMAGE_NAME + "' src='"
                + IMAGE_SRC + "' title='" + IMAGE_TITLE + "'/></a>");
        h.append("</body></html>");
    }

    public void testShouldHaveLinkWithText() throws Exception {
        testcase.page().shouldHaveLink().withText(LINK_TEXT);
        new ExpectedAssertionFailure(testcase) {

            public void run() {
                page().shouldHaveLink().withText("Foo");
            }
        };
    }

    public void testShouldHaveLinkWithPartialText() throws Exception {
        testcase.page().shouldHaveLink().withPartialText(LINK_TEXT.substring(1, 3));
        new ExpectedAssertionFailure(testcase) {

            public void run() {
                page().shouldHaveLink().withPartialText("Foo");
            }
        };
    }

    public void testShouldHaveLinkWithName() throws Exception {
        testcase.page().shouldHaveLink().withName(LINK_NAME);
        new ExpectedAssertionFailure(testcase) {

            public void run() {
                page().shouldHaveLink().withName("Foo");
            }
        };
    }

    public void testShouldHaveLinkWithId() throws Exception {
        testcase.page().shouldHaveLink().withId(LINK_ID);
        new ExpectedAssertionFailure(testcase) {

            public void run() {
                page().shouldHaveLink().withId("Foo");
            }
        };
    }

    public void testShouldHaveLinkWithHref() throws Exception {
        testcase.page().shouldHaveLink().withHref(LINK_HREF);
        new ExpectedAssertionFailure(testcase) {

            public void run() {
                page().shouldHaveLink().withHref("Foo");
            }
        };
    }

    public void testShouldHaveLinkWithClass() throws Exception {
        testcase.page().shouldHaveLink().withClass(LINK_CLASS);
        new ExpectedAssertionFailure(testcase) {

            public void run() {
                page().shouldHaveLink().withClass("Foo");
            }
        };
    }

    public void testShouldHaveLinkWithImageId() throws Exception {
        testcase.page().shouldHaveLink().withImageId(IMAGE_ID);
        new ExpectedAssertionFailure(testcase) {

            public void run() {
                page().shouldHaveLink().withImageId("Foo");
            }
        };
    }

    public void testShouldHaveLinkWithImageName() throws Exception {
        testcase.page().shouldHaveLink().withImageName(IMAGE_NAME);
        new ExpectedAssertionFailure(testcase) {

            public void run() {
                page().shouldHaveLink().withImageName("Foo");
            }
        };
    }

    public void testShouldHaveLinkWithImageSrc() throws Exception {
        testcase.page().shouldHaveLink().withImageSrc(IMAGE_SRC);
        new ExpectedAssertionFailure(testcase) {

            public void run() {
                page().shouldHaveLink().withImageSrc("Foo");
            }
        };
    }

    public void testShouldHaveLinkWithImageTitle() throws Exception {
        testcase.page().shouldHaveLink().withImageTitle(IMAGE_TITLE);
        new ExpectedAssertionFailure(testcase) {

            public void run() {
                page().shouldHaveLink().withImageTitle("Foo");
            }
        };
    }

    public void testShouldHaveLinkWithImageFilename() throws Exception {
        testcase.page().shouldHaveLink().withImageFileName(IMAGE_FILENAME);
        new ExpectedAssertionFailure(testcase) {

            public void run() {
                page().shouldHaveLink().withImageFileName("filename.gif");
            }
        };
    }
}
