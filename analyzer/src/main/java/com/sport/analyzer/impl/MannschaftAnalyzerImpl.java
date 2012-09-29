package com.sport.analyzer.impl;

import java.text.DateFormat;

import org.apache.log4j.Logger;

import com.sport.analyzer.GroupResult;
import com.sport.analyzer.MannschaftAnalyzer;
import com.sport.analyzer.SpielAnalyzer.MatchResult;
import com.sport.core.bo.Team;
import com.sport.core.helper.Messages;

/**
 * Traces logical teams to physical teams
 * 
 * @author Ronny
 * 
 */
public class MannschaftAnalyzerImpl implements MannschaftAnalyzer {

	private static final Logger LOG = Logger
			.getLogger(MannschaftAnalyzerImpl.class);

	/**
	 * Singleton pattern
	 */
	private static MannschaftAnalyzer instance;

	private MannschaftAnalyzerImpl() {

	}

	public static MannschaftAnalyzer getInstance() {
		if (instance == null)
			instance = new MannschaftAnalyzerImpl();

		return instance;
	}

	@Override
	public Team getRelGruppeMannschaftBO(Team mannschaftBO) {
		// Check that team depends on a match and is temporary team (not
		// assigned to group)
		if (mannschaftBO.getLogspielBO() == null
				|| mannschaftBO.getGruppeBO() != null)
			return mannschaftBO;

		// Calculate results of the match on which the team depends
		SpielAnalyzerImpl.MatchResult matchResult = SpielAnalyzerImpl
				.getInstance().getErgebnisDetails(mannschaftBO.getLogspielBO()).winner;

		// Mannschaft1 , wenn Gewinner gesucht und Mann1 gewinnt bzw. Verlierer
		// gesucht und Mann2 gewinnt
		if ((matchResult == MatchResult.MANNSCHAFT1 && mannschaftBO
				.getLogsort() == Team.LOGSPIEL_GEWINNER)
				|| (matchResult == MatchResult.MANNSCHAFT2 && mannschaftBO
						.getLogsort() == Team.LOGSPIEL_VERLIERER)) {
			return getRelGruppeMannschaftBO(mannschaftBO.getLogspielBO()
					.getTeam1());
		}

		// Mannschaft2, wenn Gewinner gesucht und Mann2 gewinnt bzw. Verlierer
		// gesucht und Mann1 gewinnt
		if ((matchResult == MatchResult.MANNSCHAFT2 && mannschaftBO
				.getLogsort() == Team.LOGSPIEL_GEWINNER)
				|| (matchResult == MatchResult.MANNSCHAFT1 && mannschaftBO
						.getLogsort() == Team.LOGSPIEL_VERLIERER)) {
			return getRelGruppeMannschaftBO(mannschaftBO.getLogspielBO()
					.getTeam2());
		}

		// Otherwise the match wasn't played or led to a draw -> we can't
		// evaluate the result
		return mannschaftBO;
	}

	@Override
	public Team getRelMannschaftBO(Team mannschaftBO) {
		return getRelMannschaftBOComplex(mannschaftBO, false, false);
	}

	@Override
	public Team getRelMannschaftBOComplex(Team mannschaftBO, boolean force,
			boolean onestep) {
		if (mannschaftBO == null) {
			LOG.error("Wrongly asked to resolve a null team", new Throwable());

			return null;
		}

		// Is team already a pyhsical team?
		if (mannschaftBO.getLoggruppeBO() == null
				&& mannschaftBO.getLogspielBO() == null)
			return mannschaftBO;

		// Does the team depend on the result of a particular match, e.g. winner
		// of match XYZ
		if (mannschaftBO.getLogspielBO() != null) {
			SpielAnalyzerImpl.MatchResult matchResult = SpielAnalyzerImpl
					.getInstance().getErgebnisDetails(
							mannschaftBO.getLogspielBO()).winner;

			// Mannschaft1 , wenn Gewinner gesucht und Mann1 gewinnt bzw.
			// Verlierer gesucht und Mann2 gewinnt
			if ((matchResult == MatchResult.MANNSCHAFT1 && mannschaftBO
					.getLogsort() == Team.LOGSPIEL_GEWINNER)
					|| (matchResult == MatchResult.MANNSCHAFT2 && mannschaftBO
							.getLogsort() == Team.LOGSPIEL_VERLIERER)) {
				if (onestep) {
					return mannschaftBO.getLogspielBO().getTeam1();
				} else {
					return getRelMannschaftBOComplex(mannschaftBO
							.getLogspielBO().getTeam1(), force, onestep);
				}
			}

			// Mannschaft2, wenn Gewinner gesucht und Mann gewinnt bzw.
			// Verlierer gesucht und Mann1 gewinnt
			if ((matchResult == MatchResult.MANNSCHAFT2 && mannschaftBO
					.getLogsort() == Team.LOGSPIEL_GEWINNER)
					|| (matchResult == MatchResult.MANNSCHAFT1 && mannschaftBO
							.getLogsort() == Team.LOGSPIEL_VERLIERER)) {
				if (onestep) {
					return mannschaftBO.getLogspielBO().getTeam2();
				} else {
					return getRelMannschaftBOComplex(mannschaftBO
							.getLogspielBO().getTeam2(), force, onestep);
				}
			}

			// ansonsten (nicht gespielt oder unentschieden) -> this
			return mannschaftBO;
		}

		// LOG MANNSCHAFT
		GroupResult groupResult = GruppeAnalyzerImpl.getInstance()
				.getErgebnisDetails(mannschaftBO.getLoggruppeBO());
		Team mannschaftByPlatz = groupResult.getMannschaftByPlatz(mannschaftBO
				.getLogsort());

		// falls Mannschaft nicht berechenbar(z.B. noch nicht ausgespielte
		// Gruppe)
		if (force || groupResult.isVorlaeufig()) {
			return mannschaftBO;
		}

		// Schleifen ausschliessen
		if (mannschaftByPlatz == mannschaftBO) {
			return mannschaftBO;
		}

		// aus logische Mannschaft den Platz zurueckgeben
		if (onestep) {
			return mannschaftByPlatz;
		}
		return getRelMannschaftBOComplex(mannschaftByPlatz, force, onestep);
	}

	@Override
	public String getRelName(Team mannschaftBO) {
		Team relMannschaft = getRelMannschaftBO(mannschaftBO);
		if (relMannschaft != null) {
			String name = getName(relMannschaft);

			if (name == null)
				LOG.error("Can't find name for physical team '"
						+ mannschaftBO.toString() + "'.");

			return name;
		}
		String name = getName(mannschaftBO);

		if (name == null)
			LOG.error("Can't find name for log team '"
					+ mannschaftBO.toString() + "'.");

		return name;
	}

	@Override
	public String getRelNameTrace(Team mannschaftBO) {
		// bereits phy. Mannschaft
		if (mannschaftBO.getLoggruppeBO() == null
				&& mannschaftBO.getLogspielBO() == null) {
			return getName(mannschaftBO);
		}
		// GGF. LOGSPIEL?
		if (mannschaftBO.getLogspielBO() != null) {
			SpielAnalyzerImpl.MatchResult matchResult = SpielAnalyzerImpl
					.getInstance().getErgebnisDetails(
							mannschaftBO.getLogspielBO()).winner;

			// Mannschaft1 , wenn Gewinner gesucht und Mann1 gewinnt bzw.
			// Verlierer gesucht und Mann2 gewinnt
			if ((matchResult == MatchResult.MANNSCHAFT1 && mannschaftBO
					.getLogsort() == Team.LOGSPIEL_GEWINNER)
					|| (matchResult == MatchResult.MANNSCHAFT2 && mannschaftBO
							.getLogsort() == Team.LOGSPIEL_VERLIERER)) {
				return getName(mannschaftBO)
						+ " -> " //$NON-NLS-1$
						+ getRelNameTrace(mannschaftBO.getLogspielBO()
								.getTeam1());
			}

			// Mannschaft2, wenn Gewinner gesucht und Mann gewinnt bzw.
			// Verlierer gesucht und Mann1 gewinnt
			if ((matchResult == MatchResult.MANNSCHAFT2 && mannschaftBO
					.getLogsort() == Team.LOGSPIEL_GEWINNER)
					|| (matchResult == MatchResult.MANNSCHAFT1 && mannschaftBO
							.getLogsort() == Team.LOGSPIEL_VERLIERER)) {
				return getName(mannschaftBO)
						+ " -> " //$NON-NLS-1$
						+ getRelNameTrace(mannschaftBO.getLogspielBO()
								.getTeam2());
			}

			// ansonsten (nicht gespielt oder unentschieden) -> this
			return getName(mannschaftBO);
		}
		// LOG.GRUPPE
		GroupResult groupResult = GruppeAnalyzerImpl.getInstance()
				.getErgebnisDetails(mannschaftBO.getLoggruppeBO());
		Team mannschaftByPlatz = groupResult.getMannschaftByPlatz(mannschaftBO
				.getLogsort());

		// falls Mannschaft nicht berechenbar(z.B. noch nicht ausgespielte
		// Gruppe)
		if (groupResult.isVorlaeufig()) {
			return getName(mannschaftBO);
		}

		// aus logische Mannschaft den Platz zurueckgeben
		return getName(mannschaftBO)
				+ " -> " + getRelNameTrace(mannschaftByPlatz); //$NON-NLS-1$
	}

	@Override
	public String getName(Team mannschaftBO) {
		// falls Logische Verlinkung -> diesen Anzeigen
		if (mannschaftBO.getLoggruppeBO() != null) {
			return mannschaftBO.getLogsort()
					+ Messages.getString("mannschaftbo_place_of") + mannschaftBO.getLoggruppeBO().getName(); //$NON-NLS-1$
		}
		if (mannschaftBO.getLogspielBO() != null) {
			String gew = Messages.getString("mannschaftbo_short_winner"); //$NON-NLS-1$
			if (mannschaftBO.getLogsort() != Team.LOGSPIEL_GEWINNER) {
				gew = Messages.getString("mannschaftbo_short_looser"); //$NON-NLS-1$
			}
			return gew
					+ Messages.getString("mannschaftbo_of") //$NON-NLS-1$
					+ mannschaftBO.getLogspielBO().getField().getName()
					+ "/" //$NON-NLS-1$
					+ DateFormat.getTimeInstance(DateFormat.SHORT).format(
							mannschaftBO.getLogspielBO().getStartDate());
		}
		// Virtuelle Mannschaft -> Name berechnen
		if (mannschaftBO.isVirtual()) {
			return mannschaftBO.getGruppeBO().getName() + " - "
					+ mannschaftBO.getSort();
		}
		String name = mannschaftBO.getName();

		if (name == null)
			LOG.error("Physical team '" + mannschaftBO.getId()
					+ "' doesn't have a name.");

		return name;
	}

	@Override
	public String getBaseName(Team mannschaftBO) {
		if (mannschaftBO.getGruppeBO() != null) {
			return mannschaftBO.getSort()
					+ Messages.getString("mannschaftbo_place_of") + mannschaftBO.getGruppeBO().getName(); //$NON-NLS-1$
		}

		return MannschaftAnalyzerImpl.getInstance().getName(mannschaftBO);
	}

}
