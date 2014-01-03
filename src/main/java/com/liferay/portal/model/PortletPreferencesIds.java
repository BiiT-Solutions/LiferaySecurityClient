/**
 * PortletPreferencesIds.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.liferay.portal.model;

public class PortletPreferencesIds implements java.io.Serializable {
	private long companyId;

	private long ownerId;

	private int ownerType;

	private long plid;

	private java.lang.String portletId;

	public PortletPreferencesIds() {
	}

	public PortletPreferencesIds(long companyId, long ownerId, int ownerType, long plid, java.lang.String portletId) {
		this.companyId = companyId;
		this.ownerId = ownerId;
		this.ownerType = ownerType;
		this.plid = plid;
		this.portletId = portletId;
	}

	/**
	 * Gets the companyId value for this PortletPreferencesIds.
	 * 
	 * @return companyId
	 */
	public long getCompanyId() {
		return companyId;
	}

	/**
	 * Sets the companyId value for this PortletPreferencesIds.
	 * 
	 * @param companyId
	 */
	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	/**
	 * Gets the ownerId value for this PortletPreferencesIds.
	 * 
	 * @return ownerId
	 */
	public long getOwnerId() {
		return ownerId;
	}

	/**
	 * Sets the ownerId value for this PortletPreferencesIds.
	 * 
	 * @param ownerId
	 */
	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
	}

	/**
	 * Gets the ownerType value for this PortletPreferencesIds.
	 * 
	 * @return ownerType
	 */
	public int getOwnerType() {
		return ownerType;
	}

	/**
	 * Sets the ownerType value for this PortletPreferencesIds.
	 * 
	 * @param ownerType
	 */
	public void setOwnerType(int ownerType) {
		this.ownerType = ownerType;
	}

	/**
	 * Gets the plid value for this PortletPreferencesIds.
	 * 
	 * @return plid
	 */
	public long getPlid() {
		return plid;
	}

	/**
	 * Sets the plid value for this PortletPreferencesIds.
	 * 
	 * @param plid
	 */
	public void setPlid(long plid) {
		this.plid = plid;
	}

	/**
	 * Gets the portletId value for this PortletPreferencesIds.
	 * 
	 * @return portletId
	 */
	public java.lang.String getPortletId() {
		return portletId;
	}

	/**
	 * Sets the portletId value for this PortletPreferencesIds.
	 * 
	 * @param portletId
	 */
	public void setPortletId(java.lang.String portletId) {
		this.portletId = portletId;
	}

}
