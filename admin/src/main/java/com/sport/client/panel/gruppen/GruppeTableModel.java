package com.sport.client.panel.gruppen;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;


import org.apache.log4j.Logger;

import com.sport.core.bo.SportGroup;
import com.sport.core.bo.TournamentSystem;
import com.sport.core.helper.Messages;

class GruppeTableModel extends AbstractTableModel implements Comparator<SportGroup> {

	private static final Logger LOG = Logger.getLogger(GruppeTableModel.class);

	private static final long serialVersionUID = -7806100262393913846L;

	private GruppenTable table;

	private Vector<SportGroup> data = new Vector<SportGroup>();
	final static int COL_GRUPPENFARBE = 0;
	final static int COL_SORT = 1;
	final static int COL_NAME = 2;
	final static int COL_FARBE = 3;
	final static int COL_MODUS = 4;

	int sortColumn = COL_SORT;

	public GruppeTableModel(GruppenTable table, Vector<SportGroup> data) {
		super();
		this.data = data;
		this.table = table;
	}

	public int getColumnCount() {
		return 5;
	}

	public int getRowCount() {
		return data.size();
	}

	public Object getValueAt(int row, int col) {
		SportGroup gruppeBO = (SportGroup) data.get(row);
		switch (col) {
		case COL_GRUPPENFARBE:
			return ""; //$NON-NLS-1$
		case COL_SORT:
			return new Integer(gruppeBO.getSort());
		case COL_NAME:
			return gruppeBO.getName();
		case COL_FARBE:
			return gruppeBO.getColor();
		case COL_MODUS:
			if (gruppeBO.getTournamentSystem() == null) {
				return ""; //$NON-NLS-1$
			}
			return gruppeBO.getTournamentSystem();
		}
		return Messages.getString("gruppentable_unknow_col") + col; //$NON-NLS-1$
	}

	public String getColumnName(int column) {
		switch (column) {
		case COL_GRUPPENFARBE:
			return table.TABLE_COLGRUPPENFARBE;
		case COL_SORT:
			return table.TABLE_COLSORT;
		case COL_NAME:
			return table.TABLE_COLNAME;
		case COL_FARBE:
			return table.TABLE_COLFARBE;
		case COL_MODUS:
			return table.TABLE_COLMODUS;
		}
		return Messages.getString("gruppentable_unknow_col") + column; //$NON-NLS-1$
	}

	@SuppressWarnings("unchecked")
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	public boolean isCellEditable(int row, int col) {
		return true;
	}

	public void setValueAt(Object aValue, int row, int column) {
		table.parent.dirty = true;

		SportGroup gruppeBO = (SportGroup) data.get(row);
		switch (column) {
		case COL_SORT:
			// jeder Sortwert darf nur einmal existieren! -> tauschen mit
			// anderem
			int oldSort = gruppeBO.getSort();
			int newSort = ((Integer) aValue).intValue();
			for (SportGroup gruppeBO2 : data) {
				if (gruppeBO2.getSort() == newSort) {
					gruppeBO2.setSort(oldSort);
				}
			}
			gruppeBO.setSort(newSort);
			break;
		case COL_NAME:
			gruppeBO.setName((String) aValue);
			break;
		case COL_FARBE:
			gruppeBO.setColor((String) aValue);
			// Farben aktuallisieren
			table.repaint();
			if (table.parent instanceof GruppenPanel) {
				((GruppenPanel) table.parent).mannschaftenTable.repaint();
			}
			break;
		case COL_MODUS:
			gruppeBO.setTournamentSystem((TournamentSystem) aValue);
			break;
		default:
			LOG.error(Messages.getString("gruppentable_unknow_col") + column); //$NON-NLS-1$
		}
	}

	public void sortColumn(int aSortColumn) {
		this.sortColumn = aSortColumn;
		Collections.sort(data, this);
	}

	public void sortColumn() {
		Collections.sort(data, this);
	}

	public int compare(SportGroup gruppe1, SportGroup gruppe2) {
		if (gruppe1 == null)
			return -1;
		if (gruppe2 == null)
			return 1;

		Comparable col1 = null, col2 = null;

		switch (sortColumn) {
		case COL_GRUPPENFARBE:
			col1 = new Integer(1);
			col2 = new Integer(1);
			break;
		case COL_SORT:
			col1 = new Integer(gruppe1.getSort());
			col2 = new Integer(gruppe2.getSort());
			break;
		case COL_NAME:
			col1 = gruppe1.getName();
			col2 = gruppe2.getName();
			break;
		case COL_FARBE:
			col1 = gruppe1.getColor();
			col2 = gruppe2.getColor();
			break;
		case COL_MODUS:
			col1 = gruppe1.getTournamentSystem().getName();
			col2 = gruppe2.getTournamentSystem().getName();
			break;
		default:
			LOG.error(Messages.getString("gruppentable_unknow_col") + sortColumn); //$NON-NLS-1$
		}
		return col1.compareTo(col2);
	}

	public void setData(Vector<SportGroup> objects) {
		data = objects;
		sortColumn();

		// Sort-Combobox bauen
		table.comboSort.removeAllItems();
		for (int i = 1; i <= getData().size(); i++) {
			table.comboSort.addItem(new Integer(i));
		}
	}

	public Vector<SportGroup> getData() {
		return data;
	}

};
