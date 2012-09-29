/**
 * 
 */
package com.sport.client.panel.gruppen;

import java.util.Comparator;

import com.sport.core.bo.TournamentSystem;


public class ModusComparator implements Comparator <TournamentSystem>{

	public int compare(TournamentSystem o1, TournamentSystem o2) {
		if (o1 == null)
			return -1;
		if (o2 == null)
			return 1;
		String col1 = o1.getName();
		String col2 = o2.getName();
		return col1.compareTo(col2);
	}

}
