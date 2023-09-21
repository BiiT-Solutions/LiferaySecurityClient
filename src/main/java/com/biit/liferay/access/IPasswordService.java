package com.biit.liferay.access;

import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.WebServiceAccessError;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.security.exceptions.AuthenticationRequired;

import java.io.IOException;

public interface IPasswordService extends IUserService {

    IUser<Long> updatePassword(IUser<Long> user, String plainTextPassword) throws NotConnectedToWebServiceException,
            IOException, AuthenticationRequired, WebServiceAccessError;

}
