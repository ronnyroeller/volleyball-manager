package com.sport.client.panel.gruppen;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


import org.apache.log4j.Logger;

import com.sport.client.VolleyFrame;
import com.sport.client.panel.VolleyPanel;
import com.sport.client.panel.mannschaften.MannschaftenTable;
import com.sport.core.bo.SportGroup;
import com.sport.core.bo.Team;
import com.sport.core.bo.Tournament;
import com.sport.core.bo.TournamentSystem;
import com.sport.core.helper.Messages;
import com.sport.server.remote.TurnierRemote;

/**
 * @author ronny
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class GruppenPanel extends VolleyPanel {

	private static final Logger LOG = Logger.getLogger(GruppeTableModel.class);

	private static final long serialVersionUID = -5392304373119199033L;

	private GruppenTable gruppenTable;
	protected MannschaftenTable mannschaftenTable;

	public GruppenPanel(VolleyFrame parentFrame) {
		super(parentFrame);

		gruppenTable = new GruppenTable(this, new GruppeListener(),
				new GruppeListener(), new GruppeListSelectionListener());
		mannschaftenTable = new MannschaftenTable(this,
				new MannschaftListener(), new MannschaftListener());
		// mannschaftenTable.removeColumn(
		// mannschaftenTable.getColumn(MannschaftenTable.TABLE_COLGRUPPE));
		mannschaftenTable.getColumn(mannschaftenTable.TABLE_COLNAME)
				.setCellEditor(new LogischCellEditor(mannschaftenTable));
		mannschaftenTable.removeColumn(mannschaftenTable
				.getColumn(mannschaftenTable.TABLE_COLGRUPPE));

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setBorder(BorderFactory.createEmptyBorder());
		splitPane.setContinuousLayout(true);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(200);

		splitPane.add(new JScrollPane(gruppenTable));
		splitPane.add(new JScrollPane(mannschaftenTable));

		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(splitPane, BorderLayout.CENTER);
	}

	public void loadData(Tournament aTurnierBO) {
		this.turnierBO = aTurnierBO;

		dirty = false;

		try {
			Map<String, Object> map = TurnierRemote
					.getGruppenMannschaftenSpieleByTurnierBO(aTurnierBO, true);

			// M�gliche Gruppen laden
			Set<SportGroup> gruppenBO = new HashSet(Arrays.asList((Object[]) map
					.get("gruppen"))); //$NON-NLS-1$
			gruppenTable.dataModel.setData(new Vector<SportGroup>(gruppenBO));

			// Alle m�glichen Modi in Combobox laden
			Set<TournamentSystem> modiBO = TurnierRemote.getModi();
			gruppenTable.setModi(new Vector<TournamentSystem>(modiBO));

			gruppenTable.updateUI(); // f�r Reset!
			gruppenTable.clearSelection();
		} catch (Exception e) {
			LOG.error("Can't load groups and teams from server.", e);
		}
	}

	public void save() {
		dirty = false;

		// Wird noch eine Zelle bearbeitet -> Bearbeitung beenden
		if (gruppenTable.getEditorComponent() != null) {
			gruppenTable.getCellEditor().stopCellEditing();
		}

		try {
			TurnierRemote.setGruppenMannschaftenByTurnierid(turnierBO.getId(),
					gruppenTable.dataModel.getData().toArray());
		} catch (Exception e) {
			LOG.error("Can't save groups and teams to server.", e);
		}
	}

	/**
	 * F�hrt MannschaftTable aus, wenn die Gruppe einer Mannschaft ge�ndert
	 * wird.
	 * 
	 * @param mannschaftBO
	 * @param newGruppeBO
	 */
	public void listenGruppeChanged(Team mannschaftBO,
			SportGroup newGruppeBO) {

		SportGroup newDataModellGruppeBO = (SportGroup) gruppenTable.dataModel
				.getData().get(
						gruppenTable.dataModel.getData().indexOf(newGruppeBO));

		if (mannschaftBO.getGruppeBO() != newDataModellGruppeBO) {
			mannschaftBO.getGruppeBO().removeMannschaft(mannschaftBO);
			mannschaftBO
					.setSort(newDataModellGruppeBO.getMannschaften().size() + 1);
			newDataModellGruppeBO.addMannschaft(mannschaftBO);
		}
	}

	class GruppeListener implements ActionListener, KeyListener {
		public void actionPerformed(ActionEvent e) {
			JMenuItem source = (JMenuItem) (e.getSource());
			if (source.getText() == gruppenTable.POPUP_NEWGROUP) {
				addGroup();
			} else if (source.getText() == gruppenTable.POPUP_DELGROUP) {
				delGroup();
			}
		}

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_INSERT) {
				addGroup();
			} else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
				delGroup();
			}
		}

		public void keyTyped(KeyEvent e) {
		}

		public void keyReleased(KeyEvent e) {
		}

		private void delGroup() {

			int row = gruppenTable.getSelectedRow();
			// Zeile ausgwe�hlt?
			if (row > -1 && row < gruppenTable.dataModel.getData().size()) {
				dirty = true;
				if (gruppenTable.getCellEditor() != null) {
					gruppenTable.getCellEditor().stopCellEditing();
				}

				SportGroup gruppeBO = (SportGroup) gruppenTable.dataModel.getData()
						.get(row);
				int oldSort = gruppeBO.getSort();
				gruppenTable.dataModel.getData().remove(row);

				// Sortierung anpassen
				gruppenTable.comboSort.removeItemAt(gruppenTable.comboSort
						.getItemCount() - 1);
				for (SportGroup gruppeBO2 : gruppenTable.dataModel.getData()) {
					if (gruppeBO2.getSort() > oldSort) {
						gruppeBO2.setSort(gruppeBO2.getSort() - 1);
					}
				}

				gruppenTable.updateUI();
				updateMannschaftenTable();
			}
		}

		private void addGroup() {
			dirty = true;

			int row = gruppenTable.getSelectedRow() + 1;

			// Gruppe einf�gen
			SportGroup gruppeBO = new SportGroup(turnierBO);
			gruppeBO.setSort(gruppenTable.dataModel.getData().size() + 1);

			// prefix group number with zeros
			DecimalFormat decimalFormat = new DecimalFormat();
			decimalFormat.applyPattern("00");
			String number = decimalFormat.format(gruppenTable.dataModel
					.getData().size() + 1);
			gruppeBO
					.setName(Messages.getString("gruppenpanel_group") + " " + number); //$NON-NLS-1$

			gruppeBO.setColor("#ffffff"); //$NON-NLS-1$
			// oberster Eintrag aus Modi-Combobox
			TournamentSystem modusBO = (TournamentSystem) gruppenTable.comboModus.getItemAt(0);
			gruppeBO.setTournamentSystem(modusBO);
			gruppeBO.setMannschaften(new Vector<Team>());

			gruppeBO.setSort(row + 1);

			// Gruppierung anpassen
			for (SportGroup gruppeBO2 : gruppenTable.dataModel.getData()) {
				if (gruppeBO2.getSort() >= gruppeBO.getSort()) {
					gruppeBO2.setSort(gruppeBO2.getSort() + 1);
				}
			}

			if (gruppenTable.dataModel.getData().size() > 0) {
				gruppenTable.dataModel.getData().add(row, gruppeBO);
			} else {
				gruppenTable.dataModel.getData().add(gruppeBO);
			}

			// Gruppierungen erweitern
			gruppenTable.comboSort.addItem(new Integer(gruppenTable.dataModel
					.getData().size()));

			// select inserted row
			gruppenTable.setRowSelectionInterval(row, row);

			gruppenTable.updateUI();
			updateMannschaftenTable();
		}
	}

	/**
	 * Aktuallisiert Ansicht der MannschaftenTabelle
	 * 
	 * @author ronny
	 * 
	 *         To change the template for this generated type comment go to
	 *         Window>Preferences>Java>Code Generation>Code and Comments
	 */
	private void updateMannschaftenTable() {
		SportGroup gruppeBO = null;
		int col = mannschaftenTable.getEditingColumn();
		int row = mannschaftenTable.getEditingRow();

		// aktuelle Bearbeitung beenden
		if (col > -1 && row > -1) {
			// Bearbeitung beenden, sonst Abbrechen
			if (!mannschaftenTable.getCellEditor(row, col).stopCellEditing()) {
				mannschaftenTable.getCellEditor(row, col).cancelCellEditing();
			}
		}
		if (gruppenTable.getSelectedRow() > -1
				&& (gruppenTable.getSelectedRow() < gruppenTable.dataModel
						.getData().size())) {
			gruppeBO = (SportGroup) gruppenTable.dataModel.getData().get(
					gruppenTable.getSelectedRow());

			mannschaftenTable.setGruppen(gruppenTable.dataModel.getData());

			// Sort-Combobox bauen
			mannschaftenTable.comboSort.removeAllItems();
			for (int i = 1; i <= gruppeBO.getMannschaften().size(); i++) {
				mannschaftenTable.comboSort.addItem(new Integer(i));
			}
		} else { // keine Gruppe ausgewaehlt
			gruppeBO = new SportGroup(turnierBO);
			gruppeBO.setMannschaften(new Vector<Team>());
			mannschaftenTable.comboGruppe.removeAllItems();
			mannschaftenTable.comboSort.removeAllItems();
		}
		mannschaftenTable.dataModel.setData(gruppeBO.getMannschaften());
		mannschaftenTable.clearSelection();
		mannschaftenTable.updateUI();
	}

	/**
	 * Zeigt Mannschaften der aktuell ausgew�hlten Gruppe
	 * 
	 * @author ronny
	 */
	class GruppeListSelectionListener implements ListSelectionListener {

		public void valueChanged(ListSelectionEvent e) {
			updateMannschaftenTable();
		}
	}

	/**
	 * �berwacht Popup-Fenster der MannschaftenTable
	 * 
	 * @author ronny
	 * 
	 *         To change the template for this generated type comment go to
	 *         Window>Preferences>Java>Code Generation>Code and Comments
	 */
	class MannschaftListener implements ActionListener, KeyListener {

		public void actionPerformed(ActionEvent e) {
			JMenuItem source = (JMenuItem) (e.getSource());
			if (source.getText() == mannschaftenTable.POPUP_LOGTEAM) {
				logTeam();
			} else if (source.getText() == mannschaftenTable.POPUP_NEWTEAM) {
				addTeam();
			} else if (source.getText() == mannschaftenTable.POPUP_DELTEAM) {
				delTeam();
			}
		}

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_INSERT) {
				addTeam();
			} else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
				delTeam();
			} else if (e.getKeyCode() == KeyEvent.VK_ENTER
					&& ((e.getModifiersEx() & KeyEvent.ALT_DOWN_MASK) == KeyEvent.ALT_DOWN_MASK)) {
				logTeam();
			}
		}

		public void keyTyped(KeyEvent e) {
		}

		public void keyReleased(KeyEvent e) {
		}

		private void addTeam() {
			dirty = true;

			int row = mannschaftenTable.getSelectedRow() + 1;

			// Mannschaft einf�gen
			Team mannschaftBO = new Team();

			/*
			 * // Mannschaften standardmaessig durchnummerieren int lastIndex =
			 * 1; for (int i = 0; i <
			 * mannschaftenTable.comboGruppe.getItemCount(); i++) { GruppeBO
			 * gruppeBO = (GruppeBO) mannschaftenTable.comboGruppe.getItemAt(i);
			 * Iterator it = gruppeBO.getMannschaften().iterator(); while
			 * (it.hasNext()) { MannschaftBO mannschaftBO2 = (MannschaftBO)
			 * it.next(); if (!mannschaftBO2.isLog()) { lastIndex++; } } }
			 * mannschaftBO.setName(Messages.getString("gruppenpanel_team") +
			 * " " + lastIndex); //$NON-NLS-1$
			 */
			mannschaftBO.setName("");

			SportGroup gruppeBO = (SportGroup) gruppenTable.dataModel.getData()
					.get(gruppenTable.getSelectedRow());
			mannschaftBO.setGruppeBO(gruppeBO);
			mannschaftBO.setTurnierBO(gruppeBO.getTournament());

			mannschaftBO.setSort(row + 1);

			// Gruppierung anpassen
			for (Team mannschaftBO2 : mannschaftenTable.dataModel.getData()) {
				if (mannschaftBO2.getSort() >= mannschaftBO.getSort()) {
					mannschaftBO2.setSort(mannschaftBO2.getSort() + 1);
				}
			}

			if (mannschaftenTable.dataModel.getData().size() > 0) {
				mannschaftenTable.dataModel.getData().add(row, mannschaftBO);
			} else {
				mannschaftenTable.dataModel.getData().add(mannschaftBO);
			}

			// Gruppierungen erweitern
			mannschaftenTable.comboSort.addItem(new Integer(
					mannschaftenTable.dataModel.getData().size()));

			// select inserted row
			mannschaftenTable.setRowSelectionInterval(row, row);

			mannschaftenTable.updateUI();
		}

		private void delTeam() {
			dirty = true;

			int row = mannschaftenTable.getSelectedRow();
			// Zeile ausgwe�hlt?
			if (row > -1 && row < mannschaftenTable.dataModel.getData().size()) {
				if (mannschaftenTable.getCellEditor() != null) {
					mannschaftenTable.getCellEditor().stopCellEditing();
				}

				Team oldMannschaft = (Team) mannschaftenTable.dataModel
						.getData().get(row);
				oldMannschaft.getGruppeBO().removeMannschaft(oldMannschaft);
				mannschaftenTable.comboSort
						.removeItemAt(mannschaftenTable.comboSort
								.getItemCount() - 1);

				mannschaftenTable.updateUI();
			}
		}

	}

	@SuppressWarnings("serial")
	class LogischCellEditor extends OverwriteCellEditor {

		private MannschaftenTable thisMannschaftenTable;

		public LogischCellEditor(MannschaftenTable mannschaftenTable) {
			super();

			this.thisMannschaftenTable = mannschaftenTable;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.CellEditor#isCellEditable(java.util.EventObject)
		 */
		public boolean isCellEditable(EventObject anEvent) {
			Team mannschaftBO = null;
			int row = (thisMannschaftenTable).getSelectedRow();
			if (row > -1
					&& row < thisMannschaftenTable.dataModel.getData().size()) {
				mannschaftBO = (Team) thisMannschaftenTable.dataModel
						.getData().get(row);
			}
			if (anEvent instanceof MouseEvent) {

				boolean is_alt = (((MouseEvent) anEvent).getModifiersEx() & MouseEvent.ALT_DOWN_MASK) == MouseEvent.ALT_DOWN_MASK;

				// bei [ALT] -> Logische Mannschaft aussuchen (log.
				// Mannschaften duerfen nur noch log. behandelt werden)
				// nur, wenn noch eine andere Gruppe vorhanden!
				if ((((MouseEvent) anEvent).getClickCount() > 1)
						&& (is_alt || mannschaftBO.getLoggruppeBO() != null || mannschaftBO
								.getLogspielBO() != null)) {
					logTeam(mannschaftBO);

					return false;
				}
			} // einmal auf log. gesetzte Mannschaft koennen nicht mehr
			// phy.
			// werden
			if (mannschaftBO != null && mannschaftBO.getLoggruppeBO() != null) {
				return false;
			}

			return super.isCellEditable(anEvent);
		}

	}

	private void logTeam() {
		int row = mannschaftenTable.getSelectedRow();
		// Zeile ausgwe�hlt?
		if (row > -1 && row < mannschaftenTable.dataModel.getData().size()) {
			if (mannschaftenTable.getCellEditor() != null) {
				mannschaftenTable.getCellEditor().stopCellEditing();
			}

			Team mannschaftBO = (Team) mannschaftenTable.dataModel
					.getData().get(row);
			logTeam(mannschaftBO);
		}
	}

	/**
	 * Starts Dialog to choose a log. team
	 * 
	 * @param mannschaftBO
	 */
	private void logTeam(Team mannschaftBO) {
		Vector<SportGroup> gruppen = (Vector<SportGroup>) gruppenTable.dataModel.getData().clone();
		if (gruppen.size() > 1) {
			// nicht aktuelle ausgew�hlte Gruppe
			SportGroup gruppeBO = (SportGroup) gruppenTable.dataModel.getData()
					.get(gruppenTable.getSelectedRow());
			gruppen.removeElement(gruppeBO);
			LogischMannschaftDialog dlg = new LogischMannschaftDialog(
					parentFrame, mannschaftBO, gruppen);
			dlg.setModal(true);
			dlg.show();

			if (dlg.getResult() == true) {
				Team logMannschaft = (Team) dlg.comboMannschaft
						.getSelectedItem();
				mannschaftBO.setName("auto"); //$NON-NLS-1$

				if (logMannschaft.getLogspielBO() != null) {
					mannschaftBO.setLogspielBO(logMannschaft.getLogspielBO());
					mannschaftBO.setLogsort(logMannschaft.getLogsort());
					mannschaftBO.setLoggruppeBO(null);
				} else {
					mannschaftBO.setLoggruppeBO(logMannschaft.getGruppeBO());
					mannschaftBO.setLogsort(logMannschaft.getSort());
					mannschaftBO.setLogspielBO(null);
				}
			}
		}
	}
}
