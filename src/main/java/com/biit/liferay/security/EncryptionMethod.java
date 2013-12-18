package com.biit.liferay.security;

import com.biit.liferay.security.exceptions.InvalidCredentialsException;
import com.liferay.portal.model.Company;

public interface EncryptionMethod {

	String encryptPassword(String plainTextPassword, Company company);

	void validatePassword(String plainTextPassword, String userEncodedPassword, Company company)
			throws InvalidCredentialsException;

}
