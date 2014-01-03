/**
 * CompanySoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.liferay.portal.model;

public class Company implements java.io.Serializable {
	private long accountId;

	private boolean active;

	private long companyId;

	private java.lang.String homeURL;

	private java.lang.String key;

	private long logoId;

	private int maxUsers;

	private java.lang.String mx;

	private long primaryKey;

	private boolean system;

	private java.lang.String webId;

	public Company() {
	}

	public Company(long accountId, boolean active, long companyId, java.lang.String homeURL, java.lang.String key,
			long logoId, int maxUsers, java.lang.String mx, long primaryKey, boolean system, java.lang.String webId) {
		this.accountId = accountId;
		this.active = active;
		this.companyId = companyId;
		this.homeURL = homeURL;
		this.key = key;
		this.logoId = logoId;
		this.maxUsers = maxUsers;
		this.mx = mx;
		this.primaryKey = primaryKey;
		this.system = system;
		this.webId = webId;
	}

	/**
	 * Gets the accountId value for this CompanySoap.
	 * 
	 * @return accountId
	 */
	public long getAccountId() {
		return accountId;
	}

	/**
	 * Sets the accountId value for this CompanySoap.
	 * 
	 * @param accountId
	 */
	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	/**
	 * Gets the active value for this CompanySoap.
	 * 
	 * @return active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets the active value for this CompanySoap.
	 * 
	 * @param active
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Gets the companyId value for this CompanySoap.
	 * 
	 * @return companyId
	 */
	public long getCompanyId() {
		return companyId;
	}

	/**
	 * Sets the companyId value for this CompanySoap.
	 * 
	 * @param companyId
	 */
	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	/**
	 * Gets the homeURL value for this CompanySoap.
	 * 
	 * @return homeURL
	 */
	public java.lang.String getHomeURL() {
		return homeURL;
	}

	/**
	 * Sets the homeURL value for this CompanySoap.
	 * 
	 * @param homeURL
	 */
	public void setHomeURL(java.lang.String homeURL) {
		this.homeURL = homeURL;
	}

	/**
	 * Gets the key value for this CompanySoap.
	 * 
	 * @return key
	 */
	public java.lang.String getKey() {
		return key;
	}

	/**
	 * Sets the key value for this CompanySoap.
	 * 
	 * @param key
	 */
	public void setKey(java.lang.String key) {
		this.key = key;
	}

	/**
	 * Gets the logoId value for this CompanySoap.
	 * 
	 * @return logoId
	 */
	public long getLogoId() {
		return logoId;
	}

	/**
	 * Sets the logoId value for this CompanySoap.
	 * 
	 * @param logoId
	 */
	public void setLogoId(long logoId) {
		this.logoId = logoId;
	}

	/**
	 * Gets the maxUsers value for this CompanySoap.
	 * 
	 * @return maxUsers
	 */
	public int getMaxUsers() {
		return maxUsers;
	}

	/**
	 * Sets the maxUsers value for this CompanySoap.
	 * 
	 * @param maxUsers
	 */
	public void setMaxUsers(int maxUsers) {
		this.maxUsers = maxUsers;
	}

	/**
	 * Gets the mx value for this CompanySoap.
	 * 
	 * @return mx
	 */
	public java.lang.String getMx() {
		return mx;
	}

	/**
	 * Sets the mx value for this CompanySoap.
	 * 
	 * @param mx
	 */
	public void setMx(java.lang.String mx) {
		this.mx = mx;
	}

	/**
	 * Gets the primaryKey value for this CompanySoap.
	 * 
	 * @return primaryKey
	 */
	public long getPrimaryKey() {
		return primaryKey;
	}

	/**
	 * Sets the primaryKey value for this CompanySoap.
	 * 
	 * @param primaryKey
	 */
	public void setPrimaryKey(long primaryKey) {
		this.primaryKey = primaryKey;
	}

	/**
	 * Gets the system value for this CompanySoap.
	 * 
	 * @return system
	 */
	public boolean isSystem() {
		return system;
	}

	/**
	 * Sets the system value for this CompanySoap.
	 * 
	 * @param system
	 */
	public void setSystem(boolean system) {
		this.system = system;
	}

	/**
	 * Gets the webId value for this CompanySoap.
	 * 
	 * @return webId
	 */
	public java.lang.String getWebId() {
		return webId;
	}

	/**
	 * Sets the webId value for this CompanySoap.
	 * 
	 * @param webId
	 */
	public void setWebId(java.lang.String webId) {
		this.webId = webId;
	}

}
