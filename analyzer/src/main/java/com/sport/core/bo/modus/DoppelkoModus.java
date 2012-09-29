/**
 * Created on 01.07.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.sport.core.bo.modus;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import com.sport.core.bo.SportGroup;
import com.sport.core.bo.SportMatch;
import com.sport.core.bo.Team;
import com.sport.core.bo.comparators.SpielComparator;


/**
 * @author ronny
 * 
 * In einem Doppel-KO-Modus scheidet eine Mannschaft erst aus, wenn sie zweimal
 * verloren hat!
 *  
 */
public class DoppelkoModus extends AbstractModus {

	public Vector getSpiele(SportGroup gruppeBO) {
		return null;
	}

	/**
	 * Calculates all teams of this group that can act as referees in this turn
	 * 
	 * @param gruppeBO
	 * @return
	 */
	public Vector getMglReferee(SportGroup gruppeBO) {
		// use all teams from possible games + all looser teams
		Map map = getUnusedSpieleMannschaften(gruppeBO, true);
		Vector spiele = (Vector) map.get("spiele");
		Vector looserMannschaftenVector = (Vector) map.get("looserMannschaftenVector");

		Collection teams = new HashSet ();
		teams.addAll(looserMannschaftenVector);

		Iterator spieleIt = spiele.iterator();
		while (spieleIt.hasNext()) {
			SportMatch spielBO = (SportMatch) spieleIt.next();
			teams.add(spielBO.getTeam1());
			teams.add(spielBO.getTeam2());
		}

		return new Vector (teams);
	}

	public Vector getUnusedSpiele(SportGroup gruppeBO, boolean gen) {
		Vector result = (Vector) getUnusedSpieleMannschaften(gruppeBO, gen)
				.get("spiele");
		return result;
	}

	/**
	 * Generates list of unusedSpiele and aside gives back the list of winner
	 * and looserteams
	 * 
	 * @param gruppeBO
	 * @param gen
	 * @return
	 */
	private Map getUnusedSpieleMannschaften(SportGroup gruppeBO, boolean gen) {
		// for schedule generator
		Vector spiele = new Vector();

		// jede Mannschaft (auch log.!) darf nur genau einmal spielen
		Vector usedMannschaften = new Vector();
		Iterator usedSpieleIt = gruppeBO.getMatches().iterator();
		while (usedSpieleIt.hasNext()) {
			SportMatch spielBO = (SportMatch) usedSpieleIt.next();
			usedMannschaften.add(spielBO.getTeam1());
			usedMannschaften.add(spielBO.getTeam2());
		}

		Vector startMannschaftenVector = new Vector();

		// Startmannschaften
		Object[] startMannschaften = gruppeBO.getMannschaften().toArray();
		for (int i = 0; i < startMannschaften.length; i++) {
			Team mannschaftBO = (Team) startMannschaften[i];
			if (!usedMannschaften.contains(mannschaftBO)) {
				startMannschaftenVector.add(mannschaftBO);
			}
		}

		// Menge aller Teams, die schon ein Spiel verloren haben
		Vector looserSpiele = new Vector();

		Vector winnerMannschaftenVector = new Vector();
		Vector looserMannschaftenVector = new Vector();

		// log. Spiele
		usedSpieleIt = gruppeBO.getMatches().iterator();
		while (usedSpieleIt.hasNext()) {
			SportMatch spielBO = (SportMatch) usedSpieleIt.next();

			// alle Gewinner
			Team winMannschaftBO = new Team();
			winMannschaftBO.setLogspielBO(spielBO);
			winMannschaftBO.setLogsort(Team.LOGSPIEL_GEWINNER);

			// falls ein Teamer Verlierer oder vormarkiert
			if ((spielBO.getTeam1().isLog()
					&& (looserSpiele.contains(spielBO.getTeam1()
							.getLogspielBO())) || (spielBO.getTeam1()
					.getLogspielBO() != null && spielBO.getTeam1()
					.getLogsort() == Team.LOGSPIEL_VERLIERER))
					|| (spielBO.getTeam2().isLog()
							&& (looserSpiele.contains(spielBO.getTeam2()
									.getLogspielBO())) || (spielBO
							.getTeam2().getLogspielBO() != null && spielBO
							.getTeam2().getLogsort() == Team.LOGSPIEL_VERLIERER))) {

				looserMannschaftenVector.add(winMannschaftBO);
				Iterator usedMannschaftenIt = usedMannschaften.iterator();
				while (usedMannschaftenIt.hasNext()) {
					Team usedMannschaftBO = (Team) usedMannschaftenIt
							.next();
					if (winMannschaftBO.logequals(usedMannschaftBO)) {
						looserMannschaftenVector.remove(winMannschaftBO);
					}
				}
				looserSpiele.add(spielBO);
			} else {

				winnerMannschaftenVector.add(winMannschaftBO);

				// check if winMannschaftBO is still used
				Iterator usedMannschaftenIt = usedMannschaften.iterator();
				while (usedMannschaftenIt.hasNext()) {
					Team usedMannschaftBO = (Team) usedMannschaftenIt
							.next();
					if (winMannschaftBO.logequals(usedMannschaftBO)) {
						winnerMannschaftenVector.remove(winMannschaftBO);
					}
				}

				Team looseMannschaftBO = new Team();
				looseMannschaftBO.setLogspielBO(spielBO);
				looseMannschaftBO.setLogsort(Team.LOGSPIEL_VERLIERER);
				looserMannschaftenVector.add(looseMannschaftBO);
				usedMannschaftenIt = usedMannschaften.iterator();
				while (usedMannschaftenIt.hasNext()) {
					Team usedMannschaftBO = (Team) usedMannschaftenIt
							.next();
					if (looseMannschaftBO.logequals(usedMannschaftBO)) {
						looserMannschaftenVector.remove(looseMannschaftBO);
					}
				}
			}
		}

		// final game
		// && (startMannschaften.length == 0)
		if (winnerMannschaftenVector.size() == 1
				&& looserMannschaftenVector.size() == 1) {
			winnerMannschaftenVector.add(looserMannschaftenVector
					.firstElement());
			looserMannschaftenVector = new Vector();
		}

		// STARTMANNSCHAFTEN
		startMannschaften = startMannschaftenVector.toArray();
		for (int i = 0; i < startMannschaften.length - 1; i += 2) {
			Team mannschaft1 = (Team) startMannschaften[i];
			Team mannschaft2 = (Team) startMannschaften[i + 1];

			SportMatch spielBO = new SportMatch();
			spielBO.setGroup(gruppeBO);
			spielBO.setTeam1(mannschaft1);
			spielBO.setTeam2(mannschaft2);
			spiele.add(spielBO);
		}

		// immer abwechselnd Winner und Looser
		Object[] winnerMannschaften = winnerMannschaftenVector.toArray();
		Object[] looserMannschaften = looserMannschaftenVector.toArray();
		int maxLenght = Math.max(winnerMannschaften.length,
				looserMannschaften.length);

		// falls letztes Spiel Gewinnerspiel (und kein Startspiel) -> naechstes
		// ist Verlierspiel
		boolean startLooser = false;
		Vector oldSpiele = gruppeBO.getMatches();
		if (!oldSpiele.isEmpty() && (startMannschaftenVector.isEmpty())) {
			startLooser = true;
			Collections.sort(oldSpiele, new SpielComparator());
			SportMatch lastSpiel = (SportMatch) oldSpiele.lastElement();

			//  kein Startspiel
			if (!lastSpiel.getTeam1().isLog()) {
				startLooser = false;
			} else {

				Iterator looserSpieleIt = looserSpiele.iterator();
				while (looserSpieleIt.hasNext()) {
					SportMatch looserSpiel = (SportMatch) looserSpieleIt.next();
					// LooserSpiel
					if (looserSpiel.getTeam1().logequals(
							lastSpiel.getTeam1())
							&& looserSpiel.getTeam2().logequals(
									lastSpiel.getTeam2())) {
						startLooser = false;
					}
				}
			}
		}

		// alle Mannschaften verbrauchen
		for (int i = 0; i < maxLenght - 1; i += 2) {

			if (!startLooser && (i < winnerMannschaften.length - 1)) {
				Team mannschaft1 = (Team) winnerMannschaften[i];
				Team mannschaft2 = (Team) winnerMannschaften[i + 1];

				SportMatch spielBO = new SportMatch();
				spielBO.setGroup(gruppeBO);
				spielBO.setTeam1(mannschaft1);
				spielBO.setTeam2(mannschaft2);
				spiele.add(spielBO);
			}

			if (i < looserMannschaften.length - 1) {
				Team mannschaft1 = (Team) looserMannschaften[i];
				Team mannschaft2 = (Team) looserMannschaften[i + 1];

				SportMatch spielBO = new SportMatch();
				spielBO.setGroup(gruppeBO);
				spielBO.setTeam1(mannschaft1);
				spielBO.setTeam2(mannschaft2);
				spiele.add(spielBO);
			}

			if (startLooser && (i < winnerMannschaften.length - 1)) {
				Team mannschaft1 = (Team) winnerMannschaften[i];
				Team mannschaft2 = (Team) winnerMannschaften[i + 1];

				SportMatch spielBO = new SportMatch();
				spielBO.setGroup(gruppeBO);
				spielBO.setTeam1(mannschaft1);
				spielBO.setTeam2(mannschaft2);
				spiele.add(spielBO);
			}
		}

		Map result = new HashMap();
		result.put("spiele", spiele);
		result.put("winnerMannschaftenVector", winnerMannschaftenVector);
		result.put("looserMannschaftenVector", looserMannschaftenVector);

		return result;
	}

	public Vector getMannschaften(SportGroup gruppeBO, Date vondatum) {
		return new KoModus().getMannschaften(gruppeBO, vondatum);
	}

	/*
	 * public Vector getLogMannschaften(GruppeBO gruppeBO) { return null; }
	 */
	public boolean isShowPaarung() {
		return false;
	}

	public boolean isKoSystem() {
		return true;
	}

	public Vector getSpieleGen(SportGroup gruppeBO) {
		return null;
	}

	public Vector getUnusedSpiele(SportGroup gruppeBO) {
		return getUnusedSpiele(gruppeBO, false);
	}

	public Vector getUnusedSpieleGen(SportGroup gruppeBO) {
		return getUnusedSpiele(gruppeBO, true);
	}

}