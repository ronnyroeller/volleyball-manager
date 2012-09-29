package com.sport.server.license;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.RSAPublicKeySpec;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.sport.core.bo.LicenceBO;
import com.sport.core.helper.ProductInfo;

/**
 * @author ronny
 * 
 *         To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Generation - Code and Comments
 */
public class Licence {

	private static final Logger LOG = Logger.getLogger(Licence.class);

	// new license file location
	public static final File PROPERTIES_FILE_NAME = new File(
			"../license/license.properties");
	// new directory name, but old file
	public static final File PROPERTIES_FILE_NAME_OLD = new File(
			"../license/licence.properties");
	// old directory name, but new file
	public static final File PROPERTIES_FILE_NAME_OLD2 = new File(
			"../licence/license.properties");
	// old directory and old file
	public static final File PROPERTIES_FILE_NAME_OLD3 = new File(
			"../licence/licence.properties");

	/**
	 * Checks if licence file is valid and give all values back
	 * 
	 * @return
	 */
	public static LicenceBO getLicenceBO() {
		String buildTime = ProductInfo.getInstance().getBuildTime();

		return getLicenceBO(Arrays.asList(PROPERTIES_FILE_NAME_OLD,
				PROPERTIES_FILE_NAME, PROPERTIES_FILE_NAME_OLD2,
				PROPERTIES_FILE_NAME_OLD3), buildTime);
	}

	/**
	 * Checks a list of license files if any of them is valid
	 * 
	 * @param licenseFiles
	 *            List of files, in which license is checked
	 * @param buildTime
	 *            Time when the Volleyball Manager was built (for expiration
	 *            check), e.g. 4057130783178357368369108085728
	 * @return
	 */
	protected static LicenceBO getLicenceBO(List<File> licenseFiles,
			String buildTime) {
		LicenceBO licenceBO = new LicenceBO();
		licenceBO.licencetype = LicenceBO.LICENCE_DEMO;
		licenceBO.licencedate = new Date();
		Date date;
		long loadedLicenceKey;
		String signature;

		Properties props = new Properties();
		try {
			// try to read first new license file, otherwise try old place
			// look first for old file in new directory => compatibility with
			// old customers
			boolean isFileFound = false;
			for (File licenseFile : licenseFiles) {
				try {
					props.load(new FileInputStream(licenseFile));
					isFileFound = true;
				} catch (FileNotFoundException e) {
					continue;
				}
			}

			// If no file is found -> return demo license
			if (!isFileFound) {
				StringBuffer files = new StringBuffer();
				for (File licenseFile : licenseFiles)
					files.append(licenseFile.getCanonicalPath()).append(", ");

				LOG
						.error("Can't read license file from any of these locations: "
								+ files);

				return licenceBO;
			}

			licenceBO.firstname = props.getProperty("firstname");
			licenceBO.lastname = props.getProperty("lastname");
			licenceBO.organisation = props.getProperty("organisation");
			licenceBO.city = props.getProperty("city");
			licenceBO.country = props.getProperty("country");
			licenceBO.licencetype = props.getProperty("licencetype");
			loadedLicenceKey = Long.valueOf(props.getProperty("licencekey_1"))
					.longValue();
			date = new Date(Long.valueOf(props.getProperty("licencekey_2"))
					.longValue());
			// ATTENSION: DESC CHANGED!!!

			signature = props.getProperty("licencekey_3");

			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			licenceBO.licencedate = formatter.parse(props
					.getProperty("licencedate"));

			if (!checkSignature(licenceBO, loadedLicenceKey, date.getTime(),
					signature)) {
				// Don't log wrong signature since the standard shipped license
				// file has a wrong signature (to complicated cracking the
				// software)
				licenceBO.licencetype = LicenceBO.LICENCE_DEMO;

				return licenceBO;
			}

			// COPY FROM HERE ///
			// hash
			int hashFirstname = licenceBO.firstname.hashCode() % 731;
			int hashFirstnameMul = licenceBO.firstname.hashCode() % 16637;
			int hashLastname = licenceBO.lastname.hashCode() % 3757;
			int hashOrganisation = licenceBO.organisation.hashCode() % 28473;
			int hashCity = licenceBO.city.hashCode() % 847593;
			int hashCountry = licenceBO.country.hashCode() % 5284472;
			int hashLicencedate = licenceBO.licencedate.hashCode() % 68573393;
			int hashLicencetype = licenceBO.licencetype.hashCode() % 428579477;

			Date useDate = new Date(date.getTime() + hashFirstname);
			int hashUseDate = useDate.hashCode();

			long licenceKey = hashFirstname ^ hashLastname ^ hashOrganisation
					^ hashCity ^ hashCountry ^ hashLicencedate
					^ hashLicencetype;

			licenceKey *= hashFirstnameMul;
			licenceKey ^= hashUseDate;
			// COPY TO HERE ///

			// wrong licencekey?
			if (loadedLicenceKey == 0
					|| licenceKey == 0
					|| loadedLicenceKey != licenceKey
					|| (!LicenceBO.LICENCE_PROFI.equals(licenceBO.licencetype)
							&& !LicenceBO.LICENCE_STANDARD
									.equals(licenceBO.licencetype) && !LicenceBO.LICENCE_DEMO
							.equals(licenceBO.licencetype))) {
				licenceBO.licencetype = LicenceBO.LICENCE_DEMO;
			}

			if (buildTime.length() == 31) {
				String y = buildTime.substring(22, 24);
				String m = buildTime.substring(25, 27);
				String d = buildTime.substring(10, 12);
				if (new SimpleDateFormat("yyyy-MM-dd").parse(
						"20" + y + "-" + m + "-" + d).getTime() > licenceBO.licencedate
						.getTime()) { // <- set this in ant process
					LOG.info("License expired on " + licenceBO.licencedate
							+ ". Please purchase a new license.");
					licenceBO.licencetype = LicenceBO.LICENCE_DEMO;
				}
			} else {
				licenceBO.licencetype = LicenceBO.LICENCE_DEMO;

				LOG.error("Build time is invalid: '" + buildTime + "'");
			}

		} catch (FileNotFoundException e) {
			licenceBO.licencetype = LicenceBO.LICENCE_DEMO;
			LOG.error("Can't read license file (1)", e);
		} catch (java.text.ParseException e) {
			licenceBO.licencetype = LicenceBO.LICENCE_DEMO;
			LOG.error("Can't read license file (2)");
		} catch (IOException e) {
			licenceBO.licencetype = LicenceBO.LICENCE_DEMO;
			LOG.error("Can't read license file (3)");
		}

		return licenceBO;
	}

	/**
	 * Checks Signature!
	 * 
	 * @param licenceBO
	 * @param loadedLicenceKey
	 * @param dateLong
	 * @param signature
	 * @return
	 */
	private static boolean checkSignature(LicenceBO licenceBO,
			long loadedLicenceKey, long dateLong, String signature) {
		String toSign = licenceBO.firstname;
		toSign += licenceBO.lastname;
		toSign += licenceBO.organisation;
		toSign += licenceBO.city;
		toSign += licenceBO.country;
		toSign += licenceBO.licencedate;
		toSign += licenceBO.licencetype;
		toSign += loadedLicenceKey;
		toSign += dateLong;

		try {
			BigInteger mModulus = new BigInteger(
					"B344356FE5EDB9C77CC40CBDC002D64088CCAB66822CB1EC2CF4436665A2CE59003E6DD7037E0EB21879A50A1BB5E66260E894A1316049A9E20D5FBE518DB86B62A3E5C7EF8F4A5BBAE6CF41A13D2E0FA0B56B7E14B61A56760A00DDA945028828B45A3734259626603D2E269A453F91E3DF82E4229BA7C0DF4DDBCA897038FBC9DB6CB5BCF5B2835A082569ED19EE2BE8508DD9720395B38E8993EE35A2672733B6C6268FCA8EEB8DED32A39A31F694A5E7BDDB26731FC7E754D2CC4364D6A3E22B2CA50608305AB490CAE5EBAFD39D93917469B8EE455881325D6BB789187961D418254BD124CB2BACABCF78398A5AF54D7899D7A9D5F796C64A4E2C6D6C2B",
					16);
			BigInteger mExponent = new BigInteger("10001", 16);
			RawRSAKey rawKey = new RawRSAKey(mModulus, mExponent);
			RSAPublicKeySpec publicSpec = new RSAPublicKeySpec(rawKey
					.getModulus(), rawKey.getExponent());
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey publicKey = keyFactory.generatePublic(publicSpec);
			Signature signer = Signature.getInstance("MD5withRSA");
			signer.initVerify(publicKey);

			signer.update(toSign.getBytes());

			byte[] signatureBytes = new BigInteger(signature, 16).toByteArray();
			if (signer.verify(signatureBytes)) {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}
}
