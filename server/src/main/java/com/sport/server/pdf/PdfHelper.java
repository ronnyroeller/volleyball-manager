package com.sport.server.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Locale;
import java.util.Vector;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.log4j.Logger;

import com.sport.core.bo.LicenceBO;
import com.sport.core.bo.TournamentHolder;
import com.sport.server.ejb.session.XmlSessionBean;
import com.sport.server.license.Licence;
import com.sport.server.persistency.ejb.managers.PersistencyManager;

/**
 * Helper message for PDF generation
 * 
 * @author Ronny
 * 
 */
public class PdfHelper {

	private static final Logger LOG = Logger.getLogger(PdfHelper.class);

	/**
	 * Calls FOP to generate PDF from the XML
	 * 
	 * @param xml
	 * @param formatXsl
	 * @return
	 * @throws IOException
	 * @throws FOPException
	 * @throws TransformerException
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 */
	protected static byte[] runFop(String xml, String formatXsl)
			throws IOException, FOPException, TransformerException {
		// Transform to fo and make pdf
		InputStream in = XmlSessionBean.class.getResourceAsStream("/resources/"
				+ formatXsl);
		
		if (in == null)
			throw new IllegalStateException("Can't find resource '"+formatXsl+"'");
		
		String xsl = "";

		byte buffer[] = new byte[5000];
		int len = in.read(buffer, 0, 5000);
		while (len > 0) {
			xsl += new String(buffer, 0, len);
			len = in.read(buffer, 0, 5000);
		}
		in.close();

		ByteArrayOutputStream fos = new ByteArrayOutputStream();

		FopFactory fopFactory = FopFactory.newInstance();
		Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, fos);

		TransformerFactory tranformerFactory = TransformerFactory.newInstance();
		// Set resolver to find the xsd:import XSL's in the war file (otherwise
		// FOP is looking to the work directory of the application)
		tranformerFactory.setURIResolver(new URIResolver() {
			public Source resolve(String href, String base) {
				return new StreamSource(PdfHelper.class
						.getResourceAsStream("/resources/" + href));
			}
		});

		Transformer transformer = tranformerFactory
				.newTransformer(new StreamSource(new StringReader(xsl)));

		StreamSource src = new StreamSource(new StringReader(xml));
		SAXResult res = new SAXResult(fop.getDefaultHandler());
		transformer.transform(src, res);

		return fos.toByteArray();
	}

	/**
	 * Creates FOP-XML and then generates PDF
	 * @param turnierid
	 * @param selGruppenIds
	 * @param locale
	 * @param formatXsl
	 * @return
	 */
	public static byte[] generatePdf(long tournamentId, Vector<Long> selGruppenIds,
			Locale locale, String formatXsl) {
		try {
			TournamentHolder tournamentHolder = new PersistencyManager().findByTournamentId(tournamentId);
			
			LicenceBO licenceBO = Licence.getLicenceBO();
			String xml = new FopXmlGenerator().createXml(licenceBO, tournamentHolder,
					selGruppenIds, locale);

			try {
				byte[] pdf = PdfHelper.runFop(xml, formatXsl);

				return pdf;
			} catch (TransformerException e) {
				LOG.error("Couldn't generate FOP object for XML: '"
						+ xml + "'.", e);
			}
		} catch (Exception e) {
			LOG.error("Couldn't generate FOP-XML for PDF generation.", e);
		}
		return null;
	}

}
