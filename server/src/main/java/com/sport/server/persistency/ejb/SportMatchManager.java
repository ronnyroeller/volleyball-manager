package com.sport.server.persistency.ejb;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.NoSuchObjectLocalException;
import javax.ejb.RemoveException;
import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.sport.analyzer.MannschaftAnalyzer;
import com.sport.analyzer.impl.MannschaftAnalyzerImpl;
import com.sport.core.bo.SetResult;
import com.sport.core.bo.SportMatch;
import com.sport.core.bo.Team;
import com.sport.server.ejb.HomeGetter;
import com.sport.server.ejb.interfaces.GruppeLocal;
import com.sport.server.ejb.interfaces.GruppeLocalHome;
import com.sport.server.ejb.interfaces.MannschaftLocal;
import com.sport.server.ejb.interfaces.MannschaftLocalHome;
import com.sport.server.ejb.interfaces.SatzLocal;
import com.sport.server.ejb.interfaces.SpielLocal;
import com.sport.server.ejb.interfaces.SpielLocalHome;
import com.sport.server.ejb.interfaces.SpielplatzLocal;
import com.sport.server.ejb.interfaces.SpielplatzLocalHome;
import com.sport.server.ejb.interfaces.TurnierLocal;

/**
 * Handles all persistency mappings for matches
 * 
 * @author Ronny
 * 
 */
public class SportMatchManager {

	private static final Logger LOG = Logger.getLogger(SportMatchManager.class);

	private MannschaftAnalyzer ma = MannschaftAnalyzerImpl.getInstance();

	/**
	 * Deletes all matches of a tournament and writes the new onces back
	 */
	public void setSpieleByTurnierid(long tournamentId, SportMatch[] matches) {
		try {
			SpielLocalHome spielHome = HomeGetter.getSpielHome();
			GruppeLocalHome gruppeHome = HomeGetter.getGruppeHome();
			SpielplatzLocalHome spielplatzHome = HomeGetter.getSpielplatzHome();
			MannschaftLocalHome mannschaftHome = HomeGetter.getMannschaftHome();

			TurnierLocal turnierLocal = HomeGetter.getTurnierHome().findById(
					tournamentId);

			// Delete all matches
			for (GruppeLocal gruppeLocal : (Set<GruppeLocal>) turnierLocal
					.getGruppen()) {
				SpielLocal[] oldMatches = ((Set<SpielLocal>) gruppeLocal
						.getSpiele()).toArray(new SpielLocal[0]);
				for (SpielLocal spielLocal : oldMatches) {
					try {
						spielLocal.remove();
					} catch (NoSuchObjectLocalException e) {
						// Not clear why this happens!
						LOG
								.debug("Try to remove match that was already removed.");
					}
				}
			}

			// Stores to which matches a team is linked. The relation can't be
			// set directly because the referring match may not be stored.
			// Type: MannschaftLocal -> match-ID
			Map<MannschaftLocal, Long> logSpielRelations = new HashMap<MannschaftLocal, Long>();

			// Stores mapping of all matches
			// Type: match-ID -> spielLocal
			Map<Long, SpielLocal> matchesMap = new HashMap<Long, SpielLocal>();

			// Iterate over all matches
			for (SportMatch match : matches) {
				SpielplatzLocal spielplatzLocal = spielplatzHome.findById(match
						.getField().getId());
				GruppeLocal gruppeLocal = gruppeHome.findById(match.getGroup()
						.getId());

				MannschaftLocal mannschaft1Local = null;
				MannschaftLocal mannschaft2Local = null;

				// Is first team a LogSpiel?
				Team team1 = match.getTeam1();
				if (isDependsOnMatch(team1)) {
					SportMatch logSpielBO = team1.getLogspielBO();
					mannschaft1Local = mannschaftHome.create();
					mannschaft1Local.setLogsort(team1.getLogsort());
					mannschaft1Local.setTurnier(turnierLocal);

					logSpielRelations.put(mannschaft1Local, logSpielBO.getId());
				} else {
					mannschaft1Local = HomeGetter.getMannschaftHome().findById(
							team1.getId());
				}

				// Is second team a LogSpiel?
				if (isDependsOnMatch(match.getTeam2())) {
					SportMatch logSpielBO = match.getTeam2().getLogspielBO();
					mannschaft2Local = mannschaftHome.create();
					mannschaft2Local.setLogsort(match.getTeam2().getLogsort());
					mannschaft2Local.setTurnier(turnierLocal);

					logSpielRelations.put(mannschaft2Local, logSpielBO.getId());
				} else {
					mannschaft2Local = HomeGetter.getMannschaftHome().findById(
							match.getTeam2().getId());
				}

				if (LOG.isDebugEnabled())
					LOG.debug("Match " + match.getId() + ": team1="
							+ ma.getRelName(match.getTeam1()) + ", team2="
							+ ma.getRelName(match.getTeam2()));

				MannschaftLocal schiedsrichterLocal = null;
				if (match.getReferee() != null) {
					// Is referee a LogSpiel?
					if (isDependsOnMatch(match.getReferee())) {
						SportMatch logSpielBO = match.getReferee()
								.getLogspielBO();
						schiedsrichterLocal = mannschaftHome.create();
						schiedsrichterLocal.setLogsort(match.getReferee()
								.getLogsort());
						schiedsrichterLocal.setTurnier(turnierLocal);

						logSpielRelations.put(schiedsrichterLocal, logSpielBO
								.getId());
					} else {
						schiedsrichterLocal = mannschaftHome.findById(match
								.getReferee().getId());
					}
				}

				// Create new match
				SpielLocal spielLocal = spielHome.create();

				matchesMap.put(match.getId(), spielLocal);

				// Store general attribute
				spielLocal.setVondatum(match.getStartDate());
				spielLocal.setBisdatum(match.getEndDate());
				spielLocal.setSpielplatz(spielplatzLocal);
				spielLocal.setGruppe(gruppeLocal);
				spielLocal.setMannschaft1(mannschaft1Local);
				spielLocal.setMannschaft2(mannschaft2Local);
				spielLocal.setSchiedsrichter(schiedsrichterLocal);

				if (LOG.isDebugEnabled())
					logMatch("Stored (1)", spielLocal);

				// Store sets
				if (match.getSetResults() != null) {
					for (SetResult satzBO : match.getSetResults()) {
						SatzLocal satzLocal = HomeGetter.getSatzHome().create();
						satzLocal.setSatznr(satzBO.getSetNr());
						satzLocal.setPunkte1(satzBO.getPoints1());
						satzLocal.setPunkte2(satzBO.getPoints2());
						satzLocal.setSpiel(spielLocal);
					}
				}
			}

			// Now all matches are created -> we can write all teams that depend
			// on matches
			for (MannschaftLocal mannschaftLocal : logSpielRelations.keySet()) {
				long logMatchId = logSpielRelations.get(mannschaftLocal);
				SpielLocal logSpiel = matchesMap.get(logMatchId);

				if (logSpiel == null)
					LOG.error("Team " + mannschaftLocal.getId()
							+ " refers to non-existing match " + logMatchId
							+ ". Known matches are: "
							+ StringUtils.join(matchesMap.keySet(), ", "));

				mannschaftLocal.setLogspiel(logSpiel);

				if (LOG.isDebugEnabled())
					logMatch("Connect match to team", logSpiel);
			}
		} catch (RemoteException e) {
			LOG.error("Couldn't connect to server.", e);
		} catch (FinderException e) {
			LOG.error("Couldn't find tournament with ID: " + tournamentId, e);
		} catch (NamingException e) {
			LOG.error("Couldn't find tournament with ID: " + tournamentId, e);
		} catch (CreateException e) {
			LOG.error("Couldn't create new match.", e);
		} catch (EJBException e) {
			LOG.error("Couldn't remove match.", e);
		} catch (RemoveException e) {
			LOG.error("Couldn't remove match.", e);
		}

	}

	/**
	 * Checks if a team depends on a match (logSpiel)
	 * 
	 * @param team
	 * @return
	 */
	private boolean isDependsOnMatch(Team team) {
		SportMatch logSpielBO = team.getLogspielBO();
		return (logSpielBO != null) && (team.getGruppeBO() == null);
	}

	/**
	 * Loads a team based on its ID
	 * 
	 * @param teamId
	 * @return
	 * @throws RemoteException
	 * @throws FinderException
	 * @throws RemoteException
	 * @throws FinderException
	 * @throws NamingException
	 * @throws NamingException
	 */
	private MannschaftLocal findTeamById(Long teamId) throws RemoteException,
			FinderException, NamingException {
		try {
			return HomeGetter.getMannschaftHome().findById(teamId);
		} catch (RemoteException e) {
			LOG.error("Couldn't connect to server.", e);
			throw e;
		} catch (FinderException e) {
			LOG.error("Couldn't find team with ID: " + teamId);
			throw e;
		} catch (NamingException e) {
			LOG.error("Couldn't find team with ID: " + teamId);
			throw e;
		}
	}

	/**
	 * Logs this match
	 * 
	 * @param spielLocal
	 */
	public static void logMatch(String str, SpielLocal spielLocal) {
		String teamName1 = getPrintName(spielLocal.getMannschaft1());
		String teamName2 = getPrintName(spielLocal.getMannschaft2());
		String refereeName = "none";
		if (spielLocal.getSchiedsrichter() != null)
			refereeName = getPrintName(spielLocal.getSchiedsrichter());

		StringBuffer debug = new StringBuffer();
		debug.append(str);
		debug.append(" match ").append(spielLocal.getId());
		debug.append(", start=").append(spielLocal.getVondatum().toString());
		debug.append(", end=").append(spielLocal.getBisdatum());
		debug.append(", field=").append(spielLocal.getSpielplatz().getName());
		debug.append(", group=").append(spielLocal.getGruppe().getName());
		debug.append(", team1=").append(teamName1);
		debug.append(", team2=").append(teamName2);
		debug.append(", referee=").append(refereeName);
		LOG.debug(debug);
	}

	/**
	 * Creates the print name for a physical or logical team
	 * 
	 * @param mannschaft
	 * @return
	 */
	private static String getPrintName(MannschaftLocal mannschaft) {
		String teamName = mannschaft.getName();

		// Is team1 a logical team?
		if (teamName == null) {
			teamName = mannschaft.getLogsort() + " - ";
			SpielLocal logspiel = mannschaft.getLogspiel();
			if (mannschaft.getLoggruppe() != null)
				teamName += mannschaft.getLoggruppe().getName();
			else if (logspiel != null) {
				teamName += logspiel.getVondatum() + "/"
						+ logspiel.getSpielplatz().getName();
			} else {
				teamName = "unknown";
			}
		}
		return teamName;
	}

}
