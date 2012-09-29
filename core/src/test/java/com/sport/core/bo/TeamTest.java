package com.sport.core.bo;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

public class TeamTest {
	
	private static Team team = new Team();
	private static Team sameTeam = new Team();
	private static Team differentTeam = new Team();

	@BeforeClass
	public static void setUpBeforeClass() {
		team.setId(1);
		sameTeam.setId(1);
		differentTeam.setId(2);
	}
	
	@Test
	public void testEqualsObject() {
		assertTrue("Didn't detect equal teams", team.equals(sameTeam));
		assertFalse("Didn't detect different teams", team.equals(differentTeam));
	}
	
}
