// Created by Xslt generator for Eclipse.
// XSL :  not found (java.io.FileNotFoundException:  (Das System kann den angegebenen Pfad nicht finden))
// Default XSL used : easystruts.jar$org.easystruts.xslgen.JavaClass.xsl

package com.sport.web.struts.action.beamer;

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sport.core.bo.SpielplanTableEntryBO;
import com.sport.core.bo.Tournament;
import com.sport.server.ejb.HomeGetter;
import com.sport.server.ejb.interfaces.TurnierSession;
import com.sport.web.struts.action.LocaleMgr;
import com.sport.web.struts.action.VolleyAction;

/** 
 * SpielplanAction.java created by EasyStruts - XsltGen.
 * http://easystruts.sf.net
 * created on 06-22-2003
 * 
 * XDoclet definition:
 * @struts:action path="/spielplan" name="spielplanForm" input="/form/spielplan.jsp" parameter="turnierid" validate="true"
 */
public class BeamerSpielplanAction extends VolleyAction {

	private static final Logger LOG = Logger.getLogger(BeamerSpielplanAction.class);

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

		// Filter by fields
		if (request.getParameter("fields") != null) {
			session.setAttribute("fields", request.getParameter("fields"));
		}
		// Filter by groups
		if (request.getParameter("groups") != null) {
			session.setAttribute("groups", request.getParameter("groups"));
		}
		
		int filterFieldNumber[] = null;
		if (session.getAttribute("fields") != null) {
			// split by , and store field names
			String filterFieldsArray[] = ((String) session.getAttribute("fields")).split(",");
			filterFieldNumber = new int[filterFieldsArray.length];
			for (int i=0; i<filterFieldsArray.length; i++) {
				filterFieldNumber[i] = Integer.parseInt(filterFieldsArray[i])-1;
			}
		}
		
		try {
			TurnierSession turnierSession =
				HomeGetter.getTurnierSessionHome().create();

			Tournament turnierBO = getTurnierBO(request, turnierSession);

			Vector spielplaetze =
				new Vector(
					turnierSession.getSpielplaetzeByTurnierid(
						turnierBO.getId()));

			// Reduce the amount of fields to the filtered once (if defined)
			Vector filteredSpielplaetze = null;
			if (filterFieldNumber == null) {
				filteredSpielplaetze = spielplaetze;
			}
			else {
				filteredSpielplaetze = new Vector();
				for (int i = 0; i < filterFieldNumber.length; i++) {
					filteredSpielplaetze.add(spielplaetze.get(filterFieldNumber[i]));
				}
			}
			session.setAttribute("spielplaetze", filteredSpielplaetze);				

			Vector allTableEntries =
				turnierSession.getSpielplanTableEntrysByTurnierBO(
					turnierBO);

			// naechsten beiden tableEntries
			Vector tableEntries = new Vector ();
			
			Iterator it = allTableEntries.iterator();
			while (it.hasNext() && (tableEntries.size() < 2)) {
				SpielplanTableEntryBO tableEntry = (SpielplanTableEntryBO) it.next();

				// remove filted fields
				if (filterFieldNumber != null) {
					Vector filteredSpiele = new Vector();
					for (int i = 0; i < filterFieldNumber.length; i++) {
						filteredSpiele.add(tableEntry.getSpiel(filterFieldNumber[i]));
					}
					tableEntry.setSpiele(filteredSpiele);
				}
				
				// tableEntry noch nicht vorbei
				if (tableEntry.getBisdatum().after(new Date())) {
					tableEntries.add (tableEntry);
				}
			}

			session.setAttribute("tableentries", tableEntries);
			String nextgruppeid = request.getParameter("gruppeid");
			if (nextgruppeid == null) {
				nextgruppeid = "-1";
			}
			session.setAttribute("nextgruppeid", nextgruppeid);
		}
		catch (Exception e) {
			LOG.error("Could't prepare projector view 'schedule'", e);
		}

		return (mapping.findForward("success"));
	}

}
