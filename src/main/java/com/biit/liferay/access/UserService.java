package com.biit.liferay.access;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.configuration.ConfigurationReader;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.http.UserServiceSoap;
import com.liferay.portal.service.http.UserServiceSoapServiceLocator;

/**
 * This class represent allows to obtain a user from a liferay portal.
 */
public class UserService implements LiferayService {
	private final static String SERVICE_USER_NAME = "Portal_UserService";
	private UserServiceSoap userServiceSoap = null;
	private final static UserService instance = new UserService();

	private UserService() {
	}

	@Override
	public void connectToWebService(String loginUser, String password) throws ServiceException {
		// Locate the User service
		UserServiceSoapServiceLocator locatorUser = new UserServiceSoapServiceLocator();
		userServiceSoap = locatorUser.getPortal_UserService(AccessUtils.getLiferayUrl(loginUser, password,
				SERVICE_USER_NAME));
	}

	@Override
	public void connectToWebService() throws ServiceException {
		// Read user and password.
		String loginUser = ConfigurationReader.getInstance().getUser();
		String password = ConfigurationReader.getInstance().getPassword();
		// Locate the User service.
		UserServiceSoapServiceLocator locatorUser = new UserServiceSoapServiceLocator();
		userServiceSoap = locatorUser.getPortal_UserService(AccessUtils.getLiferayUrl(loginUser, password,
				SERVICE_USER_NAME));
	}

	public static UserService getInstance() {
		return instance;
	}

	@Override
	public boolean isNotConnected() {
		return userServiceSoap == null;
	}

	@Override
	public void checkConnection() throws NotConnectedToWebServiceException {
		if (isNotConnected()) {
			throw new NotConnectedToWebServiceException(
					"user credentials are needed to use Liferay webservice. Use the connect method for this.");
		}
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
		checkConnection();
		return userServiceSoap.getUserByEmailAddress(companySoap.getCompanyId(), emailAddress);
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
	public User getUserById(long userId) throws RemoteException, NotConnectedToWebServiceException {
		checkConnection();
		return userServiceSoap.getUserById(userId);
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
		checkConnection();
		return userServiceSoap.getUserByScreenName(companySoap.getCompanyId(), screenName);
	}

	/**
	 * Adds an user to liferay portal.
	 * 
	 * @param companySoap
	 * @param user
	 * @throws RemoteException
	 * @throws NotConnectedToWebServiceException
	 */
	public void addUser(Company companySoap, User user) throws RemoteException, NotConnectedToWebServiceException {
		addUser(companySoap, user.getPassword(), user.getScreenName(), user.getEmailAddress(), user.getFacebookId(),
				user.getOpenId(), user.getTimeZoneId(), user.getFirstName(), user.getMiddleName(), user.getLastName());

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
	 * @throws RemoteException
	 * @throws NotConnectedToWebServiceException
	 */
	public void addUser(Company companySoap, String password, String screenName, String emailAddress, long facebookId,
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
		userServiceSoap.addUser(companySoap.getCompanyId(), autoPassword, password, password, autoScreenName,
				screenName, emailAddress, facebookId, openId, locale, firstName, middleName, lastName, 0, 0, false, 1,
				1, 1970, "", null, null, null, null, false, new ServiceContext());
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
		userServiceSoap.deleteUser(user.getUserId());
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
		userServiceSoap.updatePassword(user.getUserId(), plainTextPassword, plainTextPassword, false);
		user.setPassword(plainTextPassword);
	}

}
