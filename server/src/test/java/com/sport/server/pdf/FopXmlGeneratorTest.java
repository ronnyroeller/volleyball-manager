package com.sport.server.pdf;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Locale;


import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sport.core.bo.LicenceBO;
import com.sport.core.bo.TournamentHolder;
import com.sport.server.pdf.FopXmlGenerator;

public class FopXmlGeneratorTest {

	private static final String BASE_PATH = "src/test/resources/"
			+ FopXmlGeneratorTest.class.getName() + "/";
	private static final File TUR_FILE = new File(BASE_PATH + "sample1.tur");
	private static final File FOP_XML_FILE = new File(BASE_PATH
			+ "sample1.fop.xml");

	private static FopXmlGenerator fopXmlGenerator;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		fopXmlGenerator = new FopXmlGenerator();
	}

	@Test
	public void testCreateXml() throws RemoteException, Exception {
		TournamentHolder tournamentHolder = new TurReader().read(TUR_FILE);
		LicenceBO license = createLicense();
		String fopXml = fopXmlGenerator.createXml(license, tournamentHolder, null, Locale.GERMAN);
		String expectedFopXml = FileUtils.readFileToString(FOP_XML_FILE,
				"UTF-8");

		assertEquals("XML is wrongly generated", normalizeXml(expectedFopXml),
				normalizeXml(fopXml));
	}

	private LicenceBO createLicense() {
		LicenceBO license = new LicenceBO();
		license.firstname = "John";
		license.lastname = "Meyer";
		license.city = "Boston";
		license.country = "US";
		license.licencedate = new Date();
		license.organisation = "Volley Sporters";
		license.licencetype = LicenceBO.LICENCE_STANDARD;
		
		return license;
	}

	/**
	 * Remove some characters to easier compare XML. Would be better to use
	 * canonicalizer here.
	 * 
	 * @param xml
	 * @return
	 */
	private String normalizeXml(String xml) {
		return xml.replace("\n", "").replace("\r", "").replace("\t", "")
				.replace(" ", "");
	}

}
