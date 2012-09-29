package com.sport.client.panel.spielplan;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.sport.core.bo.SpielplanTableEntryBO;
import com.sport.core.bo.SportMatch;

/**
 * Renders a cell in the color of the group of the match that it represents
 * 
 * @author Ronny
 *
 */
public class ColorTableCellRender extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 5817071841157726787L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
				row, column);

		if (isSelected && (column == table.getSelectedColumn())) {
			return this;
		}

		int col = table.convertColumnIndexToModel(column);
		if ((col % 2 == 1) && (col > 0)) {
			SpieleTableModel dataModel = ((SpielplanTable) table).dataModel;
			SpielplanTableEntryBO tableEntry = (SpielplanTableEntryBO) dataModel
					.getData().get(row);

			SportMatch spielBO = tableEntry.getSpiel((col - 1) / 2);
			String color = "#ffffff"; //$NON-NLS-1$
			if (spielBO != null) {
				color = spielBO.getGroup().getColor();
			}

			// Zerlegen der HTML-Farbangabe
			int[] part = new int[3];
			part[0] = Integer.parseInt(color.substring(1, 3), 16);
			part[1] = Integer.parseInt(color.substring(3, 5), 16);
			part[2] = Integer.parseInt(color.substring(5, 7), 16);

			setBackground(new Color(part[0], part[1], part[2]));
		} else {
			setBackground(Color.white);
			setForeground(Color.black);
		}
		return this;
	}

}
