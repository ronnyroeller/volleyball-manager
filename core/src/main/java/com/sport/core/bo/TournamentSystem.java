package com.sport.core.bo;

import java.io.Serializable;

import com.sport.core.helper.Messages;


/**
 * Holds various tournament systems (see also
 * http://en.wikipedia.org/wiki/Tournaments)
 * 
 * @TODO: Further simplify
 * 
 * @author ronny
 */
public class TournamentSystem implements Serializable {

	private static final long serialVersionUID = 5331509658015725688L;

	private long id;
	private String name;

	public final static int GRUPPENMODUS = 1;
	public final static int GRUPPE_RUECKSPIELMODUS = 2;
	public final static int KOMODUS = 3;
	public final static int DOPPELKOMODUS = 4;

	public TournamentSystem() {
		// Achtung: dieser Wert wird NICHT fuer die EJB-Ids verwendet!
		// eindeutige ID, damit Combo-Box funktioniert!
		setId(Math.round(Math.random() * 2100000000));
	}

	public boolean isGruppenModus() {
		return getId() == GRUPPENMODUS;
	}

	public boolean isGruppe_rueckspielModus() {
		return getId() == GRUPPE_RUECKSPIELMODUS;
	}

	public boolean isKoModus() {
		return getId() == KOMODUS;
	}

	public boolean isDoppelkoModus() {
		return getId() == DOPPELKOMODUS;
	}

	/**
	 * gibt zur�ck, ob Gruppe nach einem KO-System gespielt wird, d.h. nicht
	 * Mannschaft mit besten Punkten, etc. gewinnt unbedingt
	 * 
	 * @return
	 */
	public boolean getKoSystem() {
		return isKoModus() || isDoppelkoModus();
	}

	public String toString() {
		return getName();
	}

	public long getId() {
		return id;
	}

	public String getName() {
		// i18n!!!
		return Messages.getString(name);
	}

	public void setId(long l) {
		id = l;
	}

	public void setName(String string) {
		name = string;
	}

	public boolean equals(Object obj) {
		if (obj instanceof TournamentSystem) {
			return ((TournamentSystem) obj).getId() == getId();
		}
		return false;
	}

}
