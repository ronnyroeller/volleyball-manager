/*
 * Created on 04.06.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.sport.core.bo;

import java.io.Serializable;
import java.util.Date;

import com.sport.core.helper.Messages;


/**
 * @author ronny
 */
public class LicenceBO implements Serializable {

	private static final long serialVersionUID = 1962362593520350085L;

	// COPYED VALUES IN JVOLLEY.HELPER.LICENCE!!!
	public static final String LICENCE_DEMO = "licence_demo";
	public static final String LICENCE_STANDARD = "licence_standard";
	public static final String LICENCE_PROFI = "licence_profi";

	public String firstname = "";
	public String lastname = "";
	public String organisation = "";
	public String city = "";
	public String country = "";
	public Date licencedate;
	public String licencetype = LICENCE_DEMO;
	
	/**
	 * @return Returns the city.
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @return Returns the country.
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @return Returns the firstname.
	 */
	public String getFirstname() {
		return firstname;
	}

	/**
	 * @return Returns the lastname.
	 */
	public String getLastname() {
		return lastname;
	}

	/**
	 * @return Returns the licencedate.
	 */
	public Date getLicencedate() {
		return licencedate;
	}

	/**
	 * @return Returns the licencetype.
	 */
	public String getLicencetype() {
		return licencetype;
	}

	/**
	 * @return Returns the licencetype.
	 */
	public String getLicencetypeLocale() {
		return Messages.getString(licencetype);
	}
	
	/**
	 * @return Returns the organisation.
	 */
	public String getOrganisation() {
		return organisation;
	}

}
