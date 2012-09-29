package com.sport.core.helper;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Serves messages in the correct language.
 * 
 * Resource files can't be UTF-8 due to a Java limitation in JDK5!
 * 
 * @author Ronny
 *
 */
public final class Messages {

	protected static final String BUNDLE_NAME_BASE = Messages.class.getPackage().getName() + ".messages"; //$NON-NLS-1$
	private static ResourceBundle resource = ResourceBundle.getBundle(BUNDLE_NAME_BASE, Locale.getDefault());
	public static final Locale GERMAN = Locale.GERMAN;
	public static final Locale ENGLISH = Locale.ENGLISH;
	public static final Locale SPANISH = new Locale ("es","","");
	public static final Locale DUTCH = new Locale ("nl","","");

	private Messages() {
	}

	public static String getString(String key) {
		try {
			if (resource != null && key != null)
				return resource.getString(key);
		} catch (MissingResourceException e) {
		}

		return '!' + key + '!';
	}

	public static void changeLocale() {
		resource =
			ResourceBundle.getBundle(BUNDLE_NAME_BASE, Locale.getDefault());
	}
	
	/**
	 * Gibt alle Messages der gewuenschten Sprache wieder.
	 * Wichtig fuer PDF-Generation
	 */
	public static Map<String, String> getMessagesMap (Locale local) {
		Map<String, String> map = new HashMap<String, String> ();
		ResourceBundle res = ResourceBundle.getBundle(BUNDLE_NAME_BASE, local);
		
		Enumeration<String> keys = res.getKeys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			map.put(key, res.getString(key));
		}
		
		return map;
	}
	
}
