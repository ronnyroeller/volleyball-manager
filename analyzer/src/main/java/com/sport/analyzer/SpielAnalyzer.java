package com.sport.analyzer;

import com.sport.core.bo.SetResult;
import com.sport.core.bo.SportMatch;

public interface SpielAnalyzer {

	/**
	 * Holds results for one calculation
	 * 
	 */
	public class DetailedMatchResult {
		public int saetze1 = 0;
		public int saetze2 = 0;
		public int punkte1 = 0;
		public int punkte2 = 0;
		public MatchResult winner = MatchResult.NICHTGESPIELT;
	}

	/**
	 * Summary on the match
	 */
	public enum MatchResult {
		/**
		 * Teams didn't play so far
		 */
		NICHTGESPIELT,

		/**
		 * Draw
		 */
		UNENTSCHIEDEN, 
		
		/**
		 * First team won
		 */
		MANNSCHAFT1,
		
		/**
		 * Second team won
		 */
		MANNSCHAFT2
	}

	/**
	 * Summary on the set
	 */
	public enum SetResults {
		/**
		 * Draw
		 */
		UNENTSCHIEDEN,
		
		/**
		 * First team won
		 */
		MANNSCHAFT1,
		
		/**
		 * Second team won
		 */
		MANNSCHAFT2
	}

	/**
	 * Calculates the winner/looser of a match
	 * 
	 * @param spielBO
	 * @return
	 */
	public abstract DetailedMatchResult getErgebnisDetails(SportMatch spielBO);

	/**
	 * Calculates which team won a set
	 */
	public abstract SetResults getSetErgebnis(SetResult satzBO);

}