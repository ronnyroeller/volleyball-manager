<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@page import="com.sport.analyzer.MannschaftAnalyzer"%>
<%@page import="com.sport.analyzer.SpielAnalyzer"%>
<%@page import="com.sport.analyzer.impl.MannschaftAnalyzerImpl"%>
<%@page import="com.sport.analyzer.impl.SpielAnalyzerImpl"%>

<bean:define id="spielplaetze" name="spielplaetze" type="java.util.Vector" />

<table cellspacing="0" cellpadding="3" border="0" width="100%">
	<tr>
		<td class="header">
			<bean:message key="jsp_spielplan_schedule"/>
		</td>
	</tr>

	<tr>
		<td class="databody">
			<table class="tabelle" cellpadding="0" cellspacing="0" width="100%">
				<tr class="dataheader">
					<td width="20%"><bean:message key="jsp_spielplan_time"/></td>
					<logic:iterate id="spielplatz" name="spielplaetze" type="com.sport.core.bo.Field">
						<td class="dataheader">&nbsp;</td>
						<td width="<%= 80/spielplaetze.size () %>%"><bean:write name="spielplatz" property="name" /></td>
					</logic:iterate>
				</tr>

				<logic:iterate id="tableentry" indexId="count" name="tableentries" type="com.sport.core.bo.SpielplanTableEntryBO">
				<tr class="databody">
					<td>
						<bean:write name="tableentry" property="vondatum" format="HH:mm" /> -
						<bean:write name="tableentry" property="bisdatum" format="HH:mm" /><br />
					</td>
					<logic:iterate id="spiel" name="tableentry" property="spiele" type="com.sport.core.bo.SportMatch">
						<td class="dataheader">
							&nbsp;
						</td>
						<td>
							<logic:present name="spiel">
				
								<%
									MannschaftAnalyzer mannschaftAnalyzer = MannschaftAnalyzerImpl.getInstance();
									String mannschaft1RelName = mannschaftAnalyzer.getRelName(spiel.getTeam1());
									String mannschaft2RelName = mannschaftAnalyzer.getRelName(spiel.getTeam2());
								%>

								<%= mannschaft1RelName %>
								<hr />
								<%= mannschaft2RelName %>

								<logic:present name="spiel" property="referee">
									<%
										String schiedsrichterRelName = mannschaftAnalyzer.getRelName(spiel.getReferee());
									%>
									<hr />
									(<%= schiedsrichterRelName %>)
								</logic:present>
								<logic:present name="spiel" property="saetze">
									<logic:iterate id="satz" name="spiel" property="saetze" type="com.sport.core.bo.SetResult">
										<bean:define id="satzBO" name="satz" type="com.sport.core.bo.SetResult" />
										<%
											// Fettschreiben der gewonnen Saetze
											String punkte1 = String.valueOf(satzBO.getPoints1());
											String punkte2 = String.valueOf(satzBO.getPoints2());
											
											switch (SpielAnalyzerImpl.getInstance().getSetErgebnis(satzBO)) {
												case MANNSCHAFT1 :
													punkte1 = "<b>" + punkte1 + "</b>";
													break;
												case MANNSCHAFT2 :
													punkte2 = "<b>" + punkte2 + "</b>";
													break;
											}
										%>
										<br />
										<%= punkte1 %> :
										<%= punkte2 %>
									</logic:iterate>
								</logic:present>
							</logic:present>
						</td>
					</logic:iterate>
				</tr>

				<% // Zwischenzeilen %>
				<tr>
					<td colspan="<%= 2*spielplaetze.size ()+1 %>" class="dataheader">
						&nbsp;
					</td>
				</tr>
				</logic:iterate>
			</table>
		</td>
	</tr>

</table>