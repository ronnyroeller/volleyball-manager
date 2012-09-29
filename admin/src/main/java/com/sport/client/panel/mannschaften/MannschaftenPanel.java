package com.sport.client.panel.mannschaften;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JScrollPane;


import org.apache.log4j.Logger;

import com.sport.client.VolleyFrame;
import com.sport.client.panel.VolleyPanel;
import com.sport.core.bo.SportGroup;
import com.sport.core.bo.Team;
import com.sport.core.bo.Tournament;
import com.sport.core.helper.Messages;
import com.sport.server.remote.TurnierRemote;

/**
 * @author ronny
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class MannschaftenPanel extends VolleyPanel {

	private static final Logger LOG = Logger.getLogger(MannschaftenPanel.class);

	private static final long serialVersionUID = -4394806316108439480L;

	private MannschaftenTable mannschaftenTable;
	private Vector<Team> virtualMannschaften;

	// stores all virtual teams that are not shown

	public MannschaftenPanel(VolleyFrame parentFrame) {
		super(parentFrame);

		mannschaftenTable = new MannschaftenTable(this, new Listener(),
				new Listener());
		mannschaftenTable.removeColumn(mannschaftenTable
				.getColumn(mannschaftenTable.TABLE_COLSORT));

		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(new JScrollPane(mannschaftenTable), BorderLayout.CENTER);
	}

	public void loadData(Tournament aTurnierBO) {
		this.turnierBO = aTurnierBO;
		dirty = false;

		try {
			// Mannschaften und Gruppen laden
			Map<String, Object> result = TurnierRemote
					.getGruppenMannschaftenSpieleByTurnierBO(aTurnierBO, false);

			// Mannschaften laden
			Set<Team> allMannschaften = new HashSet(Arrays
					.asList((Object[]) result.get("mannschaften"))); //$NON-NLS-1$

			// alle virtuellen Mannschaften aussortieren und zwischenspeichern
			Vector<Team> mannschaften = new Vector<Team>();
			virtualMannschaften = new Vector<Team>();
			for (Team mannschaftBO : allMannschaften) {
				if (mannschaftBO.isVirtual()) {
					virtualMannschaften.add(mannschaftBO);
				} else {
					mannschaften.add(mannschaftBO);
				}
			}

			mannschaftenTable.dataModel.setData(mannschaften);

			// M�gliche Gruppen in Combobox laden
			Set<SportGroup> gruppenBO = new HashSet(Arrays
					.asList((Object[]) result.get("gruppen"))); //$NON-NLS-1$
			mannschaftenTable.setGruppen(new Vector<SportGroup>(gruppenBO));

			// Sort-Combobox bauen
			mannschaftenTable.comboSort.removeAllItems();
			for (int i = 1; i <= mannschaften.size(); i++) {
				mannschaftenTable.comboSort.addItem(new Integer(i));
			}

			mannschaftenTable.dataModel
					.sortColumn(MannschaftenTableModel.COL_NAME);
			mannschaftenTable.dataModel
					.sortColumn(MannschaftenTableModel.COL_GRUPPE);
			mannschaftenTable.updateUI(); // f�r Reset!
			mannschaftenTable.clearSelection();
		} catch (Exception e) {
			LOG.error("Can't load teams from server.", e);
		}
	}

	public void save() {
		dirty = false;

		// Wird noch eine Zelle bearbeitet -> Bearbeitung beenden
		if (mannschaftenTable.getEditorComponent() != null) {
			mannschaftenTable.getCellEditor().stopCellEditing();
		}

		// virtuelle Mannschaften wieder anfuegen
		Vector<Team> mannschaften = mannschaftenTable.dataModel
				.getData();
		mannschaften.addAll(virtualMannschaften);

		try {
			TurnierRemote.setMannschaftenByTurnierid(turnierBO.getId(),
					mannschaften.toArray());
		} catch (Exception e) {
			LOG.error("Can't save teams to server.", e);
		}
	}

	/**
	 * F�hrt MannschaftTable aus, wenn die Gruppe einer Mannschaft ge�ndert
	 * wird. Achtung: newGruppe kann GruppeBO sein oder String "Keine Gruppe"!!!
	 * 
	 * @param mannschaftBO
	 * @param newGruppeBO
	 */
	public void listenGruppeChanged(Team mannschaftBO,
			Object aNewGruppeBO) {

		SportGroup newGruppeBO = null;
		if (aNewGruppeBO instanceof SportGroup) {
			newGruppeBO = (SportGroup) aNewGruppeBO;
		}

		if (mannschaftBO.getGruppeBO() != newGruppeBO) {

			Team virtualTeam = null;
			// insert a virtual team in the old group
			if (mannschaftBO.getGruppeBO() != null) {
				virtualTeam = (Team) mannschaftBO.clone();
				virtualTeam.setId(mannschaftBO.getId());
				virtualTeam.setName("");
				virtualMannschaften.add(virtualTeam);
				mannschaftBO.getGruppeBO().getMannschaften().add(virtualTeam);

				mannschaftBO.getGruppeBO().getMannschaften().remove(
						mannschaftBO);
				mannschaftBO.setId(0);
			}
			if (newGruppeBO != null) {

				// if there are still virtual Teams -> delete one!
				virtualTeam = null;
				for (Team mannschaftBO2 : newGruppeBO.getMannschaften()) {
					if (mannschaftBO2.isVirtual()) {
						virtualMannschaften.remove(mannschaftBO2);
						virtualTeam = mannschaftBO2;
					}
				}

				if (virtualTeam == null) {
					mannschaftBO
							.setSort(newGruppeBO.getMannschaften().size() + 1);
					newGruppeBO.addMannschaft(mannschaftBO);
				}
				// Copy Mannschaften
				else {
					mannschaftBO.setId(virtualTeam.getId());
					mannschaftBO.setSort(virtualTeam.getSort());
					mannschaftBO.setGruppeBO(virtualTeam.getGruppeBO());
				}
			} else {
				mannschaftBO.setGruppeBO(null);
			}
		}
	}

	class Listener implements ActionListener, KeyListener {
		public void actionPerformed(ActionEvent e) {
			JMenuItem source = (JMenuItem) (e.getSource());
			if (source.getText() == mannschaftenTable.POPUP_NEWTEAM) {
				addTeam();
			} else if (source.getText() == mannschaftenTable.POPUP_DELTEAM) {
				delTeam();
			}
		}

		private void addTeam() {
			dirty = true;

			// Mannschaft einf�gen
			Team mannschaftBO = new Team();

			// prefix team number with zeros
			DecimalFormat decimalFormat = new DecimalFormat();
			decimalFormat.applyPattern("00");
			String number = decimalFormat.format(mannschaftenTable.dataModel
					.getData().size() + 1);
			mannschaftBO.setName(Messages.getString("mannschaftenpanel_team")
					+ " " + number);

			mannschaftBO.setGruppeBO(null);
			// if only one group available -> preselect it
			if (mannschaftenTable.comboGruppe.getItemCount() == 2) {
				SportGroup gruppeBO = (SportGroup) mannschaftenTable.comboGruppe
						.getItemAt(1);
				mannschaftBO.setGruppeBO(gruppeBO);
			}

			mannschaftenTable.dataModel.getData().add(mannschaftBO);

			// Gruppierungen erweitern
			mannschaftenTable.comboSort.addItem(new Integer(mannschaftBO
					.getSort()));

			// select inserted row
			mannschaftenTable.setRowSelectionInterval(
					mannschaftenTable.dataModel.getData().size() - 1,
					mannschaftenTable.dataModel.getData().size() - 1);

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
				// Aus Gruppenliste l�schen!
				if (oldMannschaft.getGruppeBO() != null) {
					oldMannschaft.getGruppeBO().removeMannschaft(oldMannschaft);
				}
				mannschaftenTable.dataModel.getData().remove(row);

				// L�cke in Gruppierung schliessen
				mannschaftenTable.comboSort
						.removeItemAt(mannschaftenTable.comboSort
								.getItemCount() - 1);

				mannschaftenTable.updateUI();
			}
		}

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_INSERT) {
				addTeam();
			} else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
				delTeam();
			}
		}

		public void keyTyped(KeyEvent e) {
		}

		public void keyReleased(KeyEvent e) {
		}
	}

}
