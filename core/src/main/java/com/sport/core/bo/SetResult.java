package com.sport.core.bo;

import java.io.Serializable;

/**
 * Stores the results of one set in a match
 * @author ronny
 */
public class SetResult implements Serializable {
	
	private static final long serialVersionUID = 700036523856719234L;

	private long id;

	/**
	 * Number of the set within the match
	 * 
	 * @TODO: Can this be replaced by making it a ordered list within MatchResult?
	 */
	private int setNr;

	/**
	 * Points scored by the first team
	 */
	private int points1;

	/**
	 * Points scored by the second team
	 */
	private int points2;
	
	public SetResult () {
		// Achtung: dieser Wert wird NICHT fuer die EJB-Ids verwendet!
		setId(Math.round(Math.random() * 2100000000)); // eindeutige ID, damit Combo-Box funktioniert!
	}
	
	public long getId() {
		return id;
	}

	public int getPoints1() {
		return points1;
	}

	public int getPoints2() {
		return points2;
	}

	public int getSetNr() {
		return setNr;
	}

	public void setId(long l) {
		id = l;
	}

	public void setPoints1(int i) {
		points1 = i;
	}

	public void setPoints2(int i) {
		points2 = i;
	}

	public void setSetNr(int i) {
		setNr = i;
	}

}
