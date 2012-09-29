<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%> 
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<logic:present name="tournament">
	&lt;&lt; <html:link forward="turnier"><bean:message key="jsp_link_backlink"/></html:link>
</logic:present>
