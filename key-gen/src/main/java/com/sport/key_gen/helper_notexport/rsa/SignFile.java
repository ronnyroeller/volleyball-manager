package com.sport.key_gen.helper_notexport.rsa;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;

import com.sport.server.license.RawRSAKey;


/**
 * This is a command line utility that accepts an RSA key file name and
 * a file name on the command line that it uses to create a signed version of 
 * the file using the key file as the source for the RSA private key.
 */
public class SignFile {
   public SignFile () {
   }
   
   public static void main(String[] args) throws java.io.IOException {
		RawRSAKey rawKey = RawRSAKey.getInstance("src/jvolley/helper_notexport/private_raw.txt");
		RSAPrivateKeySpec privateSpec = new RSAPrivateKeySpec(rawKey.getModulus(), rawKey.getExponent());
		
		KeyFactory keyFactory = null;
		PrivateKey privateKey = null;
		Signature signer = null;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
			privateKey = keyFactory.generatePrivate(privateSpec);
			signer = Signature.getInstance("MD5withRSA");
			signer.initSign(privateKey);
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
		int numberBytesRead = 0;
		do {
			if ( numberBytesRead > 0 ) {
				try {
					signer.update(fileData, 0, numberBytesRead);
				}
				catch (SignatureException signError) {
					System.out.println(signError);
					return;
				}
			}
			numberBytesRead = fileIn.read(fileData);
		} while ( numberBytesRead != -1 );
		fileIn.close();
		
		byte[] theSignature;
		try {
			theSignature = signer.sign();
		}
		catch (SignatureException signError) {
			System.out.println(signError);
			return;
		}
	
		FileOutputStream rawOut = new FileOutputStream("src/jvolley/helper_notexport/rsa/tosign.sig");
		rawOut.write(theSignature);
		rawOut.close();	
		
		FileWriter textOut = new FileWriter("src/jvolley/helper_notexport/rsa/hexsignature.txt");
		textOut.write(new BigInteger(theSignature).toString(16).toUpperCase());
		textOut.close();
		
		System.out.println("rawsignature.sig and hexsignature.txt files created");
		System.out.println("for the file (tosign)");
   }
}
