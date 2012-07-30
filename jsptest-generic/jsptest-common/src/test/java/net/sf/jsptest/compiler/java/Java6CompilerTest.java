package net.sf.jsptest.compiler.java;

import junit.framework.TestCase;

public class Java6CompilerTest extends TestCase {
	public void testIsAvailableWhenRunningUnderJava6() throws Exception {
		assertEquals(isRunningWithJava6(), new Java6Compiler().isAvailable());
	}

	private boolean isRunningWithJava6() {
		String versionString = System.getProperty("java.specification.version");
		return Float.parseFloat(versionString) >= 1.6f;
	}
}
