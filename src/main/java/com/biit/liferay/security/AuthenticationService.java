package com.biit.liferay.security;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import com.biit.liferay.access.CompanyService;
import com.biit.liferay.access.ContactService;
import com.biit.liferay.access.PasswordService;
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
import com.biit.security.exceptions.PBKDF2EncryptorException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;

public class AuthenticationService {
	private final static AuthenticationService instance = new AuthenticationService();
	private Company company = null;
	private UserService userService = new UserService();
	private CompanyService companyService = new CompanyService();
	private UserGroupService userGroupService = new UserGroupService();
	private ContactService contactService = new ContactService();
	private PasswordService passwordService = new PasswordService();

	private AuthenticationService() {
		userService.serverConnection();
		companyService.serverConnection();
		userGroupService.serverConnection();
		contactService.serverConnection();
		passwordService.serverConnection();
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
			NotConnectedToWebServiceException, PBKDF2EncryptorException, JsonParseException, JsonMappingException,
			ClientProtocolException, IOException, AuthenticationRequired, WebServiceAccessError {
		// Login fails if either the username or password is null
		if (userMail == null || password == null) {
			throw new InvalidCredentialsException("No fields filled up.");
		}

		try {
			if (company == null) {
				company = companyService.getCompanyByVirtualHost(ConfigurationReader.getInstance().getVirtualHost());
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
			user = userService.getUserByEmailAddress(company, userMail);
		} catch (AuthenticationRequired e) {
			LiferayClientLogger.errorMessage(this.getClass().getName(), e);
			throw new NotConnectedToWebServiceException("Error connecting to Liferay service with '"
					+ ConfigurationReader.getInstance().getUser() + " at "
					+ ConfigurationReader.getInstance().getVirtualHost() + ":"
					+ ConfigurationReader.getInstance().getConnectionPort()
					+ "'.\n Check configuration at 'liferay.conf' file.");
		} catch (NotConnectedToWebServiceException e) {
			LiferayClientLogger.errorMessage(this.getClass().getName(), e);
			throw new NotConnectedToWebServiceException("Error connecting to Liferay service with '"
					+ ConfigurationReader.getInstance().getUser() + " at "
					+ ConfigurationReader.getInstance().getVirtualHost() + ":"
					+ ConfigurationReader.getInstance().getConnectionPort()
					+ "'.\n Check configuration at 'liferay.conf' file.");
		} catch (WebServiceAccessError e) {
			LiferayClientLogger.errorMessage(this.getClass().getName(), e);
			throw new NotConnectedToWebServiceException("Error connecting to Liferay service with '"
					+ ConfigurationReader.getInstance().getUser() + " at "
					+ ConfigurationReader.getInstance().getVirtualHost() + ":"
					+ ConfigurationReader.getInstance().getConnectionPort()
					+ "'.\n Check configuration at 'liferay.conf' file.");
		} catch (Exception e) {
			LiferayClientLogger.errorMessage(this.getClass().getName(), e);
			throw new InvalidCredentialsException("User does not exist or cannot connect to Liferay web services.");
		}

		LiferayClientLogger.info(this.getClass().getName(), "Access granted to user '" + userMail + "'.");
		return user;
	}

	public boolean isInGroup(UserGroup group, User user) throws NotConnectedToWebServiceException,
			ClientProtocolException, IOException, AuthenticationRequired {
		if (group != null && user != null) {
			List<UserGroup> userGroups = userGroupService.getUserUserGroups(user);
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
			List<UserGroup> userGroups = userGroupService.getUserUserGroups(user);
			if (userGroups != null && userGroups.size() > 0) {
				return userGroups.get(0);
			}
		}
		return null;
	}

	public User getUserById(long userId) throws NotConnectedToWebServiceException, UserDoesNotExistException,
			ClientProtocolException, IOException, AuthenticationRequired, WebServiceAccessError {
		return userService.getUserById(userId);
	}

	public User updatePassword(User user, String plainTextPassword) throws NotConnectedToWebServiceException,
			PBKDF2EncryptorException, JsonParseException, JsonMappingException, IOException, AuthenticationRequired,
			WebServiceAccessError {
		return passwordService.updatePassword(user, plainTextPassword);
	}
}
