package com.sport.client.panel.spielplan;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


import org.apache.log4j.Logger;

import com.sport.client.VolleyFrame;
import com.sport.client.panel.VolleyPanel;
import com.sport.core.bo.Field;
import com.sport.core.bo.SpielplanTableEntryBO;
import com.sport.core.bo.SportMatch;
import com.sport.core.bo.Tournament;
import com.sport.server.remote.TurnierRemote;

/**
 * @author ronny
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ErgebnissePanel extends VolleyPanel {

	private static final Logger LOG = Logger.getLogger(ErgebnissePanel.class);

	private static final long serialVersionUID = -278743311325514535L;

	protected SpielplanTable spielplanTable;
	private SaetzeTable saetzeTable = null;
	private JScrollPane saetzeTableContainer = new JScrollPane();

	public ErgebnissePanel(VolleyFrame parentFrame) {
		super(parentFrame);

		spielplanTable = new SpielplanTable(this, true, null, new Listener(),
				new SpielplanSelectionListener());
		spielplanTable.setDragEnabled(false);

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setBorder(BorderFactory.createEmptyBorder());
		splitPane.setContinuousLayout(true);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(200);

		splitPane.add(new JScrollPane(spielplanTable));
		splitPane.add(saetzeTableContainer);

		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(splitPane, BorderLayout.CENTER);
	}

	public void loadData(Tournament aTurnierBO) {
		this.turnierBO = aTurnierBO;
		dirty = false;

		try {
			Vector<SpielplanTableEntryBO> spielplanTableEntries = TurnierRemote
					.getSpielplanTableEntrysByTurnierBO(aTurnierBO);
			spielplanTable.dataModel.setData(spielplanTableEntries);

			// M�gliche Spielplaetze setzen
			Vector<Field> spielplaetze = TurnierRemote
					.getSpielplaetzeByTurnierid(aTurnierBO.getId());
			spielplanTable.setSpielplaetze(spielplaetze);

			spielplanTable.updateUI(); // f�r Reset!
			spielplanTable.clearSelection();
			updateErgebnisEditPanel();
		} catch (Exception e) {
			LOG.error("Can't read results from server.", e);
		}
	}

	public void save() {
		dirty = false;

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
			LOG.error("Can't save results to server.", e);
		}
	}

	/**
	 * Aktuallisiert die Detail-Ansicht f�r Spiel
	 * 
	 */
	public void updateErgebnisEditPanel() {
		int row = spielplanTable.getSelectedRow();
		int col = spielplanTable.getSelectedColumn();

		// ggf. Reste speichern
		if (saetzeTable != null) {
			if (saetzeTable.getEditingRow() > -1) {
				saetzeTable.getCellEditor(saetzeTable.getEditingRow(),
						saetzeTable.getEditingColumn()).stopCellEditing();
			}
			saetzeTableContainer.setViewportView(new JPanel());
			spielplanTable.repaint();
		}

		saetzeTable = null;
		SportMatch spielBO = null;

		if (col > -1
				&& (row < spielplanTable.dataModel.getData().size() && (col > 0))) {
			SpielplanTableEntryBO tableEntry = (SpielplanTableEntryBO) spielplanTable.dataModel
					.getData().get(row);

			spielBO = tableEntry.getSpiel((col - 1) / 2);
			if (spielBO != null) {
				saetzeTable = new SaetzeTable(this, spielBO);
				saetzeTableContainer.setViewportView(saetzeTable);
			}
		}

	}

	class Listener implements KeyListener {
		// Select row in the saetzeTable
		private boolean selectLastRowSaetze() {
			// for matches -> jump to sets
			if (spielplanTable.getSelectedColumn() > 0) {
				int rowCount = saetzeTable.getRowCount();
				if (rowCount > 0) {
					saetzeTable.requestFocusInWindow();
					saetzeTable.changeSelection(rowCount - 1,
							SaetzeTableModel.COL_PUNKTE1, false, false);
					return true;
				}
			}
			return false;
		}

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_INSERT) {
				saetzeTable.addSatz();
				selectLastRowSaetze();
			} else if (e.getKeyCode() == KeyEvent.VK_TAB) {
				if (selectLastRowSaetze()) {
					// reverse the effect of the implicit tab
					int row = spielplanTable.getSelectedRow();
					int col = spielplanTable.getSelectedColumn();
					spielplanTable.changeSelection(row, col - 1, false, false);
				}
			}
		}

		public void keyTyped(KeyEvent e) {
		}

		public void keyReleased(KeyEvent e) {
			updateErgebnisEditPanel();
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
			updateErgebnisEditPanel();
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
