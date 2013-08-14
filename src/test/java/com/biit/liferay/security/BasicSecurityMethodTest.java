package com.biit.liferay.security;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = { "basicSecurityMethodTest" })
public class BasicSecurityMethodTest {
	private final static String PLAIN_PASSWORD = "asd123";
	private final static String LIFERAY_BASIC_ENCRYPTED_PASSWORD = "KJG6zu7xZS7mmClNoOcbp4oqQGQ=";

	@Test
	public void encriptionNotEquals() {
		SecurityMethod security = new BasicSecurityMethod();
		String passwordEncrypted = security.encryptPassword("asd124");
		Assert.assertNotEquals(passwordEncrypted, LIFERAY_BASIC_ENCRYPTED_PASSWORD);
	}

	@Test
	public void encriptionEquals() {
		SecurityMethod security = new BasicSecurityMethod();
		String passwordEncrypted = security.encryptPassword(PLAIN_PASSWORD);
		Assert.assertEquals(passwordEncrypted, LIFERAY_BASIC_ENCRYPTED_PASSWORD);
	}
}
