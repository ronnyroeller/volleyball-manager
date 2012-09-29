/**
 * 
 */
package com.sport.core.bo.comparators;

import java.util.Comparator;

import com.sport.core.bo.SportGroup;


/**
 * Sorts groups
 * @author Ronny
 *
 */
public class GruppenComparator implements Comparator<SportGroup> {

	public int compare(SportGroup o1, SportGroup o2) {
		if (o1 == null)
			return -1;
		if (o2 == null)
			return 1;
		Integer col1 = new Integer(o1.getSort());
		Integer col2 = new Integer(o2.getSort());
		return col1.compareTo(col2);
	}
	
}
