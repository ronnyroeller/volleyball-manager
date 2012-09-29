package com.sport.server.pdf;

import java.io.StringWriter;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;


import org.apache.log4j.Logger;

import com.sport.analyzer.GruppeErgebnisEntity;
import com.sport.analyzer.MannschaftAnalyzer;
import com.sport.analyzer.SpielAnalyzer;
import com.sport.analyzer.SpielAnalyzer.DetailedMatchResult;
import com.sport.analyzer.SpielAnalyzer.SetResults;
import com.sport.analyzer.impl.GruppeAnalyzerImpl;
import com.sport.analyzer.impl.MannschaftAnalyzerImpl;
import com.sport.analyzer.impl.SpielAnalyzerImpl;
import com.sport.core.bo.Field;
import com.sport.core.bo.LicenceBO;
import com.sport.core.bo.SetResult;
import com.sport.core.bo.SpielplanTableEntryBO;
import com.sport.core.bo.SportGroup;
import com.sport.core.bo.SportMatch;
import com.sport.core.bo.Tournament;
import com.sport.core.bo.TournamentHolder;
import com.sport.core.bo.comparators.GruppenComparator;
import com.sport.core.helper.Messages;
import com.sport.server.fop.Block;
import com.sport.server.fop.Blocks;
import com.sport.server.fop.Fields;
import com.sport.server.fop.FopField;
import com.sport.server.fop.FopGroup;
import com.sport.server.fop.FopMatch;
import com.sport.server.fop.FopMessages;
import com.sport.server.fop.FopSet;
import com.sport.server.fop.FopTeam;
import com.sport.server.fop.FopTournament;
import com.sport.server.fop.Groups;
import com.sport.server.fop.Info;
import com.sport.server.fop.License;
import com.sport.server.fop.Message;
import com.sport.server.fop.Referee;
import com.sport.server.fop.Sets;
import com.sport.server.fop.Team1;
import com.sport.server.fop.Team2;
import com.sport.server.persistency.ejb.ResultManager;
import com.sport.server.persistency.ejb.ResultManager.Result;

/**
 * Creates the XML for PDF generation (using FOP:
 * http://xmlgraphics.apache.org/fop/)
 * 
 * @author Ronny
 * 
 */
public class FopXmlGenerator {

	private static final Logger LOG = Logger.getLogger(FopXmlGenerator.class);

	private MannschaftAnalyzer mannschaftAnalyzer = MannschaftAnalyzerImpl
			.getInstance();
	private SpielAnalyzer spielAnalyzer = SpielAnalyzerImpl.getInstance();

	private ResultManager resultManager = new ResultManager();

	/**
	 * @throws NamingException
	 * @throws CreateException
	 * @throws RemoteException
	 */
	private FopTournament createTournament(LicenceBO licenceBO, TournamentHolder tournamentHolder,
			Vector<Long> selGruppenIds, Locale locale) throws RemoteException,
			CreateException, NamingException {
		DateFormat dateformater = DateFormat.getTimeInstance(DateFormat.SHORT,
				locale);

		Tournament tournament = tournamentHolder.getTournament();

		FopTournament fopTournament = new FopTournament();

		fopTournament.setInfo(new Info());
		fopTournament.getInfo().setName(tournament.getName());

		// Add license information
		License license = new License();
		license.setType(licenceBO.getLicencetype());
		license.setTypeLocale(licenceBO.getLicencetypeLocale());
		license.setFirstname(licenceBO.getFirstname());
		license.setLastname(licenceBO.getLastname());
		license.setCity(licenceBO.getCity());
		license.setCountry(licenceBO.getCountry());
		license.setOrganisation(licenceBO.getOrganisation());
		fopTournament.getInfo().setLicense(license);

		// Add fields
		fopTournament.setFields(new Fields());
		for (Field spielplatzBO : tournamentHolder.getFields()) {
			FopField field = new FopField();
			field.setName(spielplatzBO.getName());
			fopTournament.getFields().getFopField().add(field);
		}

		// Add groups
		Vector<SportGroup> groups = tournamentHolder.getGroups();
		Collections.sort(groups, new GruppenComparator());
		fopTournament.setGroups(new Groups());
		for (SportGroup group : groups) {
			FopGroup fopGroup = new FopGroup();
			fopGroup.setName(group.getName());
			fopGroup.setColor(group.getColor());
			fopGroup.setSystem(group.getTournamentSystem().getName());

			// Teams for the group
			int rank = 1;
			for (GruppeErgebnisEntity ergebnisBO : GruppeAnalyzerImpl
					.getInstance().getErgebnisDetails(group).ergebnisDetails) {
				FopTeam fopTeam = new FopTeam();
				fopTeam.setRank(rank++);
				fopTeam.setName(mannschaftAnalyzer.getRelName(ergebnisBO
						.getMannschaftBO()));

				fopTeam.setTotalMatches(ergebnisBO.getSpiele());
				fopTeam.setWonMatches(ergebnisBO.getPspiele());
				fopTeam.setLostMatches(ergebnisBO.getNspiele());
				fopTeam.setWonSets(ergebnisBO.getPsaetze());
				fopTeam.setLostSets(ergebnisBO.getNsaetze());
				fopTeam.setWonPoints(ergebnisBO.getPpunkte());
				fopTeam.setLostPoints(ergebnisBO.getNpunkte());
				fopTeam.setDiffPoints(ergebnisBO.getDiffpunkte());

				fopGroup.getFopTeam().add(fopTeam);
			}

			fopTournament.getGroups().getFopGroup().add(fopGroup);
		}

		// Add blocks
		Result result = resultManager
				.getSpielplanTableEntrysGruppenByTurnierBO(tournamentHolder,
						selGruppenIds);
		fopTournament.setBlocks(new Blocks());
		for (SpielplanTableEntryBO tableEntry : result.tableEntries) {
			Block block = new Block();
			block.setStartTime(dateformater.format(tableEntry.getVondatum()));
			block.setEndTime(dateformater.format(tableEntry.getBisdatum()));

			// Add matches
			for (SportMatch spielBO : tableEntry.getSpiele()) {
				FopMatch fopMatch = new FopMatch();

				// Calculate which teams should be printed bold
				if (spielBO != null) {
					DetailedMatchResult resultSpiel = spielAnalyzer
							.getErgebnisDetails(spielBO);

					fopMatch.setField(spielBO.getField().getName());
					fopMatch.setGroup(spielBO.getGroup().getName());
					fopMatch.setColor(spielBO.getGroup().getColor());

					Team1 team1 = new Team1();
					team1.setValue(mannschaftAnalyzer.getName(spielBO
							.getTeam1()));
					team1.setRel(mannschaftAnalyzer.getRelName(spielBO
							.getTeam1()));
					Team2 team2 = new Team2();
					team2.setValue(mannschaftAnalyzer.getName(spielBO
							.getTeam2()));
					team2.setRel(mannschaftAnalyzer.getRelName(spielBO
							.getTeam2()));

					switch (resultSpiel.winner) {
					case MANNSCHAFT1:
						fopMatch.setWinner("team1");
						break;
					case MANNSCHAFT2:
						fopMatch.setWinner("team2");
						break;
					case UNENTSCHIEDEN:
						fopMatch.setWinner("tie");
						break;
					}
					fopMatch.setTeam1(team1);
					fopMatch.setTeam2(team2);

					// Add referee
					if (spielBO.getReferee() != null) {
						Referee referee = new Referee();
						referee.setValue(mannschaftAnalyzer.getName(spielBO
								.getReferee()));
						referee.setRel(mannschaftAnalyzer.getRelName(spielBO
								.getReferee()));
						fopMatch.setReferee(referee);
					}

					// Add sets
					fopMatch.setSets(new Sets());
					for (SetResult satzBO : spielBO.getSetResults()) {
						FopSet fopSet = new FopSet();
						SetResults setResult = spielAnalyzer
								.getSetErgebnis(satzBO);
						fopSet.setWinner(convertToString(setResult));
						fopSet.setPoints1(satzBO.getPoints1());
						fopSet.setPoints2(satzBO.getPoints2());
						fopMatch.getSets().getFopSet().add(fopSet);
					}

				}

				block.getFopMatch().add(fopMatch);
			}

			fopTournament.getBlocks().getBlock().add(block);
		}

		// Add messages
		fopTournament.setFopMessages(new FopMessages());
		Map<String, String> messagesMap = Messages.getMessagesMap(locale);
		if (messagesMap.isEmpty())
			LOG.error("Couldn't find any localized messages for locale '"
					+ locale + "'");

		for (Map.Entry<String, String> message : messagesMap.entrySet()) {
			Message fopMessage = new Message();
			fopMessage.setKey(message.getKey());
			fopMessage.setContent(message.getValue());
			fopTournament.getFopMessages().getMessage().add(fopMessage);
		}

		return fopTournament;
	}

	/**
	 * Creates string representation of SetResult
	 * 
	 * @param setResult
	 * @return
	 */
	private String convertToString(SetResults setResult) {
		switch (setResult) {
		case MANNSCHAFT1:
			return "team1";
		case MANNSCHAFT2:
			return "team2";
		case UNENTSCHIEDEN:
			return "tie";
		}

		LOG.error("Encountered unknown SetResult type: " + setResult);

		return "unknown";
	}

	/**
	 * Creates the XML for the PDF (FOP)
	 * 
	 * @param turnierLocal
	 * @param selGruppenIds
	 * @param locale
	 * @return
	 * @throws RemoteException
	 * @throws Exception
	 */
	public String createXml(LicenceBO licenceBO, TournamentHolder tournamentHolder,
			Vector<Long> selGruppenIds, Locale locale) throws RemoteException,
			Exception {
		FopTournament fopTournament = null;
		try {
			fopTournament = createTournament(licenceBO, tournamentHolder, selGruppenIds,
					locale);
		} catch (Exception e) {
			LOG.error("Couldn't generate FOP object structure.", e);
		}

		StringWriter out = new StringWriter();
		String packageName = FopTournament.class.getPackage().getName();
		JAXBContext jc = JAXBContext.newInstance(packageName);
		Marshaller u = jc.createMarshaller();
		u.marshal(fopTournament, out);

		String xml = out.toString();

		return xml;
	}

}
