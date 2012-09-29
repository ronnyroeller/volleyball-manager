package com.sport.client.panel.platzierungen;

import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;

import com.sport.client.panel.gruppen.LogischMannschaftDialog;
import com.sport.core.bo.Ranking;
import com.sport.core.bo.Team;


class LogischCellEditor extends DefaultCellEditor {

	private static final long serialVersionUID = -6995565039937303236L;

	private PlatzierungenTable platzierungenTable;

	public LogischCellEditor(PlatzierungenTable platzierungenTable) {
		super(new JTextField());

		this.platzierungenTable = platzierungenTable;
	}

	public boolean isCellEditable(EventObject anEvent) {
		Ranking platzierungBO = null;
		int row = (platzierungenTable).getSelectedRow();
		if (row > -1 && row < platzierungenTable.dataModel.getRowCount()) {
			platzierungBO = (Ranking) platzierungenTable.dataModel
					.getData().get(row);
			Team mannschaftBO = platzierungBO.getMannschaft();

			if (platzierungBO != null && anEvent instanceof MouseEvent) {

				// bei Doppelklick -> Logische Mannschaft aussuchen
				if (((MouseEvent) anEvent).getClickCount() > 1) {

					LogischMannschaftDialog dlg = new LogischMannschaftDialog(
							platzierungenTable.parent.parentFrame,
							mannschaftBO,
							platzierungenTable.gruppen);

					dlg.setModal(true);
					dlg.show();
					if (dlg.getResult() == true) {
						platzierungenTable.parent.dirty = true;

						Team logMannschaft = (Team) dlg.comboMannschaft
								.getSelectedItem();

						if (mannschaftBO == null) {
							mannschaftBO = new Team();
							platzierungBO.setMannschaft(mannschaftBO);
						}
						mannschaftBO.setName("auto"); //$NON-NLS-1$

						// ist neu erzeugte logische mannschaft (also nicht
						// gruppenmitglied)?
						if (logMannschaft.getLogspielBO() != null
								&& logMannschaft.getGruppeBO() == null) {
							mannschaftBO.setLogspielBO(logMannschaft
									.getLogspielBO());
							mannschaftBO.setLogsort(logMannschaft.getLogsort());
							mannschaftBO.setLoggruppeBO(null);
						} else {
							mannschaftBO.setLoggruppeBO(logMannschaft
									.getGruppeBO());
							mannschaftBO.setLogsort(logMannschaft.getSort());
							mannschaftBO.setLogspielBO(null);
						}
					}
				}
			}
		}

		return false;
	}

}
