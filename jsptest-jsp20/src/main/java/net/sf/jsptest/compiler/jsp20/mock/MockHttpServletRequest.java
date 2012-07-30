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
package net.sf.jsptest.compiler.jsp20.mock;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Lasse Koskela
 */
public class MockHttpServletRequest implements HttpServletRequest {

	private static final String LOCALHOST = "127.0.0.1";
	private static final Log log = LogFactory
			.getLog(MockHttpServletRequest.class);
	private Map headers = new HashMap();
	private Map parameters = new HashMap();
	private Map attributes = new HashMap();
	private MockHttpSession httpSession = null;
	private byte[] body;
	private String httpMethod;
	private String characterEncoding;
	private boolean hasAlreadyReadBody;
	private String queryString;
	private String scheme;
	private String contextPath;
	private int serverPort;
	private String serverName;
	private String servletPath;
	private String requestURI;
	static final SimpleDateFormat RFC2616 = new SimpleDateFormat(
			"EEE, dd MMM yyyy HH:mm:ss z");

	public MockHttpServletRequest() {
		this((MockHttpSession) null);
	}

	public MockHttpServletRequest(byte[] body) {
		this((MockHttpSession) null, body);
	}

	public MockHttpServletRequest(MockHttpSession httpSession) {
		this(httpSession, null);
	}

	public MockHttpServletRequest(MockHttpSession httpSession, byte[] body) {
		setSession(httpSession);
		setScheme("http");
		setContextPath("");
		setServletPath("");
		setServerPort(8080);
		setServerName("localhost");
		setCharacterEncoding("ISO-8859-1");
		setMethod("GET");
		setHeader("Content-type",
				"multipart/form-data; boundary=XXXBOUNDARYXXX");
		setBody(body);
	}

	public String getAuthType() {
		return null;
	}

	public Cookie[] getCookies() {
		return new Cookie[0];
	}

	public long getDateHeader(String name) {
		String value = (String) getHeader(name);
		if (value == null) {
			return -1;
		}
		try {
			return RFC2616.parse(value).getTime();
		} catch (ParseException e) {
			log.warn("Could not parse date header '" + name + "': " + value, e);
			throw new IllegalArgumentException("Invalid date: " + value);
		}
	}

	public void setHeader(String name, String value) {
		String headerName = resolveHeaderName(name);
		List values = (List) headers.get(headerName);
		if (values == null) {
			values = new ArrayList();
		}
		values.add(value);
		headers.put(headerName, values);
	}

	public void setHeader(String name, Date value) {
		setHeader(name, RFC2616.format(value));
	}

	public void setHeader(String name, int value) {
		setHeader(name, String.valueOf(value));
	}

	public String getHeader(String name) {
		List values = (List) headers.get(resolveHeaderName(name));
		if (values == null) {
			return null;
		}
		return (String) values.get(0);
	}

	private String resolveHeaderName(String name) {
		Iterator headerNames = headers.keySet().iterator();
		while (headerNames.hasNext()) {
			String headerName = (String) headerNames.next();
			if (headerName.equalsIgnoreCase(name)) {
				return headerName;
			}
		}
		return name;
	}

	public Enumeration getHeaders(String name) {
		Vector vector = new Vector();
		List values = (List) headers.get(resolveHeaderName(name));
		if (values != null) {
			vector.addAll(values);
		}
		return vector.elements();
	}

	public Enumeration getHeaderNames() {
		return new Vector(headers.keySet()).elements();
	}

	public int getIntHeader(String name) {
		String value = getHeader(name);
		if (value == null) {
			return -1;
		}
		return Integer.parseInt(value);
	}

	public void setMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getMethod() {
		return httpMethod;
	}

	public String getPathInfo() {
		return "/";
	}

	public String getPathTranslated() {
		return "/";
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String path) {
		if (requestURI != null && !requestURI.startsWith(path)) {
			throw new IllegalArgumentException("Context path " + path
					+ " does not match the request URI " + requestURI);
		}
		this.contextPath = path;
	}

	public String getQueryString() {
		if (hasQueryString()) {
			return queryString;
		}
		if (isGetRequest() && !parameters.isEmpty()) {
			StringBuffer s = new StringBuffer();
			Iterator parameterNames = parameters.keySet().iterator();
			while (parameterNames.hasNext()) {
				String name = (String) parameterNames.next();
				String[] values = (String[]) parameters.get(name);
				for (int i = 0; i < values.length; i++) {
					if (s.length() > 0) {
						s.append("&");
					}
					s.append(name).append("=").append(values[i]);
				}
			}
			return s.toString();
		}
		return null;
	}

	public void setQueryString(String queryStringWithoutLeadingQuestionMark) {
		this.queryString = queryStringWithoutLeadingQuestionMark;
	}

	public String getRemoteUser() {
		return null;
	}

	public boolean isUserInRole(String role) {
		return false;
	}

	public Principal getUserPrincipal() {
		return null;
	}

	public String getRequestedSessionId() {
		return "JSPTESTSESSIONID";
	}

	public void setRequestURI(String uri) {
		if (contextPath != null && !uri.startsWith(contextPath)) {
			throw new IllegalArgumentException("Request URI " + requestURI
					+ " does not match the Context path " + contextPath);
		}
		this.requestURI = uri;
	}

	public String getRequestURI() {
		if (requestURI != null) {
			return requestURI;
		}
		return getContextPath() + getServletPath();
	}

	public void setRequestURL(String requestUrl) {
		try {
			URL url = new URL(requestUrl);
			setScheme(url.getProtocol());
			setServerName(url.getHost());
			setServerPort(url.getPort());
			setRequestURI(url.getPath());
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public StringBuffer getRequestURL() {
		StringBuffer url = new StringBuffer();
		url.append(getScheme()).append("://");
		url.append(getServerName());
		if (getServerPort() != 80) {
			url.append(":").append(getServerPort());
		}
		url.append(getRequestURI());
		return url;
	}

	public void setServletPath(String path) {
		this.servletPath = path;
	}

	public String getServletPath() {
		return servletPath;
	}

	public HttpSession getSession(boolean createNewSession) {
		if (createNewSession && httpSession == null) {
			httpSession = new MockHttpSession();
		}
		return httpSession;
	}

	public HttpSession getSession() {
		return httpSession;
	}

	public void setSession(MockHttpSession session) {
		this.httpSession = session;
	}

	public boolean isRequestedSessionIdValid() {
		return true;
	}

	public boolean isRequestedSessionIdFromCookie() {
		return true;
	}

	public boolean isRequestedSessionIdFromURL() {
		return isRequestedSessionIdFromUrl();
	}

	public boolean isRequestedSessionIdFromUrl() {
		return false;
	}

	public void setBody(byte[] body) {
		this.body = body;
	}

	public String getCharacterEncoding() {
		return characterEncoding;
	}

	public void setCharacterEncoding(String encoding) {
		characterEncoding = encoding;
	}

	public int getContentLength() {
		return body.length;
	}

	public String getContentType() {
		return "multipart/form-data; boundary=xxx";
	}

	public ServletInputStream getInputStream() throws IOException {
		checkForEarlierAccessToRequestBody();
		return new MockServletInputStream(body);
	}

	public BufferedReader getReader() throws IOException {
		checkForEarlierAccessToRequestBody();
		return new BufferedReader(new InputStreamReader(
				new ByteArrayInputStream(body), characterEncoding));
	}

	private void checkForEarlierAccessToRequestBody() throws IOException {
		if (hasAlreadyReadBody) {
			throw new IOException("You've read the request body already!");
		}
		hasAlreadyReadBody = true;
	}

	public String getProtocol() {
		return "HTTP/1.1";
	}

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public boolean isSecure() {
		return getProtocol().equals("https");
	}

	public void setServerName(String name) {
		this.serverName = name;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerPort(int port) {
		this.serverPort = port;
	}

	public int getServerPort() {
		return serverPort;
	}

	public String getRemoteAddr() {
		return LOCALHOST;
	}

	public String getRemoteHost() {
		return "localhost";
	}

	public int getRemotePort() {
		return 11111;
	}

	public int getLocalPort() {
		return 22222;
	}

	public String getLocalName() {
		return LOCALHOST;
	}

	public String getLocalAddr() {
		return LOCALHOST;
	}

	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	public Enumeration getAttributeNames() {
		return new Vector(attributes.keySet()).elements();
	}

	public void setAttributes(Map attributes) {
		this.attributes.putAll(attributes);
	}

	public void setAttribute(String name, Object value) {
		attributes.put(name, value);
	}

	public void removeAttribute(String name) {
		attributes.remove(name);
	}

	public Enumeration getParameterNames() {
		return new Vector(getParameterMap().keySet()).elements();
	}

	public Map getParameterMap() {
		if (isGetRequest() && hasQueryString()) {
			return getQueryStringParameterMap();
		}
		if (isPostRequest() && hasQueryString()) {
			return join(parameters, getQueryStringParameterMap());
		}
		return new HashMap(parameters);
	}

	public String[] getParameterValues(String name) {
		return (String[]) getParameterMap().get(name);
	}

	public String getParameter(String name) {
		String values[] = (String[]) getParameterMap().get(name);
		if (values != null) {
			return values[0];
		}
		return null;
	}

	public void addParameter(String name, String[] values) {
		String[] oldValues = (String[]) parameters.get(name);
		if (oldValues != null) {
			values = join(oldValues, values);
		}
		parameters.put(name, values);
	}

	public void addParameter(String name, String value) {
		addParameter(name, new String[] { value });
	}

	public void setParameters(Map requestParameters) {
		Iterator parameterNames = requestParameters.keySet().iterator();
		while (parameterNames.hasNext()) {
			String name = (String) parameterNames.next();
			String[] values = (String[]) requestParameters.get(name);
			parameters.put(name, values);
		}
	}

	public void setParameter(String name, String value) {
		setParameter(name, new String[] { value });
	}

	public void setParameter(String name, String[] values) {
		parameters.put(name, values);
	}

	public Locale getLocale() {
		return Locale.getDefault();
	}

	public Enumeration getLocales() {
		List locales = Arrays.asList(Locale.getAvailableLocales());
		return new Vector(locales).elements();
	}

	public RequestDispatcher getRequestDispatcher(String path) {
		return new RequestDispatcher() {

			public void forward(ServletRequest request, ServletResponse response)
					throws ServletException, IOException {
			}

			public void include(ServletRequest request, ServletResponse response)
					throws ServletException, IOException {
			}
		};
	}

	public String getRealPath(String path) {
		File tmpDir = new File(System.getProperty("java.io.tmpdir"));
		File jsptestTmpDir = new File(tmpDir, "jsptest");
		return new File(jsptestTmpDir, path).getAbsolutePath();
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(getClass().getName()).append("{");
		Enumeration enumeration = getParameterNames();
		while (enumeration.hasMoreElements()) {
			String key = (String) enumeration.nextElement();
			String[] values = getParameterValues(key);
			buffer.append("parameter:");
			buffer.append(key);
			buffer.append("=[");
			for (int i = 0; i < values.length; i++) {
				buffer.append(values[i]);
				if (i < values.length - 1) {
					buffer.append(",");
				}
			}
			buffer.append("]\n");
		}
		buffer.append("}");
		return buffer.toString();
	}

	private String[] join(String[] array, String[] anotherArray) {
		List joined = new ArrayList();
		if (array != null) {
			joined.addAll(Arrays.asList(array));
		}
		if (anotherArray != null) {
			joined.addAll(Arrays.asList(anotherArray));
		}
		return (String[]) joined.toArray(new String[joined.size()]);
	}

	private Map join(Map map, Map anotherMap) {
		Map joined = new HashMap();
		Collection keys = join(map.keySet(), anotherMap.keySet());
		Iterator allKeys = keys.iterator();
		while (allKeys.hasNext()) {
			String key = (String) allKeys.next();
			String[] values1 = (String[]) map.get(key);
			String[] values2 = (String[]) anotherMap.get(key);
			joined.put(key, join(values1, values2));
		}
		return joined;
	}

	private Collection join(Collection collection, Collection anotherCollection) {
		Collection joined = new ArrayList();
		if (collection != null) {
			joined.addAll(collection);
		}
		if (anotherCollection != null) {
			joined.addAll(anotherCollection);
		}
		return joined;
	}

	private Map getQueryStringParameterMap() {
		Map parameters = new HashMap();
		String[] pairs = queryString.split("&");
		for (int i = 0; i < pairs.length; i++) {
			String[] nameAndValue = pairs[i].split("=", 2);
			String name = nameAndValue[0];
			String value = nameAndValue[1];
			String[] existingValues = (String[]) parameters.get(name);
			String[] newValues = join(existingValues, new String[] { value });
			parameters.put(name, newValues);
		}
		return parameters;
	}

	private boolean hasQueryString() {
		return queryString != null;
	}

	private boolean isGetRequest() {
		return "GET".equals(getMethod());
	}

	private boolean isPostRequest() {
		return "POST".equals(getMethod());
	}
}
