package com.biit.liferay.security;

import com.biit.liferay.security.exceptions.InvalidCredentialsException;

public interface EncryptionMethod {

	String encrypt(String plainTextPassword);

	void validate(String plainTextPassword, String userEncodedPassword)
			throws InvalidCredentialsException;

}
