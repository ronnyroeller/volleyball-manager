/*
 * Created on 09.06.2003
 * 
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.sport.client.panel.platzierungen;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import com.sport.analyzer.MannschaftAnalyzer;
import com.sport.analyzer.impl.MannschaftAnalyzerImpl;
import com.sport.client.panel.VolleyPanel;
import com.sport.core.bo.Ranking;
import com.sport.core.bo.SportGroup;
import com.sport.core.helper.Messages;


/**
 * @author ronny
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class PlatzierungenTable extends JTable {

	private static final long serialVersionUID = -4704240756017557699L;

	private JPopupMenu popupMenu = new JPopupMenu();
	public VolleyPanel parent;
	// alle Gruppen -> fuer Auswahlbox
	protected Vector<SportGroup> gruppen = new Vector<SportGroup>();

	protected JComboBox comboPlatznr = new JComboBox();
	protected PlatzierungenTableModel dataModel;

	protected final String POPUP_NEW = Messages
			.getString("platzierungentable_new_placement"); //$NON-NLS-1$
	protected final String POPUP_DEL = Messages
			.getString("platzierungentable_del_placement"); //$NON-NLS-1$

	protected final String TABLE_COLGRUPPENFARBE = " "; //$NON-NLS-1$
	protected final String TABLE_COLPLATZNR = Messages
			.getString("platzierungentable_place"); //$NON-NLS-1$
	protected final String TABLE_COLMANNSCHAFT = Messages
			.getString("platzierungentable_team"); //$NON-NLS-1$

	private final MannschaftAnalyzer mannschaftAnalyzer = MannschaftAnalyzerImpl
			.getInstance();

	public PlatzierungenTable(VolleyPanel parent, ActionListener popupListener,
			KeyListener keyListener) {
		super();

		this.parent = parent;

		dataModel = new PlatzierungenTableModel(this);
		setModel(dataModel);

		JMenuItem menuItem = new JMenuItem(POPUP_NEW);
		menuItem.addActionListener(popupListener);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0));
		popupMenu.add(menuItem);
		menuItem = new JMenuItem(POPUP_DEL);
		menuItem.addActionListener(popupListener);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		popupMenu.add(menuItem);

		setCellSelectionEnabled(true);
		setColumnSelectionAllowed(false);
		getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		getTableHeader().addMouseListener(new PopupListener());
		addMouseListener(new PopupListener());
		addKeyListener(keyListener);
		getTableHeader().setReorderingAllowed(false);

		getColumn(TABLE_COLPLATZNR).setCellEditor(
				new DefaultCellEditor(comboPlatznr));
		getColumn(TABLE_COLMANNSCHAFT).setCellEditor(
				new LogischCellEditor(this));
		getColumn(TABLE_COLGRUPPENFARBE).setMaxWidth(10);
		getColumn(TABLE_COLPLATZNR).setMaxWidth(50);

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

		// Show colors by rendering them in their own color.
		DefaultTableCellRenderer colorRenderer = new DefaultTableCellRenderer() {

			private static final long serialVersionUID = -3027912647356763522L;

			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {

				super.getTableCellRendererComponent(table, value, isSelected,
						hasFocus, row, column);

				if (isSelected) {
					return this;
				}

				Ranking platzierungBO = (Ranking) dataModel
						.getData().get(row);
				if ((column == PlatzierungenTableModel.COL_GRUPPENFARBE)
						&& (platzierungBO.getMannschaft() != null)) {
					SportGroup gruppeBO = null;

					if (platzierungBO.getMannschaft().getLogspielBO() != null) {
						gruppeBO = platzierungBO.getMannschaft()
								.getLogspielBO().getGroup();
					} else {
						gruppeBO = platzierungBO.getMannschaft()
								.getLoggruppeBO();
					}

					String color = gruppeBO.getColor();

					// Zerlegen der HTML-Farbangabe
					int[] part = new int[3];
					part[0] = Integer.parseInt(color.substring(1, 3), 16);
					part[1] = Integer.parseInt(color.substring(3, 5), 16);
					part[2] = Integer.parseInt(color.substring(5, 7), 16);

					setBackground(new Color(part[0], part[1], part[2]));
				} else {
					setBackground(Color.white);
				}

				return this;
			}
		};

		setDefaultRenderer(Object.class, colorRenderer);

	}

	public String getToolTipText(MouseEvent event) {
		int row = rowAtPoint(event.getPoint());
		int col = columnAtPoint(event.getPoint());

		// Namen-Spalte
		if (col == PlatzierungenTableModel.COL_MANNSCHAFT) {
			Ranking platzierungBO = (Ranking) dataModel.getData()
					.get(row);
			if (platzierungBO.getMannschaft() == null) {
				return null;
			}
			return mannschaftAnalyzer.getRelNameTrace(platzierungBO
					.getMannschaft());
		}

		return null;
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
				// nur wenn Platzierung markiert ist, darf gel�scht werden
				if (e.getComponent() instanceof JTableHeader) {
					popupMenu.getComponent(1).setEnabled(false);
				} else {
					// nur l�schen, wenn Platzierung ausgew�hlt ist
					popupMenu.getComponent(1).setEnabled(getSelectedRow() > -1);
				}

				popupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}

}
