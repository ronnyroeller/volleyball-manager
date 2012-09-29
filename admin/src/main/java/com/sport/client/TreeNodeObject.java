/*
 * Created on 01.06.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.sport.client;

import com.sport.core.bo.Tournament;
import com.sport.core.helper.Messages;


/**
 * @author ronny
 */
public class TreeNodeObject {
	
	private Tournament turnier;
	private int type; // Typ des Knotens
	
	public static final int NOPANEL = -1;
	public static final int TURNIER = 0;
	public static final int MANNSCHAFTEN = 1;
	public static final int GRUPPEN = 2;
	public static final int SPIELPLAN = 3;
	public static final int ERGEBNISSE = 4;
	public static final int PLATZIERUNGEN = 5;
	
	TreeNodeObject (Tournament turnier, int type) {
		setTurnier(turnier);
		setType(type);
	}

	public String toString () {
		switch (getType()) {
			case TURNIER: return (getTurnier() != null) ? getTurnier().getName() : "Unknown";
			case MANNSCHAFTEN: return Messages.getString("treenodeobject_teams"); //$NON-NLS-1$
			case GRUPPEN: return Messages.getString("treenodeobject_groups"); //$NON-NLS-1$
			case SPIELPLAN: return Messages.getString("treenodeobject_schedule"); //$NON-NLS-1$
			case ERGEBNISSE: return Messages.getString("treenodeobject_results"); //$NON-NLS-1$
			case PLATZIERUNGEN: return Messages.getString("treenodeobject_placings"); //$NON-NLS-1$
			default: return Messages.getString("treenodeobject_unknow_type") + " " + getType(); //$NON-NLS-1$
		}
	}

	/**
	 * @return
	 */
	public Tournament getTurnier() {
		return turnier;
	}

	/**
	 * @return
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param turnierBO
	 */
	public void setTurnier(Tournament turnierBO) {
		turnier = turnierBO;
	}

	/**
	 * @param i
	 */
	public void setType(int i) {
		type = i;
	}

}
