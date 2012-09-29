package com.sport.core.bo;

import java.util.Vector;


/**
 * Holds all information about a tournament. Temporary required because business
 * objects are not all linked to each other.
 * 
 * @author Ronny
 * 
 */
public class TournamentHolder {

	private Tournament tournament;
	private Vector<Field> fields;
	private Vector<SportGroup> groups;
	private Vector<Team> teams;
	private Vector<SportMatch> matches;
	private Vector<Ranking> rankings;

	public TournamentHolder() {
		fields = new Vector<Field>();
		groups = new Vector<SportGroup>();
		teams = new Vector<Team>();
		matches = new Vector<SportMatch>();
		rankings = new Vector<Ranking>();
	}

	public Tournament getTournament() {
		return tournament;
	}

	public void setTournament(Tournament tournament) {
		this.tournament = tournament;
	}

	public Vector<Field> getFields() {
		return fields;
	}

	public void addField(Field field) {
		fields.add(field);
	}

	public void setFields(Vector<Field> fields) {
		this.fields = fields;
	}

	public Vector<SportGroup> getGroups() {
		return groups;
	}

	public void addGroup(SportGroup group) {
		groups.add(group);
	}

	public void setGroups(Vector<SportGroup> groups) {
		this.groups = groups;
	}

	public Vector<Team> getTeams() {
		return teams;
	}

	public void addTeam(Team team) {
		teams.add(team);
	}

	public Vector<SportMatch> getMatches() {
		return matches;
	}

	public void addMatch(SportMatch match) {
		matches.add(match);
	}

	public Vector<Ranking> getRankings() {
		return rankings;
	}

	public void addRanking(Ranking ranking) {
		rankings.add(ranking);
	}

	/**
	 * Returns the field with a specific ID
	 * @param id
	 * @return null if the field is not known
	 */
	public Field getField(long fieldId) {
		// Iterate over all fields to find the one with the requested ID
		// TODO: Improve performance!
		for (Field field : fields)
			if (field.getId() == fieldId)
				return field;
				
		return null;
	}

	/**
	 * Returns the group with a specific ID
	 * @param id
	 * @return
	 */
	public SportGroup getGroup(long groupId) {
		// Iterate over all groups to find the one with the requested ID
		// TODO: Improve performance!
		for (SportGroup group : groups)
			if (group.getId() == groupId)
				return group;

		return null;
	}

	public void setTeams(Vector<Team> teams) {
		this.teams = teams;
	}

}
