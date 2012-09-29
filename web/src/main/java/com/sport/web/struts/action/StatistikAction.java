// Created by Xslt generator for Eclipse.
// XSL :  not found (java.io.FileNotFoundException:  (Das System kann den angegebenen Pfad nicht finden))
// Default XSL used : easystruts.jar$org.easystruts.xslgen.JavaClass.xsl

package com.sport.web.struts.action;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sport.analyzer.GroupResult;
import com.sport.analyzer.GruppeErgebnisEntity;
import com.sport.analyzer.impl.GruppeAnalyzerImpl;
import com.sport.analyzer.impl.GruppeErgebnisEntryComperator;
import com.sport.analyzer.impl.MannschaftAnalyzerImpl;
import com.sport.core.bo.SportGroup;
import com.sport.core.bo.Tournament;
import com.sport.core.helper.Messages;
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
public class StatistikAction extends VolleyAction {

	private static final Logger LOG = Logger.getLogger(StatistikAction.class);

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

			String linkid = request.getParameter("linkid");
			Tournament turnierBO = (Tournament) session.getAttribute("tournament");
			if (turnierBO == null
				|| (linkid != null
					&& !turnierBO.getLinkid().equals(linkid))) {
				turnierBO = turnierSession.getTurnierByLinkid(linkid);
				session.setAttribute("tournament", turnierBO);
			}

			// Daten laden
			Map result =
				turnierSession.getGruppenMannschaftenSpieleByTurnierBO(
					turnierBO,
					true);
			Set gruppen = new HashSet (Arrays.asList((Object[]) result.get("gruppen")));

			// allgemeine Statistik
			int saetzeCount = 0;
			int ppunkteCount = 0;

			Map ergebnisseMap = new HashMap();

			// alle Gruppen durchgehen und Ergebnisdetails aufaddieren
			Iterator it = gruppen.iterator();
			while (it.hasNext()) {
				SportGroup gruppeBO = (SportGroup) it.next();
				
				GroupResult gruppeResult = GruppeAnalyzerImpl.getInstance().getErgebnisDetails(gruppeBO);
				for (GruppeErgebnisEntity entity : gruppeResult.ergebnisDetails) {
					String mannName = MannschaftAnalyzerImpl.getInstance().getRelName(entity.getMannschaftBO());

					GruppeErgebnisEntity entity2 =
						(GruppeErgebnisEntity) ergebnisseMap.get(mannName);

					// Eintrag fuer diese Mannschaft noch nicht vorhanden? -> anlegen
					if (entity2 == null) {
						ergebnisseMap.put(mannName, entity);
					}
					else {
						// doch vorhanden -> aufaddieren
						entity2.spiele  += entity.getSpiele();
						entity2.pspiele += entity.getPspiele();
						entity2.nspiele += entity.getNspiele();
						entity2.psaetze += entity.getPsaetze();
						entity2.nsaetze += entity.getNsaetze();
						entity2.ppunkte += entity.getPpunkte();
						entity2.npunkte += entity.getNpunkte();
					}

					// allgemeine Statistik
					saetzeCount += entity.getPsaetze();
					ppunkteCount += entity.getPpunkte();
				}
			}

			// in Vector umschreiben und sortieren
			Vector ergebnisse = new Vector();
			Iterator it2 = ergebnisseMap.entrySet().iterator();
			while (it2.hasNext()) {
				Map.Entry entity = (Map.Entry) it2.next();
				ergebnisse.add(entity.getValue());
			}
			Collections.sort(
				ergebnisse,
				new GruppeErgebnisEntryComperator());

			session.setAttribute("ergebnisse", ergebnisse);

			// Allgemeine Statistikwerte
			Vector gesamtstatistik = new Vector();
			Map statistik = new HashMap();
			statistik.put("name", Messages.getString("statistikaction_points_per_set"));
			statistik.put(
				"wert",
				new Float(
					((float) ppunkteCount)
						/ (saetzeCount / turnierBO.getPointsPerSet())));
			gesamtstatistik.add(statistik);
			session.setAttribute("gesamtstatistik", gesamtstatistik);

		}
		catch (Exception e) {
			LOG.error("Could't prepare terminal view 'statistics'", e);
		}

		return (mapping.findForward("success"));
	}

}
