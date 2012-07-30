<%@ page language="Java" %>
<%@ page import="java.util.*" %>

Request attributes:
<%
    Enumeration requestAttributes = request.getAttributeNames(); 
    while (requestAttributes.hasMoreElements()) {
        String name = (String) requestAttributes.nextElement();
        String value = String.valueOf(request.getAttribute(name));
        out.println("request attribute: '" + name + "'='" + value + "'");
    }
%>

Session attributes:
<%
    Enumeration sessionAttributes = session.getAttributeNames(); 
    while (sessionAttributes.hasMoreElements()) {
        String name = (String) sessionAttributes.nextElement();
        String value = String.valueOf(session.getAttribute(name));
        out.println("session attribute: '" + name + "'='" + value + "'");
    }
%>
