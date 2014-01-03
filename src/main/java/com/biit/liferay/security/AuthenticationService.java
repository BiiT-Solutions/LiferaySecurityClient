package com.biit.liferay.security;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import com.biit.liferay.access.CompanyService;
import com.biit.liferay.access.UserGroupService;
import com.biit.liferay.access.UserService;
import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.UserDoesNotExistException;
import com.biit.liferay.access.exceptions.WebServiceAccessError;
import com.biit.liferay.configuration.ConfigurationReader;
import com.biit.liferay.log.LiferayClientLogger;
import com.biit.liferay.security.exceptions.InvalidCredentialsException;
import com.biit.liferay.security.exceptions.LiferayConnectionException;
import com.biit.liferay.security.exceptions.PasswordEncryptorException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;

public class AuthenticationService {
	private final static AuthenticationService instance = new AuthenticationService();

	private AuthenticationService() {
		// Connect if it is not connected the fist time used.
		if (UserService.getInstance().isNotConnected()) {
			UserService.getInstance().serverConnection();
		}
		if (CompanyService.getInstance().isNotConnected()) {
			CompanyService.getInstance().serverConnection();
		}
		// Connect if not connected the fist time used.
		if (UserGroupService.getInstance().isNotConnected()) {
			UserGroupService.getInstance().serverConnection();
		}

		// LiferayClientLogger
		// .fatal(AuthenticationService.class.getName(),
		// "Cannot connect to UserService and/or CompanyService. Please, configure the file 'liferay.conf' correctly.");
	}

	public static AuthenticationService getInstance() {
		return instance;
	}

	/**
	 * Obtains a liferay UserSoap by the mail and the password.
	 * 
	 * @param userMail
	 * @param password
	 * @return
	 * @throws InvalidCredentialsException
	 * @throws ServiceException
	 * @throws NotConnectedToWebServiceException
	 * @throws LiferayConnectionException
	 * @throws PasswordEncryptorException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @throws AuthenticationRequired 
	 * @throws WebServiceAccessError 
	 */
	public User authenticate(String userMail, String password) throws InvalidCredentialsException,
			NotConnectedToWebServiceException, LiferayConnectionException, PasswordEncryptorException,
			JsonParseException, JsonMappingException, ClientProtocolException, IOException, AuthenticationRequired, WebServiceAccessError {
		// Login fails if either the username or password is null
		if (userMail == null || password == null) {
			throw new InvalidCredentialsException("No fields filled up.");
		}

		User UserSoap = null;
		try {
			UserSoap = UserService.getInstance().getUserByEmailAddress(
					CompanyService.getInstance().getCompanyByVirtualHost(
							ConfigurationReader.getInstance().getVirtualHost()), userMail);
		} catch (RemoteException e) {
			if (e.getLocalizedMessage().contains("No User exists with the key")) {
				LiferayClientLogger.warning(this.getClass().getName(), "Attempt to loggin failed with user '"
						+ userMail + "'.");
				throw new InvalidCredentialsException("User does not exist.");
			} else if (e.getLocalizedMessage().contains("Connection refused: connect")) {
				throw new LiferayConnectionException("Error connecting to Liferay service with '"
						+ ConfigurationReader.getInstance().getUser() + "@"
						+ ConfigurationReader.getInstance().getVirtualHost() + ":"
						+ ConfigurationReader.getInstance().getConnectionPort()
						+ "'.\n Check configuration at 'liferay.conf' file");
			} else {
				LiferayClientLogger.errorMessage(this.getClass().getName(), e);
				throw e;
			}
		}

		if (ConfigurationReader.getInstance().getPasswordEncryptationAlgorithm()
				.equals(PasswordEncryptationAlgorithmType.PBKDF2)) {
			new PBKDF2PasswordEncryptor().validate(password, UserSoap.getPassword());
		} else {
			BasicEncryptionMethod.getInstance().validate(password, UserSoap.getPassword());
		}

		LiferayClientLogger.info(this.getClass().getName(), "Access granted to user '" + userMail + "'");
		return UserSoap;
	}

	public boolean isInGroup(UserGroup group, User UserSoap) throws NotConnectedToWebServiceException,
			ClientProtocolException, IOException, AuthenticationRequired {
		if (group != null && UserSoap != null) {
			List<UserGroup> userGroups = UserGroupService.getInstance().getUserUserGroups(UserSoap);
			for (UserGroup UserGroupSoap : userGroups) {
				if (UserGroupSoap.getUserGroupId() == group.getUserGroupId()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Get first group of UserSoap.
	 * 
	 * @param UserSoap
	 * @return
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws AuthenticationRequired 
	 */
	public UserGroup getDefaultGroup(User UserSoap) throws NotConnectedToWebServiceException, ClientProtocolException,
			IOException, AuthenticationRequired {
		if (UserSoap != null) {
			List<UserGroup> userGroups = UserGroupService.getInstance().getUserUserGroups(UserSoap);
			if (userGroups != null && userGroups.size() > 0) {
				return userGroups.get(0);
			}
		}
		return null;
	}

	public User getUserById(long userId) throws NotConnectedToWebServiceException, UserDoesNotExistException,
			ClientProtocolException, IOException, AuthenticationRequired, WebServiceAccessError {
		return UserService.getInstance().getUserById(userId);
	}

	public void updatePassword(User UserSoap, String plainTextPassword) throws NotConnectedToWebServiceException,
			PasswordEncryptorException, JsonParseException, JsonMappingException, IOException, AuthenticationRequired,
			WebServiceAccessError {
		UserService.getInstance().updatePassword(UserSoap, plainTextPassword);
	}
}
