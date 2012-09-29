package com.sport.analyzer.impl;

import java.util.Comparator;

import com.sport.analyzer.GruppeErgebnisEntity;


/**
 * Sorts all teams within a group.
 * 
 * @author ronny
 * 
 */
public class GruppeErgebnisEntryComperator implements Comparator<GruppeErgebnisEntity> {
	public int compare(GruppeErgebnisEntity ent1, GruppeErgebnisEntity ent2) {
		if (ent1 == null)
			return -1;
		if (ent2 == null)
			return 1;

		// Spiele
		if (ent1.pspiele != ent2.pspiele) {
			return (new Integer(ent2.pspiele)).compareTo(new Integer(
					ent1.pspiele));
		}
		if (ent1.nspiele != ent2.nspiele) {
			return (new Integer(ent1.nspiele)).compareTo(new Integer(
					ent2.nspiele));
		}

		// Saetze
		if (ent1.psaetze != ent2.psaetze) {
			return (new Integer(ent2.psaetze)).compareTo(new Integer(
					ent1.psaetze));
		}
		if (ent1.nsaetze != ent2.nsaetze) {
			return (new Integer(ent1.nsaetze)).compareTo(new Integer(
					ent2.nsaetze));
		}

		// Direktbegegnung
		// MannschaftBO team1 = ent1.getMannschaftBO();
		// MannschaftBO team2 = ent2.getMannschaftBO();

		// Punktedifferenz
		if ((ent1.ppunkte - ent1.npunkte) != (ent2.ppunkte - ent2.npunkte)) {
			return (new Integer(ent2.ppunkte - ent2.npunkte))
					.compareTo(new Integer(ent1.ppunkte - ent1.npunkte));
		}

		// Punkte
		if (ent1.ppunkte != ent2.ppunkte) {
			return (new Integer(ent2.ppunkte)).compareTo(new Integer(
					ent1.ppunkte));
		}

		return (new Integer(ent1.npunkte)).compareTo(new Integer(
				ent2.npunkte));
	}
}