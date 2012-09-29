/*
 * Created on 09.06.2003
 * 
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.sport.client.panel.mannschaften;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.EventObject;
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
import com.sport.client.panel.gruppen.OverwriteCellEditor;
import com.sport.core.bo.SportGroup;
import com.sport.core.bo.Team;
import com.sport.core.bo.comparators.GruppenComparator;
import com.sport.core.helper.Messages;


/**
 * @author ronny
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class MannschaftenTable extends JTable {

	private static final long serialVersionUID = -5725298137131640681L;

	private JPopupMenu popupMenu = new JPopupMenu();
	public VolleyPanel parent;

	public JComboBox comboGruppe = new JComboBox();
	public JComboBox comboSort = new JComboBox();
	public MannschaftenTableModel dataModel;

	public final String POPUP_LOGTEAM = Messages
			.getString("mannschaftentable_log_team"); //$NON-NLS-1$
	public final String POPUP_NEWTEAM = Messages
			.getString("mannschaftentable_new_team"); //$NON-NLS-1$
	public final String POPUP_DELTEAM = Messages
			.getString("mannschaftentable_del_team"); //$NON-NLS-1$

	protected final String TABLE_COLGRUPPENFARBE = " "; //$NON-NLS-1$
	protected final String TABLE_COLSORT = Messages
			.getString("mannschaftentable_placeingroup"); //$NON-NLS-1$
	public final String TABLE_COLNAME = Messages
			.getString("mannschaftentable_name"); //$NON-NLS-1$
	public final String TABLE_COLGRUPPE = Messages
			.getString("mannschaftentable_group"); //$NON-NLS-1$

	private final MannschaftAnalyzer mannschaftAnalyzer = MannschaftAnalyzerImpl
			.getInstance();

	@SuppressWarnings("serial")
	public MannschaftenTable(VolleyPanel parent, ActionListener popupListener,
			KeyListener keyListener) {
		super();

		this.parent = parent;

		dataModel = new MannschaftenTableModel(this, new Vector<Team>());
		setModel(dataModel);

		JMenuItem menuItem = new JMenuItem(POPUP_LOGTEAM);
		menuItem.addActionListener(popupListener);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,
				KeyEvent.ALT_DOWN_MASK));
		popupMenu.add(menuItem);
		menuItem = new JMenuItem(POPUP_NEWTEAM);
		menuItem.addActionListener(popupListener);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0));
		popupMenu.add(menuItem);
		menuItem = new JMenuItem(POPUP_DELTEAM);
		menuItem.addActionListener(popupListener);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		popupMenu.add(menuItem);

		setCellSelectionEnabled(true);
		setColumnSelectionAllowed(false);
		getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		getTableHeader().addMouseListener(new HeaderSortListener());
		getTableHeader().addMouseListener(new PopupListener());
		addMouseListener(new PopupListener());
		addKeyListener(keyListener);

		// Messages.getString("mannschaftentable_nogroup")

		getColumn(TABLE_COLGRUPPE).setCellRenderer(
				new DefaultTableCellRenderer() {
					public Component getTableCellRendererComponent(
							JTable table, Object value, boolean isSelected,
							boolean hasFocus, int row, int column) {

						super.getTableCellRendererComponent(table, value,
								isSelected, hasFocus, row, column);

						Team mannschaftBO = (Team) dataModel
								.getData().get(row);

						if (mannschaftBO.getGruppeBO() == null) {
							this.setText(Messages
									.getString("mannschaftentable_nogroup"));
						}
						return this;
					}

				});

		getColumn(TABLE_COLGRUPPE).setCellEditor(
				new DefaultCellEditor(comboGruppe) {
					public boolean isCellEditable(EventObject evt) {
						if (evt != null) {
							return ((MouseEvent) evt).getClickCount() >= 2;
						}
						return false;
					}
				});
		getColumn(TABLE_COLSORT).setCellEditor(
				new DefaultCellEditor(comboSort) {
					public boolean isCellEditable(EventObject evt) {
						if (evt != null) {
							return ((MouseEvent) evt).getClickCount() >= 2;
						}
						return false;
					}
				});
		getColumn(TABLE_COLNAME).setCellEditor(new OverwriteCellEditor());
		getColumn(TABLE_COLGRUPPENFARBE).setMaxWidth(10);
		getColumn(TABLE_COLSORT).setMaxWidth(200);

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
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {

				super.getTableCellRendererComponent(table, value, isSelected,
						hasFocus, row, column);

				if (isSelected) {
					return this;
				}

				Team mannschaftBO = (Team) dataModel.getData()
						.get(row);
				if (convertColumnIndexToModel(column) == MannschaftenTableModel.COL_GRUPPENFARBE
						&& mannschaftBO.getGruppeBO() != null) {
					String color = mannschaftBO.getGruppeBO().getColor();

					// Zerlegen der HTML-Farbangabe
					int[] part = new int[3];
					part[0] = Integer.parseInt(color.substring(1, 3), 16);
					part[1] = Integer.parseInt(color.substring(3, 5), 16);
					part[2] = Integer.parseInt(color.substring(5, 7), 16);

					setBackground(new Color(part[0], part[1], part[2]));
				} else if (getRealColumnnr(column) == MannschaftenTableModel.COL_NAME) {

					// log. Mannschaften grau hinterlegen
					if (mannschaftBO.getLoggruppeBO() != null
							|| mannschaftBO.getLogspielBO() != null) {
						setBackground(Color.lightGray);
					}
					// virtuelle Mannschaften hellgrau hinterlegen
					else if (mannschaftBO.isVirtual()) {
						setBackground(new Color(230, 230, 230));
					} else {
						setBackground(Color.white);
					}
				} else {
					setBackground(Color.white);
				}

				return this;
			}
		};

		setDefaultRenderer(Object.class, colorRenderer);

	}

	/**
	 * F�llt Gruppen-Combobox mit allen m�glichen Gruppen
	 * 
	 * @param gruppen
	 */
	public void setGruppen(Vector<SportGroup> gruppen) {
		comboGruppe.removeAllItems();
		comboGruppe.addItem(Messages.getString("mannschaftentable_nogroup"));
		Collections.sort(gruppen, new GruppenComparator());
		for (SportGroup gruppeBO : gruppen)
			comboGruppe.addItem(gruppeBO);
	}

	// auf Schriftzug �berpr�fen, falls Spalten ausgeblendet sind
	public int getRealColumnnr(int column) {
		int columnnr = MannschaftenTableModel.COL_NAME;
		String columnname = getColumnName(column);
		if (columnname == TABLE_COLGRUPPE) {
			columnnr = MannschaftenTableModel.COL_GRUPPE;
		} else if (columnname == TABLE_COLSORT) {
			columnnr = MannschaftenTableModel.COL_SORT;
		}

		return columnnr;
	}

	public String getToolTipText(MouseEvent event) {
		int row = rowAtPoint(event.getPoint());
		int col = columnAtPoint(event.getPoint());

		// Namen-Spalte
		if (getRealColumnnr(col) == MannschaftenTableModel.COL_NAME) {
			Team mannschaftBO = (Team) dataModel.getData().get(
					row);
			return mannschaftAnalyzer.getRelName(mannschaftBO);
		}

		return null;
	}

	class HeaderSortListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			int column = getColumnModel().getColumnIndexAtX(e.getX());
			if (column != -1 && e.getButton() == MouseEvent.BUTTON1) {
				dataModel.sortColumn(getRealColumnnr(column));
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
				popupMenu.getComponent(1).setEnabled(true);
				// nur wenn Mannschaft markiert ist, darf gel�scht werden
				if (e.getComponent() instanceof JTableHeader) {
					// Mannschaften k�nnen nur angelegt werden, wenn eine
					// Gruppe vorhanden ist!
					if (comboGruppe.getItemCount() == 0) {
						popupMenu.getComponent(1).setEnabled(false);
					}
					popupMenu.getComponent(0).setEnabled(false);
					popupMenu.getComponent(2).setEnabled(false);
				} else {
					// nur l�schen, wenn Mannschaft ausgew�hlt ist
					popupMenu.getComponent(2).setEnabled(getSelectedRow() > -1);
					popupMenu.getComponent(0).setEnabled(getSelectedRow() > -1);
				}

				popupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}

}
