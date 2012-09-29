package com.sport.server.persistency.ejb;

import java.rmi.RemoteException;
import java.util.Set;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sport.core.bo.Ranking;
import com.sport.core.bo.Team;
import com.sport.server.ejb.HomeGetter;
import com.sport.server.ejb.interfaces.GruppeLocal;
import com.sport.server.ejb.interfaces.MannschaftLocal;
import com.sport.server.ejb.interfaces.PlatzierungLocal;
import com.sport.server.ejb.interfaces.PlatzierungLocalHome;
import com.sport.server.ejb.interfaces.SpielLocal;
import com.sport.server.ejb.interfaces.TurnierLocal;

/**
 * Handles all persistency mappings for rankings
 * 
 * @author Ronny
 * 
 */
public class RankingManager {

	private static final Logger LOG = Logger.getLogger(RankingManager.class);

	private SportMatchManager sportMatchManager = new SportMatchManager();

	/**
	 * Saves all rankings for a tournament
	 */
	public void store(long tournamentId, Ranking[] newRankings) {

		// List of all new rankings -> others will be deleted
		Vector<Long> newRankingIds = new Vector<Long>();

		try {
			// Find tournament
			TurnierLocal turnierLocal = HomeGetter.getTurnierHome().findById(
					tournamentId);

			PlatzierungLocalHome platzierungHome = HomeGetter
					.getPlatzierungHome();

			for (Ranking ranking : newRankings) {
				// Only save rankings that have team
				if (ranking.getMannschaft() != null) {
					PlatzierungLocal platzierungLocal = null;

					try {
						// Ranking already exists -> update it
						platzierungLocal = platzierungHome.findById(ranking
								.getId());
					} catch (FinderException e) {
						// Create a new ranking
						platzierungLocal = platzierungHome.create();
					}

					platzierungLocal.setPlatznr(ranking.getRank());
					platzierungLocal.setTurnier(turnierLocal);

					// Always delete all logical teams and recreate them
					if (platzierungLocal.getMannschaft() != null)
						platzierungLocal.getMannschaft().remove();

					if (ranking.getMannschaft() != null) {
						Team team = ranking.getMannschaft();

						// Create team
						MannschaftLocal mannschaftLocal = HomeGetter
								.getMannschaftHome().create();
						mannschaftLocal.setTurnier(turnierLocal);

						mannschaftLocal.setLogsort(team.getLogsort());

						// Ranking links to a group? e.g. '1st of group A'
						if (team.getLoggruppeBO() != null) {
							try {
								long logGroupId = team.getLoggruppeBO().getId();
								GruppeLocal loggruppeLocal = HomeGetter
										.getGruppeHome().findById(logGroupId);
								mannschaftLocal.setLoggruppe(loggruppeLocal);
							} catch (FinderException e) {
								// Referencing group was deleted earlier
								LOG.error("Logical group is invalid.", e);
							}
						} else {
							// Ranking links to a match? e.g. 'Winner of match
							// A:B'
							try {
								long logMatchId = team.getLogspielBO().getId();
								SpielLocal logspielLocal = HomeGetter
										.getSpielHome().findById(logMatchId);
								mannschaftLocal.setLogspiel(logspielLocal);
							} catch (FinderException e) {
								// Referencing match was deleted earlier
								LOG.error("Logical match is invalid.", e);
							}
						}
						platzierungLocal.setMannschaft(mannschaftLocal);
					}

					newRankingIds.add(platzierungLocal.getId());
				}
			}

			// Remove all old rankings that were not in the list of new rankings
			PlatzierungLocal[] oldRankingsLocal = ((Set<PlatzierungLocal>) turnierLocal
					.getPlatzierungen()).toArray(new PlatzierungLocal[0]);
			for (PlatzierungLocal platzierungLocal : oldRankingsLocal) {
				Long rankingId = platzierungLocal.getId();
				// Not updated -> then remove it
				if (!newRankingIds.contains(rankingId))
					platzierungLocal.remove();
			}
		} catch (RemoteException e) {
			LOG.error("Couldn't connect to server.", e);
		} catch (FinderException e) {
			LOG.error("Couldn't find tournament with ID: " + tournamentId, e);
		} catch (NamingException e) {
			LOG.error("Couldn't find tournament with ID: " + tournamentId, e);
		} catch (CreateException e) {
			LOG.error("Couldn't create new ranking.", e);
		} catch (EJBException e) {
			LOG.error("Couldn't remove ranking.", e);
		} catch (RemoveException e) {
			LOG.error("Couldn't remove ranking.", e);
		}
	}

}
