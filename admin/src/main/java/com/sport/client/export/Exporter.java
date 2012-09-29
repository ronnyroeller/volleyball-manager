/*
 * Created on 01.12.2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package com.sport.client.export;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import com.sport.core.bo.Tournament;
import com.sport.server.remote.XmlRemote;


/**
 * Keeps all functions to export turniers to PDF, HTML, etc.
 * 
 * @author ronny
 */
public class Exporter {

	/**
	 * Exports the schedule of a turnier to PDF
	 * 
	 * @param path
	 * @param turnierBO
	 * @param selectedGruppeIds
	 * @throws Exception
	 */
	public static void exportPdfSpielplan(String path, Tournament turnierBO,
			Vector<Long> selectedGruppeIds) throws Exception {

		byte[] pdf = XmlRemote.getSpielplanPDFByTurnierid(turnierBO.getId(),
				selectedGruppeIds, Locale.getDefault());

		FileOutputStream fos = new FileOutputStream(path);
		fos.write(pdf);
		fos.close();
	}

	public static void exportPdfSchiedsrichter(String path,
			Tournament turnierBO, Vector<Long> selectedGruppeIds)
			throws Exception {
		FileOutputStream fos = new FileOutputStream(path);

		byte[] pdf = XmlRemote.getSchiedsrichterPDFByTurnierid(turnierBO
				.getId(), selectedGruppeIds, Locale.getDefault());

		fos.write(pdf);
		fos.close();
	}

	public static void exportPdfSpielbericht(String path, Tournament turnierBO,
			Vector<Long> selectedGruppeIds) throws Exception {
		FileOutputStream fos = new FileOutputStream(path);

		byte[] pdf = XmlRemote.getSpielberichtPDFByTurnierid(turnierBO.getId(),
				selectedGruppeIds, Locale.getDefault());

		fos.write(pdf);
		fos.close();
	}

	public static void exportPdfGruppen(String path, Tournament turnierBO,
			Vector<Long> selectedGruppeIds) throws Exception {
		FileOutputStream fos = new FileOutputStream(path);

		/**
		 * //////////////////// String xml = HomeGetter .getXmlSessionHome()
		 * .create() .getGruppenPDFByTurnieridFOO( turnierBO.getId(),
		 * dlg.getSelectedGruppeIds()); System.out.println (xml); // Transform
		 * to fo and make pdf File in2 = new File
		 * ("c://home//jvolley//resources//gruppen.xsl"); String xsl = "";
		 * 
		 * FileReader in = new FileReader (in2);
		 * 
		 * char buffer[] = new char[5000]; int len = in.read(buffer, 0, 5000);
		 * while (len > 0) { xsl += new String(buffer, 0, len); len =
		 * in.read(buffer, 0, 5000); } in.close();
		 * 
		 * ByteArrayOutputStream fos2 = new ByteArrayOutputStream();
		 * 
		 * Driver driver = new Driver(); driver.setOutputStream(fos2);
		 * driver.setRenderer(Driver.RENDER_PDF);
		 * 
		 * Transformer transformer =
		 * TransformerFactory.newInstance().newTransformer( new StreamSource(new
		 * StringReader(xsl))); transformer.transform( new StreamSource(new
		 * StringReader(xml)), new SAXResult(driver.getContentHandler()));
		 * 
		 * byte[] pdf = fos2.toByteArray();
		 * 
		 * ///////////////////
		 */
		byte[] pdf = XmlRemote.getGruppenPDFByTurnierid(turnierBO.getId(),
				selectedGruppeIds, Locale.getDefault());

		fos.write(pdf);
		fos.close();
	}

	public static void exportHtml(File file, Tournament turnierBO) {
		// Sprache umstellen
		// Language-Seite nicht mit speichern
		Vector<String> notUsed = new Vector<String>();
		notUsed.add("languagede.html");
		notUsed.add("languageen.html");
		notUsed.add("languagedu.html");
		notUsed.add("languagenl.html");
		notUsed.add("languageru.html");
		notUsed.add("languagefr.html");
		notUsed.add("languagees.html");

		Map<String, String> referer = new HashMap<String, String>();
		ExportHtml
				.exportHtmlFile(
						file,
						"turnier.do?locale=" + Locale.getDefault() + "&linkid=" + turnierBO.getLinkid(), //$NON-NLS-1$
						"turnier.html", referer, notUsed);
	}

}
