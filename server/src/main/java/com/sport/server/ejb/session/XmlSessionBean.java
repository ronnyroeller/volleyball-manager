package com.sport.server.ejb.session;

import java.util.Locale;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.apache.log4j.Logger;

import com.sport.core.bo.Tournament;
import com.sport.core.bo.TournamentHolder;
import com.sport.server.ejb.HomeGetter;
import com.sport.server.ejb.interfaces.TurnierLocal;
import com.sport.server.pdf.PdfHelper;
import com.sport.server.persistency.ejb.managers.PersistencyManager;
import com.sport.server.persistency.xml.TurReader;
import com.sport.server.persistency.xml.TurWriter;

/**
 * This session bean provides the saving/reading *.tur files and PDF generation.
 * 
 * @ejb:bean name="XmlSession" type="Stateless" jndi-name="XmlSessionHome"
 *           local-jndi-name="XmlSessionLocal" view-type="both"
 */
public class XmlSessionBean implements SessionBean {

	private static final long serialVersionUID = 8372816783685984682L;

	private static final Logger LOG = Logger.getLogger(XmlSessionBean.class);

	private SessionContext mContext;

	/**
	 * gibt XML-String eines Turnier wieder
	 * 
	 * @ejb:interface-method view-type="both"
	 */
	public String saveXMLByTurnierid(long tournamentId) {
		try {
			try {
				TournamentHolder tournamentHolder = new PersistencyManager().findByTournamentId(tournamentId);

				String xml = new TurWriter().write(tournamentHolder);

				return xml;
			} catch (Exception e) {
				LOG.error("Couldn't export tournament '" + tournamentId
						+ "' to XML.", e);
			}
		} catch (Exception e) {
			LOG.error("Couldn't save to XML", e);
		}
		return null;
	}

	/**
	 * Baut Turnier aus XML-String auf und gibt entsprechendes BO zurueck
	 * 
	 * @ejb:interface-method view-type="both"
	 */
	public Tournament loadTurnierByXMLUserid(String xml, long userid) {
		TurnierLocal turnierLocal = null;
		try {
			turnierLocal = new TurReader().read(xml);
			turnierLocal.setUser(HomeGetter.getUserHome().findById(
					new Long(userid)));
		} catch (Exception e) {
			LOG.error("Couldn't load tournament from XML file.", e);
		}

		if (turnierLocal == null) {
			return null;
		}

		return BOCreator.createTurnierBO(turnierLocal);
	}

	/**
	 * Exportiert PDF-String f�r PDF Spielplan selGruppenIds ... Menge aller
	 * Gruppen-IDs, die fuer den Spielplan beachtet werden sollen.
	 * 
	 * @ejb:interface-method view-type="both"
	 */
	public byte[] getSpielplanPDFByTurnierid(long turnierid,
			Vector selGruppenIds, Locale locale) {

		return PdfHelper.generatePdf(turnierid, selGruppenIds, locale, "schedule.xsl");
	}

	/**
	 * Exportiert PDF-String f�r PDF Schiedsrichterplan
	 * 
	 * @param isSpielbericht
	 *            ... xsl fuer Spielbericht verwenden? -> sonst schiedsrichter
	 * 
	 * @ejb:interface-method view-type="both"
	 */
	public byte[] getSchiedsrichterPDFByTurnierid(long turnierid,
			Vector selGruppenIds, Locale locale, boolean isSpielbericht) {
		String filename = (isSpielbericht) ? "match-reports.xsl"
				: "referee.xsl";

		return PdfHelper.generatePdf(turnierid, selGruppenIds, locale, filename);
	}

	/**
	 * Exportiert PDF-String f�r PDF Gruppenansicht
	 * 
	 * @ejb:interface-method view-type="both"
	 */
	public byte[] getGruppenPDFByTurnierid(long turnierid,
			Vector selGruppenIds, Locale locale) {
		return PdfHelper.generatePdf(turnierid, selGruppenIds, locale, "groups.xsl");
	}

	/**
	 * Create the Session Bean
	 * 
	 * @throws CreateException
	 * 
	 * @ejb:create-method view-type="both"
	 */
	public void ejbCreate() throws CreateException {
	}

	/**
	 * Describes the instance and its content for debugging purpose
	 * 
	 * @return Debugging information about the instance and its content
	 */
	public String toString() {
		return "XmlSessionBean [ " + " ]";
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
