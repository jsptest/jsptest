/*
 * Copyright 2008 Lasse Koskela.
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
package net.sf.jsptest.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

/**
 * The <tt>IO</tt> class provides utility methods for handling I/O related tasks
 * such as reading from and writing to files, streams, and writers.
 * 
 * @author Lasse Koskela
 */
public class IO {

	public static byte[] readToByteArray(File file) throws IOException {
		return readToByteArray(new FileInputStream(file));
	}

	public static byte[] readToByteArray(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[8096];
		int r = -1;
		while ((r = in.read(buffer)) != -1) {
			out.write(buffer, 0, r);
		}
		in.close();
		return out.toByteArray();
	}

	public static void write(String what, File to) throws IOException {
		write(what, new FileWriter(to, false));
	}

	public static void append(String what, File to) throws IOException {
		write(what, new FileWriter(to, true));
	}

	private static void write(String what, FileWriter to) throws IOException {
		to.write(what);
		to.close();
	}

	public static String readToString(File file) throws IOException {
		return new String(readToByteArray(file));
	}

	public static String readToString(InputStream stream) throws IOException {
		return new String(readToByteArray(stream));
	}
}
