<%@page import="com.sport.core.helper.Licence" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:html>
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

	<link rel=stylesheet type="text/css" href="<html:rewrite page="/css/beamer.css"/>"/>
	<meta name="author" content="(c) 2004 Volleyball Manager">
	<meta name="organisator" content="<bean:write name="licenceBO" property="organisation" />">
	<meta name="description" content="">
	<meta name="keywords" content="">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<tiles:insert attribute="meta"/>
</head>
<body>

	<table height="100%" width="100%">
		<logic:notEqual name="licenceBO" property="licencetype" value="<%= Licence.LICENCE_PROFI %>">
		<tr height="20">
			<td height="20">
				<h1 style="color: #FF0000;text-align: center;font-size: 18px;font-weight: bold"><bean:message key="licence_notregistered"/></h1>
			</td>
		</tr>
		</logic:notEqual>
		<tr height="95%">
			<td valign="top">
				<tiles:insert attribute="body"/>
			</td>
		</tr>
		
		<tr height="20">
			<td valign="bottom">

				<!-- WERBUNG -->
				<logic:equal name="licenceBO" property="licencetype" value="<%= Licence.LICENCE_PROFI %>">
					<logic:present name="tournament">
						 <bean:write name="tournament" property="bannerLink" filter="false" />
					</logic:present>
				</logic:equal>

				<logic:notEqual name="licenceBO" property="licencetype" value="<%= Licence.LICENCE_PROFI %>">
					<p class="werbung">http://www.volleyball-manager.com</p>
				</logic:notEqual>
				<!-- / WERBUNG -->
			
			</td>
		</tr>
	</table>

</body>
</html:html>
