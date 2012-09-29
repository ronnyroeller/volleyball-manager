/*
 * Created on 15.06.2003
 * 
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.sport.client.export;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.sport.client.ExampleFileFilter;
import com.sport.client.VolleyFrame;
import com.sport.core.bo.SportGroup;
import com.sport.core.bo.Tournament;
import com.sport.core.bo.comparators.GruppenComparator;
import com.sport.core.helper.Messages;
import com.sport.server.remote.TurnierRemote;

/**
 * @author ronny
 * 
 */
public class ExportDialog extends JDialog implements ActionListener {

	private static final Logger LOG = Logger.getLogger(ExportDialog.class);

	private static final long serialVersionUID = -4426698358716038855L;

	private JTextField pfadText = new JTextField();
	private JButton buttonOkay = new JButton(Messages
			.getString("exportdialog_export")); //$NON-NLS-1$
	private JButton buttonCancel = new JButton(Messages
			.getString("exportdialog_abort")); //$NON-NLS-1$

	private VolleyFrame parent = null;
	private Vector<GruppeCheck> gruppenCheck = new Vector<GruppeCheck>();
	boolean result = false; // true, falls Daten uebernommen werden sollen

	private String bezeichnung; // was soll exportiert werden -> fuer Views

	public ExportDialog(VolleyFrame parent, Tournament turnierBO,
			String bezeichnung) throws HeadlessException {
		super(parent);

		this.bezeichnung = bezeichnung;
		this.parent = parent;

		Vector<SportGroup> gruppenBO = new Vector<SportGroup>();

		// Gruppen laden
		try {

			// M�gliche Gruppen laden
			Object[] gruppenArray = (Object[]) TurnierRemote
					.getGruppenMannschaftenSpieleByTurnierBO(turnierBO, true)
					.get("gruppen");
			gruppenBO = new Vector(Arrays.asList(gruppenArray)); //$NON-NLS-1$
			Collections.sort(gruppenBO, new GruppenComparator());
		} catch (Exception e) {
			LOG.error("Can't fetch groups from server", e);
		}

		this.getContentPane().setLayout(null);
		JLabel jLabel = new JLabel(Messages.getString("exportdialog_path")); //$NON-NLS-1$
		jLabel.setBounds(new Rectangle(13, 15, 68, 15));
		getContentPane().add(jLabel, null);
		jLabel = new JLabel(Messages.getString("exportdialog_groups")); //$NON-NLS-1$
		jLabel.setBounds(new Rectangle(13, 40, 73, 15));
		getContentPane().add(jLabel, null);

		pfadText.setBounds(new Rectangle(70, 12, 366, 21));
		if (parent.lastChoosenDir != null) {
			pfadText.setText(parent.lastChoosenDir.getAbsolutePath());
		}
		pfadText.addKeyListener(new Listener());
		JButton selButton = new JButton();
		selButton.setBounds(438, 12, 21, 21);
		selButton.setIcon(new ImageIcon(VolleyFrame.class
				.getResource("/resources/icons/opens.png"))); //$NON-NLS-1$
		selButton.addActionListener(new SelActionListener());

		JPanel checkPanel = new JPanel();
		checkPanel
				.setPreferredSize(new Dimension(360, (gruppenBO.size()) * 23));
		checkPanel.setLayout(null);

		// f�r jede Gruppe eine Checkbox einf�gen
		for (SportGroup gruppeBO : gruppenBO) {
			JCheckBox check = new JCheckBox(gruppeBO.getName());
			check.setBounds(new Rectangle(2, 2 + 23 * (gruppenCheck.size()),
					360, 21));
			
			// Don't pre-select groups
			check.setSelected(false);
			checkPanel.add(check);

			GruppeCheck gruppeCheck = new GruppeCheck(check, gruppeBO);
			gruppenCheck.add(gruppeCheck);
		}

		buttonOkay.setBounds(new Rectangle(121, 223, 101, 25));
		buttonCancel.setBounds(new Rectangle(250, 223, 101, 25));
		buttonOkay.addActionListener(this);
		buttonCancel.addActionListener(this);

		JScrollPane scrollPanel = new JScrollPane(checkPanel);
		scrollPanel.setBounds(70, 37, 390, 175);

		getContentPane().add(pfadText, null);
		getContentPane().add(selButton, null);
		getContentPane().add(scrollPanel, null);
		getContentPane().add(buttonOkay, null);
		getContentPane().add(buttonCancel, null);

		setTitle(this.bezeichnung
				+ " - " + Messages.getString("exportdialog_export_to_pdf")); //$NON-NLS-1$
		setSize(478, 284);
		setResizable(false);
		Dimension dlgSize = getSize();
		Dimension frmSize = parent.getSize();
		Point loc = parent.getLocation();
		setLocation((frmSize.width - dlgSize.width) / 2 + loc.x,
				(frmSize.height - dlgSize.height) / 2 + loc.y);
	}

	/**
	 * gibt ausgew�hlten Pfad zur�ck
	 */
	public String getPfad() {
		return pfadText.getText();
	}

	/**
	 * gibt ausgew�hlte Gruppen-Ids zur�ck
	 */
	public Vector<Long> getSelectedGruppeIds() {
		Vector<Long> thisResult = new Vector<Long>();

		// alle Checkboxen pr�fen
		for (GruppeCheck gruppeCheck : gruppenCheck) {
			JCheckBox check = gruppeCheck.getCheckBox(); //$NON-NLS-1$
			if (check.isSelected()) {
				SportGroup gruppeBO = gruppeCheck.getGruppeBO(); //$NON-NLS-1$
				thisResult.add(new Long(gruppeBO.getId()));
			}
		}

		return thisResult;
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
		// if successful -> keep last choosen dir
		if (result) {
			parent.setLastChoosenDir(new File(getPfad()).getParentFile());
		}
		dispose();
	}

	/**
	 * @return
	 */
	public boolean getResult() {
		return result;
	}

	class SelActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			ExampleFileFilter filter = new ExampleFileFilter();
			filter.addExtension("pdf"); //$NON-NLS-1$
			filter.setDescription(Messages.getString("exportdialog_pdf_file")); //$NON-NLS-1$
			fc.setFileFilter(filter);
			fc.setApproveButtonText(Messages.getString("exportdialog_save")); //$NON-NLS-1$
			fc.setDialogTitle(bezeichnung
					+ Messages.getString("exportdialog_export_to_pdf")); //$NON-NLS-1$
			fc.setCurrentDirectory(new File(pfadText.getText()));
			int thisResult = fc.showSaveDialog(parent);
			if (thisResult == JFileChooser.APPROVE_OPTION) {
				parent.setLastChoosenDir(fc.getCurrentDirectory());
				String filepath = fc.getSelectedFile().getAbsolutePath();

				// falls keine erweiterung -> auto. ergaenzen
				if (!fc.getSelectedFile().getName().matches(".*\\..*")) { //$NON-NLS-1$
					filepath += ".pdf"; //$NON-NLS-1$
				}

				pfadText.setText(filepath);
			}
		}
	}

	/**
	 * allows to access the dialog also via shortcuts
	 * 
	 * @author ronny
	 */
	class Listener implements KeyListener {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				cancel();
			} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				result = true;
				cancel();
			}
		}

		public void keyTyped(KeyEvent e) {
		}

		public void keyReleased(KeyEvent e) {
		}
	}

}