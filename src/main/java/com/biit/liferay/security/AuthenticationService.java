package com.biit.liferay.security;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import com.biit.liferay.access.CompanyService;
import com.biit.liferay.access.UserService;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.security.exceptions.InvalidCredentialsException;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.User;

public class AuthenticationService {

	private final static String LOGIN_USER = "test@liferay.com";
	private final static String LOGIN_PASSWORD = "test";
	private final static String TEST_USER = "testUser";
	private final static String TEST_USER_MAIL = TEST_USER + "@biit-sourcing.com";
	private final static String VIRTUALHOST = "localhost";

	private final static AuthenticationService instance = new AuthenticationService();

	private AuthenticationService() {

	}

	public static AuthenticationService getInstance() {
		return instance;
	}

	public Company getCompany() throws ServiceException, RemoteException {
		CompanyService companyService = new CompanyService(LOGIN_USER, LOGIN_PASSWORD);
		Company company = companyService.getCompanyByVirtualHost(VIRTUALHOST);
		return company;
	}

	public User authenticate(String userName, String password) throws InvalidCredentialsException, ServiceException,
			RemoteException, NotConnectedToWebServiceException {
		// Login fails if either the username or password is null
		if (userName == null || password == null) {
			throw new InvalidCredentialsException("No fields filled up.");
		}

		User user = UserService.getInstance().getUserByEmailAddress(getCompany(), TEST_USER_MAIL);

		if (user == null) {
			throw new InvalidCredentialsException("User does not exist.");
		}

		BasicEncryptionMethod.getInstance().validatePassword(password, user.getPassword());
		return user;
	}
}
