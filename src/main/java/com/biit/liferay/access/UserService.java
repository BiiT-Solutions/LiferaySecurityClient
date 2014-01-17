package com.biit.liferay.access;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.UserDoesNotExistException;
import com.biit.liferay.access.exceptions.WebServiceAccessError;
import com.biit.liferay.log.LiferayClientLogger;
import com.biit.security.exceptions.PBKDF2EncryptorException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.User;

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

	@Override
	public List<User> decodeListFromJson(String json, Class<User> objectClass) throws JsonParseException,
			JsonMappingException, IOException {
		List<User> myObjects = new ObjectMapper().readValue(json, new TypeReference<List<User>>() {
		});

		return myObjects;
	}

	/**
	 * Get user information using the email as identificator.
	 * 
	 * @param company
	 *            liferay portal where look up for.
	 * @param emailAddress
	 *            the email of the user.
	 * @return a user.
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws AuthenticationRequired
	 * @throws WebServiceAccessError
	 */
	public User getUserByEmailAddress(Company company, String emailAddress) throws NotConnectedToWebServiceException,
			ClientProtocolException, IOException, AuthenticationRequired, WebServiceAccessError {
		emailAddress = emailAddress.toLowerCase();
		// Look up user in the pool.
		User user = userPool.getUserByEmailAddress(emailAddress);
		if (user != null) {
			return user;
		}

		// Look up user in the liferay.
		checkConnection();

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("companyId", company.getCompanyId() + ""));
		params.add(new BasicNameValuePair("emailAddress", emailAddress));

		String result = getHttpResponse("user/get-user-by-email-address", params);
		if (result != null) {
			// A Simple JSON Response Read
			user = decodeFromJson(result, User.class);
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
	 * @throws AuthenticationRequired
	 * @throws WebServiceAccessError
	 */
	public User getUserById(long userId) throws NotConnectedToWebServiceException, UserDoesNotExistException,
			ClientProtocolException, IOException, AuthenticationRequired, WebServiceAccessError {
		if (userId >= 0) {
			// Look up user in the pool.
			User user = userPool.getUserById(userId);
			if (user != null) {
				return user;
			}

			// Read from Liferay.
			checkConnection();

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userId", Long.toString(userId)));

			String result = getHttpResponse("user/get-user-by-id", params);
			if (result != null) {
				// A Simple JSON Response Read
				user = decodeFromJson(result, User.class);
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
	 * @throws AuthenticationRequired
	 * @throws WebServiceAccessError
	 */
	public User getUserByScreenName(Company companySoap, String screenName) throws NotConnectedToWebServiceException,
			ClientProtocolException, IOException, AuthenticationRequired, WebServiceAccessError {
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
			user = decodeFromJson(result, User.class);
			userPool.addUser(user);
			return user;
		}

		return null;
	}

	/**
	 * Adds an user to Liferay portal.
	 * 
	 * @param companySoap
	 * @param user
	 * @return a user.
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws AuthenticationRequired
	 * @throws WebServiceAccessError
	 */
	public User addUser(Company companySoap, User user) throws NotConnectedToWebServiceException,
			ClientProtocolException, IOException, AuthenticationRequired, WebServiceAccessError {
		if (user != null) {
			return addUser(companySoap, user.getPassword(), user.getScreenName(), user.getEmailAddress(),
					user.getFacebookId(), user.getOpenId(), user.getTimeZoneId(), user.getFirstName(),
					user.getMiddleName(), user.getLastName(), 0, 0, true, 1, 1, 1900, user.getJobTitle(), new long[0],
					new long[0], new long[0], new long[0], false);
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
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws AuthenticationRequired
	 * @throws WebServiceAccessError
	 */
	public User addUser(Company company, String password, String screenName, String emailAddress, long facebookId,
			String openId, String locale, String firstName, String middleName, String lastName, int prefixId,
			int sufixId, boolean male, int birthdayDay, int birthdayMonth, int birthdayYear, String jobTitle,
			long[] groupIds, long[] organizationIds, long[] roleIds, long[] userGroupIds, boolean sendEmail)
			throws NotConnectedToWebServiceException, ClientProtocolException, IOException, AuthenticationRequired,
			WebServiceAccessError {
		checkConnection();
		boolean autoPassword = false;
		boolean autoScreenName = false;
		if (password == null || password.length() == 0) {
			autoPassword = true;
		}
		if (screenName == null || screenName.length() == 0) {
			autoScreenName = true;
		}

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("companyId", Long.toString(company.getCompanyId())));
		params.add(new BasicNameValuePair("autoPassword", Boolean.toString(autoPassword)));
		params.add(new BasicNameValuePair("password1", password));
		params.add(new BasicNameValuePair("password2", password));
		params.add(new BasicNameValuePair("autoScreenName", Boolean.toString(autoScreenName)));
		params.add(new BasicNameValuePair("screenName", screenName));
		params.add(new BasicNameValuePair("emailAddress", emailAddress));
		params.add(new BasicNameValuePair("facebookId", Long.toString(facebookId)));
		params.add(new BasicNameValuePair("openId", openId));
		params.add(new BasicNameValuePair("locale", locale));
		params.add(new BasicNameValuePair("firstName", firstName));
		params.add(new BasicNameValuePair("middleName", middleName));
		params.add(new BasicNameValuePair("lastName", lastName));
		params.add(new BasicNameValuePair("prefixId", Integer.toString(prefixId)));
		params.add(new BasicNameValuePair("sufixId", Integer.toString(sufixId)));
		params.add(new BasicNameValuePair("male", Boolean.toString(male)));
		params.add(new BasicNameValuePair("birthdayMonth", Integer.toString(birthdayMonth)));
		params.add(new BasicNameValuePair("birthdayDay", Integer.toString(birthdayDay)));
		params.add(new BasicNameValuePair("birthdayYear", Integer.toString(birthdayYear)));
		params.add(new BasicNameValuePair("jobTitle", jobTitle));
		params.add(new BasicNameValuePair("groupIds", Arrays.toString(groupIds)));
		params.add(new BasicNameValuePair("organizationIds", Arrays.toString(organizationIds)));
		params.add(new BasicNameValuePair("roleIds", Arrays.toString(roleIds)));
		params.add(new BasicNameValuePair("userGroupIds", Arrays.toString(userGroupIds)));
		params.add(new BasicNameValuePair("sendEmail", Boolean.toString(sendEmail)));
		params.add(new BasicNameValuePair("serviceContext", null));

		String result = getHttpResponse("user/add-user", params);
		User user = null;
		if (result != null) {
			// A Simple JSON Response Read
			user = decodeFromJson(result, User.class);
			userPool.addUser(user);
			LiferayClientLogger.info(this.getClass().getName(), "User '" + user.getScreenName() + "' added.");
			return user;
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
	 * @throws AuthenticationRequired
	 */
	public void deleteUser(User user) throws NotConnectedToWebServiceException, ClientProtocolException, IOException,
			AuthenticationRequired {
		checkConnection();

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userId", user.getUserId() + ""));

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
	 * @throws PBKDF2EncryptorException
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @throws AuthenticationRequired
	 * @throws WebServiceAccessError
	 */
	public User updatePassword(User user, String plainTextPassword) throws NotConnectedToWebServiceException,
			PBKDF2EncryptorException, JsonParseException, JsonMappingException, IOException, AuthenticationRequired,
			WebServiceAccessError {
		checkConnection();

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userId", user.getUserId() + ""));
		params.add(new BasicNameValuePair("password1", plainTextPassword));
		params.add(new BasicNameValuePair("password2", plainTextPassword));
		params.add(new BasicNameValuePair("passwordReset", "false"));

		String result = getHttpResponse("user/delete-user", params);
		if (result != null) {
			// A Simple JSON Response Read
			User obtainedUser = decodeFromJson(result, User.class);
			user.setPassword(obtainedUser.getPassword());
			LiferayClientLogger.info(this.getClass().getName(), "User has change its password '" + user.getScreenName()
					+ "'.");
			return obtainedUser;
		}
		return null;
	}

}
