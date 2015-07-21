package com.biit.liferay.access;

import org.testng.annotations.Test;

import com.biit.liferay.access.exceptions.NotValidPasswordException;
import com.biit.liferay.security.AccessUtils;

public class AccessUtilsTest {

	@Test(groups = { "password" }, expectedExceptions = NotValidPasswordException.class)
	public void notValidPassword() throws NotValidPasswordException {
		AccessUtils.checkPassword("111@22");
	}
	
	@Test(groups = { "password" }, expectedExceptions = NotValidPasswordException.class)
	public void notValidPassword2() throws NotValidPasswordException {
		AccessUtils.checkPassword("asd¡");
	}

	@Test(groups = { "password" })
	public void validPassword() throws NotValidPasswordException {
		AccessUtils.checkPassword("A!Sa(dfgH*))!120");
	}
}
