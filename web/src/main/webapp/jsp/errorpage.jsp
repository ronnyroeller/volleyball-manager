<%@ page isErrorPage="true" contentType="text/html" %>
<%@ page import="org.apache.struts.action.Action" %> 
<%@ page import="org.apache.struts.action.ActionErrors" %>
<%@ page language="java" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.lang.*" %>

<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<body>

<h1>Received error <%= exception %></h1>

<html:messages id="error" message="false">
<bean:write name="error" />
</html:messages>

<%
exception = (Throwable)request.getAttribute("javax.servlet.error.exception"); 
%>
<h1> Exception: <%= exception %></h1>

</body>
</html>
