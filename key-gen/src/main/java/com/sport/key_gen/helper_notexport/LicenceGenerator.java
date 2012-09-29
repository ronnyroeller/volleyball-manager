/*
 * Created on 27.01.2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package com.sport.key_gen.helper_notexport;

import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.RSAPrivateKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import com.sport.core.bo.LicenceBO;
import com.sport.server.license.RawRSAKey;


/**
 * @author ronny
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class LicenceGenerator {

	/**
	 * Write Licence-File
	 * 
	 * @return
	 */
	public static void writeValid(LicenceBO licenceBO)
		throws Exception {
		Date date = new Date();
		long licenceKey;

		// make simple licencedate
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		licenceBO.licencedate = formatter.parse(formatter.format(licenceBO.licencedate));

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

		licenceKey =
			hashFirstname
				^ hashLastname
				^ hashOrganisation
				^ hashCity
				^ hashCountry
				^ hashLicencedate
				^ hashLicencetype;

		licenceKey *= hashFirstnameMul;
		licenceKey ^= hashUseDate;
		// COPY TO HERE ///

		// Sign everything
		String signature = signLicenceBO(licenceBO, licenceKey, date.getTime());
		
		Properties props = new Properties();
		props.setProperty("firstname", licenceBO.firstname);
		props.setProperty("lastname", licenceBO.lastname);
		props.setProperty("organisation", licenceBO.organisation);
		props.setProperty("city", licenceBO.city);
		props.setProperty("country", licenceBO.country);
		props.setProperty("licencedate", formatter.format(licenceBO.licencedate));
		props.setProperty("licencetype", licenceBO.licencetype);
		props.setProperty("licencekey_1", String.valueOf(licenceKey));
		props.setProperty("licencekey_2", String.valueOf(date.getTime()));
		props.setProperty("licencekey_3", signature);
		// ATTENSION: DESC CHANGED!!!
		props.store(
			new FileOutputStream("license.properties"),null);
	}

	/**
	 * signs a LicenceBO with private key
	 */
	private static String signLicenceBO(LicenceBO licenceBO, long licenceKey, long dateLong) throws Exception {
		String toSign = licenceBO.firstname;
		toSign += licenceBO.lastname;
		toSign += licenceBO.organisation;
		toSign += licenceBO.city;
		toSign += licenceBO.country;
		toSign += licenceBO.licencedate;
		toSign += licenceBO.licencetype;
		toSign += licenceKey;
		toSign += dateLong;
		
		return signString(toSign);
	}
	
	/**
	 * signs a String with private key
	 */
	private static String signString(String toSign) throws Exception {
		BigInteger mModulus =
		new BigInteger(
				"B344356FE5EDB9C77CC40CBDC002D64088CCAB66822CB1EC2CF4436665A2CE59003E6DD7037E0EB21879A50A1BB5E66260E894A1316049A9E20D5FBE518DB86B62A3E5C7EF8F4A5BBAE6CF41A13D2E0FA0B56B7E14B61A56760A00DDA945028828B45A3734259626603D2E269A453F91E3DF82E4229BA7C0DF4DDBCA897038FBC9DB6CB5BCF5B2835A082569ED19EE2BE8508DD9720395B38E8993EE35A2672733B6C6268FCA8EEB8DED32A39A31F694A5E7BDDB26731FC7E754D2CC4364D6A3E22B2CA50608305AB490CAE5EBAFD39D93917469B8EE455881325D6BB789187961D418254BD124CB2BACABCF78398A5AF54D7899D7A9D5F796C64A4E2C6D6C2B",
				16);
		BigInteger mExponent =
		new BigInteger(
				"875A69EA9A35FB7ED702840845511F0A4D00142E6AFD6A3DC3D78E886317212C6C15B703909C7E66143ACCD52C0514F594EC2744EA2E249083A0CB78BCC4FFC8FD0A793593F8A2114EB67528BDA09F05A7B7337958140320E3313C6421297CF6E79CFD3D0F3F8DF65DD5F3E1ABBBBB7AE10E631AF3D28940049B19E7FC0BE2C95E9C831E7E8E5183EA758D31A0379A98C5F8E193483D08E29036339A71BACFD262FDD18B6BF7F3BB2011B6CA08E6FF5C6363ACD76890A5C4D87E4641B0C6A709F60AFFC047824A9CB1990F43522BFE6F3498D00E5A1C8F7B39052E794CD2A97C3A50B8DD26E02FB238DBAB0B16AEF3E0F14F117F1A7BEBA4EBD9F052E07E8549",
				16);
		RawRSAKey rawKey = new RawRSAKey(mModulus, mExponent);
		RSAPrivateKeySpec privateSpec =
		new RSAPrivateKeySpec(rawKey.getModulus(), rawKey.getExponent());

		KeyFactory keyFactory = null;
		PrivateKey privateKey = null;
		Signature signer = null;
		keyFactory = KeyFactory.getInstance("RSA");
		privateKey = keyFactory.generatePrivate(privateSpec);
		signer = Signature.getInstance("MD5withRSA");
		signer.initSign(privateKey);

		signer.update(toSign.getBytes());
		return (new BigInteger(signer.sign()).toString(16).toUpperCase()).toString();
	}
}
