package com.biit.liferay.security.exceptions;

public class EncryptorException extends Exception {

	public EncryptorException(String message) {
		super(message);
	}

	public EncryptorException(Exception e) {
		super(e);
	}
}
