/*
 * Created on 01.06.2003
 * 
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.sport.client;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sport.core.helper.Messages;
import com.sport.core.helper.ProductInfo;


/**
 * Shows the About dialog
 * 
 * @author ronny
 */
public class AboutDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = -2258983668536980193L;

	private VolleyFrame parent;
	private JPanel panel1 = new JPanel();
	private JPanel panel2 = new JPanel();
	private JPanel insetsPanel1 = new JPanel();
	private JPanel insetsPanel2 = new JPanel();
	private JPanel insetsPanel3 = new JPanel();
	private JButton buttonOk = new JButton();
	private JButton buttonErrorReport = new JButton();
	private JLabel imageLabel = new JLabel();
	private BorderLayout borderLayout1 = new BorderLayout();
	private BorderLayout borderLayout2 = new BorderLayout();
	private FlowLayout flowLayout1 = new FlowLayout();
	private GridLayout gridLayout1 = new GridLayout();
	static String product = "Volleyball Manager";
	static String copyright = "(c) 2000, 2012 Roeller";
	static String email = "Email: info@volleyball-manager.com";
	static String web = "Web: " + VolleyMenuBar.WEBSITE_GENERIC;

	public AboutDialog(VolleyFrame parent) {
		super(parent);
		
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		init(parent);
	}

	/**
	 * Initialisierung der Komponenten
	 * 
	 * @param aParent
	 * @throws Exception
	 */
	private void init(VolleyFrame aParent) {
		parent = aParent;
		imageLabel.setIcon(Icons.APPLICATION);
		this.setTitle(Messages.getString("aboutdialog_info")); //$NON-NLS-1$
		panel1.setLayout(borderLayout1);
		panel2.setLayout(borderLayout2);
		insetsPanel1.setLayout(flowLayout1);
		insetsPanel2.setLayout(flowLayout1);
		insetsPanel2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		Vector<String> information = new Vector<String>();
		information.add(product + " " + ProductInfo.getInstance().getVersion());
		information.add(copyright);
		information.add("");
		information.add(email);
		information.add(web);
		information.add("");
		information.add(Messages.getString("licence_licencefor"));
		if (!"".equals(parent.licenceBO.lastname)) {
			information.add(parent.licenceBO.firstname + " "
					+ parent.licenceBO.lastname + " ("
					+ parent.licenceBO.organisation + ")");
			information.add(parent.licenceBO.city + ", "
					+ parent.licenceBO.country);
			String licencedate = DateFormat.getDateInstance(DateFormat.SHORT)
					.format(parent.licenceBO.licencedate);
			information.add(Messages.getString(parent.licenceBO.licencetype)
					+ " (" + licencedate + ")");
		} else {
			information.add(Messages.getString(parent.licenceBO.licencetype));
		}
		gridLayout1.setRows(information.size());
		gridLayout1.setColumns(1);
		insetsPanel3.setLayout(gridLayout1);
		insetsPanel3.setBorder(BorderFactory.createEmptyBorder(10, 60, 10, 10));
		buttonOk.setText(Messages.getString("aboutdialog_ok")); //$NON-NLS-1$
		buttonOk.addActionListener(this);
		buttonErrorReport
				.setText(Messages.getString("aboutdialog_errorreport")); //$NON-NLS-1$
		buttonErrorReport.addActionListener(this);
		insetsPanel2.add(imageLabel, null);
		panel2.add(insetsPanel2, BorderLayout.WEST);
		this.getContentPane().add(panel1, null);

		for (String info : information)
			insetsPanel3.add(new JLabel(info), null);

		panel2.add(insetsPanel3, BorderLayout.CENTER);
		insetsPanel1.add(buttonErrorReport, null);
		insetsPanel1.add(buttonOk, null);
		panel1.add(insetsPanel1, BorderLayout.SOUTH);
		panel1.add(panel2, BorderLayout.NORTH);
		setResizable(true);
	}

	/**
	 * �berschrieben, so dass eine Beendigung beim Schlie�en des Fensters
	 * m�glich ist.
	 */
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			cancel();
		}
		super.processWindowEvent(e);
	}

	/**
	 * Dialog schlie�en
	 */
	void cancel() {
		dispose();
	}

	/**
	 * Dialog bei Schalter-Ereignis schlie�en
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == buttonOk) {
			cancel();
		} else if (e.getSource() == buttonErrorReport) {
			// default settings for window
			String data = "?";
			data += "clversion=" + parent.licenceBO.licencetype;
			data += "&clnumber=" + ProductInfo.getInstance().getVersion();
			data += "&clorganisation=" + parent.licenceBO.organisation;
			data += "&clname=" + parent.licenceBO.firstname + " "
					+ parent.licenceBO.lastname;
			
			String url = (VolleyMenuBar.isGerman()) ? VolleyMenuBar.WEBSITE_BUG_GERMAN: VolleyMenuBar.WEBSITE_BUG_ENGLISH;
			
			VolleyFrame.showExternalExplorer(url + data);
		}
	}
}
