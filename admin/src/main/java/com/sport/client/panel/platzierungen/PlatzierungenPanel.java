package com.sport.client.panel.platzierungen;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JScrollPane;


import org.apache.log4j.Logger;

import com.sport.client.VolleyFrame;
import com.sport.client.panel.VolleyPanel;
import com.sport.core.bo.Ranking;
import com.sport.core.bo.SportGroup;
import com.sport.core.bo.Tournament;
import com.sport.core.bo.comparators.GruppenComparator;
import com.sport.server.remote.TurnierRemote;

/**
 * @author ronny
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class PlatzierungenPanel extends VolleyPanel {

	private static final Logger LOG = Logger.getLogger(PlatzierungenPanel.class);

	private static final long serialVersionUID = 5706140707244297379L;

	private PlatzierungenTable platzierungenTable;

	public PlatzierungenPanel(VolleyFrame parentFrame) {
		super(parentFrame);

		platzierungenTable = new PlatzierungenTable(this, new Listener(),
				new Listener());

		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(new JScrollPane(platzierungenTable), BorderLayout.CENTER);
	}

	public void loadData(Tournament aTurnierBO) {
		this.turnierBO = aTurnierBO;
		dirty = false;

		try {
			// M�gliche Gruppen laden (f�r Mannschaft-Auswahlbox)
			Object[] gruppen = (Object[]) TurnierRemote
					.getGruppenMannschaftenSpieleByTurnierBO(aTurnierBO, true)
					.get("gruppen");
			Set<SportGroup> gruppenBO = new HashSet(Arrays.asList(gruppen));
			platzierungenTable.gruppen = new Vector<SportGroup>(gruppenBO);
			Collections.sort(platzierungenTable.gruppen,
					new GruppenComparator());

			// Platzierungen laden
			Set<Ranking> platzierungen = TurnierRemote
					.getPlatzierungenByTurnierBO(aTurnierBO);

			platzierungenTable.dataModel.setData(new Vector<Ranking>(
					platzierungen));

			platzierungenTable.updateUI(); // f�r Reset!
			platzierungenTable.clearSelection();
		} catch (Exception e) {
			LOG.error("Can't load groups from server", e);
		}
	}

	public void save() {
		dirty = false;

		// Wird noch eine Zelle bearbeitet -> Bearbeitung beenden
		if (platzierungenTable.getEditorComponent() != null) {
			platzierungenTable.getCellEditor().stopCellEditing();
		}

		try {
			TurnierRemote.setPlatzierungenByTurnierid(turnierBO.getId(),
					platzierungenTable.dataModel.getData().toArray());
		} catch (Exception e) {
			LOG.error("Can't save rankings to server", e);
		}
	}

	class Listener implements ActionListener, KeyListener {
		public void actionPerformed(ActionEvent e) {
			JMenuItem source = (JMenuItem) (e.getSource());
			if (source.getText() == platzierungenTable.POPUP_NEW) {
				addPlacing();
			} else if (source.getText() == platzierungenTable.POPUP_DEL) {
				delPlacing();
			}
		}

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_INSERT) {
				addPlacing();
			} else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
				delPlacing();
			}
		}

		public void keyTyped(KeyEvent e) {
		}

		public void keyReleased(KeyEvent e) {
		}

		private void delPlacing() {
			int row = platzierungenTable.getSelectedRow();
			// Zeile ausgew�hlt?
			if (row > -1 && row < platzierungenTable.dataModel.getData().size()) {
				dirty = true;

				Ranking oldPlatzierung = (Ranking) platzierungenTable.dataModel
						.getData().get(row);
				long oldPlatznr = oldPlatzierung.getRank();
				platzierungenTable.dataModel.getData().remove(row);

				// Gruppierung anpassen
				for (Ranking platzierungBO : platzierungenTable.dataModel
						.getData()) {
					if (platzierungBO.getRank() > oldPlatznr) {
						platzierungBO
								.setRank(platzierungBO.getRank() - 1);
					}
				}

				// L�cke in Platznr schliessen
				platzierungenTable.comboPlatznr
						.removeItemAt(platzierungenTable.comboPlatznr
								.getItemCount() - 1);

				platzierungenTable.updateUI();
			}
		}

		private void addPlacing() {
			dirty = true;

			int row = platzierungenTable.getSelectedRow() + 1;

			// Platzierung einf�gen
			Ranking platzierungBO = new Ranking();
			platzierungBO.setMannschaft(null);
			platzierungBO.setRank(row + 1);

			// Gruppierung anpassen
			for (Ranking platzierungBO2 : platzierungenTable.dataModel
					.getData()) {
				if (platzierungBO2.getRank() >= platzierungBO.getRank())
					platzierungBO2.setRank(platzierungBO2.getRank() + 1);
			}

			if (platzierungenTable.dataModel.getData().size() > 0) {
				platzierungenTable.dataModel.getData().add(row, platzierungBO);
			} else {
				platzierungenTable.dataModel.getData().add(platzierungBO);
			}

			// Gruppierungen erweitern
			platzierungenTable.comboPlatznr.addItem(new Long(
					platzierungenTable.dataModel.getData().size()));

			// select inserted row
			platzierungenTable.setRowSelectionInterval(row, row);

			platzierungenTable.updateUI();
		}
	}

}
