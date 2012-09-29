package com.sport.analyzer.impl;

import java.util.Vector;

import com.sport.core.bo.SetResult;
import com.sport.core.bo.SportMatch;
import com.sport.core.bo.Team;
import com.sport.core.bo.Tournament;


/**
 * Helps to manually set up tournaments
 * 
 * @author Ronny
 *
 */
public class Helper {

	/**
	 * Create a match
	 * @param team1
	 * @param team2
	 * @return
	 */
	public static SportMatch createMatch(Team team1, Team team2) {
		SportMatch match = new SportMatch();
		
		match.setTeam1(team1);
		match.setTeam2(team2);
		match.setSetResults(new Vector<SetResult>());
		
		return match;
	}

	/**
	 * Add a set to a match
	 */
	public static void addSet(SportMatch match, int points1, int points2) {
		SetResult setResult = new SetResult();
		setResult.setPoints1(points1);
		setResult.setPoints2(points2);
		match.getSetResults().add(setResult);
	}
	
	/**
	 * Create a team
	 * @param name
	 * @return
	 */
	public static Team createTeam(String name) {
		Team team = new Team();
		team.setName(name);
		return team;
	}

	/**
	 * Create a standard Volleyball tournament
	 * @return
	 */
	public static Tournament createTournament() {
		Tournament tournament = new Tournament();
		tournament.setPointsPerMatch(2);
		tournament.setPointsPerSet(2);
		tournament.setPointsPerTie(1);
		return tournament;
	}

}
