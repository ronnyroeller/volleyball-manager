package com.sport.server.persistency.ejb.maps;

import java.util.HashMap;

import com.sport.core.bo.Ranking;
import com.sport.server.ejb.interfaces.PlatzierungLocal;

/**
 * Class to store relations between EJB representation and BO objects
 * 
 * @author Ronny
 *
 */
public class RankingMap extends HashMap<PlatzierungLocal, Ranking> {

	private static final long serialVersionUID = -5651793368846899273L;

}
