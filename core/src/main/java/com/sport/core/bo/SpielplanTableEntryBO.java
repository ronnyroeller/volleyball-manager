/*
 * Created on 22.06.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.sport.core.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

/**
 * @author ronny
 */
public class SpielplanTableEntryBO implements Serializable {

	private static final long serialVersionUID = 7334564443812255980L;

	private Date vondatum = null;
	private Date bisdatum = null;
	private Vector<SportMatch> spiele = new Vector<SportMatch>();

	public SpielplanTableEntryBO() {
	}
	public SpielplanTableEntryBO(Date vondatum) {
		this.vondatum = vondatum;
	}
	/**
	 * @param vondatum ... start date
	 * @param duration ... duration of the game in miliseconds
	 */
	public SpielplanTableEntryBO(Date vondatum, long duration) {
		setVondatum(vondatum);
		setBisdatum(new Date(vondatum.getTime() + duration));
	}
	/**
	 * @return
	 */
	public Vector<SportMatch> getSpiele() {
		return spiele;
	}
	public SportMatch getSpiel(int index) {
		return (SportMatch) spiele.get(index);
	}

	/**
	 * @return
	 */
	public Date getVondatum() {
		return vondatum;
	}

	/**
	 * @param vector
	 */
	public void setSpiele(Vector<SportMatch> vector) {
		spiele = vector;
	}

	public void setSpiel(int index, SportMatch spielBO) {
		spiele.set(index, spielBO);
	}
	public void addSpiel(SportMatch spielBO) {
		spiele.add(spielBO);
	}

	/**
	 * @param date
	 */
	public void setVondatum(Date date) {
		vondatum = date;

		// Durchschreiben auf alle Spiele des Blocks
		for (SportMatch spielBO : spiele) {
			if (spielBO != null) {
				spielBO.setStartDate(vondatum);
			}
		}
	}

	/**
	 * @return
	 */
	public Date getBisdatum() {
		return bisdatum;
	}

	/**
	 * @param date
	 */
	public void setBisdatum(Date date) {
		bisdatum = date;

		// Durchschreiben auf alle Spiele des Blocks
		for (SportMatch spielBO : spiele) {
			if (spielBO != null) {
				spielBO.setEndDate(bisdatum);
			}
		}
	}

	// Verschiebt Zeit um timediff
	public void moveDatum (long timediff) {
		setVondatum(new Date (getVondatum().getTime() + timediff));
		setBisdatum(new Date (getBisdatum().getTime() + timediff));
	}

}
