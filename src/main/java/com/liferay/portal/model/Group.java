/**
 * GroupSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.liferay.portal.model;

public class Group implements java.io.Serializable {
	private boolean active;

	private long classNameId;

	private long classPK;

	private long companyId;

	private long creatorUserId;

	private java.lang.String description;

	private java.lang.String friendlyURL;

	private long groupId;

	private long liveGroupId;

	private boolean manualMembership;

	private int membershipRestriction;

	private java.lang.String name;

	private long parentGroupId;

	private long primaryKey;

	private int remoteStagingGroupCount;

	private boolean site;

	private java.lang.String treePath;

	private int type;

	private java.lang.String typeSettings;

	private java.lang.String uuid;

	public Group() {
	}

	public Group(boolean active, long classNameId, long classPK, long companyId, long creatorUserId,
			java.lang.String description, java.lang.String friendlyURL, long groupId, long liveGroupId,
			boolean manualMembership, int membershipRestriction, java.lang.String name, long parentGroupId,
			long primaryKey, int remoteStagingGroupCount, boolean site, java.lang.String treePath, int type,
			java.lang.String typeSettings, java.lang.String uuid) {
		this.active = active;
		this.classNameId = classNameId;
		this.classPK = classPK;
		this.companyId = companyId;
		this.creatorUserId = creatorUserId;
		this.description = description;
		this.friendlyURL = friendlyURL;
		this.groupId = groupId;
		this.liveGroupId = liveGroupId;
		this.manualMembership = manualMembership;
		this.membershipRestriction = membershipRestriction;
		this.name = name;
		this.parentGroupId = parentGroupId;
		this.primaryKey = primaryKey;
		this.remoteStagingGroupCount = remoteStagingGroupCount;
		this.site = site;
		this.treePath = treePath;
		this.type = type;
		this.typeSettings = typeSettings;
		this.uuid = uuid;
	}

	/**
	 * Gets the active value for this GroupSoap.
	 * 
	 * @return active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets the active value for this GroupSoap.
	 * 
	 * @param active
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Gets the classNameId value for this GroupSoap.
	 * 
	 * @return classNameId
	 */
	public long getClassNameId() {
		return classNameId;
	}

	/**
	 * Sets the classNameId value for this GroupSoap.
	 * 
	 * @param classNameId
	 */
	public void setClassNameId(long classNameId) {
		this.classNameId = classNameId;
	}

	/**
	 * Gets the classPK value for this GroupSoap.
	 * 
	 * @return classPK
	 */
	public long getClassPK() {
		return classPK;
	}

	/**
	 * Sets the classPK value for this GroupSoap.
	 * 
	 * @param classPK
	 */
	public void setClassPK(long classPK) {
		this.classPK = classPK;
	}

	/**
	 * Gets the companyId value for this GroupSoap.
	 * 
	 * @return companyId
	 */
	public long getCompanyId() {
		return companyId;
	}

	/**
	 * Sets the companyId value for this GroupSoap.
	 * 
	 * @param companyId
	 */
	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	/**
	 * Gets the creatorUserId value for this GroupSoap.
	 * 
	 * @return creatorUserId
	 */
	public long getCreatorUserId() {
		return creatorUserId;
	}

	/**
	 * Sets the creatorUserId value for this GroupSoap.
	 * 
	 * @param creatorUserId
	 */
	public void setCreatorUserId(long creatorUserId) {
		this.creatorUserId = creatorUserId;
	}

	/**
	 * Gets the description value for this GroupSoap.
	 * 
	 * @return description
	 */
	public java.lang.String getDescription() {
		return description;
	}

	/**
	 * Sets the description value for this GroupSoap.
	 * 
	 * @param description
	 */
	public void setDescription(java.lang.String description) {
		this.description = description;
	}

	/**
	 * Gets the friendlyURL value for this GroupSoap.
	 * 
	 * @return friendlyURL
	 */
	public java.lang.String getFriendlyURL() {
		return friendlyURL;
	}

	/**
	 * Sets the friendlyURL value for this GroupSoap.
	 * 
	 * @param friendlyURL
	 */
	public void setFriendlyURL(java.lang.String friendlyURL) {
		this.friendlyURL = friendlyURL;
	}

	/**
	 * Gets the groupId value for this GroupSoap.
	 * 
	 * @return groupId
	 */
	public long getGroupId() {
		return groupId;
	}

	/**
	 * Sets the groupId value for this GroupSoap.
	 * 
	 * @param groupId
	 */
	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	/**
	 * Gets the liveGroupId value for this GroupSoap.
	 * 
	 * @return liveGroupId
	 */
	public long getLiveGroupId() {
		return liveGroupId;
	}

	/**
	 * Sets the liveGroupId value for this GroupSoap.
	 * 
	 * @param liveGroupId
	 */
	public void setLiveGroupId(long liveGroupId) {
		this.liveGroupId = liveGroupId;
	}

	/**
	 * Gets the manualMembership value for this GroupSoap.
	 * 
	 * @return manualMembership
	 */
	public boolean isManualMembership() {
		return manualMembership;
	}

	/**
	 * Sets the manualMembership value for this GroupSoap.
	 * 
	 * @param manualMembership
	 */
	public void setManualMembership(boolean manualMembership) {
		this.manualMembership = manualMembership;
	}

	/**
	 * Gets the membershipRestriction value for this GroupSoap.
	 * 
	 * @return membershipRestriction
	 */
	public int getMembershipRestriction() {
		return membershipRestriction;
	}

	/**
	 * Sets the membershipRestriction value for this GroupSoap.
	 * 
	 * @param membershipRestriction
	 */
	public void setMembershipRestriction(int membershipRestriction) {
		this.membershipRestriction = membershipRestriction;
	}

	/**
	 * Gets the name value for this GroupSoap.
	 * 
	 * @return name
	 */
	public java.lang.String getName() {
		return name;
	}

	/**
	 * Sets the name value for this GroupSoap.
	 * 
	 * @param name
	 */
	public void setName(java.lang.String name) {
		this.name = name;
	}

	/**
	 * Gets the parentGroupId value for this GroupSoap.
	 * 
	 * @return parentGroupId
	 */
	public long getParentGroupId() {
		return parentGroupId;
	}

	/**
	 * Sets the parentGroupId value for this GroupSoap.
	 * 
	 * @param parentGroupId
	 */
	public void setParentGroupId(long parentGroupId) {
		this.parentGroupId = parentGroupId;
	}

	/**
	 * Gets the primaryKey value for this GroupSoap.
	 * 
	 * @return primaryKey
	 */
	public long getPrimaryKey() {
		return primaryKey;
	}

	/**
	 * Sets the primaryKey value for this GroupSoap.
	 * 
	 * @param primaryKey
	 */
	public void setPrimaryKey(long primaryKey) {
		this.primaryKey = primaryKey;
	}

	/**
	 * Gets the remoteStagingGroupCount value for this GroupSoap.
	 * 
	 * @return remoteStagingGroupCount
	 */
	public int getRemoteStagingGroupCount() {
		return remoteStagingGroupCount;
	}

	/**
	 * Sets the remoteStagingGroupCount value for this GroupSoap.
	 * 
	 * @param remoteStagingGroupCount
	 */
	public void setRemoteStagingGroupCount(int remoteStagingGroupCount) {
		this.remoteStagingGroupCount = remoteStagingGroupCount;
	}

	/**
	 * Gets the site value for this GroupSoap.
	 * 
	 * @return site
	 */
	public boolean isSite() {
		return site;
	}

	/**
	 * Sets the site value for this GroupSoap.
	 * 
	 * @param site
	 */
	public void setSite(boolean site) {
		this.site = site;
	}

	/**
	 * Gets the treePath value for this GroupSoap.
	 * 
	 * @return treePath
	 */
	public java.lang.String getTreePath() {
		return treePath;
	}

	/**
	 * Sets the treePath value for this GroupSoap.
	 * 
	 * @param treePath
	 */
	public void setTreePath(java.lang.String treePath) {
		this.treePath = treePath;
	}

	/**
	 * Gets the type value for this GroupSoap.
	 * 
	 * @return type
	 */
	public int getType() {
		return type;
	}

	/**
	 * Sets the type value for this GroupSoap.
	 * 
	 * @param type
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * Gets the typeSettings value for this GroupSoap.
	 * 
	 * @return typeSettings
	 */
	public java.lang.String getTypeSettings() {
		return typeSettings;
	}

	/**
	 * Sets the typeSettings value for this GroupSoap.
	 * 
	 * @param typeSettings
	 */
	public void setTypeSettings(java.lang.String typeSettings) {
		this.typeSettings = typeSettings;
	}

	/**
	 * Gets the uuid value for this GroupSoap.
	 * 
	 * @return uuid
	 */
	public java.lang.String getUuid() {
		return uuid;
	}

	/**
	 * Sets the uuid value for this GroupSoap.
	 * 
	 * @param uuid
	 */
	public void setUuid(java.lang.String uuid) {
		this.uuid = uuid;
	}

}
