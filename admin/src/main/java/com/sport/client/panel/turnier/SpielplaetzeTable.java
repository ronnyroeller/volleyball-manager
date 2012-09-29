/*
 * Created on 09.06.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.sport.client.panel.turnier;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.JTableHeader;

import com.sport.client.panel.VolleyPanel;
import com.sport.client.panel.gruppen.OverwriteCellEditor;
import com.sport.core.bo.Field;
import com.sport.core.helper.Messages;


/**
 * @author ronny
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SpielplaetzeTable extends JTable {

	private static final long serialVersionUID = 58390089016784259L;

	private JPopupMenu popupMenu = new JPopupMenu();
	public VolleyPanel parent;

	protected SpielplatzTableModel dataModel = null;

	private final String POPUP_NEW = Messages.getString("spielplaetzetable_new_field"); //$NON-NLS-1$
	private final String POPUP_DEL = Messages.getString("spielplaetzetable_del_field"); //$NON-NLS-1$

	protected final String TABLE_COLNAME = Messages.getString("spielplaetzetable_name"); //$NON-NLS-1$

	public SpielplaetzeTable(VolleyPanel parent) {
		super();

		this.parent = parent;

		dataModel = new SpielplatzTableModel(this, new Vector<Field>());
		setModel(dataModel);

		JMenuItem menuItem = new JMenuItem(POPUP_NEW);
		menuItem.addActionListener(new PopupListenerAction());
		popupMenu.add(menuItem);
		menuItem = new JMenuItem(POPUP_DEL);
		menuItem.addActionListener(new PopupListenerAction());
		popupMenu.add(menuItem);
		addMouseListener(new PopupListener());
		addKeyListener(new PopupListenerAction());
		getTableHeader().addMouseListener(new PopupListener());

		setCellSelectionEnabled(true);
		setColumnSelectionAllowed(false);
		getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		getTableHeader().setReorderingAllowed(false);

		getColumn(TABLE_COLNAME).setCellEditor(new OverwriteCellEditor());

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
				// nur wenn Spielplatz markiert ist, darf gel�scht werden
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

	class PopupListenerAction implements ActionListener, KeyListener {
		public void actionPerformed(ActionEvent e) {
			JMenuItem source = (JMenuItem) (e.getSource());
			if (source.getText() == POPUP_NEW) {
				addField();
			} else if (source.getText() == POPUP_DEL) {
				delField();
			}
		}

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_INSERT) {
				addField();
			} else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
				delField();
			}
		}

		public void keyTyped(KeyEvent e) {
		}

		public void keyReleased(KeyEvent e) {
		}

		/**
		 * Deletes a field from the table
		 */
		private void delField() {
			parent.dirty = true;

			int row = getSelectedRow();
			// Zeile ausgwe�hlt?
			if (row > -1 && row < dataModel.getData().size()) {
				dataModel.getData().remove(row);
				updateUI();
			}
		}

		/**
		 * Adds a field to the table
		 */
		private void addField() {
			parent.dirty = true;

			// Spielplatz einf�gen
			Field spielplatzBO = new Field();

			DecimalFormat decimalFormat = new DecimalFormat();
			decimalFormat.applyPattern("00");
			String number = decimalFormat
					.format(dataModel.getData().size() + 1);

			spielplatzBO
					.setName(Messages.getString("spielplaetzetable_field") + " " + number); //$NON-NLS-1$

			dataModel.getData().add(spielplatzBO);

			// select inserted row
			setRowSelectionInterval(dataModel.getData().size() - 1, dataModel
					.getData().size() - 1);

			updateUI();
		}
	}

}
