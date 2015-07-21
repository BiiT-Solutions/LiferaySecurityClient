/**
 * WebsiteSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.liferay.portal.model;

public class Website implements java.io.Serializable {
	private static final long serialVersionUID = -2073244277325614089L;

	private long classNameId;

	private long classPK;

	private long companyId;

	private java.util.Calendar createDate;

	private java.util.Calendar modifiedDate;

	private boolean primary;

	private long primaryKey;

	private int typeId;

	private java.lang.String url;

	private long userId;

	private java.lang.String userName;

	private java.lang.String uuid;

	private long websiteId;

	public Website() {
	}

	public Website(long classNameId, long classPK, long companyId, java.util.Calendar createDate,
			java.util.Calendar modifiedDate, boolean primary, long primaryKey, int typeId, java.lang.String url,
			long userId, java.lang.String userName, java.lang.String uuid, long websiteId) {
		this.classNameId = classNameId;
		this.classPK = classPK;
		this.companyId = companyId;
		this.createDate = createDate;
		this.modifiedDate = modifiedDate;
		this.primary = primary;
		this.primaryKey = primaryKey;
		this.typeId = typeId;
		this.url = url;
		this.userId = userId;
		this.userName = userName;
		this.uuid = uuid;
		this.websiteId = websiteId;
	}

	/**
	 * Gets the classNameId value for this WebsiteSoap.
	 * 
	 * @return classNameId
	 */
	public long getClassNameId() {
		return classNameId;
	}

	/**
	 * Gets the classPK value for this WebsiteSoap.
	 * 
	 * @return classPK
	 */
	public long getClassPK() {
		return classPK;
	}

	/**
	 * Gets the companyId value for this WebsiteSoap.
	 * 
	 * @return companyId
	 */
	public long getCompanyId() {
		return companyId;
	}

	/**
	 * Gets the createDate value for this WebsiteSoap.
	 * 
	 * @return createDate
	 */
	public java.util.Calendar getCreateDate() {
		return createDate;
	}

	/**
	 * Gets the modifiedDate value for this WebsiteSoap.
	 * 
	 * @return modifiedDate
	 */
	public java.util.Calendar getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * Gets the primaryKey value for this WebsiteSoap.
	 * 
	 * @return primaryKey
	 */
	public long getPrimaryKey() {
		return primaryKey;
	}

	/**
	 * Gets the typeId value for this WebsiteSoap.
	 * 
	 * @return typeId
	 */
	public int getTypeId() {
		return typeId;
	}

	/**
	 * Gets the url value for this WebsiteSoap.
	 * 
	 * @return url
	 */
	public java.lang.String getUrl() {
		return url;
	}

	/**
	 * Gets the userId value for this WebsiteSoap.
	 * 
	 * @return userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * Gets the userName value for this WebsiteSoap.
	 * 
	 * @return userName
	 */
	public java.lang.String getUserName() {
		return userName;
	}

	/**
	 * Gets the uuid value for this WebsiteSoap.
	 * 
	 * @return uuid
	 */
	public java.lang.String getUuid() {
		return uuid;
	}

	/**
	 * Gets the websiteId value for this WebsiteSoap.
	 * 
	 * @return websiteId
	 */
	public long getWebsiteId() {
		return websiteId;
	}

	/**
	 * Gets the primary value for this WebsiteSoap.
	 * 
	 * @return primary
	 */
	public boolean isPrimary() {
		return primary;
	}

	/**
	 * Sets the classNameId value for this WebsiteSoap.
	 * 
	 * @param classNameId
	 */
	public void setClassNameId(long classNameId) {
		this.classNameId = classNameId;
	}

	/**
	 * Sets the classPK value for this WebsiteSoap.
	 * 
	 * @param classPK
	 */
	public void setClassPK(long classPK) {
		this.classPK = classPK;
	}

	/**
	 * Sets the companyId value for this WebsiteSoap.
	 * 
	 * @param companyId
	 */
	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	/**
	 * Sets the createDate value for this WebsiteSoap.
	 * 
	 * @param createDate
	 */
	public void setCreateDate(java.util.Calendar createDate) {
		this.createDate = createDate;
	}

	/**
	 * Sets the modifiedDate value for this WebsiteSoap.
	 * 
	 * @param modifiedDate
	 */
	public void setModifiedDate(java.util.Calendar modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * Sets the primary value for this WebsiteSoap.
	 * 
	 * @param primary
	 */
	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

	/**
	 * Sets the primaryKey value for this WebsiteSoap.
	 * 
	 * @param primaryKey
	 */
	public void setPrimaryKey(long primaryKey) {
		this.primaryKey = primaryKey;
	}

	/**
	 * Sets the typeId value for this WebsiteSoap.
	 * 
	 * @param typeId
	 */
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	/**
	 * Sets the url value for this WebsiteSoap.
	 * 
	 * @param url
	 */
	public void setUrl(java.lang.String url) {
		this.url = url;
	}

	/**
	 * Sets the userId value for this WebsiteSoap.
	 * 
	 * @param userId
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * Sets the userName value for this WebsiteSoap.
	 * 
	 * @param userName
	 */
	public void setUserName(java.lang.String userName) {
		this.userName = userName;
	}

	/**
	 * Sets the uuid value for this WebsiteSoap.
	 * 
	 * @param uuid
	 */
	public void setUuid(java.lang.String uuid) {
		this.uuid = uuid;
	}

	/**
	 * Sets the websiteId value for this WebsiteSoap.
	 * 
	 * @param websiteId
	 */
	public void setWebsiteId(long websiteId) {
		this.websiteId = websiteId;
	}

}
