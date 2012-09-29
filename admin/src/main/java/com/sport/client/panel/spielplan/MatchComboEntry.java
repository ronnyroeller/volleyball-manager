package com.sport.client.panel.spielplan;

import com.sport.analyzer.impl.MannschaftAnalyzerImpl;
import com.sport.core.bo.SportMatch;


/**
 * Entry for combo box of matches. Provides correct rendering of the match name,
 * which relies on the analyzer.
 * 
 * @author Ronny
 * 
 */
public class MatchComboEntry {

	private SportMatch match;

	public MatchComboEntry(SportMatch match) {
		this.match = match;
	}

	public SportMatch getMatch() {
		return match;
	}

	@Override
	public String toString() {
		return MannschaftAnalyzerImpl.getInstance().getName(match.getTeam1())
				+ " - "
				+ MannschaftAnalyzerImpl.getInstance()
						.getName(match.getTeam2());
	}

}
