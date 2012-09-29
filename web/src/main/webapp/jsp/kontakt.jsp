<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<table cellspacing="0" cellpadding="3" border="0" width="100%">
	<tr class="header">
		<td>
			<bean:message key="jsp_kontakt_realization"/>
			<logic:present name="tournament">
				<bean:write name="tournament" property="name" />
			</logic:present>
		</td>
	</tr>

	<tr class="subheader">
		<td>
            <bean:message key="jsp_kontakt_software"/>
		</td>
	</tr>
	<tr class="databody">
		<td>
			<bean:message key="jsp_kontakt_softwaretext"/>
			<br />
			&nbsp;
		</td>
	</tr>

	<tr class="subheader">
		<td>
			<bean:message key="licence_licencefor"/>
		</td>
	</tr>
	<tr class="databody">
		<td>
			<logic:notEqual name="licenceBO" property="lastname" value="">
				<bean:write name="licenceBO" property="firstname" /> <bean:write name="licenceBO" property="lastname" />
				(<bean:write name="licenceBO" property="organisation" />)<br />
				<bean:write name="licenceBO" property="city" />, <bean:write name="licenceBO" property="country" /><br />
				<bean:write name="licenceBO" property="licencetypeLocale" /> (<bean:write name="licenceBO" property="licencedate" format="dd. MMMM yyyy" />)
			</logic:notEqual>
			<logic:equal name="licenceBO" property="lastname" value="">
				<bean:write name="licenceBO" property="licencetypeLocale" />
			</logic:equal>
			<br />
			&nbsp;
		</td>
	</tr>

	<tr class="subheader">
		<td>
			<bean:message key="jsp_kontakt_contact"/>
		</td>
	</tr>
	<tr class="databody">
		<td>
		    Ronny R&ouml;ller<br />
		    Am Goldenen Stiefel 23<br />
		    01219 Dresden<br />
		    Germany<br />
			&nbsp;<br />
			<html:link href="mailto:info@volleyball-manager.com">info@volleyball-manager.com</html:link><br>
			&nbsp;
		</td>
	</tr>

	<tr class="header">
		<td>
			<bean:message key="jsp_kontakt_legals"/>
		</td>
	</tr>

	<tr class="subheader">
		<td>
			<bean:message key="jsp_kontakt_copyright"/>
		</td>
	</tr>
	<tr class="subbody">
		<td>
			<bean:message key="jsp_kontakt_copyrighttext"/>
			<br />&nbsp;
		</td>
	</tr>

	<tr class="subheader">
		<td>
			<bean:message key="jsp_kontakt_responsibility"/>
		</td>
	</tr>
	<tr class="subbody">
		<td>
			<bean:message key="jsp_kontakt_responsibilitytext"/>
			<br />
			&nbsp;
		</td>
	</tr>
</table>
