package com.sport.server.pdf;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import javax.xml.transform.TransformerException;

import org.apache.commons.io.FileUtils;
import org.apache.fop.apps.FOPException;
import org.junit.Test;

public class PdfHelperTest {

	private static final String BASE_PATH = "src/test/resources/"
			+ PdfHelperTest.class.getName() + "/";
	private static final File FOP_XML_FILE = new File(BASE_PATH + "fop.xml");
	private static final File FOP_XML_FILE_EMPTY_GROUP = new File(BASE_PATH + "empty-group.xml");

	private static final File PDF_FILE_REFEREE = new File(BASE_PATH + "referee.pdf");
	private static final File PDF_FILE_EMPTY_GROUP = new File(BASE_PATH + "empty-group.pdf");

	@Test
	public void testRunFop() throws IOException, FOPException,
			TransformerException {
		String fopXml = FileUtils.readFileToString(FOP_XML_FILE, "UTF-8");

		byte[] actualPdf = PdfHelper.runFop(fopXml, "referee.xsl");

		byte[] expectedPdf = FileUtils.readFileToByteArray(PDF_FILE_REFEREE);
		
		assertEquals("PDF not generated correctly", expectedPdf.length, actualPdf.length);
	}

	/**
	 * Check if group PDF can be generated for a tournament with empty groups
	 * @throws IOException
	 * @throws FOPException
	 * @throws TransformerException
	 */
	@Test
	public void testFopWithEmptyGroups() throws IOException, FOPException,
			TransformerException {
		String fopXml = FileUtils.readFileToString(FOP_XML_FILE_EMPTY_GROUP, "UTF-8");

		byte[] actualPdf = PdfHelper.runFop(fopXml, "groups.xsl");

		byte[] expectedPdf = FileUtils.readFileToByteArray(PDF_FILE_EMPTY_GROUP);
		
		assertEquals("PDF not generated correctly", expectedPdf.length, actualPdf.length);
	}

}
