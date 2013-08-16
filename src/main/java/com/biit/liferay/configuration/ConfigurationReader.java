package com.biit.liferay.configuration;

import java.io.IOException;
import java.util.Properties;

import com.biit.utils.file.PropertiesFile;

public class ConfigurationReader {
	private final String LIFERAY_CONFIG_FILE = "liferay.conf";
	private final String USER_TAG = "user";
	private final String PASSWORD_TAG = "password";

	private final String DEFAULT_USER = "user";
	private final String DEFAULT_PASSWORD = "pass";

	private String user;
	private String password;

	private static ConfigurationReader instance;

	private ConfigurationReader() {
		readConfig();
	}

	public static ConfigurationReader getInstance() {
		if (instance == null) {
			synchronized (ConfigurationReader.class) {
				if (instance == null) {
					instance = new ConfigurationReader();
				}
			}
		}
		return instance;
	}

	/**
	 * Read database config from resource and update default connection
	 * parameters.
	 */
	private void readConfig() {
		Properties prop = new Properties();
		try {
			prop = PropertiesFile.load(LIFERAY_CONFIG_FILE);
			user = prop.getProperty(USER_TAG);
			password = prop.getProperty(PASSWORD_TAG);
		} catch (IOException e) {
			// Do nothing.
		}

		if (user == null) {
			user = DEFAULT_USER;
		}
		if (password == null) {
			password = DEFAULT_PASSWORD;
		}
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}
}
