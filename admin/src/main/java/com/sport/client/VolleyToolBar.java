/*
 * Created on 01.12.2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package com.sport.client;

import java.awt.Dimension;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JToolBar;

import com.sport.client.panel.spielplan.SpielplanPanel;
import com.sport.core.bo.Tournament;
import com.sport.core.helper.Messages;


/**
 * @author ronny
 * 
 *         To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Generation - Code and Comments
 */
public class VolleyToolBar extends JToolBar {

	private static final long serialVersionUID = -1985467295000506436L;

	private VolleyFrame parentFrame;

	private JButton buttonNew = null;
	private JButton buttonImport = null;
	private JButton buttonExport = null;
	private JButton buttonPdf = null;
	private JButton buttonOnline = null;
	private JButton buttonBeamer = null;
	private JButton buttonScheduleAuto = null;
	private JButton buttonScheduleLock = null;
	private JButton buttonScheduleUnlock = null;

	private PopupMenu pdfPopup = null;
	private MenuItem jMenuFilePdfSpielplan = new MenuItem(Messages
			.getString("volleyframe_pdf_schedule")); //$NON-NLS-1$
	private MenuItem jMenuFilePdfSchiedsrichter = new MenuItem(Messages
			.getString("volleyframe_pdf_referee")); //$NON-NLS-1$
	private MenuItem jMenuFilePdfSpielbericht = new MenuItem(Messages
			.getString("volleyframe_pdf_spielbericht")); //$NON-NLS-1$
	private MenuItem jMenuFilePdfGruppen = new MenuItem(Messages
			.getString("volleyframe_pdf_gruppen")); //$NON-NLS-1$

	public VolleyToolBar(VolleyFrame parentFrame) {
		this.parentFrame = parentFrame;

		buttonNew = new JButton(Icons.NEW);
		buttonImport = new JButton(Icons.OPEN);
		buttonExport = new JButton(Icons.SAVE);
		buttonPdf = new JButton(Icons.PDF);
		buttonOnline = new JButton(Icons.ONLINE);
		buttonBeamer = new JButton(Icons.BEAMER);
		buttonScheduleAuto = new JButton(Icons.AUTO);
		buttonScheduleLock = new JButton(Icons.LOCK);
		buttonScheduleUnlock = new JButton(Icons.UNLOCK);

		buttonNew.setToolTipText(Messages
				.getString("volleyframe_new_challenge")); //$NON-NLS-1$
		buttonImport.setToolTipText(Messages
				.getString("volleyframe_import_challenge")); //$NON-NLS-1$
		buttonExport.setToolTipText(Messages
				.getString("volleyframe_export_challenge")); //$NON-NLS-1$
		buttonPdf.setToolTipText(Messages
				.getString("volleyframe_pdf_challenge")); //$NON-NLS-1$
		buttonOnline.setToolTipText(Messages.getString("volleyframe_online")); //$NON-NLS-1$
		buttonBeamer.setToolTipText(Messages.getString("volleyframe_beamer")); //$NON-NLS-1$
		buttonScheduleAuto.setToolTipText(Messages
				.getString("spielplantable_autogen")); //$NON-NLS-1$
		buttonScheduleLock.setToolTipText(Messages
				.getString("spielplantable_lock")); //$NON-NLS-1$
		buttonScheduleUnlock.setToolTipText(Messages
				.getString("spielplantable_unlock")); //$NON-NLS-1$

		buttonNew.addActionListener(new ButtonActionListener());
		buttonImport.addActionListener(new ButtonActionListener());
		buttonExport.addActionListener(new ButtonActionListener());
		buttonPdf.addActionListener(new ButtonActionListener());
		buttonOnline.addActionListener(new ButtonActionListener());
		buttonBeamer.addActionListener(new ButtonActionListener());
		buttonScheduleAuto.addActionListener(new ButtonActionListener());
		buttonScheduleLock.addActionListener(new ButtonActionListener());
		buttonScheduleUnlock.addActionListener(new ButtonActionListener());

		// Submenu for PDF Generation
		pdfPopup = new PopupMenu();

		pdfPopup.add(jMenuFilePdfSpielplan);
		pdfPopup.add(jMenuFilePdfSchiedsrichter);
		pdfPopup.add(jMenuFilePdfSpielbericht);
		pdfPopup.add(jMenuFilePdfGruppen);
		jMenuFilePdfSpielplan.addActionListener(new PopupActionListener());
		jMenuFilePdfSchiedsrichter.addActionListener(new PopupActionListener());
		jMenuFilePdfSpielbericht.addActionListener(new PopupActionListener());
		jMenuFilePdfGruppen.addActionListener(new PopupActionListener());
		buttonPdf.add(pdfPopup);
		buttonPdf.addMouseListener(new PopupListener());

		add(buttonNew);
		add(buttonImport);
		add(buttonExport);
		Separator sep = new Separator();
		sep.setSeparatorSize(new Dimension(20, 0));
		add(sep);
		add(buttonPdf);
		add(buttonOnline);
		add(buttonBeamer);
		sep = new Separator();
		sep.setSeparatorSize(new Dimension(20, 0));
		add(sep);
		add(buttonScheduleAuto);
		add(buttonScheduleLock);
		add(buttonScheduleUnlock);
	}

	/**
	 * Activates/Deactivates all menuelements depending if a tournament is
	 * selected.
	 * 
	 * @param enable
	 */
	public void setTurnierEnable(boolean enable, int treeNodeType,
			Tournament turnierBO) {
		buttonExport.setEnabled(enable);
		buttonPdf.setEnabled(enable);
		buttonOnline.setEnabled(enable);
		buttonBeamer.setEnabled(enable);
		buttonScheduleLock.setEnabled(enable);
		buttonScheduleUnlock.setEnabled(enable);

		// if Turnier is enable -> check if Schedule -> show specific buttons
		if (treeNodeType == TreeNodeObject.SPIELPLAN) {
			buttonScheduleAuto.setVisible(true);
			if (turnierBO.getSpielplangesperrt()) {
				buttonScheduleAuto.setEnabled(false);
				buttonScheduleLock.setVisible(false);
				buttonScheduleUnlock.setVisible(true);
			} else {
				buttonScheduleAuto.setEnabled(true);
				buttonScheduleLock.setVisible(true);
				buttonScheduleUnlock.setVisible(false);
			}
		} else {
			buttonScheduleAuto.setVisible(false);
			buttonScheduleLock.setVisible(false);
			buttonScheduleUnlock.setVisible(false);
		}
	}

	class ButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == buttonNew) {
				parentFrame.newTurnier();
			} else if (e.getSource() == buttonImport) {
				parentFrame.openTurnier();
			} else if (e.getSource() == buttonExport) {
				parentFrame.saveTurnier();
			} else if (e.getSource() == buttonOnline) {
				parentFrame.showOnline();
			} else if (e.getSource() == buttonBeamer) {
				parentFrame.showBeamer();
			} else if (e.getSource() == buttonScheduleAuto) {
				((SpielplanPanel) parentFrame.aktPanel).autoGen();
			} else if (e.getSource() == buttonScheduleLock) {
				((SpielplanPanel) parentFrame.aktPanel).lockSchedule();
			} else if (e.getSource() == buttonScheduleUnlock) {
				((SpielplanPanel) parentFrame.aktPanel).unlockSchedule();
			}
		}
	}

	class PopupListener extends MouseAdapter {
		public void mouseReleased(MouseEvent e) {
			showPopup(e);
		}

		private void showPopup(MouseEvent e) {
			if (buttonPdf.isEnabled()) {
				pdfPopup.show(e.getComponent(), 0,
						e.getComponent().getSize().height);
			}
		}

	}

	class PopupActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == jMenuFilePdfSpielplan) {
				parentFrame.exportPdfSpielplan();
			} else if (e.getSource() == jMenuFilePdfSchiedsrichter) {
				parentFrame.exportPdfSchiedsrichter();
			} else if (e.getSource() == jMenuFilePdfSpielbericht) {
				parentFrame.exportPdfSpielbericht();
			} else if (e.getSource() == jMenuFilePdfGruppen) {
				parentFrame.exportPdfGruppen();
			}
		}
	}

}
