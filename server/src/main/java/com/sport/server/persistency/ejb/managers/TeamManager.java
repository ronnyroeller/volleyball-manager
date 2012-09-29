package com.sport.server.persistency.ejb.managers;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.sport.core.bo.Team;
import com.sport.server.ejb.interfaces.GruppeLocal;
import com.sport.server.ejb.interfaces.MannschaftLocal;
import com.sport.server.ejb.interfaces.SpielLocal;
import com.sport.server.ejb.interfaces.TurnierLocal;
import com.sport.server.ejb.session.BOCreator;
import com.sport.server.persistency.ejb.maps.GroupMap;
import com.sport.server.persistency.ejb.maps.MatchMap;
import com.sport.server.persistency.ejb.maps.TeamMap;

/**
 * Loads and stores teams
 * 
 * @author Ronny
 * 
 */
public class TeamManager {

	private static final Logger LOG = Logger.getLogger(TeamManager.class);

	/**
	 * Load all teams without any linking
	 * 
	 * @param turnierLocal
	 * @param gruppenLocal
	 */
	protected TeamMap load(TurnierLocal turnierLocal) {
		TeamMap teams = new TeamMap();

		// Collect all teams
		Set<MannschaftLocal> teamsLocal = new HashSet<MannschaftLocal>();

		// Load all teams
		Collection<GruppeLocal> gruppenLocal = turnierLocal.getGruppen();
		for (GruppeLocal gruppeLocal : gruppenLocal)
			teamsLocal.addAll((Set<MannschaftLocal>) gruppeLocal
					.getMannschaften());

		// Find all teams without groups (used for rankings and before
		// assigning to groups)
		if (turnierLocal.getMannschaften() != null)
			teamsLocal.addAll((Set<MannschaftLocal>) turnierLocal
					.getMannschaften());

		for (MannschaftLocal mannschaftLocal : teamsLocal) {
			Team team = BOCreator.createMannschaftBO(mannschaftLocal);
			teams.put(mannschaftLocal, team);
			LOG
					.debug("Loaded team " + team.getId() + " name="
							+ team.getName());
		}

		return teams;
	}

	/**
	 * Links logical teams to all related objects
	 * 
	 * @param groups
	 * @param teams
	 * @param matches
	 */
	protected void link(GroupMap groups, TeamMap teams, MatchMap matches) {
		// Link logical teams
		for (MannschaftLocal teamLocal : teams.keySet()) {
			Team team = teams.get(teamLocal);

			GruppeLocal logGroup = teamLocal.getLoggruppe();
			SpielLocal logMatch = teamLocal.getLogspiel();
			if (logGroup != null)
				team.setLoggruppeBO(groups.get(logGroup));
			if (logMatch != null)
				team.setLogspielBO(matches.get(logMatch));

			// Check that there is no corrupt data
			if ((team.getName() == null) && (team.getLoggruppeBO() == null)
					&& (team.getLogspielBO() == null)) {
				String logGroupId = (teamLocal.getLoggruppe() != null) ? teamLocal
						.getLoggruppe().getId().toString()
						: "null";
				String logMatchId = (teamLocal.getLogspiel() != null) ? teamLocal
						.getLogspiel().getId().toString()
						: "null";

				LOG
						.error("Corrupt data: Found a team that has no name and is not linked to a group or a match: ID="
								+ teamLocal.getId()
								+ ", name="
								+ teamLocal.getName()
								+ ", logGroup="
								+ logGroupId + ", logMatch=" + logMatchId);
			}
		}
	}

}
