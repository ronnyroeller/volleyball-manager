package com.sport.core.bo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sport.core.bo.SportMatch;

public class SportMatchTest {

	private static SportMatch match = new SportMatch();
	private static SportMatch sameMatch = new SportMatch();
	private static SportMatch differentMatch = new SportMatch();

	@BeforeClass
	public static void setUpBeforeClass() {
		match.setId(1);
		sameMatch.setId(1);
		differentMatch.setId(2);
	}
	
	@Test
	public void testEqualsObject() {
		assertTrue("Same matches were not detected", match.equals(sameMatch));
		assertFalse("Different matches was not detected", match.equals(differentMatch));		
	}

	/**
	 * Checks if a map with matches as keys is holding each equal match only once
	 */
	@Test
	public void testMap() {
		Map<SportMatch, Integer> map = new HashMap<SportMatch, Integer>();
		map.put(match, 1);
		map.put(sameMatch, 2);
		map.put(differentMatch, 3);
		
		assertEquals("Same matches were not detected", 2, map.size());
	}
	
}
