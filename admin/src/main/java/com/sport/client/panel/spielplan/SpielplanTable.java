/*
 * Created on 09.06.2003
 * 
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.sport.client.panel.spielplan;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.EventListener;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JToolTip;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;

import com.sport.analyzer.MannschaftAnalyzer;
import com.sport.analyzer.SpielAnalyzer;
import com.sport.analyzer.SpielAnalyzer.DetailedMatchResult;
import com.sport.analyzer.impl.MannschaftAnalyzerImpl;
import com.sport.analyzer.impl.SpielAnalyzerImpl;
import com.sport.client.Icons;
import com.sport.client.MultiLineToolTip;
import com.sport.client.panel.VolleyPanel;
import com.sport.core.bo.Field;
import com.sport.core.bo.SpielplanTableEntryBO;
import com.sport.core.bo.SportMatch;
import com.sport.core.bo.comparators.SpielplatzComparator;
import com.sport.core.helper.Messages;


/**
 * @author ronny
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SpielplanTable extends JTable {

	private static final long serialVersionUID = 3553910897180556005L;

	private JPopupMenu popupMenu = new JPopupMenu();

	public VolleyPanel parent;
	public SpieleTableModel dataModel;

	private boolean relNames = false;

	// resolvierte Namen anzeigen oder logische?

	final String POPUP_NEWBLOCK = Messages
			.getString("spielplantable_new_block"); //$NON-NLS-1$

	final String POPUP_DELBLOCK = Messages
			.getString("spielplantable_del_block"); //$NON-NLS-1$

	final String POPUP_DELSPIEL = Messages.getString("spielplantable_del_game"); //$NON-NLS-1$

	final String POPUP_AUTOGEN = Messages.getString("spielplantable_autogen"); //$NON-NLS-1$

	final String POPUP_RENEW = Messages.getString("spielplantable_renew"); //$NON-NLS-1$

	final String POPUP_LOCK = Messages.getString("spielplantable_lock"); //$NON-NLS-1$

	final String POPUP_UNLOCK = Messages.getString("spielplantable_unlock"); //$NON-NLS-1$

	final String TABLE_COLVONDATUM = Messages.getString("spielplantable_time"); //$NON-NLS-1$

	private final MannschaftAnalyzer mannschaftAnalyzer = MannschaftAnalyzerImpl
			.getInstance();

	public SpielplanTable(VolleyPanel parent, boolean relNames,
			ActionListener popupListener, KeyListener keyListener,
			EventListener selectionListener) {
		super();

		this.parent = parent;
		this.relNames = relNames;

		dataModel = new SpieleTableModel(this,
				new Vector<SpielplanTableEntryBO>(), new Vector<Field>());
		setModel(dataModel);

		setTransferHandler(new SpielplanTransferHandler());
		setDragEnabled(true);

		getSelectionModel().addListSelectionListener(
				(ListSelectionListener) selectionListener);
		addMouseListener((MouseListener) selectionListener);

		// nur PopUp-Menu erstellen, wenn auch Listener vorhanden ist
		if (popupListener != null) {
			JMenuItem menuItem = new JMenuItem(POPUP_NEWBLOCK);
			menuItem.addActionListener(popupListener);
			popupMenu.add(menuItem);
			menuItem = new JMenuItem(POPUP_DELBLOCK);
			menuItem.addActionListener(popupListener);
			popupMenu.add(menuItem);
			menuItem = new JMenuItem(POPUP_DELSPIEL);
			menuItem.addActionListener(popupListener);
			popupMenu.add(menuItem);
			menuItem = new JMenuItem(POPUP_RENEW);
			menuItem.addActionListener(popupListener);
			popupMenu.add(menuItem);
			menuItem = new JMenuItem(POPUP_AUTOGEN, Icons.AUTO_SMALL);
			menuItem.addActionListener(popupListener);
			popupMenu.add(menuItem);
			menuItem = new JMenuItem(POPUP_LOCK, Icons.LOCK_SMALL);
			menuItem.addActionListener(popupListener);
			popupMenu.add(menuItem);
			menuItem = new JMenuItem(POPUP_UNLOCK, Icons.UNLOCK_SMALL);
			menuItem.addActionListener(popupListener);
			popupMenu.add(menuItem);

			addMouseListener(new PopupListener());
			getTableHeader().addMouseListener(new PopupListener());
		}

		if (keyListener != null) {
			addKeyListener(keyListener);
		}

		setCellSelectionEnabled(true);
		setColumnSelectionAllowed(false);
		// setRowSelectionAllowed(false);
		getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);

		getColumn(TABLE_COLVONDATUM).setMaxWidth(60);

		// Show colors by rendering them in their own color.
		setDefaultRenderer(Object.class, new ColorTableCellRender());

		// Immer ganze Zeilen fokusieren
		addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent focusEvent) {
				int row = getSelectionModel().getAnchorSelectionIndex();
				if ((row > -1) && (row < dataModel.getRowCount())) {
					setRowSelectionInterval(row, row);
				} else {
					clearSelection();
				}
			}
		});
	}

	public void setSpielplaetze(Vector<Field> spielplaetze) {
		Collections.sort(spielplaetze, new SpielplatzComparator());
		dataModel.setHeader(spielplaetze);
	}

	public class SpielplanTransferHandler extends TransferHandler {

		private static final long serialVersionUID = 5066294956492616016L;

		int row; // Position, von wo drag gestartet wurde
		int col;

		public int getSourceActions(JComponent c) {
			return TransferHandler.MOVE;
		}

		// does an old game exist on this field?
		public boolean canImport(JComponent comp, DataFlavor transferFlavors[]) {
			return true;
		}

		public Transferable createTransferable(JComponent comp) {
			row = getSelectedRow();
			col = getSelectedColumn();

			// Datum nicht verschieben
			if (col < 1) {
				return null;
			}

			SpielplanTableEntryBO tableEntry = (SpielplanTableEntryBO) dataModel
					.getData().get(row);
			SportMatch spielBO = tableEntry.getSpiel((col - 1) / 2);

			// nur vorhandene Spiele verschieben
			if (spielBO == null) {
				return null;
			}

			return new SpielTransferable(spielBO);
		}

		public boolean importData(JComponent c, Transferable t) {
			if (t != null) {
				if (t.isDataFlavorSupported(SpielTransferable.spielFlavor)) {
					int br = getSelectedRow();
					int bc = getSelectedColumn();

					// nicht in Zeit-Spalte ziehen und nicht in gleiche
					// Zelle kopieren
					if (bc > 0
							&& (((bc - 1) / 2) != ((col - 1) / 2) || br != row)) {
						// Spiel, das bewegt wurde
						SportMatch spielBO = ((SpielplanTableEntryBO) ((SpielplanTable) c).dataModel
								.getData().get(row)).getSpiel((col - 1) / 2);

						// Block, auf den gezogen wurde
						SpielplanTableEntryBO newSpielplanTableEntry = (SpielplanTableEntryBO) ((SpielplanTable) c).dataModel
								.getData().get(br);

						// old game
						SportMatch oldSpielBO = newSpielplanTableEntry
								.getSpiel((bc - 1) / 2);

						// if old game exist -> delete it proberly
						if (oldSpielBO != null) {
							((SpielplanPanel) parent).delGame(br, bc);
						}

						spielBO.setField(dataModel
								.getSpielplatz((bc - 1) / 2));
						spielBO.setStartDate(newSpielplanTableEntry
								.getVondatum());
						spielBO.setEndDate(newSpielplanTableEntry
								.getBisdatum());

						newSpielplanTableEntry.setSpiel((bc - 1) / 2, spielBO);

						((SpielplanPanel) parent).editPanel = ((SpielplanPanel) parent).nullPanel;
						((SpielplanPanel) parent).updateEditPanel();

						updateUI();
						return true;
					}
				}
			}
			return false;
		}

		public void exportDone(JComponent source, Transferable data, int action) {
			// Drop erfolgreich?
			if (data != null) {
				SpielplanTableEntryBO tableEntry = (SpielplanTableEntryBO) dataModel
						.getData().get(row);
				tableEntry.setSpiel((col - 1) / 2, null);
			}
		}

	}

	class PopupListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			showPopup(e);
		}

		public void mouseReleased(MouseEvent e) {
			showPopup(e);
		}

		private void showPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {
				// Lock or Unlock?
				if (!parent.turnierBO.getSpielplangesperrt()) {
					popupMenu.getComponent(0).setEnabled(true);
					popupMenu.getComponent(2).setEnabled(false);

					int row = getSelectedRow();
					int col = getSelectedColumn();

					// nur wenn Block markiert ist, darf gel�scht werden
					if (e.getComponent() instanceof JTableHeader) {
						popupMenu.getComponent(1).setEnabled(false);
					} else {

						// nur l�schen, wenn Block ausgew�hlt ist
						if (row > -1) {
							popupMenu.getComponent(1).setEnabled(true);

							// ist Spiel ausgew�hlt?
							if (col > 0) {
								SpielplanTableEntryBO tableEntry = (SpielplanTableEntryBO) dataModel
										.getData().get(row);
								SportMatch spielBO = tableEntry
										.getSpiel((col - 1) / 2);
								if (spielBO != null) { // l�schen erlauben
									popupMenu.getComponent(2).setEnabled(true);
								}
							}
						} else {
							popupMenu.getComponent(1).setEnabled(false);
						}
					}

					popupMenu.getComponent(3).setEnabled(true);
					popupMenu.getComponent(4).setVisible(true);
					popupMenu.getComponent(5).setVisible(false);
				} else {
					popupMenu.getComponent(0).setEnabled(false);
					popupMenu.getComponent(1).setEnabled(false);
					popupMenu.getComponent(2).setEnabled(false);
					popupMenu.getComponent(3).setEnabled(false);
					popupMenu.getComponent(4).setVisible(false);
					popupMenu.getComponent(5).setVisible(true);
				}

				popupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}

	public JToolTip createToolTip() {
		MultiLineToolTip tip = new MultiLineToolTip();
		tip.setComponent(this);
		return tip;
	}

	public String getToolTipText(MouseEvent event) {
		int row = rowAtPoint(event.getPoint());
		int col = columnAtPoint(event.getPoint());

		// Zeit-Spalte
		if (col == SpieleTableModel.COLVONDATUM) {
			Date date = ((SpielplanTableEntryBO) dataModel.getData().get(row))
					.getVondatum();
			return DateFormat.getDateTimeInstance(DateFormat.FULL,
					DateFormat.SHORT).format(date);
		}

		// Tooltip auch auf Farbzellen
		if (col % 2 == 1) {
			col++;
		}

		SportMatch spielBO = ((SpielplanTableEntryBO) dataModel.getData().get(row))
				.getSpiel((col / 2) - 1);
		String paarung = Messages.getString("spielplantable_no_game"); //$NON-NLS-1$
		if (spielBO != null) {
			String ergebnisse = ""; //$NON-NLS-1$
			DetailedMatchResult resultSpiel = SpielAnalyzerImpl.getInstance()
					.getErgebnisDetails(spielBO);
			if (resultSpiel.winner != SpielAnalyzer.MatchResult.NICHTGESPIELT) {
				ergebnisse = " (" //$NON-NLS-1$
						+ resultSpiel.saetze1 //$NON-NLS-1$
						+ ":" //$NON-NLS-1$
						+ resultSpiel.saetze2 //$NON-NLS-1$
						+ ")"; //$NON-NLS-1$
			}

			if (isRelNames()) {
				paarung = mannschaftAnalyzer.getName(spielBO.getTeam1())
						+ " - " //$NON-NLS-1$
						+ mannschaftAnalyzer.getName(spielBO.getTeam2())
						+ ergebnisse;
			} else {
				paarung = mannschaftAnalyzer.getRelName(spielBO
						.getTeam1())
						+ " - " //$NON-NLS-1$
						+ mannschaftAnalyzer.getRelName(spielBO
								.getTeam2()) + ergebnisse;
			}

			paarung += "\n" + spielBO.getGroup().getName(); //$NON-NLS-1$
		}

		return (row + 1)
				+ Messages.getString("spielplantable_block") //$NON-NLS-1$
				+ dataModel.getSpielplatz((col / 2) - 1).getName()
				+ "\n" + paarung; //$NON-NLS-1$
	}

	/**
	 * @return
	 */
	public boolean isRelNames() {
		return relNames;
	}

}
