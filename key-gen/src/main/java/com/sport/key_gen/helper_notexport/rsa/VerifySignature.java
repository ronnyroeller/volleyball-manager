package com.sport.key_gen.helper_notexport.rsa;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

import com.sport.server.license.RawRSAKey;

/**
 * This is a command line utility that accepts an RSA key file name,
 * a file name and a signature file name on the command line that are
 * used to verify that the signature is for the file and supplied
 * public key.
 */
public class VerifySignature {
	/**
	 * No instance of SignFile is actually created.
	 */
   public VerifySignature () {
   }
   
   public static void main(String[] args) throws java.io.IOException {

		RawRSAKey rawKey = RawRSAKey.getInstance("src/jvolley/helper_notexport/rsa/public_raw.txt");
		RSAPublicKeySpec publicSpec = new RSAPublicKeySpec(rawKey.getModulus(), rawKey.getExponent());

		KeyFactory keyFactory = null;
		PublicKey publicKey = null;
		Signature signer = null;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
			publicKey = keyFactory.generatePublic(publicSpec);
			signer = Signature.getInstance("MD5withRSA");
			signer.initVerify(publicKey);
		}
		catch(NoSuchAlgorithmException noAlgorithm) {
			System.out.println(noAlgorithm);
			return;
		}
		catch(InvalidKeySpecException badSpec) {
			System.out.println(badSpec);
			return;
		}
		catch(InvalidKeyException badKey) {
			System.out.println(badKey);
			return;
		}

		FileInputStream fileIn = new FileInputStream("src/jvolley/helper_notexport/rsa/tosign.txt");
		byte[] fileData = new byte[100];
		int numberFileBytesRead = 0;
		do {
			if ( numberFileBytesRead > 0 ) {
				try {
					signer.update(fileData, 0, numberFileBytesRead);
				}
				catch (SignatureException signError) {
					System.out.println(signError);
					return;
				}
			}
			numberFileBytesRead = fileIn.read(fileData);
		} while ( numberFileBytesRead != -1 );
		fileIn.close();
		
		ByteArrayOutputStream theSignature = new ByteArrayOutputStream(2000);
		FileInputStream signatureIn = new FileInputStream("src/jvolley/helper_notexport/rsa/tosign.sig");
		byte[] signatureData = new byte[100];
		int numberSignatureBytesRead = 0;
		do {
			if ( numberSignatureBytesRead > 0 ) {
				theSignature.write(signatureData, 0, numberSignatureBytesRead);
			}
			numberSignatureBytesRead = signatureIn.read(signatureData);
		} while ( numberSignatureBytesRead != -1 );
		signatureIn.close();

		
		boolean signatureVerified = false;
		try {
			signatureVerified = signer.verify(theSignature.toByteArray());
		}
		catch (SignatureException signError) {
			System.out.println(signError);
			return;
		}
		
		System.out.println("===========================================================");
		if ( signatureVerified ) {
			System.out.println("The signature is valid for the file.");
		}
		else {
			System.out.println("NOT A VALID SIGNATURE FOR THE FILE!");
		}
		System.out.println("-----------------------------------------------------------");
		System.out.println("      Public Key: public_raw.txt");
		System.out.println("            File: tosign.txt");
		System.out.println("       Signature: tosign.sig");
		System.out.println("Signature Length: " + theSignature.toByteArray().length);
		System.out.println("===========================================================");
   }
}
