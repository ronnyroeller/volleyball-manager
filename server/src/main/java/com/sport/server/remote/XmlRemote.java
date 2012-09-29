/*
 * Created on 14.07.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.sport.server.remote;

import java.util.Locale;
import java.util.Vector;

import com.sport.core.bo.Tournament;
import com.sport.server.ejb.HomeGetter;


/**
 * @author ronny
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class XmlRemote {

	public static byte[] getSpielplanPDFByTurnierid(
		long turnierid,
		Vector selGruppenIds,
		Locale locale)
		throws Exception {

		return HomeGetter
			.getXmlSessionHome()
			.create()
			.getSpielplanPDFByTurnierid(
			turnierid,
			selGruppenIds,
			locale);
	}

	public static byte[] getSchiedsrichterPDFByTurnierid(
			long turnierid,
			Vector selGruppenIds,
			Locale locale)
			throws Exception {

			return HomeGetter
				.getXmlSessionHome()
				.create()
				.getSchiedsrichterPDFByTurnierid(
				turnierid,
				selGruppenIds,
				locale,
				false);
		}

	public static byte[] getSpielberichtPDFByTurnierid(
			long turnierid,
			Vector selGruppenIds,
			Locale locale)
			throws Exception {

			return HomeGetter
				.getXmlSessionHome()
				.create()
				.getSchiedsrichterPDFByTurnierid(
				turnierid,
				selGruppenIds,
				locale,
				true);
		}

	public static byte[] getGruppenPDFByTurnierid(
		long turnierid,
		Vector selGruppenIds,
		Locale locale)
		throws Exception {

		return HomeGetter
			.getXmlSessionHome()
			.create()
			.getGruppenPDFByTurnierid(
			turnierid,
			selGruppenIds,
			locale);
	}

	public static Tournament loadTurnierByXMLUserid(String xml, long userid)
		throws Exception {

		return HomeGetter.getXmlSessionHome().create().loadTurnierByXMLUserid(
			xml,
			userid);
	}

	public static String saveXMLByTurnierid(long turnierid) throws Exception {

		return HomeGetter.getXmlSessionHome().create().saveXMLByTurnierid(
			turnierid);
	}

}
