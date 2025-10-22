package com.biit.liferay.security;

/*-
 * #%L
 * Liferay Authentication Client with Web Services
 * %%
 * Copyright (C) 2013 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.liferay.access.exceptions.NotValidPasswordException;
import com.biit.liferay.configuration.LiferayConfigurationReader;
import com.biit.liferay.log.LiferayClientLogger;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class AccessUtils {
    private static final Integer MIN_PASSWORD_LENGTH = 3;
    private static final Integer MAX_PASSWORD_LENGTH = 25;
    private static final String PASSWORD_ALLOWED_CHARS = "a-zA-Z0-9!()*_-";

    private static final String PASSWORD_PATTERN = "^[" + PASSWORD_ALLOWED_CHARS + "]{" + MIN_PASSWORD_LENGTH + ","
            + MAX_PASSWORD_LENGTH + "}$";

    private AccessUtils() {

    }

    /**
     * Check if a password is valid. It is valid if it not uses any special
     * character that will cause problems when connection to the Liferay server
     * (characters not allowed in URL).
     *
     * @param password
     * @throws NotValidPasswordException
     */
    public static void checkPassword(String password) throws NotValidPasswordException {
        final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        final Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            throw new NotValidPasswordException("Password used to connect to Lifera is not valid. Only characters '"
                    + PASSWORD_ALLOWED_CHARS + "' allowed and password length must be in range [" + MIN_PASSWORD_LENGTH
                    + "," + MAX_PASSWORD_LENGTH + "].");
        }
    }

    /**
     * Get the URL Liferay SOAP Service
     *
     * @param remoteUser  user with access to liferay
     * @param password    password of the user
     * @param serviceName name of the webservice.
     * @return
     */
    protected static URL getLiferayUrl(String remoteUser, String password, String serviceName) {
        try {
            checkPassword(password);
            final URL url = new URL(LiferayConfigurationReader.getInstance().getLiferayProtocol() + "://"
                    + URLEncoder.encode(remoteUser, "UTF-8") + ":" + password + "@"
                    + LiferayConfigurationReader.getInstance().getHost() + ":"
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
