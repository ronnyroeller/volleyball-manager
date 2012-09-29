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

import com.sport.core.bo.Team;
import com.sport.server.ejb.HomeGetter;
import com.sport.server.ejb.interfaces.GruppeLocal;
import com.sport.server.ejb.interfaces.GruppeLocalHome;
import com.sport.server.ejb.interfaces.MannschaftLocal;
import com.sport.server.ejb.interfaces.MannschaftLocalHome;
import com.sport.server.ejb.interfaces.SpielLocal;
import com.sport.server.ejb.interfaces.SpielLocalHome;
import com.sport.server.ejb.interfaces.TurnierLocal;
import com.sport.server.ejb.interfaces.TurnierLocalHome;

/**
 * Handles all persistency mappings for teams
 * 
 * @author Ronny
 * 
 */
public class TeamManager {

	private static final Logger LOG = Logger.getLogger(TeamManager.class);

	/**
	 * Stores teams for a tournament
	 * 
	 * @param tournamentId
	 * @param newTeams
	 * @param isStoreLogTeams
	 *            Werden auch log. Mannschaften mit behandelt
	 * @param alsoAuslosung
	 *            Auch Mannschaften, die nicht in Gruppen sind (auf Auslosung
	 *            warten)
	 */
	public void store(long tournamentId, Vector<Team> newTeams,
			boolean isStoreLogTeams, boolean alsoAuslosungen) {

		// List of all new teams -> others will be deleted
		Vector<Long> newTeamsIds = new Vector<Long>();

		try {
			MannschaftLocalHome mannschaftHome = HomeGetter.getMannschaftHome();
			GruppeLocalHome gruppeHome = HomeGetter.getGruppeHome();
			SpielLocalHome spielHome = HomeGetter.getSpielHome();
			TurnierLocalHome turnierHome = HomeGetter.getTurnierHome();

			// fuer Turnier-Verlinkungen in Mannschaften
			TurnierLocal turnierLocal = turnierHome.findById(tournamentId);

			for (Team team : newTeams) {
				MannschaftLocal mannschaftLocal = null;

				try {
					// Does team already exist? -> update it
					mannschaftLocal = mannschaftHome.findById(team.getId());
				} catch (Exception e) {
					// Create team
					mannschaftLocal = mannschaftHome.create();
				}

				// Set general attributes
				mannschaftLocal.setTurnier(turnierLocal);
				mannschaftLocal.setName(team.getNamePhy());
				mannschaftLocal.setSort(team.getSort());
				mannschaftLocal.setLogsort(0);

				// Link with group
				GruppeLocal gruppeLocal = null;
				if (team.getGruppeBO() != null) {
					gruppeLocal = gruppeHome.findById(team.getGruppeBO()
							.getId());
				}
				mannschaftLocal.setGruppe(gruppeLocal);

				// Link with log group
				if (team.getLoggruppeBO() != null) {
					try {
						GruppeLocal loggruppeLocal = gruppeHome.findById(team
								.getLoggruppeBO().getId());
						mannschaftLocal.setLoggruppe(loggruppeLocal);
						mannschaftLocal.setLogsort(team.getLogsort());
					} catch (FinderException e) {
						// Group was deleted earlier
						mannschaftLocal.setName("Logical group is invalid!");
					}
				} else {
					mannschaftLocal.setLoggruppe(null);
				}

				if (team.getLogspielBO() != null) {
					try {
						SpielLocal logspielLocal = spielHome.findById(team
								.getLogspielBO().getId());
						mannschaftLocal.setLogspiel(logspielLocal);
						mannschaftLocal.setLogsort(team.getLogsort());
					} catch (FinderException e) {
						// Match was deleted earlier
						mannschaftLocal.setName("Logical match is invalid!");
					}
				} else {
					mannschaftLocal.setLogspiel(null);
				}

				newTeamsIds.add(mannschaftLocal.getId());
			}

			// Remove all old teams that are linked to groups and not in the
			// list of new teams
			for (GruppeLocal gruppeLocal : (Set<GruppeLocal>) turnierLocal
					.getGruppen()) {
				MannschaftLocal[] oldTeamsLocal = ((Set<MannschaftLocal>) gruppeLocal
								.getMannschaften()).toArray(new MannschaftLocal[0]);
				for (MannschaftLocal mannschaftLocal : oldTeamsLocal) {
					// Remove the team if: a) Not updated and not log team
					// (except is forced)
					if (!newTeamsIds.contains(mannschaftLocal.getId())
							&& (isStoreLogTeams || !isLogTeam(mannschaftLocal)))
						mannschaftLocal.remove();
				}
			}

			// Remove all old teams that are not linked to groups
			if (alsoAuslosungen) {
				MannschaftLocal[] oldTeamsLocal = ((Set<MannschaftLocal>) turnierLocal
						.getMannschaften()).toArray(new MannschaftLocal[0]);
				for (MannschaftLocal mannschaftLocal : oldTeamsLocal) {
					if (!newTeamsIds.contains(mannschaftLocal.getId())
							&& (isStoreLogTeams || !isLogTeam(mannschaftLocal)))
						mannschaftLocal.remove();
				}
			}
		} catch (RemoteException e) {
			LOG.error("Couldn't connect to server.", e);
		} catch (FinderException e) {
			LOG.error("Couldn't find tournament with ID: " + tournamentId, e);
		} catch (NamingException e) {
			LOG.error("Couldn't find tournament with ID: " + tournamentId, e);
		} catch (CreateException e) {
			LOG.error("Couldn't create new team.", e);
		} catch (EJBException e) {
			LOG.error("Couldn't remove team.", e);
		} catch (RemoveException e) {
			LOG.error("Couldn't remove team.", e);
		}

	}

	/**
	 * Checks if a team is a logical team
	 * 
	 * @param mannschaftLocal
	 * @return
	 */
	public static boolean isLogTeam(MannschaftLocal mannschaftLocal) {
		return (mannschaftLocal.getLoggruppe() != null)
				|| (mannschaftLocal.getLogspiel() != null);
	}

	/**
	 * Checks if a team is a logical team
	 * 
	 * @param mannschaftLocal
	 * @return
	 */
	public static boolean isLogTeam(Team team) {
		return (team.getLoggruppeBO() != null)
				|| (team.getLogspielBO() != null);
	}

}
