/**
 * UserGroupRoleSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.liferay.portal.model;

public class UserGroupRole implements java.io.Serializable {
	private long groupId;

	private long roleId;

	private long userId;

	public UserGroupRole() {
	}

	public UserGroupRole(long groupId, long roleId, long userId) {
		this.groupId = groupId;
		this.roleId = roleId;
		this.userId = userId;
	}

	/**
	 * Gets the groupId value for this UserGroupRoleSoap.
	 * 
	 * @return groupId
	 */
	public long getGroupId() {
		return groupId;
	}

	/**
	 * Sets the groupId value for this UserGroupRoleSoap.
	 * 
	 * @param groupId
	 */
	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	/**
	 * Gets the roleId value for this UserGroupRoleSoap.
	 * 
	 * @return roleId
	 */
	public long getRoleId() {
		return roleId;
	}

	/**
	 * Sets the roleId value for this UserGroupRoleSoap.
	 * 
	 * @param roleId
	 */
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	/**
	 * Gets the userId value for this UserGroupRoleSoap.
	 * 
	 * @return userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * Sets the userId value for this UserGroupRoleSoap.
	 * 
	 * @param userId
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

}
