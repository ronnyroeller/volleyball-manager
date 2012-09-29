package com.sport.analyzer.impl;

import static org.junit.Assert.assertEquals;

import java.util.Vector;


import org.junit.BeforeClass;
import org.junit.Test;

import com.sport.analyzer.GroupResult;
import com.sport.analyzer.GruppeErgebnisEntity;
import com.sport.analyzer.impl.GruppeAnalyzerImpl;
import com.sport.core.bo.SportGroup;
import com.sport.core.bo.SportMatch;
import com.sport.core.bo.Team;

public class GruppeAnalyzerImplTest {

	private static SportGroup group = new SportGroup();
	private static Team team1 = Helper.createTeam("Klotsche");
	private static Team team2 = Helper.createTeam("Sieblingen");
	private static Team team3 = Helper.createTeam("Boeller");
	private static Vector<GruppeErgebnisEntity> details;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		group.setTournament(Helper.createTournament());
		
		group.setMannschaften(new Vector<Team>());
		group.addMannschaft(team1);
		group.addMannschaft(team2);
		group.addMannschaft(team3);
	}

	/**
	 * Set up tournament which played:
	 * <ol>
	 * <li>Klotsche:Sieblingen - 12:27</li>
	 * <li>Boeller:Sieblingen - 13:18</li>
	 * <li>Boeller:Klotsche - 19:19</li>
	 * </ol>
	 */
	@Test
	public void testAllPlayed() {
		SportMatch match1 = Helper.createMatch(team1, team2);
		SportMatch match2 = Helper.createMatch(team3, team2);
		SportMatch match3 = Helper.createMatch(team3, team1);
		Helper.addSet(match1, 12, 27);
		Helper.addSet(match2, 13, 18);
		Helper.addSet(match3, 19, 19);
		group.setMatches(new Vector<SportMatch>());
		group.addMatch(match1);
		group.addMatch(match2);
		group.addMatch(match3);
		
		GroupResult groupResult = GruppeAnalyzerImpl.getInstance().getErgebnisDetails(group);
		
		details = groupResult.getErgebnisDetails();

		// Order
		assertEquals("First team is wrong", team2, details.get(0).getMannschaftBO());
		assertEquals("Second team is wrong", team3, details.get(1).getMannschaftBO());
		assertEquals("Thrid team is wrong", team1, details.get(2).getMannschaftBO());

		// Total matches
		assertEquals("Wrong total matches for first team", 2, details.get(0).getSpiele());
		assertEquals("Wrong total matches for second team", 2, details.get(1).getSpiele());
		assertEquals("Wrong total matches for third team", 2, details.get(2).getSpiele());

		// Won matches
		assertEquals("Wrong won matches for first team", 4, details.get(0).getPspiele());
		assertEquals("Wrong won matches for second team", 1, details.get(1).getPspiele());
		assertEquals("Wrong won matches for third team", 1, details.get(2).getPspiele());

		// Lost matches
		assertEquals("Wrong lost matches for first team", 0, details.get(0).getNspiele());
		assertEquals("Wrong lost matches for second team", 3, details.get(1).getNspiele());
		assertEquals("Wrong lost matches for third team", 3, details.get(2).getNspiele());

		// Won sets
		assertEquals("Wrong won sets for first team", 4, details.get(0).getPsaetze());
		assertEquals("Wrong won sets for second team", 1, details.get(1).getPsaetze());
		assertEquals("Wrong won sets for third team", 1, details.get(2).getPsaetze());

		// Lost sets
		assertEquals("Wrong lost sets for first team", 0, details.get(0).getNsaetze());
		assertEquals("Wrong lost sets for second team", 3, details.get(1).getNsaetze());
		assertEquals("Wrong lost sets for third team", 3, details.get(2).getNsaetze());

		// Won points
		assertEquals("Wrong won points for first team", 45, details.get(0).getPpunkte());
		assertEquals("Wrong won points for second team", 32, details.get(1).getPpunkte());
		assertEquals("Wrong won points for third team", 31, details.get(2).getPpunkte());

		// Lost points
		assertEquals("Wrong lost points for first team", 25, details.get(0).getNpunkte());
		assertEquals("Wrong lost points for second team", 37, details.get(1).getNpunkte());
		assertEquals("Wrong lost points for third team", 46, details.get(2).getNpunkte());
	}

	/**
	 * Set up tournament which played:
	 * <ol>
	 * <li>Klotsche:Sieblingen - 1:0, 0:2</li>
	 * </ol>
	 */
	@Test
	public void testPartialPlayed() {
		SportMatch match1 = Helper.createMatch(team1, team2);
		Helper.addSet(match1, 1, 0);
		Helper.addSet(match1, 0, 2);
		group.setMatches(new Vector<SportMatch>());
		group.addMatch(match1);
		
		GroupResult groupResult = GruppeAnalyzerImpl.getInstance().getErgebnisDetails(group);
		
		details = groupResult.getErgebnisDetails();

		// Order
		assertEquals("First team is wrong", team2, details.get(0).getMannschaftBO());
		assertEquals("Second team is wrong", team1, details.get(1).getMannschaftBO());
		assertEquals("Thrid team is wrong", team3, details.get(2).getMannschaftBO());

		// Total matches
		assertEquals("Wrong total matches for first team", 1, details.get(0).getSpiele());
		assertEquals("Wrong total matches for second team", 1, details.get(1).getSpiele());
		assertEquals("Wrong total matches for third team", 0, details.get(2).getSpiele());

		// Won matches
		assertEquals("Wrong won matches for first team", 1, details.get(0).getPspiele());
		assertEquals("Wrong won matches for second team", 1, details.get(1).getPspiele());
		assertEquals("Wrong won matches for third team", 0, details.get(2).getPspiele());

		// Lost matches
		assertEquals("Wrong lost matches for first team", 1, details.get(0).getNspiele());
		assertEquals("Wrong lost matches for second team", 1, details.get(1).getNspiele());
		assertEquals("Wrong lost matches for third team", 0, details.get(2).getNspiele());

		// Won sets
		assertEquals("Wrong won sets for first team", 2, details.get(0).getPsaetze());
		assertEquals("Wrong won sets for second team", 2, details.get(1).getPsaetze());
		assertEquals("Wrong won sets for third team", 0, details.get(2).getPsaetze());

		// Lost sets
		assertEquals("Wrong lost sets for first team", 2, details.get(0).getNsaetze());
		assertEquals("Wrong lost sets for second team", 2, details.get(1).getNsaetze());
		assertEquals("Wrong lost sets for third team", 0, details.get(2).getNsaetze());

		// Won points
		assertEquals("Wrong won points for first team", 2, details.get(0).getPpunkte());
		assertEquals("Wrong won points for second team", 1, details.get(1).getPpunkte());
		assertEquals("Wrong won points for third team", 0, details.get(2).getPpunkte());

		// Lost points
		assertEquals("Wrong lost points for first team", 1, details.get(0).getNpunkte());
		assertEquals("Wrong lost points for second team", 2, details.get(1).getNpunkte());
		assertEquals("Wrong lost points for third team", 0, details.get(2).getNpunkte());
	}

}
