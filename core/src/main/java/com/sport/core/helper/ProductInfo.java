package com.sport.core.helper;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Class that reads the version number from the resources file (auto injected by build process)
 * 
 * @author Ronny
 *
 */
public class ProductInfo {

	private static final Logger LOG = Logger.getLogger(ProductInfo.class);

	/**
	 * File that contains the version number
	 */
	private static final String INFO_FILE = "/info.properties";

	/**
	 * Singleton pattern
	 */
	private static ProductInfo instance;
	
	private String versionNumber = "unknown";
	private String buildTime = "unknown";

	/**
	 * Reads the version number
	 */
	private ProductInfo () {
		Properties props = new Properties();
		
		try {
			props.load(this.getClass().getResourceAsStream(INFO_FILE));
			if (props == null)
				LOG.error("Couldn't read info file '"+INFO_FILE+"'");
			else {
				versionNumber = props.getProperty("version");
				buildTime = props.getProperty("build-time");
			}
		} catch (IOException e) {
			LOG.error("Couldn't read version number");
		}
	}

	public static ProductInfo getInstance() {
		if (instance == null)
			instance = new ProductInfo();
		
		return instance;
	}
	
	/**
	 * Returns the version number of Volleyball Manager build
	 * @return
	 */
	public String getVersion() {
		return versionNumber;
	}
	
	/**
	 * Returns when the Volleyball Manager release was built
	 * @return
	 */
	public String getBuildTime() {
		return buildTime;
	}
	
}
