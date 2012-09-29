/**
 * 
 */
package com.sport.core.bo.comparators;

import java.util.Comparator;
import java.util.Date;

import com.sport.analyzer.impl.MannschaftAnalyzerImpl;
import com.sport.core.bo.SportMatch;


public class SpielComparator implements Comparator<SportMatch> {
	public int compare(SportMatch o1, SportMatch o2) {
		if (o1 == null)
			return -1;
		if (o2 == null)
			return 1;

		// Zeit (try -> falls Datum nicht gesetzt!)
		try {
			Date col1 = o1.getStartDate();
			Date col2 = o2.getStartDate();

			if (col1.compareTo(col2) != 0) {
				return col1.compareTo(col2);
			}
		} catch (NullPointerException e) {
		}

		// Bis-Zeit (try -> falls Datum nicht gesetzt!)
		try {
			Date col1 = o1.getEndDate();
			Date col2 = o2.getEndDate();

			if (col1.compareTo(col2) != 0) {
				return col2.compareTo(col1);
			}
		} catch (NullPointerException e) {
		}

		// Spielplatz (try -> falls Spielplatz nicht gesetzt!)
		try {
			String col1 = o1.getField().getName();
			String col2 = o2.getField().getName();

			if (col1.compareTo(col2) != 0) {
				return col1.compareTo(col2);
			}
		} catch (NullPointerException e) {
		}

		// Mannschaft 1
		String col1 = MannschaftAnalyzerImpl.getInstance().getName(o1.getTeam1());
		String col2 = MannschaftAnalyzerImpl.getInstance().getName(o2.getTeam1());

		if (col1.compareTo(col2) != 0) {
			return col1.compareTo(col2);
		}

		// Mannschaft 2
		col1 = MannschaftAnalyzerImpl.getInstance().getName(o1.getTeam2());
		col2 = MannschaftAnalyzerImpl.getInstance().getName(o2.getTeam2());

		return col1.compareTo(col2);
	}
}