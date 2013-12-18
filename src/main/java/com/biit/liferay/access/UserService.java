package com.biit.liferay.access;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.rpc.ServiceException;

import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.UserDoesNotExistException;
import com.biit.liferay.log.LiferayClientLogger;
import com.biit.liferay.security.BasicEncryptionMethod;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.http.UserServiceSoap;
import com.liferay.portal.service.http.UserServiceSoapServiceLocator;

/**
 * This class allows to manage users from Liferay portal.
 */
public class UserService extends ServiceAccess {
	private final static String SERVICE_USER_NAME = "Portal_UserService";
	private final static UserService instance = new UserService();
	private UserPool userPool;

	private UserService() {
		userPool = new UserPool();
	}

	public static UserService getInstance() {
		return instance;
	}

	@Override
	public void connectToWebService(String loginUser, String password) throws ServiceException {
		// Locate the UserSoap service
		UserServiceSoapServiceLocator locatorUser = new UserServiceSoapServiceLocator();
		setServiceSoap(locatorUser.getPortal_UserService(AccessUtils.getLiferayUrl(loginUser, password,
				getServiceName())));
	}

	@Override
	public String getServiceName() {
		return SERVICE_USER_NAME;
	}

	/**
	 * Get UserSoap information using the email as identificator.
	 * 
	 * @param companySoap
	 *            liferay portal where look up for.
	 * @param emailAddress
	 *            the email of the UserSoap.
	 * @return a UserSoap.
	 * @throws RemoteException
	 *             if there is any communication problem.
	 * @throws NotConnectedToWebServiceException
	 */
	public User getUserByEmailAddress(Company companySoap, String emailAddress) throws RemoteException,
			NotConnectedToWebServiceException {
		emailAddress = emailAddress.toLowerCase();
		// Look up UserSoap in the pool.
		User UserSoap = userPool.getUserByEmailAddress(emailAddress);
		if (UserSoap != null) {
			return UserSoap;
		}

		// Look up UserSoap in the liferay.
		checkConnection();
		UserSoap = ((UserServiceSoap) getServiceSoap()).getUserByEmailAddress(companySoap.getCompanyId(), emailAddress);
		userPool.addUser(UserSoap);
		return UserSoap;
	}

	/**
	 * Get UserSoap information using the UserSoap's primary key.
	 * 
	 * @param userId
	 *            UserSoap's primary key.
	 * @return a UserSoap.
	 * @throws RemoteException
	 *             if there is any communication problem.
	 * @throws NotConnectedToWebServiceException
	 */
	public User getUserById(long userId) throws RemoteException, NotConnectedToWebServiceException,
			UserDoesNotExistException {
		if (userId >= 0) {
			// Look up UserSoap in the pool.
			User UserSoap = userPool.getUserById(userId);
			if (UserSoap != null) {
				return UserSoap;
			}

			// Read from Liferay.
			checkConnection();
			try {
				UserSoap = ((UserServiceSoap) getServiceSoap()).getUserById(userId);
				userPool.addUser(UserSoap);
				return UserSoap;
			} catch (RemoteException re) {
				if (re.getLocalizedMessage().contains("No User exists with the primary key")) {
					throw new UserDoesNotExistException("User with id '" + userId + "' does not exists.");
				} else {
					re.printStackTrace();
					throw re;
				}
			}
		}
		return null;
	}

	/**
	 * Get UserSoap information using the UserSoap's primary key.
	 * 
	 * @param companySoap
	 *            liferay portal where look up for.
	 * @param screenName
	 *            is a unique token that identifies a liferay UserSoap from
	 *            another, so two users cannot use the same screenname.
	 * @return a UserSoap.
	 * @throws RemoteException
	 *             if there is any communication problem.
	 * @throws NotConnectedToWebServiceException
	 */
	public User getUserByScreenName(Company companySoap, String screenName) throws RemoteException,
			NotConnectedToWebServiceException {
		screenName = screenName.toLowerCase();

		// Look up UserSoap in the pool.
		User UserSoap = userPool.getUserByScreenName(screenName);
		if (UserSoap != null) {
			return UserSoap;
		}

		// Read from liferay.
		checkConnection();
		UserSoap = ((UserServiceSoap) getServiceSoap()).getUserByScreenName(companySoap.getCompanyId(), screenName);
		userPool.addUser(UserSoap);
		return UserSoap;
	}

	/**
	 * Adds an UserSoap to liferay portal.
	 * 
	 * @param companySoap
	 * @param UserSoap
	 * @return a UserSoap.
	 * @throws RemoteException
	 * @throws NotConnectedToWebServiceException
	 */
	public User addUser(Company companySoap, User UserSoap) throws RemoteException, NotConnectedToWebServiceException {
		if (UserSoap != null) {
			return addUser(companySoap, UserSoap.getPassword(), UserSoap.getScreenName(), UserSoap.getEmailAddress(),
					UserSoap.getFacebookId(), UserSoap.getOpenId(), UserSoap.getTimeZoneId(), UserSoap.getFirstName(),
					UserSoap.getMiddleName(), UserSoap.getLastName());
		}
		return null;
	}

	/**
	 * Creates an UserSoap into liferay portal. If password and/or screenname
	 * are not set, they will be auto-generated.
	 * 
	 * @param companySoap
	 * @param password
	 * @param screenName
	 * @param emailAddress
	 * @param facebookId
	 * @param openId
	 * @param locale
	 * @param firstName
	 * @param middleName
	 * @param lastName
	 * @return a UserSoap.
	 * @throws RemoteException
	 * @throws NotConnectedToWebServiceException
	 */
	public User addUser(Company companySoap, String password, String screenName, String emailAddress, long facebookId,
			String openId, String locale, String firstName, String middleName, String lastName) throws RemoteException,
			NotConnectedToWebServiceException {
		checkConnection();
		boolean autoPassword = false;
		boolean autoScreenName = false;
		if (password == null || password.length() == 0) {
			autoPassword = true;
		}
		if (screenName == null || screenName.length() == 0) {
			autoScreenName = true;
		}
		User UserSoap = ((UserServiceSoap) getServiceSoap()).addUser(companySoap.getCompanyId(), autoPassword,
				password, password, autoScreenName, screenName.toLowerCase(), emailAddress.toLowerCase(), facebookId,
				openId, locale, firstName, middleName, lastName, 0, 0, false, 1, 1, 1970, "", null, null, null, null,
				false, new ServiceContext());

		if (UserSoap != null) {
			userPool.addUser(UserSoap);
		}
		return UserSoap;
	}

	/**
	 * Deletes an UserSoap from the database.
	 * 
	 * @param UserSoap
	 * @throws RemoteException
	 * @throws NotConnectedToWebServiceException
	 */
	public void deleteUser(User UserSoap) throws RemoteException, NotConnectedToWebServiceException {
		checkConnection();
		((UserServiceSoap) getServiceSoap()).deleteUser(UserSoap.getUserId());
		userPool.removeUser(UserSoap);
		LiferayClientLogger.info(this.getClass().getName(), "User '" + UserSoap.getScreenName() + "' deleted.");
	}

	/**
	 * Updates the password of a UserSoap.
	 * 
	 * @param UserSoap
	 * @param plainTextPassword
	 * @throws NotConnectedToWebServiceException
	 * @throws RemoteException
	 */
	public void updatePassword(User UserSoap, String plainTextPassword, Company company)
			throws NotConnectedToWebServiceException, RemoteException {
		checkConnection();
		((UserServiceSoap) getServiceSoap()).updatePassword(UserSoap.getUserId(), plainTextPassword, plainTextPassword,
				false);
		UserSoap.setPassword(BasicEncryptionMethod.getInstance().encryptPassword(plainTextPassword, company));
		LiferayClientLogger.info(this.getClass().getName(),
				"User has change its password '" + UserSoap.getScreenName() + "'.");
	}

	/**
	 * Add a list of users to a group. For testing use only.
	 * 
	 * @param users
	 * @param group
	 * @throws RemoteException
	 * @throws NotConnectedToWebServiceException
	 */
	public void addUsersToGroup(List<User> users, UserGroup group) throws RemoteException,
			NotConnectedToWebServiceException {
		if (users != null && users.size() > 0 && group != null) {
			checkConnection();
			long[] userIds = new long[users.size()];
			for (int i = 0; i < users.size(); i++) {
				userIds[i] = users.get(i).getUserId();
			}
			((UserServiceSoap) getServiceSoap()).addUserGroupUsers(group.getUserGroupId(), userIds);
		}
	}

	/**
	 * Add a UserSoap to a group. For testing use only.
	 * 
	 * @param users
	 * @param group
	 * @throws RemoteException
	 * @throws NotConnectedToWebServiceException
	 */
	public void addUserToGroup(User UserSoap, UserGroup group) throws RemoteException,
			NotConnectedToWebServiceException {
		List<User> users = new ArrayList<User>();
		users.add(UserSoap);
		addUsersToGroup(users, group);
	}

}
