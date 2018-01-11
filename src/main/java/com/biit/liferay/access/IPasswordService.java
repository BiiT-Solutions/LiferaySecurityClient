package com.biit.liferay.access;

import java.io.IOException;

import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.WebServiceAccessError;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.security.exceptions.AuthenticationRequired;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface IPasswordService extends IUserService {

	IUser<Long> updatePassword(IUser<Long> user, String plainTextPassword) throws NotConnectedToWebServiceException, JsonParseException, JsonMappingException,
			IOException, AuthenticationRequired, WebServiceAccessError;

}
