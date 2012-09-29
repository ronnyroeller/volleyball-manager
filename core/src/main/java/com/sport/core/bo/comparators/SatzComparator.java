package com.sport.core.bo.comparators;

import java.util.Comparator;

import com.sport.core.bo.SetResult;


/**
 * Compares two sets to eachother
 * @author Ronny
 *
 */
public class SatzComparator implements Comparator<SetResult> {
	public int compare(SetResult o1, SetResult o2) {
		if (o1 == null)
			return -1;
		if (o2 == null)
			return 1;
		Long col1 = new Long(o1.getSetNr());
		Long col2 = new Long(o2.getSetNr());
		return col1.compareTo(col2);
	}
}