<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@page import="com.sport.analyzer.MannschaftAnalyzer"%>
<%@page import="com.sport.analyzer.impl.MannschaftAnalyzerImpl"%>

<table cellspacing="0" cellpadding="3" border="0" width="100%">
	<tr>
		<td class="header">
			<bean:write name="gruppe" property="name" />
		</td>
	</tr>

	<tr>
		<td class="databody">
			<table class="tabelle" cellpadding="0" cellspacing="0" width="100%">
				<tr class="dataheader">
					<td><bean:message key="jsp_gruppe_platz"/></td>
					<td>&nbsp;</td>
					<td><bean:message key="jsp_gruppe_team"/></td>
					<td>&nbsp;</td>
					<td><bean:message key="jsp_gruppe_ga"/></td>
					<td>&nbsp;</td>
					<td><bean:message key="jsp_gruppe_pga"/></td>
					<td>&nbsp;</td>
					<td><bean:message key="jsp_gruppe_nga"/></td>
					<td>&nbsp;</td>
					<td><bean:message key="jsp_gruppe_pset"/></td>
					<td>&nbsp;</td>
					<td><bean:message key="jsp_gruppe_nset"/></td>
					<td>&nbsp;</td>
					<td><bean:message key="jsp_gruppe_ppo"/></td>
					<td>&nbsp;</td>
					<td><bean:message key="jsp_gruppe_npo"/></td>
					<td>&nbsp;</td>
					<td><bean:message key="jsp_gruppe_diffpo"/></td>
				</tr>

				<logic:iterate indexId="platznr" id="gruppeErgebnisEntity" name="gruppeResult" property="ergebnisDetails" type="com.sport.analyzer.GruppeErgebnisEntity">
				<tr class="databody">
					<td>
						<%= platznr.intValue ()+1 %>
					</td>
					<td class="dataheader">&nbsp;</td>
					<td>
						<%
							MannschaftAnalyzer mannschaftAnalyzer = MannschaftAnalyzerImpl.getInstance();
							String relName = mannschaftAnalyzer.getRelName(gruppeErgebnisEntity.getMannschaftBO());
						%>
						<%= relName %>
					</td>
					<td class="dataheader">&nbsp;</td>
					<td><bean:write name="gruppeErgebnisEntity" property="spiele" /></td>
					<td class="dataheader">&nbsp;</td>
					<td><bean:write name="gruppeErgebnisEntity" property="pspiele" /></td>
					<td class="dataheader">&nbsp;</td>
					<td><bean:write name="gruppeErgebnisEntity" property="nspiele" /></td>
					<td class="dataheader">&nbsp;</td>
					<td><bean:write name="gruppeErgebnisEntity" property="psaetze" /></td>
					<td class="dataheader">&nbsp;</td>
					<td><bean:write name="gruppeErgebnisEntity" property="nsaetze" /></td>
					<td class="dataheader">&nbsp;</td>
					<td><bean:write name="gruppeErgebnisEntity" property="ppunkte" /></td>
					<td class="dataheader">&nbsp;</td>
					<td><bean:write name="gruppeErgebnisEntity" property="npunkte" /></td>
					<td class="dataheader">&nbsp;</td>
					<td><bean:write name="gruppeErgebnisEntity" property="diffpunkte" /></td>
				</tr>
				</logic:iterate>
			</table>
		</td>
	</tr>
</table>
