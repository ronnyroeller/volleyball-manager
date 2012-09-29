package com.sport.server.persistency.xml;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;


import org.apache.commons.io.FileUtils;

import com.sport.core.bo.Field;
import com.sport.core.bo.Ranking;
import com.sport.core.bo.SetResult;
import com.sport.core.bo.SportGroup;
import com.sport.core.bo.SportMatch;
import com.sport.core.bo.Team;
import com.sport.core.bo.Tournament;
import com.sport.core.bo.TournamentHolder;
import com.sport.core.bo.comparators.GruppenComparator;
import com.sport.core.bo.comparators.PlatzierungenComparator;
import com.sport.core.bo.comparators.SatzComparator;
import com.sport.core.bo.comparators.SpielComparator;
import com.sport.server.persistency.tur.Gruppe;
import com.sport.server.persistency.tur.Mannschaft;
import com.sport.server.persistency.tur.Platzierung;
import com.sport.server.persistency.tur.Satz;
import com.sport.server.persistency.tur.Spiel;
import com.sport.server.persistency.tur.Spielplatz;
import com.sport.server.persistency.tur.Turnier;

/**
 * Writes tournament to *.tur file
 * 
 * @author Ronny
 * 
 */
public class TurWriter {

	// English format: 10/31/09 2:00 PM
	private static final DateFormat DATE_FORMAT_ENGLISH = new SimpleDateFormat(
			"MM/dd/yy hh:mm aa");

	/**
	 * Converts date to string
	 * 
	 * @param date
	 * @return
	 */
	private String convertDate(Date date) {
		return DATE_FORMAT_ENGLISH.format(date);
	}

	/**
	 * Creates node from tournament
	 */
	private Turnier createTurnier(TournamentHolder tournamentHolder) {
		Tournament tournament = tournamentHolder.getTournament();

		String formattedDate = convertDate(tournament.getDate());

		Turnier turnier = new Turnier();

		// Add general settings
		turnier.setLinkid(tournament.getLinkid());
		turnier.setName(tournament.getName());
		turnier.setDatum(formattedDate);
		turnier.setPunkteprosatz((int) tournament.getPointsPerSet());
		turnier.setPunkteprospiel((int) tournament.getPointsPerMatch());
		turnier.setPunkteprounentschiedenspiel(tournament.getPointsPerTie());
		turnier.setSpieldauer((int) tournament.getDurationMatch());
		turnier.setPausedauer((int) tournament.getDurationBreak());
		turnier.setBeamerumschaltzeit((int) tournament
				.getDurationProjectorSwitch());
		turnier.setBannerLink(tournament.getBannerLink());
		turnier.setText(tournament.getText());

		// Add fields
		for (Field field : tournamentHolder.getFields()) {
			Spielplatz spielplatz = new Spielplatz();
			spielplatz.setId((int) field.getId());
			spielplatz.setName(field.getName());

			turnier.getSpielplatz().add(spielplatz);
		}

		// Add groups
		Vector<SportGroup> groups = tournamentHolder.getGroups();
		// Write in defined order -> easier for unit testing
		Collections.sort(groups, new GruppenComparator());
		for (SportGroup group : groups) {
			Gruppe gruppe = new Gruppe();
			gruppe.setId((int) group.getId());
			gruppe.setSort(group.getSort());
			gruppe.setName(group.getName());
			gruppe.setFarbe(group.getColor());
			gruppe.setModusId((int) group.getTournamentSystem().getId());

			turnier.getGruppe().add(gruppe);
		}

		// Add matches
		for (SportGroup group : groups) {

			Vector<SportMatch> matches = group.getMatches();
			Collections.sort(matches, new SpielComparator());
			for (SportMatch match : matches) {
				Spiel spiel = new Spiel();

				spiel.setId((int) match.getId());
				spiel.setVondatum(convertDate(match.getStartDate()));
				spiel.setBisdatum(convertDate(match.getEndDate()));
				spiel.setGruppeId((int) group.getId());

				Team mannschaft1 = match.getTeam1();
				Team mannschaft2 = match.getTeam2();
				Team referee = match.getReferee();

				if (mannschaft1 != null)
					spiel.setMannschaft1Id((int) mannschaft1.getId());
				if (mannschaft2 != null)
					spiel.setMannschaft2Id((int) mannschaft2.getId());
				if (referee != null)
					spiel.setSchiedsrichterId((int) referee.getId());

				spiel.setSpielplatzId((int) match.getField().getId());

				turnier.getSpiel().add(spiel);

				// Add sets
				Vector<SetResult> setResults = match.getSetResults();
				Collections.sort(setResults, new SatzComparator());
				for (SetResult setResult : setResults) {
					Satz satz = new Satz();
					satz.setPunkte1(setResult.getPoints1());
					satz.setPunkte2(setResult.getPoints2());
					satz.setSatznr(setResult.getSetNr());

					spiel.getSatz().add(satz);
				}
			}

		}

		// Add rankings
		Vector<Ranking> rankings = tournamentHolder.getRankings();
		Collections.sort(rankings, new PlatzierungenComparator());
		for (Ranking ranking : rankings) {
			Platzierung platzierung = new Platzierung();
			platzierung.setPlatznr((int) ranking.getRank());
			platzierung.setMannschaftId((int) ranking.getMannschaft().getId());

			turnier.getPlatzierung().add(platzierung);
		}

		// Add teams
		List<Team> teamsList = new LinkedList<Team>(tournamentHolder.getTeams());
		Collections.sort(teamsList, new Comparator<Team>() {
			@Override
			public int compare(Team team1, Team team2) {
				return new Long(team1.getId()).compareTo(team2.getId());
			}
		});
		for (Team team : teamsList) {
			Mannschaft mannschaft = createMannschaft(team);

			turnier.getMannschaft().add(mannschaft);
		}

		return turnier;
	}

	/**
	 * Creates a mannschaft entry in *.tur file for a local team
	 * 
	 * @param group
	 * @param team
	 * @return
	 */
	private Mannschaft createMannschaft(Team team) {
		Mannschaft mannschaft = new Mannschaft();
		mannschaft.setId((int) team.getId());

		// Add sort if available
		mannschaft.setSort(team.getSort());

		// Link to group
		SportGroup group = team.getGruppeBO();
		if (group != null)
			mannschaft.setGruppeId((int) group.getId());

		if (team.getLoggruppeBO() != null) {
			// Link to Log-Gruppe
			mannschaft.setLoggruppeId(String.valueOf(team.getLoggruppeBO()
					.getId()));
		} else if (team.getLogspielBO() != null) {
			// Link to Log-Spiel
			mannschaft.setLogspielId(String.valueOf(team.getLogspielBO()
					.getId()));
		} else {
			// Only write name if it's not a logical team
			mannschaft.setName(team.getName());
		}

		// Link to Log-sort
		mannschaft.setLogsort(team.getLogsort());

		return mannschaft;
	}

	/**
	 * @return XML string for the *.tur file
	 * @throws TransformerException
	 * @throws ParserConfigurationException
	 * @throws JAXBException
	 */
	public String write(TournamentHolder tournamentHolder)
			throws TransformerException, ParserConfigurationException,
			JAXBException {
		Turnier turnier = createTurnier(tournamentHolder);

		StringWriter sw = new StringWriter();

		String packageName = Turnier.class.getPackage().getName();
		JAXBContext jc = JAXBContext.newInstance(packageName);
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.marshal(turnier, sw);

		String xml = sw.toString();

		return xml;
	}

}
