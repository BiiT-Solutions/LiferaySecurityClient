/**
 * RoleSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.liferay.portal.model;

public class Role implements java.io.Serializable {
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

	/**
	 * Gets the classNameId value for this RoleSoap.
	 * 
	 * @return classNameId
	 */
	public long getClassNameId() {
		return classNameId;
	}

	/**
	 * Sets the classNameId value for this RoleSoap.
	 * 
	 * @param classNameId
	 */
	public void setClassNameId(long classNameId) {
		this.classNameId = classNameId;
	}

	/**
	 * Gets the classPK value for this RoleSoap.
	 * 
	 * @return classPK
	 */
	public long getClassPK() {
		return classPK;
	}

	/**
	 * Sets the classPK value for this RoleSoap.
	 * 
	 * @param classPK
	 */
	public void setClassPK(long classPK) {
		this.classPK = classPK;
	}

	/**
	 * Gets the companyId value for this RoleSoap.
	 * 
	 * @return companyId
	 */
	public long getCompanyId() {
		return companyId;
	}

	/**
	 * Sets the companyId value for this RoleSoap.
	 * 
	 * @param companyId
	 */
	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	/**
	 * Gets the createDate value for this RoleSoap.
	 * 
	 * @return createDate
	 */
	public java.util.Calendar getCreateDate() {
		return createDate;
	}

	/**
	 * Sets the createDate value for this RoleSoap.
	 * 
	 * @param createDate
	 */
	public void setCreateDate(java.util.Calendar createDate) {
		this.createDate = createDate;
	}

	/**
	 * Gets the description value for this RoleSoap.
	 * 
	 * @return description
	 */
	public java.lang.String getDescription() {
		return description;
	}

	/**
	 * Sets the description value for this RoleSoap.
	 * 
	 * @param description
	 */
	public void setDescription(java.lang.String description) {
		this.description = description;
	}

	/**
	 * Gets the modifiedDate value for this RoleSoap.
	 * 
	 * @return modifiedDate
	 */
	public java.util.Calendar getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * Sets the modifiedDate value for this RoleSoap.
	 * 
	 * @param modifiedDate
	 */
	public void setModifiedDate(java.util.Calendar modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * Gets the name value for this RoleSoap.
	 * 
	 * @return name
	 */
	public java.lang.String getName() {
		return name;
	}

	/**
	 * Sets the name value for this RoleSoap.
	 * 
	 * @param name
	 */
	public void setName(java.lang.String name) {
		this.name = name;
	}

	/**
	 * Gets the primaryKey value for this RoleSoap.
	 * 
	 * @return primaryKey
	 */
	public long getPrimaryKey() {
		return primaryKey;
	}

	/**
	 * Sets the primaryKey value for this RoleSoap.
	 * 
	 * @param primaryKey
	 */
	public void setPrimaryKey(long primaryKey) {
		this.primaryKey = primaryKey;
	}

	/**
	 * Gets the roleId value for this RoleSoap.
	 * 
	 * @return roleId
	 */
	public long getRoleId() {
		return roleId;
	}

	/**
	 * Sets the roleId value for this RoleSoap.
	 * 
	 * @param roleId
	 */
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	/**
	 * Gets the subtype value for this RoleSoap.
	 * 
	 * @return subtype
	 */
	public java.lang.String getSubtype() {
		return subtype;
	}

	/**
	 * Sets the subtype value for this RoleSoap.
	 * 
	 * @param subtype
	 */
	public void setSubtype(java.lang.String subtype) {
		this.subtype = subtype;
	}

	/**
	 * Gets the title value for this RoleSoap.
	 * 
	 * @return title
	 */
	public java.lang.String getTitle() {
		return title;
	}

	/**
	 * Sets the title value for this RoleSoap.
	 * 
	 * @param title
	 */
	public void setTitle(java.lang.String title) {
		this.title = title;
	}

	/**
	 * Gets the type value for this RoleSoap.
	 * 
	 * @return type
	 */
	public int getType() {
		return type;
	}

	/**
	 * Sets the type value for this RoleSoap.
	 * 
	 * @param type
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * Gets the userId value for this RoleSoap.
	 * 
	 * @return userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * Sets the userId value for this RoleSoap.
	 * 
	 * @param userId
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * Gets the userName value for this RoleSoap.
	 * 
	 * @return userName
	 */
	public java.lang.String getUserName() {
		return userName;
	}

	/**
	 * Sets the userName value for this RoleSoap.
	 * 
	 * @param userName
	 */
	public void setUserName(java.lang.String userName) {
		this.userName = userName;
	}

	/**
	 * Gets the uuid value for this RoleSoap.
	 * 
	 * @return uuid
	 */
	public java.lang.String getUuid() {
		return uuid;
	}

	protected java.lang.String getDescriptionCurrentValue() {
		return descriptionCurrentValue;
	}

	protected void setDescriptionCurrentValue(java.lang.String descriptionCurrentValue) {
		this.descriptionCurrentValue = descriptionCurrentValue;
	}

	protected java.lang.String getTitleCurrentValue() {
		return titleCurrentValue;
	}

	protected void setTitleCurrentValue(java.lang.String titleCurrentValue) {
		this.titleCurrentValue = titleCurrentValue;
	}

	public String toString() {
		return getName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (roleId ^ (roleId >>> 32));
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
		Role other = (Role) obj;
		if (roleId != other.roleId)
			return false;
		return true;
	}

	public RoleType getRoleType() {
		return RoleType.getRoleType(getType());
	}

	public void setRoleType(RoleType roleType) {
		setType(roleType.getLiferayCode());
	}

}
