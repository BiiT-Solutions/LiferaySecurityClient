/**
 * UserGroupSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.liferay.portal.model;

public class UserGroup implements java.io.Serializable {
	private boolean addedByLDAPImport;

	private long companyId;

	private java.util.Calendar createDate;

	private java.lang.String description;

	private java.util.Calendar modifiedDate;

	private java.lang.String name;

	private long parentUserGroupId;

	private long primaryKey;

	private long userGroupId;

	private long userId;

	private java.lang.String userName;

	private java.lang.String uuid;

	public UserGroup() {
	}

	public UserGroup(boolean addedByLDAPImport, long companyId, java.util.Calendar createDate,
			java.lang.String description, java.util.Calendar modifiedDate, java.lang.String name,
			long parentUserGroupId, long primaryKey, long userGroupId, long userId, java.lang.String userName,
			java.lang.String uuid) {
		this.addedByLDAPImport = addedByLDAPImport;
		this.companyId = companyId;
		this.createDate = createDate;
		this.description = description;
		this.modifiedDate = modifiedDate;
		this.name = name;
		this.parentUserGroupId = parentUserGroupId;
		this.primaryKey = primaryKey;
		this.userGroupId = userGroupId;
		this.userId = userId;
		this.userName = userName;
		this.uuid = uuid;
	}

	/**
	 * Gets the addedByLDAPImport value for this UserGroupSoap.
	 * 
	 * @return addedByLDAPImport
	 */
	public boolean isAddedByLDAPImport() {
		return addedByLDAPImport;
	}

	/**
	 * Sets the addedByLDAPImport value for this UserGroupSoap.
	 * 
	 * @param addedByLDAPImport
	 */
	public void setAddedByLDAPImport(boolean addedByLDAPImport) {
		this.addedByLDAPImport = addedByLDAPImport;
	}

	/**
	 * Gets the companyId value for this UserGroupSoap.
	 * 
	 * @return companyId
	 */
	public long getCompanyId() {
		return companyId;
	}

	/**
	 * Sets the companyId value for this UserGroupSoap.
	 * 
	 * @param companyId
	 */
	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	/**
	 * Gets the createDate value for this UserGroupSoap.
	 * 
	 * @return createDate
	 */
	public java.util.Calendar getCreateDate() {
		return createDate;
	}

	/**
	 * Sets the createDate value for this UserGroupSoap.
	 * 
	 * @param createDate
	 */
	public void setCreateDate(java.util.Calendar createDate) {
		this.createDate = createDate;
	}

	/**
	 * Gets the description value for this UserGroupSoap.
	 * 
	 * @return description
	 */
	public java.lang.String getDescription() {
		return description;
	}

	/**
	 * Sets the description value for this UserGroupSoap.
	 * 
	 * @param description
	 */
	public void setDescription(java.lang.String description) {
		this.description = description;
	}

	/**
	 * Gets the modifiedDate value for this UserGroupSoap.
	 * 
	 * @return modifiedDate
	 */
	public java.util.Calendar getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * Sets the modifiedDate value for this UserGroupSoap.
	 * 
	 * @param modifiedDate
	 */
	public void setModifiedDate(java.util.Calendar modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * Gets the name value for this UserGroupSoap.
	 * 
	 * @return name
	 */
	public java.lang.String getName() {
		return name;
	}

	/**
	 * Sets the name value for this UserGroupSoap.
	 * 
	 * @param name
	 */
	public void setName(java.lang.String name) {
		this.name = name;
	}

	/**
	 * Gets the parentUserGroupId value for this UserGroupSoap.
	 * 
	 * @return parentUserGroupId
	 */
	public long getParentUserGroupId() {
		return parentUserGroupId;
	}

	/**
	 * Sets the parentUserGroupId value for this UserGroupSoap.
	 * 
	 * @param parentUserGroupId
	 */
	public void setParentUserGroupId(long parentUserGroupId) {
		this.parentUserGroupId = parentUserGroupId;
	}

	/**
	 * Gets the primaryKey value for this UserGroupSoap.
	 * 
	 * @return primaryKey
	 */
	public long getPrimaryKey() {
		return primaryKey;
	}

	/**
	 * Sets the primaryKey value for this UserGroupSoap.
	 * 
	 * @param primaryKey
	 */
	public void setPrimaryKey(long primaryKey) {
		this.primaryKey = primaryKey;
	}

	/**
	 * Gets the userGroupId value for this UserGroupSoap.
	 * 
	 * @return userGroupId
	 */
	public long getUserGroupId() {
		return userGroupId;
	}

	/**
	 * Sets the userGroupId value for this UserGroupSoap.
	 * 
	 * @param userGroupId
	 */
	public void setUserGroupId(long userGroupId) {
		this.userGroupId = userGroupId;
	}

	/**
	 * Gets the userId value for this UserGroupSoap.
	 * 
	 * @return userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * Sets the userId value for this UserGroupSoap.
	 * 
	 * @param userId
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * Gets the userName value for this UserGroupSoap.
	 * 
	 * @return userName
	 */
	public java.lang.String getUserName() {
		return userName;
	}

	/**
	 * Sets the userName value for this UserGroupSoap.
	 * 
	 * @param userName
	 */
	public void setUserName(java.lang.String userName) {
		this.userName = userName;
	}

	/**
	 * Gets the uuid value for this UserGroupSoap.
	 * 
	 * @return uuid
	 */
	public java.lang.String getUuid() {
		return uuid;
	}

	/**
	 * Sets the uuid value for this UserGroupSoap.
	 * 
	 * @param uuid
	 */
	public void setUuid(java.lang.String uuid) {
		this.uuid = uuid;
	}

}
