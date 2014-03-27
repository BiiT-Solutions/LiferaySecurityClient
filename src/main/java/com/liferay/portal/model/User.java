/**
 * user.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.liferay.portal.model;

import java.util.Calendar;
import java.util.Date;

public class User implements java.io.Serializable {

	private boolean agreedToTermsOfUse;

	private java.lang.String comments;

	private long companyId;

	private long contactId;

	private java.util.Calendar createDate;

	private boolean defaultUser;

	private java.lang.String digest;

	private java.lang.String emailAddress;

	private boolean emailAddressVerified;

	private long facebookId;

	private int failedLoginAttempts;

	private java.lang.String firstName;

	private int graceLoginCount;

	private java.lang.String greeting;

	private java.lang.String jobTitle;

	private java.lang.String languageId;

	private java.util.Calendar lastFailedLoginDate;

	private java.util.Calendar lastLoginDate;

	private java.lang.String lastLoginIP;

	private java.lang.String lastName;

	private long ldapServerId;

	private boolean lockout;

	private java.util.Calendar lockoutDate;

	private java.util.Calendar loginDate;

	private java.lang.String loginIP;

	private java.lang.String middleName;

	private java.util.Calendar modifiedDate;

	private java.lang.String openId;

	private java.lang.String password;

	private boolean passwordEncrypted;

	private java.util.Calendar passwordModifiedDate;

	private boolean passwordReset;

	private long portraitId;

	private long primaryKey;

	private java.lang.String reminderQueryAnswer;

	private java.lang.String reminderQueryQuestion;

	private java.lang.String screenName;

	private int status;

	private java.lang.String timeZoneId;

	private long userId;

	private java.lang.String uuid;

	// This information in Liferay is in the Contact object.
	private Date birthday;

	private boolean male;

	public User() {
	}

	public User(boolean agreedToTermsOfUse, java.lang.String comments, long companyId, long contactId,
			java.util.Calendar createDate, boolean defaultUser, java.lang.String digest, java.lang.String emailAddress,
			boolean emailAddressVerified, long facebookId, int failedLoginAttempts, java.lang.String firstName,
			int graceLoginCount, java.lang.String greeting, java.lang.String jobTitle, java.lang.String languageId,
			java.util.Calendar lastFailedLoginDate, java.util.Calendar lastLoginDate, java.lang.String lastLoginIP,
			java.lang.String lastName, long ldapServerId, boolean lockout, java.util.Calendar lockoutDate,
			java.util.Calendar loginDate, java.lang.String loginIP, java.lang.String middleName,
			java.util.Calendar modifiedDate, java.lang.String openId, java.lang.String password,
			boolean passwordEncrypted, java.util.Calendar passwordModifiedDate, boolean passwordReset, long portraitId,
			long primaryKey, java.lang.String reminderQueryAnswer, java.lang.String reminderQueryQuestion,
			java.lang.String screenName, int status, java.lang.String timeZoneId, long userId, java.lang.String uuid) {
		this.agreedToTermsOfUse = agreedToTermsOfUse;
		this.comments = comments;
		this.companyId = companyId;
		this.contactId = contactId;
		this.createDate = createDate;
		this.defaultUser = defaultUser;
		this.digest = digest;
		this.emailAddress = emailAddress;
		this.emailAddressVerified = emailAddressVerified;
		this.facebookId = facebookId;
		this.failedLoginAttempts = failedLoginAttempts;
		this.firstName = firstName;
		this.graceLoginCount = graceLoginCount;
		this.greeting = greeting;
		this.jobTitle = jobTitle;
		this.languageId = languageId;
		this.lastFailedLoginDate = lastFailedLoginDate;
		this.lastLoginDate = lastLoginDate;
		this.lastLoginIP = lastLoginIP;
		this.lastName = lastName;
		this.ldapServerId = ldapServerId;
		this.lockout = lockout;
		this.lockoutDate = lockoutDate;
		this.loginDate = loginDate;
		this.loginIP = loginIP;
		this.middleName = middleName;
		this.modifiedDate = modifiedDate;
		this.openId = openId;
		this.password = password;
		this.passwordEncrypted = passwordEncrypted;
		this.passwordModifiedDate = passwordModifiedDate;
		this.passwordReset = passwordReset;
		this.portraitId = portraitId;
		this.primaryKey = primaryKey;
		this.reminderQueryAnswer = reminderQueryAnswer;
		this.reminderQueryQuestion = reminderQueryQuestion;
		this.screenName = screenName;
		this.status = status;
		this.timeZoneId = timeZoneId;
		this.userId = userId;
		this.uuid = uuid;
	}

	/**
	 * Gets the agreedToTermsOfUse value for this user.
	 * 
	 * @return agreedToTermsOfUse
	 */
	public boolean isAgreedToTermsOfUse() {
		return agreedToTermsOfUse;
	}

	/**
	 * Sets the agreedToTermsOfUse value for this user.
	 * 
	 * @param agreedToTermsOfUse
	 */
	public void setAgreedToTermsOfUse(boolean agreedToTermsOfUse) {
		this.agreedToTermsOfUse = agreedToTermsOfUse;
	}

	/**
	 * Gets the comments value for this user.
	 * 
	 * @return comments
	 */
	public java.lang.String getComments() {
		return comments;
	}

	/**
	 * Sets the comments value for this user.
	 * 
	 * @param comments
	 */
	public void setComments(java.lang.String comments) {
		this.comments = comments;
	}

	/**
	 * Gets the companyId value for this user.
	 * 
	 * @return companyId
	 */
	public long getCompanyId() {
		return companyId;
	}

	/**
	 * Sets the companyId value for this user.
	 * 
	 * @param companyId
	 */
	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	/**
	 * Gets the contactId value for this user.
	 * 
	 * @return contactId
	 */
	public long getContactId() {
		return contactId;
	}

	/**
	 * Sets the contactId value for this user.
	 * 
	 * @param contactId
	 */
	public void setContactId(long contactId) {
		this.contactId = contactId;
	}

	/**
	 * Gets the createDate value for this user.
	 * 
	 * @return createDate
	 */
	public java.util.Calendar getCreateDate() {
		return createDate;
	}

	/**
	 * Sets the createDate value for this user.
	 * 
	 * @param createDate
	 */
	public void setCreateDate(java.util.Calendar createDate) {
		this.createDate = createDate;
	}

	/**
	 * Gets the defaultUser value for this user.
	 * 
	 * @return defaultUser
	 */
	public boolean isDefaultUser() {
		return defaultUser;
	}

	/**
	 * Sets the defaultUser value for this user.
	 * 
	 * @param defaultUser
	 */
	public void setDefaultUser(boolean defaultUser) {
		this.defaultUser = defaultUser;
	}

	/**
	 * Gets the digest value for this user.
	 * 
	 * @return digest
	 */
	public java.lang.String getDigest() {
		return digest;
	}

	/**
	 * Sets the digest value for this user.
	 * 
	 * @param digest
	 */
	public void setDigest(java.lang.String digest) {
		this.digest = digest;
	}

	/**
	 * Gets the emailAddress value for this user.
	 * 
	 * @return emailAddress
	 */
	public java.lang.String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * Sets the emailAddress value for this user.
	 * 
	 * @param emailAddress
	 */
	public void setEmailAddress(java.lang.String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * Gets the emailAddressVerified value for this user.
	 * 
	 * @return emailAddressVerified
	 */
	public boolean isEmailAddressVerified() {
		return emailAddressVerified;
	}

	/**
	 * Sets the emailAddressVerified value for this user.
	 * 
	 * @param emailAddressVerified
	 */
	public void setEmailAddressVerified(boolean emailAddressVerified) {
		this.emailAddressVerified = emailAddressVerified;
	}

	/**
	 * Gets the facebookId value for this user.
	 * 
	 * @return facebookId
	 */
	public long getFacebookId() {
		return facebookId;
	}

	/**
	 * Sets the facebookId value for this user.
	 * 
	 * @param facebookId
	 */
	public void setFacebookId(long facebookId) {
		this.facebookId = facebookId;
	}

	/**
	 * Gets the failedLoginAttempts value for this user.
	 * 
	 * @return failedLoginAttempts
	 */
	public int getFailedLoginAttempts() {
		return failedLoginAttempts;
	}

	/**
	 * Sets the failedLoginAttempts value for this user.
	 * 
	 * @param failedLoginAttempts
	 */
	public void setFailedLoginAttempts(int failedLoginAttempts) {
		this.failedLoginAttempts = failedLoginAttempts;
	}

	/**
	 * Gets the firstName value for this user.
	 * 
	 * @return firstName
	 */
	public java.lang.String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the firstName value for this user.
	 * 
	 * @param firstName
	 */
	public void setFirstName(java.lang.String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets the graceLoginCount value for this user.
	 * 
	 * @return graceLoginCount
	 */
	public int getGraceLoginCount() {
		return graceLoginCount;
	}

	/**
	 * Sets the graceLoginCount value for this user.
	 * 
	 * @param graceLoginCount
	 */
	public void setGraceLoginCount(int graceLoginCount) {
		this.graceLoginCount = graceLoginCount;
	}

	/**
	 * Gets the greeting value for this user.
	 * 
	 * @return greeting
	 */
	public java.lang.String getGreeting() {
		return greeting;
	}

	/**
	 * Sets the greeting value for this user.
	 * 
	 * @param greeting
	 */
	public void setGreeting(java.lang.String greeting) {
		this.greeting = greeting;
	}

	/**
	 * Gets the jobTitle value for this user.
	 * 
	 * @return jobTitle
	 */
	public java.lang.String getJobTitle() {
		return jobTitle;
	}

	/**
	 * Sets the jobTitle value for this user.
	 * 
	 * @param jobTitle
	 */
	public void setJobTitle(java.lang.String jobTitle) {
		this.jobTitle = jobTitle;
	}

	/**
	 * Gets the languageId value for this user.
	 * 
	 * @return languageId
	 */
	public java.lang.String getLanguageId() {
		return languageId;
	}

	/**
	 * Sets the languageId value for this user.
	 * 
	 * @param languageId
	 */
	public void setLanguageId(java.lang.String languageId) {
		this.languageId = languageId;
	}

	/**
	 * Gets the lastFailedLoginDate value for this user.
	 * 
	 * @return lastFailedLoginDate
	 */
	public java.util.Calendar getLastFailedLoginDate() {
		return lastFailedLoginDate;
	}

	/**
	 * Sets the lastFailedLoginDate value for this user.
	 * 
	 * @param lastFailedLoginDate
	 */
	public void setLastFailedLoginDate(java.util.Calendar lastFailedLoginDate) {
		this.lastFailedLoginDate = lastFailedLoginDate;
	}

	/**
	 * Gets the lastLoginDate value for this user.
	 * 
	 * @return lastLoginDate
	 */
	public java.util.Calendar getLastLoginDate() {
		return lastLoginDate;
	}

	/**
	 * Sets the lastLoginDate value for this user.
	 * 
	 * @param lastLoginDate
	 */
	public void setLastLoginDate(java.util.Calendar lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	/**
	 * Gets the lastLoginIP value for this user.
	 * 
	 * @return lastLoginIP
	 */
	public java.lang.String getLastLoginIP() {
		return lastLoginIP;
	}

	/**
	 * Sets the lastLoginIP value for this user.
	 * 
	 * @param lastLoginIP
	 */
	public void setLastLoginIP(java.lang.String lastLoginIP) {
		this.lastLoginIP = lastLoginIP;
	}

	/**
	 * Gets the lastName value for this user.
	 * 
	 * @return lastName
	 */
	public java.lang.String getLastName() {
		return lastName;
	}

	/**
	 * Sets the lastName value for this user.
	 * 
	 * @param lastName
	 */
	public void setLastName(java.lang.String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Gets the ldapServerId value for this user.
	 * 
	 * @return ldapServerId
	 */
	public long getLdapServerId() {
		return ldapServerId;
	}

	/**
	 * Sets the ldapServerId value for this user.
	 * 
	 * @param ldapServerId
	 */
	public void setLdapServerId(long ldapServerId) {
		this.ldapServerId = ldapServerId;
	}

	/**
	 * Gets the lockout value for this user.
	 * 
	 * @return lockout
	 */
	public boolean isLockout() {
		return lockout;
	}

	/**
	 * Sets the lockout value for this user.
	 * 
	 * @param lockout
	 */
	public void setLockout(boolean lockout) {
		this.lockout = lockout;
	}

	/**
	 * Gets the lockoutDate value for this user.
	 * 
	 * @return lockoutDate
	 */
	public java.util.Calendar getLockoutDate() {
		return lockoutDate;
	}

	/**
	 * Sets the lockoutDate value for this user.
	 * 
	 * @param lockoutDate
	 */
	public void setLockoutDate(java.util.Calendar lockoutDate) {
		this.lockoutDate = lockoutDate;
	}

	/**
	 * Gets the loginDate value for this user.
	 * 
	 * @return loginDate
	 */
	public java.util.Calendar getLoginDate() {
		return loginDate;
	}

	/**
	 * Sets the loginDate value for this user.
	 * 
	 * @param loginDate
	 */
	public void setLoginDate(java.util.Calendar loginDate) {
		this.loginDate = loginDate;
	}

	/**
	 * Gets the loginIP value for this user.
	 * 
	 * @return loginIP
	 */
	public java.lang.String getLoginIP() {
		return loginIP;
	}

	/**
	 * Sets the loginIP value for this user.
	 * 
	 * @param loginIP
	 */
	public void setLoginIP(java.lang.String loginIP) {
		this.loginIP = loginIP;
	}

	/**
	 * Gets the middleName value for this user.
	 * 
	 * @return middleName
	 */
	public java.lang.String getMiddleName() {
		return middleName;
	}

	/**
	 * Sets the middleName value for this user.
	 * 
	 * @param middleName
	 */
	public void setMiddleName(java.lang.String middleName) {
		this.middleName = middleName;
	}

	/**
	 * Gets the modifiedDate value for this user.
	 * 
	 * @return modifiedDate
	 */
	public java.util.Calendar getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * Sets the modifiedDate value for this user.
	 * 
	 * @param modifiedDate
	 */
	public void setModifiedDate(java.util.Calendar modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * Gets the openId value for this user.
	 * 
	 * @return openId
	 */
	public java.lang.String getOpenId() {
		return openId;
	}

	/**
	 * Sets the openId value for this user.
	 * 
	 * @param openId
	 */
	public void setOpenId(java.lang.String openId) {
		this.openId = openId;
	}

	/**
	 * Gets the password value for this user.
	 * 
	 * @return password
	 */
	public java.lang.String getPassword() {
		return password;
	}

	/**
	 * Sets the password value for this user.
	 * 
	 * @param password
	 */
	public void setPassword(java.lang.String password) {
		this.password = password;
	}

	/**
	 * Gets the passwordEncrypted value for this user.
	 * 
	 * @return passwordEncrypted
	 */
	public boolean isPasswordEncrypted() {
		return passwordEncrypted;
	}

	/**
	 * Sets the passwordEncrypted value for this user.
	 * 
	 * @param passwordEncrypted
	 */
	public void setPasswordEncrypted(boolean passwordEncrypted) {
		this.passwordEncrypted = passwordEncrypted;
	}

	/**
	 * Gets the passwordModifiedDate value for this user.
	 * 
	 * @return passwordModifiedDate
	 */
	public java.util.Calendar getPasswordModifiedDate() {
		return passwordModifiedDate;
	}

	/**
	 * Sets the passwordModifiedDate value for this user.
	 * 
	 * @param passwordModifiedDate
	 */
	public void setPasswordModifiedDate(java.util.Calendar passwordModifiedDate) {
		this.passwordModifiedDate = passwordModifiedDate;
	}

	/**
	 * Gets the passwordReset value for this user.
	 * 
	 * @return passwordReset
	 */
	public boolean isPasswordReset() {
		return passwordReset;
	}

	/**
	 * Sets the passwordReset value for this user.
	 * 
	 * @param passwordReset
	 */
	public void setPasswordReset(boolean passwordReset) {
		this.passwordReset = passwordReset;
	}

	/**
	 * Gets the portraitId value for this user.
	 * 
	 * @return portraitId
	 */
	public long getPortraitId() {
		return portraitId;
	}

	/**
	 * Sets the portraitId value for this user.
	 * 
	 * @param portraitId
	 */
	public void setPortraitId(long portraitId) {
		this.portraitId = portraitId;
	}

	/**
	 * Gets the primaryKey value for this user.
	 * 
	 * @return primaryKey
	 */
	public long getPrimaryKey() {
		return primaryKey;
	}

	/**
	 * Sets the primaryKey value for this user.
	 * 
	 * @param primaryKey
	 */
	public void setPrimaryKey(long primaryKey) {
		this.primaryKey = primaryKey;
	}

	/**
	 * Gets the reminderQueryAnswer value for this user.
	 * 
	 * @return reminderQueryAnswer
	 */
	public java.lang.String getReminderQueryAnswer() {
		return reminderQueryAnswer;
	}

	/**
	 * Sets the reminderQueryAnswer value for this user.
	 * 
	 * @param reminderQueryAnswer
	 */
	public void setReminderQueryAnswer(java.lang.String reminderQueryAnswer) {
		this.reminderQueryAnswer = reminderQueryAnswer;
	}

	/**
	 * Gets the reminderQueryQuestion value for this user.
	 * 
	 * @return reminderQueryQuestion
	 */
	public java.lang.String getReminderQueryQuestion() {
		return reminderQueryQuestion;
	}

	/**
	 * Sets the reminderQueryQuestion value for this user.
	 * 
	 * @param reminderQueryQuestion
	 */
	public void setReminderQueryQuestion(java.lang.String reminderQueryQuestion) {
		this.reminderQueryQuestion = reminderQueryQuestion;
	}

	/**
	 * Gets the screenName value for this user.
	 * 
	 * @return screenName
	 */
	public java.lang.String getScreenName() {
		return screenName;
	}

	/**
	 * Sets the screenName value for this user.
	 * 
	 * @param screenName
	 */
	public void setScreenName(java.lang.String screenName) {
		this.screenName = screenName;
	}

	/**
	 * Gets the status value for this user.
	 * 
	 * @return status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Sets the status value for this user.
	 * 
	 * @param status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * Gets the timeZoneId value for this user.
	 * 
	 * @return timeZoneId
	 */
	public java.lang.String getTimeZoneId() {
		return timeZoneId;
	}

	/**
	 * Sets the timeZoneId value for this user.
	 * 
	 * @param timeZoneId
	 */
	public void setTimeZoneId(java.lang.String timeZoneId) {
		this.timeZoneId = timeZoneId;
	}

	/**
	 * Gets the userId value for this user.
	 * 
	 * @return userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * Sets the userId value for this user.
	 * 
	 * @param userId
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * Gets the uuid value for this user.
	 * 
	 * @return uuid
	 */
	public java.lang.String getUuid() {
		return uuid;
	}

	/**
	 * Sets the uuid value for this user.
	 * 
	 * @param uuid
	 */
	public void setUuid(java.lang.String uuid) {
		this.uuid = uuid;
	}

	/**
	 * Gets the user birthday.
	 * 
	 * @return
	 */
	public Date getBirthday() {
		return birthday;
	}

	/**
	 * Sets the user birthday.
	 */
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public boolean isMale() {
		return male;
	}

	public void setMale(boolean male) {
		this.male = male;
	}

	/**
	 * Returns the gender of the user.
	 * 
	 * @return
	 */
	public Gender getGender() {
		if (male) {
			return Gender.MALE;
		} else {
			return Gender.FEMALE;
		}
	}

	/**
	 * Sets the gender of a user.
	 * 
	 * @param gender
	 */
	public void setGender(Gender gender) {
		if (gender == Gender.MALE) {
			male = true;
		} else {
			male = false;
		}
	}

	/**
	 * Returns the number of years that a user has.
	 * 
	 * @return
	 */
	public int getAge() {
		Calendar dob = Calendar.getInstance();
		dob.setTime(birthday);
		Calendar today = Calendar.getInstance();
		int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
		if (today.get(Calendar.MONTH) < dob.get(Calendar.MONTH)) {
			age--;
		} else if (today.get(Calendar.MONTH) == dob.get(Calendar.MONTH)
				&& today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)) {
			age--;
		}
		return age;
	}

}
