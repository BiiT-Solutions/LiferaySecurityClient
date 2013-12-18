package com.biit.liferay.security.exceptions;

public class PwdEncryptorException extends Exception {

	public PwdEncryptorException(String text) {
		super(text);
	}

	public PwdEncryptorException(String message, Exception e) {
		super(message, e);
	}
}
