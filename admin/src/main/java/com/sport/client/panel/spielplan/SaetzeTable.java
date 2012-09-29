/*
 * Created on 09.06.2003
 * 
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.sport.client.panel.spielplan;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import com.sport.client.panel.VolleyPanel;
import com.sport.client.panel.gruppen.OverwriteCellEditor;
import com.sport.core.bo.SetResult;
import com.sport.core.bo.SportMatch;
import com.sport.core.helper.Messages;


/**
 * @author ronny
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SaetzeTable extends JTable {

	private static final long serialVersionUID = 845946517983783102L;

	private JPopupMenu popupMenu = new JPopupMenu();
	protected VolleyPanel parent;

	private SaetzeTableModel dataModel = null;

	private final String POPUP_NEW = Messages.getString("saetzetable_new_set"); //$NON-NLS-1$
	private final String POPUP_DEL = Messages.getString("saetzetable_del_set"); //$NON-NLS-1$

	protected final String TABLE_COLWINNERFARBE = " "; //$NON-NLS-1$
	protected final String TABLE_COLSATZNR = Messages
			.getString("saetzetable_set"); //$NON-NLS-1$
	protected final String TABLE_COLPUNKTE1 = Messages
			.getString("saetzetable_first_team"); //$NON-NLS-1$
	protected final String TABLE_COLPUNKTE2 = Messages
			.getString("saetzetable_second_team"); //$NON-NLS-1$

	public SaetzeTable(VolleyPanel parent, SportMatch spielBO) {
		super();

		this.parent = parent;

		dataModel = new SaetzeTableModel(this);
		setModel(dataModel);

		JMenuItem menuItem = new JMenuItem(POPUP_NEW);
		menuItem.addActionListener(new PopupListenerAction());
		popupMenu.add(menuItem);
		menuItem = new JMenuItem(POPUP_DEL);
		menuItem.addActionListener(new PopupListenerAction());
		popupMenu.add(menuItem);
		addMouseListener(new PopupListener());
		getTableHeader().addMouseListener(new PopupListener());

		getColumn(TABLE_COLPUNKTE1).setCellEditor(new OverwriteCellEditor());
		getColumn(TABLE_COLPUNKTE2).setCellEditor(new OverwriteCellEditor());

		getColumn(TABLE_COLWINNERFARBE).setMaxWidth(10);
		getColumn(TABLE_COLSATZNR).setMaxWidth(40);
		getColumn(TABLE_COLPUNKTE1).setIdentifier(TABLE_COLPUNKTE1);
		getColumn(TABLE_COLPUNKTE2).setIdentifier(TABLE_COLPUNKTE2);

		/*
		 * getColumn(TABLE_COLPUNKTE1).setCellEditor( new DefaultCellEditor(new
		 * JFormattedTextField()));
		 */

		setCellSelectionEnabled(true);
		setColumnSelectionAllowed(false);
		getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		getTableHeader().setReorderingAllowed(false);

		dataModel.setSpiel(spielBO);

		// Immer ganfze Zeilen fokusieren
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

		// Immer ganze Zeilen fokusieren
		addKeyListener(new Listener());
		// Show colors by rendering them in their own color.
		DefaultTableCellRenderer colorRenderer = new DefaultTableCellRenderer() {
			private static final long serialVersionUID = -3426981042708312800L;

			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {

				super.getTableCellRendererComponent(table, value, isSelected,
						hasFocus, row, column);

				if (isSelected) {
					return this;
				}

				// farbl. gestalten, wer Satz gewinnt
				if (column == 0) {
					SetResult satzBO = (SetResult) dataModel.getData().get(row);

					Color color = Color.LIGHT_GRAY;
					if (satzBO.getPoints1() > satzBO.getPoints2()) {
						color = Color.GREEN;
					} else if (satzBO.getPoints1() < satzBO.getPoints2()) {
						color = Color.RED;
					}

					setBackground(color);
				} else {
					setBackground(Color.white);
				}
				return this;
			}
		};

		setDefaultRenderer(Object.class, colorRenderer);
	}

	class Listener implements KeyListener {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_TAB) {
				// jump back to the spielplan table
				((ErgebnissePanel) parent).spielplanTable.requestFocus();
			} else if (e.getKeyCode() == KeyEvent.VK_INSERT) {
				addSatz();
			} else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
				delSatz();
			} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				addSatz();
			}

		}

		public void keyTyped(KeyEvent e) {
		}

		public void keyReleased(KeyEvent e) {
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
				popupMenu.getComponent(0).setEnabled(true);
				// nur wenn Ergebniszeile markiert ist, darf gel�scht werden
				if (e.getComponent() instanceof JTableHeader
						|| getSelectedRow() == -1) {
					popupMenu.getComponent(1).setEnabled(false);
				} else {
					popupMenu.getComponent(1).setEnabled(true);
				}

				popupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}

	class PopupListenerAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JMenuItem source = (JMenuItem) (e.getSource());
			if (source.getText() == POPUP_NEW) {
				addSatz();
			} else if (source.getText() == POPUP_DEL) {
				delSatz();
			}
		}
	}

	public void addSatz() {
		parent.dirty = true;

		int row = getRowCount();

		// Spielplatz einf�gen
		SetResult satzBO = new SetResult();
		satzBO.setSetNr(row + 1);
		satzBO.setPoints1(0);
		satzBO.setPoints2(0);

		// Gruppierung anpassen
		for (SetResult satzBO2 : dataModel.getData()) {
			if (satzBO2.getSetNr() >= satzBO.getSetNr()) {
				satzBO2.setSetNr(satzBO2.getSetNr() + 1);
			}
		}

		if (dataModel.getData().size() > 0) {
			dataModel.getData().add(row, satzBO);
		} else {
			dataModel.getData().add(satzBO);
		}

		changeSelection(row - 1, SaetzeTableModel.COL_PUNKTE1, false, false);

		updateUI();
	}

	private void delSatz() {
		parent.dirty = true;

		int row = getSelectedRow();
		// Zeile ausgwe�hlt?
		if (row > -1 && row < dataModel.getData().size()) {
			int oldSatznr = ((SetResult) dataModel.getData().get(row)).getSetNr();

			dataModel.getData().remove(row);
			updateUI();

			// Gruppierung anpassen
			for (SetResult satzBO : dataModel.getData()) {
				if (satzBO.getSetNr() > oldSatznr) {
					satzBO.setSetNr(satzBO.getSetNr() - 1);
				}
			}
			/*
			 * // if still a row exists -> select the one before if
			 * (dataModel.getData().size() > 0) { changeSelection(row,
			 * SaetzeTableModel.COL_PUNKTE1, false, false); }
			 */
		}
	}

}

