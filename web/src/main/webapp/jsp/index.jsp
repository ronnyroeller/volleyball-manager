<%@page import="java.util.Locale,com.sport.core.helper.Messages" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<table cellspacing="0" cellpadding="3" border="0" width="100%">
	<tr class="header">
		<td>
			<bean:message key="jsp_index_tournamentoverview"/>
		</td>
	</tr>

	<tr class="databody">
		<td>
			<table class="tabelle" cellpadding="3" cellspacing="3">
				<tr class="dataheader">
					<td><bean:message key="jsp_index_date"/></td>
					<td><bean:message key="jsp_index_name"/></td>
					<td><bean:message key="jsp_index_beamer"/></td>
				</tr>

				<logic:iterate id="turnier" name="turniere" type="com.sport.core.bo.Tournament">
				<tr class="databody">
					<td>
						<bean:write name="turnier" property="date" format="dd. MMMM yyyy HH:mm" />
					</td>
					<td>
						<html:link forward="turnier" paramId="linkid" paramName="turnier" paramProperty="linkid" >
							<bean:write name="turnier" property="name" />
						</html:link>
					</td>
					<td>
						<html:link forward="beamerspielplan" paramId="linkid" paramName="turnier" paramProperty="linkid" >
							<bean:message key="jsp_index_beamer"/>
						</html:link>
					</td>
				</tr>
				</logic:iterate>
			</table>
		</td>
	</tr>

</table>
