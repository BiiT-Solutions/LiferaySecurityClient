package com.biit.liferay.access;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.omg.IOP.ServiceContext;

import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.UserDoesNotExistException;
import com.biit.liferay.log.LiferayClientLogger;
import com.biit.liferay.security.PBKDF2PasswordEncryptor;
import com.biit.liferay.security.exceptions.PasswordEncryptorException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;

/**
 * This class allows to manage users from Liferay portal.
 */
public class UserService extends ServiceAccess<User> {
	private final static UserService instance = new UserService();
	private UserPool userPool;

	private UserService() {
		userPool = new UserPool();
	}

	public static UserService getInstance() {
		return instance;
	}

	/**
	 * Get user information using the email as identificator.
	 * 
	 * @param companySoap
	 *            liferay portal where look up for.
	 * @param emailAddress
	 *            the email of the user.
	 * @return a user.
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public User getUserByEmailAddress(Company companySoap, String emailAddress)
			throws NotConnectedToWebServiceException, ClientProtocolException, IOException {
		emailAddress = emailAddress.toLowerCase();
		// Look up user in the pool.
		User user = userPool.getUserByEmailAddress(emailAddress);
		if (user != null) {
			return user;
		}

		// Look up user in the liferay.
		checkConnection();

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("companyId", companySoap.getCompanyId() + ""));
		params.add(new BasicNameValuePair("emailAddress", emailAddress));

		String result = getHttpResponse("user/get-user-by-email-address", params);
		if (result != null) {
			// A Simple JSON Response Read
			user = decodeFromJason(result, User.class);
			userPool.addUser(user);
		}

		return user;
	}

	/**
	 * Get user information using the user's primary key.
	 * 
	 * @param userId
	 *            user's primary key.
	 * @return a user.
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public User getUserById(long userId) throws NotConnectedToWebServiceException, UserDoesNotExistException,
			ClientProtocolException, IOException {
		if (userId >= 0) {
			// Look up user in the pool.
			User user = userPool.getUserById(userId);
			if (user != null) {
				return user;
			}

			// Read from Liferay.
			checkConnection();

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userId", userId + ""));

			String result = getHttpResponse("user/get-user-by-id", params);
			if (result != null) {
				// A Simple JSON Response Read
				user = decodeFromJason(result, User.class);
				userPool.addUser(user);
				return user;
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
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public User getUserByScreenName(Company companySoap, String screenName) throws NotConnectedToWebServiceException,
			ClientProtocolException, IOException {
		screenName = screenName.toLowerCase();

		// Look up user in the pool.
		User user = userPool.getUserByScreenName(screenName);
		if (user != null) {
			return user;
		}

		// Read from liferay.
		checkConnection();

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("companyId", companySoap.getCompanyId() + ""));
		params.add(new BasicNameValuePair("screenName", screenName));

		String result = getHttpResponse("user/get-user-by-screen-name", params);
		if (result != null) {
			// A Simple JSON Response Read
			user = decodeFromJason(result, User.class);
			userPool.addUser(user);
			return user;
		}

		return null;
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
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public void removeUser(User user) throws NotConnectedToWebServiceException, ClientProtocolException, IOException {
		checkConnection();

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userId ", user.getUserId() + ""));

		getHttpResponse("user/delete-user", params);

		userPool.removeUser(user);
		LiferayClientLogger.info(this.getClass().getName(), "User '" + user.getScreenName() + "' deleted.");
	}

	/**
	 * Updates the password of a user.
	 * 
	 * @param user
	 * @param plainTextPassword
	 * @throws NotConnectedToWebServiceException
	 * @throws PasswordEncryptorException
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public void updatePassword(User user, String plainTextPassword) throws NotConnectedToWebServiceException,
			PasswordEncryptorException, JsonParseException, JsonMappingException, IOException {
		checkConnection();

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userId", user.getUserId() + ""));
		params.add(new BasicNameValuePair("password1", plainTextPassword));
		params.add(new BasicNameValuePair("password2", plainTextPassword));
		params.add(new BasicNameValuePair("passwordReset", "false"));

		String result = getHttpResponse("user/delete-user", params);
		if (result != null) {
			// A Simple JSON Response Read
			User obtainedUser = decodeFromJason(result, User.class);
			user.setPassword(obtainedUser.getPassword());
			LiferayClientLogger.info(this.getClass().getName(), "User has change its password '" + user.getScreenName()
					+ "'.");
		}
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
