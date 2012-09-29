package com.sport.server.persistency.ejb.managers;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.ejb.FinderException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.sport.core.bo.Field;
import com.sport.core.bo.Ranking;
import com.sport.core.bo.SportGroup;
import com.sport.core.bo.SportMatch;
import com.sport.core.bo.Team;
import com.sport.core.bo.Tournament;
import com.sport.core.bo.TournamentHolder;
import com.sport.core.bo.comparators.SpielplatzComparator;
import com.sport.server.ejb.HomeGetter;
import com.sport.server.ejb.interfaces.GruppeLocal;
import com.sport.server.ejb.interfaces.TurnierLocal;
import com.sport.server.ejb.session.BOCreator;
import com.sport.server.persistency.ejb.maps.FieldMap;
import com.sport.server.persistency.ejb.maps.GroupMap;
import com.sport.server.persistency.ejb.maps.MatchMap;
import com.sport.server.persistency.ejb.maps.RankingMap;
import com.sport.server.persistency.ejb.maps.TeamMap;

/**
 * Handles mapping of persistency layer (EJB) to business objects
 * 
 * @author Ronny
 * 
 */
public class PersistencyManager {

	private static final Logger LOG = Logger
			.getLogger(PersistencyManager.class);

	private FieldManager fieldManager = new FieldManager();
	private GroupManager groupManager = new GroupManager();
	private MatchManager matchManager = new MatchManager();
	private RankingManager rankingManager = new RankingManager();
	private TeamManager teamManager = new TeamManager();

	/**
	 * Returns all tournament objects with their linking
	 * 
	 * @param turnierid
	 * @param forceLoadLogTeams
	 *            ... also give logical teams
	 * @return
	 */
	public TournamentHolder findByTournamentId(long tournamentId) {
		TournamentHolder tournamentHolder = new TournamentHolder();

		try {
			TurnierLocal turnierLocal = HomeGetter.getTurnierHome().findById(
					tournamentId);
			Tournament tournament = BOCreator.createTurnierBO(turnierLocal);

			Collection<GruppeLocal> gruppenLocal = null;

			gruppenLocal = HomeGetter.getGruppeHome().findByTurnierid(
					tournamentId);
			if (gruppenLocal == null)
				LOG.error("Couldn't load groups for tournament '" + tournament
						+ "'.");

			// Load raw objects without linking
			FieldMap fields = fieldManager.load(turnierLocal);
			GroupMap groups = groupManager.load(turnierLocal);
			TeamMap teams = teamManager.load(turnierLocal);
			MatchMap matches = matchManager.load(turnierLocal);
			RankingMap rankings = rankingManager.load(turnierLocal);

			// Link objects to each other
			groupManager.loadLink(tournament, groups, teams, matches);
			matchManager.link(teams, matches, fields);
			teamManager.link(groups, teams, matches);
			rankingManager.link(rankings, teams);

			// Store results
			tournamentHolder.setTournament(tournament);
			for (SportGroup group : groups.values())
				tournamentHolder.addGroup(group);
			for (Team team : teams.values())
				tournamentHolder.addTeam(team);
			for (SportMatch match : matches.values())
				tournamentHolder.addMatch(match);
			for (Field field : fields.values())
				tournamentHolder.addField(field);
			for (Ranking ranking : rankings.values())
				tournamentHolder.addRanking(ranking);

			// Ensure that the fields in the tournamentHolder are in sync with
			// the result table entries
			Collections.sort(tournamentHolder.getFields(),
					new SpielplatzComparator());
		} catch (RemoteException e) {
			LOG.error("Couldn't connect to server.", e);
		} catch (FinderException e) {
			LOG.error("Couldn't load objects.", e);
		} catch (NamingException e) {
			LOG.error("Couldn't load objects.", e);
		}

		return tournamentHolder;
	}

	/**
	 * Stores just the fields
	 * 
	 * @param tournamentHolder
	 */
	public void storeFields(TournamentHolder tournamentHolder) {
		Tournament tournament = tournamentHolder.getTournament();
		try {
			TurnierLocal turnierLocal = HomeGetter.getTurnierHome().findById(
					tournament.getId());

			fieldManager.store(turnierLocal, tournamentHolder);
		} catch (RemoteException e) {
			LOG.error("Couldn't connect to server.", e);
		} catch (FinderException e) {
			LOG.error("Couldn't store objects.", e);
		} catch (NamingException e) {
			LOG.error("Couldn't store objects.", e);
		}

	}

	/**
	 * Creates return structure of EJB session bean (legacy support)
	 * 
	 * @param tournamentHolder
	 * @param isKeepLogTeams
	 *            defines if logical teams should be removed
	 */
	public static Map<String, Object> createResultMap(
			TournamentHolder tournamentHolder, boolean isKeepLogTeams) {
		// Remove logical teams if requested
		Vector<Team> filteredList = new Vector<Team>();
		if (!isKeepLogTeams) {
			for (Team team : tournamentHolder.getTeams())
				if (!com.sport.server.persistency.ejb.TeamManager
						.isLogTeam(team))
					filteredList.add(team);
			tournamentHolder.setTeams(filteredList);
		}

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("gruppen", tournamentHolder.getGroups().toArray());
		resultMap.put("mannschaften", tournamentHolder.getTeams().toArray());
		resultMap.put("spiele", tournamentHolder.getMatches().toArray());

		Map<Long, SportGroup> groupsMap = new HashMap<Long, SportGroup>();
		for (SportGroup group : tournamentHolder.getGroups())
			groupsMap.put(group.getId(), group);
		resultMap.put("gruppenMap", groupsMap);

		Map<Long, Team> teamsMap = new HashMap<Long, Team>();
		for (Team team : tournamentHolder.getTeams())
			teamsMap.put(team.getId(), team);
		resultMap.put("mannschaftenMap", teamsMap);

		Map<Long, SportMatch> matchesMap = new HashMap<Long, SportMatch>();
		for (SportMatch match : tournamentHolder.getMatches())
			matchesMap.put(match.getId(), match);
		resultMap.put("spieleMap", matchesMap);

		return resultMap;
	}

}
