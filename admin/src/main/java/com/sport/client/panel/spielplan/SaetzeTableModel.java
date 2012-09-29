package com.sport.client.panel.spielplan;

import java.util.Collections;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;


import org.apache.log4j.Logger;

import com.sport.analyzer.impl.MannschaftAnalyzerImpl;
import com.sport.core.bo.SetResult;
import com.sport.core.bo.SportMatch;
import com.sport.core.bo.comparators.SatzComparator;
import com.sport.core.helper.Messages;

class SaetzeTableModel extends AbstractTableModel {

	private static final Logger LOG = Logger.getLogger(SaetzeTableModel.class);

	private static final long serialVersionUID = -6062352387558683470L;

	private SaetzeTable table;

	private Vector<SetResult> data = new Vector<SetResult>();
	private final static int COL_WINNERFARBE = 0;
	private final static int COL_SATZNR = 1;
	protected final static int COL_PUNKTE1 = 2;
	protected final static int COL_PUNKTE2 = 3;

	public SaetzeTableModel(SaetzeTable table) {
		super();
		this.table = table;
	}

	public int getColumnCount() {
		return 4;
	}

	public int getRowCount() {
		return data.size();
	}

	public Object getValueAt(int row, int col) {
		if (row >= data.size()) {
			return null;
		}
		SetResult satzBO = (SetResult) data.get(row);
		switch (col) {
		case COL_WINNERFARBE:
			return " "; //$NON-NLS-1$
		case COL_SATZNR:
			return new Long(satzBO.getSetNr());
		case COL_PUNKTE1:
			return new Long(satzBO.getPoints1());
		case COL_PUNKTE2:
			return new Long(satzBO.getPoints2());
		}
		return Messages.getString("saetzetable_unknow_col") + col; //$NON-NLS-1$
	}

	public String getColumnName(int column) {
		switch (column) {
		case COL_WINNERFARBE:
			return table.TABLE_COLWINNERFARBE;
		case COL_SATZNR:
			return table.TABLE_COLSATZNR;
		case COL_PUNKTE1:
			return table.TABLE_COLPUNKTE1;
		case COL_PUNKTE2:
			return table.TABLE_COLPUNKTE2;
		}
		return Messages.getString("saetzetable_unknow_col") + column; //$NON-NLS-1$
	}

	@SuppressWarnings("unchecked")
	public Class getColumnClass(int c) {
		Object o = getValueAt(0, c);
		if (o != null) {
			return o.getClass();
		}
		return null;
	}

	public boolean isCellEditable(int row, int col) {
		return (col > 1);
	}

	public void setValueAt(Object aValue, int row, int column) {

		if (row > -1 && row < data.size()) {
			table.parent.dirty = true;

			SetResult satzBO = (SetResult) data.get(row);
			if (satzBO != null) {
				int intVal = 0;
				// Parse from Long
				if (aValue instanceof Long) {
					intVal = ((Long) aValue).intValue();
				}
				// Parse from String
				else {
					try {
						intVal = Integer.parseInt((String) aValue);
					} catch (NumberFormatException e) {
					}
				}
				switch (column) {
				case COL_PUNKTE1:
					satzBO.setPoints1(intVal);
					break;
				case COL_PUNKTE2:
					satzBO.setPoints2(intVal);
					break;
				default:
					LOG.error(Messages
							.getString("saetzetable_unknow_col") + column); //$NON-NLS-1$
				}
			}
		}
	}

	public void setSpiel(SportMatch spielBO) {
		table.getColumn(table.TABLE_COLPUNKTE1).setHeaderValue(
				MannschaftAnalyzerImpl.getInstance().getRelName(
						spielBO.getTeam1()));
		table.getColumn(table.TABLE_COLPUNKTE2).setHeaderValue(
				MannschaftAnalyzerImpl.getInstance().getRelName(
						spielBO.getTeam2()));

		Vector<SetResult> saetze = spielBO.getSetResults();

		setData(saetze);

		/*
		 * // falls noch kein Satz gespielt -> auto. einen anlegen if
		 * (saetze.size() == 0) { table.addSatz (); }
		 */

		table.getTableHeader().repaint();
	}

	private void setData(Vector<SetResult> objects) {
		data = objects;
		Collections.sort(data, new SatzComparator());
	}

	public Vector<SetResult> getData() {
		return data;
	}

};
