package com.biit.liferay.access;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import com.liferay.portal.model.CompanySoap;
import com.liferay.portal.model.UserSoap;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.http.UserServiceSoap;
import com.liferay.portal.service.http.UserServiceSoapServiceLocator;

/**
 * This class represent allows to obtain a user from a liferay portal.
 */
public class UserService {
	private final static String SERVICE_USER_NAME = "Portal_UserService";
	private UserServiceSoap userServiceSoap;

	public UserService(String loginUser, String password) throws ServiceException {
		// Locate the User service
		UserServiceSoapServiceLocator locatorUser = new UserServiceSoapServiceLocator();
		userServiceSoap = locatorUser.getPortal_UserService(AccessUtils.getLiferayUrl(loginUser, password,
				SERVICE_USER_NAME));
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
	 */
	public UserSoap getUserByEmailAddress(CompanySoap companySoap, String emailAddress) throws RemoteException {
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
	 */
	public UserSoap getUserById(long userId) throws RemoteException {
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
	 */
	public UserSoap getUserByScreenName(CompanySoap companySoap, String screenName) throws RemoteException {
		return userServiceSoap.getUserByScreenName(companySoap.getCompanyId(), screenName);
	}

	/**
	 * Adds an user to liferay portal.
	 * 
	 * @param companySoap
	 * @param user
	 * @throws RemoteException
	 */
	public void addUser(CompanySoap companySoap, UserSoap user) throws RemoteException {
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
	 */
	public void addUser(CompanySoap companySoap, String password, String screenName, String emailAddress,
			long facebookId, String openId, String locale, String firstName, String middleName, String lastName)
			throws RemoteException {
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

	public void deleteUser(UserSoap user) throws RemoteException {
		userServiceSoap.deleteUser(user.getUserId());
	}

}
