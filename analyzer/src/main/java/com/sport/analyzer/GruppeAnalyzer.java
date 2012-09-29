package com.sport.analyzer;

import com.sport.core.bo.SportGroup;

public interface GruppeAnalyzer {

	/**
	 * Calculates all results for a specific group
	 * 
	 * @param gruppeBO
	 * @return
	 */
	public GroupResult getErgebnisDetails(SportGroup gruppeBO);

}