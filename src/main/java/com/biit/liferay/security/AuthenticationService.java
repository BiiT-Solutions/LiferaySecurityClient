package com.biit.liferay.security;

import java.io.IOException;
import java.util.Set;

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
import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.entity.IUser;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class AuthenticationService implements IAuthenticationService {
	public static AuthenticationService getInstance() {
		return instance;
	}

	private final static AuthenticationService instance = new AuthenticationService();
	private IGroup<Long> company = null;
	private UserService userService = new UserService();
	private CompanyService companyService = new CompanyService();
	private UserGroupService userGroupService = new UserGroupService();
	private ContactService contactService = new ContactService();

	private PasswordService passwordService = new PasswordService();

	protected AuthenticationService() {
		userService.serverConnection();
		companyService.serverConnection();
		userGroupService.serverConnection();
		contactService.serverConnection();
		passwordService.serverConnection();
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
	@Override
	public IUser<Long> authenticate(String userMail, String password) throws InvalidCredentialsException,
			NotConnectedToWebServiceException {
		// Login fails if either the username or password is null
		if (userMail == null || password == null) {
			throw new InvalidCredentialsException("No fields filled up.");
		}

		// Check password.
		try {
			if (!VerificationService.getInstance().testConnection(getCompany(), userMail, password)) {
				throw new InvalidCredentialsException("Invalid user or password.");
			}
		} catch (Exception ar) {
			// Cannot access to user, but already has a company. The error is with the user or password.
			throw new InvalidCredentialsException("Invalid user or password.");
		}

		try {
			// Get user information.
			IUser<Long> user = null;
			user = userService.getUserByEmailAddress(getCompany(), userMail);

			LiferayClientLogger.info(this.getClass().getName(), "Access granted to user '" + userMail + "'.");
			return user;
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
	}

	private IGroup<Long> getCompany() throws NotConnectedToWebServiceException {
		try {
			if (company == null) {
				company = companyService.getCompanyByVirtualHost(ConfigurationReader.getInstance().getVirtualHost());
			}
			return company;
		} catch (Exception ar) {
			LiferayClientLogger.errorMessage(this.getClass().getName(), ar);
			throw new NotConnectedToWebServiceException("Error connecting to Liferay service with '"
					+ ConfigurationReader.getInstance().getUser() + " at "
					+ ConfigurationReader.getInstance().getVirtualHost() + ":"
					+ ConfigurationReader.getInstance().getConnectionPort()
					+ "'.\n Check configuration at 'liferay.conf' file.");
		}
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
	@Override
	public IGroup<Long> getDefaultGroup(IUser<Long> user) throws NotConnectedToWebServiceException,
			ClientProtocolException, IOException, AuthenticationRequired {
		if (user != null) {
			Set<IGroup<Long>> userGroups = userGroupService.getUserUserGroups(user);
			if (userGroups != null && userGroups.size() > 0) {
				return userGroups.iterator().next();
			}
		}
		return null;
	}

	@Override
	public IUser<Long> getUserByEmail(String userEmail) throws NotConnectedToWebServiceException,
			UserDoesNotExistException, ClientProtocolException, IOException, AuthenticationRequired,
			WebServiceAccessError {
		return userService.getUserByEmailAddress(getCompany(), userEmail);
	}

	@Override
	public IUser<Long> getUserById(long userId) throws NotConnectedToWebServiceException, UserDoesNotExistException,
			ClientProtocolException, IOException, AuthenticationRequired, WebServiceAccessError {
		return userService.getUserById(userId);
	}

	@Override
	public boolean isInGroup(IGroup<Long> group, IUser<Long> user) throws NotConnectedToWebServiceException,
			ClientProtocolException, IOException, AuthenticationRequired {
		if (group != null && user != null) {
			Set<IGroup<Long>> userGroups = userGroupService.getUserUserGroups(user);
			for (IGroup<Long> UserGroupSoap : userGroups) {
				if (UserGroupSoap.getId().equals(group.getId())) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public IUser<Long> updatePassword(IUser<Long> user, String plainTextPassword)
			throws NotConnectedToWebServiceException, PBKDF2EncryptorException, JsonParseException,
			JsonMappingException, IOException, AuthenticationRequired, WebServiceAccessError {
		return passwordService.updatePassword(user, plainTextPassword);
	}

	public void reset() {
		userGroupService.reset();
		companyService.reset();
		userService.reset();
		passwordService.reset();
	}
}
