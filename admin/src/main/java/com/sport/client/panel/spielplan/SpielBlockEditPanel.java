/*
 * Created on 16.06.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.sport.client.panel.spielplan;

import java.awt.Rectangle;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;

import com.sport.client.panel.EditListener;
import com.sport.core.bo.SpielplanTableEntryBO;
import com.sport.core.helper.Messages;


/**
 * @author ronny
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SpielBlockEditPanel extends JPanel {

	private static final long serialVersionUID = -1962884709312379091L;

	protected SpielplanTableEntryBO tableEntry = null;
	
	// aktuell angezeigtes Spiel
	private SpielplanPanel spielpanel = null;

	private JSpinner vondatum = null;
	private JSpinner dauer = null;

	public SpielBlockEditPanel(SpielplanPanel spielplanel) {
		this.spielpanel = spielplanel;

		JLabel jLabel1 = new JLabel(Messages.getString("spielblockeditpanel_begin")); //$NON-NLS-1$
		jLabel1.setBounds(new Rectangle(13, 5, 90, 15));
		JLabel jLabel2 = new JLabel(Messages.getString("spielblockeditpanel_during")); //$NON-NLS-1$
		jLabel2.setBounds(new Rectangle(13, 30, 90, 15));

		// Use date pattern that fits the current selected Locale
		String datePattern = ((SimpleDateFormat) SimpleDateFormat.getInstance()).toPattern();

		// Datum
		SpinnerDateModel model = new SpinnerDateModel();
		model.setCalendarField(Calendar.HOUR_OF_DAY);
		vondatum = new JSpinner(model);
		JSpinner.DateEditor editor =
			new JSpinner.DateEditor(vondatum, datePattern); //$NON-NLS-1$
		vondatum.setEditor(editor);

		SpinnerNumberModel model2 = new SpinnerNumberModel(0,0,240,1);
		dauer = new JSpinner (model2);
		JSpinner.NumberEditor editor2 =	new JSpinner.NumberEditor(dauer);
		dauer.setEditor(editor2);

		((JSpinner.DateEditor) vondatum.getEditor()).getTextField().setFont(jLabel2.getFont());
		((JSpinner.NumberEditor) dauer.getEditor()).getTextField().setFont(jLabel1.getFont());

		vondatum.setBounds(new Rectangle(110, 3, 140, 21));
		dauer.setBounds(new Rectangle(110, 28, 50, 21));

		this.setLayout(null);
		this.add(jLabel1, null);
		this.add(jLabel2, null);

		this.add(vondatum, null);
		this.add(dauer, null);

		vondatum.addKeyListener(new EditListener(this.spielpanel));
		vondatum.addMouseListener(new EditListener(this.spielpanel));
		dauer.addKeyListener(new EditListener(this.spielpanel));
		dauer.addMouseListener(new EditListener(this.spielpanel));

	}

	/**
	 * alte Daten zur�ckspeichern
	 *
	 */
	public void save() {
		if (tableEntry != null) {
			SpielplanTableEntryBO oldTableEntry = this.tableEntry;
			
			Date date = (Date) vondatum.getValue();
			date = new Date (date.getTime() + ((Number) dauer.getValue()).longValue() * 60*1000);
			
			// Zeitdifferenz
			Date oldBisdatum = oldTableEntry.getBisdatum();
			long timediff = date.getTime() - oldBisdatum.getTime();

			spielpanel.spielplanTable.dataModel.moveTime(oldBisdatum, timediff);

			oldTableEntry.setVondatum((Date) vondatum.getValue());
			oldTableEntry.setBisdatum(date);
			
		}
	}

	/**
	 * Setzt Daten
	 * @param spielBO
	 */
	public void setTableEntry(SpielplanTableEntryBO tableEntry) {
		this.tableEntry = tableEntry;
		// kein Spiel ausgewaehlt
		if (tableEntry != null) {
			vondatum.setValue(tableEntry.getVondatum());
			dauer.setValue(new Long ((tableEntry.getBisdatum().getTime () - tableEntry.getVondatum().getTime ()) / (1000*60)));
		}
	}

}
