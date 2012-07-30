package net.sf.jsptest.assertion;

import net.sf.jsptest.HtmlTestCase;
import junit.framework.AssertionFailedError;

/**
 * @author Lasse Koskela
 */
public abstract class ExpectedAssertionFailure {

    private HtmlTestCase testcase;

    /**
     * Override this method to perform a test that should fail with an exception.
     */
    protected abstract void run() throws Exception;

    /**
     * @param testcase
     *            The <tt>HtmlTestCase</tt> that serves as the context for this operation.
     */
    public ExpectedAssertionFailure(HtmlTestCase testcase) throws Exception {
        this(testcase, "Operation should've failed but it didn't.");
    }

    /**
     * @param testcase
     *            The <tt>HtmlTestCase</tt> that serves as the context for this operation.
     * @param message
     *            The message to display if the specified operation doesn't throw an
     *            AssertionFailedError.
     */
    public ExpectedAssertionFailure(HtmlTestCase testcase, String message) throws Exception {
        this.testcase = testcase;
        verify(message);
    }

    /**
     * Gives access to a <tt>PageAssertion</tt> object, enabling page-oriented (HTML) assertions.
     */
    protected PageAssertion page() {
        return testcase.page();
    }

    /**
     * Gives access to an <tt>OutputAssertion</tt> object, enabling raw output-oriented
     * assertions.
     */
    protected OutputAssertion output() {
        return testcase.output();
    }

    /**
     * Gives access to an <tt>ElementAssertion</tt> object for the HTML element identified by the
     * given XPath expression.
     * 
     * @param xpath
     * @return
     */
    protected ElementAssertion element(String xpath) {
        return testcase.element(xpath);
    }

    private void verify(String message) {
        try {
            run();
            throw new NoExceptionWasThrown();
        } catch (AssertionFailedError expected) {
            // everything went according to the plan!
        } catch (NoExceptionWasThrown e) {
            throw new AssertionFailedError(message);
        } catch (Throwable e) {
            throw new IncorrectExceptionError("A non-assertion exception was thrown: "
                    + e.getClass().getName(), e);
        }
    }

    /**
     * Thrown from an assertion method indicating that the wrong kind of exception was thrown by the
     * code under test.
     * 
     * @author Lasse Koskela
     */
    public static class IncorrectExceptionError extends RuntimeException {

        public IncorrectExceptionError(String message, Throwable e) {
            super(message, e);
        }
    }

    /**
     * Thrown from an assertion method indicating that no exception was thrown by the code under
     * test against the expectations. This class is only used internally and is never passed to
     * client code (test written by a JspTest user).
     * 
     * @author Lasse Koskela
     */
    private static class NoExceptionWasThrown extends Exception {
    }
}
