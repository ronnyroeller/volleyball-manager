<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@page import="com.sport.analyzer.MannschaftAnalyzer"%>
<%@page import="com.sport.analyzer.SpielAnalyzer"%>
<%@page import="com.sport.analyzer.SpielAnalyzer.DetailedMatchResult"%>
<%@page import="com.sport.analyzer.impl.MannschaftAnalyzerImpl"%>
<%@page import="com.sport.analyzer.impl.SpielAnalyzerImpl"%>
<%@page import="com.sport.core.bo.Field"%>
<%@page import="com.sport.core.bo.SpielplanTableEntryBO"%>
<%@page import="com.sport.core.bo.SportMatch"%>
<%@page import="com.sport.core.bo.SetResult"%>
<bean:define id="spielplaetze" name="spielplaetze" type="java.util.Vector" />

<table cellspacing="0" cellpadding="3" border="0" width="100%">
	<tr class="header">
		<td>
	    	<html:link page="/jsp/spielplanpdf.jsp" target="_blank">
	    		<html:img page="/img/print.gif" align="right" border="0" />
	    	</html:link>
			<bean:message key="jsp_spielplan_schedule"/>
		</td>
	</tr>

	<tr>
		<td class="databody">
			<table class="tabelle" cellpadding="3" cellspacing="3" width="100%">
				<tr class="dataheader">
					<td width="15%"><bean:message key="jsp_spielplan_time"/></td>
					<logic:iterate id="spielplatz" name="spielplaetze" type="Field">
						<td width="<%= 85/spielplaetze.size () %>%"><bean:write name="spielplatz" property="name" /></td>
					</logic:iterate>
				</tr>

				<logic:iterate id="tableentry" indexId="count" name="tableentries" type="SpielplanTableEntryBO">
				<tr class="databody">
					<td>
						<bean:write name="tableentry" property="vondatum" format="HH:mm" /> -
						<bean:write name="tableentry" property="bisdatum" format="HH:mm" /><br />
						<%= count.intValue()+1 %>. <bean:message key="jsp_spielplan_block"/><br />
					</td>
					<logic:iterate id="spiel" name="tableentry" property="spiele" type="SportMatch">
						<td>
							<logic:present name="spiel">
								<html:img page="/jsp/point.jsp?" paramId="color" paramName="spiel" paramProperty="group.color" height="10" width="10" border="1" />
								
								<bean:define id="match" name="spiel" type="SportMatch" />
								<%
									// Fettschreibung fuer Gewinner
									SpielAnalyzer.DetailedMatchResult matchResult = SpielAnalyzerImpl.getInstance().getErgebnisDetails(match);
									
									boolean bold1 = false;
									boolean bold2 = false;
									switch (matchResult.winner) {
									 	case MANNSCHAFT1 :
											bold1 = true;
											break;
										case MANNSCHAFT2 :
											bold2 = true;
											break;
										case UNENTSCHIEDEN :
											bold1 = true;
											bold2 = true;
											break;
									}
									
									MannschaftAnalyzer mannschaftAnalyzer = MannschaftAnalyzerImpl.getInstance();
									String mann1 = mannschaftAnalyzer.getRelName(match.getTeam1());
									String mann2 = mannschaftAnalyzer.getRelName(match.getTeam2());
									if (bold1) {
										mann1 = "<b>" + mann1 + "</b>";
									}
									if (bold2) {
										mann2 = "<b>" + mann2 + "</b>";
									}
								%>
								<%= mann1 %> : <%= mann2 %>
								<logic:present name="spiel" property="referee">
									(<%= mannschaftAnalyzer.getRelName(spiel.getReferee()) %>)
								</logic:present>
								<logic:present name="spiel" property="setResults">
									<logic:iterate id="satz" name="spiel" property="setResults" type="SetResult">
										<bean:define id="satzBO" name="satz" type="SetResult" />
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
				</logic:iterate>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="jsp_spielplan_text"/>
		</td>
	</tr>
</table>