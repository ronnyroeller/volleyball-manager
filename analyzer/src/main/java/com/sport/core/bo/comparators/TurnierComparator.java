/**
 * 
 */
package com.sport.core.bo.comparators;

import java.util.Comparator;
import java.util.Date;

import com.sport.core.bo.Tournament;


public class TurnierComparator implements Comparator<Tournament> {
	public int compare(Tournament o1, Tournament o2) {
		if (o1 == null)
			return -1;
		if (o2 == null)
			return 1;
		
		Date date1 = o1.getDate();
		Date date2 = o2.getDate();

		if (date1.compareTo(date2) != 0) {
			return date1.compareTo(date2);
		}

		String name1 = o1.getName();
		String name2 = o2.getName();

		if (name1.compareTo(name2) != 0) {
			return name1.compareTo(name2);
		}

		Long id1 = new Long(o1.getId());
		Long id2 = new Long(o2.getId());
		return id2.compareTo(id1);
	}
}