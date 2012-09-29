package com.sport.client.panel.spielplan;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


import org.apache.log4j.Logger;

import com.sport.client.TreeNodeObject;
import com.sport.client.VolleyFrame;
import com.sport.client.panel.VolleyPanel;
import com.sport.core.bo.Field;
import com.sport.core.bo.SpielplanTableEntryBO;
import com.sport.core.bo.SportGroup;
import com.sport.core.bo.SportMatch;
import com.sport.core.bo.Tournament;
import com.sport.scheduler.ScheduleGen;
import com.sport.server.remote.TurnierRemote;

/**
 * @author ronny
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SpielplanPanel extends VolleyPanel {

	private static final Logger LOG = Logger.getLogger(SpielplanPanel.class);

	private static final long serialVersionUID = -2699395387514023992L;

	protected SpielplanTable spielplanTable;
	private SpielEditPanel spielEdit = null;
	private SpielBlockEditPanel spielBlockEdit = null;
	private JScrollPane editScroll = new JScrollPane();
	protected JPanel nullPanel = new JPanel();
	protected JPanel editPanel = nullPanel; // aktueller Editor
	private Vector<SportGroup> gruppen = null;
	private Vector<Field> spielplaetze = null;

	public SpielplanPanel(VolleyFrame parentFrame) {
		super(parentFrame);

		spielplanTable = new SpielplanTable(this, false, new Listener(),
				new Listener(), new SpielplanSelectionListener());

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setBorder(BorderFactory.createEmptyBorder());
		splitPane.setContinuousLayout(true);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(200);

		editScroll.add(editPanel);

		splitPane.add(new JScrollPane(spielplanTable));
		splitPane.add(editScroll);

		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(splitPane, BorderLayout.CENTER);

		nullPanel.setVisible(false);

		// Spielplan sperren?
		// spielplanTable.setEnabled(false);
	}

	public void loadData(Tournament aTurnierBO) {
		this.turnierBO = aTurnierBO;
		dirty = false;

		try {

			// Daten laden
			Map<String, Object> result = TurnierRemote
					.getSpielplanTableEntrysGruppenByTurnierBO(aTurnierBO);

			Vector<SpielplanTableEntryBO> spielplanTableEntries = (Vector<SpielplanTableEntryBO>) result
					.get("tableEntries");
			spielplanTable.dataModel.setData(spielplanTableEntries);

			// Moegliche Mannschaften setzen
			gruppen = new Vector<SportGroup>((Set<SportGroup>) result.get("gruppen"));

			// M�gliche Spielplaetze setzen
			spielplaetze = TurnierRemote.getSpielplaetzeByTurnierid(aTurnierBO
					.getId());
			spielplanTable.setSpielplaetze(spielplaetze);

			spielplanTable.updateUI(); // f�r Reset!
			spielplanTable.clearSelection();

			// set lock/unlock
			if (turnierBO.getSpielplangesperrt()) {
				lockSchedule();
			} else {
				unlockSchedule();
			}

			dirty = false; // undo lockSchedule/unlockSchedule effect

			updateEditPanel();
		} catch (Exception e) {
			LOG.error("Can't read schedule from server.", e);
		}
	}

	public void save() {
		dirty = false;

		if (editPanel instanceof SpielEditPanel) {
			((SpielEditPanel) editPanel).save();
		}

		if (editPanel instanceof SpielBlockEditPanel) {
			((SpielBlockEditPanel) editPanel).save();
		}

		// Spiele aus TableEntry nach Vector umspeichern
		Vector<SportMatch> data = new Vector<SportMatch>();
		// alle Zeilen
		for (SpielplanTableEntryBO tableEntry : spielplanTable.dataModel
				.getData()) {
			// alle Spalten
			for (SportMatch spielBO : tableEntry.getSpiele()) {

				if (spielBO != null)
					data.add(spielBO);
			}
		}

		try {
			TurnierRemote.setSpieleByTurnierid(turnierBO.getId(), data
					.toArray());
		} catch (Exception e) {
			LOG.error("Can't save schedule to server.", e);
		}

		// Save Turnier -> for lock/unlock
		try {
			TurnierRemote.saveByTurnierBO(turnierBO, parentFrame.getUserBO());
		} catch (Exception e) {
			LOG.error("Can't save tournament to server.", e);
		}

		spielplanTable.clearSelection();
	}

	/**
	 * Aktuallisiert die Detail-Ansicht f�r Spiel, das gerade selektiert ist
	 */
	public void updateEditPanel() {
		int col = spielplanTable.getSelectedColumn();
		int row = spielplanTable.getSelectedRow();

		updateEditPanel(col, row);
	}

	/**
	 * Aktuallisiert die Detail-Ansicht f�r Spiel bei col,row
	 */
	public void updateEditPanel(int col, int row) {

		if (editPanel instanceof SpielEditPanel) {
			((SpielEditPanel) editPanel).save();
		}

		if (editPanel instanceof SpielBlockEditPanel) {
			((SpielBlockEditPanel) editPanel).save();
		}

		SportMatch spielBO = null;
		editPanel = nullPanel;

		if (row < spielplanTable.dataModel.getData().size() && (col > -1)
				&& (row > -1)) {
			SpielplanTableEntryBO tableEntry = (SpielplanTableEntryBO) spielplanTable.dataModel
					.getData().get(row);

			// SpielEdit
			if (col > 0) {
				spielBO = tableEntry.getSpiel((col - 1) / 2);
				spielEdit = new SpielEditPanel(this);
				spielEdit.setGruppen(gruppen);
				if (spielBO != spielEdit.spielBO && spielBO != null) {
					spielEdit.setSpielBO(spielBO);
				}
				editPanel = spielEdit;
			}

			// SpielBlockEdit
			if (col == 0) {
				spielBlockEdit = new SpielBlockEditPanel(this);
				editPanel = spielBlockEdit;

				if (tableEntry != spielBlockEdit.tableEntry) {
					spielBlockEdit.setTableEntry(tableEntry);
				}
			}

		}

		editScroll.setViewportView(editPanel);

		spielplanTable.repaint();
	}

	public void autoGen() {
		dirty = true;

		// if still editing -> stop it!
		if (spielplanTable.getCellEditor() != null) {
			spielplanTable.getCellEditor().stopCellEditing();
		}
		if (spielplanTable.getSelectedRow() > -1) {
			spielplanTable.changeSelection(spielplanTable.getSelectedRow(),
					spielplanTable.getSelectedColumn(), true, false);
		}

		updateEditPanel(-1, -1);

		ScheduleGen scheduleGen = new ScheduleGen(turnierBO, gruppen,
				spielplaetze);
		spielplanTable.dataModel.setData(scheduleGen.genSchedule());

		updateEditPanel();
		spielplanTable.updateUI();
	}

	public void lockSchedule() {
		dirty = true;

		turnierBO.setSpielplangesperrt(true);
		// if still editing -> stop it!
		if (spielplanTable.getCellEditor() != null) {
			spielplanTable.getCellEditor().stopCellEditing();
		}
		if (spielplanTable.getSelectedRow() > -1) {
			spielplanTable.changeSelection(spielplanTable.getSelectedRow(),
					spielplanTable.getSelectedColumn(), true, false);
		}

		spielplanTable.setEnabled(false);
		updateEditPanel(-1, -1);
		parentFrame.toolBar.setTurnierEnable(true, TreeNodeObject.SPIELPLAN,
				turnierBO);
	}

	public void unlockSchedule() {
		dirty = true;

		turnierBO.setSpielplangesperrt(false);
		spielplanTable.setEnabled(true);
		parentFrame.toolBar.setTurnierEnable(true, TreeNodeObject.SPIELPLAN,
				turnierBO);
	}

	public void delGame(int row, int col) {
		if (col > 0) {
			dirty = true;
			updateEditPanel(col, row);
			spielEdit.gruppe.setSelectedItem(null);
			updateEditPanel();
		}
	}

	class Listener implements ActionListener, KeyListener {
		public void actionPerformed(ActionEvent e) {
			JMenuItem source = (JMenuItem) (e.getSource());

			int row = spielplanTable.getSelectedRow();
			int col = spielplanTable.getSelectedColumn();

			if (source.getText() == spielplanTable.POPUP_NEWBLOCK) {
				addBlock(row);
			} else if (source.getText() == spielplanTable.POPUP_DELBLOCK) {
				deleteBlock(row);
			} else if (source.getText() == spielplanTable.POPUP_RENEW) {
				deleteSchedule();
			} else if (source.getText() == spielplanTable.POPUP_DELSPIEL) {
				delGame(row, col);
			} else if (source.getText() == spielplanTable.POPUP_AUTOGEN) {
				autoGen();
			} else if (source.getText() == spielplanTable.POPUP_LOCK) {
				lockSchedule();
			} else if (source.getText() == spielplanTable.POPUP_UNLOCK) {
				unlockSchedule();
			}
		}

		/**
		 * @param row
		 */
		private void addBlock(int row) {
			dirty = true;

			Date vondatum = null;
			// falls vorhanden -> selektierter Eintrag (oder letzer) + 5
			// min
			if (spielplanTable.dataModel.getData().size() > 0) {
				SpielplanTableEntryBO spielplanTableEntry = null;
				if (row >= 0) {
					spielplanTableEntry = (SpielplanTableEntryBO) spielplanTable.dataModel
							.getData().get(row);
				} else {
					spielplanTableEntry = (SpielplanTableEntryBO) spielplanTable.dataModel
							.getData().lastElement();
				}
				vondatum = spielplanTableEntry.getBisdatum();
				vondatum = new Date(vondatum.getTime() + 1000
						* turnierBO.getDurationBreak());

				spielplanTable.dataModel.moveTime(vondatum, 1000 * (turnierBO
						.getDurationBreak() + turnierBO.getDurationMatch()));
			} else { // erster Eintrag -> Startzeit des Turniers
				vondatum = turnierBO.getDate();
			}

			SpielplanTableEntryBO tableEntry = new SpielplanTableEntryBO(
					vondatum);
			tableEntry.setBisdatum(new Date(vondatum.getTime() + 1000
					* turnierBO.getDurationMatch()));

			// Spiele einf�gen
			for (int i = 0; i < spielplanTable.dataModel.getHeader().size(); i++)
				tableEntry.addSpiel(null);

			spielplanTable.dataModel.getData().add(tableEntry);
			spielplanTable.dataModel.sortColumn();

			spielplanTable.updateUI();
		}

		/**
		 * @param row
		 */
		private void deleteBlock(int row) {
			dirty = true;
			SpielplanTableEntryBO tableEntry = (SpielplanTableEntryBO) spielplanTable.dataModel
					.getData().get(row);
			for (int i = 0; i < tableEntry.getSpiele().size(); i++) {
				tableEntry.setSpiel(i, null);
			}
			spielplanTable.clearSelection();
			updateEditPanel();
			spielplanTable.dataModel.getData().remove(row);

			// Zeit der folgenden Bloecke verschieben (falls
			// spaetere Bloecke bestehen)
			if (spielplanTable.dataModel.getData().size() > row) {
				SpielplanTableEntryBO tableNextEntry = (SpielplanTableEntryBO) spielplanTable.dataModel
						.getData().get(row);

				long timediff = tableEntry.getVondatum().getTime()
						- tableNextEntry.getVondatum().getTime();
				spielplanTable.dataModel.moveTime(tableEntry.getVondatum(),
						timediff);
			}

			spielplanTable.updateUI();
			updateEditPanel();
		}

		/**
		 * 
		 */
		private void deleteSchedule() {
			dirty = true;

			// reloads all groups with teams!
			loadData(turnierBO);
			spielplanTable.dataModel
					.setData(new Vector<SpielplanTableEntryBO>());
			spielplanTable.clearSelection();
			spielplanTable.updateUI();
			updateEditPanel();
		}

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_DELETE) {
				delGame(spielplanTable.getSelectedRow(), spielplanTable
						.getSelectedColumn());
			}
		}

		public void keyTyped(KeyEvent e) {
		}

		public void keyReleased(KeyEvent e) {
		}

	}

	/**
	 * Wird ausgef�hrt, wenn Nutzer einen anderen Spiel-Eintrag aus der Tabelle
	 * w�hlt
	 * 
	 * @author ronny
	 * 
	 *         To change the template for this generated type comment go to
	 *         Window>Preferences>Java>Code Generation>Code and Comments
	 */
	class SpielplanSelectionListener implements ListSelectionListener,
			MouseListener {

		public void valueChanged(ListSelectionEvent e) {
		}

		public void mouseClicked(MouseEvent e) {
			updateEditPanel();
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}

	}

}
