package com.sport.server.pdf;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;


import org.apache.log4j.Logger;

import com.sport.core.bo.Field;
import com.sport.core.bo.Ranking;
import com.sport.core.bo.SetResult;
import com.sport.core.bo.SportGroup;
import com.sport.core.bo.SportMatch;
import com.sport.core.bo.Team;
import com.sport.core.bo.Tournament;
import com.sport.core.bo.TournamentHolder;
import com.sport.core.bo.TournamentSystem;
import com.sport.server.persistency.tur.Gruppe;
import com.sport.server.persistency.tur.Mannschaft;
import com.sport.server.persistency.tur.Platzierung;
import com.sport.server.persistency.tur.Satz;
import com.sport.server.persistency.tur.Spiel;
import com.sport.server.persistency.tur.Spielplatz;
import com.sport.server.persistency.tur.Turnier;

/**
 * Reads *.tur files to business objects (by passing the persistency layer)
 * 
 * @author Ronny
 * 
 */
public class TurReader {

	private static final Logger LOG = Logger.getLogger(TurReader.class);

	// English format: 10/31/09 2:00 PM
	private static final DateFormat DATE_FORMAT_ENGLISH = new SimpleDateFormat(
			"MM/dd/yy hh:mm aa");

	// German format 31.10.09 14:00
	private static final DateFormat DATE_FORMAT_GERMAN = new SimpleDateFormat(
			"dd.MM.yy HH:mm");

	/**
	 * Maps tournament system IDs to instances
	 */
	private Map<Integer, TournamentSystem> tournamentSystems;
	
	public TurReader() {
		tournamentSystems = new HashMap<Integer, TournamentSystem>();
		addTournamentSystem(TournamentSystem.GRUPPENMODUS, "modusbo_groupmode");
		addTournamentSystem(TournamentSystem.GRUPPE_RUECKSPIELMODUS,"modusbo_groupmodeback");
		addTournamentSystem(TournamentSystem.KOMODUS,"modusbo_komode");
		addTournamentSystem(TournamentSystem.DOPPELKOMODUS,"modusbo_doppelkomode");
	}

	/**
	 * Adds one tournament system to the list of known systems
	 * @param id
	 * @param name
	 */
	private void addTournamentSystem(int id, String name) {
		TournamentSystem tournamentSystem = new TournamentSystem();
		tournamentSystem.setId(id);
		tournamentSystem.setName(name);
		tournamentSystems.put(id, tournamentSystem);
	}
	
	private TournamentHolder read(Turnier turnier) throws Exception {
		TournamentHolder tournamentHolder = new TournamentHolder();
		
		Tournament tournament = new Tournament();
		tournamentHolder.setTournament(tournament);

		tournament.setName(turnier.getName());
		tournament.setDate(extractDate(turnier.getDatum()));
		tournament.setPointsPerSet(turnier.getPunkteprosatz());
		tournament.setPointsPerMatch(turnier.getPunkteprospiel());
		tournament.setPointsPerTie(turnier
				.getPunkteprounentschiedenspiel());
		tournament.setDurationMatch(turnier.getSpieldauer());
		tournament.setDurationBreak(turnier.getPausedauer());
		tournament.setDurationProjectorSwitch(turnier
				.getBeamerumschaltzeit());
		tournament.setBannerLink(turnier.getBannerLink());
		tournament.setText(turnier.getText());

		// if link-id exists in another tournament -> change the new one
		String linkid = turnier.getLinkid();

		/*
		 * TO BE HANDLED IN PERSISTENCY LAYER
		 * 
		boolean noConflict = false;
		while (!noConflict) {
			if (!HomeGetter.getTurnierSessionHome().create()
					.existsTurnierByLinkid(linkid).booleanValue()) {
				noConflict = true;
			} else {
				linkid += "#";
			}
		}
		*/
		tournament.setLinkid(linkid);

		// Cache IDs with objects -> to link them afterwards (e.g. assign match
		// to a group)
		Map<Integer, Field> spielplatzMap = new HashMap<Integer, Field>();
		Map<Integer, SportGroup> gruppeMap = new HashMap<Integer, SportGroup>();
		Map<Integer, Team> mannschaftMap = new HashMap<Integer, Team>();
		Map<Integer, SportMatch> spielMap = new HashMap<Integer, SportMatch>();

		// Read fields
		for (Spielplatz spielplatz : turnier.getSpielplatz()) {
			Field field = new Field();
			field.setId(spielplatz.getId());
			// field.setTurnier(tournament);
			field.setName(spielplatz.getName());

			spielplatzMap.put(spielplatz.getId(), field);
			tournamentHolder.addField(field);
		}

		// Read groups
		for (Gruppe gruppe : turnier.getGruppe()) {
			SportGroup sportGroup = new SportGroup();
			sportGroup.setId(gruppe.getId());
			sportGroup.setTournament(tournament);
			sportGroup.setName(gruppe.getName());
			sportGroup.setColor(gruppe.getFarbe());
			sportGroup.setSort(gruppe.getSort());
			
			TournamentSystem tournamentSystem = tournamentSystems.get(gruppe.getModusId());
			sportGroup.setTournamentSystem(tournamentSystem);

			sportGroup.setMannschaften(new Vector<Team>());
			sportGroup.setMatches(new Vector<SportMatch>());
			
			gruppeMap.put(gruppe.getId(), sportGroup);
			if (LOG.isDebugEnabled())
				LOG.debug("Read group " + gruppe.getId() + " (internal ID: "
						+ sportGroup.getId() + ")");

			tournamentHolder.addGroup(sportGroup);
		}

		// Stores references to logicalTeam for which the matches have to be
		// filled out later on (can't be done now because matches aren't
		// created so far)
		Map<Team, Integer> missingMatchReferences = new HashMap<Team, Integer>();

		// Read teams
		for (Mannschaft mannschaft : turnier.getMannschaft()) {
			Team team = new Team();
			team.setId(mannschaft.getId());
			team.setName(mannschaft.getName());
			team.setTurnierBO(tournament);

			team.setSort(mannschaft.getSort());

			if (mannschaft.getGruppeId() > 0) {
				SportGroup gruppe = gruppeMap.get(mannschaft.getGruppeId());
				if (gruppe == null)
					LOG.error("Can't find group " + mannschaft.getGruppeId()
							+ ", which should be linked to team "
							+ mannschaft.getId() + ".");
				team.setGruppeBO(gruppe);
				// Backlink
				gruppe.addMannschaft(team);
			}

			String loggruppeId = mannschaft.getLoggruppeId();
			if (loggruppeId != null && !loggruppeId.isEmpty()) {
				// Convert to integer -> otherwise hashcode differs, and we
				// won't find it in the map
				SportGroup logGruppe = gruppeMap.get(Integer
						.valueOf(loggruppeId));
				if (logGruppe == null)
					LOG.error("Can't find log group "
							+ loggruppeId
							+ ", which should be linked to team "
							+ mannschaft.getId()
							+ ". Known groups: "
							+ new ArrayList<Integer>(gruppeMap.keySet())
									.toString());
				team.setLoggruppeBO(logGruppe);
			}

			if (mannschaft.getLogsort() > 0)
				team.setLogsort(mannschaft.getLogsort());

			String logspielId = mannschaft.getLogspielId();
			if (logspielId != null && !logspielId.isEmpty())
				missingMatchReferences.put(team, Integer
						.valueOf(logspielId));

			mannschaftMap.put(mannschaft.getId(), team);
			tournamentHolder.addTeam(team);
		}

		// Read matches
		for (Spiel spiel : turnier.getSpiel()) {
			SportMatch match = new SportMatch();
			match.setId(spiel.getId());
			match.setStartDate(extractDate(spiel.getVondatum()));
			match.setEndDate(extractDate(spiel.getBisdatum()));

			// Link field
			match.setField(spielplatzMap
					.get(spiel.getSpielplatzId()));

			// Link group
			SportGroup group = gruppeMap.get(spiel.getGruppeId());
			match.setGroup(group);
			// Backlink
			group.addMatch(match);

			// Link teams
			match.setTeam1(mannschaftMap.get(spiel
					.getMannschaft1Id()));
			match.setTeam2(mannschaftMap.get(spiel
					.getMannschaft2Id()));
			if (spiel.getSchiedsrichterId() > 0)
				match.setReferee(mannschaftMap.get(spiel
						.getSchiedsrichterId()));

			// Read results of sets
			match.setSetResults(new Vector<SetResult>());
			for (Satz satz : spiel.getSatz()) {
				SetResult setResult = new SetResult();
				setResult.setPoints1(satz.getPunkte1());
				setResult.setPoints2(satz.getPunkte2());
				setResult.setSetNr(satz.getSatznr());

				// Spiel verbinden
				match.addSatz(setResult);
			}

			spielMap.put(spiel.getId(), match);
			tournamentHolder.addMatch(match);
		}

		// Zwischengespeicherte Verbindungen zwischen Mannschaft und Logspiel
		// schreiben nicht eher moeglich, da erst Spiele eingelesen werden
		// muessen!
		for (Team team : missingMatchReferences.keySet()) {
			Integer matchId = missingMatchReferences.get(team);
			SportMatch match = spielMap.get(matchId);

			if (match == null)
				LOG.error("Can't find match " + matchId
						+ ", which is linked to team "
						+ team.getId());

			team.setLogspielBO(match);
		}

		// Read ranking
		for (Platzierung platzierung : turnier.getPlatzierung()) {
			Ranking ranking = new Ranking();
			// ranking.setTurnier(tournament);
			ranking.setRank(platzierung.getPlatznr());

			// Mannschaften verbinden
			if (platzierung.getMannschaftId() > 0) {
				Team team = mannschaftMap.get(platzierung
						.getMannschaftId());

				if (team == null)
					LOG.error("Couldn't find team '"
							+ platzierung.getMannschaftId() + "' for rank "
							+ platzierung.getPlatznr());

				ranking.setMannschaft(team);
			}

			tournamentHolder.addRanking(ranking);
		}

		return tournamentHolder;
	}

	public TournamentHolder read(String xml) throws Exception {
		InputStream turStream = new ByteArrayInputStream(xml
				.getBytes("UTF-8"));
		
		return read(turStream);
	}

	public TournamentHolder read(InputStream turStream) throws Exception {
		Turnier turnier = readStream(turStream);

		return read(turnier);
	}

	/**
	 * Reads a *.tur file into object structure (uses JAXB)
	 * 
	 * @param turFile
	 * @return
	 */
	private Turnier readStream(InputStream turStream) {
		Turnier turnier = null;

		String packageName = Turnier.class.getPackage().getName();
		try {
			JAXBContext jc = JAXBContext.newInstance(packageName);
			Unmarshaller u = jc.createUnmarshaller();
			turnier = (Turnier) u.unmarshal(turStream);
		} catch (JAXBException e) {
			LOG.error("Can't create JAXB context for package '" + packageName
					+ "'");
		}

		return turnier;
	}

	/**
	 * Extracts a date from German/English serialization
	 * 
	 * @return
	 */
	protected Date extractDate(String dateStr) {
		try {
			return DATE_FORMAT_ENGLISH.parse(dateStr);
		} catch (ParseException e) {
			// Try German format if English didn't work
			try {
				return DATE_FORMAT_GERMAN.parse(dateStr);
			} catch (ParseException e1) {
				LOG.error("Can't parse date '" + dateStr + "'");
			}
		}

		return null;
	}

	public TournamentHolder read(File turFile) throws Exception {
		FileInputStream fis = new FileInputStream(turFile);
		
		return read(fis);
	}

}
