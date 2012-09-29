/*
 * Created on 02.07.2003
 * 
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.sport.client.export;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.apache.log4j.Logger;

import com.sport.server.ejb.HomeGetter;

/**
 * @author ronny
 */
public class ExportHtml {

	private static final Logger LOG = Logger.getLogger(ExportHtml.class);

	/**
	 * Liesst eine Url aus und schreibt diese als lokales HTML-File. Dabei
	 * werden alle Links umgeschrieben und ein Array angelegt, damit diese
	 * Dateien ebenfalls auf die lokale Platte kopiert werden k�nnen.
	 * 
	 * @param dir -
	 *            Zielverzeichnis, wohin die HTML kopiert werden sollen
	 * @param urlStr -
	 *            zu lesende Datei
	 * @param filename -
	 *            Name der lokalen Kopie
	 * @param ersetzungen -
	 *            bis jetzt ersetzte Urls
	 * @param geschriebeneDateien -
	 *            bis jetzt geschriebene Dateien
	 */
	public static void exportHtmlFile(
		File dir,
		String urlStr,
		String filename,
		Map<String, String> ersetzungen,
		Vector<String> geschriebeneDateien) {

		// ggf. Initialisieren
		if (ersetzungen == null) {
			// Speichert alle Ersetzungen
			ersetzungen = new HashMap<String, String>();
		}
		if (geschriebeneDateien == null) {
			// Speichert alle schon geschriebenen Dateien
			geschriebeneDateien = new Vector<String>();
		}

		// falls Zielverzeichnis noch nicht existiert -> anlegen
		if (!dir.exists()) {
			dir.mkdir();
		}

		byte[] fileBytes = leseDatei(urlStr);

		geschriebeneDateien.add(filename);

		// falls keine Binaerdatei -> Pfade umschreiben
		String[] binaryFile = { "jpg", "gif", "png", "pdf" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		if (!Arrays
			.asList(binaryFile)
			.contains(filename.substring(filename.length() - 3))) {
			fileBytes =
				schreibePfade(new String(fileBytes), ersetzungen).getBytes();
		}

		// Datei schreiben
		String fname = dir.getPath() + File.separatorChar + filename;
		try {
			FileOutputStream fos = new FileOutputStream(fname); //$NON-NLS-1$
			fos.write(fileBytes);
			fos.close();
		} catch (FileNotFoundException e) {
			LOG.error("Can't find file '"+fname+"' to export HTML.");
		} catch (IOException e) {
			LOG.error("Can't export HTML to file '"+fname+"'.");
		}

		// n�chste Datei bearbeiten (alle zu bearbeitenden durchsuchen, bis
		// eine noch
		// nicht geschriebene gefunden wird
		for (String replaceUrlStr : ersetzungen.keySet()) {
			filename = (String) ersetzungen.get(replaceUrlStr);
			// Datei noch nicht geschrieben?
			if (!geschriebeneDateien.contains(filename)) {
				exportHtmlFile(
					dir,
					urlStr,
					filename,
					ersetzungen,
					geschriebeneDateien);
				break;
			}
		}
	}

	public static byte[] leseDatei(String urlStr) {
		byte[] fileBytes = null;
		try {
			// skip invalid characters
			urlStr = urlStr.replaceAll("&amp;", "&");
			// HTML einlesen (ohne Sprachwahl!)
			urlStr =
				"http://"
					+ HomeGetter.getHost()
					+ ":"
					+ HomeGetter.getPort()
					+ "/"
					+ urlStr;
			if (urlStr.indexOf("?") > 0) {
				urlStr += "&nolang=true";
			} else {
				urlStr += "?nolang=true";
			}
			URL url = new URL(urlStr); //$NON-NLS-1$
			URLConnection con = url.openConnection();
			InputStream in = con.getInputStream();
			byte[] out = new byte[50000];
			int len = 0;
			while ((len = in.read(out)) >= 0) {
				if (fileBytes != null) {
					byte[] oldhtmlBytes = fileBytes;
					fileBytes = new byte[oldhtmlBytes.length + len];
					System.arraycopy(
						oldhtmlBytes,
						0,
						fileBytes,
						0,
						oldhtmlBytes.length);
					System.arraycopy(
						out,
						0,
						fileBytes,
						oldhtmlBytes.length,
						len);
				} else {
					fileBytes = new byte[len];
					System.arraycopy(out, 0, fileBytes, 0, len);
				}
			}
		} catch (MalformedURLException e) {
			LOG.error("Can't create URL for '"+urlStr+"'.");
		} catch (IOException e) {
			LOG.error("Can't read file '"+urlStr+"'.");
		}

		return fileBytes;
	}

	private static String schreibePfade(String html, Map<String, String> ersetzungen) {

		// HTML transformieren -> Links umschreiben
		Pattern p = Pattern.compile("(img src)?(=\"|url\\()(/(?:[^\"^\\)]*/)?([^/^\\?^\"\\)]*)\\.(do|jsp|css|gif|jpg);[^\"\\?\\)]*(\\?[^=]*=([^\"\\)]*))?)"); //$NON-NLS-1$
		Matcher m = p.matcher(html);

		// *.do nach html umschreiben
		while (m.find()) {
			String urlName = m.group(3); // urspr�ngliche URL
			String linkfilename = (String) ersetzungen.get(urlName);

			// ist Url noch nicht genutzt wurden? -> Ersetzung berechnen
			if (linkfilename == null) {
				linkfilename = m.group(4); // Ziel Datei
				// ggf. Parameter anfuegen
				if (m.group(7) != null) {
					linkfilename += m.group(7);
				}
				// Erweiterung bestimmen
				String ext = "html"; //$NON-NLS-1$

				String[] noScript = { "css", "jpg", "gif" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				if (Arrays.asList(noScript).contains(m.group(5))) {
					ext = m.group(5);
				} else if (m.group(1) != null) { // ist GERENDERTES Bild?
					ext = "jpg"; //$NON-NLS-1$
				} else if (linkfilename.indexOf("pdf") >= 0) { // ist pdf-Datei?
					ext = "pdf"; //$NON-NLS-1$
				}
				linkfilename += "." + ext; //$NON-NLS-1$

				// ung�ltige Zeichen filtern
				linkfilename = linkfilename.replace('%', '_');

				ersetzungen.put(urlName, linkfilename);
			}

			// alle Vorkommen in der Datei ersetzen
			int index = 0;
			while ((index = html.indexOf(urlName)) >= 0) {
				html =
					html.substring(0, index)
						+ linkfilename
						+ html.substring(index + urlName.length());
			}
		}

		return html;
	}

}
