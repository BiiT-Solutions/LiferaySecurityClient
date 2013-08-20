package com.biit.liferay.access;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import com.biit.liferay.configuration.ConfigurationReader;

public class AccessUtils {
	private static final String LIFERAY_PROTOCOL = "http://";
	private static final String LIFERAY_AXIS_PATH = "api/secure/axis/";

	/**
	 * Get the URL Liferay SOAP Service
	 * 
	 * @param remoteUser
	 *            user with access to liferay
	 * @param password
	 *            password of the user
	 * @param serviceName
	 *            name of the webservice.
	 * @return
	 */
	protected static URL getLiferayUrl(String remoteUser, String password, String serviceName) {
		try {
			return new URL(LIFERAY_PROTOCOL + URLEncoder.encode(remoteUser, "UTF-8") + ":"
					+ URLEncoder.encode(password, "UTF-8") + "@" + ConfigurationReader.getInstance().getVirtualHost()
					+ ":" + ConfigurationReader.getInstance().getConnectionPort() + "/"
					+ ConfigurationReader.getInstance().getWebAppName() + "/" + LIFERAY_AXIS_PATH + serviceName);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

}
