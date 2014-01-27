/**
 * OrganizationSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.liferay.portal.model;

public class Organization implements java.io.Serializable {

	private java.lang.String comments;

	private long companyId;

	private long countryId;

	private java.util.Calendar createDate;

	private java.util.Calendar modifiedDate;

	private java.lang.String name;

	private long organizationId;

	private long parentOrganizationId;

	private long primaryKey;

	private boolean recursable;

	private long regionId;

	private int statusId;

	private java.lang.String treePath;

	private java.lang.String type;

	private long userId;

	private java.lang.String userName;

	private java.lang.String uuid;

	public Organization() {
	}

	public Organization(java.lang.String comments, long companyId, long countryId, java.util.Calendar createDate,
			java.util.Calendar modifiedDate, java.lang.String name, long organizationId, long parentOrganizationId,
			long primaryKey, boolean recursable, long regionId, int statusId, java.lang.String treePath,
			java.lang.String type, long userId, java.lang.String userName, java.lang.String uuid) {
		this.comments = comments;
		this.companyId = companyId;
		this.countryId = countryId;
		this.createDate = createDate;
		this.modifiedDate = modifiedDate;
		this.name = name;
		this.organizationId = organizationId;
		this.parentOrganizationId = parentOrganizationId;
		this.primaryKey = primaryKey;
		this.recursable = recursable;
		this.regionId = regionId;
		this.statusId = statusId;
		this.treePath = treePath;
		this.type = type;
		this.userId = userId;
		this.userName = userName;
		this.uuid = uuid;
	}

	/**
	 * Gets the comments value for this OrganizationSoap.
	 * 
	 * @return comments
	 */
	public java.lang.String getComments() {
		return comments;
	}

	/**
	 * Sets the comments value for this OrganizationSoap.
	 * 
	 * @param comments
	 */
	public void setComments(java.lang.String comments) {
		this.comments = comments;
	}

	/**
	 * Gets the companyId value for this OrganizationSoap.
	 * 
	 * @return companyId
	 */
	public long getCompanyId() {
		return companyId;
	}

	/**
	 * Sets the companyId value for this OrganizationSoap.
	 * 
	 * @param companyId
	 */
	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	/**
	 * Gets the countryId value for this OrganizationSoap.
	 * 
	 * @return countryId
	 */
	public long getCountryId() {
		return countryId;
	}

	/**
	 * Sets the countryId value for this OrganizationSoap.
	 * 
	 * @param countryId
	 */
	public void setCountryId(long countryId) {
		this.countryId = countryId;
	}

	/**
	 * Gets the createDate value for this OrganizationSoap.
	 * 
	 * @return createDate
	 */
	public java.util.Calendar getCreateDate() {
		return createDate;
	}

	/**
	 * Sets the createDate value for this OrganizationSoap.
	 * 
	 * @param createDate
	 */
	public void setCreateDate(java.util.Calendar createDate) {
		this.createDate = createDate;
	}

	/**
	 * Gets the modifiedDate value for this OrganizationSoap.
	 * 
	 * @return modifiedDate
	 */
	public java.util.Calendar getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * Sets the modifiedDate value for this OrganizationSoap.
	 * 
	 * @param modifiedDate
	 */
	public void setModifiedDate(java.util.Calendar modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * Gets the name value for this OrganizationSoap.
	 * 
	 * @return name
	 */
	public java.lang.String getName() {
		return name;
	}

	/**
	 * Sets the name value for this OrganizationSoap.
	 * 
	 * @param name
	 */
	public void setName(java.lang.String name) {
		this.name = name;
	}

	/**
	 * Gets the organizationId value for this OrganizationSoap.
	 * 
	 * @return organizationId
	 */
	public long getOrganizationId() {
		return organizationId;
	}

	/**
	 * Sets the organizationId value for this OrganizationSoap.
	 * 
	 * @param organizationId
	 */
	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}

	/**
	 * Gets the parentOrganizationId value for this OrganizationSoap.
	 * 
	 * @return parentOrganizationId
	 */
	public long getParentOrganizationId() {
		return parentOrganizationId;
	}

	/**
	 * Sets the parentOrganizationId value for this OrganizationSoap.
	 * 
	 * @param parentOrganizationId
	 */
	public void setParentOrganizationId(long parentOrganizationId) {
		this.parentOrganizationId = parentOrganizationId;
	}

	/**
	 * Gets the primaryKey value for this OrganizationSoap.
	 * 
	 * @return primaryKey
	 */
	public long getPrimaryKey() {
		return primaryKey;
	}

	/**
	 * Sets the primaryKey value for this OrganizationSoap.
	 * 
	 * @param primaryKey
	 */
	public void setPrimaryKey(long primaryKey) {
		this.primaryKey = primaryKey;
	}

	/**
	 * Gets the recursable value for this OrganizationSoap.
	 * 
	 * @return recursable
	 */
	public boolean isRecursable() {
		return recursable;
	}

	/**
	 * Sets the recursable value for this OrganizationSoap.
	 * 
	 * @param recursable
	 */
	public void setRecursable(boolean recursable) {
		this.recursable = recursable;
	}

	/**
	 * Gets the regionId value for this OrganizationSoap.
	 * 
	 * @return regionId
	 */
	public long getRegionId() {
		return regionId;
	}

	/**
	 * Sets the regionId value for this OrganizationSoap.
	 * 
	 * @param regionId
	 */
	public void setRegionId(long regionId) {
		this.regionId = regionId;
	}

	/**
	 * Gets the statusId value for this OrganizationSoap.
	 * 
	 * @return statusId
	 */
	public int getStatusId() {
		return statusId;
	}

	/**
	 * Sets the statusId value for this OrganizationSoap.
	 * 
	 * @param statusId
	 */
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	/**
	 * Gets the treePath value for this OrganizationSoap.
	 * 
	 * @return treePath
	 */
	public java.lang.String getTreePath() {
		return treePath;
	}

	/**
	 * Sets the treePath value for this OrganizationSoap.
	 * 
	 * @param treePath
	 */
	public void setTreePath(java.lang.String treePath) {
		this.treePath = treePath;
	}

	/**
	 * Gets the type value for this OrganizationSoap.
	 * 
	 * @return type
	 */
	public java.lang.String getType() {
		return type;
	}

	/**
	 * Sets the type value for this OrganizationSoap.
	 * 
	 * @param type
	 */
	public void setType(java.lang.String type) {
		this.type = type;
	}

	/**
	 * Gets the userId value for this OrganizationSoap.
	 * 
	 * @return userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * Sets the userId value for this OrganizationSoap.
	 * 
	 * @param userId
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * Gets the userName value for this OrganizationSoap.
	 * 
	 * @return userName
	 */
	public java.lang.String getUserName() {
		return userName;
	}

	/**
	 * Sets the userName value for this OrganizationSoap.
	 * 
	 * @param userName
	 */
	public void setUserName(java.lang.String userName) {
		this.userName = userName;
	}

	/**
	 * Gets the uuid value for this OrganizationSoap.
	 * 
	 * @return uuid
	 */
	public java.lang.String getUuid() {
		return uuid;
	}

	/**
	 * Sets the uuid value for this OrganizationSoap.
	 * 
	 * @param uuid
	 */
	public void setUuid(java.lang.String uuid) {
		this.uuid = uuid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (organizationId ^ (organizationId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Organization other = (Organization) obj;
		if (organizationId != other.organizationId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getName();
	}

}
