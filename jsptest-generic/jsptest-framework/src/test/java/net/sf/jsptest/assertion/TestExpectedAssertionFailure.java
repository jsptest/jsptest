package net.sf.jsptest.assertion;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import net.sf.jsptest.HtmlTestCase;

/**
 * @author Lasse Koskela
 */
public class TestExpectedAssertionFailure extends TestCase {

    private HtmlTestCase testcase;

    protected void setUp() throws Exception {
        super.setUp();
        this.testcase = new HtmlTestCase() {
        };
    }

    public void testConstructorPassesWhenAssertionFailedErrorIsThrown() throws Exception {
        new ExpectedAssertionFailure(testcase) {

            protected void run() throws Exception {
                throw new AssertionFailedError();
            }
        };
    }

    public void testConstructorPassesWhenSubclassOfAssertionFailedErrorIsThrown() throws Exception {
        new ExpectedAssertionFailure(testcase) {

            protected void run() throws Exception {
                throw new AssertionFailedError() {
                };
            }
        };
    }

    public void testConstructorFailsWhenSomeOtherExceptionIsThrown() throws Exception {
        try {
            new ExpectedAssertionFailure(testcase) {

                protected void run() throws Exception {
                    throw new Exception();
                }
            };
            throw new RuntimeException("Constructor should've failed.");
        } catch (ExpectedAssertionFailure.IncorrectExceptionError expected) {
            // things went according to the plan
        }
    }

    public void testConstructorFailsWhenNoExceptionIsThrown() throws Exception {
        try {
            new ExpectedAssertionFailure(testcase) {

                protected void run() throws Exception {
                }
            };
            throw new RuntimeException("Constructor should've failed.");
        } catch (AssertionFailedError expected) {
            // things went according to the plan
        }
    }
}
