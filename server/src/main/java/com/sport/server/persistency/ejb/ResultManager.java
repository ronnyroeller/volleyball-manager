package com.sport.server.persistency.ejb;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import com.sport.core.bo.Field;
import com.sport.core.bo.SpielplanTableEntryBO;
import com.sport.core.bo.SportGroup;
import com.sport.core.bo.SportMatch;
import com.sport.core.bo.Tournament;
import com.sport.core.bo.TournamentHolder;
import com.sport.core.bo.comparators.SpielplanTableEntryComparator;
import com.sport.server.persistency.ejb.managers.PersistencyManager;


/**
 * Handles all persistency mappings for group results (SpielplanTableEntrys)
 * 
 * @author Ronny
 * 
 */
public class ResultManager {

	private PersistencyManager persistencyManager = new PersistencyManager();

	/**
	 * Stores the result of the function call
	 */
	public class Result {
		// Lists of all loaded entities
		public Set<SportGroup> groups = new HashSet<SportGroup>();
		public Vector<SpielplanTableEntryBO> tableEntries = new Vector<SpielplanTableEntryBO>();
	}

	/**
	 * Returns schedule and linked groups
	 */
	public Result getSpielplanTableEntrysGruppenByTurnierBO(
			Tournament tournament, Vector<Long> selGruppenIds) {
		// Daten laden
		TournamentHolder tournamentHolder = persistencyManager.findByTournamentId(tournament.getId());

		return getSpielplanTableEntrysGruppenByTurnierBO(tournamentHolder, selGruppenIds);
	}

	/**
	 * Returns schedule and linked groups
	 */
	public Result getSpielplanTableEntrysGruppenByTurnierBO(
			TournamentHolder tournamentHolder, Vector<Long> selGruppenIds) {
		Result result = new Result();

		Vector<Field> fields = tournamentHolder.getFields();
		
		Set<SportMatch> filteredMatches = new HashSet<SportMatch>();

		// If filters are set -> remove matches from other groups
		if (selGruppenIds != null) {
			for (SportMatch match : tournamentHolder.getMatches()) {
				long groupId = match.getGroup().getId();
				if (selGruppenIds.contains(groupId))
					filteredMatches.add(match);
			}
		} else {
			filteredMatches = new HashSet<SportMatch>(tournamentHolder.getMatches());
		}

		// Maps start dates to the fields->match
		Map<Date, Map<Long, SportMatch>> startDateMap = new HashMap<Date, Map<Long, SportMatch>>();
		for (SportMatch spielBO : filteredMatches) {
			// Already data for this start time? If so -> extend map;
			// otherwise create new entry
			Map<Long, SportMatch> spieleMap = startDateMap.get(spielBO
					.getStartDate());
			if (spieleMap == null)
				spieleMap = new HashMap<Long, SportMatch>();

			spieleMap.put(spielBO.getField().getId(), spielBO);
			startDateMap.put(spielBO.getStartDate(), spieleMap);
		}

		// Iterate over all start dates and create table entries
		for (Date startDate : startDateMap.keySet()) {
			Map<Long, SportMatch> spieleMap = startDateMap.get(startDate);

			// Fill up all cells -> with found matches or empty matches
			SpielplanTableEntryBO tableEntry = new SpielplanTableEntryBO(
					startDate);
			for (Field field : fields) {
				SportMatch match = spieleMap.get(field.getId());
				tableEntry.addSpiel(match);

				if (match != null)
					tableEntry.setBisdatum(match.getEndDate());
			}
			result.tableEntries.add(tableEntry);
		}

		Collections.sort(result.tableEntries,
				new SpielplanTableEntryComparator());

		result.groups = new HashSet<SportGroup>(tournamentHolder.getGroups());

		return result;
	}

}
