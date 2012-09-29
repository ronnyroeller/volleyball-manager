/*
 * Created on 30.11.2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package com.sport.web.struts.action;

import java.util.Locale;

import javax.servlet.http.HttpSession;


import org.apache.struts.Globals;

import com.sport.core.helper.Messages;

/**
 * @author ronny
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class LocaleMgr {

	public static void setLocale(String localeString, HttpSession session) {

		// Sprache �ndern?
		if (localeString != null) {
			Locale locale = Messages.ENGLISH;
			if (localeString.equalsIgnoreCase(Messages.GERMAN.getLanguage())) {
				locale = Messages.GERMAN;
			}
			if (localeString.equalsIgnoreCase(Messages.SPANISH.getLanguage())) {
				locale = Messages.SPANISH;
			}
			if (localeString.equalsIgnoreCase(Messages.DUTCH.getLanguage())) {
				locale = Messages.DUTCH;
			}
			session.setAttribute(Globals.LOCALE_KEY, locale);
			Locale.setDefault(locale);
			Messages.changeLocale();
		}
	}

}
