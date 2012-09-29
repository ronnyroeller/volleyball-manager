package com.sport.server.license;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Arrays;

import org.junit.Test;

import com.sport.core.bo.LicenceBO;

public class LicenceTest {

	private static final String BUILD_TIME = "4057130783178357368369108085728";

	/**
	 * Base path to license files
	 */
	private static final String BASE_PATH = "src/test/resources/"
			+ LicenceTest.class.getName() + "/";

	@Test
	public void testGetLicenceBO() {
		LicenceBO unregisteredLicense = Licence.getLicenceBO(Arrays
				.asList(new File(BASE_PATH + "unregistered.properties")),
				BUILD_TIME);
		assertEquals("Unregistered license wasn't detected",
				LicenceBO.LICENCE_DEMO, unregisteredLicense.getLicencetype());

		LicenceBO expiredLicense = Licence.getLicenceBO(Arrays.asList(new File(
				BASE_PATH + "expired.properties")), BUILD_TIME);
		assertEquals("Expired license wasn't detected", LicenceBO.LICENCE_DEMO,
				expiredLicense.getLicencetype());

		LicenceBO standardLicense = Licence.getLicenceBO(Arrays
				.asList(new File(BASE_PATH + "standard.properties")),
				BUILD_TIME);
		assertEquals("Expired license wasn't detected",
				LicenceBO.LICENCE_STANDARD, standardLicense.getLicencetype());

		LicenceBO profiLicense = Licence.getLicenceBO(Arrays.asList(new File(
				BASE_PATH + "profi.properties")), BUILD_TIME);
		assertEquals("Expired license wasn't detected",
				LicenceBO.LICENCE_PROFI, profiLicense.getLicencetype());

		LicenceBO tooLongBuildTime = Licence.getLicenceBO(Arrays.asList(new File(
				BASE_PATH + "profi.properties")), BUILD_TIME + "a");
		assertEquals("Too long build time wasn't handled",
				LicenceBO.LICENCE_DEMO, tooLongBuildTime.getLicencetype());
		
		LicenceBO tooShortBuildTime = Licence.getLicenceBO(Arrays.asList(new File(
				BASE_PATH + "profi.properties")), BUILD_TIME.substring(0,
				BUILD_TIME.length() - 1));
		assertEquals("Too short build time wasn't handled",
				LicenceBO.LICENCE_DEMO, tooShortBuildTime.getLicencetype());
	}

}
