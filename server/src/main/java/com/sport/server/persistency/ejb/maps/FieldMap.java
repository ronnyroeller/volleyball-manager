package com.sport.server.persistency.ejb.maps;

import java.util.HashMap;

import com.sport.core.bo.Field;
import com.sport.server.ejb.interfaces.SpielplatzLocal;

/**
 * Class to store relations between EJB representation and BO objects
 * 
 * @author Ronny
 *
 */
public class FieldMap extends HashMap<SpielplatzLocal, Field> {

	private static final long serialVersionUID = -5651793368846899277L;

}
