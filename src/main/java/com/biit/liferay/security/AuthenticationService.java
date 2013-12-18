package com.biit.liferay.security;

import java.rmi.RemoteException;
import java.util.List;

import javax.xml.rpc.ServiceException;

import com.biit.liferay.access.CompanyService;
import com.biit.liferay.access.UserGroupService;
import com.biit.liferay.access.UserService;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.UserDoesNotExistException;
import com.biit.liferay.configuration.ConfigurationReader;
import com.biit.liferay.log.LiferayClientLogger;
import com.biit.liferay.security.exceptions.InvalidCredentialsException;
import com.biit.liferay.security.exceptions.LiferayConnectionException;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;

public class AuthenticationService {
	private final static AuthenticationService instance = new AuthenticationService();

	private AuthenticationService() {
		try {
			// Connect if it is not connected the fist time used.
			if (UserService.getInstance().isNotConnected()) {
				UserService.getInstance().connectToWebService();
			}
			if (CompanyService.getInstance().isNotConnected()) {
				CompanyService.getInstance().connectToWebService();
			}
			// Connect if not connected the fist time used.
			if (UserGroupService.getInstance().isNotConnected()) {
				UserGroupService.getInstance().connectToWebService();
			}
		} catch (ServiceException se) {
			LiferayClientLogger
					.fatal(AuthenticationService.class.getName(),
							"Cannot connect to UserService and/or CompanyService. Please, configure the file 'liferay.conf' correctly.");
		}
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
	 * @throws RemoteException
	 * @throws NotConnectedToWebServiceException
	 * @throws LiferayConnectionException
	 */
	public User authenticate(String userMail, String password, Company company) throws InvalidCredentialsException,
			ServiceException, RemoteException, NotConnectedToWebServiceException, LiferayConnectionException {
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
			if (e.getLocalizedMessage().contains("No UserSoap exists with the key")) {
				LiferayClientLogger.warning(this.getClass().getName(), "Attempt to loggin failed with UserSoap '"
						+ userMail + "'.");
				throw new InvalidCredentialsException("UserSoap does not exist.");
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

		BasicEncryptionMethod.getInstance().validatePassword(password, UserSoap.getPassword(), company);
		LiferayClientLogger.info(this.getClass().getName(), "Access granted to UserSoap '" + userMail + "'");
		return UserSoap;
	}

	public boolean isInGroup(UserGroup group, User UserSoap) throws RemoteException, NotConnectedToWebServiceException {
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
	 * @throws RemoteException
	 * @throws NotConnectedToWebServiceException
	 */
	public UserGroup getDefaultGroup(User UserSoap) throws RemoteException, NotConnectedToWebServiceException {
		if (UserSoap != null) {
			List<UserGroup> userGroups = UserGroupService.getInstance().getUserUserGroups(UserSoap);
			if (userGroups != null && userGroups.size() > 0) {
				return userGroups.get(0);
			}
		}
		return null;
	}

	public User getUserById(long userId) throws RemoteException, NotConnectedToWebServiceException,
			UserDoesNotExistException {
		return UserService.getInstance().getUserById(userId);
	}

	public void updatePassword(User UserSoap, String plainTextPassword, Company company) throws RemoteException,
			NotConnectedToWebServiceException {
		UserService.getInstance().updatePassword(UserSoap, plainTextPassword, company);
	}
}
