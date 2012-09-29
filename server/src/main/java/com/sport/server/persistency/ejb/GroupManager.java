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

import com.sport.core.bo.SportGroup;
import com.sport.core.bo.Team;
import com.sport.server.ejb.HomeGetter;
import com.sport.server.ejb.interfaces.GruppeLocal;
import com.sport.server.ejb.interfaces.GruppeLocalHome;
import com.sport.server.ejb.interfaces.ModusLocal;
import com.sport.server.ejb.interfaces.TurnierLocal;

/**
 * Handles all persistency mappings for groups
 * 
 * @author Ronny
 * 
 */
public class GroupManager {

	private static final Logger LOG = Logger.getLogger(GroupManager.class);

	private TeamManager teamManager = new TeamManager();

	/**
	 * Stores all groups
	 * 
	 * @ejb:interface-method view-type="both"
	 */
	public void store(long tournamentId, SportGroup[] newGroups) {

		// List of all new groups -> others will be deleted
		Vector<Long> newGroupIds = new Vector<Long>();
		Vector<Team> teams = new Vector<Team>();

		try {
			GruppeLocalHome gruppeHome = HomeGetter.getGruppeHome();

			// aktuelles Turnier
			TurnierLocal turnierLocal = HomeGetter.getTurnierHome().findById(
					tournamentId);

			for (SportGroup group : newGroups) {
				GruppeLocal gruppeLocal = null;

				try {
					// Group already exists -> update it
					gruppeLocal = gruppeHome.findById(group.getId());
				} catch (FinderException e) {
					// Otherwise create it
					gruppeLocal = gruppeHome.create();
				}

				gruppeLocal.setSort(group.getSort());
				gruppeLocal.setName(group.getName());
				gruppeLocal.setFarbe(group.getColor());
				gruppeLocal.setTurnier(turnierLocal);
				ModusLocal modusLocal = HomeGetter.getModusHome().findById(
						group.getTournamentSystem().getId());
				gruppeLocal.setModus(modusLocal);

				// Store new ID -> to create connections
				group.setId(gruppeLocal.getId().longValue());
				for (Team team : group.getMannschaften())
					teams.add(team);

				newGroupIds.add(gruppeLocal.getId());
			}

			// Remove all old group that are in the new list
			GruppeLocal[] oldGroupsLocal = ((Set<GruppeLocal>) turnierLocal
					.getGruppen()).toArray(new GruppeLocal[0]);
			for (GruppeLocal gruppeLocal : oldGroupsLocal) {
				if (!newGroupIds.contains(gruppeLocal.getId()))
					gruppeLocal.remove();
			}

			teamManager.store(tournamentId, teams, true, false);
		} catch (RemoteException e) {
			LOG.error("Couldn't connect to server.", e);
		} catch (FinderException e) {
			LOG.error("Couldn't find tournament with ID: " + tournamentId, e);
		} catch (NamingException e) {
			LOG.error("Couldn't find tournament with ID: " + tournamentId, e);
		} catch (CreateException e) {
			LOG.error("Couldn't create new group.", e);
		} catch (EJBException e) {
			LOG.error("Couldn't remove group.", e);
		} catch (RemoveException e) {
			LOG.error("Couldn't remove group.", e);
		}
	}

}
