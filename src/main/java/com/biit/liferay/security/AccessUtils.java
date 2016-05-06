package com.biit.liferay.security;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.biit.liferay.access.exceptions.NotValidPasswordException;
import com.biit.liferay.configuration.LiferayConfigurationReader;
import com.biit.liferay.log.LiferayClientLogger;

public class AccessUtils {
	private static final Integer MIN_PASSWORD_LENGTH = 3;
	private static final Integer MAX_PASSWORD_LENGTH = 25;
	private static final String PASSWORD_ALLOWED_CHARS = "a-zA-Z0-9!()*_-";

	private static final String PASSWORD_PATTERN = "^[" + PASSWORD_ALLOWED_CHARS + "]{" + MIN_PASSWORD_LENGTH + ","
			+ MAX_PASSWORD_LENGTH + "}$";

	/**
	 * Check if a password is valid. It is valid if it not uses any special
	 * character that will cause problems when connection to the Liferay server
	 * (characters not allowed in URL).
	 * 
	 * @param password
	 * @return
	 * @throws NotValidPasswordException
	 */
	public static void checkPassword(String password) throws NotValidPasswordException {
		Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
		Matcher matcher = pattern.matcher(password);
		if (!matcher.matches()) {
			throw new NotValidPasswordException("Password used to connect to Lifera is not valid. Only characters '"
					+ PASSWORD_ALLOWED_CHARS + "' allowed and password length must be in range [" + MIN_PASSWORD_LENGTH
					+ "," + MAX_PASSWORD_LENGTH + "].");
		}
	}

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
			checkPassword(password);
			URL url = new URL(LiferayConfigurationReader.getInstance().getLiferayProtocol() + "://"
					+ URLEncoder.encode(remoteUser, "UTF-8") + ":" + password + "@"
					+ LiferayConfigurationReader.getInstance().getVirtualHost() + ":"
					+ LiferayConfigurationReader.getInstance().getConnectionPort() + "/"
					+ LiferayConfigurationReader.getInstance().getWebAppName()
					+ LiferayConfigurationReader.getInstance().getWebServicesPath() + serviceName);
			return url;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			LiferayClientLogger.error(AccessUtils.class.getName(), e.getLocalizedMessage());
			return null;
		} catch (NotValidPasswordException e) {
			e.printStackTrace();
			LiferayClientLogger.error(AccessUtils.class.getName(), e.getLocalizedMessage());
			return null;
		}
	}
}
