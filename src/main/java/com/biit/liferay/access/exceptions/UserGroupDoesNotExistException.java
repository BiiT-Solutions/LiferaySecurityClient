package com.biit.liferay.access.exceptions;

public class UserGroupDoesNotExistException extends Exception {
	private static final long serialVersionUID = -8126258050535956599L;

	public UserGroupDoesNotExistException(String info) {
		super(info);
	}
}
