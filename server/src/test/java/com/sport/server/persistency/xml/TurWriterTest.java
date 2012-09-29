package com.sport.server.persistency.xml;

import static org.junit.Assert.assertEquals;

import java.io.File;


import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.sport.core.bo.TournamentHolder;
import com.sport.server.pdf.TurReader;
import com.sport.server.persistency.xml.TurWriter;

public class TurWriterTest {

	private static final String BASE_PATH = "src/test/resources/"
			+ TurWriterTest.class.getName() + "/";
	private static final File TUR_FILE = new File(BASE_PATH + "sample1.tur");

	/**
	 * Reads a *.tur file and writes it again out. We expect that the *.tur file stays the same.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testWrite() throws Exception {
		TournamentHolder tournamentHolder = new TurReader().read(TUR_FILE);
		String turXml = new TurWriter().write(tournamentHolder);
		
		String expectedTurXml = FileUtils.readFileToString(TUR_FILE,
				"UTF-8");
		
//		FileUtils.writeStringToFile(new File(BASE_PATH + "sample1.exp.tur"), turXml, "UTF-8");
		
		assertEquals("XML is wrongly generated", normalizeXml(expectedTurXml),
				normalizeXml(turXml));
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
