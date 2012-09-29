// Created by Xslt generator for Eclipse.
// XSL :  not found (java.io.FileNotFoundException:  (Das System kann den angegebenen Pfad nicht finden))
// Default XSL used : easystruts.jar$org.easystruts.xslgen.JavaClass.xsl

package com.sport.web.struts.action;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sport.core.bo.Tournament;
import com.sport.server.ejb.HomeGetter;
import com.sport.server.ejb.interfaces.TurnierSession;

/** 
 * SpielplanAction.java created by EasyStruts - XsltGen.
 * http://easystruts.sf.net
 * created on 06-22-2003
 * 
 * XDoclet definition:
 * @struts:action path="/spielplan" name="spielplanForm" input="/form/spielplan.jsp" parameter="turnierid" validate="true"
 */
public class SpielplanAction extends VolleyAction {

	private static final Logger LOG = Logger.getLogger(SpielplanAction.class);

	// --------------------------------------------------------- Instance Variables

	// --------------------------------------------------------- Methods

	/** 
	 * Method execute
	 * @param ActionMapping mapping
	 * @param ActionForm form
	 * @param HttpServletRequest request
	 * @param HttpServletResponse response
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession();

		// Sprache �ndern?
		LocaleMgr.setLocale(request.getParameter("locale"), session);
		
		try {
			TurnierSession turnierSession =
				HomeGetter.getTurnierSessionHome().create();

			Tournament turnierBO = getTurnierBO(request, turnierSession);
			
			Vector spielplaetze =
				new Vector(
					turnierSession.getSpielplaetzeByTurnierid(
						turnierBO.getId()));

			session.setAttribute("spielplaetze", spielplaetze);

			Vector tableEntries =
				turnierSession.getSpielplanTableEntrysByTurnierBO(
					turnierBO);

			session.setAttribute("tableentries", tableEntries);
		}
		catch (Exception e) {
			LOG.error("Could't prepare terminal view 'schedule'", e);
		}

		return (mapping.findForward("success"));
	}

}
