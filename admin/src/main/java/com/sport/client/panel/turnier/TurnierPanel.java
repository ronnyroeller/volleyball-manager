/*
 * Created on 03.06.2003
 * 
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.sport.client.panel.turnier;

import java.awt.Rectangle;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;


import org.apache.log4j.Logger;

import com.sport.client.VolleyFrame;
import com.sport.client.panel.EditListener;
import com.sport.client.panel.VolleyPanel;
import com.sport.core.bo.Field;
import com.sport.core.bo.Tournament;
import com.sport.core.helper.Licence;
import com.sport.core.helper.Messages;
import com.sport.server.ejb.HomeGetter;
import com.sport.server.remote.TurnierRemote;

/**
 * Panel for general tournament configurations
 * 
 * @author ronny
 */
public class TurnierPanel extends VolleyPanel {

	private static final Logger LOG = Logger.getLogger(TurnierPanel.class);

	private static final long serialVersionUID = 516090944702007717L;

	private JTextField turniername = new JTextField();
	private JSpinner datumSpinner = null;
	private JTextArea text = new JTextArea();
	private JTextField linkid = new JTextField();
	private JSpinner beamerumschaltzeit = null;
	private JSpinner punkteprosatz = null;
	private JSpinner punkteprospiel = null;
	private JSpinner punkteprounentschiedenspiel = null;
	private JSpinner spieldauer = null;
	private JSpinner pausendauer = null;
	private JTextField bannerlink = new JTextField();
	private SpielplaetzeTable spielplaetzeTable;

	public TurnierPanel(VolleyFrame parentFrame) {
		super(parentFrame);

		// Datum
		SpinnerDateModel model = new SpinnerDateModel();
		model.setCalendarField(Calendar.WEEK_OF_MONTH);
		datumSpinner = new JSpinner(model);
		// Use date pattern that fits the current selected Locale
		String datePattern = ((SimpleDateFormat) SimpleDateFormat.getInstance()).toPattern();
		JSpinner.DateEditor editor = new JSpinner.DateEditor(datumSpinner, datePattern); //$NON-NLS-1$
		datumSpinner.setEditor(editor);

		// Spinner fuer PunktePro...
		SpinnerNumberModel nummodel = new SpinnerNumberModel(1, 1, 50, 1);
		punkteprosatz = new JSpinner(nummodel);
		JSpinner.NumberEditor numeditor =
			new JSpinner.NumberEditor(punkteprosatz);
		punkteprosatz.setEditor(numeditor);

		nummodel = new SpinnerNumberModel(1, 1, 300, 1);
		beamerumschaltzeit = new JSpinner(nummodel);
		numeditor = new JSpinner.NumberEditor(beamerumschaltzeit);
		beamerumschaltzeit.setEditor(numeditor);

		nummodel = new SpinnerNumberModel(1, 1, 50, 1);
		punkteprospiel = new JSpinner(nummodel);
		numeditor = new JSpinner.NumberEditor(punkteprospiel);
		punkteprospiel.setEditor(numeditor);

		nummodel = new SpinnerNumberModel(1, 1, 50, 1);
		punkteprounentschiedenspiel = new JSpinner(nummodel);
		numeditor = new JSpinner.NumberEditor(punkteprounentschiedenspiel);
		punkteprounentschiedenspiel.setEditor(numeditor);

		nummodel = new SpinnerNumberModel(1, 1, 60, 1);
		spieldauer = new JSpinner(nummodel);
		numeditor = new JSpinner.NumberEditor(spieldauer);
		spieldauer.setEditor(numeditor);

		nummodel = new SpinnerNumberModel(1, 1, 60, 1);
		pausendauer = new JSpinner(nummodel);
		numeditor = new JSpinner.NumberEditor(pausendauer);
		pausendauer.setEditor(numeditor);

		JLabel jLabel1 = new JLabel(Messages.getString("turnierpanel_name_of_challenge")); //$NON-NLS-1$
		jLabel1.setBounds(new Rectangle(6, 11, 105, 15));
		JLabel jLabel2 = new JLabel(Messages.getString("turnierpanel_date")); //$NON-NLS-1$
		jLabel2.setBounds(new Rectangle(6, 36, 105, 15));
		JLabel jLabel3 = new JLabel(Messages.getString("turnierpanel_text")); //$NON-NLS-1$
		jLabel3.setBounds(new Rectangle(6, 61, 105, 15));
		JLabel jLabel4 = new JLabel(Messages.getString("turnierpanel_linkid")); //$NON-NLS-1$
		jLabel4.setBounds(new Rectangle(6, 127, 105, 15));
		JLabel jLabel12 = new JLabel(Messages.getString("turnierpanel_bannerlink")); //$NON-NLS-1$
		jLabel12.setBounds(new Rectangle(6, 152, 105, 15));
		JLabel jLabel9 = new JLabel(Messages.getString("turnierpanel_beamerchangetime")); //$NON-NLS-1$
		jLabel9.setBounds(new Rectangle(6, 177, 105, 15));
		JLabel jLabel5 = new JLabel(Messages.getString("turnierpanel_points_per_set")); //$NON-NLS-1$
		jLabel5.setBounds(new Rectangle(6, 201, 105, 15));
		JLabel jLabel6 = new JLabel(Messages.getString("turnierpanel_points_per_game")); //$NON-NLS-1$
		jLabel6.setBounds(new Rectangle(6, 227, 105, 15));
		JLabel jLabel8 = new JLabel(Messages.getString("turnierpanel_points_per_tiegame")); //$NON-NLS-1$
		jLabel8.setBounds(new Rectangle(227, 227, 122, 15));
		jLabel8.setHorizontalAlignment(JLabel.RIGHT);
		JLabel jLabel10 = new JLabel(Messages.getString("turnierpanel_durationgame")); //$NON-NLS-1$
		jLabel10.setBounds(new Rectangle(6, 253, 105, 15));
		JLabel jLabel11 = new JLabel(Messages.getString("turnierpanel_durationbreak")); //$NON-NLS-1$
		jLabel11.setBounds(new Rectangle(227, 253, 122, 15));
		jLabel11.setHorizontalAlignment(JLabel.RIGHT);
		JLabel jLabel7 = new JLabel(Messages.getString("turnierpanel_fields")); //$NON-NLS-1$
		jLabel7.setBounds(new Rectangle(6, 297, 105, 15));

		text.setFont(turniername.getFont());
		((JSpinner.DateEditor) datumSpinner.getEditor())
			.getTextField()
			.setFont(
			turniername.getFont());
		text.setLineWrap(true);
		text.setWrapStyleWord(true);

		JScrollPane scrollText = new JScrollPane(text);
		spielplaetzeTable = new SpielplaetzeTable(this);
		JScrollPane spielplaetzeTableScroll =
			new JScrollPane(spielplaetzeTable);

		turniername.setBounds(new Rectangle(110, 10, 300, 21));
		datumSpinner.setBounds(new Rectangle(110, 35, 300, 21));
		scrollText.setBounds(new Rectangle(110, 60, 300, 63));
		linkid.setBounds(new Rectangle(110, 126, 300, 21));
		bannerlink.setBounds(new Rectangle(110, 151, 300, 21));
		beamerumschaltzeit.setBounds(new Rectangle(110, 176, 50, 21));
		punkteprosatz.setBounds(new Rectangle(110, 201, 50, 21));
		punkteprospiel.setBounds(new Rectangle(110, 226, 50, 21));
		punkteprounentschiedenspiel.setBounds(new Rectangle(361, 226, 50, 21));
		spieldauer.setBounds(new Rectangle(110, 251, 50, 21));
		pausendauer.setBounds(new Rectangle(361, 251, 50, 21));
		spielplaetzeTableScroll.setBounds(new Rectangle(110, 276, 300, 88));

		turniername.addKeyListener(new EditListener(this));
		datumSpinner.addChangeListener(new EditListener(this));
		text.addKeyListener(new EditListener(this));
		linkid.addKeyListener(new EditListener(this));
		bannerlink.addKeyListener(new EditListener(this));
		beamerumschaltzeit.addKeyListener(new EditListener(this));
		punkteprosatz.addKeyListener(new EditListener(this));
		punkteprospiel.addKeyListener(new EditListener(this));
		punkteprounentschiedenspiel.addKeyListener(new EditListener(this));
		spieldauer.addKeyListener(new EditListener(this));
		pausendauer.addKeyListener(new EditListener(this));

		mainPanel.setLayout(null);
		mainPanel.add(jLabel1);
		mainPanel.add(turniername);
		mainPanel.add(jLabel2);
		mainPanel.add(datumSpinner);
		mainPanel.add(jLabel3);
		mainPanel.add(scrollText);
		mainPanel.add(jLabel4);
		mainPanel.add(linkid);
		mainPanel.add(jLabel12);
		mainPanel.add(bannerlink);
		mainPanel.add(jLabel9);
		mainPanel.add(beamerumschaltzeit);
		mainPanel.add(jLabel5);
		mainPanel.add(punkteprosatz);
		mainPanel.add(jLabel6);
		mainPanel.add(punkteprospiel);
		mainPanel.add(jLabel8);
		mainPanel.add(punkteprounentschiedenspiel);
		mainPanel.add(jLabel10);
		mainPanel.add(spieldauer);
		mainPanel.add(jLabel11);
		mainPanel.add(pausendauer);
		mainPanel.add(jLabel7);
		mainPanel.add(spielplaetzeTableScroll);
	}

	/**
	 * l�dt Daten in die Maske
	 * 
	 * @param turnierBO
	 */
	public void loadData(Tournament aTurnierBO) {
		this.turnierBO = aTurnierBO;

		turniername.setText(aTurnierBO.getName());
		datumSpinner.setValue(aTurnierBO.getDate());
		text.setText(aTurnierBO.getText());
		linkid.setText(aTurnierBO.getLinkid());
		bannerlink.setText(aTurnierBO.getBannerLink());
		beamerumschaltzeit.setValue(
			new Integer((int) aTurnierBO.getDurationProjectorSwitch()));
		punkteprosatz.setValue(
			new Integer((int) aTurnierBO.getPointsPerSet()));
		punkteprospiel.setValue(
			new Integer((int) aTurnierBO.getPointsPerMatch()));
		punkteprounentschiedenspiel.setValue(
			new Integer((int) aTurnierBO.getPointsPerTie()));
		spieldauer.setValue(new Integer((int) aTurnierBO.getDurationMatch() / 60));
		pausendauer.setValue(
			new Integer((int) aTurnierBO.getDurationBreak() / 60));

		// M�gliche Spielplaetze setzen
		try {
			Vector<Field> spielplaetze =
				TurnierRemote.getSpielplaetzeByTurnierid(aTurnierBO.getId());
			spielplaetzeTable.dataModel.setData(spielplaetze);
		} catch (Exception e) {
			LOG.error("Can't read tournament from server.", e);
		}

		// banner link can't be changed for non profi version
		if (parentFrame.licenceBO.getLicencetype().equals(Licence.LICENCE_PROFI)) {
			bannerlink.setEnabled(true);
		}
		else {
			bannerlink.setEnabled(false);
		}

		dirty = false;
	}

	public void save() {
		dirty = false;

		if (turnierBO != null) {

			// if link-id exists in another tournament -> ask
			Tournament linkidTurnierBO = null;
			if (!linkid.getText().equals(turnierBO.getLinkid())) {
				try {
					linkidTurnierBO =
						HomeGetter
							.getTurnierSessionHome()
							.create()
							.getTurnierByLinkid(
							linkid.getText());
				} catch (Exception e) {
					linkidTurnierBO = null;
				}
			}
			if (linkidTurnierBO != null
				&& linkidTurnierBO.getId() != turnierBO.getId()) {
				JOptionPane.showMessageDialog(
					this,
					Messages.getString("turnierpanel_linkidconflict"),
					Messages.getString("volleyframe_view_challenge"),
					JOptionPane.ERROR_MESSAGE);
			} else {
				turnierBO.setName(turniername.getText());
				turnierBO.setDate((Date) datumSpinner.getValue());
				turnierBO.setText(text.getText());
				turnierBO.setLinkid(linkid.getText());
				turnierBO.setBannerLink(bannerlink.getText());
				turnierBO.setDurationProjectorSwitch(
					((Integer) beamerumschaltzeit.getValue()).longValue());
				turnierBO.setPointsPerSet(
					((Integer) punkteprosatz.getValue()).longValue());
				turnierBO.setPointsPerMatch(
					((Integer) punkteprospiel.getValue()).longValue());
				turnierBO.setPointsPerTie(
					((Integer) punkteprounentschiedenspiel.getValue())
						.longValue());
				turnierBO.setDurationMatch(
					((Integer) spieldauer.getValue()).longValue() * 60);
				turnierBO.setDurationBreak(
					((Integer) pausendauer.getValue()).longValue() * 60);

				try {
					TurnierRemote.saveByTurnierBO(turnierBO, null);
					TurnierRemote.setSpielplaetzeByTurnierId(
						turnierBO.getId(),
						spielplaetzeTable.dataModel.getData().toArray());
				} catch (Exception e) {
					LOG.error("Can't save tournament to server.", e);
				}
			}
		}
	}

}
