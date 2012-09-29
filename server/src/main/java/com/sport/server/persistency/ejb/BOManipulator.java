package com.sport.server.persistency.ejb;

import java.util.Vector;

import com.sport.core.bo.Field;
import com.sport.core.bo.TournamentHolder;


/**
 * Changes the BO objects. This is the business logic between the external
 * interface (session bean) and the persistency layer.
 * 
 * @author Ronny
 * 
 */
public class BOManipulator {

	/**
	 * Sets the fields in the tournamentHolder object
	 * 
	 * @param tournamentHolder
	 * @param spielplaetzeBO
	 * @return
	 */
	public TournamentHolder setFields(TournamentHolder tournamentHolder,
			Object[] spielplaetzeBO) {
		Vector<Field> newFields = new Vector<Field>();
		for (int i = 0; i < spielplaetzeBO.length; i++) {
			Field field = (Field) spielplaetzeBO[i];

			Field oldField = tournamentHolder.getField(field.getId());

			// If not already in the current tournamentHolder -> just add
			if (oldField == null)
				newFields.add(field);
			else {
				// Update old field -> so that we don't loose the references
				oldField.setName(field.getName());
				newFields.add(oldField);
			}
		}
		tournamentHolder.setFields(newFields);

		return tournamentHolder;
	}

}
