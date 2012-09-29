package com.sport.server.persistency.ejb.managers;

import java.util.Set;

import org.apache.log4j.Logger;

import com.sport.core.bo.Ranking;
import com.sport.core.bo.Team;
import com.sport.server.ejb.interfaces.PlatzierungLocal;
import com.sport.server.ejb.interfaces.TurnierLocal;
import com.sport.server.ejb.session.BOCreator;
import com.sport.server.persistency.ejb.maps.RankingMap;
import com.sport.server.persistency.ejb.maps.TeamMap;

/**
 * Loads and stores rankings
 * 
 * @author Ronny
 * 
 */
public class RankingManager {

	private static final Logger LOG = Logger.getLogger(RankingManager.class);

	/**
	 * Load all rankings without any linking
	 * 
	 * @param turnierLocal
	 */
	protected RankingMap load(TurnierLocal turnierLocal) {
		RankingMap rankings = new RankingMap();

		// Load all fields
		for (PlatzierungLocal platzierungLocal : (Set<PlatzierungLocal>) turnierLocal
				.getPlatzierungen()) {
			Ranking ranking = BOCreator.createPlatzierungBO(platzierungLocal);

			rankings.put(platzierungLocal, ranking);
		}

		return rankings;
	}

	/**
	 * Links rankings to all related objects
	 * 
	 * @param rankings
	 * @param teams
	 */
	protected void link(RankingMap rankings, TeamMap teams) {
		// Iterate over all rankings
		for (PlatzierungLocal platzierungLocal : rankings.keySet()) {
			Ranking ranking = rankings.get(platzierungLocal);

			// Connect team
			if (platzierungLocal.getMannschaft() != null) {
				Team team = teams.get(platzierungLocal.getMannschaft());

				if (team == null)
					LOG.error("Couldn't find team " + platzierungLocal.getMannschaft().getId()
							+ ", which should be linked to ranking "
							+ ranking.getId() + " (rank " + ranking.getRank()
							+ ")");

				ranking.setMannschaft(team);
			}
		}
	}

}
