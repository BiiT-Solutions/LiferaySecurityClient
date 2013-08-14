package com.biit.liferay.access;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class AccessUtils {
	private static final String LIFERAY_PROTOCOL = "http://";
	private static final String LIFERAY_TCP_PORT = "8080";
	private static final String LIFERAY_FQDN = "localhost";
	private static final String LIFERAY_AXIS_PATH = "/api/secure/axis/";

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
	protected static URL getURL(String remoteUser, String password, String serviceName) {
		try {
			return new URL(LIFERAY_PROTOCOL + URLEncoder.encode(remoteUser, "UTF-8") + ":"
					+ URLEncoder.encode(password, "UTF-8") + "@" + LIFERAY_FQDN + ":" + LIFERAY_TCP_PORT
					+ LIFERAY_AXIS_PATH + serviceName);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

}
