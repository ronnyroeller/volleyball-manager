/*
 * Created on 01.06.2003
 * 
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.sport.client.export;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import com.sport.core.helper.Messages;


/**
 * @author ronny
 */
public class ProgressDialog extends JDialog {

	private static final long serialVersionUID = 4574773038517880609L;

	private JProgressBar progress = new JProgressBar(0, 100);

	public ProgressDialog(Frame parent) {
		super(parent);
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		init(parent);
	}

	/**
	 * Initialisierung der Komponenten
	 * @param parent
	 */
	private void init(Frame parent) {
		progress.setIndeterminate(true);
		progress.setPreferredSize(new Dimension (300,20));
		progress.setBackground(Color.GRAY);

		JLabel label = new JLabel (Messages.getString("progressdialog_wait"));
		this.getContentPane().add(label, BorderLayout.NORTH);
		this.getContentPane().add(progress, BorderLayout.CENTER);
		setResizable(false);
		setUndecorated(true);
		
		Dimension dlgSize = getPreferredSize();
		Dimension frmSize = parent.getSize();
		Point loc = parent.getLocation();
		setLocation(
				(frmSize.width - dlgSize.width) / 2 + loc.x,
				(frmSize.height - dlgSize.height) / 2 + loc.y);
		setModal(true);
		pack();
	}

}
