<%@ tag language="Java" %>
<%@ attribute name="x" required="false" rtexprvalue="true" %>
<%@ attribute name="y" required="false" rtexprvalue="true" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
This content is coming from custom tag file
<c:if test="${not empty x}">x = ${x}</c:if>
<c:if test="${not empty y}">y = ${y}</c:if>
