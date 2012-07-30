package net.sf.jsptest.compiler.jsp20.mock;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Locale;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Lasse Koskela
 */
public class MockHttpServletResponse implements HttpServletResponse {

	private String characterEncoding;
	private ServletOutputStream servletOutputStream;
	private String description;
	private PrintWriter writer;
	private Locale locale;
	private String contentType;
	private int bufferSize;

	public MockHttpServletResponse() {
		this(new ByteArrayOutputStream());
		description = "(in-memory)";
	}

	public MockHttpServletResponse(File output) throws FileNotFoundException {
		this(new FileOutputStream(output));
		description = "(output file: " + output.getAbsolutePath() + ")";
	}

	private MockHttpServletResponse(final OutputStream output) {
		this.servletOutputStream = new JspTestServletOutputStream(output);
		writer = new PrintWriter(servletOutputStream) {
		};
		this.bufferSize = 0;
		this.locale = Locale.getDefault();
		this.characterEncoding = "ISO-8859-1";
	}

	public String toString() {
		return super.toString() + " " + description;
	}

	public void addCookie(Cookie cookie) {
	}

	public void setDateHeader(String name, long timestamp) {
	}

	public void setHeader(String name, String value) {
	}

	public void setIntHeader(String name, int value) {
	}

	public void addDateHeader(String name, long timestamp) {
	}

	public void addHeader(String name, String value) {
	}

	public void addIntHeader(String name, int value) {
	}

	public boolean containsHeader(String name) {
		return false;
	}

	public String encodeRedirectURL(String url) {
		return url;
	}

	/** @deprecated */
	public String encodeRedirectUrl(String url) {
		return url;
	}

	public String encodeURL(String url) {
		return url;
	}

	/** @deprecated */
	public String encodeUrl(String url) {
		return url;
	}

	public void reset() {
	}

	public void resetBuffer() {
	}

	public void setBufferSize(int size) {
		this.bufferSize = size;
	}

	public int getBufferSize() {
		return bufferSize;
	}

	public void flushBuffer() throws IOException {
		getOutputStream().flush();
	}

	public String getCharacterEncoding() {
		return characterEncoding;
	}

	public void setCharacterEncoding(String reference) {
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public ServletOutputStream getOutputStream() throws IOException {
		return servletOutputStream;
	}

	public PrintWriter getWriter() throws IOException {
		return writer;
	}

	public boolean isCommitted() {
		return false;
	}

	/** @deprecated */
	public void setStatus(int statusCode) {
	}

	public void setStatus(int statusCode, String description) {
	}

	public void sendError(int statusCode) throws IOException {
		throw new RuntimeException("Not implemented");
	}

	public void sendError(int statusCode, String description)
			throws IOException {
		throw new RuntimeException("Not implemented");
	}

	public void sendRedirect(String location) throws IOException {
		throw new RuntimeException("Not implemented");
	}

	public void setContentLength(int contentLength) {
	}

	public void setContentType(String mimeType) {
		this.contentType = mimeType;
	}

	public String getContentType() {
		return contentType;
	}
}
