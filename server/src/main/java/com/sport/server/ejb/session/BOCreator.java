package com.sport.server.ejb.session;

import com.sport.core.bo.Field;
import com.sport.core.bo.Ranking;
import com.sport.core.bo.SetResult;
import com.sport.core.bo.SportGroup;
import com.sport.core.bo.SportMatch;
import com.sport.core.bo.Team;
import com.sport.core.bo.Tournament;
import com.sport.core.bo.TournamentSystem;
import com.sport.core.bo.UserBO;
import com.sport.server.ejb.interfaces.GruppeLocal;
import com.sport.server.ejb.interfaces.MannschaftLocal;
import com.sport.server.ejb.interfaces.ModusLocal;
import com.sport.server.ejb.interfaces.PlatzierungLocal;
import com.sport.server.ejb.interfaces.SatzLocal;
import com.sport.server.ejb.interfaces.SpielLocal;
import com.sport.server.ejb.interfaces.SpielplatzLocal;
import com.sport.server.ejb.interfaces.TurnierLocal;
import com.sport.server.ejb.interfaces.UserLocal;

/**
 * Class that creates business objects (BO) from EJBs
 * 
 * @author Ronny
 * 
 */
public class BOCreator {

	/**
	 * @param userLocal
	 * @return
	 */
	public static UserBO createUserBO(UserLocal userLocal) {
		UserBO userBO = new UserBO();

		userBO.setId(userLocal.getId().longValue());
		userBO.setUsername(userLocal.getUsername());
		userBO.setPasswort(userLocal.getPasswort());
		userBO.setName(userLocal.getName());
		userBO.setVorname(userLocal.getVorname());
		userBO.setEmail(userLocal.getEmail());
		userBO.setLevel(userLocal.getLevel());

		return userBO;
	}

	public static Tournament createTurnierBO(TurnierLocal turnierLocal) {
		Tournament turnierBO = new Tournament();

		turnierBO.setId(turnierLocal.getId().longValue());
		turnierBO.setLinkid(turnierLocal.getLinkid());
		turnierBO.setName(turnierLocal.getName());
		turnierBO.setDate(turnierLocal.getDatum());
		turnierBO.setText(turnierLocal.getText());
		if (turnierLocal.getPunkteprosatz() != null) {
			turnierBO.setPointsPerSet(turnierLocal.getPunkteprosatz()
					.longValue());
		}
		if (turnierLocal.getPunkteprospiel() != null) {
			turnierBO.setPointsPerMatch(turnierLocal.getPunkteprospiel()
					.longValue());
		}
		if (turnierLocal.getPunkteprounentschiedenspiel() != null) {
			turnierBO.setPointsPerTie(turnierLocal
					.getPunkteprounentschiedenspiel().doubleValue());
		}
		if (turnierLocal.getSpieldauer() != null) {
			turnierBO.setDurationMatch(turnierLocal.getSpieldauer().longValue());
		}
		if (turnierLocal.getPausedauer() != null) {
			turnierBO.setDurationBreak(turnierLocal.getPausedauer().longValue());
		}
		if (turnierLocal.getBannerLink() != null) {
			turnierBO.setBannerLink(turnierLocal.getBannerLink());
		}
		if (turnierLocal.getBeamerumschaltzeit() != null) {
			turnierBO.setDurationProjectorSwitch(turnierLocal
					.getBeamerumschaltzeit().longValue());
		}
		if (turnierLocal.getSpielplangesperrt() != null) {
			turnierBO.setSpielplangesperrt(turnierLocal.getSpielplangesperrt()
					.booleanValue());
		}

		return turnierBO;
	}

	public static Field createSpielplatzBO(
			SpielplatzLocal spielplatzLocal) {
		Field spielplatzBO = new Field();

		spielplatzBO.setId(spielplatzLocal.getId().longValue());
		spielplatzBO.setName(spielplatzLocal.getName());

		return spielplatzBO;
	}

	public static Team createMannschaftBO(
			MannschaftLocal mannschaftLocal) {
		Team mannschaftBO = new Team();
		mannschaftBO.setId(mannschaftLocal.getId().longValue());
		if (mannschaftLocal.getSort() != null) {
			mannschaftBO.setSort(mannschaftLocal.getSort().intValue());
		}
		mannschaftBO.setName(mannschaftLocal.getName());
		if (mannschaftLocal.getLogsort() != null) {
			mannschaftBO.setLogsort(mannschaftLocal.getLogsort().intValue());
		}
		return mannschaftBO;
	}

	public static Ranking createPlatzierungBO(
			PlatzierungLocal platzierungLocal) {
		Ranking platzierungBO = new Ranking();
		
		platzierungBO.setId(platzierungLocal.getId().longValue());
		platzierungBO.setRank(platzierungLocal.getPlatznr().longValue());

		return platzierungBO;
	}

	public static TournamentSystem createModusBO(ModusLocal modusLocal) {
		TournamentSystem modusBO = new TournamentSystem();
		
		modusBO.setId(modusLocal.getId().longValue());
		modusBO.setName(modusLocal.getName());

		return modusBO;
	}

	public static SportGroup createGruppeBO(GruppeLocal gruppeLocal) {
		SportGroup gruppeBO = new SportGroup();
		
		gruppeBO.setId(gruppeLocal.getId().longValue());
		if (gruppeLocal.getSort() != null) {
			gruppeBO.setSort(gruppeLocal.getSort().intValue());
		}
		gruppeBO.setName(gruppeLocal.getName());
		gruppeBO.setColor(gruppeLocal.getFarbe());

		return gruppeBO;
	}

	public static SetResult createSatzBO(SatzLocal satzLocal) {
		SetResult satzBO = new SetResult();

		satzBO.setId(satzLocal.getId().longValue());
		satzBO.setSetNr(satzLocal.getSatznr().intValue());
		satzBO.setPoints1(satzLocal.getPunkte1().intValue());
		satzBO.setPoints2(satzLocal.getPunkte2().intValue());

		return satzBO;
	}

	public static SportMatch createSpielBO(SpielLocal spielLocal) {
		SportMatch spielBO = new SportMatch();
		
		spielBO.setId(spielLocal.getId().longValue());
		spielBO.setStartDate(spielLocal.getVondatum());
		spielBO.setEndDate(spielLocal.getBisdatum());

		return spielBO;
	}

}
