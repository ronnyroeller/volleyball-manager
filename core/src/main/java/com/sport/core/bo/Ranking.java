/*
 * Created on 04.06.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.sport.core.bo;

import java.io.Serializable;

/**
 * One ranking entry.
 * 
 * @author ronny
 */
public class Ranking implements Serializable {

	private static final long serialVersionUID = 7859603452096811767L;

	private long id;

	/**
	 * Rank of this place, e.g. first, second, third, etc.
	 */
	private long rank;
	
	private Team mannschaft = null;
	
	public Ranking () {
		// Achtung: dieser Wert wird NICHT fuer die EJB-Ids verwendet!
		setId(Math.round(Math.random() * 2100000000)); // eindeutige ID, damit Combo-Box funktioniert!
	}

	public long getId() {
		return id;
	}

	public long getRank() {
		return rank;
	}

	public void setId(long l) {
		id = l;
	}

	public void setRank(long l) {
		rank = l;
	}

	public Team getMannschaft() {
		return mannschaft;
	}

	public void setMannschaft(Team mannschaftBO) {
		mannschaft = mannschaftBO;
	}

}
