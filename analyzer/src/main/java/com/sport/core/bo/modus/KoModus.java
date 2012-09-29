

/*
 * Created on 01.07.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.sport.core.bo.modus;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import com.sport.core.bo.SportGroup;
import com.sport.core.bo.SportMatch;
import com.sport.core.bo.Team;


/**
 * @author ronny
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class KoModus extends AbstractModus {

	public Vector getSpiele(SportGroup gruppeBO) {
		return null;
	}

	/**
	 * Calculates all teams of this group that can act as referees in this turn
	 * @param gruppeBO
	 * @return
	 */
	public Vector getMglReferee(SportGroup gruppeBO) {
		// use all teams from possible games + all looser teams
		Vector unusedSpiele = getUnusedSpieleGen(gruppeBO);

		Collection teams = new HashSet ();
		// add all losing teams
		Iterator spieleIt = gruppeBO.getMatches().iterator();
		while (spieleIt.hasNext()) {
			SportMatch spielBO = (SportMatch) spieleIt.next();
			Team mannBO = new Team ();
			mannBO.setLogspielBO(spielBO);
			mannBO.setLogsort(Team.LOGSPIEL_VERLIERER);
			teams.add(mannBO);
		}

		// add all teams who actual play
		spieleIt = unusedSpiele.iterator();
		while (spieleIt.hasNext()) {
			SportMatch spielBO = (SportMatch) spieleIt.next();
			teams.add(spielBO.getTeam1());
			teams.add(spielBO.getTeam2());
		}

		return new Vector (teams);
	}

	public Vector getUnusedSpiele(SportGroup gruppeBO, boolean gen) {
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

		Vector mannschaftenVector = new Vector();

		// Startmannschaften
		Object[] mannschaften = gruppeBO.getMannschaften().toArray();
		for (int i = 0; i < mannschaften.length; i++) {
			Team mannschaftBO = (Team) mannschaften[i];
			if (!usedMannschaften.contains(mannschaftBO)) {
				mannschaftenVector.add(mannschaftBO);
			}
		}

		// log. Spiele
		usedSpieleIt = gruppeBO.getMatches().iterator();
		while (usedSpieleIt.hasNext()) {
			SportMatch spielBO = (SportMatch) usedSpieleIt.next();
			Team mannschaftBO = new Team();
			mannschaftBO.setLogspielBO(spielBO);
			mannschaftBO.setLogsort(Team.LOGSPIEL_GEWINNER);
			mannschaftenVector.add(mannschaftBO);
			Iterator usedMannschaftenIt = usedMannschaften.iterator();
			while (usedMannschaftenIt.hasNext()) {
				Team usedMannschaftBO =
					(Team) usedMannschaftenIt.next();
				if (mannschaftBO.logequals(usedMannschaftBO)) {
					mannschaftenVector.remove(mannschaftBO);
				}
			}
		}

		mannschaften = mannschaftenVector.toArray();
		for (int i = 0; i < mannschaften.length - 1; i += 2) {
			Team mannschaft1 = (Team) mannschaften[i];
			Team mannschaft2 = (Team) mannschaften[i + 1];

			SportMatch spielBO = new SportMatch();
			spielBO.setGroup(gruppeBO);
			spielBO.setTeam1(mannschaft1);
			spielBO.setTeam2(mannschaft2);
			spiele.add(spielBO);
		}

		return spiele;
	}

	public Vector getMannschaften(SportGroup gruppeBO, Date vondatum) {
		// alle Ausgangsmannschaften
		Vector mannschaften = (Vector) gruppeBO.getMannschaften().clone();

		// alle schon beendeten Spiele
		Iterator it = gruppeBO.getMatches().iterator();
		while (it.hasNext()) {
			SportMatch spielBO = (SportMatch) it.next();
			if (vondatum == null || spielBO.getEndDate().before(vondatum)) {
				Team mannschaftBO = new Team();
				mannschaftBO.setLogspielBO(spielBO);
				mannschaftBO.setLogsort(Team.LOGSPIEL_GEWINNER);
				mannschaften.add(mannschaftBO);

				mannschaftBO = new Team();
				mannschaftBO.setLogspielBO(spielBO);
				mannschaftBO.setLogsort(Team.LOGSPIEL_VERLIERER);
				mannschaften.add(mannschaftBO);
			}
		}

		return mannschaften;
	}

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
