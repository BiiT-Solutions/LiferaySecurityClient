package com.biit.liferay.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.axis.encoding.Base64;

import com.biit.liferay.security.exceptions.InvalidCredentialsException;
import com.biit.liferay.security.exceptions.PwdEncryptorException;
import com.liferay.portal.model.Company;

public class BasicEncryptionMethod implements EncryptionMethod {
	private static final String DEFAULT_ALGORITHM = "SHA";
	private static final String ENCODING = "UTF-8";
	private static final BasicEncryptionMethod instance = new BasicEncryptionMethod();

	private BasicEncryptionMethod() {

	}

	public static BasicEncryptionMethod getInstance() {
		return instance;
	}

	@Override
	public String encryptPassword(String plainTextPassword, Company company) {
		return digestBase64(DEFAULT_ALGORITHM, plainTextPassword, company);
	}

	private static String digestBase64(String algorithm, String text, Company company) {
		MessageDigest messageDigest;
		try {
			messageDigest = MessageDigest.getInstance(algorithm);
			messageDigest.update(text.getBytes(ENCODING));
			return Base64.encode(messageDigest.digest());

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void validatePassword(String plainTextPassword, String userEncodedPassword, Company company)
			throws InvalidCredentialsException {
		String currentEncryptedPassword = encryptPassword(plainTextPassword, company);
		if (!currentEncryptedPassword.equals(userEncodedPassword)) {
			throw new InvalidCredentialsException("Password does not match.");
		}
	}
}
