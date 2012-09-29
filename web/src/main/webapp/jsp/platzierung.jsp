<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@page import="com.sport.analyzer.impl.MannschaftAnalyzerImpl"%>
<%@page import="com.sport.analyzer.MannschaftAnalyzer"%>

<table cellspacing="0" cellpadding="3" border="0" width="100%">
	<tr class="header">
		<td>
			<bean:message key="jsp_platzierung_placings"/>
		</td>
	</tr>

	<tr>
		<td class="databody">
			<table class="tabelle" cellpadding="3" cellspacing="3">
				<tr class="dataheader">
					<td><bean:message key="jsp_platzierung_place"/></td>
					<td><bean:message key="jsp_platzierung_team"/></td>
				</tr>

				<logic:iterate id="platzierung" name="platzierungen" type="com.sport.core.bo.Ranking">
				<tr class="databody">
					<td>
						<bean:write name="platzierung" property="rank" />.
					</td>
					<td>
						<logic:present name="platzierung" property="mannschaft" >
							<logic:present name="platzierung" property="mannschaft.loggruppeBO" >
								<html:img page="/jsp/point.jsp?" paramId="color" paramName="platzierung" paramProperty="mannschaft.loggruppeBO.color" height="10" width="10" border="1" />
							</logic:present>
							<logic:notPresent name="platzierung" property="mannschaft.loggruppeBO" >
								<html:img page="/jsp/point.jsp?" paramId="color" paramName="platzierung" paramProperty="mannschaft.logspielBO.group.color" height="10" width="10" border="1" />
							</logic:notPresent>
							<%
								MannschaftAnalyzer mannschaftAnalyzer = MannschaftAnalyzerImpl.getInstance();
								String mannschaftRelName = mannschaftAnalyzer.getRelName(platzierung.getMannschaft());
								String mannschaftName = mannschaftAnalyzer.getName(platzierung.getMannschaft());
							%>
							<%= mannschaftRelName %>
							(<%= mannschaftName %>)
						</logic:present>
						<logic:notPresent name="platzierung" property="mannschaft" >
							<bean:message key="platzierungentable_please_select"/>
						</logic:notPresent>
					</td>
				</tr>
				</logic:iterate>
			</table>
		</td>
	</tr>
</table>