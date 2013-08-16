package com.biit.liferay.security;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import com.biit.liferay.access.CompanyService;
import com.biit.liferay.access.UserService;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.configuration.ConfigurationReader;
import com.biit.liferay.security.exceptions.InvalidCredentialsException;
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

	public User authenticate(String userName, String password) throws InvalidCredentialsException, ServiceException,
			RemoteException, NotConnectedToWebServiceException {
		// Login fails if either the username or password is null
		if (userName == null || password == null) {
			throw new InvalidCredentialsException("No fields filled up.");
		}

		User user = null;
		try {
			user = UserService.getInstance().getUserByScreenName(
					CompanyService.getInstance().getCompanyByVirtualHost(
							ConfigurationReader.getInstance().getVirtualHost()), userName);
		} catch (RemoteException e) {
			if (e.getLocalizedMessage().contains("No User exists with the key")) {
				throw new InvalidCredentialsException("User does not exist.");
			} else {
				throw e;
			}
		}

		BasicEncryptionMethod.getInstance().validatePassword(password, user.getPassword());
		return user;
	}
}
