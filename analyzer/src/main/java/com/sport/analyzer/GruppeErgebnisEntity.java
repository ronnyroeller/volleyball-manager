/*
 * Created on 17.06.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.sport.analyzer;

import java.io.Serializable;

import com.sport.core.bo.Team;


/**
 * Saves temporarily ErgebnisDetails
 * 
 * @author ronny
 *
 * @jboss-net.xml-schema
 *   		 data-object="true"
 *   		 urn="jvolley:GruppeErgebnisEntity"
 */
public class GruppeErgebnisEntity implements Serializable {

	private static final long serialVersionUID = -1959146187233860059L;

	public Team mannschaftBO = null;
	public int spiele = 0; // Spiele gesamt
	public int pspiele = 0; // Punkte fuer gewonnene Spiele
	public int nspiele = 0; // Punkte fuer verlorenen Spiele
	public int psaetze = 0;
	public int nsaetze = 0;
	public int ppunkte = 0;
	public int npunkte = 0;

	public GruppeErgebnisEntity() {
	}

	public GruppeErgebnisEntity(Team mannschaftBO) {
		this.mannschaftBO = mannschaftBO;
	}

	/**
	 * @return Gesamtanzahl aller Spiele
	 */
	public int getSpiele() {
		return spiele;
	}

	// nie 0 zur�ckgeben! f�r divisionen
	public int getSpieleNotNull() {
		if (getSpiele() == 0) {
			return 1;
		}
		return getSpiele();
	}

	// Punktedifferenz
	public int getDiffpunkte() {
		return ppunkte - npunkte;
	}

	/**
	 * @return
	 */
	public Team getMannschaftBO() {
		return mannschaftBO;
	}

	/**
	 * @return number of lost points
	 */
	public int getNpunkte() {
		return npunkte;
	}

	/**
	 * @return number of lost sets
	 */
	public int getNsaetze() {
		return nsaetze;
	}

	/**
	 * @return number of lost matches
	 */
	public int getNspiele() {
		return nspiele;
	}

	/**
	 * @return number of won points
	 */
	public int getPpunkte() {
		return ppunkte;
	}

	/**
	 * @return number of won sets
	 */
	public int getPsaetze() {
		return psaetze;
	}

	/**
	 * @return number of won matches
	 */
	public int getPspiele() {
		return pspiele;
	}

	public float getNpunkteAvg() {
		return ((float) getNpunkte()) / getSpieleNotNull();
	}

	public float getNsaetzeAvg() {
		return ((float) getNsaetze()) / getSpieleNotNull();
	}

	public float getNspieleAvg() {
		return ((float) getNspiele()) / getSpieleNotNull();
	}

	public float getPpunkteAvg() {
		return ((float) getPpunkte()) / getSpieleNotNull();
	}

	public float getPsaetzeAvg() {
		return ((float) getPsaetze()) / getSpieleNotNull();
	}

	public float getPspieleAvg() {
		return ((float) getPspiele()) / getSpieleNotNull();
	}

	public float getDiffpunkteAvg() {
		return ((float) getDiffpunkte()) / getSpieleNotNull();
	}

	/**
	 * @param mannschaftBO
	 */
	public void setMannschaftBO(Team mannschaftBO) {
		this.mannschaftBO = mannschaftBO;
	}

	/**
	 * @param i
	 */
	public void setNpunkte(int i) {
		npunkte = i;
	}

	/**
	 * @param i
	 */
	public void setNsaetze(int i) {
		nsaetze = i;
	}

	/**
	 * @param i
	 */
	public void setNspiele(int i) {
		nspiele = i;
	}

	/**
	 * @param i
	 */
	public void setPpunkte(int i) {
		ppunkte = i;
	}

	/**
	 * @param i
	 */
	public void setPsaetze(int i) {
		psaetze = i;
	}

	/**
	 * @param i
	 */
	public void setPspiele(int i) {
		pspiele = i;
	}

	/**
	 * @param spiele The spiele to set.
	 */
	public void setSpiele(int spiele) {
		this.spiele = spiele;
	}

}
