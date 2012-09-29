package com.sport.core.bo;

import java.io.Serializable;

/**
 * @author ronny
 */
public class Field implements Serializable {

	private static final long serialVersionUID = -6889532052811022938L;

	private long id;
	private String name;
	
	public Field() {
		// Achtung: dieser Wert wird NICHT fuer die EJB-Ids verwendet!
		// eindeutige ID, damit Combo-Box funktioniert!
		setId(Math.round(Math.random() * 2100000000));
	}

	public String toString() {
		return getName();
	}

	public boolean equals(Object obj) {
		if (obj instanceof Field) {
			return ((Field) obj).getId() == getId();
		}
		return false;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setId(long l) {
		id = l;
	}

	public void setName(String string) {
		name = string;
	}

}
