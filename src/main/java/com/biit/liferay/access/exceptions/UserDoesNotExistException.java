package com.biit.liferay.access.exceptions;

public class UserDoesNotExistException extends Exception {
	private static final long serialVersionUID = -8126258050535956599L;

	public UserDoesNotExistException(String info) {
		super(info);
	}
}
