package com.sport.server.persistency.ejb.maps;

import java.util.HashMap;

import com.sport.core.bo.SportGroup;
import com.sport.server.ejb.interfaces.GruppeLocal;

/**
 * Class to store relations between EJB representation and BO objects
 * 
 * @author Ronny
 *
 */
public class GroupMap extends HashMap<GruppeLocal, SportGroup> {

	private static final long serialVersionUID = -5651793368846899276L;

}
