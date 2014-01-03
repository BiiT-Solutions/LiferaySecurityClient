/**
 * PhoneSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.liferay.portal.model;

public class Phone implements java.io.Serializable {
	private long classNameId;

	private long classPK;

	private long companyId;

	private java.util.Calendar createDate;

	private java.lang.String extension;

	private java.util.Calendar modifiedDate;

	private java.lang.String number;

	private long phoneId;

	private boolean primary;

	private long primaryKey;

	private int typeId;

	private long userId;

	private java.lang.String userName;

	private java.lang.String uuid;

	public Phone() {
	}

	public Phone(long classNameId, long classPK, long companyId, java.util.Calendar createDate,
			java.lang.String extension, java.util.Calendar modifiedDate, java.lang.String number, long phoneId,
			boolean primary, long primaryKey, int typeId, long userId, java.lang.String userName, java.lang.String uuid) {
		this.classNameId = classNameId;
		this.classPK = classPK;
		this.companyId = companyId;
		this.createDate = createDate;
		this.extension = extension;
		this.modifiedDate = modifiedDate;
		this.number = number;
		this.phoneId = phoneId;
		this.primary = primary;
		this.primaryKey = primaryKey;
		this.typeId = typeId;
		this.userId = userId;
		this.userName = userName;
		this.uuid = uuid;
	}

	/**
	 * Gets the classNameId value for this PhoneSoap.
	 * 
	 * @return classNameId
	 */
	public long getClassNameId() {
		return classNameId;
	}

	/**
	 * Sets the classNameId value for this PhoneSoap.
	 * 
	 * @param classNameId
	 */
	public void setClassNameId(long classNameId) {
		this.classNameId = classNameId;
	}

	/**
	 * Gets the classPK value for this PhoneSoap.
	 * 
	 * @return classPK
	 */
	public long getClassPK() {
		return classPK;
	}

	/**
	 * Sets the classPK value for this PhoneSoap.
	 * 
	 * @param classPK
	 */
	public void setClassPK(long classPK) {
		this.classPK = classPK;
	}

	/**
	 * Gets the companyId value for this PhoneSoap.
	 * 
	 * @return companyId
	 */
	public long getCompanyId() {
		return companyId;
	}

	/**
	 * Sets the companyId value for this PhoneSoap.
	 * 
	 * @param companyId
	 */
	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	/**
	 * Gets the createDate value for this PhoneSoap.
	 * 
	 * @return createDate
	 */
	public java.util.Calendar getCreateDate() {
		return createDate;
	}

	/**
	 * Sets the createDate value for this PhoneSoap.
	 * 
	 * @param createDate
	 */
	public void setCreateDate(java.util.Calendar createDate) {
		this.createDate = createDate;
	}

	/**
	 * Gets the extension value for this PhoneSoap.
	 * 
	 * @return extension
	 */
	public java.lang.String getExtension() {
		return extension;
	}

	/**
	 * Sets the extension value for this PhoneSoap.
	 * 
	 * @param extension
	 */
	public void setExtension(java.lang.String extension) {
		this.extension = extension;
	}

	/**
	 * Gets the modifiedDate value for this PhoneSoap.
	 * 
	 * @return modifiedDate
	 */
	public java.util.Calendar getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * Sets the modifiedDate value for this PhoneSoap.
	 * 
	 * @param modifiedDate
	 */
	public void setModifiedDate(java.util.Calendar modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * Gets the number value for this PhoneSoap.
	 * 
	 * @return number
	 */
	public java.lang.String getNumber() {
		return number;
	}

	/**
	 * Sets the number value for this PhoneSoap.
	 * 
	 * @param number
	 */
	public void setNumber(java.lang.String number) {
		this.number = number;
	}

	/**
	 * Gets the phoneId value for this PhoneSoap.
	 * 
	 * @return phoneId
	 */
	public long getPhoneId() {
		return phoneId;
	}

	/**
	 * Sets the phoneId value for this PhoneSoap.
	 * 
	 * @param phoneId
	 */
	public void setPhoneId(long phoneId) {
		this.phoneId = phoneId;
	}

	/**
	 * Gets the primary value for this PhoneSoap.
	 * 
	 * @return primary
	 */
	public boolean isPrimary() {
		return primary;
	}

	/**
	 * Sets the primary value for this PhoneSoap.
	 * 
	 * @param primary
	 */
	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

	/**
	 * Gets the primaryKey value for this PhoneSoap.
	 * 
	 * @return primaryKey
	 */
	public long getPrimaryKey() {
		return primaryKey;
	}

	/**
	 * Sets the primaryKey value for this PhoneSoap.
	 * 
	 * @param primaryKey
	 */
	public void setPrimaryKey(long primaryKey) {
		this.primaryKey = primaryKey;
	}

	/**
	 * Gets the typeId value for this PhoneSoap.
	 * 
	 * @return typeId
	 */
	public int getTypeId() {
		return typeId;
	}

	/**
	 * Sets the typeId value for this PhoneSoap.
	 * 
	 * @param typeId
	 */
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	/**
	 * Gets the userId value for this PhoneSoap.
	 * 
	 * @return userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * Sets the userId value for this PhoneSoap.
	 * 
	 * @param userId
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * Gets the userName value for this PhoneSoap.
	 * 
	 * @return userName
	 */
	public java.lang.String getUserName() {
		return userName;
	}

	/**
	 * Sets the userName value for this PhoneSoap.
	 * 
	 * @param userName
	 */
	public void setUserName(java.lang.String userName) {
		this.userName = userName;
	}

	/**
	 * Gets the uuid value for this PhoneSoap.
	 * 
	 * @return uuid
	 */
	public java.lang.String getUuid() {
		return uuid;
	}

	/**
	 * Sets the uuid value for this PhoneSoap.
	 * 
	 * @param uuid
	 */
	public void setUuid(java.lang.String uuid) {
		this.uuid = uuid;
	}

}
