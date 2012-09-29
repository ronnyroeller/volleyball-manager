package com.sport.client.panel.gruppen;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.DefaultCellEditor;
import javax.swing.JColorChooser;
import javax.swing.JTextField;

import com.sport.core.helper.Messages;


/**
 * Ruft ColorChoser auf f�r Farbeingabe
 * 
 * @author ronny
 * 
 */
class FarbeCellEditor extends DefaultCellEditor {

	private static final long serialVersionUID = -8411681387080217298L;

	private GruppenTable gruppenTable = null;

	public FarbeCellEditor(GruppenTable gruppenTable) {
		super(new JTextField());
		this.gruppenTable = gruppenTable;
	}

	public boolean isCellEditable(EventObject anEvent) {
		int row = gruppenTable.getSelectedRow();
		if (anEvent instanceof MouseEvent) {

			// bei Doppelklick
			if (((MouseEvent) anEvent).getClickCount() > 1) {

				Color c = JColorChooser.showDialog(gruppenTable, Messages
						.getString("gruppentable_select_color"), //$NON-NLS-1$
						null);

				if (c != null) {
					String red = Integer.toString(c.getRed(), 16);
					String green = Integer.toString(c.getGreen(), 16);
					String blue = Integer.toString(c.getBlue(), 16);

					String htmlcolor = "#"; //$NON-NLS-1$
					if (red.length() < 2) {
						htmlcolor += "0"; //$NON-NLS-1$
					}
					htmlcolor += red;
					if (green.length() < 2) {
						htmlcolor += "0"; //$NON-NLS-1$
					}
					htmlcolor += green;
					if (blue.length() < 2) {
						htmlcolor += "0"; //$NON-NLS-1$
					}
					htmlcolor += blue;

					gruppenTable.dataModel.setValueAt(htmlcolor, row,
							GruppeTableModel.COL_FARBE);
				}

				return false;
			}
		}

		return super.isCellEditable(anEvent);
	}

}
