<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<table cellspacing="0" cellpadding="3" border="0" width="100%">
	<tr class="header">
		<td>
			<bean:write name="tournament" property="name" />
		</td>
	</tr>
	<tr class="databody">
		<td>
			<bean:message key="jsp_turnier_startdate"/>: <bean:write name="tournament" property="date" format="dd. MMMM yyyy HH:mm" /><br>
			&nbsp;<br>
		</td>
	</tr>
	<tr class="dataheader">
		<td>
			<bean:message key="jsp_turnier_general"/>
		</td>
	</tr>
	<tr class="databody">
		<td>
			<html:link forward="spielplan"><bean:message key="jsp_turnier_schedule"/></html:link><br>
			<html:link forward="platzierung"><bean:message key="jsp_turnier_placings"/></html:link><br>
			<html:link forward="statistik"><bean:message key="jsp_turnier_statistic"/></html:link><br>
			&nbsp;<br>
		</td>
	</tr>
	<tr class="dataheader">
		<td>
			<bean:message key="jsp_turnier_placingofgroups"/>
		</td>
	</tr>

	<logic:iterate id="gruppe" name="gruppen" type="com.sport.core.bo.SportGroup">
	<tr class="databody">
		<td>
			<html:link forward="gruppe" paramId="gruppeid" paramName="gruppe" paramProperty="id" >
				<html:img page="/jsp/point.jsp?" paramId="color" paramName="gruppe" paramProperty="color" height="10" width="10" border="1" />
				<bean:write name="gruppe" property="name" />
			</html:link>
		</td>
	</tr>
	</logic:iterate>
		
	<tr class="databody">
		<td>
			&nbsp;
		</td>
	</tr>

	<tr class="dataheader">
		<td>
			<bean:message key="jsp_turnier_createdby"/>
		</td>
	</tr>
	<tr class="databody">
		<td>
			<html:link forward="kontakt"><bean:message key="jsp_turnier_contactimpressum"/></html:link><br>
			&nbsp;
		</td>
	</tr>

</table>
	