package com.sport.server.persistency.ejb.maps;

import java.util.HashMap;

import com.sport.core.bo.SportMatch;
import com.sport.server.ejb.interfaces.SpielLocal;

/**
 * Class to store relations between EJB representation and BO objects
 * 
 * @author Ronny
 *
 */
public class MatchMap extends HashMap<SpielLocal, SportMatch> {

	private static final long serialVersionUID = -5651793368846899272L;

}
