package com.sport.analyzer;

import java.util.Vector;


import org.apache.log4j.Logger;

import com.sport.core.bo.Team;

/**
 * Holds the results after analyzing a group
 * 
 * @author Ronny
 * 
 */
public class GroupResult {

	private static final Logger LOG = Logger
			.getLogger(GroupResult.class);

	public Vector<GruppeErgebnisEntity> ergebnisDetails = new Vector<GruppeErgebnisEntity>();

	/**
	 * alle Spiele ausgespielt sind, k�nnen nur vorl�ufige Ergebnisse erstellt
	 * werden
	 */
	public boolean vorlaeufig = false;

	/**
	 * gibt Mannschaft nach Gruppenplatzierung wieder
	 */
	public Team getMannschaftByPlatz(int platz) {
		if (platz < 1) {
			LOG.error("Asked for place "+platz+" in group results. Value should be always larger than 0.", new Throwable());
			return null;
		}
		
		if (platz > ergebnisDetails.size()) {
			LOG.error("Asked for place "+platz+" for a group results that contain only "+ergebnisDetails.size()+" entries.", new Throwable());

			return null;
		}

		GruppeErgebnisEntity gruppeErgebnisEntry = (GruppeErgebnisEntity) ergebnisDetails
				.get(platz - 1);
		return gruppeErgebnisEntry.mannschaftBO;
	}

	public boolean isVorlaeufig() {
		return vorlaeufig;
	}


	public void setVorlaeufig(boolean vorlaeufig) {
		this.vorlaeufig = vorlaeufig;
	}

	public Vector<GruppeErgebnisEntity> getErgebnisDetails() {
		return ergebnisDetails;
	}

	public void setErgebnisDetails(Vector<GruppeErgebnisEntity> ergebnisDetails) {
		this.ergebnisDetails = ergebnisDetails;
	}
	
}
