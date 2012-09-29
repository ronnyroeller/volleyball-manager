/*
 * Created on 14.07.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.sport.server.remote;

import java.util.Map;
import java.util.Set;
import java.util.Vector;

import com.sport.core.bo.Field;
import com.sport.core.bo.LicenceBO;
import com.sport.core.bo.Ranking;
import com.sport.core.bo.SpielplanTableEntryBO;
import com.sport.core.bo.Tournament;
import com.sport.core.bo.TournamentSystem;
import com.sport.core.bo.UserBO;
import com.sport.server.ejb.HomeGetter;


/**
 * @author ronny
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TurnierRemote {

	public static Set<Ranking> getPlatzierungenByTurnierBO(Tournament turnierBO)
			throws Exception {
		return HomeGetter.getTurnierSessionHome().create()
				.getPlatzierungenByTurnierBO(turnierBO);
	}

	public static LicenceBO getLicenceBO() throws Exception {
		return HomeGetter.getTurnierSessionHome().create().getLicenceBO();
	}

	public static Tournament getTurnierById(long turnierid) throws Exception {
		return HomeGetter.getTurnierSessionHome().create().getTurnierById(
				turnierid);
	}

	public static Tournament getTurnierByLinkid(String linkid) throws Exception {
		return HomeGetter.getTurnierSessionHome().create().getTurnierByLinkid(
				linkid);
	}

	public static Set<Tournament> getTurniereByUserid(long userid) throws Exception {
		return HomeGetter.getTurnierSessionHome().create().getTurniereByUserid(
				userid);
	}

	public static Tournament saveByTurnierBO(Tournament turnierBO, UserBO userBO)
			throws Exception {
		return HomeGetter.getTurnierSessionHome().create().saveByTurnierBO(
				turnierBO, userBO);
	}

	public static void removeByTurnierBO(Tournament turnierBO) throws Exception {
		HomeGetter.getTurnierSessionHome().create()
				.removeByTurnierBO(turnierBO);
	}

	public static Map<String, Object> getGruppenMannschaftenSpieleByTurnierBO(
			Tournament turnierBO, boolean alsoLogmannschaften) throws Exception {
		return HomeGetter.getTurnierSessionHome().create()
				.getGruppenMannschaftenSpieleByTurnierBO(turnierBO,
						alsoLogmannschaften);
	}

	public static Vector<Field> getSpielplaetzeByTurnierid(long turnierid)
			throws Exception {
		return HomeGetter.getTurnierSessionHome().create()
				.getSpielplaetzeByTurnierid(turnierid);
	}

	public static void setSpielplaetzeByTurnierId(long turnierid,
			Object[] spielplaetzeBO) throws Exception {
		HomeGetter.getTurnierSessionHome().create().setSpielplaetzeByTurnierId(
				turnierid, spielplaetzeBO);
	}

	public static void setMannschaftenByTurnierid(long turnierid,
			Object[] mannschaftenBO) throws Exception {
		HomeGetter.getTurnierSessionHome().create()
				.setMannschaftenByTurnierid(turnierid, mannschaftenBO);
	}

	public static Set<TournamentSystem> getModi() throws Exception {
		return HomeGetter.getTurnierSessionHome().create().getModi();
	}

	public static void setGruppenMannschaftenByTurnierid(long turnierid,
			Object[] gruppenBO) throws Exception {
		HomeGetter.getTurnierSessionHome().create()
				.setGruppenMannschaftenByTurnierid(turnierid, gruppenBO);
	}

	public static void setPlatzierungenByTurnierid(long turnierid,
			Object[] platzierungenBO) throws Exception {
		HomeGetter.getTurnierSessionHome().create()
				.setPlatzierungenByTurnierid(turnierid, platzierungenBO);
	}

	public static Vector<SpielplanTableEntryBO> getSpielplanTableEntrysByTurnierBO(Tournament turnierBO)
			throws Exception {
		return HomeGetter.getTurnierSessionHome().create()
				.getSpielplanTableEntrysByTurnierBO(turnierBO);
	}

	public static void setSpieleByTurnierid(long turnierid, Object[] spieleBO)
			throws Exception {
		HomeGetter.getTurnierSessionHome().create().setSpieleByTurnierid(
				turnierid, spieleBO);
	}

	public static Map<String, Object> getSpielplanTableEntrysGruppenByTurnierBO(
			Tournament turnierBO) throws Exception {
		return HomeGetter.getTurnierSessionHome().create()
				.getSpielplanTableEntrysGruppenByTurnierBO(turnierBO);
	}

}
