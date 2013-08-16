package com.biit.liferay.security;

import com.biit.liferay.security.exceptions.InvalidCredentialsException;

public interface EncryptionMethod {

	String encryptPassword(String plainTextPassword);

	void validatePassword(String plainTextPassword, String userEncodedPassword) throws InvalidCredentialsException;

}
