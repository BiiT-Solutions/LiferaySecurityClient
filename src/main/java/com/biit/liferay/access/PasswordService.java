package com.biit.liferay.access;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.WebServiceAccessError;
import com.biit.liferay.log.LiferayClientLogger;
import com.biit.security.exceptions.PBKDF2EncryptorException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.liferay.portal.model.User;

public class PasswordService extends UserService {

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

		String result = getHttpResponse("user/update-password", params);
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

	@Override
	public List<User> decodeListFromJson(String json, Class<User> objectClass) throws JsonParseException,
			JsonMappingException, IOException {
		// TODO Auto-generated method stub
		return null;
	}
}
