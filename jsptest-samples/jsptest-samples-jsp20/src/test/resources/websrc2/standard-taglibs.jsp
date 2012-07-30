<%@ page language="Java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core-rt" %>
<c:forEach var="i" begin="1" end="10">
	<c:out value="loop"/>-<c:out value="${i}" />
</c:forEach>