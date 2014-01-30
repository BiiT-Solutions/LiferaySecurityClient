package com.biit.liferay.security;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import com.biit.liferay.access.CompanyService;
import com.biit.liferay.access.ContactService;
import com.biit.liferay.access.UserGroupService;
import com.biit.liferay.access.UserService;
import com.biit.liferay.access.VerificationService;
import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.UserDoesNotExistException;
import com.biit.liferay.access.exceptions.WebServiceAccessError;
import com.biit.liferay.configuration.ConfigurationReader;
import com.biit.liferay.log.LiferayClientLogger;
import com.biit.liferay.security.exceptions.InvalidCredentialsException;
import com.biit.liferay.security.exceptions.LiferayConnectionException;
import com.biit.security.exceptions.PBKDF2EncryptorException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;

public class AuthenticationService {
	private final static AuthenticationService instance = new AuthenticationService();
	private Company company = null;

	private AuthenticationService() {
		// Connect if it is not connected the fist time used.
		if (UserService.getInstance().isNotConnected()) {
			UserService.getInstance().serverConnection();
		}
		if (CompanyService.getInstance().isNotConnected()) {
			CompanyService.getInstance().serverConnection();
		}
		if (UserGroupService.getInstance().isNotConnected()) {
			UserGroupService.getInstance().serverConnection();
		}
		if (ContactService.getInstance().isNotConnected()) {
			ContactService.getInstance().serverConnection();
		}

		// LiferayClientLogger
		// .fatal(AuthenticationService.class.getName(),
		// "Cannot connect to UserService and/or CompanyService. Please, configure the file 'liferay.conf' correctly.");
	}

	public static AuthenticationService getInstance() {
		return instance;
	}

	/**
	 * Obtains a liferay user by the mail and the password.
	 * 
	 * @param userMail
	 * @param password
	 * @return
	 * @throws InvalidCredentialsException
	 * @throws ServiceException
	 * @throws NotConnectedToWebServiceException
	 * @throws LiferayConnectionException
	 * @throws PBKDF2EncryptorException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @throws AuthenticationRequired
	 * @throws WebServiceAccessError
	 */
	public User authenticate(String userMail, String password) throws InvalidCredentialsException,
			NotConnectedToWebServiceException, LiferayConnectionException, PBKDF2EncryptorException,
			JsonParseException, JsonMappingException, ClientProtocolException, IOException, AuthenticationRequired,
			WebServiceAccessError {
		// Login fails if either the username or password is null
		if (userMail == null || password == null) {
			throw new InvalidCredentialsException("No fields filled up.");
		}

		if (company == null) {
			company = CompanyService.getInstance().getCompanyByVirtualHost(
					ConfigurationReader.getInstance().getVirtualHost());
		}

		// Check password.
		try {
			if (!VerificationService.getInstance().testConnection(company, userMail, password)) {
				throw new InvalidCredentialsException("Invalid user or password.");
			}
		} catch (AuthenticationRequired ar) {
			throw new InvalidCredentialsException("Invalid user or password.");
		}

		// Get user information.
		User user = null;
		try {
			user = UserService.getInstance().getUserByEmailAddress(company, userMail);
		} catch (AuthenticationRequired e) {
			LiferayClientLogger.errorMessage(this.getClass().getName(), e);
			throw new LiferayConnectionException("Error connecting to Liferay service with '"
					+ ConfigurationReader.getInstance().getUser() + " at "
					+ ConfigurationReader.getInstance().getVirtualHost() + ":"
					+ ConfigurationReader.getInstance().getConnectionPort()
					+ "'.\n Check configuration at 'liferay.conf' file");
		} catch (NotConnectedToWebServiceException e) {
			LiferayClientLogger.errorMessage(this.getClass().getName(), e);
			throw new LiferayConnectionException("Error connecting to Liferay service with '"
					+ ConfigurationReader.getInstance().getUser() + " at "
					+ ConfigurationReader.getInstance().getVirtualHost() + ":"
					+ ConfigurationReader.getInstance().getConnectionPort()
					+ "'.\n Check configuration at 'liferay.conf' file");
		} catch (WebServiceAccessError e) {
			LiferayClientLogger.errorMessage(this.getClass().getName(), e);
			throw new LiferayConnectionException("Error connecting to Liferay service with '"
					+ ConfigurationReader.getInstance().getUser() + " at "
					+ ConfigurationReader.getInstance().getVirtualHost() + ":"
					+ ConfigurationReader.getInstance().getConnectionPort()
					+ "'.\n Check configuration at 'liferay.conf' file");
		} catch (Exception e) {
			LiferayClientLogger.errorMessage(this.getClass().getName(), e);
			throw new InvalidCredentialsException("User does not exist or cannot connect to Liferay web services.");
		}

		LiferayClientLogger.info(this.getClass().getName(), "Access granted to user '" + userMail + "'");
		return user;
	}

	public boolean isInGroup(UserGroup group, User user) throws NotConnectedToWebServiceException,
			ClientProtocolException, IOException, AuthenticationRequired {
		if (group != null && user != null) {
			List<UserGroup> userGroups = UserGroupService.getInstance().getUserUserGroups(user);
			for (UserGroup UserGroupSoap : userGroups) {
				if (UserGroupSoap.getUserGroupId() == group.getUserGroupId()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Get first group of user.
	 * 
	 * @param user
	 * @return
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws AuthenticationRequired
	 */
	public UserGroup getDefaultGroup(User user) throws NotConnectedToWebServiceException, ClientProtocolException,
			IOException, AuthenticationRequired {
		if (user != null) {
			List<UserGroup> userGroups = UserGroupService.getInstance().getUserUserGroups(user);
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

	public User updatePassword(User user, String plainTextPassword) throws NotConnectedToWebServiceException,
			PBKDF2EncryptorException, JsonParseException, JsonMappingException, IOException, AuthenticationRequired,
			WebServiceAccessError {
		return UserService.getInstance().updatePassword(user, plainTextPassword);
	}
}
