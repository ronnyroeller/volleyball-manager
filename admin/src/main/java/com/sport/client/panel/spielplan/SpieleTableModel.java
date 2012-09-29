package com.sport.client.panel.spielplan;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import com.sport.analyzer.MannschaftAnalyzer;
import com.sport.analyzer.SpielAnalyzer;
import com.sport.analyzer.SpielAnalyzer.DetailedMatchResult;
import com.sport.analyzer.impl.MannschaftAnalyzerImpl;
import com.sport.analyzer.impl.SpielAnalyzerImpl;
import com.sport.core.bo.Field;
import com.sport.core.bo.SpielplanTableEntryBO;
import com.sport.core.bo.SportMatch;
import com.sport.core.bo.comparators.SpielplanTableEntryComparator;
import com.sport.core.helper.Messages;

class SpieleTableModel extends AbstractTableModel {

	private static final Logger LOG = Logger.getLogger(SpieleTableModel.class);

	private static final long serialVersionUID = -1307313482104223524L;

	protected final static int COLVONDATUM = 0;

	private Vector<SpielplanTableEntryBO> data = new Vector<SpielplanTableEntryBO>();
	private Vector<Field> header = new Vector<Field>();
	private SpielplanTable table;

	private final MannschaftAnalyzer mannschaftAnalyzer = MannschaftAnalyzerImpl
			.getInstance();

	public SpieleTableModel(SpielplanTable table,
			Vector<SpielplanTableEntryBO> data, Vector<Field> header) {
		super();
		this.table = table;
		this.header = header;
		setData(data);
	}

	public int getColumnCount() {
		return 1 + 2 * header.size();
	}

	public int getRowCount() {
		return data.size();
	}

	public Object getValueAt(int row, int col) {
		SpielplanTableEntryBO tableEntry = (SpielplanTableEntryBO) data
				.get(row);
		switch (col) {
		case COLVONDATUM:
			return DateFormat.getTimeInstance(DateFormat.SHORT).format(
					tableEntry.getVondatum());
		}
		
		// Every 2nd cell is empty cell rendered in the color of the group of the match
		if ((col - 1) % 2 == 0) {
			return " "; //$NON-NLS-1$
		}
		
		SportMatch spielBO = tableEntry.getSpiel((col - 1) / 2);
		if (spielBO == null) {
			return ""; //$NON-NLS-1$
		}
		String ergebnisse = ""; //$NON-NLS-1$
		DetailedMatchResult resultSpiel = SpielAnalyzerImpl.getInstance()
				.getErgebnisDetails(spielBO);
		if (resultSpiel.winner != SpielAnalyzer.MatchResult.NICHTGESPIELT) {
			ergebnisse = " (" //$NON-NLS-1$
					+ resultSpiel.saetze1 //$NON-NLS-1$
					+ ":" //$NON-NLS-1$
					+ resultSpiel.saetze2 //$NON-NLS-1$
					+ ")"; //$NON-NLS-1$
		}
		if (table.isRelNames()) {
			// Result view
			return mannschaftAnalyzer.getRelName(spielBO.getTeam1())
					+ " - " //$NON-NLS-1$
					+ mannschaftAnalyzer.getRelName(spielBO.getTeam2())
					+ ergebnisse;
		}

		// Schedule view
		return mannschaftAnalyzer.getName(spielBO.getTeam1())
				+ " - " //$NON-NLS-1$
				+ mannschaftAnalyzer.getName(spielBO.getTeam2())
				+ ergebnisse;
	}

	public String getColumnName(int column) {
		switch (column) {
		case COLVONDATUM:
			return table.TABLE_COLVONDATUM;
		default:
			if ((column - 1) % 2 == 0) {
				return "COL_PLATZFARBE" + (column - 1) / 2; //$NON-NLS-1$
			}
			return "COL_PLATZ" + (column - 1) / 2; //$NON-NLS-1$
		}
	}

	@SuppressWarnings("unchecked")
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	public boolean isCellEditable(int row, int col) {
		return false;
	}

	public void setValueAt(Object aValue, int row, int column) {
		SpielplanTableEntryBO tableEntry = (SpielplanTableEntryBO) data
				.get(row);

		table.parent.dirty = true;

		if (column == COLVONDATUM) {
			tableEntry.setVondatum((Date) aValue);
		} else {
			if (!(aValue instanceof SportMatch)) {
				LOG.error(Messages
						.getString("spielplantable_error_in_setvalueat")); //$NON-NLS-1$
			}
			int pos = (column - 1) / 2;
			SportMatch spielBO = (SportMatch) aValue;
			// Platz, Zeit anpassen
			spielBO.setStartDate(tableEntry.getVondatum());
			spielBO.setEndDate(tableEntry.getBisdatum());
			spielBO.setField((Field) header.get(pos));

			tableEntry.setSpiel(pos, spielBO);
		}
	}

	public void sortColumn() {
		Collections.sort(data, new SpielplanTableEntryComparator());
	}

	// in TableEntrys umschreiben
	public void setData(Vector<SpielplanTableEntryBO> data) {
		this.data = data;
	}

	public Vector<SpielplanTableEntryBO> getData() {
		return data;
	}

	public void setHeader(Vector<Field> header) {
		this.header = header;

		// alle alten Spalten l�schen
		for (int i = 1; table.getColumnCount() > 1; i++) {
			table.removeColumn(table.getColumn(getColumnName(i)));
		}

		// neue Spalten einf�gen
		for (int i = 0; i < header.size(); i++) {
			TableColumn gruppefarbeCol = new TableColumn(2 * i + 1);
			gruppefarbeCol.setMaxWidth(10);
			gruppefarbeCol.setIdentifier("COL_PLATZFARBE" + i); //$NON-NLS-1$
			gruppefarbeCol.setHeaderValue(" "); //$NON-NLS-1$
			table.addColumn(gruppefarbeCol);

			Field spielplatzBO = (Field) header.get(i);
			TableColumn paarungCol = new TableColumn(2 * i + 2);
			paarungCol.setIdentifier("COL_PLATZ" + i); //$NON-NLS-1$
			paarungCol.setHeaderValue(spielplatzBO.getName());
			table.addColumn(paarungCol);
		}
	}

	/**
	 * verschiebt die Anfangszeit der Bloecke um timediff (alle Bloecke, die <=
	 * vondatum starten!
	 */
	public void moveTime(Date vondatum, long timediff) {
		// Zeitdifferenz auf alle folgenden Bloecke anwenden!
		for (SpielplanTableEntryBO spielplanTableEntry : getData()) {
			// nur spaetere Bloecke
			if (!vondatum.after(spielplanTableEntry.getVondatum()))
				spielplanTableEntry.moveDatum(timediff);

		}
	}

	/**
	 * @return
	 */
	public Vector<Field> getHeader() {
		return header;
	}

	/**
	 * Returns the spielplatz which is on the header position
	 * 
	 * @param headerIndex
	 * @return
	 */
	public Field getSpielplatz(int headerIndex) {
		return getHeader().get(headerIndex);
	}
};
