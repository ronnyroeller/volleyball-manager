<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@page import="com.sport.analyzer.MannschaftAnalyzer"%>
<%@page import="com.sport.analyzer.impl.MannschaftAnalyzerImpl"%>

<table cellspacing="0" cellpadding="3" border="0" width="100%">
	<tr class="header">
		<td>
			<html:img page="/jsp/point.jsp?" paramId="color" paramName="gruppe" paramProperty="color" height="10" width="10" border="1" />
			<bean:write name="gruppe" property="name" /> (<bean:write name="gruppe" property="tournamentSystem.name" />)
		</td>
	</tr>

	<tr>
		<td class="databody">
			<table class="tabelle" cellpadding="3" cellspacing="3">
				<tr class="dataheader">
					<td><bean:message key="jsp_gruppe_platz"/></td>
					<td><bean:message key="jsp_gruppe_team"/></td>
					<td><bean:message key="jsp_gruppe_ga"/></td>
					<logic:notEqual name="gruppe" property="tournamentSystem.koSystem" value="true">
						<td><bean:message key="jsp_gruppe_pga"/></td>
						<td><bean:message key="jsp_gruppe_nga"/></td>
						<td><bean:message key="jsp_gruppe_pset"/></td>
						<td><bean:message key="jsp_gruppe_nset"/></td>
						<td><bean:message key="jsp_gruppe_ppo"/></td>
						<td><bean:message key="jsp_gruppe_npo"/></td>
						<td><bean:message key="jsp_gruppe_diffpo"/></td>
					</logic:notEqual>
				</tr>

				<logic:iterate indexId="platznr" id="gruppeErgebnisEntity" name="groupResult" property="ergebnisDetails" type="com.sport.analyzer.GruppeErgebnisEntity">
				<tr class="databody">
					<td>
						<%= platznr.intValue ()+1 %>
					</td>
					<td>
						<%
						MannschaftAnalyzer mannschaftAnalyzer = MannschaftAnalyzerImpl.getInstance();
						String mannschaftRelName = mannschaftAnalyzer.getRelName(gruppeErgebnisEntity.getMannschaftBO());
						%>
						<%= mannschaftRelName %>
					</td>
					<td><bean:write name="gruppeErgebnisEntity" property="spiele" /></td>
					<logic:notEqual name="gruppe" property="tournamentSystem.koSystem" value="true">
						<td><bean:write name="gruppeErgebnisEntity" property="pspiele" /></td>
						<td><bean:write name="gruppeErgebnisEntity" property="nspiele" /></td>
						<td><bean:write name="gruppeErgebnisEntity" property="psaetze" /></td>
						<td><bean:write name="gruppeErgebnisEntity" property="nsaetze" /></td>
						<td><bean:write name="gruppeErgebnisEntity" property="ppunkte" /></td>
						<td><bean:write name="gruppeErgebnisEntity" property="npunkte" /></td>
						<td><bean:write name="gruppeErgebnisEntity" property="diffpunkte" /></td>
					</logic:notEqual>
				</tr>
				</logic:iterate>
			</table>
		</td>
	</tr>
</table>