package com.sport.scheduler;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Vector;

import com.sport.analyzer.impl.MannschaftAnalyzerImpl;
import com.sport.core.bo.Field;
import com.sport.core.bo.SpielplanTableEntryBO;
import com.sport.core.bo.SportGroup;
import com.sport.core.bo.SportMatch;
import com.sport.core.bo.Team;
import com.sport.core.bo.Tournament;
import com.sport.core.bo.modus.ModusMgr;


/**
 * @author ronny
 * 
 *         This class generates the schedule
 */
public class ScheduleGen {

	private Tournament turnier;

	private Vector gruppen;

	private Vector spielplaetze;

	private Map relation; // relations between groups

	private Map bitpattern; // bit pattern for a specific group

	public ScheduleGen(Tournament turnier, Vector gruppen, Vector spielplaetze) {
		this.turnier = turnier;
		this.gruppen = gruppen;
		this.spielplaetze = spielplaetze;
	}

	/**
	 * Generates schedule
	 */
	public Vector<SpielplanTableEntryBO> genSchedule() {

		init();

		// Spiele verteilen
		return gen();
	}

	/**
	 * Prepares data for generator
	 */
	private void init() {
		// define bitpatterns
		bitpattern = new HashMap();
		long pattern = 1;
		Iterator gruppeIt = gruppen.iterator();
		while (gruppeIt.hasNext()) {
			SportGroup gruppeBO = (SportGroup) gruppeIt.next();
			bitpattern.put(gruppeBO, new Long(pattern));
			pattern *= 2;
		}

		// create relation matrix (relations between groups)
		relation = new HashMap();
		gruppeIt = gruppen.iterator();
		while (gruppeIt.hasNext()) {
			SportGroup gruppeBO = (SportGroup) gruppeIt.next();
			pattern = 0;

			// alle Mannschaften checken, ob zu anderer Gruppe verlinkt
			Iterator mannIt = gruppeBO.getMannschaften().iterator();
			while (mannIt.hasNext()) {
				Team mannschaftBO = (Team) mannIt.next();

				// group is in relation with the group of act. team
				if (mannschaftBO.getLoggruppeBO() != null) {
					pattern |= ((Long) bitpattern.get(mannschaftBO
							.getLoggruppeBO())).longValue();
				}
			}
			relation.put(gruppeBO, new Long(pattern));
		}

	}

	// creates the schedule
	private Vector gen() {
		int gruppeNr = 0; // Rotationsscheibe ueber allen Gruppen

		Vector spielplanTableEntryVector = new Vector();

		// Ring, aller verfuegbaren Gruppen, die momentan verwendet werden
		// koennen
		Vector ring = new Vector();

		// alle moeglichen Spiele (sortiert nach Gruppen)
		Map mglSpiele = new HashMap();

		// Hashmap stores how often a team was referee
		Map<String, Integer> refereeMap = new HashMap<String, Integer>();

		Date vondatum = turnier.getDate();

		// delete all games in groups
		deleteAllGamesInGroups();

		// wenn Gruppe ausgespielt -> wird Abhaengigkeit geloescht
		while (!(relation.isEmpty() && mglSpiele.isEmpty())) { // ALL BLOCKS

			// all teams that have been used in this block -> can't use again
			gruppeNr = deletePlayedGroupsFromRing(gruppeNr, ring, mglSpiele,
					false);

			SpielplanTableEntryBO spielplanTableEntry = new SpielplanTableEntryBO(
					vondatum, 1000 * turnier.getDurationMatch());
			vondatum = new Date(vondatum.getTime() + 1000
					* (turnier.getDurationMatch() + turnier.getDurationBreak()));

			Vector spiele = new Vector();

			// analyze new state (new round, maybe new groups) -> add new
			// available games
			updateRing(ring, mglSpiele);

			// generate referees for this round
			// create vector of all possible teams without playing teams
			Vector mglReferees = new Vector();
			Iterator ringIt = ring.iterator();
			while (ringIt.hasNext()) {
				SportGroup gruppeBO = (SportGroup) ringIt.next();
				Vector mglRefereesForGroup = ModusMgr.getMglReferee(gruppeBO);
				if (mglRefereesForGroup != null) {
					mglReferees.addAll(mglRefereesForGroup);
				}
			}

			// list of teams that played in this block
			Vector teamInBlock = new Vector();
			gruppeNr = genSpieleForBlock(gruppeNr, ring, mglSpiele,
					spielplanTableEntry, teamInBlock, spiele);

			// if spiele in spielplanTable -> then save
			if (spiele.firstElement() != null) {

				// delete all teams that played during this block
				Iterator teamIt = teamInBlock.iterator();
				while (teamIt.hasNext()) {
					Team mannschaftBO = (Team) teamIt.next();
					// if team is used in block -> delete from referees (also
					// logequal!)
					if (mglReferees.contains(mannschaftBO)) { // checks equals
						mglReferees.remove(mannschaftBO);
					} else {
						// check also logEquals
						Iterator refereeIt = mglReferees.iterator();
						while (refereeIt.hasNext()) {
							Team referee = (Team) refereeIt.next();
							if (mannschaftBO.logequals(referee)) {
								mglReferees.remove(referee);
								break;
							}
						}
					}
				}

				// for all games
				Iterator gameIt = spiele.iterator();
				while (gameIt.hasNext()) {
					SportMatch gameBO = (SportMatch) gameIt.next();
					if (gameBO == null) {
						continue;
					}

					// sort by HashMap
					Collections.sort(mglReferees, new RefereeComparator(gameBO
							.getGroup(), refereeMap));

					// iterate over group -> find a team which is not in actual
					// group
					try {
						Team mglReferee = (Team) mglReferees.firstElement();
						if (mglReferee != null) {
							gameBO.setReferee(mglReferee);
							mglReferees.remove(mglReferee);

							// inc. refereeMap entry
							String teamName = MannschaftAnalyzerImpl.getInstance().getName(mglReferee);
							Integer countInt = (Integer) refereeMap
									.get(teamName);
							int count = (countInt != null) ? countInt
									.intValue() : 0;
							refereeMap.put(teamName, new Integer(++count));
						}
					} catch (NoSuchElementException e) {
					}
				}

				spielplanTableEntry.setSpiele(spiele);
				spielplanTableEntryVector.add(spielplanTableEntry);
			}
		}

		return spielplanTableEntryVector;
	}

	static class RefereeComparator implements Comparator {
		SportGroup gameGruppe;

		Map refereeMap;

		RefereeComparator(SportGroup gameGruppe, Map refereeMap) {
			this.gameGruppe = gameGruppe;
			this.refereeMap = refereeMap;
		}

		public int compare(Object o1, Object o2) {
			if (o1 == null)
				return -1;
			if (o2 == null)
				return 1;

			Team mann1 = (Team) o1;
			Team mann2 = (Team) o2;

			// put games of actual group to the end
			SportGroup gruppe1 = mann1.getGruppeBO();
			gruppe1 = (gruppe1 != null) ? gruppe1 : mann1.getLoggruppeBO();
			gruppe1 = (gruppe1 != null) ? gruppe1 : mann1.getLogspielBO()
					.getGroup();
			if (gruppe1.equals(gameGruppe)) {
				return 1;
			}
			SportGroup gruppe2 = mann2.getGruppeBO();
			gruppe2 = (gruppe2 != null) ? gruppe2 : mann2.getLoggruppeBO();
			gruppe2 = (gruppe2 != null) ? gruppe2 : mann2.getLogspielBO()
					.getGroup();
			if (gruppe2.equals(gameGruppe)) {
				return -1;
			}

			// first looser than winner (if logical)
			boolean mann1Win = (mann1.getLogspielBO() != null && mann1
					.getLogsort() == Team.LOGSPIEL_GEWINNER);
			boolean mann2Win = (mann2.getLogspielBO() != null && mann2
					.getLogsort() == Team.LOGSPIEL_GEWINNER);
			if (mann1Win && !mann2Win) {
				return 1;
			}
			if (mann2Win && !mann1Win) {
				return -1;
			}

			// test on most used
			String teamName1 = MannschaftAnalyzerImpl.getInstance().getName(mann1);
			String teamName2 = MannschaftAnalyzerImpl.getInstance().getName(mann2);
			Comparable col1 = (Integer) refereeMap.get(teamName1);
			Comparable col2 = (Integer) refereeMap.get(teamName2);
			if (col1 == null) {
				return -1;
			}
			if (col2 == null) {
				return 1;
			}
			return col1.compareTo(col2);
		}
	}

	/**
	 * @param gruppeNr
	 * @param ring
	 * @param mglSpiele
	 * @param spielplanTableEntry
	 * @param spiele
	 * @return
	 */
	private int genSpieleForBlock(int gruppeNr, Vector ring, Map mglSpiele,
			SpielplanTableEntryBO spielplanTableEntry, Vector teamInBlock,
			Vector spiele) {

		// alle Felder bespielen
		Iterator spielplaetzeIt = spielplaetze.iterator();
		while (spielplaetzeIt.hasNext()) { // ALL FIELDS
			Field spielplatzBO = (Field) spielplaetzeIt.next();

			if (ring.isEmpty()) {
				// no more games available in this round
				spiele.add(null);
			} else {
				// naechstes Spiel suchen (im Ring suchen)
				int startGruppeNr = gruppeNr;
				SportMatch spielBO = null;
				boolean first = true;

				// einmal Ring durchlaufen bis Treffer
				while (spielBO == null
						&& ((startGruppeNr == gruppeNr) && first)) {
					first = false;
					gruppeNr = incGruppeNr(gruppeNr, ring);
					SportGroup gruppeBO = (SportGroup) ring.get(gruppeNr);

					Vector mglSpieleOfGroup = (Vector) mglSpiele.get(gruppeBO);
					if (mglSpieleOfGroup.isEmpty()) {
						spielBO = null;
					} else {
						// Spiel suchen fuer Block/Spielplatz
						spielBO = (SportMatch) mglSpieleOfGroup.firstElement();
					}
					if (isSpielValid(spielplanTableEntry, spielBO, teamInBlock)) {
						((Vector) mglSpiele.get(gruppeBO)).remove(spielBO);
						spielBO.setStartDate(spielplanTableEntry.getVondatum());
						spielBO.setEndDate(spielplanTableEntry.getBisdatum());
						spielBO.setField(spielplatzBO);
						gruppeBO.addMatch(spielBO);
						teamInBlock.add(spielBO.getTeam1());
						teamInBlock.add(spielBO.getTeam2());

						mglSpiele.put(gruppeBO, ModusMgr
								.getUnusedSpieleGen(gruppeBO));
					} else {
						spielBO = null;
					}
					gruppeNr = deletePlayedGroupsFromRing(gruppeNr, ring,
							mglSpiele, true);

				}
				spiele.add(spielBO);

			}

		}
		return gruppeNr;
	}

	/**
	 * verstoesst Spiel gegen Regeln (Mannschaft spielt schon? oder log. Spiel
	 * noch nicht beendet?)
	 * 
	 * @param spielplanTableEntry
	 * @param spielBO
	 * @return
	 */
	private boolean isSpielValid(SpielplanTableEntryBO spielplanTableEntry,
			SportMatch spielBO, Vector teamInBlock) {
		if (spielBO == null) {
			return false;
		}
		SportMatch logSpiel1 = spielBO.getTeam1().getLogspielBO();
		SportMatch logSpiel2 = spielBO.getTeam2().getLogspielBO();
		if (logSpiel1 != null) {
			if (!logSpiel1.getEndDate().before(
					spielplanTableEntry.getVondatum())) {
				return false;
			}
		}
		if (logSpiel2 != null) {
			if (!logSpiel2.getEndDate().before(
					spielplanTableEntry.getVondatum())) {
				return false;
			}
		}
		if (teamInBlock.contains(spielBO.getTeam1())
				|| teamInBlock.contains(spielBO.getTeam2())) {
			return false;
		}
		return true;
	}

	/**
	 * @param gruppeNr
	 * @param ring
	 * @return
	 */
	private int incGruppeNr(int gruppeNr, Vector ring) {
		gruppeNr++;
		if (gruppeNr >= ring.size()) {
			gruppeNr = 0;
		}
		return gruppeNr;
	}

	/**
	 * @param ring
	 * @param mglSpiele
	 */
	private void updateRing(Vector ring, Map mglSpiele) {
		// Ist eine Relation 0 -> Gruppe verfuegbar -> in Ring einfuegen
		if (relation.containsValue(new Long(0))) {
			Vector keys = (Vector) new Vector(relation.keySet()).clone();
			Iterator keysIt = keys.iterator();
			while (keysIt.hasNext()) {
				SportGroup gruppeBO = (SportGroup) keysIt.next();
				if (((Long) relation.get(gruppeBO)).longValue() == 0) {
					ring.add(gruppeBO);
					relation.remove(gruppeBO);

					// mgl. Spiele speichern
					gruppeBO.setMatches(new Vector());
					mglSpiele.put(gruppeBO, ModusMgr
							.getUnusedSpieleGen(gruppeBO));
				}
			}
		}
	}

	/**
	 *  
	 */
	private void deleteAllGamesInGroups() {
		Iterator groupIt = gruppen.iterator();
		while (groupIt.hasNext()) {
			SportGroup gruppeBO = (SportGroup) groupIt.next();
			gruppeBO.setMatches(new Vector());
		}
	}

	/**
	 * alle Gruppen loeschen, die keine Spiele mehr haben
	 * 
	 * @param gruppeNr
	 * @param ring
	 * @param mglSpiele
	 * @param noKO
	 *            true -> don't throw out KO-groups (only for new blocks)
	 * @return
	 */
	private int deletePlayedGroupsFromRing(int gruppeNr, Vector ring,
			Map mglSpiele, boolean noKO) {
		Iterator groupIt;
		groupIt = gruppen.iterator();
		while (groupIt.hasNext()) {
			SportGroup gruppeBO = (SportGroup) groupIt.next();
			if (ring.contains(gruppeBO)
					&& ModusMgr.getUnusedSpieleGen(gruppeBO).isEmpty()) {
				if (!noKO || !gruppeBO.getTournamentSystem().getKoSystem()) {
					// letztes Spiel aus der Gruppe -> relations
					// loeschen
					relation.remove(gruppeBO);
					mglSpiele.remove(gruppeBO);
					int posInRing = ring.indexOf(gruppeBO);
					ring.remove(gruppeBO);
					if (posInRing <= gruppeNr) {
						gruppeNr--;
					}

					// delete group from all other relations
					Iterator relationIt = relation.keySet().iterator();
					while (relationIt.hasNext()) {
						SportGroup relGruppeBO = (SportGroup) relationIt.next();
						long op1 = ((Long) relation.get(relGruppeBO))
								.longValue();
						long op2 = ((Long) bitpattern.get(gruppeBO))
								.longValue();
						// Bit gesetzt -> haengen Gruppen
						// voneinander ab
						if ((op1 & op2) == op2) {
							// Bit loeschen
							relation.put(relGruppeBO, new Long(op1 - op2));
						}
					}
				}
			}
		}
		return gruppeNr;
	}
}