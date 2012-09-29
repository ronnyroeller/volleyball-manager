/*
 * Created on 15.06.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.sport.client.panel.gruppen;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.util.Collections;
import java.util.Vector;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;

import com.sport.analyzer.impl.MannschaftAnalyzerImpl;
import com.sport.core.bo.SportGroup;
import com.sport.core.bo.Team;
import com.sport.core.bo.comparators.MannschaftenBaseComparator;
import com.sport.core.helper.Messages;


/**
 * @author ronny
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class LogischMannschaftDialog extends JDialog implements ItemListener,
		ActionListener {

	private static final long serialVersionUID = -2795449213972120951L;

	private Team mannschaftBO = null;

	private JComboBox comboGruppe = new JComboBox();
	public JComboBox comboMannschaft = new JComboBox();
	private JButton buttonOkay = new JButton(Messages
			.getString("logischmannschaftdialog_accept")); //$NON-NLS-1$
	private JButton buttonCancel = new JButton(Messages
			.getString("logischmannschaftdialog_abort")); //$NON-NLS-1$

	boolean result = false; // true, falls Daten uebernommen werden sollen

	public LogischMannschaftDialog(JFrame parent, Team mannschaftBO,
			Vector<SportGroup> gruppen) throws HeadlessException {
		super(parent);

		this.mannschaftBO = mannschaftBO;

		this.getContentPane().setLayout(null);
		JLabel jLabel = new JLabel(Messages
				.getString("logischmannschaftdialog_group")); //$NON-NLS-1$
		jLabel.setBounds(new Rectangle(13, 15, 68, 15));
		getContentPane().add(jLabel, null);

		jLabel = new JLabel(Messages.getString("logischmannschaftdialog_team")); //$NON-NLS-1$
		jLabel.setBounds(new Rectangle(13, 40, 73, 15));
		getContentPane().add(jLabel, null);

		comboMannschaft.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 179986293409397140L;

			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean hasFocus) {
				setText(MannschaftAnalyzerImpl.getInstance().getBaseName(
						(Team) value));
				return this;
			}

		});

		comboGruppe.setBounds(new Rectangle(87, 12, 170, 21));
		comboMannschaft.setBounds(new Rectangle(87, 37, 170, 21));
		buttonOkay.setBounds(new Rectangle(21, 69, 101, 25));
		buttonCancel.setBounds(new Rectangle(150, 69, 101, 25));
		buttonOkay.addActionListener(this);
		buttonCancel.addActionListener(this);

		getContentPane().add(comboGruppe, null);
		getContentPane().add(comboMannschaft, null);
		getContentPane().add(buttonOkay, null);
		getContentPane().add(buttonCancel, null);

		comboGruppe.addItemListener(this);

		setTitle(Messages.getString("logischmannschaftdialog_select_team")); //$NON-NLS-1$
		setSize(275, 140);
		setResizable(false);
		Dimension dlgSize = getSize();
		Dimension frmSize = parent.getSize();
		Point loc = parent.getLocation();
		setLocation((frmSize.width - dlgSize.width) / 2 + loc.x,
				(frmSize.height - dlgSize.height) / 2 + loc.y);

		// Daten einladen
		for (SportGroup gruppeBO : gruppen) {
			// nur Gruppen mit Mannschaften
			if (gruppeBO.getMannschaften().size() > 0) {
				comboGruppe.addItem(gruppeBO);
			}
		}

		/*
		 * if (mannschaftBO.getLogspielBO() != null) { GruppeBO gruppeBO =
		 * mannschaftBO.getLogspielBO().getGruppeBO();
		 * comboGruppe.setSelectedItem(gruppeBO);
		 */

		// ggf. Mannschaft vorselectieren
		if (mannschaftBO != null && mannschaftBO.getLoggruppeBO() != null) {
			comboGruppe.setSelectedItem(mannschaftBO.getLoggruppeBO());
		} else { // ansonsten jeweils erstes Element
			comboGruppe.setSelectedIndex(0);
		}
	}

	public void itemStateChanged(ItemEvent e) {
		SportGroup gruppeBO = (SportGroup) comboGruppe.getSelectedItem();
		if (e.getStateChange() == ItemEvent.SELECTED
				&& e.getSource() == comboGruppe) {
			comboMannschaft.removeAllItems();

			// -> keine log. Mannschaften, die sich auf Spiele beziehen!
			// -> Vector mannschaften = ModusMgr.getMannschaften(gruppeBO,
			// null);
			Vector<Team> mannschaften = (Vector<Team>) gruppeBO
					.getMannschaften().clone();
			if (mannschaften != null) {
				Collections
						.sort(mannschaften, new MannschaftenBaseComparator());
				for (Team mannschaftBO2 : mannschaften) {
					comboMannschaft.addItem(mannschaftBO2);

					// ggf. selectieren
					if (mannschaftBO != null) {

						// LogGruppe
						if (mannschaftBO.getLoggruppeBO() != null
								&& mannschaftBO.getLoggruppeBO().equals(
										gruppeBO)
								&& mannschaftBO.getLogsort() == mannschaftBO2
										.getSort()) {
							comboMannschaft.setSelectedItem(mannschaftBO2);
						}

						// LogSpiel
						if (mannschaftBO.getLogspielBO() != null
								&& mannschaftBO.getLogspielBO().equals(
										mannschaftBO2.getLogspielBO())
								&& mannschaftBO.getLogsort() == mannschaftBO2
										.getLogsort()) {
							comboMannschaft.setSelectedItem(mannschaftBO2);
						}
					}
				}
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JButton) {
			result = (e.getSource() == buttonOkay);
			cancel();
		}
	}

	// �berschrieben, so dass eine Beendigung beim Schlie�en des Fensters
	// m�glich ist.
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			cancel();
		}
		super.processWindowEvent(e);
	}

	// Dialog schlie�en
	void cancel() {
		dispose();
	}

	/**
	 * @return
	 */
	public boolean getResult() {
		return result;
	}

}
