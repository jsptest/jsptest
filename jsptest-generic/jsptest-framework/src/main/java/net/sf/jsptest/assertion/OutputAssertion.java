package net.sf.jsptest.assertion;

/**
 * Provides assertion methods related to raw text output.
 * 
 * @author Lasse Koskela
 */
public class OutputAssertion extends AbstractAssertion {

    private final String content;

    /**
     * @param content
     *            The raw output to perform assertions on.
     */
    public OutputAssertion(String content) {
        this.content = content;
    }

    /**
     * Assert that the output should contain the given text.
     * 
     * @param text
     *            The (partial) content that should be found from the output.
     */
    public void shouldContain(String text) {
        String msg = "Rendered output did not contain the expected text <" + text + ">:\n"
                + content;
        assertContains(msg, content, text);
    }

    /**
     * Assert that the output should not contain the given text.
     * 
     * @param text
     *            The (partial) content that shouldn't be found from the output.
     */
    public void shouldNotContain(String text) {
        String msg = "Rendered output contained unexpected text <" + text + ">:\n" + content;
        assertDoesNotContain(msg, content, text);
    }
}
