package com.sport.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import com.sport.core.helper.Messages;
import com.sport.core.helper.ProductInfo;
import com.sport.server.ejb.HomeGetter;

/**
 * @author ronny
 * 
 *         Testklassen, um Entitybeans zu testen. Alle Anfragen werden durch das
 *         Session- bean gefuehrt.
 */
public class VolleyApp {

	private static final Logger LOG = Logger.getLogger(VolleyApp.class
			.getName());

	private static final String PROPERTIES_FILE_NAME = "jvolley.properties";
	private static Properties props = new Properties();
	private static VolleyFrame jvolleyframe = null;
	private static String lastChoosenDir;

	public static void main(String[] args) throws Exception {
		// Have all uncatched exceptions handled
		Thread.setDefaultUncaughtExceptionHandler(
		        new DefaultExceptionHandler());

		LOG.info("Starting Volleyball Manager " + ProductInfo.getInstance().getVersion()
				+ " - Administrator");

		loadProperties();

		JFrame fooFrame = new JFrame();
		fooFrame.setIconImage(Icons.APPLICATION.getImage());
		JDialog dlg = new SplashDialog(fooFrame);

		Runnable loadVolleyFrameRunnable = new LoadVolleyFrameRunnable(dlg);
		Thread thread = new Thread(loadVolleyFrameRunnable);
		thread.start();

		dlg.show();
	}

	public static class LoadVolleyFrameRunnable implements Runnable {
		JDialog dlg;

		public LoadVolleyFrameRunnable(JDialog dlg) {
			this.dlg = dlg;
		}

		public void run() {
			jvolleyframe = VolleyFrame.getInstance();
			jvolleyframe.setVisible(true);
			if (lastChoosenDir != null) {
				jvolleyframe.lastChoosenDir = new File(lastChoosenDir);
			}
			dlg.hide();
		}
	};

	// Read properties file and set language + host
	private static void loadProperties() throws IOException {
		// Default values
		String language = Messages.ENGLISH.getLanguage();
		String host = "";
		String lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";

		// if properties files exists -> use these values!
		try {
			props.load(new FileInputStream(PROPERTIES_FILE_NAME));
			language = props.getProperty("language");
			host = props.getProperty("host");
			lookAndFeel = props.getProperty("lookandfeel");
			lastChoosenDir = props.getProperty("lastChoosenDir");
		} catch (FileNotFoundException e) {
		}

		try {
			UIManager.setLookAndFeel(lookAndFeel); //$NON-NLS-1$
		} catch (Exception e) {
			try {
				UIManager
						.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			} catch (Exception ex) {
				LOG.info("Couldn't look&feel");
			}
		}

		try {
			Locale locale;
			// translate ISO3 to locale style (ISO3 is written by IZPack)
			if (language.equals("deu")) {
				locale = Messages.GERMAN;
			} else if (language.equals("eng")) {
				locale = Messages.ENGLISH;
			} else if (language.equals("ned")) {
				locale = Messages.DUTCH;
			} else if (language.equals("spa")) {
				locale = Messages.SPANISH;
			} else {
				locale = new Locale(language, "");
			}
			Locale.setDefault(locale);
			Messages.changeLocale();
		} catch (Exception e) {
			Locale.setDefault(Messages.ENGLISH);
			Messages.changeLocale();
		}

		// TODO: This won't work if multiple Administrators are running from
		// different machines
		HomeGetter.setHost(host);
	}

	/**
	 * changes the locale -> write to ResourceBundle
	 */
	public static void setLocale(Locale locale) {
		props.setProperty("language", locale.getLanguage());
		Locale.setDefault(locale);
		Messages.changeLocale();

		try {
			props.store(new FileOutputStream(PROPERTIES_FILE_NAME),
					"Volleyball Manager Configfile");
		} catch (FileNotFoundException e) {
			LOG.error("Can't find the properties file '" + PROPERTIES_FILE_NAME
					+ "'", e);
		} catch (IOException e) {
			LOG.error("Can't read the properties file '" + PROPERTIES_FILE_NAME
					+ "'", e);
		}

		// recreate Window
		jvolleyframe.dispose();
		jvolleyframe = VolleyFrame.getNewInstance();
		jvolleyframe.setVisible(true);
	}

	/**
	 * changes the lastchoosendir -> write to ResourceBundle
	 */
	public static void writeLastChoosenDir(String aLastChoosenDir) {
		props.setProperty("lastChoosenDir", aLastChoosenDir);

		try {
			props.store(new FileOutputStream(PROPERTIES_FILE_NAME),
					"Volleyball Manager Configfile");
		} catch (FileNotFoundException e) {
			LOG.error("Can't find properties file '" + PROPERTIES_FILE_NAME
					+ "'", e);
		} catch (IOException e) {
			LOG.error("Can't write the last choose dir '" + aLastChoosenDir
					+ "' to properties file '" + PROPERTIES_FILE_NAME + "'", e);
		}
	}

}
