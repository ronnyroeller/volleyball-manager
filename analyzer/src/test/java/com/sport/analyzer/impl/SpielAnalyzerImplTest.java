package com.sport.analyzer.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.sport.analyzer.SpielAnalyzer.DetailedMatchResult;
import com.sport.analyzer.SpielAnalyzer.MatchResult;
import com.sport.analyzer.impl.SpielAnalyzerImpl;
import com.sport.core.bo.SportGroup;
import com.sport.core.bo.SportMatch;
import com.sport.core.bo.Team;

public class SpielAnalyzerImplTest {

	private static Team team1 = Helper.createTeam("Klotsche");
	private static Team team2 = Helper.createTeam("Sieblingen");

	private SportGroup group = new SportGroup();
	private SportMatch match;

	@Before
	public void setUp() throws Exception {
		group.setTournament(Helper.createTournament());

		match = Helper.createMatch(team1, team2);
		match.setGroup(group);
	}

	@Test
	public void testWinTeam1() {
		Helper.addSet(match, 30, 10); // team1 wins
		Helper.addSet(match, 10, 20); // team2 wins
		Helper.addSet(match, 10, 10); // tie
		Helper.addSet(match, 20, 10); // team1 wins
		DetailedMatchResult matchResult = SpielAnalyzerImpl.getInstance()
				.getErgebnisDetails(match);
		assertEquals("Wrong team was detected as winner",
				MatchResult.MANNSCHAFT1, matchResult.winner);
		assertEquals("Wrong number of sets for team1", 5, matchResult.saetze1);
		assertEquals("Wrong number of sets for team2", 3, matchResult.saetze2);
		assertEquals("Wrong number of points for team1", 70,
				matchResult.punkte1);
		assertEquals("Wrong number of points for team2", 50,
				matchResult.punkte2);
	}

	@Test
	public void testWinTeam2() {
		Helper.addSet(match, 10, 30); // team2 wins
		Helper.addSet(match, 20, 10); // team1 wins
		Helper.addSet(match, 10, 10); // tie
		Helper.addSet(match, 10, 20); // team2 wins
		DetailedMatchResult matchResult = SpielAnalyzerImpl.getInstance()
				.getErgebnisDetails(match);
		assertEquals("Wrong team was detected as winner",
				MatchResult.MANNSCHAFT2, matchResult.winner);
		assertEquals("Wrong number of sets for team1", 3, matchResult.saetze1);
		assertEquals("Wrong number of sets for team2", 5, matchResult.saetze2);
		assertEquals("Wrong number of points for team1", 50,
				matchResult.punkte1);
		assertEquals("Wrong number of points for team2", 70,
				matchResult.punkte2);
	}

	@Test
	public void testTie() {
		Helper.addSet(match, 10, 30); // team2 wins
		Helper.addSet(match, 20, 10); // team1 wins
		Helper.addSet(match, 10, 10); // tie
		DetailedMatchResult matchResult = SpielAnalyzerImpl.getInstance()
				.getErgebnisDetails(match);
		assertEquals("Wrong team was detected as winner",
				MatchResult.UNENTSCHIEDEN, matchResult.winner);
		assertEquals("Wrong number of sets for team1", 3, matchResult.saetze1);
		assertEquals("Wrong number of sets for team2", 3, matchResult.saetze2);
		assertEquals("Wrong number of points for team1", 40,
				matchResult.punkte1);
		assertEquals("Wrong number of points for team2", 50,
				matchResult.punkte2);
	}

	@Test
	public void testNotPlayed() {
		DetailedMatchResult matchResult = SpielAnalyzerImpl.getInstance()
		.getErgebnisDetails(match);
		assertEquals("Not detected that it wasn't played",
				MatchResult.NICHTGESPIELT, matchResult.winner);
	}
	
}
