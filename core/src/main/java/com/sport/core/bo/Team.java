package com.sport.core.bo;

import java.io.Serializable;

import org.apache.log4j.Logger;

/**
 * A real or virtual team
 * 
 * @author ronny
 */
public class Team implements Serializable {

	private static final Logger LOG = Logger.getLogger(Team.class);

	private static final long serialVersionUID = -4179835587854703237L;

	private long id;
	private int sort = 0;

	/**
	 * Name of the team
	 */
	private String name = ""; //$NON-NLS-1$
	private int logsort = 0;
	private SportGroup loggruppeBO = null;
	private SportMatch logspielBO = null;
	private SportGroup gruppeBO = null;
	private Tournament tournament = null;
	
	public static final int LOGSPIEL_GEWINNER = 1;
	public static final int LOGSPIEL_VERLIERER = 2;

	public Team() {
		// Achtung: dieser Wert wird NICHT fuer die EJB-Ids verwendet!
		// eindeutige ID, damit Combo-Box funktioniert!
		setId(Math.round(Math.random() * 2100000000));
	}

	public Object clone () {
		Team obj = new Team ();
		obj.setGruppeBO(getGruppeBO());
		obj.setLoggruppeBO(getLoggruppeBO());
		obj.setLogsort(getLogsort());
		obj.setLogspielBO(getLogspielBO());
		obj.setName(getNamePhy());
		obj.setSort(getSort());
		obj.setTurnierBO(getTournament());
		return obj;
	}
	
	/**
	 * THIS IS CHANGED FROM FORMER VM VERSIONS -> MAY LEAD TO ISSUE IN ADMINISTRATOR/WEB (cell editors)
	 */
	public String toString() {
		LOG.error("This method shouldn't be called.", new Throwable());
		//return MannschaftAnalyzerImpl.getInstance().getName(this);
		return name;
	}

	public boolean equals(Object obj) {
		if (obj instanceof Team) {
			return ((Team) obj).getId() == getId();
		}
		return false;
	}

	/**
	 * zeigen zwei Mannschaften auf die gleiche logische Mannschaft
	 */
	public boolean logequals(Team mann2) {
		// beide logische Spiele?
		if ((mann2.getLogspielBO() == null) || (getLogspielBO() == null)) {
			return false;
		}
		// gleiches Spiel?
		if (mann2.getLogspielBO().getId() != getLogspielBO().getId()) {
			return false;
		}

		// Gewinner/Verlierer gleich?
		return (getLogsort() == mann2.getLogsort());
	}

	public long getId() {
		return id;
	}
	
	/**
	 * Returns the name which is saved in the database
	 * @return
	 */
	public String getNamePhy () {
		return name;
	}

	/**
	 * ist Mannschaft eine logische Mannschaft
	 * @return
	 */
	public boolean isLog () {
		return (getLoggruppeBO() != null) || (getLogspielBO() != null);
	}

	/**
	 * Virtual teams are place holders before the drawing
	 * @return
	 */
	public boolean isVirtual () {
		return "".equals(name);
	}
	
	public int getSort() {
		return sort;
	}

	public void setId(long l) {
		id = l;
	}

	public void setName(String string) {
		name = string;
	}

	public String getName() {
		return name;
	}

	public void setSort(int i) {
		sort = i;
	}

	public SportGroup getGruppeBO() {
		return gruppeBO;
	}

	public void setGruppeBO(SportGroup gruppeBO) {
		this.gruppeBO = gruppeBO;
	}

	public SportGroup getLoggruppeBO() {
		return loggruppeBO;
	}

	public int getLogsort() {
		return logsort;
	}

	public void setLoggruppeBO(SportGroup gruppeBO) {
		loggruppeBO = gruppeBO;
	}

	public void setLogsort(int i) {
		logsort = i;
	}

	public SportMatch getLogspielBO() {
		return logspielBO;
	}

	public void setLogspielBO(SportMatch spielBO) {
		logspielBO = spielBO;
	}

	public Tournament getTournament() {
		if (gruppeBO != null) {
			return gruppeBO.getTournament();
		}
		return tournament;
	}

	public void setTurnierBO(Tournament tournament) {
		this.tournament = tournament;
	}

}
