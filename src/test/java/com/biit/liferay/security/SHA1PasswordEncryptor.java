package com.biit.liferay.security;

import com.biit.liferay.security.exceptions.InvalidCredentialsException;
import com.biit.security.SHA1Encryptor;
import com.biit.security.exceptions.SHA1EncryptorException;

public class SHA1PasswordEncryptor extends SHA1Encryptor {
	private static final SHA1PasswordEncryptor instance = new SHA1PasswordEncryptor();

	private SHA1PasswordEncryptor() {

	}

	public static SHA1PasswordEncryptor getInstance() {
		return instance;
	}

	public void validatePassword(String passwordPlainText, String passwordEncrypted) throws SHA1EncryptorException,
			InvalidCredentialsException {
		String newEncPwd = encrypt(passwordPlainText);

		if (passwordEncrypted == null || !passwordEncrypted.equals(newEncPwd)) {
			throw new InvalidCredentialsException("Password does not match!");
		}
	}
}
