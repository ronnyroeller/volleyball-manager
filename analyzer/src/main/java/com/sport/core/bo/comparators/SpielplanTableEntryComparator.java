/**
 * 
 */
package com.sport.core.bo.comparators;

import java.util.Comparator;
import java.util.Date;

import com.sport.core.bo.SpielplanTableEntryBO;


public class SpielplanTableEntryComparator implements Comparator<SpielplanTableEntryBO> {
	public int compare(SpielplanTableEntryBO o1, SpielplanTableEntryBO o2) {
		if (o1 == null)
			return -1;
		if (o2 == null)
			return 1;
		
		Date col1 = o1.getVondatum();
		Date col2 = o2.getVondatum();
		
		return col1.compareTo(col2);
	}
}