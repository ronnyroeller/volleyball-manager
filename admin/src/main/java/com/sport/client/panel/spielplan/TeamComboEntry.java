package com.sport.client.panel.spielplan;

import com.sport.analyzer.impl.MannschaftAnalyzerImpl;
import com.sport.core.bo.Team;


/**
 * Entry for combo box of teams. Provides correct rendering of the team name,
 * which relies on the analyzer.
 * 
 * @author Ronny
 * 
 */
public class TeamComboEntry {

	private Team team;

	public TeamComboEntry(Team team) {
		this.team = team;
	}

	public Team getTeam() {
		return team;
	}

	@Override
	public String toString() {
		return MannschaftAnalyzerImpl.getInstance().getName(team);
	}

}
