package com.biit.liferay.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.axis.encoding.Base64;

import com.biit.liferay.security.exceptions.InvalidCredentialsException;

public class BasicSecurityMethod implements SecurityMethod {
	private static final String DEFAULT_ALGORITHM = "SHA";
	private static final String ENCODING = "UTF-8";

	@Override
	public String encryptPassword(String plainTextPassword) {
		return digestBase64(DEFAULT_ALGORITHM, plainTextPassword);
	}

	private static String digestBase64(String algorithm, String text) {
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
	public void validatePassword(String plainTextPassword, String userEncodedPassword)
			throws InvalidCredentialsException {
		String currentEncryptedPassword = encryptPassword(plainTextPassword);
		if (!currentEncryptedPassword.equals(userEncodedPassword)) {
			throw new InvalidCredentialsException("Password does not match.");
		}
	}
}
