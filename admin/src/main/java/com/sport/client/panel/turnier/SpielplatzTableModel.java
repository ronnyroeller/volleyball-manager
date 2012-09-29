package com.sport.client.panel.turnier;

import java.util.Collections;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;


import org.apache.log4j.Logger;

import com.sport.core.bo.Field;
import com.sport.core.bo.comparators.SpielplatzComparator;
import com.sport.core.helper.Messages;

class SpielplatzTableModel extends AbstractTableModel {

	private static final Logger LOG = Logger.getLogger(SpielplatzTableModel.class);

	private static final long serialVersionUID = -7431400994814991756L;

	private SpielplaetzeTable table;

	private Vector<Field> data = new Vector<Field>();
	final static int COL_NAME = 0;

	public SpielplatzTableModel(SpielplaetzeTable table,
			Vector<Field> data) {
		super();
		this.data = data;
		this.table = table;
	}

	public int getColumnCount() {
		return 1;
	}

	public int getRowCount() {
		return data.size();
	}

	public Object getValueAt(int row, int col) {
		Field spielplatzBO = (Field) data.get(row);
		switch (col) {
		case COL_NAME:
			return spielplatzBO.getName();
		}
		return Messages.getString("spielplaetzetable_unknow_col") + col; //$NON-NLS-1$
	}

	public String getColumnName(int column) {
		switch (column) {
		case COL_NAME:
			return table.TABLE_COLNAME;
		}
		return Messages.getString("spielplaetzetable_unknow_col") + column; //$NON-NLS-1$
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

		Field spielplatzBO = (Field) data.get(row);
		switch (column) {
		case COL_NAME:
			spielplatzBO.setName((String) aValue);
			break;
		default:
			LOG.error(Messages
					.getString("spielplaetzetable_unknow_col") + column); //$NON-NLS-1$
		}
	}

	public void setData(Vector<Field> objects) {
		data = objects;
		Collections.sort(data, new SpielplatzComparator());
	}

	public Vector<Field> getData() {
		return data;
	}

};
