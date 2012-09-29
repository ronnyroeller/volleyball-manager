/**
 * 
 */
package com.sport.core.bo.comparators;

import java.util.Comparator;

import com.sport.core.bo.Field;


public class SpielplatzComparator implements Comparator<Field> {
	public int compare(Field o1, Field o2) {
		if (o1 == null)
			return -1;
		if (o2 == null)
			return 1;

		String col1 = o1.getName();
		String col2 = o2.getName();

		return col1.compareTo(col2);
	}
}