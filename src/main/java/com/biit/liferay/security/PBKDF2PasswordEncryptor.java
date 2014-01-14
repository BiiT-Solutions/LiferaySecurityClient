package com.biit.liferay.security;

import com.biit.liferay.security.exceptions.InvalidCredentialsException;
import com.biit.security.PBKDF2Encryptor;
import com.biit.security.exceptions.PBKDF2EncryptorException;

public class PBKDF2PasswordEncryptor extends PBKDF2Encryptor {
	public static final String TYPE_PBKDF2 = "PBKDF2";
	public static final String ALGORITHM = "PBKDF2WithHmacSHA1";

	public String[] getSupportedAlgorithmTypes() {
		return new String[] { TYPE_PBKDF2 };
	}

	public static void validate(String newPasswordPlainText,
			String oldPasswordEncrypted) throws InvalidCredentialsException,
			PBKDF2EncryptorException {
		String newEncPwd = encrypt(newPasswordPlainText, oldPasswordEncrypted);

		if (oldPasswordEncrypted == null
				|| !oldPasswordEncrypted.equals(newEncPwd)) {
			throw new InvalidCredentialsException("Password does not match!");
		}
	}
}
