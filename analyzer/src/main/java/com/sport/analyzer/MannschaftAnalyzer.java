package com.sport.analyzer;

import com.sport.core.bo.Team;

public interface MannschaftAnalyzer {

	/**
	 * Sucht durch logSpiel-Verbindungen bis eine loggruppe-Mannschaft oder phy.
	 * Mannschaft oder logSpiel-Mannschaft (die aber Gruppenmitglied ist)
	 * gefunden ist! Wichtig fuer Berechnung der Gruppen-Ergebnisse.
	 * 
	 * @return
	 */
	public abstract Team getRelGruppeMannschaftBO(
			Team mannschaftBO);

	/**
	 * Sucht durch logVerbindungen nach phy. Mannschaft
	 * 
	 * @return
	 */
	public abstract Team getRelMannschaftBO(Team mannschaftBO);

	/**
	 * Sucht durch logVerbindungen
	 * 
	 * @param force ... stopt nicht, wenn noch nicht
	 * auswertbar
	 * @param onestep ... sucht nicht bis zur phy. Mannschaft, sondern nur
	 * einen Schritt nach unten
	 * 
	 * @return
	 */
	public abstract Team getRelMannschaftBOComplex(
			Team mannschaftBO, boolean force, boolean onestep);

	/**
	 * Returns the real name of a team
	 * 
	 * @param mannschaftBO
	 * @return
	 */
	public abstract String getRelName(Team mannschaftBO);

	/**
	 * Provides complete trace of the team
	 * 
	 * @param mannschaftBO
	 * @return
	 */
	public abstract String getRelNameTrace(Team mannschaftBO);

	/**
	 * Returns the name which is shown in the GUI
	 * 
	 * @return
	 */
	public abstract String getName(Team mannschaftBO);
	
	/**
	 * Returns the name of the team within a group (without further resolving
	 * 
	 * @param mannschaftBO
	 * @return
	 */
	public abstract String getBaseName(Team mannschaftBO);
	
}
