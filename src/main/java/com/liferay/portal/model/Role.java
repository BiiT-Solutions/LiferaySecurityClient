/**
 * Role.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.liferay.portal.model;

import com.biit.usermanager.entity.IRole;

public class Role implements java.io.Serializable, IRole<Long> {
	private static final long serialVersionUID = 7329105650809872362L;

	private long classNameId;

	private long classPK;

	private long companyId;

	private java.util.Calendar createDate;

	private java.lang.String description;

	private java.lang.String descriptionCurrentValue;

	private java.util.Calendar modifiedDate;

	private java.lang.String name;

	private long primaryKey;

	private long roleId;

	private java.lang.String subtype;

	private java.lang.String title;

	private java.lang.String titleCurrentValue;

	private int type;

	private long userId;

	private java.lang.String userName;

	private java.lang.String uuid;

	public Role() {
	}

	public Role(long classNameId, long classPK, long companyId, java.util.Calendar createDate,
			java.lang.String description, java.util.Calendar modifiedDate, java.lang.String name, long primaryKey,
			long roleId, java.lang.String subtype, java.lang.String title, int type, long userId,
			java.lang.String userName, java.lang.String uuid) {
		this.classNameId = classNameId;
		this.classPK = classPK;
		this.companyId = companyId;
		this.createDate = createDate;
		this.description = description;
		this.modifiedDate = modifiedDate;
		this.name = name;
		this.primaryKey = primaryKey;
		this.roleId = roleId;
		this.subtype = subtype;
		this.title = title;
		this.type = type;
		this.userId = userId;
		this.userName = userName;
		this.uuid = uuid;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		if (roleId != other.roleId)
			return false;
		return true;
	}

	/**
	 * Gets the classNameId value for this Role.
	 * 
	 * @return classNameId
	 */
	public long getClassNameId() {
		return classNameId;
	}

	/**
	 * Gets the classPK value for this Role.
	 * 
	 * @return classPK
	 */
	public long getClassPK() {
		return classPK;
	}

	/**
	 * Gets the companyId value for this Role.
	 * 
	 * @return companyId
	 */
	public long getCompanyId() {
		return companyId;
	}

	/**
	 * Gets the createDate value for this Role.
	 * 
	 * @return createDate
	 */
	public java.util.Calendar getCreateDate() {
		return createDate;
	}

	/**
	 * Gets the description value for this Role.
	 * 
	 * @return description
	 */
	public java.lang.String getDescription() {
		return description;
	}

	protected java.lang.String getDescriptionCurrentValue() {
		return descriptionCurrentValue;
	}

	/**
	 * Gets the roleId value for this Role.
	 * 
	 * @return roleId
	 */
	@Override
	public Long getId() {
		return roleId;
	}

	/**
	 * Gets the modifiedDate value for this Role.
	 * 
	 * @return modifiedDate
	 */
	public java.util.Calendar getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * Gets the name value for this Role.
	 * 
	 * @return name
	 */
	public java.lang.String getName() {
		return name;
	}

	/**
	 * Gets the primaryKey value for this Role.
	 * 
	 * @return primaryKey
	 */
	public long getPrimaryKey() {
		return primaryKey;
	}

	public RoleType getRoleType() {
		return RoleType.getRoleType(getType());
	}

	/**
	 * Gets the subtype value for this Role.
	 * 
	 * @return subtype
	 */
	public java.lang.String getSubtype() {
		return subtype;
	}

	/**
	 * Gets the title value for this Role.
	 * 
	 * @return title
	 */
	public java.lang.String getTitle() {
		return title;
	}

	protected java.lang.String getTitleCurrentValue() {
		return titleCurrentValue;
	}

	/**
	 * Gets the type value for this Role.
	 * 
	 * @return type
	 */
	public int getType() {
		return type;
	}

	@Override
	public String getUniqueName() {
		return getName();
	}

	/**
	 * Gets the userId value for this Role.
	 * 
	 * @return userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * Gets the userName value for this Role.
	 * 
	 * @return userName
	 */
	public java.lang.String getUserName() {
		return userName;
	}

	/**
	 * Gets the uuid value for this Role.
	 * 
	 * @return uuid
	 */
	public java.lang.String getUuid() {
		return uuid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (roleId ^ (roleId >>> 32));
		return result;
	}

	/**
	 * Sets the classNameId value for this Role.
	 * 
	 * @param classNameId
	 */
	public void setClassNameId(long classNameId) {
		this.classNameId = classNameId;
	}

	/**
	 * Sets the classPK value for this Role.
	 * 
	 * @param classPK
	 */
	public void setClassPK(long classPK) {
		this.classPK = classPK;
	}

	/**
	 * Sets the companyId value for this Role.
	 * 
	 * @param companyId
	 */
	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	/**
	 * Sets the createDate value for this Role.
	 * 
	 * @param createDate
	 */
	public void setCreateDate(java.util.Calendar createDate) {
		this.createDate = createDate;
	}

	/**
	 * Sets the description value for this Role.
	 * 
	 * @param description
	 */
	public void setDescription(java.lang.String description) {
		this.description = description;
	}

	protected void setDescriptionCurrentValue(java.lang.String descriptionCurrentValue) {
		this.descriptionCurrentValue = descriptionCurrentValue;
	}

	/**
	 * Sets the modifiedDate value for this Role.
	 * 
	 * @param modifiedDate
	 */
	public void setModifiedDate(java.util.Calendar modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * Sets the name value for this Role.
	 * 
	 * @param name
	 */
	public void setName(java.lang.String name) {
		this.name = name;
	}

	/**
	 * Sets the primaryKey value for this Role.
	 * 
	 * @param primaryKey
	 */
	public void setPrimaryKey(long primaryKey) {
		this.primaryKey = primaryKey;
	}

	/**
	 * Sets the roleId value for this Role.
	 * 
	 * @param roleId
	 */
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public void setRoleType(RoleType roleType) {
		setType(roleType.getLiferayCode());
	}

	/**
	 * Sets the subtype value for this Role.
	 * 
	 * @param subtype
	 */
	public void setSubtype(java.lang.String subtype) {
		this.subtype = subtype;
	}

	/**
	 * Sets the title value for this Role.
	 * 
	 * @param title
	 */
	public void setTitle(java.lang.String title) {
		this.title = title;
	}

	protected void setTitleCurrentValue(java.lang.String titleCurrentValue) {
		this.titleCurrentValue = titleCurrentValue;
	}

	/**
	 * Sets the type value for this Role.
	 * 
	 * @param type
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * Sets the userId value for this Role.
	 * 
	 * @param userId
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * Sets the userName value for this Role.
	 * 
	 * @param userName
	 */
	public void setUserName(java.lang.String userName) {
		this.userName = userName;
	}

	public String toString() {
		return getName();
	}

}
