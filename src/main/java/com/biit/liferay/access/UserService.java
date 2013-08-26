package com.biit.liferay.access;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.rpc.ServiceException;

import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.UserDoesNotExistException;
import com.biit.liferay.log.LiferayAuthenticationClientLogger;
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
		// Locate the User service
		UserServiceSoapServiceLocator locatorUser = new UserServiceSoapServiceLocator();
		setServiceSoap(locatorUser.getPortal_UserService(AccessUtils.getLiferayUrl(loginUser, password,
				getServiceName())));
	}

	@Override
	public String getServiceName() {
		return SERVICE_USER_NAME;
	}

	/**
	 * Get user information using the email as identificator.
	 * 
	 * @param companySoap
	 *            liferay portal where look up for.
	 * @param emailAddress
	 *            the email of the user.
	 * @return a user.
	 * @throws RemoteException
	 *             if there is any communication problem.
	 * @throws NotConnectedToWebServiceException
	 */
	public User getUserByEmailAddress(Company companySoap, String emailAddress) throws RemoteException,
			NotConnectedToWebServiceException {
		emailAddress = emailAddress.toLowerCase();
		// Look up user in the pool.
		User user = userPool.getUserByEmailAddress(emailAddress);
		if (user != null) {
			return user;
		}

		// //Look up user in the liferay.
		checkConnection();
		user = ((UserServiceSoap) getServiceSoap()).getUserByEmailAddress(companySoap.getCompanyId(), emailAddress);
		userPool.addUser(user);
		return user;
	}

	/**
	 * Get user information using the user's primary key.
	 * 
	 * @param userId
	 *            user's primary key.
	 * @return a user.
	 * @throws RemoteException
	 *             if there is any communication problem.
	 * @throws NotConnectedToWebServiceException
	 */
	public User getUserById(long userId) throws RemoteException, NotConnectedToWebServiceException,
			UserDoesNotExistException {
		if (userId >= 0) {
			// Look up user in the pool.
			User user = userPool.getUserById(userId);
			if (user != null) {
				return user;
			}

			// Read from Liferay.
			checkConnection();
			try {
				user = ((UserServiceSoap) getServiceSoap()).getUserById(userId);
				userPool.addUser(user);
				return user;
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
	 * Get user information using the user's primary key.
	 * 
	 * @param companySoap
	 *            liferay portal where look up for.
	 * @param screenName
	 *            is a unique token that identifies a liferay user from another,
	 *            so two users cannot use the same screenname.
	 * @return a user.
	 * @throws RemoteException
	 *             if there is any communication problem.
	 * @throws NotConnectedToWebServiceException
	 */
	public User getUserByScreenName(Company companySoap, String screenName) throws RemoteException,
			NotConnectedToWebServiceException {
		screenName = screenName.toLowerCase();

		// Look up user in the pool.
		User user = userPool.getUserByScreenName(screenName);
		if (user != null) {
			return user;
		}

		// Read from liferay.
		checkConnection();
		user = ((UserServiceSoap) getServiceSoap()).getUserByScreenName(companySoap.getCompanyId(), screenName);
		userPool.addUser(user);
		return user;
	}

	/**
	 * Adds an user to liferay portal.
	 * 
	 * @param companySoap
	 * @param user
	 * @return a user.
	 * @throws RemoteException
	 * @throws NotConnectedToWebServiceException
	 */
	public User addUser(Company companySoap, User user) throws RemoteException, NotConnectedToWebServiceException {
		if (user != null) {
			return addUser(companySoap, user.getPassword(), user.getScreenName(), user.getEmailAddress(),
					user.getFacebookId(), user.getOpenId(), user.getTimeZoneId(), user.getFirstName(),
					user.getMiddleName(), user.getLastName());
		}
		return null;
	}

	/**
	 * Creates an user into liferay portal. If password and/or screenname are
	 * not set, they will be auto-generated.
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
	 * @return a user.
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
		User user = ((UserServiceSoap) getServiceSoap()).addUser(companySoap.getCompanyId(), autoPassword, password,
				password, autoScreenName, screenName.toLowerCase(), emailAddress.toLowerCase(), facebookId, openId,
				locale, firstName, middleName, lastName, 0, 0, false, 1, 1, 1970, "", null, null, null, null, false,
				new ServiceContext());

		if (user != null) {
			userPool.addUser(user);
		}
		return user;
	}

	/**
	 * Deletes an user from the database.
	 * 
	 * @param user
	 * @throws RemoteException
	 * @throws NotConnectedToWebServiceException
	 */
	public void deleteUser(User user) throws RemoteException, NotConnectedToWebServiceException {
		checkConnection();
		((UserServiceSoap) getServiceSoap()).deleteUser(user.getUserId());
		userPool.removeUser(user);
		LiferayAuthenticationClientLogger.info(this.getClass().getName(), "User '" + user.getScreenName()
				+ "' deleted.");
	}

	/**
	 * Updates the password of a user.
	 * 
	 * @param user
	 * @param plainTextPassword
	 * @throws NotConnectedToWebServiceException
	 * @throws RemoteException
	 */
	public void updatePassword(User user, String plainTextPassword) throws NotConnectedToWebServiceException,
			RemoteException {
		checkConnection();
		((UserServiceSoap) getServiceSoap()).updatePassword(user.getUserId(), plainTextPassword, plainTextPassword,
				false);
		user.setPassword(BasicEncryptionMethod.getInstance().encryptPassword(plainTextPassword));
		LiferayAuthenticationClientLogger.info(this.getClass().getName(),
				"User has change its password '" + user.getScreenName() + "'.");
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
	 * Add a user to a group. For testing use only.
	 * 
	 * @param users
	 * @param group
	 * @throws RemoteException
	 * @throws NotConnectedToWebServiceException
	 */
	public void addUserToGroup(User user, UserGroup group) throws RemoteException, NotConnectedToWebServiceException {
		List<User> users = new ArrayList<User>();
		users.add(user);
		addUsersToGroup(users, group);
	}

}
