/*
 * Created on 01.07.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.sport.core.bo.modus;

import java.util.Date;
import java.util.Vector;

import com.sport.core.bo.SportGroup;


/**
 * Spiegelt einen konkreten Modus wieder
 * @author ronny
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class AbstractModus {

	/**
	 * Auswahl �ber Paarungen oder Mannschaften?
	 * @return
	 */
	public abstract boolean isShowPaarung ();

	/**
	 * KO-Modus System?
	 * @return
	 */
	public abstract boolean isKoSystem ();

	/**
	 * Gibt alle moeglichen Spiele des Modus f�r eine bestimmte Gruppe zur�ck
	 * optimiert fuer AUSWAHLLISTE
	 * @return
	 */
	public abstract Vector getSpiele (SportGroup gruppeBO);

	/**
	 * Gibt alle moeglichen Spiele des Modus f�r eine bestimmte Gruppe zur�ck
	 * optimiert fuer SCHEDULEGEN
	 * @return
	 */
	public abstract Vector getSpieleGen (SportGroup gruppeBO);

	/**
	 * Gibt alle moeglichen Spiele des Modus f�r eine bestimmte Gruppe zur�ck,
	 * die noch nicht verwendet wurden!
	 * @return
	 */
	public abstract Vector getUnusedSpiele(SportGroup gruppeBO, boolean gen);

	/**
	 * optimiert fuer Auswahlliste
	 */
	public abstract Vector getUnusedSpiele(SportGroup gruppeBO);

	/**
	 * optimiert fuer ScheduleGen
	 */
	public abstract Vector getUnusedSpieleGen(SportGroup gruppeBO);

	/**
	 * Gibt alle moeglichen Mannschaften edn Modus f�r eine bestimmte Gruppe zur�ck
	 * ab einer bestimmten Zeit (KO-Spiel!)
	 * @return
	 */
	public abstract Vector getMannschaften (SportGroup gruppeBO, Date vondatum);

	/**
	 * Calculates all teams of this group that can act as referees in this turn
	 * @param gruppeBO
	 * @return
	 */
	public abstract Vector getMglReferee (SportGroup gruppeBO);
	
}
