/*
 * Created on 01.06.2003
 * 
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.sport.client;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sport.core.helper.Messages;
import com.sport.core.helper.ProductInfo;


;
/**
 * @author ronny
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SplashDialog extends JDialog {

	private static final long serialVersionUID = 5275488490367784245L;

	private JPanel panel1 = new JPanel();
	private JPanel panel2 = new JPanel();
	private JPanel insetsPanel1 = new JPanel();
	private JPanel insetsPanel2 = new JPanel();
	private JPanel insetsPanel3 = new JPanel();
	private JLabel imageLabel = new JLabel();
	private JLabel label1 = new JLabel();
	private JLabel label3 = new JLabel();
	private JLabel label4 = new JLabel();
	private JLabel label5 = new JLabel();
	private JLabel label6 = new JLabel();
	private BorderLayout borderLayout1 = new BorderLayout();
	private BorderLayout borderLayout2 = new BorderLayout();
	private FlowLayout flowLayout1 = new FlowLayout();
	private GridLayout gridLayout1 = new GridLayout();

	public SplashDialog(JFrame frame) {
		super(frame);
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		init();
	}

	/**
	 * Initialisierung der Komponenten
	 * 
	 * @throws Exception
	 */
	private void init() {
		imageLabel.setIcon(Icons.APPLICATION);
		this.setTitle(Messages.getString("aboutdialog_info")); //$NON-NLS-1$
		panel1.setLayout(borderLayout1);
		panel2.setLayout(borderLayout2);
		insetsPanel1.setLayout(flowLayout1);
		insetsPanel2.setLayout(flowLayout1);
		insetsPanel2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		gridLayout1.setRows(5);
		gridLayout1.setColumns(1);
		label1.setText(AboutDialog.product + " " + ProductInfo.getInstance().getVersion());
		label3.setText(AboutDialog.copyright);
		label4.setText(" ");
		label5.setText(AboutDialog.email);
		label6.setText(AboutDialog.web);
		insetsPanel3.setLayout(gridLayout1);
		insetsPanel3.setBorder(BorderFactory.createEmptyBorder(10, 60, 10, 10));
		insetsPanel2.add(imageLabel, null);
		panel2.add(insetsPanel2, BorderLayout.WEST);
		this.getContentPane().add(panel1, null);
		insetsPanel3.add(label1, null);
		insetsPanel3.add(label3, null);
		insetsPanel3.add(label4, null);
		insetsPanel3.add(label5, null);
		insetsPanel3.add(label6, null);
		panel2.add(insetsPanel3, BorderLayout.CENTER);
		panel1.add(insetsPanel1, BorderLayout.SOUTH);
		panel1.add(panel2, BorderLayout.NORTH);
		setResizable(false);

		setUndecorated(true);
		pack();
		Dimension dlgSize = getSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - dlgSize.width) / 2,
				(screenSize.height - dlgSize.height) / 2);
	}

}
