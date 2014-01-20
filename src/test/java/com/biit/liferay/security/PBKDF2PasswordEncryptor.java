package com.biit.liferay.security;

import com.biit.liferay.security.exceptions.InvalidCredentialsException;
import com.biit.security.PBKDF2Encryptor;
import com.biit.security.exceptions.PBKDF2EncryptorException;

public class PBKDF2PasswordEncryptor extends PBKDF2Encryptor {
	public static final String TYPE_PBKDF2 = "PBKDF2";
	public static final String ALGORITHM = "PBKDF2WithHmacSHA1";
	private static PBKDF2PasswordEncryptor instance;

	private PBKDF2PasswordEncryptor() {

	}

	public static PBKDF2PasswordEncryptor getInstance() {
		if (instance == null) {
			instance = new PBKDF2PasswordEncryptor();
		}
		return instance;
	}

	public String[] getSupportedAlgorithmTypes() {
		return new String[] { TYPE_PBKDF2 };
	}

	/**
	 * Validates a password. Check the password in plain text against the codified one.
	 * 
	 * @param passwordPlainText
	 * @param passwordEncrypted
	 * @throws InvalidCredentialsException
	 * @throws PBKDF2EncryptorException
	 */
	public void validate(String passwordPlainText, String passwordEncrypted) throws InvalidCredentialsException,
			PBKDF2EncryptorException {
		String newEncPwd = encrypt(passwordPlainText, passwordEncrypted);

		if (passwordEncrypted == null || !passwordEncrypted.equals(newEncPwd)) {
			throw new InvalidCredentialsException("Password does not match!");
		}
	}

}
