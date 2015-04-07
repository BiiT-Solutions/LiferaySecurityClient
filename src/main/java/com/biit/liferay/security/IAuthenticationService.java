package com.biit.liferay.security;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.UserDoesNotExistException;
import com.biit.liferay.access.exceptions.WebServiceAccessError;
import com.biit.liferay.security.exceptions.InvalidCredentialsException;
import com.biit.security.exceptions.PBKDF2EncryptorException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;

public interface IAuthenticationService {

	User authenticate(String userMail, String password) throws InvalidCredentialsException,
			NotConnectedToWebServiceException;

	UserGroup getDefaultGroup(User user) throws NotConnectedToWebServiceException, ClientProtocolException,
			IOException, AuthenticationRequired;

	User getUserByEmail(String userEmail) throws NotConnectedToWebServiceException, UserDoesNotExistException,
			ClientProtocolException, IOException, AuthenticationRequired, WebServiceAccessError;

	User getUserById(long userId) throws NotConnectedToWebServiceException, UserDoesNotExistException,
			ClientProtocolException, IOException, AuthenticationRequired, WebServiceAccessError;

	boolean isInGroup(UserGroup group, User user) throws NotConnectedToWebServiceException, ClientProtocolException,
			IOException, AuthenticationRequired;

	User updatePassword(User user, String plainTextPassword) throws NotConnectedToWebServiceException,
			PBKDF2EncryptorException, JsonParseException, JsonMappingException, IOException, AuthenticationRequired,
			WebServiceAccessError;

}
