package net.sf.jsptest.compiler.jsp20.mock;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import junit.framework.TestCase;

public class TestMockHttpServletRequest extends TestCase {

	private MockHttpServletRequest get;
	private MockHttpServletRequest post;
	private byte[] body;

	protected void setUp() throws Exception {
		body = "body".getBytes();
		get = new MockHttpServletRequest();
		post = new MockHttpServletRequest(body);
		post.setMethod("POST");
	}

	public void testCharacterEncodingCanBeConfigured() throws Exception {
		assertEquals("ISO-8859-1", get.getCharacterEncoding());
		get.setCharacterEncoding("UTF-8");
		assertEquals("UTF-8", get.getCharacterEncoding());
	}

	public void testReadingRequestBodyAsInputStream() throws Exception {
		assertEquals(new String(body), asString(post.getInputStream()));
	}

	public void testReadingRequestBodyThroughBufferedReader() throws Exception {
		assertEquals(new String(body), asString(post.getReader()));
	}

	public void testCannotReadBodyTwiceThroughInputStream() throws Exception {
		post.getInputStream();
		try {
			post.getInputStream();
			fail("shouldn't let us access the InputStream twice!");
		} catch (IOException expected) {
		}
	}

	public void testCannotReadBodyTwiceThroughReader() throws Exception {
		post.getReader();
		try {
			post.getReader();
			fail("shouldn't let us access the BufferedReader twice!");
		} catch (IOException expected) {
		}
	}

	public void testCannotReadBodyTwiceWithDifferentMethods() throws Exception {
		try {
			post.getReader();
			post.getInputStream();
			fail("shouldn't let us access the BufferedReader AND the InputStream!");
		} catch (IOException expected) {
		}
		try {
			post.getInputStream();
			post.getReader();
			fail("shouldn't let us access the InputStreamAND the BufferedReader !");
		} catch (IOException expected) {
		}
	}

	// TODO: test handling body and its length

	public void testSettingIndividualHeader() throws Exception {
		get.setHeader("X-Vibe", "Cool");
		assertEquals("Cool", get.getHeader("X-Vibe"));
		assertEquals("Cool", get.getHeader("x-VibE"));
		assertEquals(asList(new String[] { "Cool" }),
				collect(get.getHeaders("x-vibe")));
		assertTrue(collect(get.getHeaderNames()).contains("X-Vibe"));
		assertFalse(collect(get.getHeaderNames()).contains("x-ViBe"));
	}

	public void testSettingMultipleValuesForHeader() throws Exception {
		get.setHeader("X-VIBE", "Uppercase");
		get.setHeader("X-VIBE", "Another Uppercase");
		get.setHeader("x-vibe", "Lowercase");
		assertEquals("Uppercase", get.getHeader("X-vIbE"));
		assertEquals(asList(new String[] { "Uppercase", "Another Uppercase",
				"Lowercase" }), collect(get.getHeaders("x-VibE")));
		assertTrue(collect(get.getHeaderNames()).contains("X-VIBE"));
		assertFalse(collect(get.getHeaderNames()).contains("x-vibe"));
	}

	public void testDateHeader() throws Exception {
		Date date = MockHttpServletRequest.RFC2616
				.parse("Fri, 04 May 2012 06:41:23 GMT");
		get.setHeader("X-Date1", date);
		assertEquals(date.getTime(), get.getDateHeader("X-Date1"));
		get.setHeader("X-Date2", MockHttpServletRequest.RFC2616.format(date));
		assertEquals(date.getTime(), get.getDateHeader("X-Date2"));
	}

	public void testIntHeader() throws Exception {
		get.setHeader("X-NotANumber", "NotANumber");
		get.setHeader("X-Number", 123);
		get.setHeader("X-Number", "456");
		assertEquals(123, get.getIntHeader("X-Number"));
		assertEquals(-1, get.getIntHeader("X-No-Such-Header"));
		try {
			get.getIntHeader("X-NotANumber");
			fail("Using getIntHeader() on NaN header value should yield an exception");
		} catch (NumberFormatException expected) {
		}
	}

	public void testAttributes() throws Exception {
		post.setAttribute("name", "value");
		post.setAttribute("over", "whatever");
		post.setAttributes(new HashMap() {
			{
				put("over", "written");
			}
		});
		assertEquals("value", post.getAttribute("name"));
		assertEquals("written", post.getAttribute("over"));
		assertTrue(collect(post.getAttributeNames()).contains("name"));
		assertTrue(collect(post.getAttributeNames()).contains("over"));
	}

	public void testAddingValueForSameParameterMultipleTimes() {
		post.addParameter("name", "value1");
		post.addParameter("name", "value2");
		assertTrue(collect(post.getParameterNames()).contains("name"));
		List parameterValues = asList(post.getParameterValues("name"));
		assertTrue(parameterValues.contains("value1"));
		assertTrue(parameterValues.contains("value2"));
	}

	public void testSettingParametersOverwritesExistingValues()
			throws Exception {
		post.addParameter("name", "original");
		post.setParameters(new HashMap() {
			{
				put("name", new String[] { "new value" });
			}
		});
		assertEquals("new value", post.getParameter("name"));
		assertFalse(asList(post.getParameterValues("name"))
				.contains("original"));
	}

	public void testGetQueryStringReturnsNullWhenNoQueryParametersWereSet()
			throws Exception {
		assertNull(get.getQueryString());
		assertNull(post.getQueryString());
	}

	public void testQueryStringShouldBeTheOneThatWasSet() throws Exception {
		get.setQueryString("foo=bar&xyz=123");
		assertEquals("foo=bar&xyz=123", get.getQueryString());
	}

	public void testQueryStringShouldMatchParametersAddedToGetRequest()
			throws Exception {
		get.addParameter("xyz", new String[] { "456", "123" });
		get.addParameter("foo", "bar");
		assertEquals("foo=bar&xyz=456&xyz=123", get.getQueryString());
	}

	public void testParametersShouldMatchTheQueryStringSetToGetRequest()
			throws Exception {
		get.setQueryString("foo=bar&xyz=456&xyz=123");
		assertEquals("bar", get.getParameter("foo"));
		assertEquals(asList(new String[] { "456", "123" }),
				asList(get.getParameterValues("xyz")));
		assertEquals(asList(new String[] { "foo", "xyz" }),
				collect(get.getParameterNames()));
	}

	public void testPostRequestCouldHaveBothQueryStringParametersAndBodyParameters()
			throws Exception {
		post.setQueryString("param=querystring");
		post.addParameter("param", "added");
		assertEquals(asList(new String[] { "added", "querystring" }),
				asList(post.getParameterValues("param")));
	}

	public void testGetRealPathPointsUnderSystemTempDir() throws Exception {
		String realPath = get.getRealPath("/admin/index.jsp");
		assertTrue(realPath.endsWith("/admin/index.jsp"));
		assertTrue(realPath.startsWith(System.getProperty("java.io.tmpdir")));
	}

	public void testGetRequestURLIsConsistentWithSpecificQueryMethods()
			throws Exception {
		get.setScheme("https");
		get.setContextPath("/contextpath");
		get.setServerPort(1234);
		get.setServerName("servername");
		get.setServletPath("/servletpath");
		get.setQueryString("query=string");

		assertEquals("/contextpath/servletpath", get.getRequestURI());
		assertEquals("https://servername:1234/contextpath/servletpath", get
				.getRequestURL().toString());
	}

	public void testRequestURICanBeSet() throws Exception {
		get.setRequestURI("/path/to/page.jsp");
		assertEquals("/path/to/page.jsp", get.getRequestURI());
		assertEquals("", get.getContextPath());
		assertEquals("", get.getServletPath());
	}

	public void testSettingRequestURIContextPathAndServletPaths()
			throws Exception {
		get.setRequestURI("/path/to/page.jsp");
		get.setContextPath("/path");
		get.setServletPath("/to");
		assertEquals("/path/to/page.jsp", get.getRequestURI());
		assertEquals("/path", get.getContextPath());
		assertEquals("/to", get.getServletPath());
	}

	public void testRequestURIMustMatchPreviouslySetContextPath()
			throws Exception {
		post.setContextPath("/context");
		try {
			post.setRequestURI("/differentContext/page.jsp");
			fail("Request URI must match the context path that was set.");
		} catch (IllegalArgumentException expected) {
		}
	}

	public void testContextPathMustMatchPreviouslySetRequestURI()
			throws Exception {
		get.setRequestURI("/path/to/page.jsp");
		try {
			get.setContextPath("/differentPath");
			fail("Context path must match the request URI that was set.");
		} catch (IllegalArgumentException expected) {
		}
	}

	public void testRequestURLCanBeSet() throws Exception {
		get.setRequestURL("http://server:99/path/to/page.jsp");
		assertEquals("http://server:99/path/to/page.jsp", get.getRequestURL()
				.toString());
		assertEquals("", get.getContextPath());
		assertEquals("", get.getServletPath());
	}

	public void testSettingSpecificPartsAfterSettingTheFullRequestURL()
			throws Exception {
		get.setRequestURL("http://whatever/context/path");
		get.setServerName("something.el.se");
		get.setServerPort(8000);
		get.setContextPath("/context");
		assertEquals("http://something.el.se:8000/context/path", get
				.getRequestURL().toString());
		assertEquals("something.el.se", get.getServerName());
		assertEquals(8000, get.getServerPort());
		assertEquals("/context", get.getContextPath());
	}

	private String asString(InputStream stream)
			throws UnsupportedEncodingException {
		return asString(stream, "UTF-8");
	}

	private String asString(InputStream stream, String charset)
			throws UnsupportedEncodingException {
		return new String(contentOf(stream, charset), charset);
	}

	private String asString(BufferedReader reader)
			throws UnsupportedEncodingException {
		return asString(reader, "UTF-8");
	}

	private String asString(BufferedReader reader, String charset)
			throws UnsupportedEncodingException {
		return new String(contentOf(reader), charset);
	}

	private byte[] contentOf(InputStream stream, String charset) {
		try {
			return contentOf(new InputStreamReader(stream, charset));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	private byte[] contentOf(Reader reader) {
		try {
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int b = -1;
			while ((b = reader.read()) != -1) {
				buffer.write(b);
			}
			return buffer.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private Collection collect(Enumeration enumeration) {
		Collection list = new ArrayList();
		while (enumeration.hasMoreElements()) {
			list.add(enumeration.nextElement());
		}
		return list;
	}

	private List asList(String string) {
		return asList(new String[] { string });
	}

	private List asList(String[] array) {
		return Arrays.asList(array);
	}
}
