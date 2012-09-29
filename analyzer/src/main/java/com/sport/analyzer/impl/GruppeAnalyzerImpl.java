package com.sport.analyzer.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.sport.analyzer.GroupResult;
import com.sport.analyzer.GruppeAnalyzer;
import com.sport.analyzer.GruppeErgebnisEntity;
import com.sport.analyzer.MannschaftAnalyzer;
import com.sport.analyzer.SpielAnalyzer;
import com.sport.analyzer.SpielAnalyzer.DetailedMatchResult;
import com.sport.core.bo.SportGroup;
import com.sport.core.bo.SportMatch;
import com.sport.core.bo.Team;


/**
 * Calculate results for a group
 * 
 * @author Ronny
 * 
 */
public class GruppeAnalyzerImpl implements GruppeAnalyzer {

	/**
	 * Singleton pattern
	 */
	private static GruppeAnalyzerImpl instance;

	private MannschaftAnalyzer mannschaftAnalyzer = MannschaftAnalyzerImpl
			.getInstance();

	private GruppeAnalyzerImpl() {

	}

	public static GruppeAnalyzer getInstance() {
		if (instance == null)
			instance = new GruppeAnalyzerImpl();

		return instance;
	}

	@Override
	public GroupResult getErgebnisDetails(SportGroup group) {
		GroupResult groupResult = new GroupResult();

		groupResult.vorlaeufig = false;

		// Ergebnis nur berechenbar falls Mannschaften vorhanden sind!
		if (group.getMannschaften() != null
				&& group.getMannschaften().size() > 0) {

			// Stores results for all teams
			Map<Long, GruppeErgebnisEntity> teamResults = new HashMap<Long, GruppeErgebnisEntity>();

			// Initialize map
			for (Team team : group.getMannschaften()) {
				GruppeErgebnisEntity gruppeErgebnisEntry = new GruppeErgebnisEntity(
						team);
				teamResults.put(team.getId(), gruppeErgebnisEntry);
			}

			// Analyze each match
			if (group.getMatches() != null) {
				for (SportMatch match : group.getMatches()) {
					DetailedMatchResult resultSpiel = SpielAnalyzerImpl
							.getInstance().getErgebnisDetails(match);

					// Flag if one of the group match wasn't played so far
					if (resultSpiel.winner == SpielAnalyzer.MatchResult.NICHTGESPIELT) {
						groupResult.vorlaeufig = true;
					} else {
						// Get other results of physical team1
						Team team1 = match.getTeam1();
						Team relTeam1 = mannschaftAnalyzer
								.getRelGruppeMannschaftBO(team1);
						GruppeErgebnisEntity gruppeErgebnisEntry1 = teamResults
								.get(relTeam1.getId());

						// Get other results of physical team2
						Team team2 = match.getTeam2();
						Team relTeam2 = mannschaftAnalyzer
								.getRelGruppeMannschaftBO(team2);
						GruppeErgebnisEntity gruppeErgebnisEntry2 = teamResults
								.get(relTeam2.getId());

						// If teams can't be resolve to a physical team, we
						// create a temporary GruppeErgebnisEntity. Afterwards
						// they will be removed. This avoids
						// NullPointerExceptions.
						if (gruppeErgebnisEntry1 == null)
							gruppeErgebnisEntry1 = new GruppeErgebnisEntity();

						if (gruppeErgebnisEntry2 == null)
							gruppeErgebnisEntry2 = new GruppeErgebnisEntity();

						// Spiele
						switch (resultSpiel.winner) {
						case MANNSCHAFT1:
							gruppeErgebnisEntry1.pspiele += group
									.getTournament().getPointsPerMatch();
							gruppeErgebnisEntry2.nspiele += group
									.getTournament().getPointsPerMatch();
							break;
						case MANNSCHAFT2:
							gruppeErgebnisEntry1.nspiele += group
									.getTournament().getPointsPerMatch();
							gruppeErgebnisEntry2.pspiele += group
									.getTournament().getPointsPerMatch();
							break;
						case UNENTSCHIEDEN:
							gruppeErgebnisEntry1.pspiele += group
									.getTournament().getPointsPerTie();
							gruppeErgebnisEntry1.nspiele += group
									.getTournament().getPointsPerTie();
							gruppeErgebnisEntry2.pspiele += group
									.getTournament().getPointsPerTie();
							gruppeErgebnisEntry2.nspiele += group
									.getTournament().getPointsPerTie();
							break;
						}

						// Count sets
						gruppeErgebnisEntry1.psaetze += resultSpiel.saetze1;
						gruppeErgebnisEntry2.nsaetze += resultSpiel.saetze1;
						gruppeErgebnisEntry1.nsaetze += resultSpiel.saetze2;
						gruppeErgebnisEntry2.psaetze += resultSpiel.saetze2;

						// Count points
						gruppeErgebnisEntry1.ppunkte += resultSpiel.punkte1;
						gruppeErgebnisEntry2.npunkte += resultSpiel.punkte1;
						gruppeErgebnisEntry1.npunkte += resultSpiel.punkte2;
						gruppeErgebnisEntry2.ppunkte += resultSpiel.punkte2;

						// Count matches
						gruppeErgebnisEntry1.spiele++;
						gruppeErgebnisEntry2.spiele++;
					}
				}
			}

			// Add all results to the final result structure
			for (GruppeErgebnisEntity gruppeErgebnisEntry : teamResults
					.values())
				groupResult.ergebnisDetails.add(gruppeErgebnisEntry);

			// Sort results
			Collections.sort(groupResult.ergebnisDetails,
					new GruppeErgebnisEntryComperator());
		}

		return groupResult;
	}

}
