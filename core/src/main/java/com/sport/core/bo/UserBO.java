package com.sport.core.bo;

import java.io.Serializable;

/**
 * @author ronny
 */
public class UserBO implements Serializable {

	private static final long serialVersionUID = -1423412330745973750L;

	private long id;
	private String username;
	private String passwort;
	private String name;
	private String vorname;
	private String email;
	private long level;
	
	public UserBO () {
		// Achtung: dieser Wert wird NICHT fuer die EJB-Ids verwendet!
		setId(Math.round(Math.random() * 2100000000)); // eindeutige ID, damit Combo-Box funktioniert!
	}
	
	public String toString () {
		return getVorname() + " " + getName();
	}
	

	/**
	 * @return
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param l
	 */
	public void setId(long l) {
		id = l;
	}

	/**
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * @return
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return
	 */
	public long getLevel() {
		return level;
	}

	/**
	 * @return
	 */
	public String getPasswort() {
		return passwort;
	}

	/**
	 * @return
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return
	 */
	public String getVorname() {
		return vorname;
	}

	/**
	 * @param string
	 */
	public void setEmail(String string) {
		email = string;
	}

	/**
	 * @param l
	 */
	public void setLevel(long l) {
		level = l;
	}

	/**
	 * @param string
	 */
	public void setPasswort(String string) {
		passwort = string;
	}

	/**
	 * @param string
	 */
	public void setUsername(String string) {
		username = string;
	}

	/**
	 * @param string
	 */
	public void setVorname(String string) {
		vorname = string;
	}

}
