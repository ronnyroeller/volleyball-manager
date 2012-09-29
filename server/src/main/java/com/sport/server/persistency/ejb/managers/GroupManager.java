package com.sport.server.persistency.ejb.managers;

import java.util.Collection;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.sport.core.bo.SportGroup;
import com.sport.core.bo.SportMatch;
import com.sport.core.bo.Team;
import com.sport.core.bo.Tournament;
import com.sport.core.bo.TournamentSystem;
import com.sport.server.ejb.interfaces.GruppeLocal;
import com.sport.server.ejb.interfaces.MannschaftLocal;
import com.sport.server.ejb.interfaces.SpielLocal;
import com.sport.server.ejb.interfaces.TurnierLocal;
import com.sport.server.ejb.session.BOCreator;
import com.sport.server.persistency.ejb.maps.GroupMap;
import com.sport.server.persistency.ejb.maps.MatchMap;
import com.sport.server.persistency.ejb.maps.TeamMap;


/**
 * Loads and stores groups
 * 
 * @author Ronny
 *
 */
public class GroupManager {

	private static final Logger LOG = Logger.getLogger(GroupManager.class);

	/**
	 * Load all groups without linking
	 * 
	 * @param tournament
	 * @param gruppenLocal
	 * @return
	 */
	protected GroupMap load(TurnierLocal turnierLocal) {
		Collection<GruppeLocal> gruppenLocal = turnierLocal.getGruppen();

		GroupMap groups = new GroupMap();

		// Load all groups
		for (GruppeLocal gruppeLocal : gruppenLocal) {
			SportGroup group = BOCreator.createGruppeBO(gruppeLocal);
			TournamentSystem tournamentSystem = BOCreator
					.createModusBO(gruppeLocal.getModus());
			group.setTournamentSystem(tournamentSystem);
			group.setMannschaften(new Vector<Team>());
			group.setMatches(new Vector<SportMatch>());

			groups.put(gruppeLocal, group);
		}

		return groups;
	}

	/**
	 * Links groups to all related objects
	 * 
	 * @param groups
	 * @param teams
	 * @param matches
	 */
	protected void loadLink(Tournament tournament, GroupMap groups,
			TeamMap teams, MatchMap matches) {
		for (GruppeLocal gruppeLocal : groups.keySet()) {
			SportGroup group = groups.get(gruppeLocal);

			group.setTournament(tournament);

			// Link teams to group
			for (MannschaftLocal mannschaftLocal : (Set<MannschaftLocal>) gruppeLocal
					.getMannschaften()) {
				Team team = teams.get(mannschaftLocal);
				group.addMannschaft(team);
			}

			// Link matches to groups
			for (SpielLocal spielLocal : (Set<SpielLocal>) gruppeLocal
					.getSpiele()) {
				SportMatch match = matches.get(spielLocal);
				group.addMatch(match);
			}
		}
	}

}
