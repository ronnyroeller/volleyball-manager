package com.sport.client.panel.mannschaften;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;

import com.sport.analyzer.MannschaftAnalyzer;
import com.sport.analyzer.impl.MannschaftAnalyzerImpl;
import com.sport.client.panel.gruppen.GruppenPanel;
import com.sport.core.bo.SportGroup;
import com.sport.core.bo.Team;
import com.sport.core.helper.Messages;

public class MannschaftenTableModel extends AbstractTableModel implements
		Comparator<Team> {

	private static final Logger LOG = Logger
			.getLogger(MannschaftenTableModel.class);

	private static final long serialVersionUID = 7219048886988205170L;

	private Vector<Team> data = new Vector<Team>();
	protected final static int COL_GRUPPENFARBE = 0;
	protected final static int COL_SORT = 1;
	protected final static int COL_NAME = 2;
	protected final static int COL_GRUPPE = 3;

	private MannschaftenTable table;

	private final MannschaftAnalyzer mannschaftAnalyzer = MannschaftAnalyzerImpl
			.getInstance();

	int sortColumn = 1;

	public MannschaftenTableModel(MannschaftenTable table, Vector<Team> data) {
		super();
		this.data = data;
		this.table = table;
	}

	public int getColumnCount() {
		return 4;
	}

	public int getRowCount() {
		return data.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		Team mannschaftBO = data.get(row);
		switch (col) {
		case COL_GRUPPENFARBE:
			return ""; //$NON-NLS-1$
		case COL_SORT:
			return mannschaftBO.getSort();
		case COL_NAME:
			return mannschaftAnalyzer.getName(mannschaftBO);
		case COL_GRUPPE:
			if (mannschaftBO.getGruppeBO() == null) {
				return Messages.getString("mannschaftentable_nogroup");
			}
			return mannschaftBO.getGruppeBO();
		}
		return Messages.getString("mannschaftentable_unknow_col") + col; //$NON-NLS-1$
	}

	public String getColumnName(int column) {
		switch (column) {
		case COL_GRUPPENFARBE:
			return table.TABLE_COLGRUPPENFARBE;
		case COL_SORT:
			return table.TABLE_COLSORT;
		case COL_NAME:
			return table.TABLE_COLNAME;
		case COL_GRUPPE:
			return table.TABLE_COLGRUPPE;
		}
		return Messages.getString("mannschaftentable_unknow_col") + column; //$NON-NLS-1$
	}

	@SuppressWarnings("unchecked")
	public Class getColumnClass(int c) {
		Object valueAt = getValueAt(0, c);

		if (valueAt == null)
			LOG.error("Table contains null values for column " + c + ".");

		return valueAt.getClass();
	}

	public boolean isCellEditable(int row, int col) {
		return (col > 0);
	}

	public void setValueAt(Object aValue, int row, int column) {
		if ((row > -1) && (row < getRowCount())) {
			Team mannschaftBO = data.get(row);

			table.parent.dirty = true;

			switch (column) {
			case COL_SORT:
				// jeder Sortwert darf nur einmal existieren! -> tauschen
				// mit anderem
				int oldSort = mannschaftBO.getSort();
				int newSort = ((Integer) aValue).intValue();
				for (Team mannschaftBO2 : data) {
					if (mannschaftBO2.getSort() == newSort) {
						mannschaftBO2.setSort(oldSort);
					}
				}
				mannschaftBO.setSort(newSort);
				break;
			case COL_NAME:
				mannschaftBO.setName((String) aValue);
				break;
			case COL_GRUPPE:
				if (table.parent instanceof MannschaftenPanel) {
					if (aValue instanceof String) {
						aValue = null;
					}
					((MannschaftenPanel) table.parent).listenGruppeChanged(
							mannschaftBO, aValue);
				}
				if (table.parent instanceof GruppenPanel) {
					((GruppenPanel) table.parent).listenGruppeChanged(
							mannschaftBO, (SportGroup) aValue);
					table.updateUI();
				}
				// mannschaftBO.setGruppeBO((GruppeBO) aValue);
				break;
			default:
				LOG
						.error(Messages
								.getString("mannschaftentable_unknow_col") + column); //$NON-NLS-1$
			}
		}
	}

	public void sortColumn(int aSortColumn) {
		this.sortColumn = aSortColumn;
		Collections.sort(data, this);
	}

	public void sortColumn() {
		Collections.sort(data, this);
	}

	public int compare(Team mann1, Team mann2) {
		if (mann1 == null)
			return -1;
		if (mann2 == null)
			return 1;

		Comparable col1 = null, col2 = null;

		switch (sortColumn) {
		case COL_GRUPPENFARBE:
			return 0;
		case COL_SORT:
			col1 = new Integer(mann1.getSort());
			col2 = new Integer(mann2.getSort());
			break;
		case COL_NAME:
			col1 = mannschaftAnalyzer.getName(mann1);
			col2 = mannschaftAnalyzer.getName(mann2);
			break;
		case COL_GRUPPE:
			col1 = "";
			col2 = "";
			if (mann1.getGruppeBO() != null)
				col1 = mann1.getGruppeBO().getName();

			if (mann2.getGruppeBO() != null)
				col2 = mann2.getGruppeBO().getName();

			break;
		default:
			LOG
					.error(Messages.getString("mannschaftentable_unknow_col") + sortColumn); //$NON-NLS-1$
		}

		if (col1 == null)
			return -1;
		if (col2 == null)
			return 1;

		return col1.compareTo(col2);
	}

	public void setData(Vector<Team> objects) {
		data = objects;
		sortColumn();
	}

	public Vector<Team> getData() {
		return data;
	}

};
