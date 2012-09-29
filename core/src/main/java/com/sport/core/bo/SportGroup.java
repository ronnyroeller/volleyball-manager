package com.sport.core.bo;

import java.io.Serializable;
import java.util.Vector;

/**
 * Stores one group (which contains teams)
 * 
 * Class is called SportGroup instead of just Group to avoid classes with
 * reserved SQL keyword 'group'
 * 
 * @author ronny
 */
public class SportGroup implements Serializable {

	private static final long serialVersionUID = -6848047314188647544L;

	private long id;
	private int sort = 0;

	/**
	 * Name of the group
	 */
	private String name;

	/**
	 * Color that is shown with this group
	 */
	private String color;

	private TournamentSystem tournamentSystem = null;
	private Vector<Team> mannschaften = null;
	private Vector<SportMatch> matches = null;
	private Tournament tournament = null;

	public SportGroup(Tournament tournament) {
		this.tournament = tournament;

		// Achtung: dieser Wert wird NICHT fuer die EJB-Ids verwendet!
		// eindeutige ID, damit Combo-Box funktioniert!
		setId(Math.round(Math.random() * 2100000000));
		setTournamentSystem(null);
	}

	public SportGroup() {
		this(null);
	}

	public String toString() {
		return getName();
	}

	public String getColor() {
		return color;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public void setId(long l) {
		id = l;
	}

	public void setName(String string) {
		name = string;
	}

	public TournamentSystem getTournamentSystem() {
		return tournamentSystem;
	}

	public void setTournamentSystem(TournamentSystem tournamentSystem) {
		this.tournamentSystem = tournamentSystem;
	}

	public Vector<Team> getMannschaften() {
		return mannschaften;
	}

	public void addMannschaft(Team mannschaftBO) {
		mannschaftBO.setGruppeBO(this);
		mannschaftBO.setTurnierBO(this.getTournament());
		getMannschaften().add(mannschaftBO);
	}

	public void removeMannschaft(Team mannschaftBO) {
		long oldSort = mannschaftBO.getSort();
		getMannschaften().remove(mannschaftBO);
		// Gruppierung anpassen
		for (Team curMannschaftBO : getMannschaften()) {
			if (curMannschaftBO.getSort() > oldSort) {
				curMannschaftBO.setSort(curMannschaftBO.getSort() - 1);
			}
		}
	}

	/**
	 * Delete a virtual team
	 * 
	 * @return if there was a virtual team which could be deleted
	 * 
	 */
	public boolean removeVirtualTeam() {
		// check for first virtual team
		for (Team mannschaftBO : getMannschaften()) {
			if (mannschaftBO.isVirtual()) {
				removeMannschaft(mannschaftBO);
				return true;
			}
		}
		return false;
	}

	public void setMannschaften(Vector<Team> vector) {
		mannschaften = vector;
	}

	public boolean equals(Object obj) {
		if (obj instanceof SportGroup) {
			return ((SportGroup) obj).getId() == getId();
		}
		return false;
	}

	public Vector<SportMatch> getMatches() {
		return matches;
	}

	public void setMatches(Vector<SportMatch> matches) {
		this.matches = matches;
	}

	public void addMatch(SportMatch match) {
		match.setGroup(this);
		matches.add(match);
	}

	public void removeMatch(SportMatch match) {
		match.setGroup(null);
		matches.remove(match);
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int l) {
		sort = l;
	}

	public Tournament getTournament() {
		return tournament;
	}

	public void setTournament(Tournament tournament) {
		this.tournament = tournament;
	}

}
