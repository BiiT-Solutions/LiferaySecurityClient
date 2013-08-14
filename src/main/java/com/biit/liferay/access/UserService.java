package com.biit.liferay.access;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import com.liferay.portal.model.CompanySoap;
import com.liferay.portal.model.UserSoap;
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
		userServiceSoap = locatorUser.getPortal_UserService(AccessUtils.getURL(loginUser, password, SERVICE_USER_NAME));
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

}
