package com.sport.core.bo.comparators;

import java.util.Comparator;

import com.sport.analyzer.impl.MannschaftAnalyzerImpl;
import com.sport.core.bo.Team;


/**
 * Sort based on the base name (only trace back to Groupno, not to phy. team!)
 * 
 * @author ronny
 * 
 */
public class MannschaftenBaseComparator implements Comparator<Team> {
	
	public int compare(Team team1, Team team2) {
		if (team1 == null)
			return -1;
		if (team2 == null)
			return 1;
		
		String col1 = MannschaftAnalyzerImpl.getInstance().getBaseName(team1);
		String col2 = MannschaftAnalyzerImpl.getInstance().getBaseName(team2);
		
		return col1.compareTo(col2);
	}
	
}
