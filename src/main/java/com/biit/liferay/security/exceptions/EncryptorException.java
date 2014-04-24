package com.biit.liferay.security.exceptions;

public class EncryptorException extends Exception {

	private static final long serialVersionUID = -8449693987156888975L;

	public EncryptorException(String message) {
		super(message);
	}

	public EncryptorException(Exception e) {
		super(e);
	}
}
