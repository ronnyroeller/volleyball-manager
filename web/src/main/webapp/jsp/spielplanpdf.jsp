<%@ page import="com.sport.server.ejb.HomeGetter, java.util.Locale,com.sport.core.bo.Tournament" 
%><%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" 
%><bean:define id="tournament" name="tournament" type="Tournament" /><%
	byte[] pdf =
	HomeGetter
		.getXmlSessionHome()
		.create()
		.getSpielplanPDFByTurnierid(
		tournament.getId(), null, Locale.getDefault());
		
	response.setContentType("application/pdf");
	response.getOutputStream().write(pdf);
%>