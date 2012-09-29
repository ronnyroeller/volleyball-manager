<%@page language="java" pageEncoding="utf8" contentType="text/html; charset=UTF8"%>
<%@page import="java.util.Locale,com.sport.core.helper.Messages,com.sport.core.helper.Licence"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:html locale="true">
<head>
	<title>
		Volleyball Manager
		<logic:present name="tournament">
			 - <bean:write name="tournament" property="name" />
		</logic:present>
		<logic:notEqual name="licenceBO" property="licencetype" value="<%= Licence.LICENCE_PROFI %>">
			- <bean:message key="licence_notregistered"/>
		</logic:notEqual>
	</title>

	<link rel=stylesheet type="text/css" href="<html:rewrite page="/css/admin.css"/>"/>
	<meta name="author" content="(c) 2012 Volleyball Manager">
	<meta name="organisator" content="<bean:write name="licenceBO" property="organisation" />">
	<meta name="description" content="">
	<meta name="keywords" content="">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body style="background-image: url(<html:rewrite page="/img/back.gif"/>)">

	<table cellspacing="0" cellpadding="0" border="0" width="90%" align="center">
		<tr>
			<td align="center" class="mainwerbung" width="100%" height="68" valign="middle" >
				<!-- WERBUNG -->
				<logic:equal name="licenceBO" property="licencetype" value="<%= Licence.LICENCE_PROFI %>">
					<logic:present name="tournament">
						 <bean:write name="tournament" property="bannerLink" filter="false" />
					</logic:present>
				</logic:equal>

				<logic:notEqual name="licenceBO" property="licencetype" value="<%= Licence.LICENCE_PROFI %>">
					<A HREF="http://www.volleyball-manager.com" TARGET="_top"><html:img page="/img/ad4.jpg" border="1" width="475" height="60" alt="Volleyball Manager" /></a>
				</logic:notEqual>
				<!-- / WERBUNG -->
			</td>

			<td align="right" class="mainwerbung" colspan="2">
				<%
				// made Locales visible
				pageContext.setAttribute("german", Messages.GERMAN);
				pageContext.setAttribute("english", Messages.ENGLISH);
				pageContext.setAttribute("spanish", Messages.SPANISH);
				pageContext.setAttribute("dutch", Messages.DUTCH);
			
				if (request.getParameter("nolang") == null) {
				%>
					<html:link href="<%= request.getRequestURL().toString() %>" paramId="locale" paramName="english" >
						<html:img page="/img/english.gif" border="1" width="18" height="12" alt="English" />
					</html:link>
					<html:link href="<%= request.getRequestURL().toString() %>" paramId="locale" paramName="german" >
						<html:img page="/img/deutsch.gif" border="1" width="18" height="12" alt="Deutsch" /><br />
					</html:link>
					<html:link href="<%= request.getRequestURL().toString() %>" paramId="locale" paramName="dutch" >
						<html:img page="/img/dutch.gif" border="1" width="18" height="12" alt="Nederlands" />
					</html:link>
					<html:link href="<%= request.getRequestURL().toString() %>" paramId="locale" paramName="spanish" >
						<html:img page="/img/spanish.gif" border="1" width="18" height="12" alt="Espanol" />
					</html:link>
				<%
				}
				%>
			</td>
		</tr>

		<logic:notEqual name="licenceBO" property="licencetype" value="<%= Licence.LICENCE_PROFI %>">
		<tr class="mainwerbung">
			<td colspan="3">
				<p style="color: #FFDDDD;text-align: center;font-size: 18px;font-weight: bold"><bean:message key="licence_notregistered"/></p>
			</td>
		</tr>
		</logic:notEqual>

		<tr>
			<td style="background-color: #ffffff" colspan="2">
	    		<tiles:insert attribute="body"/>
	    		<tiles:insert attribute="backlink_turnier"/>
			</td>
			<td class="mainwerbung" style="vertical-align: top">
			  &nbsp;
			</td>
		</tr>
	
		<tr>
			<td align="center" class="mainwerbung" colspan="3">
				<html:link forward="kontakt" style="color: #ffffff">(c) 2000-2012 Volleyball Manager</html:link>
			</td>
		</tr>
	</table>

</body>
</html:html>
