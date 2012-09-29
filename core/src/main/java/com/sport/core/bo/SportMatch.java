package com.sport.core.bo;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;


import org.apache.log4j.Logger;

import com.sport.core.bo.comparators.SatzComparator;

/**
 * One sportMatch. Called "SportMatch" instead of "Match" to avoid clashes with
 * reserved SQL keyword "sportMatch"
 * 
 * @author ronny
 */
public class SportMatch implements Serializable {

	private static final Logger LOG = Logger.getLogger(SportMatch.class);

	private static final long serialVersionUID = 4237676860766622582L;

	private long id;
	private Date startDate;
	private Date endDate;
	private Field field = null;
	private Team team1 = null;
	private Team team2 = null;
	private Team referee = null;
	private SportGroup group = null;
	private Vector<SetResult> setResults = null;

	public SportMatch() {
		// Achtung: dieser Wert wird NICHT fuer die EJB-Ids verwendet!
		// eindeutige ID, damit Combo-Box funktioniert!
		setId(Math.round(Math.random() * 2100000000));
	}

	public Date getEndDate() {
		return endDate;
	}

	public SportGroup getGroup() {
		return group;
	}

	public long getId() {
		return id;
	}

	public Team getTeam1() {
		return team1;
	}

	public Team getTeam2() {
		return team2;
	}

	public Team getReferee() {
		return referee;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setEndDate(Date EndDate) {
		this.endDate = EndDate;
	}

	public void setGroup(SportGroup group) {
		this.group = group;
	}

	public void setId(long l) {
		id = l;
	}

	public void setTeam1(Team team1) {
		this.team1 = team1;
	}

	public void setTeam2(Team team2) {
		this.team2 = team2;
	}

	public void setReferee(Team referee) {
		this.referee = referee;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	/**
	 * @return sorted list of sets
	 */
	public Vector<SetResult> getSetResults() {
		// Always sort sets before returning them
		if (setResults != null) {
			Collections.sort(setResults, new SatzComparator());
		}
		return setResults;
	}

	public void setSetResults(Vector<SetResult> setResults) {
		this.setResults = setResults;
	}

	public void addSatz(SetResult SetResult) {
		setResults.add(SetResult);
	}

	public SetResult getSetResult(int row) {
		return (SetResult) setResults.get(row);
	}

	/**
	 * THIS IS CHANGED FROM FORMER VM VERSIONS -> MAY LEAD TO ISSUE IN
	 * ADMINISTRATOR/WEB (cell editors)
	 */
	public String toString() {
		LOG.error("This method shouldn't be called.", new Throwable());
		return team1.getName() + " - " + team2.getName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SportMatch other = (SportMatch) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
