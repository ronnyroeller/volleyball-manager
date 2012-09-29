package com.sport.client.panel.platzierungen;

import java.util.Collections;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;


import org.apache.log4j.Logger;

import com.sport.analyzer.MannschaftAnalyzer;
import com.sport.analyzer.impl.MannschaftAnalyzerImpl;
import com.sport.core.bo.Ranking;
import com.sport.core.bo.SportGroup;
import com.sport.core.bo.Team;
import com.sport.core.bo.comparators.PlatzierungenComparator;
import com.sport.core.helper.Messages;

class PlatzierungenTableModel extends AbstractTableModel {

	private static final Logger LOG = Logger.getLogger(PlatzierungenTableModel.class);

	private static final long serialVersionUID = 7136540282013297832L;

	private Vector<Ranking> data = new Vector<Ranking>();
	protected final static int COL_GRUPPENFARBE = 0;
	protected final static int COL_PLATZNR = 1;
	protected final static int COL_MANNSCHAFT = 2;

	private PlatzierungenTable table;

	private final MannschaftAnalyzer mannschaftAnalyzer = MannschaftAnalyzerImpl
			.getInstance();

	public PlatzierungenTableModel(PlatzierungenTable table) {
		super();
		this.table = table;
	}

	public int getColumnCount() {
		return 3;
	}

	public int getRowCount() {
		return data.size();
	}

	public Object getValueAt(int row, int col) {
		Ranking platzierungBO = (Ranking) data.get(row);
		switch (col) {
		case COL_GRUPPENFARBE:
			return ""; //$NON-NLS-1$
		case COL_PLATZNR:
			return new Long(platzierungBO.getRank());
		case COL_MANNSCHAFT:
			if (platzierungBO.getMannschaft() == null) {
				return Messages.getString("platzierungentable_please_select"); //$NON-NLS-1$
			}
			return mannschaftAnalyzer.getRelName(platzierungBO.getMannschaft());
		}
		return Messages.getString("platzierungentable_unknown_col") + col; //$NON-NLS-1$
	}

	public String getColumnName(int column) {
		switch (column) {
		case COL_GRUPPENFARBE:
			return table.TABLE_COLGRUPPENFARBE;
		case COL_PLATZNR:
			return table.TABLE_COLPLATZNR;
		case COL_MANNSCHAFT:
			return table.TABLE_COLMANNSCHAFT;
		}
		return Messages.getString("platzierungentable_unknown_col") + column; //$NON-NLS-1$
	}

	@SuppressWarnings("unchecked")
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	public boolean isCellEditable(int row, int col) {
		return (col > 0);
	}

	public void setValueAt(Object aValue, int row, int column) {
		Ranking platzierungBO = (Ranking) data.get(row);

		table.parent.dirty = true;

		switch (column) {
		case COL_PLATZNR:
			/*
			 * // jeder Sortwert darf nur einmal existieren! -> tauschen mit
			 * anderem long oldPlatznr = platzierungBO.getPlatznr(); int
			 * newPlatznr = ((Long) aValue).intValue(); Iterator it =
			 * data.iterator(); while (it.hasNext()) { PlatzierungBO
			 * platzierungBO2 = (PlatzierungBO) it.next(); if
			 * (platzierungBO2.getPlatznr() == newPlatznr) {
			 * platzierungBO2.setPlatznr(oldPlatznr); } }
			 * platzierungBO.setPlatznr(newPlatznr); Collections.sort (data, new
			 * Comparatoren.PlatzierungenComparator ());
			 */
			platzierungBO.setRank(((Long) aValue).intValue());
			Collections.sort(data, new PlatzierungenComparator());
			break;
		default:
			LOG.error(Messages
					.getString("platzierungentable_unknown_col") + column); //$NON-NLS-1$
		}
	}

	public void setData(Vector<Ranking> objects) {
		data = objects;

		// default -> soviele Platzierungen wie physische Mannschaften
		if (data.size() == 0) {
			int counter = 1;
			for (SportGroup gruppeBO : table.gruppen) {

				for (Team mannschaftBO : gruppeBO.getMannschaften()) {
					if (mannschaftBO.getLoggruppeBO() == null
							&& mannschaftBO.getLogspielBO() == null) {
						Ranking platzierungBO = new Ranking();
						platzierungBO.setRank(counter++);
						data.add(platzierungBO);
						table.parent.dirty = true;
					}
				}

			}
		}

		Collections.sort(data, new PlatzierungenComparator());

		// Platznr-Combobox bauen
		table.comboPlatznr.removeAllItems();
		for (int i = 1; i <= data.size(); i++) {
			table.comboPlatznr.addItem(new Long(i));
		}
	}

	public Vector<Ranking> getData() {
		return data;
	}

};
