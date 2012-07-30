package net.sf.jsptest.compiler.java;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import net.sf.jsptest.utils.IO;

public class Java6Compiler extends CommandLineJavac {
	private static final String TMPDIR = System.getProperty("java.io.tmpdir");

	protected boolean execute(String[] arguments) throws IOException,
			InterruptedException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			return executeWithReflection(arguments, output) == 0;
		} finally {
			File logFile = new File(TMPDIR, getClass().getName() + ".log");
			IO.append(output.toString(), logFile);
		}
	}

	private int executeWithReflection(String[] arguments,
			ByteArrayOutputStream output) {
		try {
			Object systemJavaCompiler = acquireCompilerImplementation();
			Method[] methods = systemJavaCompiler.getClass().getMethods();
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].getName().equals("run")) {
					Object returnValue = methods[i].invoke(systemJavaCompiler,
							new Object[] {
									new ByteArrayInputStream(new byte[0]),
									output, output, arguments });
					return Integer.parseInt(String.valueOf(returnValue));
				}
			}
			return -1;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected String[] buildCommandLine(String pathToJavaSource,
			String outputDirectory, String classpathString) {
		return new String[] { "-verbose", "-classpath", classpathString, "-d",
				outputDirectory, pathToJavaSource };
	}

	public boolean isAvailable() {
		return acquireCompilerImplementation() != null;
	}

	private Object acquireCompilerImplementation() {
		try {
			Class factory = Class.forName("javax.tools.ToolProvider");
			Method method = factory.getMethod("getSystemJavaCompiler", null);
			return method.invoke(null, null);
		} catch (Exception e) {
			return null;
		}
	}
}
