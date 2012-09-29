/*
 * Created on 09.06.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.sport.client.panel.gruppen;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
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
import javax.swing.JToolTip;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import com.sport.analyzer.GroupResult;
import com.sport.analyzer.GruppeErgebnisEntity;
import com.sport.analyzer.impl.GruppeAnalyzerImpl;
import com.sport.analyzer.impl.MannschaftAnalyzerImpl;
import com.sport.client.MultiLineToolTip;
import com.sport.client.panel.VolleyPanel;
import com.sport.core.bo.SportGroup;
import com.sport.core.bo.TournamentSystem;
import com.sport.core.helper.Messages;


/**
 * @author ronny
 * 
 */
public class GruppenTable extends JTable {

	private static final long serialVersionUID = 8630177752331881969L;

	private JPopupMenu popupMenu = new JPopupMenu();
	public VolleyPanel parent;

	protected JComboBox comboModus = new JComboBox();
	protected JComboBox comboSort = new JComboBox();
	protected GruppeTableModel dataModel;

	final String POPUP_NEWGROUP = Messages.getString("gruppentable_new_group"); //$NON-NLS-1$
	final String POPUP_DELGROUP = Messages.getString("gruppentable_del_group"); //$NON-NLS-1$

	final String TABLE_COLGRUPPENFARBE = " "; //$NON-NLS-1$
	final String TABLE_COLSORT = Messages.getString("gruppentable_number"); //$NON-NLS-1$
	final String TABLE_COLNAME = Messages.getString("gruppentable_name"); //$NON-NLS-1$
	final String TABLE_COLFARBE = Messages.getString("gruppentable_color"); //$NON-NLS-1$
	final String TABLE_COLMODUS = Messages.getString("gruppentable_modus"); //$NON-NLS-1$

	@SuppressWarnings("serial")
	public GruppenTable(VolleyPanel parent, ActionListener popupListener,
			KeyListener keyListener, ListSelectionListener selectionListener) {
		super();

		this.parent = parent;

		dataModel = new GruppeTableModel(this, new Vector<SportGroup>());
		setModel(dataModel);

		JMenuItem menuItem = new JMenuItem(POPUP_NEWGROUP);
		menuItem.addActionListener(popupListener);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0));
		popupMenu.add(menuItem);
		menuItem = new JMenuItem(POPUP_DELGROUP);
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
		getSelectionModel().addListSelectionListener(selectionListener);

		getColumn(TABLE_COLMODUS).setCellEditor(
				new DefaultCellEditor(comboModus) {
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
						return ((MouseEvent) evt).getClickCount() >= 2;
					}
				});
		getColumn(TABLE_COLNAME).setCellEditor(new OverwriteCellEditor());

		getColumn(TABLE_COLGRUPPENFARBE).setCellEditor(
				new FarbeCellEditor(this));
		getColumn(TABLE_COLFARBE).setCellEditor(new FarbeCellEditor(this));

		getColumn(TABLE_COLGRUPPENFARBE).setMaxWidth(10);
		getColumn(TABLE_COLSORT).setMaxWidth(40);

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

				if (convertColumnIndexToModel(column) == GruppeTableModel.COL_GRUPPENFARBE) {
					SportGroup gruppeBO = (SportGroup) dataModel.getData().get(row);
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

	public JToolTip createToolTip() {
		MultiLineToolTip tip = new MultiLineToolTip();
		tip
				.setFont(new Font(
						"Monospaced", Font.PLAIN, tip.getFont().getSize())); //$NON-NLS-1$
		tip.setComponent(this);
		return tip;
	}

	/**
	 * bringt den String str zur Laengte len und f�llt ggf. mit Zeichen fill auf
	 * 
	 * @param str
	 * @param len
	 * @return
	 */
	private String makeLen(String str, int len, boolean right) {
		if (str.length() >= len) {
			return str.substring(0, len);
		}
		for (int i = str.length(); i < len; i++) {
			if (right) {
				str = ' ' + str;
			} else {
				str += ' ';
			}
		}

		return str;
	}

	private String makeLen(int i, int len) {
		return makeLen(String.valueOf(i), len, true);
	}

	public String getToolTipText(MouseEvent event) {
		int row = rowAtPoint(event.getPoint());
		String result = null;

		SportGroup gruppeBO = (SportGroup) dataModel.getData().get(row);
		if (gruppeBO != null) {
			result = Messages.getString("gruppentable_groupplacement") //$NON-NLS-1$
					+ "\n" + Messages.getString("gruppentable_popup_title") //$NON-NLS-1$
					+ "\n" + Messages.getString("gruppentable_popup_title2") //$NON-NLS-1$
					+ "\n";

			GroupResult gruppeResult = GruppeAnalyzerImpl.getInstance()
					.getErgebnisDetails(gruppeBO);
			for (GruppeErgebnisEntity entity : gruppeResult.ergebnisDetails) {
				result += makeLen(MannschaftAnalyzerImpl.getInstance().getRelName(
						entity.getMannschaftBO()), 20, false)
						+ "|" //$NON-NLS-1$
						+ makeLen(entity.getSpiele(), 3) + "|"; //$NON-NLS-1$;
				// fuer KO-Systeme keine Statistik anzeigen (verwirrt!)
				// if (!gruppeBO.getModusBO().getKoSystem())
				{
					result += makeLen(entity.getPspiele(), 3) + "|" //$NON-NLS-1$
							+ makeLen(entity.getNspiele(), 3) + "|" //$NON-NLS-1$
							+ makeLen(entity.getPsaetze(), 3) + "|" //$NON-NLS-1$
							+ makeLen(entity.getNsaetze(), 3) + "|" //$NON-NLS-1$
							+ makeLen(entity.getPpunkte(), 3) + "|" //$NON-NLS-1$
							+ makeLen(entity.getNpunkte(), 3) + "|" //$NON-NLS-1$
							+ makeLen(entity.getDiffpunkte(), 3); //$NON-NLS-1$
				}
				result += "\n";
			}
		}

		return result;
	}

	/**
	 * F�llt Modus-Combobox mit allen m�glichen Gruppen
	 * 
	 * @param modus
	 */
	public void setModi(Vector<TournamentSystem> modi) {
		comboModus.removeAllItems();
		Collections.sort(modi, new ModusComparator());
		for (TournamentSystem modusBO : modi)
			comboModus.addItem(modusBO);
	}

	class HeaderSortListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			int column = getColumnModel().getColumnIndexAtX(e.getX());
			if (column != -1 && e.getButton() == MouseEvent.BUTTON1) {
				dataModel.sortColumn(column);
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
				popupMenu.getComponent(0).setEnabled(true);
				// nur wenn Gruppe markiert ist, darf gel�scht werden
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

	/*
	 * public void changeSelection( int rowIndex, int columnIndex, boolean
	 * toggle, boolean extend) { super.changeSelection(rowIndex, columnIndex,
	 * toggle, extend); updateUI(); }
	 */
}
