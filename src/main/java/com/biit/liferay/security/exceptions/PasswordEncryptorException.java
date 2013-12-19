package com.biit.liferay.security.exceptions;

public class PasswordEncryptorException extends Exception {

	public PasswordEncryptorException(String text) {
		super(text);
	}

	public PasswordEncryptorException(String message, Exception e) {
		super(message, e);
	}
}
