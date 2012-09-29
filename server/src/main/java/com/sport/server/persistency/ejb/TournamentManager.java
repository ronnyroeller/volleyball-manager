package com.sport.server.persistency.ejb;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sport.core.bo.Tournament;
import com.sport.core.bo.TournamentSystem;
import com.sport.core.bo.UserBO;
import com.sport.server.ejb.HomeGetter;
import com.sport.server.ejb.interfaces.ModusLocal;
import com.sport.server.ejb.interfaces.TurnierLocal;
import com.sport.server.ejb.interfaces.UserLocal;
import com.sport.server.ejb.session.BOCreator;

/**
 * Handles all persistency mappings for tournaments
 * 
 * @author Ronny
 * 
 */
public class TournamentManager {

	private static final Logger LOG = Logger.getLogger(TournamentManager.class);

	/**
	 * Finds all tournaments for a specific user
	 * 
	 * @param userId
	 * @return
	 */
	public Set<Tournament> findByUserId(long userId) {
		Set<Tournament> result = new HashSet<Tournament>();

		try {
			UserLocal userLocal = HomeGetter.getUserHome().findById(userId);

			for (TurnierLocal turnierLocal : (Set<TurnierLocal>) userLocal
					.getTurniere()) {
				Tournament turnierBO = BOCreator.createTurnierBO(turnierLocal);
				result.add(turnierBO);
			}
		} catch (RemoteException e) {
			LOG.error("Couldn't connect to server.", e);
		} catch (FinderException e) {
			LOG.error("Couldn't find user with ID: " + userId, e);
		} catch (NamingException e) {
			LOG.error("Couldn't find user with ID: " + userId, e);
		}

		return result;
	}

	/**
	 * Finds a tournament
	 */
	public Tournament findById(long tournamentId) {
		Tournament tournament = null;

		try {
			TurnierLocal turnierLocal = HomeGetter.getTurnierHome().findById(
					tournamentId);

			tournament = BOCreator.createTurnierBO(turnierLocal);
		} catch (RemoteException e) {
			LOG.error("Couldn't connect to server.", e);
		} catch (FinderException e) {
			LOG.error("Couldn't find tournament with ID: " + tournamentId, e);
		} catch (NamingException e) {
			LOG.error("Couldn't find tournament with ID: " + tournamentId, e);
		}

		return tournament;
	}

	/**
	 * Removes a tournament
	 */
	public void remove(long tournamentId) {
		TurnierLocal turnierLocal = null;

		try {
			turnierLocal = HomeGetter.getTurnierHome().findById(tournamentId);
			turnierLocal.remove();
		} catch (RemoteException e) {
			LOG.error("Couldn't connect to server.", e);
		} catch (FinderException e) {
			LOG.error("Couldn't find tournament with ID: " + tournamentId, e);
		} catch (NamingException e) {
			LOG.error("Couldn't find tournament with ID: " + tournamentId, e);
		} catch (EJBException e) {
			LOG.error("Couldn't remove tournament with ID: " + tournamentId, e);
		} catch (RemoveException e) {
			LOG.error("Couldn't remove tournament with ID: " + tournamentId, e);
		}
	}

	/**
	 * Finds tournament with a specific linkID
	 * 
	 * @param linkid
	 * @param noException
	 *            ... if false -> no StackTrace will be output if not found
	 * @return
	 */
	public Tournament findByLinkid(String linkId, boolean noStackTrace) {
		Tournament tournament = null;
		try {
			TurnierLocal turnierLocal = HomeGetter.getTurnierHome()
					.findByLinkid(linkId);

			tournament = BOCreator.createTurnierBO(turnierLocal);
		} catch (RemoteException e) {
			LOG.error("Couldn't connect to server.", e);
		} catch (FinderException e) {
			if (!noStackTrace)
				LOG.error("Couldn't find tournament with link-ID '" + linkId
						+ "'", e);
		} catch (NamingException e) {
			LOG.error("Couldn't find tournament with link-ID: " + linkId, e);
		}

		return tournament;
	}

	/**
	 * Saves a tournament. Creates a new one if it doesn't exist so far
	 */
	public Tournament store(Tournament tournament, UserBO user) {
		TurnierLocal turnierLocal = null;
		try {
			turnierLocal = HomeGetter.getTurnierHome().findById(
					tournament.getId());
		} catch (Exception e) {
			try {
				// Create tournament if it doesn't exist so far
				turnierLocal = HomeGetter.getTurnierHome().create();
				UserLocal userLocal = HomeGetter.getUserHome().findById(
						user.getId());
				turnierLocal.setUser(userLocal);

				// Store auto-generated ID
				tournament.setId(turnierLocal.getId().longValue());
			} catch (RemoteException e2) {
				LOG.error("Couldn't connect to server.", e2);
			} catch (CreateException e2) {
				LOG.error("Couldn't create new tournament.", e2);
			} catch (NamingException e2) {
				LOG.error("Couldn't save tournament.", e2);
			} catch (FinderException e2) {
				LOG.error("Couldn't find user.", e2);
			}
		}

		// Save to tournament if we found a local instance
		if (turnierLocal != null) {
			turnierLocal.setLinkid(tournament.getLinkid());
			turnierLocal.setName(tournament.getName());
			turnierLocal.setDatum(tournament.getDate());
			turnierLocal.setText(tournament.getText());
			turnierLocal.setPunkteprosatz(tournament.getPointsPerSet());
			turnierLocal.setPunkteprospiel(tournament.getPointsPerMatch());
			turnierLocal.setPunkteprounentschiedenspiel(tournament
					.getPointsPerTie());
			turnierLocal.setSpieldauer(tournament.getDurationMatch());
			turnierLocal.setBannerLink(tournament.getBannerLink());
			turnierLocal.setPausedauer(tournament.getDurationBreak());
			turnierLocal.setBeamerumschaltzeit(tournament
					.getDurationProjectorSwitch());
			turnierLocal
					.setSpielplangesperrt(tournament.getSpielplangesperrt());
		}

		return tournament;
	}

	/**
	 * Returns all tournament systems
	 */
	public Set<TournamentSystem> getTournamentSystems() {
		Set<TournamentSystem> tournamentSystems = new HashSet<TournamentSystem>();
		try {
			Collection<ModusLocal> modiLocal = HomeGetter.getModusHome()
					.findAll();

			for (ModusLocal modusLocal : modiLocal) {
				TournamentSystem modusBO = BOCreator.createModusBO(modusLocal);
				tournamentSystems.add(modusBO);
			}
		} catch (RemoteException e) {
			LOG.error("Couldn't connect to server.", e);
		} catch (FinderException e) {
			LOG.error("Can't load tournament systems.", e);
		} catch (NamingException e) {
			LOG.error("Can't load tournament systems.", e);
		}

		return tournamentSystems;
	}

}
