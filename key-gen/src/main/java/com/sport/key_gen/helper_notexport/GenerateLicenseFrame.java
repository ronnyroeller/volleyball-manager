/*
 * Created on 08.05.2004
 * 
 * (c) Ronny Roeller. All rights reserved.
 */
package com.sport.key_gen.helper_notexport;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;

import com.sport.core.bo.LicenceBO;
import com.sport.core.helper.Licence;


/**
 * @author ronny
 *  
 */
public class GenerateLicenseFrame extends JFrame {

	JTextField firstname = new JTextField(40);
	JTextField lastname = new JTextField(40);
	JTextField organisation = new JTextField(40);
	JTextField city = new JTextField(40);
	JTextField country = new JTextField(40);
	JComboBox licencetype = new JComboBox();
	JSpinner date;
	JButton createButton = new JButton("Create License File");

	// number of this generator
//	long gennumber = 635689525; // Dagi (1)
	long gennumber = 784357445; // Ronny Test
	String serversecrete = "krasses-passwort";

	public GenerateLicenseFrame() {
		setTitle("Create License File");
		JPanel contentPane = (JPanel) getContentPane();
		contentPane.setLayout(new GridLayout(16, 1));

		// create Date element
		SpinnerDateModel model = new SpinnerDateModel();
		model.setCalendarField(Calendar.WEEK_OF_MONTH);
		date = new JSpinner(model);
		JSpinner.DateEditor editor = new JSpinner.DateEditor(date, "dd. MMM yyyy"); //$NON-NLS-1$
		date.setEditor(editor);

		// create license types
		licencetype.addItem(Licence.LICENCE_STANDARD);
		licencetype.addItem(Licence.LICENCE_PROFI);
		licencetype.addItem(Licence.LICENCE_DEMO);

		// put all together
		contentPane.add(new JLabel("Firstname"));
		contentPane.add(firstname);
		contentPane.add(new JLabel("Lastname"));
		contentPane.add(lastname);
		contentPane.add(new JLabel("Organisation"));
		contentPane.add(organisation);
		contentPane.add(new JLabel("City"));
		contentPane.add(city);
		contentPane.add(new JLabel("Country"));
		contentPane.add(country);
		contentPane.add(new JLabel("License type"));
		contentPane.add(licencetype);
		contentPane.add(new JLabel("Enddate"));
		contentPane.add(date);
		contentPane.add(new JLabel(""));
		contentPane.add(createButton);
		pack();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screenSize.width - getWidth()) / 2;
		int y = (screenSize.height - getHeight()) / 2;
		setBounds(x, y, getWidth(), getHeight());

		createButton.addActionListener(new Adapter());

		// default values
		country.setText("Deutschland");
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.WEEK_OF_MONTH, 1);
		cal.add(Calendar.YEAR, 1);
		date.setValue(cal.getTime());
	}

	//�berschrieben, so dass eine Beendigung beim Schlie�en des Fensters
	// m�glich ist.
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			System.exit(0);
		} else {
			super.processWindowEvent(e);
		}
	}

	class Adapter implements java.awt.event.ActionListener {
		public void actionPerformed(ActionEvent e) {
			LicenceBO licenceBO = new LicenceBO();

			licenceBO.firstname = firstname.getText().trim();
			licenceBO.lastname = lastname.getText().trim();
			licenceBO.organisation = organisation.getText().trim();
			licenceBO.city = city.getText().trim();
			licenceBO.country = country.getText().trim();
			licenceBO.licencetype = (String) licencetype.getSelectedItem();
			licenceBO.licencedate = (Date) date.getValue();

			String err = "";

			// check for reasonable data
			// never longer than 2y!
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.YEAR, 2);

			if (licenceBO.licencedate.after(cal.getTime())) {
				JOptionPane.showMessageDialog(
					null,
					"Under no cirumstances you can create licenses for longer than 2 years!",
					"Fatal Error",
					JOptionPane.ERROR_MESSAGE);
			} else {
				if (licenceBO.firstname.length() == 0
					|| licenceBO.lastname.length() == 0
					|| licenceBO.city.length() == 0
					|| licenceBO.country.length() == 0) {
					JOptionPane.showMessageDialog(
						null,
						"All fields must have a value!",
						"Fatal Error",
						JOptionPane.ERROR_MESSAGE);
				} else {

					// check for non-fatal warnings
					if (licenceBO.firstname.length() < 3) {
						err
							+= "  + Firstname is shorter than 3 character (must be a real name!)\n";
					}
					if (licenceBO.lastname.length() < 3) {
						err
							+= "  + Lastname is shorter than 3 character (must be a real name!)\n";
					}
					if (licenceBO.organisation.length() < 7) {
						err
							+= "  + Organisation is shorter than 7 character (abbreviations are not allowed!)\n";
					}
					if (licenceBO.city.length() < 3) {
						err
							+= "  + City is shorter than 3 character (do not use abbreviations!)\n";
					}
					cal.setTime(new Date());
					cal.add(Calendar.MONTH, 2);
					if (licenceBO.licencedate.before(cal.getTime())) {
						err += "  + License time is shorter than 2 months.\n";
					}

					// did any warnings occure?
					boolean no_license = false;
					if (err.length() > 0) {
						if (JOptionPane
							.showConfirmDialog(
								null,
								"Some warnings occure (please check all carefully):\n"
									+ err
									+ "Everything okay? You really want to generate the license file?",
								"Warnings",
								JOptionPane.WARNING_MESSAGE)
							!= JOptionPane.OK_OPTION) {
							no_license = true;
						}
					}

					// create license
					if (!no_license) {
						// all security checks passed?
						boolean isWriteLicense = false;

						// send information to server and wait for an answer
						long nounce =
							Math.round(Math.random() * Long.MAX_VALUE);
						URL url;
						try {
							SimpleDateFormat formatter =
								new SimpleDateFormat("MM/dd/yyyy");
							url =
								new URL(
									"http://volley.kommdot.de/gen/do.php?firstname="
										+ URLEncoder.encode(licenceBO.firstname)
										+ "&lastname="
										+ URLEncoder.encode(licenceBO.lastname)
										+ "&organisation="
										+ URLEncoder.encode(
											licenceBO.organisation)
										+ "&city="
										+ URLEncoder.encode(licenceBO.city)
										+ "&country="
										+ URLEncoder.encode(licenceBO.country)
										+ "&licencetype="
										+ URLEncoder.encode(
											licenceBO.licencetype)
										+ "&licencedate="
										+ URLEncoder.encode(
											formatter.format(
												licenceBO.licencedate))
										+ "&genno="
										+ gennumber
										+ "&nounce="
										+ nounce);
							HttpURLConnection conn =
								(HttpURLConnection) url.openConnection();
							conn.setRequestMethod("GET");
							conn.setUseCaches(false);
							if (conn.getResponseCode()
								!= HttpURLConnection.HTTP_OK) {
								System.out.println(conn.getResponseMessage());
							} else {
								InputStream in = conn.getInputStream();
								byte[] bytes =
									new byte[conn.getContentLength()];
								in.read(bytes);
								String result = new String(bytes);

								// invalid message?
								if (!result.equals("invalid")) {
									// calculate the same hash and compare to
									// answer from php
									MessageDigest messageDigest =
										MessageDigest.getInstance("MD5");
									byte[] hash =
										messageDigest.digest(
											(nounce + serversecrete)
												.getBytes());

									// transform bytes to hex representation
									// (make it comparable to php response)
									String hashStr = "";
									for (int i = 0; i < hash.length; i++) {
										hashStr
											+= Integer.toString(
												(hash[i] & 0xff) + 0x100,
												16).substring(
												1);
									}
									if (hashStr.equals(result)) {
										isWriteLicense = true;
									}
								}
							}
						} catch (Exception e2) {
							e2.printStackTrace();
						}

						if (isWriteLicense) {
							try {
								LicenceGenerator.writeValid(licenceBO);
								JOptionPane.showMessageDialog(
									null,
									"Check the file now in your installation!\n"
										+ "Never forget to recheck the license file for typos!!!",
									"License generated",
									JOptionPane.WARNING_MESSAGE);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}
						else {
							JOptionPane.showMessageDialog(
									null,
									"Some error occured during generation!\n"+
									"Check if your internet connection is available.",
									"No license file generated",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		}
	}

}
