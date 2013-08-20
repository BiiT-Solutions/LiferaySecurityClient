package com.biit.liferay.configuration;

import java.io.IOException;
import java.util.Properties;

import com.biit.utils.file.PropertiesFile;

public class ConfigurationReader {
	private final String LIFERAY_CONFIG_FILE = "liferay.conf";

	private static final String USER_TAG = "user";
	private static final String PASSWORD_TAG = "password";
	private static final String VIRTUAL_HOST_TAG = "virtualhost";
	private static final String WEBAPP_TAG = "webapp";
	private static final String PORT_TAG = "port";

	private final String DEFAULT_USER = "user";
	private final String DEFAULT_PASSWORD = "pass";
	private final String DEFAULT_VIRTUAL_HOST = "localhost";
	private final String DEFAULT_LIFERAY_WEBAPP = "lportal-6.1.1";
	private final String DEFAULT_PORT = "8080";

	private String user;
	private String password;
	private String virtualhost;
	private String webappName;
	private String connectionport;

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
			virtualhost = prop.getProperty(VIRTUAL_HOST_TAG);
			webappName = prop.getProperty(WEBAPP_TAG);
			connectionport = prop.getProperty(PORT_TAG);
		} catch (IOException e) {
			
		} catch (NullPointerException e) {

		}

		if (user == null) {
			user = DEFAULT_USER;
		}
		if (password == null) {
			password = DEFAULT_PASSWORD;
		}

		if (virtualhost == null) {
			virtualhost = DEFAULT_VIRTUAL_HOST;
		}

		if (connectionport == null) {
			connectionport = DEFAULT_PORT;
		}

		if (webappName == null) {
			webappName = DEFAULT_LIFERAY_WEBAPP;
		}
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public String getVirtualHost() {
		return virtualhost;
	}

	public String getConnectionPort() {
		return connectionport;
	}

	public String getWebAppName() {
		return webappName;
	}
}
