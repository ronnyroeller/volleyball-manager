package com.sport.server.persistency.xml;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import com.sport.server.ejb.HomeGetter;
import com.sport.server.ejb.interfaces.GruppeLocal;
import com.sport.server.ejb.interfaces.MannschaftLocal;
import com.sport.server.ejb.interfaces.ModusLocal;
import com.sport.server.ejb.interfaces.PlatzierungLocal;
import com.sport.server.ejb.interfaces.SatzLocal;
import com.sport.server.ejb.interfaces.SpielLocal;
import com.sport.server.ejb.interfaces.SpielplatzLocal;
import com.sport.server.ejb.interfaces.TurnierLocal;
import com.sport.server.persistency.tur.Gruppe;
import com.sport.server.persistency.tur.Mannschaft;
import com.sport.server.persistency.tur.Platzierung;
import com.sport.server.persistency.tur.Satz;
import com.sport.server.persistency.tur.Spiel;
import com.sport.server.persistency.tur.Spielplatz;
import com.sport.server.persistency.tur.Turnier;

/**
 * Reads *.tur files
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

	private TurnierLocal read(Turnier turnier) throws Exception {
		TurnierLocal turnierLocal = HomeGetter.getTurnierHome().create();

		turnierLocal.setName(turnier.getName());
		turnierLocal.setDatum(extractDate(turnier.getDatum()));
		turnierLocal.setPunkteprosatz(new Long(turnier.getPunkteprosatz()));
		turnierLocal.setPunkteprospiel(new Long(turnier.getPunkteprospiel()));
		turnierLocal.setPunkteprounentschiedenspiel(turnier
				.getPunkteprounentschiedenspiel());
		turnierLocal.setSpieldauer(new Long(turnier.getSpieldauer()));
		turnierLocal.setPausedauer(new Long(turnier.getPausedauer()));
		turnierLocal.setBeamerumschaltzeit(new Long(turnier
				.getBeamerumschaltzeit()));
		turnierLocal.setBannerLink(turnier.getBannerLink());
		turnierLocal.setText(turnier.getText());

		// if link-id exists in another tournament -> change the new one
		String linkid = turnier.getLinkid();

		boolean noConflict = false;
		while (!noConflict) {
			if (!HomeGetter.getTurnierSessionHome().create()
					.existsTurnierByLinkid(linkid).booleanValue()) {
				noConflict = true;
			} else {
				linkid += "#";
			}
		}
		turnierLocal.setLinkid(linkid);

		// Cache IDs with objects -> to link them afterwards (e.g. assign match
		// to a group)
		Map<Integer, SpielplatzLocal> spielplatzMap = new HashMap<Integer, SpielplatzLocal>();
		Map<Integer, GruppeLocal> gruppeMap = new HashMap<Integer, GruppeLocal>();
		Map<Integer, MannschaftLocal> mannschaftMap = new HashMap<Integer, MannschaftLocal>();
		Map<Integer, SpielLocal> spielMap = new HashMap<Integer, SpielLocal>();

		// Read fields
		for (Spielplatz spielplatz : turnier.getSpielplatz()) {
			SpielplatzLocal spielplatzLocal = HomeGetter.getSpielplatzHome()
					.create();
			spielplatzLocal.setTurnier(turnierLocal);
			spielplatzLocal.setName(spielplatz.getName());

			spielplatzMap.put(spielplatz.getId(), spielplatzLocal);
		}

		// Read groups
		for (Gruppe gruppe : turnier.getGruppe()) {
			GruppeLocal gruppeLocal = HomeGetter.getGruppeHome().create();
			gruppeLocal.setTurnier(turnierLocal);
			gruppeLocal.setName(gruppe.getName());
			gruppeLocal.setFarbe(gruppe.getFarbe());
			gruppeLocal.setSort(gruppe.getSort());

			// Modus verbinden
			ModusLocal modusLocal = HomeGetter.getModusHome().findById(
					new Long(gruppe.getModusId()));
			gruppeLocal.setModus(modusLocal);

			gruppeMap.put(gruppe.getId(), gruppeLocal);
			if (LOG.isDebugEnabled())
				LOG.debug("Read group " + gruppe.getId() + " (internal ID: "
						+ gruppeLocal.getId() + ")");
		}

		// Stores references to logicalTeam for which the matches have to be
		// filled out later on (can't be done now because matches aren't
		// created so far)
		Map<MannschaftLocal, Integer> missingMatchReferences = new HashMap<MannschaftLocal, Integer>();

		// Read teams
		for (Mannschaft mannschaft : turnier.getMannschaft()) {
			MannschaftLocal mannschaftLocal = HomeGetter.getMannschaftHome()
					.create();
			mannschaftLocal.setName(mannschaft.getName());
			mannschaftLocal.setTurnier(turnierLocal);

			mannschaftLocal.setSort(mannschaft.getSort());

			if (mannschaft.getGruppeId() > 0) {
				GruppeLocal gruppe = gruppeMap.get(mannschaft.getGruppeId());
				if (gruppe == null)
					LOG.error("Can't find group " + mannschaft.getGruppeId()
							+ ", which should be linked to team "
							+ mannschaft.getId() + ".");
				mannschaftLocal.setGruppe(gruppe);
			}

			String loggruppeId = mannschaft.getLoggruppeId();
			if (loggruppeId != null && !loggruppeId.isEmpty()) {
				// Convert to integer -> otherwise hashcode differs, and we
				// won't find it in the map
				GruppeLocal logGruppe = gruppeMap.get(Integer
						.valueOf(loggruppeId));
				if (logGruppe == null)
					LOG.error("Can't find log group "
							+ loggruppeId
							+ ", which should be linked to team "
							+ mannschaft.getId()
							+ ". Known groups: "
							+ new ArrayList<Integer>(gruppeMap.keySet())
									.toString());
				mannschaftLocal.setLoggruppe(logGruppe);
			}

			if (mannschaft.getLogsort() > 0)
				mannschaftLocal.setLogsort(mannschaft.getLogsort());

			String logspielId = mannschaft.getLogspielId();
			if (logspielId != null && !logspielId.isEmpty())
				missingMatchReferences.put(mannschaftLocal, Integer
						.valueOf(logspielId));

			mannschaftMap.put(mannschaft.getId(), mannschaftLocal);
		}

		// Read matches
		for (Spiel spiel : turnier.getSpiel()) {
			SpielLocal spieleLocal = HomeGetter.getSpielHome().create();
			spieleLocal.setVondatum(extractDate(spiel.getVondatum()));
			spieleLocal.setBisdatum(extractDate(spiel.getBisdatum()));

			// Link field
			spieleLocal.setSpielplatz(spielplatzMap
					.get(spiel.getSpielplatzId()));

			// Link group
			spieleLocal.setGruppe(gruppeMap.get(spiel.getGruppeId()));

			// Link teams
			spieleLocal.setMannschaft1(mannschaftMap.get(spiel
					.getMannschaft1Id()));
			spieleLocal.setMannschaft2(mannschaftMap.get(spiel
					.getMannschaft2Id()));
			if (spiel.getSchiedsrichterId() > 0)
				spieleLocal.setSchiedsrichter(mannschaftMap.get(spiel
						.getSchiedsrichterId()));

			// Read results of sets
			for (Satz satz : spiel.getSatz()) {
				SatzLocal satzLocal = HomeGetter.getSatzHome().create();
				satzLocal.setPunkte1(satz.getPunkte1());
				satzLocal.setPunkte2(satz.getPunkte2());
				satzLocal.setSatznr(satz.getSatznr());

				// Spiel verbinden
				satzLocal.setSpiel(spieleLocal);
			}

			spielMap.put(spiel.getId(), spieleLocal);
		}

		// Zwischengespeicherte Verbindungen zwischen Mannschaft und Logspiel
		// schreiben nicht eher moeglich, da erst Spiele eingelesen werden
		// muessen!
		for (MannschaftLocal mannschaftLocal : missingMatchReferences.keySet()) {
			Integer matchId = missingMatchReferences.get(mannschaftLocal);
			SpielLocal match = spielMap.get(matchId);

			if (match == null)
				LOG.error("Can't find match " + matchId
						+ ", which is linked to team "
						+ mannschaftLocal.getId());

			mannschaftLocal.setLogspiel(match);
		}

		// Read ranking
		for (Platzierung platzierung : turnier.getPlatzierung()) {
			PlatzierungLocal platzierungLocal = HomeGetter.getPlatzierungHome()
					.create();
			platzierungLocal.setTurnier(turnierLocal);
			platzierungLocal.setPlatznr(new Long(platzierung.getPlatznr()));

			// Mannschaften verbinden
			if (platzierung.getMannschaftId() > 0) {
				MannschaftLocal team = mannschaftMap.get(platzierung
						.getMannschaftId());

				if (team == null)
					LOG.error("Couldn't find team '"
							+ platzierung.getMannschaftId() + "' for rank "
							+ platzierung.getPlatznr());

				platzierungLocal.setMannschaft(team);
			}
		}

		return turnierLocal;
	}

	public TurnierLocal read(String xml) throws Exception {
		Turnier turnier = null;
		// Reads *.tur file
		try {
			// First test for legacy encoding
			turnier = readStream(xml,"ISO-8859-1");
		} catch (JAXBException e) {
			try {
				// Let's try our new encoding
				turnier = readStream(xml,"UTF-8");
			} catch (JAXBException e2) {
				LOG.error("Can't read XML file with JAXB in encoding UTF-8 or ISO-8859-1", e2);
			}
		}

		if (turnier != null)
			return read(turnier);

		return null;
	}

	/**
	 * Reads a *.tur file into object structure (uses JAXB)
	 * 
	 * @param turFile
	 * @return
	 * @throws UnsupportedEncodingException, JAXBException 
	 */
	protected Turnier readStream(String xml, String encoding) throws UnsupportedEncodingException, JAXBException {
		InputStream turStream = new ByteArrayInputStream(xml
				.getBytes(encoding));

		String packageName = Turnier.class.getPackage().getName();
		JAXBContext jc = JAXBContext.newInstance(packageName);
		Unmarshaller u = jc.createUnmarshaller();
		return (Turnier) u.unmarshal(turStream);
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

}
