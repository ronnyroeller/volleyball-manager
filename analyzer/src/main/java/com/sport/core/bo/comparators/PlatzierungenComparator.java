/**
 * 
 */
package com.sport.core.bo.comparators;

import java.util.Comparator;

import com.sport.core.bo.Ranking;


public class PlatzierungenComparator implements Comparator<Ranking> {
	public int compare(Ranking o1, Ranking o2) {
		if (o1 == null)
			return -1;
		if (o2 == null)
			return 1;
		Long col1 = new Long(o1.getRank());
		Long col2 = new Long(o2.getRank());
		return col1.compareTo(col2);
	}
}