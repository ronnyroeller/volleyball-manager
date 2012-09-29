/*
 * Created on 03.06.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.sport.client.panel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.sport.client.VolleyFrame;
import com.sport.core.bo.Tournament;
import com.sport.core.helper.Messages;


/**
 * @author ronny
 */
public abstract class VolleyPanel extends JPanel {

	private static final long serialVersionUID = -8865514911373202692L;

	public VolleyFrame parentFrame = null;
	public Tournament turnierBO = null;
	private JButton save = new JButton(Messages.getString("volleypanel_save")); //$NON-NLS-1$
	private JButton reset = new JButton(Messages.getString("volleypanel_reset")); //$NON-NLS-1$
	
	// mainPanel enth�lt View-spezifische Eingabemaske
	public JPanel mainPanel = new JPanel ();

	public boolean dirty = false; // zeigt an, ob Daten ge�ndert wurden

	public VolleyPanel (VolleyFrame parentFrame) {
		super ();

		this.parentFrame = parentFrame;
		setLayout(new BorderLayout());

		add(mainPanel, BorderLayout.CENTER);

		reset.addActionListener(new VolleyPanel_reset_actionAdapter(this));
		save.addActionListener(new VolleyPanel_save_actionAdapter(this));
		JPanel panel = new JPanel ();
		panel.add(save);
		panel.add(reset);
		add(panel, BorderLayout.SOUTH);
	}

	/**
	 * L�dt neue Daten in das Panel
	 * @param turnierBO
	 */
	public abstract void loadData (Tournament aTurnierBO);

	/**
	 * Speichert die aktuellen Daten des Panels
	 *
	 */
	public abstract void save ();

	class VolleyPanel_reset_actionAdapter
		implements java.awt.event.ActionListener {
		VolleyPanel adaptee;
		VolleyPanel_reset_actionAdapter(VolleyPanel adaptee) {
			this.adaptee = adaptee;
		}
		public void actionPerformed(ActionEvent e) {
			adaptee.loadData(turnierBO);
		}
	}

	class VolleyPanel_save_actionAdapter
		implements java.awt.event.ActionListener {
		VolleyPanel adaptee;
		VolleyPanel_save_actionAdapter(VolleyPanel adaptee) {
			this.adaptee = adaptee;
		}
		public void actionPerformed(ActionEvent e) {
			adaptee.save();
		}
	}

}
	