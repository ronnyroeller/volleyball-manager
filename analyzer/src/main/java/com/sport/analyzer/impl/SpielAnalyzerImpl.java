package com.sport.analyzer.impl;

import com.sport.analyzer.SpielAnalyzer;
import com.sport.core.bo.SetResult;
import com.sport.core.bo.SportMatch;


/**
 * Calculate results for a match
 * 
 * @author Ronny
 * 
 */
public class SpielAnalyzerImpl implements SpielAnalyzer {

	/**
	 * Singleton pattern
	 */
	private static SpielAnalyzer instance;

	private SpielAnalyzerImpl() {

	}

	public static SpielAnalyzer getInstance() {
		if (instance == null)
			instance = new SpielAnalyzerImpl();

		return instance;
	}

	@Override
	public DetailedMatchResult getErgebnisDetails(SportMatch match) {
		DetailedMatchResult result = new DetailedMatchResult();

		result.winner = MatchResult.NICHTGESPIELT;

		// Match is already played?
		if (match.getSetResults() != null && !match.getSetResults().isEmpty()) {
			// Analyze each set
			for (SetResult satzBO : match.getSetResults()) {
				result.punkte1 += satzBO.getPoints1();
				result.punkte2 += satzBO.getPoints2();
				
				switch (getSetErgebnis(satzBO)) {
				case UNENTSCHIEDEN:
					result.saetze1 += match.getGroup().getTournament()
							.getPointsPerSet() / 2;
					result.saetze2 += match.getGroup().getTournament()
							.getPointsPerSet() / 2;
					break;
				case MANNSCHAFT1:
					result.saetze1 += match.getGroup().getTournament()
							.getPointsPerSet();
					break;
				case MANNSCHAFT2:
					result.saetze2 += match.getGroup().getTournament()
							.getPointsPerSet();
					break;
				}
			}

			if (result.saetze1 > result.saetze2) {
				result.winner = MatchResult.MANNSCHAFT1;
			} else if (result.saetze1 < result.saetze2) {
				result.winner = MatchResult.MANNSCHAFT2;
			} else {
				// nur Gewinnsaetze -> keine Punkte einrechnen!
				result.winner = MatchResult.UNENTSCHIEDEN;
			}
		}

		return result;
	}

	@Override
	public SetResults getSetErgebnis(SetResult satzBO) {
		if (satzBO.getPoints1() == satzBO.getPoints2())
			return SetResults.UNENTSCHIEDEN;

		if (satzBO.getPoints1() > satzBO.getPoints2())
			return SetResults.MANNSCHAFT1;

		return SetResults.MANNSCHAFT2;
	}

}
