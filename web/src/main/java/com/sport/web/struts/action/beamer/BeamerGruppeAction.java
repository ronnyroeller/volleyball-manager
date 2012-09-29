// Created by Xslt generator for Eclipse.
// XSL :  not found (java.io.FileNotFoundException:  (Das System kann den angegebenen Pfad nicht finden))
// Default XSL used : easystruts.jar$org.easystruts.xslgen.JavaClass.xsl

package com.sport.web.struts.action.beamer;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sport.analyzer.GroupResult;
import com.sport.analyzer.GruppeAnalyzer;
import com.sport.analyzer.impl.GruppeAnalyzerImpl;
import com.sport.analyzer.impl.MannschaftAnalyzerImpl;
import com.sport.core.bo.SportGroup;
import com.sport.core.bo.Team;
import com.sport.core.bo.Tournament;
import com.sport.core.bo.comparators.GruppenComparator;
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
public class BeamerGruppeAction extends VolleyAction {

	private static final Logger LOG = Logger.getLogger(BeamerGruppeAction.class);

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
		
		long filterGroupId[] = null;
		// Split string into array of long's
		if (session.getAttribute("groups") != null) {
			String filterGroupIdArray[] = ((String) session.getAttribute("groups")).split(",");
			filterGroupId = new long[filterGroupIdArray.length];
			for (int i=0; i<filterGroupIdArray.length; i++) {
				filterGroupId[i] = Long.parseLong(filterGroupIdArray[i]);
			}
		}

		try {
			TurnierSession turnierSession =
				HomeGetter.getTurnierSessionHome().create();

			Tournament turnierBO = getTurnierBO(request, turnierSession);
			
			String gruppeid = request.getParameter("gruppeid");

			// Daten laden
			Map result =
				turnierSession.getGruppenMannschaftenSpieleByTurnierBOMap(
					turnierBO,
					true);
			Map gruppen = (Map) result.get("gruppenMap");
			SportGroup gruppe = null;
			if (gruppeid == null || gruppeid == "") {
				gruppe = (SportGroup) new Vector(gruppen.values()).firstElement();
			}
			else {
				gruppe = (SportGroup) gruppen.get(new Long(gruppeid));
				if (gruppe == null) {
					gruppe = (SportGroup) new Vector(gruppen.values()).firstElement();
				}
			}

			session.setAttribute("gruppe", gruppe);

			GruppeAnalyzer gruppeAnalyzer = GruppeAnalyzerImpl.getInstance();
			session.setAttribute("gruppeResult", gruppeAnalyzer.getErgebnisDetails(gruppe));

			// folgende Gruppe bestimmen
			SportGroup nextGruppe = null;
			Vector gruppenVector = new Vector(gruppen.values());
			Collections.sort(
				gruppenVector,
				new GruppenComparator());

			int pos = gruppenVector.indexOf(gruppe);
			while (nextGruppe == null) {
				if (++pos > gruppenVector.size() - 1) {
					pos = 0;
				}
				nextGruppe = (SportGroup) gruppenVector.get(pos);

				GroupResult gruppeResult = GruppeAnalyzerImpl.getInstance().getErgebnisDetails(nextGruppe);
				// Ist Gruppe bereits ausgespielt (alle Spiele mit Ergebnis)?
				if (!gruppeResult.isVorlaeufig()) {
					nextGruppe = null;
				}
				else {
					// If filter is set -> is group part of the filter?
					if (filterGroupId != null) {
						boolean contains = false;
						for (int i = 0; i < filterGroupId.length; i++) {
							if (filterGroupId[i] == nextGruppe.getId()) {
								contains = true;
								break;
							}
						}
						
						if (!contains)
							nextGruppe = null;
					}
				}
				
				if (nextGruppe != null) {
					// Ansetzungen bekannt (keine nicht ausgespielten log. Gruppen)
					Iterator mannIt = nextGruppe.getMannschaften().iterator();
					while (mannIt.hasNext() && (nextGruppe != null)) {
						Team mannschaftBO =
							(Team) mannIt.next();
						// Mannschaft nicht auf phy. Mannschaft zurueckfuehrbar
						// -> Ansetzungen noch nicht alle bekannt
						if (MannschaftAnalyzerImpl.getInstance().getRelMannschaftBO(mannschaftBO).isLog()) {
							nextGruppe = null;
						}
					}
				}

				// falls keine andere Gruppe vorhanden -> gleiche!
				if (pos == gruppenVector.indexOf(gruppe)) {
					nextGruppe = gruppe;
				}
			}
			session.setAttribute(
				"nextgruppeid",
				String.valueOf(nextGruppe.getId()));
		} catch (Exception e) {
			LOG.error("Could't prepare projector view 'group'", e);
		}

		return (mapping.findForward("success"));
	}

}
