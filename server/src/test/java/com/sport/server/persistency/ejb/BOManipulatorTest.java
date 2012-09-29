package com.sport.server.persistency.ejb;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sport.core.bo.Field;
import com.sport.core.bo.TournamentHolder;
import com.sport.server.persistency.ejb.BOManipulator;

public class BOManipulatorTest {
	
	private BOManipulator bom = new BOManipulator();

	@BeforeClass
	public static void setUpBeforeClass() {
	}
	
	@Test
	public void testSetFields() {
		TournamentHolder th = new TournamentHolder();

		// Create initial object
		Field oldField1 = new Field();
		Field oldField2 = new Field();
		oldField1.setId(1);
		oldField1.setName("Field 1");
		oldField2.setId(2);
		th.addField(oldField1);
		th.addField(oldField2);
		
		// Now change it
		Field updatedField1 = new Field();
		Field newField3 = new Field();
		updatedField1.setId(1);
		updatedField1.setName("Updated field 1");
		newField3.setId(3);
		bom.setFields(th, new Object[]{updatedField1, newField3});
		
		assertEquals("Obsolete field was not removed", 2, th.getFields().size());
		assertEquals("Instance of existing field changed", oldField1, th.getFields().get(0));
		assertEquals("Content of existing field was not updated", "Updated field 1", th.getFields().get(0).getName());
		assertEquals("New field was not added", newField3, th.getFields().get(1));
	}

}
