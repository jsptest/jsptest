<%@ page language="Java" %>
<%@ taglib uri="http://jsptest.sf.net/taglib" prefix="custom" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<html>
  <body>

    <small>&lt;custom:date tz="GMT"&gt;EEE, dd/MMM/yyyy HH:mm:ss ZZZ&lt;/custom:date&gt; ==&gt;</small>
    <custom:date tz="GMT">EEE, dd/MMM/yyyy HH:mm:ss ZZZ</custom:date>
    <br/>
    
    <small>&lt;custom:date tz="EST"&gt;EEE, dd-MMM-yyyy HH:mm:ss ZZZ&lt;/custom:date&gt; ==&gt;</small>
    <custom:date tz="EST">EEE, dd-MMM-yyyy HH:mm:ss ZZZ</custom:date>
    <br/>

  </body>
</html>
