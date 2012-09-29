/*
 * Created on 01.07.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.sport.core.bo.modus;

import java.util.Date;
import java.util.Vector;


import org.apache.log4j.Logger;

import com.sport.core.bo.SportGroup;
import com.sport.core.bo.SportMatch;
import com.sport.core.bo.Team;

/**
 * Bietet einen transparenten Zugriff auf die Modi
 * @author ronny
 *
 */
public class ModusMgr {

	private static final Logger LOG = Logger.getLogger(ModusMgr.class);

	private static AbstractModus getModus (SportGroup gruppeBO) {
		if (gruppeBO.getTournamentSystem().isGruppenModus()) {
			return new GruppenModus ();
		}
		else if (gruppeBO.getTournamentSystem().isGruppe_rueckspielModus()) {
			return new Gruppe_rueckspielModus ();
		}
		else if (gruppeBO.getTournamentSystem().isKoModus()) {
			return new KoModus ();
		}
		else if (gruppeBO.getTournamentSystem().isDoppelkoModus()) {
			return new DoppelkoModus ();
		}

		LOG.error("[ModusMgr] Fehler! Unbekannter Modus: "+gruppeBO.getId());
		
		return null;
	}

	/**
	 * Gibt alle moeglichen Spiele des Modus f�r eine bestimmte Gruppe zur�ck
	 * @return
	 */
	public static Vector getSpiele (SportGroup gruppeBO) {
		return getModus(gruppeBO).getSpiele (gruppeBO);
	}

	/**
	 * Gibt alle moeglichen Spiele des Modus f�r eine bestimmte Gruppe zur�ck,
	 * die noch nicht verwendet wurden!
	 * optimiert fuer Auswahlliste
	 * @return
	 */
	public static Vector<SportMatch> getUnusedSpiele (SportGroup gruppeBO) {
		return getModus(gruppeBO).getUnusedSpiele(gruppeBO);
	}

	/**
	 * optimiert fuer ScheduleGen
	 */
	public static Vector getUnusedSpieleGen(SportGroup gruppeBO) {
		return getModus(gruppeBO).getUnusedSpieleGen(gruppeBO);
	}

	/**
	 * Calculates all teams of this group that can act as referees in this turn
	 * @param gruppeBO
	 * @return
	 */
	public static Vector getMglReferee (SportGroup gruppeBO) {
		return getModus(gruppeBO).getMglReferee(gruppeBO);
	}

	/**
	 * Gibt alle moeglichen Mannschaften edn Modus f�r eine bestimmte Gruppe zur�ck
	 * ab einer bestimmten Zeit (KO-Spiel!)
	 * @return
	 */
	public static Vector<Team> getMannschaften (SportGroup gruppeBO, Date vondatum) {
		return getModus(gruppeBO).getMannschaften(gruppeBO, vondatum);
	}

	/**
	 * Auswahl �ber Paarungen oder Mannschaften?
	 * @return
	 */
	public static boolean isShowPaarung (SportGroup gruppeBO) {
		return getModus(gruppeBO).isShowPaarung();
	}

	/**
	 * KO-Modus System?
	 * @return
	 */
	public static boolean isKoSystem (SportGroup gruppeBO) {
		return getModus(gruppeBO).isKoSystem();
	}

}
