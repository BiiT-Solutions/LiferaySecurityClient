package com.biit.liferay.security;

import java.io.IOException;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.http.client.ClientProtocolException;

import com.biit.liferay.access.CompanyService;
import com.biit.liferay.access.ContactService;
import com.biit.liferay.access.PasswordService;
import com.biit.liferay.access.UserGroupService;
import com.biit.liferay.access.UserService;
import com.biit.liferay.access.VerificationService;
import com.biit.liferay.access.exceptions.DuplicatedLiferayElement;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.UserDoesNotExistException;
import com.biit.liferay.access.exceptions.WebServiceAccessError;
import com.biit.liferay.configuration.LiferayConfigurationReader;
import com.biit.liferay.log.LiferayClientLogger;
import com.biit.logger.BiitCommonLogger;
import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.security.IAuthenticationService;
import com.biit.usermanager.security.exceptions.AuthenticationRequired;
import com.biit.usermanager.security.exceptions.InvalidCredentialsException;
import com.biit.usermanager.security.exceptions.UserManagementException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.liferay.portal.model.User;

/**
 * Implements Liferay service access for authentication.
 */
public class AuthenticationService implements IAuthenticationService<Long, Long> {
	private IGroup<Long> company = null;

	@Inject
	private UserService userService;

	@Inject
	private CompanyService companyService;

	@Inject
	private UserGroupService userGroupService;

	@Inject
	private ContactService contactService;

	@Inject
	private PasswordService passwordService;

	@PostConstruct
	public void connect() {
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
	 * @throws UserManagementException
	 * @throws ServiceException
	 * @throws LiferayConnectionException
	 * @throws PBKDF2EncryptorException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @throws AuthenticationRequired
	 * @throws InvalidCredentialsException
	 * @throws WebServiceAccessError
	 */
	@Override
	public IUser<Long> authenticate(String userMail, String password)
			throws UserManagementException, AuthenticationRequired, InvalidCredentialsException {
		// Login fails if either the username or password is null
		if (userMail == null || password == null) {
			throw new InvalidCredentialsException("No fields filled up.");
		}

		// Check password.
		try {
			if (!VerificationService.getInstance().testConnection(getCompany(), userMail, password)) {
				throw new InvalidCredentialsException(
						"Invalid password for user '" + userMail + "' at company + '" + getCompany() + "'.");
			}
		} catch (Exception ar) {
			// Cannot access to user, but already has a company. The error is
			// with the user or password.
			BiitCommonLogger.errorMessageNotification(this.getClass(), ar);
			throw new InvalidCredentialsException("Invalid user or password.");
		}

		try {
			// Get user information.
			IUser<Long> user = null;
			user = userService.getUserByEmailAddress(getCompany(), userMail);

			LiferayClientLogger.info(this.getClass().getName(), "Access granted to user '" + userMail + "'.");
			return user;
		} catch (NotConnectedToWebServiceException e) {
			LiferayClientLogger.errorMessage(this.getClass().getName(), e);
			throw new UserManagementException(
					"Error connecting to Liferay service with '" + LiferayConfigurationReader.getInstance().getUser()
							+ " at " + LiferayConfigurationReader.getInstance().getHost() + ":"
							+ LiferayConfigurationReader.getInstance().getConnectionPort()
							+ "'. Check configuration at 'liferay.conf' file.");
		} catch (WebServiceAccessError e) {
			LiferayClientLogger.errorMessage(this.getClass().getName(), e);
			throw new UserManagementException(
					"Error connecting to Liferay service with '" + LiferayConfigurationReader.getInstance().getUser()
							+ " at " + LiferayConfigurationReader.getInstance().getHost() + ":"
							+ LiferayConfigurationReader.getInstance().getConnectionPort()
							+ "'. Check configuration at 'liferay.conf' file.");
		} catch (ClientProtocolException e) {
			LiferayClientLogger.errorMessage(this.getClass().getName(), e);
			throw new UserManagementException(
					"Error connecting to Liferay service with '" + LiferayConfigurationReader.getInstance().getUser()
							+ " at " + LiferayConfigurationReader.getInstance().getHost() + ":"
							+ LiferayConfigurationReader.getInstance().getConnectionPort()
							+ "'. Check configuration at 'liferay.conf' file.");
		} catch (IOException e) {
			LiferayClientLogger.errorMessage(this.getClass().getName(), e);
			throw new UserManagementException(
					"Error connecting to Liferay service with '" + LiferayConfigurationReader.getInstance().getUser()
							+ " at " + LiferayConfigurationReader.getInstance().getHost() + ":"
							+ LiferayConfigurationReader.getInstance().getConnectionPort()
							+ "'. Check configuration at 'liferay.conf' file.");
		}
	}

	private IGroup<Long> getCompany() throws NotConnectedToWebServiceException {
		try {
			if (company == null) {
				company = companyService
						.getCompanyByVirtualHost(LiferayConfigurationReader.getInstance().getVirtualHost());
			}
			return company;
		} catch (Exception ar) {
			LiferayClientLogger.errorMessage(this.getClass().getName(), ar);
			throw new NotConnectedToWebServiceException(
					"Error connecting to Liferay service with '" + LiferayConfigurationReader.getInstance().getUser()
							+ " at " + LiferayConfigurationReader.getInstance().getVirtualHost() + ":"
							+ LiferayConfigurationReader.getInstance().getConnectionPort()
							+ "'.\n Check configuration at 'liferay.conf' file.");
		}
	}

	/**
	 * Get first group of user.
	 * 
	 * @param user
	 * @return
	 * @throws UserManagementException
	 */
	@Override
	public IGroup<Long> getDefaultGroup(IUser<Long> user) throws UserManagementException {
		if (user != null) {
			Set<IGroup<Long>> userGroups;
			try {
				userGroups = userGroupService.getUserUserGroups(user);
			} catch (ClientProtocolException e) {
				LiferayClientLogger.errorMessage(this.getClass().getName(), e);
				throw new UserManagementException(e.getMessage());
			} catch (NotConnectedToWebServiceException e) {
				LiferayClientLogger.errorMessage(this.getClass().getName(), e);
				throw new UserManagementException(e.getMessage());
			} catch (IOException e) {
				LiferayClientLogger.errorMessage(this.getClass().getName(), e);
				throw new UserManagementException(e.getMessage());
			} catch (AuthenticationRequired e) {
				LiferayClientLogger.errorMessage(this.getClass().getName(), e);
				throw new UserManagementException(e.getMessage());
			}
			if (userGroups != null && userGroups.size() > 0) {
				return userGroups.iterator().next();
			}
		}
		return null;
	}

	@Override
	public IUser<Long> getUserByEmail(String userEmail) throws UserManagementException {
		try {
			return userService.getUserByEmailAddress(getCompany(), userEmail);
		} catch (ClientProtocolException e) {
			LiferayClientLogger.errorMessage(this.getClass().getName(), e);
			throw new UserManagementException(e.getMessage());
		} catch (NotConnectedToWebServiceException e) {
			LiferayClientLogger.errorMessage(this.getClass().getName(), e);
			throw new UserManagementException(e.getMessage());
		} catch (IOException e) {
			LiferayClientLogger.errorMessage(this.getClass().getName(), e);
			throw new UserManagementException(e.getMessage());
		} catch (AuthenticationRequired e) {
			LiferayClientLogger.errorMessage(this.getClass().getName(), e);
			throw new UserManagementException(e.getMessage());
		} catch (WebServiceAccessError e) {
			LiferayClientLogger.errorMessage(this.getClass().getName(), e);
			throw new UserManagementException(e.getMessage());
		}
	}

	@Override
	public IUser<Long> getUserById(long userId) throws UserManagementException {
		try {
			return userService.getUserById(userId);
		} catch (ClientProtocolException e) {
			LiferayClientLogger.errorMessage(this.getClass().getName(), e);
			throw new UserManagementException(e.getMessage());
		} catch (NotConnectedToWebServiceException e) {
			LiferayClientLogger.errorMessage(this.getClass().getName(), e);
			throw new UserManagementException(e.getMessage());
		} catch (UserDoesNotExistException e) {
			LiferayClientLogger.errorMessage(this.getClass().getName(), e);
			throw new UserManagementException(e.getMessage());
		} catch (IOException e) {
			LiferayClientLogger.errorMessage(this.getClass().getName(), e);
			throw new UserManagementException(e.getMessage());
		} catch (AuthenticationRequired e) {
			LiferayClientLogger.errorMessage(this.getClass().getName(), e);
			throw new UserManagementException(e.getMessage());
		} catch (WebServiceAccessError e) {
			LiferayClientLogger.errorMessage(this.getClass().getName(), e);
			throw new UserManagementException(e.getMessage());
		}
	}

	@Override
	public boolean isInGroup(IGroup<Long> group, IUser<Long> user) throws UserManagementException {
		if (group != null && user != null) {
			Set<IGroup<Long>> userGroups;
			try {
				userGroups = userGroupService.getUserUserGroups(user);
			} catch (ClientProtocolException e) {
				LiferayClientLogger.errorMessage(this.getClass().getName(), e);
				throw new UserManagementException(e.getMessage());
			} catch (NotConnectedToWebServiceException e) {
				LiferayClientLogger.errorMessage(this.getClass().getName(), e);
				throw new UserManagementException(e.getMessage());
			} catch (IOException e) {
				LiferayClientLogger.errorMessage(this.getClass().getName(), e);
				throw new UserManagementException(e.getMessage());
			} catch (AuthenticationRequired e) {
				LiferayClientLogger.errorMessage(this.getClass().getName(), e);
				throw new UserManagementException(e.getMessage());
			}
			for (IGroup<Long> UserGroupSoap : userGroups) {
				if (UserGroupSoap.getId().equals(group.getId())) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void reset() {
		userGroupService.reset();
		companyService.reset();
		userService.reset();
		passwordService.reset();
		contactService.reset();
	}

	@Override
	public IUser<Long> updatePassword(IUser<Long> user, String plainTextPassword) throws UserManagementException {
		try {
			return passwordService.updatePassword(user, plainTextPassword);
		} catch (JsonParseException e) {
			LiferayClientLogger.errorMessage(this.getClass().getName(), e);
			throw new UserManagementException(e.getMessage());
		} catch (JsonMappingException e) {
			LiferayClientLogger.errorMessage(this.getClass().getName(), e);
			throw new UserManagementException(e.getMessage());
		} catch (NotConnectedToWebServiceException e) {
			LiferayClientLogger.errorMessage(this.getClass().getName(), e);
			throw new UserManagementException(e.getMessage());
		} catch (IOException e) {
			LiferayClientLogger.errorMessage(this.getClass().getName(), e);
			throw new UserManagementException(e.getMessage());
		} catch (AuthenticationRequired e) {
			LiferayClientLogger.errorMessage(this.getClass().getName(), e);
			throw new UserManagementException(e.getMessage());
		} catch (WebServiceAccessError e) {
			LiferayClientLogger.errorMessage(this.getClass().getName(), e);
			throw new UserManagementException(e.getMessage());
		}
	}

	@Override
	public IUser<Long> updateUser(IUser<Long> user) throws UserManagementException {
		if (user instanceof User) {
			try {
				return userService.updateUser((User) user);
			} catch (ClientProtocolException e) {
				LiferayClientLogger.errorMessage(this.getClass().getName(), e);
				throw new UserManagementException(e.getMessage());
			} catch (NotConnectedToWebServiceException e) {
				LiferayClientLogger.errorMessage(this.getClass().getName(), e);
				throw new UserManagementException(e.getMessage());
			} catch (IOException e) {
				LiferayClientLogger.errorMessage(this.getClass().getName(), e);
				throw new UserManagementException(e.getMessage());
			} catch (AuthenticationRequired e) {
				LiferayClientLogger.errorMessage(this.getClass().getName(), e);
				throw new UserManagementException(e.getMessage());
			} catch (WebServiceAccessError e) {
				LiferayClientLogger.errorMessage(this.getClass().getName(), e);
				throw new UserManagementException(e.getMessage());
			}
		}
		LiferayClientLogger.error(this.getClass().getName(), "Invalid user " + user);
		return null;
	}

	@Override
	public IUser<Long> addUser(IGroup<Long> company, String password, String screenName, String emailAddress,
			String locale, String firstName, String middleName, String lastName) throws UserManagementException {

		try {
			return userService.addUser(company, password, screenName, emailAddress, 0l, "", locale, firstName,
					middleName, lastName, 0, 0, true, 1, 1, 1970, "", new long[0], new long[0], new long[0],
					new long[0], false);

		} catch (ClientProtocolException e) {
			LiferayClientLogger.errorMessage(this.getClass().getName(), e);
			throw new UserManagementException("User '" + emailAddress + "' not added correctly.");
		} catch (NotConnectedToWebServiceException e) {
			LiferayClientLogger.errorMessage(this.getClass().getName(), e);
			throw new UserManagementException("User '" + emailAddress + "' not added correctly.");
		} catch (IOException e) {
			LiferayClientLogger.errorMessage(this.getClass().getName(), e);
			throw new UserManagementException("User '" + emailAddress + "' not added correctly.");
		} catch (AuthenticationRequired e) {
			LiferayClientLogger.errorMessage(this.getClass().getName(), e);
			throw new UserManagementException("User '" + emailAddress + "' not added correctly.");
		} catch (WebServiceAccessError e) {
			LiferayClientLogger.errorMessage(this.getClass().getName(), e);
			throw new UserManagementException("User '" + emailAddress + "' not added correctly.");
		} catch (DuplicatedLiferayElement e) {
			LiferayClientLogger.errorMessage(this.getClass().getName(), e);
			throw new UserManagementException("User '" + emailAddress + "' not added correctly.");
		}

	}
}
