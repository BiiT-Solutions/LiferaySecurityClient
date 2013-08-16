package com.biit.liferay.access.exceptions;

public class NotConnectedToWebServiceException extends Exception {
	private static final long serialVersionUID = 7081791659319260502L;

	public NotConnectedToWebServiceException(String info) {
		super(info);
	}
}
