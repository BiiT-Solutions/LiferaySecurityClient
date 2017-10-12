package com.biit.liferay.access;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.WebServiceAccessError;
import com.biit.liferay.log.LiferayClientLogger;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.security.exceptions.AuthenticationRequired;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.liferay.portal.model.User;

public class PasswordService extends UserService {

	@Override
	public Set<IUser<Long>> decodeListFromJson(String json, Class<User> objectClass) throws JsonParseException, JsonMappingException, IOException {
		return null;
	}

	/**
	 * Updates the password of a user.
	 * 
	 * @param user
	 * @param plainTextPassword
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @throws AuthenticationRequired
	 * @throws WebServiceAccessError
	 */
	public IUser<Long> updatePassword(IUser<Long> user, String plainTextPassword) throws NotConnectedToWebServiceException, JsonParseException,
			JsonMappingException, IOException, AuthenticationRequired, WebServiceAccessError {
		checkConnection();
		
		if(user==null){
			LiferayClientLogger.info(this.getClass().getName(), "User is null.");
			return null;
		}

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userId", user.getId() + ""));
		params.add(new BasicNameValuePair("password1", plainTextPassword));
		params.add(new BasicNameValuePair("password2", plainTextPassword));
		params.add(new BasicNameValuePair("passwordReset", "false"));

		String result = getHttpResponse("user/update-password", params);
		if (result != null) {
			// A Simple JSON Response Read
			IUser<Long> obtainedUser = decodeFromJson(result, User.class);
			user.setPassword(obtainedUser.getPassword());
			LiferayClientLogger.info(this.getClass().getName(), "User has change its password '" + user.getUniqueName() + "'.");
			return obtainedUser;
		}
		return null;
	}
}
