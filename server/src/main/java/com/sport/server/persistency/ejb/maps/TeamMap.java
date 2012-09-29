package com.sport.server.persistency.ejb.maps;

import java.util.HashMap;

import com.sport.core.bo.Team;
import com.sport.server.ejb.interfaces.MannschaftLocal;

/**
 * Class to store relations between EJB representation and BO objects
 * 
 * @author Ronny
 *
 */
public class TeamMap extends HashMap<MannschaftLocal, Team> {

	private static final long serialVersionUID = -5651793368846899271L;

}
