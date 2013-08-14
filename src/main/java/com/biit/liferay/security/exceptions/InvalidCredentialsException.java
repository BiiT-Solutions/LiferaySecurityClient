package com.biit.liferay.security.exceptions;

public class InvalidCredentialsException extends Exception {
	private static final long serialVersionUID = 2624983905858473042L;

	public InvalidCredentialsException(String info) {
		super(info);
	}

}
