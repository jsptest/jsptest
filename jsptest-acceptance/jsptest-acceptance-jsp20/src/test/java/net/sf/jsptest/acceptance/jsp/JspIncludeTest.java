package net.sf.jsptest.acceptance.jsp;

import net.sf.jsptest.JspTestCase;
/**
 * @author Pietro Martinelli
 */
public class JspIncludeTest extends JspTestCase {
	protected String getWebRoot() {
        return "src/test/resources/websrc";
    }
	
	public void testRenderingJspWithJspInclusion() throws Exception {
		get("/include/container.jsp");
		output().shouldContain("Content from container JSP");
		output().shouldContain("Content from included JSP");
	}
}
