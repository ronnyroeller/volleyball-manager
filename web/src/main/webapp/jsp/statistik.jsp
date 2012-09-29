<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@page import="com.sport.core.bo.Team"%>
<%@page import="com.sport.analyzer.MannschaftAnalyzer"%>
<%@page import="com.sport.analyzer.impl.MannschaftAnalyzerImpl"%>
<%@page import="com.sport.analyzer.GruppeErgebnisEntity"%>

<table cellspacing="0" cellpadding="3" border="0" width="100%">
	<tr class="header">
		<td>
			<bean:message key="jsp_statistik_statistic"/>
		</td>
	</tr>

	<tr class="databody">
		<td>
			<bean:message key="jsp_statistik_text"/>
		</td>
	</tr>

	<tr>
		<td class="databody">
			<table class="tabelle" cellpadding="3" cellspacing="3" width="100%">
				<tr class="dataheader">
					<td><bean:message key="jsp_gruppe_platz"/></td>
					<td><bean:message key="jsp_gruppe_team"/></td>
					<td><bean:message key="jsp_gruppe_ga"/></td>
					<td><bean:message key="jsp_gruppe_pga"/></td>
					<td><bean:message key="jsp_gruppe_nga"/></td>
					<td><bean:message key="jsp_gruppe_pset"/></td>
					<td><bean:message key="jsp_gruppe_nset"/></td>
					<td><bean:message key="jsp_gruppe_ppo"/></td>
					<td><bean:message key="jsp_gruppe_npo"/></td>
					<td><bean:message key="jsp_gruppe_diffpo"/></td>
				</tr>

				<logic:iterate indexId="platznr" id="gruppeErgebnisEntity" name="ergebnisse" type="GruppeErgebnisEntity">
				<bean:define id="varGruppeErgebnisEntity" name="gruppeErgebnisEntity" type="GruppeErgebnisEntity" />
				<tr class="databody">
					<td>
						<%= platznr.intValue ()+1 %>
					</td>
					<td>
						<%
							MannschaftAnalyzer mannschaftAnalyzer = MannschaftAnalyzerImpl.getInstance();
							Team relTeam = mannschaftAnalyzer.getRelMannschaftBO(gruppeErgebnisEntity.getMannschaftBO());
							String color = java.net.URLEncoder.encode(relTeam.getGruppeBO().getColor(), "UTF-8");
							
							String relName = mannschaftAnalyzer.getRelName(gruppeErgebnisEntity.getMannschaftBO());
						%>
						<img src="/jsp/point.jsp?color=<%= color %>" height="10" width="10" border="1" />
						<%= relName %>
					</td>
					<td align="right">
						<bean:write name="gruppeErgebnisEntity" property="spiele" />
					</td>
					<td align="right">
						<bean:write name="gruppeErgebnisEntity" property="pspiele" /><br />
						(<bean:write name="gruppeErgebnisEntity" property="pspieleAvg" format="##0.00" />)
					</td>
					<td align="right">
						<bean:write name="gruppeErgebnisEntity" property="nspiele" /><br />
						(<bean:write name="gruppeErgebnisEntity" property="nspieleAvg" format="##0.00" />)
					</td>
					<td align="right">
						<bean:write name="gruppeErgebnisEntity" property="psaetze" /><br />
						(<bean:write name="gruppeErgebnisEntity" property="psaetzeAvg" format="##0.00" />)
					</td>
					<td align="right">
						<bean:write name="gruppeErgebnisEntity" property="nsaetze" /><br />
						(<bean:write name="gruppeErgebnisEntity" property="nsaetzeAvg" format="##0.00" />)
					</td>
					<td align="right">
						<bean:write name="gruppeErgebnisEntity" property="ppunkte" /><br />
						(<bean:write name="gruppeErgebnisEntity" property="ppunkteAvg" format="##0.00" />)
					</td>
					<td align="right">
						<bean:write name="gruppeErgebnisEntity" property="npunkte" /><br />
						(<bean:write name="gruppeErgebnisEntity" property="npunkteAvg" format="##0.00" />)
					</td>
					<td align="right">
						<bean:write name="gruppeErgebnisEntity" property="diffpunkte" /><br />
						(<bean:write name="gruppeErgebnisEntity" property="diffpunkteAvg" format="##0.00" />)
					</td>
				</tr>
				</logic:iterate>
			</table>
		</td>
	</tr>


	<tr>
		<td class="databody">
			<table class="tabelle" cellpadding="3" cellspacing="3" >
				<tr class="dataheader">
					<td colspan="2"><bean:message key="jsp_statistik_general"/></td>
				</tr>

				<logic:iterate id="statistik" name="gesamtstatistik">
				<bean:define id="varStatistik" name="statistik" type="java.util.Map" />
				<tr class="databody">
					<td>
						<%= ((java.util.Map) varStatistik).get("name") %>
					</td>
					<td>
						<%= ((java.util.Map) varStatistik).get("wert") %>
					</td>
				</tr>
				</logic:iterate>
			</table>
		</td>
	</tr>
</table>