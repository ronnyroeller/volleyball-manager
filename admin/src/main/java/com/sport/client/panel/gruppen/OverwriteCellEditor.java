package com.sport.client.panel.gruppen;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.sport.analyzer.impl.MannschaftAnalyzerImpl;
import com.sport.core.bo.SportMatch;
import com.sport.core.bo.Team;


/**
 * When typing into a table cell the text is not appended to the field but overwrites it.
 * @author ronny
 *
 */
public class OverwriteCellEditor extends DefaultCellEditor {

	private static final long serialVersionUID = -731934335912059600L;

	public OverwriteCellEditor(){
		super(new JFormattedTextField());
	}
	
	public Component getTableCellEditorComponent 
		(JTable table, Object value, boolean isSelected, int row, int column) {

		String text = value.toString();
		
		// Logic was moved from BO to administrator
		if (value instanceof Team)
			text = MannschaftAnalyzerImpl.getInstance().getName(((Team) value));
		else if (value instanceof SportMatch) {
			SportMatch match = (SportMatch) value;
			if (match.getTeam1() != null && match.getTeam2() != null) {
				text= MannschaftAnalyzerImpl.getInstance().getName(match.getTeam1())
						+ " - "
						+ MannschaftAnalyzerImpl.getInstance().getName(match.getTeam2());
			}
		}
		
		((JTextField)editorComponent).setText (text);
		((JTextField)editorComponent).selectAll ();
		return editorComponent;
	}
	
}
