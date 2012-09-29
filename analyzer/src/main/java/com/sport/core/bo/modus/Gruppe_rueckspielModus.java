/*
 * Created on 01.07.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.sport.core.bo.modus;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import com.sport.core.bo.SportGroup;
import com.sport.core.bo.SportMatch;
import com.sport.core.bo.Team;


/**
 * Verwaltet GruppenModus -> d.h. jeder spielt gegen jeden 2x
 * @author ronny
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Gruppe_rueckspielModus extends AbstractModus {

	public Vector getSpiele(SportGroup gruppeBO) {
		return new GruppenModus ().getSpiele(gruppeBO);
	}
	
	/**
	 * Calculates all teams of this group that can act as referees in this turn
	 * @param gruppeBO
	 * @return
	 */
	public Vector getMglReferee(SportGroup gruppeBO) {
		// every team is possible referee
		Vector mglReferees = new Vector ();
		Iterator teamIt = gruppeBO.getMannschaften().iterator();
		while (teamIt.hasNext()) {
			Team mannschaftBO = (Team) teamIt
					.next();
			mglReferees.add(mannschaftBO);
		}
		return mglReferees;
	}

	public Vector getUnusedSpiele(SportGroup gruppeBO, boolean gen) {
		Vector spiele = new Vector();

		Vector allSpiele = null;
		if (gen) {
			allSpiele = getSpieleGen(gruppeBO);
		}
		else {
			allSpiele = getSpiele(gruppeBO);
		}

		// falls Paarung schon verwendet -> ausfiltern!
		Iterator allSpieleIt = allSpiele.iterator();
		while (allSpieleIt.hasNext()) {
			SportMatch spielBO = (SportMatch) allSpieleIt.next();
			Iterator usedSpieleIt = gruppeBO.getMatches().iterator();

			spiele.add(spielBO);

			while (usedSpieleIt.hasNext()) {
				SportMatch usedSpielBO = (SportMatch) usedSpieleIt.next();
				if (usedSpielBO
					.getTeam1()
					.equals(spielBO.getTeam1())
					&& usedSpielBO.getTeam2().equals(
						spielBO.getTeam2())) {
					spiele.remove(spielBO);
					break;
				}
			}
		}

		return spiele;
	}

	public Vector getMannschaften(SportGroup gruppeBO, Date vondatum) {
		return (Vector) gruppeBO.getMannschaften().clone();
	}

	public boolean isShowPaarung() {
		return true;
	}

	public boolean isKoSystem() {
		return false;
	}


	/**
	 * Erst alle moeglichen Spiele berechnen, dann diese so sortieren,
	 * dass ein max. Abstand zwischen den spielenden Mannschaften ist.
	 * 1. Mannschaft -> Mannschaft, mit den meisten offenen Spielen
	 * 2. Mannschaft -> Mannschaft, mit den meisten offenen Spielen, die noch nicht gegen Mannschaft 1 gespielt hat
	 */
	public Vector getSpieleGen(SportGroup gruppeBO) {
		Map spieleMap = new HashMap();
		Vector anzSpiele = new Vector(); // Anzahl der offenen Spiele

		Iterator mannIt = 	gruppeBO.getMannschaften().iterator();
		while (mannIt.hasNext()) {
			Object[] array = new Object[2];
			array[0] = (Team) mannIt.next();
			array[1] = new Long(gruppeBO.getMannschaften().size() - 1);
			anzSpiele.add(array);
		}
	

		Object[] mannschaften = gruppeBO.getMannschaften().toArray();
		for (int i = 0; i < mannschaften.length - 1; i++) {
			Team mannschaft1 = (Team) mannschaften[i];

			for (int j = i + 1; j < mannschaften.length; j++) {
				Team mannschaft2 = (Team) mannschaften[j];

				// Hinspiel
				SportMatch hinspielBO = new SportMatch();
				hinspielBO.setGroup(gruppeBO);
				hinspielBO.setTeam1(mannschaft1);
				hinspielBO.setTeam2(mannschaft2);
				spieleMap.put(
					mannschaft1.getId() + "#" + mannschaft2.getId(),
					hinspielBO);

				// Rueckspiel
				SportMatch rueckspielBO = new SportMatch();
				rueckspielBO.setGroup(gruppeBO);
				rueckspielBO.setTeam1(mannschaft2);
				rueckspielBO.setTeam2(mannschaft1);
				spieleMap.put(
					mannschaft2.getId() + "#" + mannschaft1.getId(),
					rueckspielBO);
			}
		}

		// sortierte Spiele
		Vector spiele = new Vector();
		// solang noch Spiele zum Verteilen da sind
		while (!spieleMap.isEmpty()) {
			// Mannschaft mit den meisten offenen Spielen suchen
			Collections.sort(anzSpiele, new AnzSpieleComparator());
			Object[] array = (Object[]) anzSpiele.firstElement();
			Team mannschaft1 = (Team) array[0];
			// Anz. offener Spiele decrementieren
			anzSpiele.remove(array);
			array[1] = new Long(((Long) array[1]).longValue() - 1);
			anzSpiele.add(array);

			SportMatch spielBO = null;

			Collections.sort(anzSpiele, new AnzSpieleComparator());
			Iterator anzSpieleIt = anzSpiele.iterator();

			while (spielBO == null) {
				// 2. Mannschaft bestimmen, mit den meisten offen Spielen
				array = (Object[]) anzSpieleIt.next();
				Team mannschaft2 = (Team) array[0];

				String key = mannschaft1.getId() + "#" + mannschaft2.getId();
				spielBO = (SportMatch) spieleMap.get(key);

				// ggf. andere Rueckspiel?
				if ( spielBO == null) {
					key = mannschaft2.getId() + "#" + mannschaft1.getId();
					spielBO = (SportMatch) spieleMap.get(key);
				}

				// noch nicht gegen Mannschaft1 gespielt?
				if (spielBO != null) {
					spieleMap.remove(key);

					// Anz. offener Spiele decrementieren
					anzSpiele.remove(array);
					array[1] = new Long(((Long) array[1]).longValue() - 1);
					anzSpiele.add(array);
				}
			}

			spiele.add(spielBO);
		}

		return spiele;
	}


	/**
	 * @see com.sport.core.bo.modus.AbstractModus#getUnusedSpiele(SportGroup)
	 */
	public Vector getUnusedSpiele(SportGroup gruppeBO) {
		return getUnusedSpiele(gruppeBO, false);
	}

	/**
	 * @see com.sport.core.bo.modus.AbstractModus#getUnusedSpieleGen(SportGroup)
	 */
	public Vector getUnusedSpieleGen(SportGroup gruppeBO) {
		return getUnusedSpiele(gruppeBO, true);
	}


	public static class AnzSpieleComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			if (o1 == null)
				return -1;
			if (o2 == null)
				return 1;
			Comparable col1 =
				(Long) ((Object[]) o1)[1];
			Comparable col2 =
				(Long) ((Object[]) o2)[1];
			return col2.compareTo(col1);
		}
	}

}
