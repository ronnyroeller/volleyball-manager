package com.sport.server.ejb.session;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;


import org.apache.log4j.Logger;

import com.sport.core.bo.LicenceBO;
import com.sport.core.bo.Ranking;
import com.sport.core.bo.SportGroup;
import com.sport.core.bo.SportMatch;
import com.sport.core.bo.Team;
import com.sport.core.bo.Tournament;
import com.sport.core.bo.TournamentHolder;
import com.sport.core.bo.UserBO;
import com.sport.server.license.Licence;
import com.sport.server.persistency.ejb.BOManipulator;
import com.sport.server.persistency.ejb.GroupManager;
import com.sport.server.persistency.ejb.RankingManager;
import com.sport.server.persistency.ejb.ResultManager;
import com.sport.server.persistency.ejb.SportMatchManager;
import com.sport.server.persistency.ejb.TeamManager;
import com.sport.server.persistency.ejb.TournamentManager;
import com.sport.server.persistency.ejb.managers.PersistencyManager;

/**
 * @ejb:bean name="TurnierSession" type="Stateless"
 *           jndi-name="TurnierSessionHome"
 *           local-jndi-name="TurnierSessionLocal" view-type="both"
 * 
 */
public class TurnierSessionBean implements SessionBean {

	private static final Logger LOG = Logger
			.getLogger(TurnierSessionBean.class);

	private SessionContext mContext;

	private TournamentManager tournamentManager = new TournamentManager();
	private RankingManager rankingManager = new RankingManager();
	private SportMatchManager sportMatchManager = new SportMatchManager();
	private TeamManager teamManager = new TeamManager();
	private GroupManager groupManager = new GroupManager();
	private ResultManager resultManager = new ResultManager();
	private PersistencyManager persistencyManager = new PersistencyManager();
	private BOManipulator boManipulator = new BOManipulator();

	/**
	 * gibt Lizenzinformationen des Servers zurueck
	 * 
	 * @ejb:interface-method view-type="both"
	 */
	public LicenceBO getLicenceBO() {
		LicenceBO licenceBO = Licence.getLicenceBO();
		return licenceBO;
	}

	/**
	 * gibt alle Turniere eines bestimmten Nutzers zur�ck
	 * 
	 * @ejb:interface-method view-type="both"
	 */
	public Set getTurniereByUserid(long userid) {
		return tournamentManager.findByUserId(userid);
	}

	/**
	 * gibt Turnier mit bestimmter ID zur�ck
	 * 
	 * @ejb:interface-method view-type="both"
	 */
	public Tournament getTurnierById(long turnierid) {
		return tournamentManager.findById(turnierid);
	}

	/**
	 * gibt Turnier mit bestimmter Link-ID zur�ck
	 * 
	 * @param linkid
	 * @param noException
	 *            ... if false -> no StackTrace will be output if not found
	 * @return @ejb:interface-method view-type="both"
	 */
	public Tournament getTurnierByLinkid(String linkid, boolean noStackTrace) {
		return tournamentManager.findByLinkid(linkid, noStackTrace);
	}

	/**
	 * gibt Turnier mit bestimmter Link-ID zur�ck
	 * 
	 * @ejb:interface-method view-type="both"
	 */
	public Tournament getTurnierByLinkid(String linkid) {
		return getTurnierByLinkid(linkid, true);
	}

	/**
	 * testet ob Turnier mit bestimmter Link-ID existiert
	 * 
	 * @ejb:interface-method view-type="both"
	 */
	public Boolean existsTurnierByLinkid(String linkid) {
		Tournament turnierBO = getTurnierByLinkid(linkid, true);
		return new Boolean(turnierBO != null);
	}

	/**
	 * gibt alle Spielplaetze eine bestimmtes Turnier zur�ck
	 * 
	 * @ejb:interface-method view-type="both"
	 */
	public Vector getSpielplaetzeByTurnierid(long tournamentId) {
		TournamentHolder tournamentHolder = persistencyManager
				.findByTournamentId(tournamentId);

		return tournamentHolder.getFields();
	}

	/**
	 * gibt alle Platzierungen eines bestimmtes Turnier zur�ck
	 * 
	 * @ejb:interface-method view-type="both"
	 */
	public Set getPlatzierungenByTurnierBO(Tournament tournament) {
		TournamentHolder tournamentHolder = persistencyManager
				.findByTournamentId(tournament.getId());
		return new HashSet(tournamentHolder.getRankings());
	}

	/**
	 * gibt alle Gruppen-Modi zur�ck
	 * 
	 * @ejb:interface-method view-type="both"
	 */
	public Set getModi() {
		return tournamentManager.getTournamentSystems();
	}

	/**
	 * gibt alle Gruppen mit Mannschaftenverlinkung UND alle Mannschaften mit
	 * Gruppen zurueck
	 * 
	 * @ejb:interface-method view-type="both"
	 */
	public Map getGruppenMannschaftenSpieleByTurnierBOMap(Tournament turnierBO,
			boolean alsoLogmannschaften) {
		TournamentHolder tournamentHolder = persistencyManager
				.findByTournamentId(turnierBO.getId());

		return persistencyManager.createResultMap(tournamentHolder,
				alsoLogmannschaften);
	}

	/**
	 * @param turnierid
	 * @param alsoLogmannschaften
	 *            ... auch Mannschaften liefern, die logisch berechnet werden
	 * @return gibt alle Gruppen mit Mannschaftenverlinkung (ohne Maps!) UND
	 *         alle Mannschaften mit Gruppen zurueck
	 * 
	 * @ejb:interface-method view-type="both"
	 */
	public Map getGruppenMannschaftenSpieleByTurnierBO(Tournament turnierBO,
			boolean alsoLogmannschaften) {
		TournamentHolder tournamentHolder = persistencyManager
				.findByTournamentId(turnierBO.getId());

		Map resultMap = persistencyManager.createResultMap(tournamentHolder,
				alsoLogmannschaften);
		resultMap.remove("gruppenMap");
		resultMap.remove("mannschaftenMap");
		resultMap.remove("spieleMap");
		return resultMap;
	}

	/**
	 * gibt vorbereitete Spielplan-Struktur zur�ck
	 * 
	 * @ejb:interface-method view-type="both"
	 */
	public Vector getSpielplanTableEntrysByTurnierBO(Tournament turnierBO) {
		return getSpielplanTableEntrysByTurnierBO(turnierBO, null);
	}

	/**
	 * gibt vorbereitete Spielplan-Struktur zur�ck
	 * 
	 * @ejb:interface-method view-type="both"
	 */
	public Vector getSpielplanTableEntrysByTurnierBO(Tournament turnierBO,
			Vector selGruppenIds) {
		Map result = getSpielplanTableEntrysGruppenByTurnierBO(turnierBO,
				selGruppenIds);

		return (Vector) result.get("tableEntries");
	}

	/**
	 * gibt vorbereitete Spielplan-Struktur und verlinkte Gruppen zur�ck
	 * 
	 * @ejb:interface-method view-type="both"
	 */
	public Map getSpielplanTableEntrysGruppenByTurnierBO(Tournament turnierBO,
			Vector selGruppenIds) {
		com.sport.server.persistency.ejb.ResultManager.Result result = resultManager
				.getSpielplanTableEntrysGruppenByTurnierBO(turnierBO,
						selGruppenIds);
		Map resultMap = new HashMap();
		resultMap.put("tableEntries", result.tableEntries);
		resultMap.put("gruppen", result.groups);
		return resultMap;
	}

	/**
	 * gibt vorbereitete Spielplan-Struktur und verlinkte Gruppen zur�ck
	 * 
	 * @ejb:interface-method view-type="both"
	 */
	public Map getSpielplanTableEntrysGruppenByTurnierBO(Tournament turnierBO) {
		return getSpielplanTableEntrysGruppenByTurnierBO(turnierBO, null);
	}

	/**
	 * This method is used in the team panel
	 * 
	 * @param turnierid
	 * @param mannschaftenBO
	 * @param alsoLogmannschaften
	 *            Werden auch log. Mannschaften mit behandelt
	 * @param alsoAuslosung
	 *            Auch Mannschaften, die nicht in Gruppen sind (auf Auslosung
	 *            warten)
	 * 
	 *            schreibt alle Mannschaften eines bestimmten Turniers zur�ck
	 * 
	 * @ejb:interface-method view-type="both"
	 */
	public void setMannschaftenByTurnierid(long turnierid,
			Object[] mannschaftenBO) {
		Team[] teamsArray = new Team[mannschaftenBO.length];
		System.arraycopy(mannschaftenBO, 0, teamsArray, 0,
				mannschaftenBO.length);
		Vector teams = new Vector(Arrays.asList(teamsArray));
		teamManager.store(turnierid, teams, false,
				true);
	}

	/**
	 * l�scht alle Spiele in DB und schreibt neue zurueck! schreibt alle Spiele
	 * eines bestimmten Turniers zur�ck
	 * 
	 * @ejb:interface-method view-type="both"
	 */
	public void setSpieleByTurnierid(long turnierid, Object[] spieleBO) {
		SportMatch[] matches = new SportMatch[spieleBO.length];
		System.arraycopy(spieleBO, 0, matches, 0, spieleBO.length);
		sportMatchManager.setSpieleByTurnierid(turnierid, matches);
	}

	/**
	 * schreibt alle Gruppen eines bestimmten Turniers zur�ck
	 * 
	 * This is used in the group panel
	 * 
	 * @ejb:interface-method view-type="both"
	 */
	public void setGruppenMannschaftenByTurnierid(long tournamentId,
			Object[] gruppenBO) {
		SportGroup[] groups = new SportGroup[gruppenBO.length];
		System.arraycopy(gruppenBO, 0, groups, 0, gruppenBO.length);
		groupManager.store(tournamentId, groups);
	}

	/**
	 * schreibt alle Spielplaetze eines bestimmten Turniers zur�ck
	 * 
	 * @ejb:interface-method view-type="both"
	 */
	public void setSpielplaetzeByTurnierId(long tournamentId,
			Object[] spielplaetzeBO) {
		TournamentHolder tournamentHolder = persistencyManager
				.findByTournamentId(tournamentId);

		tournamentHolder = boManipulator.setFields(tournamentHolder,
				spielplaetzeBO);

		persistencyManager.storeFields(tournamentHolder);
	}

	/**
	 * schreibt alle Platzierungen eines bestimmten Turniers zur�ck
	 * 
	 * @ejb:interface-method view-type="both"
	 */
	public void setPlatzierungenByTurnierid(long turnierid,
			Object[] platzierungenBO) {
		Ranking[] rankings = new Ranking[platzierungenBO.length];
		System.arraycopy(platzierungenBO, 0, rankings, 0,
				platzierungenBO.length);
		rankingManager.store(turnierid, rankings);
	}

	/**
	 * speichert ein Turnier zur�ck bzw. legt es an, falls noch nicht vorhanden
	 * 
	 * @ejb:interface-method view-type="both"
	 */
	public Tournament saveByTurnierBO(Tournament turnierBO, UserBO userBO) {
		return tournamentManager.store(turnierBO, userBO);
	}

	/**
	 * l�scht ein Turnier
	 * 
	 * @ejb:interface-method view-type="both"
	 */
	public void removeByTurnierBO(Tournament turnierBO) {
		tournamentManager.remove(turnierBO.getId());
	}

	/**
	 * Create the Session Bean
	 * 
	 * @throws CreateException
	 * 
	 * @ejb:create-method view-type="remote"
	 */
	public void ejbCreate() throws CreateException {
	}

	/**
	 * Describes the instance and its content for debugging purpose
	 * 
	 * @return Debugging information about the instance and its content
	 */
	public String toString() {
		return "TurnierSessionBean [ " + " ]";
	}

	public void setSessionContext(SessionContext aContext) throws EJBException {
		mContext = aContext;
	}

	public void ejbActivate() throws EJBException {
	}

	public void ejbPassivate() throws EJBException {
	}

	public void ejbRemove() throws EJBException {
	}

}
