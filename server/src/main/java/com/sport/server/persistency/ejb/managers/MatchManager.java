package com.sport.server.persistency.ejb.managers;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import com.sport.core.bo.Field;
import com.sport.core.bo.SetResult;
import com.sport.core.bo.SportMatch;
import com.sport.server.ejb.interfaces.GruppeLocal;
import com.sport.server.ejb.interfaces.MannschaftLocal;
import com.sport.server.ejb.interfaces.PlatzierungLocal;
import com.sport.server.ejb.interfaces.SatzLocal;
import com.sport.server.ejb.interfaces.SpielLocal;
import com.sport.server.ejb.interfaces.TurnierLocal;
import com.sport.server.ejb.session.BOCreator;
import com.sport.server.persistency.ejb.SportMatchManager;
import com.sport.server.persistency.ejb.maps.FieldMap;
import com.sport.server.persistency.ejb.maps.MatchMap;
import com.sport.server.persistency.ejb.maps.TeamMap;

/**
 * Loads and stores matches
 * 
 * @author Ronny
 *
 */
public class MatchManager {

	/**
	 * Loads matches without linking
	 * 
	 * @param turnierLocal
	 * @param gruppenLocal
	 * @return
	 */
	protected MatchMap load(TurnierLocal turnierLocal) {
		MatchMap matches = new MatchMap();

		Collection<GruppeLocal> gruppenLocal = turnierLocal.getGruppen();

		// Load all matches from the groups and from rankings
		Set<SpielLocal> spieleLocal = new HashSet<SpielLocal>();

		// Add all matches from the groups
		for (GruppeLocal gruppeLocal : gruppenLocal)
			spieleLocal.addAll(gruppeLocal.getSpiele());

		// Add all matches from rankings
		Set<PlatzierungLocal> platzierungenLocal = turnierLocal
				.getPlatzierungen();
		for (PlatzierungLocal platzierungLocal : platzierungenLocal) {
			if (platzierungLocal.getMannschaft() != null) {
				SpielLocal spielLocal = platzierungLocal.getMannschaft()
						.getLogspiel();
				if (spielLocal != null)
					spieleLocal.addAll(spieleLocal);
			}
		}

		// Iterate over all matches
		for (SpielLocal spielLocal : spieleLocal) {
			SportMatch match = BOCreator.createSpielBO(spielLocal);

			// Add sets
			match.setSetResults(new Vector<SetResult>());
			for (SatzLocal satzLocal : (Set<SatzLocal>) spielLocal.getSaetze()) {
				SetResult setResult = BOCreator.createSatzBO(satzLocal);
				match.addSatz(setResult);
			}

			matches.put(spielLocal, match);
		}

		return matches;
	}

	/**
	 * Links matches to all related objects
	 * 
	 * @param teams
	 * @param matches
	 */
	protected void link(TeamMap teams, MatchMap matches,
			FieldMap fields) {
		// Link matches to teams
		for (SpielLocal spielLocal : matches.keySet()) {
			SportMatch match = matches.get(spielLocal);

			MannschaftLocal team1Local = spielLocal.getMannschaft1();
			MannschaftLocal team2Local = spielLocal.getMannschaft2();
			MannschaftLocal refereeLocal = spielLocal.getSchiedsrichter();
			if (team1Local != null)
				match.setTeam1(teams.get(team1Local));
			if (team2Local != null)
				match.setTeam2(teams.get(team2Local));
			if (refereeLocal != null)
				match.setReferee(teams.get(refereeLocal));

			Field field = fields.get(spielLocal.getSpielplatz());
			match.setField(field);
			
			SportMatchManager.logMatch("Loaded", spielLocal);
		}
	}

}
