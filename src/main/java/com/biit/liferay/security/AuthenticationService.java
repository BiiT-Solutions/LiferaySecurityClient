package com.biit.liferay.security;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import com.biit.liferay.access.CompanyService;
import com.biit.liferay.access.UserService;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.configuration.ConfigurationReader;
import com.biit.liferay.log.LiferayAuthenticationClientLogger;
import com.biit.liferay.security.exceptions.InvalidCredentialsException;
import com.biit.liferay.security.exceptions.LiferayConnectionException;
import com.liferay.portal.model.User;

public class AuthenticationService {
	private final static AuthenticationService instance = new AuthenticationService();

	private AuthenticationService() {

	}

	public static AuthenticationService getInstance() throws ServiceException {
		// Connect if not connected the fist time used.
		if (UserService.getInstance().isNotConnected()) {
			UserService.getInstance().connectToWebService();
		}
		if (CompanyService.getInstance().isNotConnected()) {
			CompanyService.getInstance().connectToWebService();
		}
		return instance;
	}

	public User authenticate(String userMail, String password) throws InvalidCredentialsException, ServiceException,
			RemoteException, NotConnectedToWebServiceException, LiferayConnectionException {
		// Login fails if either the username or password is null
		if (userMail == null || password == null) {
			throw new InvalidCredentialsException("No fields filled up.");
		}

		User user = null;
		try {
			user = UserService.getInstance().getUserByEmailAddress(
					CompanyService.getInstance().getCompanyByVirtualHost(
							ConfigurationReader.getInstance().getVirtualHost()), userMail);
		} catch (RemoteException e) {
			if (e.getLocalizedMessage().contains("No User exists with the key")) {
				LiferayAuthenticationClientLogger.warning(this.getClass().getName(), "Attempt to loggin fail using user '" + userMail + "'.");
				throw new InvalidCredentialsException("User does not exist.");
			} else if (e.getLocalizedMessage().contains("Connection refused: connect")) {
				LiferayAuthenticationClientLogger.severe(this.getClass().getName(), "Error connecting to Liferay service. Using "
						+ ConfigurationReader.getInstance().getUser() + "@"
						+ ConfigurationReader.getInstance().getVirtualHost() + ":"
						+ ConfigurationReader.getInstance().getConnectionPort() + ".");
				throw new LiferayConnectionException("Error connecting to Liferay service. Using "
						+ ConfigurationReader.getInstance().getUser() + "@"
						+ ConfigurationReader.getInstance().getVirtualHost() + ":"
						+ ConfigurationReader.getInstance().getConnectionPort() + ".\n Check the liferay.conf file");
			} else {
				LiferayAuthenticationClientLogger.errorMessage(this.getClass().getName(), e);
				throw e;
			}
		}

		LiferayAuthenticationClientLogger.info(this.getClass().getName(), "User '" + userMail + "' exists in Liferay Portal.");
		BasicEncryptionMethod.getInstance().validatePassword(password, user.getPassword());
		LiferayAuthenticationClientLogger.info(this.getClass().getName(), "Acess granted to user '" + userMail + "'");
		return user;
	}
}
