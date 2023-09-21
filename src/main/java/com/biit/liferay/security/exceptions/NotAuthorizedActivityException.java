package com.biit.liferay.security.exceptions;

public class NotAuthorizedActivityException extends Exception {
    private static final long serialVersionUID = 984965016369080807L;

    public NotAuthorizedActivityException(String info) {
        super(info);
    }
}
