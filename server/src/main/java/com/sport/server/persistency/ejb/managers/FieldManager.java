package com.sport.server.persistency.ejb.managers;

import java.rmi.RemoteException;
import java.util.Set;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sport.core.bo.Field;
import com.sport.core.bo.TournamentHolder;
import com.sport.server.ejb.HomeGetter;
import com.sport.server.ejb.interfaces.SpielplatzLocal;
import com.sport.server.ejb.interfaces.SpielplatzLocalHome;
import com.sport.server.ejb.interfaces.TurnierLocal;
import com.sport.server.ejb.session.BOCreator;
import com.sport.server.persistency.ejb.maps.FieldMap;

/**
 * Loads and stores fields
 * 
 * @author Ronny
 *
 */
public class FieldManager {

	private static final Logger LOG = Logger.getLogger(FieldManager.class);

	/**
	 * Load all fields without any linking
	 * 
	 * @param turnierLocal
	 */
	protected FieldMap load(TurnierLocal turnierLocal) {
		FieldMap fields = new FieldMap();

		// Load all fields
		for (SpielplatzLocal spielplatzLocal : (Set<SpielplatzLocal>) turnierLocal
				.getSpielplaetze()) {
			Field spielplatzBO = BOCreator.createSpielplatzBO(spielplatzLocal);
			fields.put(spielplatzLocal, spielplatzBO);
		}

		return fields;
	}

	/**
	 * Stores all fields for a tournament
	 */
	public void store(TurnierLocal turnierLocal, TournamentHolder tournamentHolder) {

		// List of all new fields -> others will be deleted
		Vector<Long> newFieldIds = new Vector<Long>();

		try {
			SpielplatzLocalHome spielplatzHome = HomeGetter.getSpielplatzHome();

			// Store/update each new field
			for (Field spielplatzBO : tournamentHolder.getFields()) {
				SpielplatzLocal spielplatzLocal = null;

				try {
					// Field already exists -> update it
					spielplatzLocal = spielplatzHome.findById(spielplatzBO
							.getId());
				} catch (FinderException e) {
					// Create new field
					spielplatzLocal = spielplatzHome.create();
				}

				spielplatzLocal.setName(spielplatzBO.getName());
				// Not used in application -> remove from BO but kept in
				// persistency layer for backwards-compatibility
				spielplatzLocal.setFarbe("#FFFFFF");
				spielplatzLocal.setTurnier(turnierLocal);

				newFieldIds.add(spielplatzLocal.getId());
			}

			// Remove all old fields that were not in the list of new fields
			SpielplatzLocal[] oldFieldsLocal = ((Set<SpielplatzLocal>) turnierLocal
					.getSpielplaetze()).toArray(new SpielplatzLocal[0]);
			for (SpielplatzLocal oldFieldLocal : oldFieldsLocal) {
				// Not updated -> then remove it
				Long oldFieldId = oldFieldLocal.getId();
				if (!newFieldIds.contains(oldFieldId))
					oldFieldLocal.remove();
			}
		} catch (RemoteException e) {
			LOG.error("Couldn't connect to server.", e);
		} catch (CreateException e) {
			LOG.error("Couldn't create new field.", e);
		} catch (EJBException e) {
			LOG.error("Couldn't remove field.", e);
		} catch (RemoveException e) {
			LOG.error("Couldn't remove field.", e);
		} catch (NamingException e) {
			LOG.error("Couldn't find Field Home.", e);
		}

	}

}
