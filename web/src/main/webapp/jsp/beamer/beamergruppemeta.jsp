<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
	<meta http-equiv="refresh" content="<bean:write name="tournament" property="durationProjectorSwitch" />; URL=<html:rewrite forward="beamerspielplan" paramId="gruppeid" paramName="nextgruppeid" />">
