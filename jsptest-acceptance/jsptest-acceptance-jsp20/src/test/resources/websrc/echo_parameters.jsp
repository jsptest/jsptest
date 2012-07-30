<%@ page language="Java" %>
<%@ page import="java.util.*" %>

Request parameters:
<%
    Enumeration requestParameters = request.getParameterNames(); 
    while (requestParameters.hasMoreElements()) {
        String name = (String) requestParameters.nextElement();
        String[] values = request.getParameterValues(name);
        out.print("request parameter: '" + name + "'=");
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                out.print(",");
            }
            out.print("'" + values[i] + "'");
        }
        out.println();
        //String value = String.valueOf(request.getParameter(name));
        //out.println("request parameter: '" + name + "'='" + value + "'");
    }
%>
