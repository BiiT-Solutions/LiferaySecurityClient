package com.biit.liferay.access.exceptions;

public class NotValidPasswordException extends Exception {
	private static final long serialVersionUID = -6825746285158248770L;

	public NotValidPasswordException(String info) {
		super(info);
	}
}
