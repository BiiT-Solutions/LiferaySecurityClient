package com.biit.liferay.security;

import com.biit.liferay.security.exceptions.InvalidCredentialsException;

public interface SecurityMethod {

	public String encryptPassword(String plainTextPassword);

	public void validatePassword(String plainTextPassword, String userEncodedPassword)
			throws InvalidCredentialsException;

}
