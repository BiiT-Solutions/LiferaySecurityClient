package com.biit.liferay.security;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.UserDoesNotExistException;
import com.biit.liferay.access.exceptions.WebServiceAccessError;
import com.biit.liferay.security.exceptions.InvalidCredentialsException;
import com.biit.security.exceptions.PBKDF2EncryptorException;
import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.entity.IUser;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface IAuthenticationService {

	IUser<Long> authenticate(String userMail, String password) throws InvalidCredentialsException,
			NotConnectedToWebServiceException;

	IGroup<Long> getDefaultGroup(IUser<Long> user) throws NotConnectedToWebServiceException, ClientProtocolException,
			IOException, AuthenticationRequired;

	IUser<Long> getUserByEmail(String userEmail) throws NotConnectedToWebServiceException, UserDoesNotExistException,
			ClientProtocolException, IOException, AuthenticationRequired, WebServiceAccessError;

	IUser<Long> getUserById(long userId) throws NotConnectedToWebServiceException, UserDoesNotExistException,
			ClientProtocolException, IOException, AuthenticationRequired, WebServiceAccessError;

	boolean isInGroup(IGroup<Long> group, IUser<Long> user) throws NotConnectedToWebServiceException,
			ClientProtocolException, IOException, AuthenticationRequired;

	IUser<Long> updatePassword(IUser<Long> user, String plainTextPassword) throws NotConnectedToWebServiceException,
			PBKDF2EncryptorException, JsonParseException, JsonMappingException, IOException, AuthenticationRequired,
			WebServiceAccessError;

}
