package com.goeuro.search.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author EG004950
 * @version 0.1
 * 
 */
/*
 * Utility class for loading configuration file
 */
public class PropertyLoader {

	private static final Logger LOGGER = Logger.getLogger( PropertyLoader.class.getName() );
	private static Properties prop = null;
	private static InputStream input = null;
	private static final String CONFIG_FILE_NAME = "config.properties";

	public static String get(String key) {

		try {
			if (prop == null && input == null) {
				prop = new Properties();
				input = PropertyLoader.class.getResourceAsStream(CONFIG_FILE_NAME);
				// load a properties file
				prop.load(input);
			}

			String value = prop.getProperty(key);
			LOGGER.log( Level.FINE, "Value For Key ("+key+") = "+value);
			return prop.getProperty(key);

		} catch (IOException ex) {
			ex.printStackTrace();
			LOGGER.log( Level.SEVERE, "Failed to load configuration file", ex);
			LOGGER.log( Level.SEVERE, ex.toString(), ex);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					LOGGER.log( Level.SEVERE, "Failed closing input stream", e);
					LOGGER.log( Level.SEVERE, e.toString(), e);
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
