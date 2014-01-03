/**
 * AddressSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.liferay.portal.model;

public class Address implements java.io.Serializable {
	private long addressId;

	private java.lang.String city;

	private long classNameId;

	private long classPK;

	private long companyId;

	private long countryId;

	private java.util.Calendar createDate;

	private boolean mailing;

	private java.util.Calendar modifiedDate;

	private boolean primary;

	private long primaryKey;

	private long regionId;

	private java.lang.String street1;

	private java.lang.String street2;

	private java.lang.String street3;

	private int typeId;

	private long userId;

	private java.lang.String userName;

	private java.lang.String uuid;

	private java.lang.String zip;

	public Address() {
	}

	public Address(long addressId, java.lang.String city, long classNameId, long classPK, long companyId,
			long countryId, java.util.Calendar createDate, boolean mailing, java.util.Calendar modifiedDate,
			boolean primary, long primaryKey, long regionId, java.lang.String street1, java.lang.String street2,
			java.lang.String street3, int typeId, long userId, java.lang.String userName, java.lang.String uuid,
			java.lang.String zip) {
		this.addressId = addressId;
		this.city = city;
		this.classNameId = classNameId;
		this.classPK = classPK;
		this.companyId = companyId;
		this.countryId = countryId;
		this.createDate = createDate;
		this.mailing = mailing;
		this.modifiedDate = modifiedDate;
		this.primary = primary;
		this.primaryKey = primaryKey;
		this.regionId = regionId;
		this.street1 = street1;
		this.street2 = street2;
		this.street3 = street3;
		this.typeId = typeId;
		this.userId = userId;
		this.userName = userName;
		this.uuid = uuid;
		this.zip = zip;
	}

	/**
	 * Gets the addressId value for this AddressSoap.
	 * 
	 * @return addressId
	 */
	public long getAddressId() {
		return addressId;
	}

	/**
	 * Sets the addressId value for this AddressSoap.
	 * 
	 * @param addressId
	 */
	public void setAddressId(long addressId) {
		this.addressId = addressId;
	}

	/**
	 * Gets the city value for this AddressSoap.
	 * 
	 * @return city
	 */
	public java.lang.String getCity() {
		return city;
	}

	/**
	 * Sets the city value for this AddressSoap.
	 * 
	 * @param city
	 */
	public void setCity(java.lang.String city) {
		this.city = city;
	}

	/**
	 * Gets the classNameId value for this AddressSoap.
	 * 
	 * @return classNameId
	 */
	public long getClassNameId() {
		return classNameId;
	}

	/**
	 * Sets the classNameId value for this AddressSoap.
	 * 
	 * @param classNameId
	 */
	public void setClassNameId(long classNameId) {
		this.classNameId = classNameId;
	}

	/**
	 * Gets the classPK value for this AddressSoap.
	 * 
	 * @return classPK
	 */
	public long getClassPK() {
		return classPK;
	}

	/**
	 * Sets the classPK value for this AddressSoap.
	 * 
	 * @param classPK
	 */
	public void setClassPK(long classPK) {
		this.classPK = classPK;
	}

	/**
	 * Gets the companyId value for this AddressSoap.
	 * 
	 * @return companyId
	 */
	public long getCompanyId() {
		return companyId;
	}

	/**
	 * Sets the companyId value for this AddressSoap.
	 * 
	 * @param companyId
	 */
	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	/**
	 * Gets the countryId value for this AddressSoap.
	 * 
	 * @return countryId
	 */
	public long getCountryId() {
		return countryId;
	}

	/**
	 * Sets the countryId value for this AddressSoap.
	 * 
	 * @param countryId
	 */
	public void setCountryId(long countryId) {
		this.countryId = countryId;
	}

	/**
	 * Gets the createDate value for this AddressSoap.
	 * 
	 * @return createDate
	 */
	public java.util.Calendar getCreateDate() {
		return createDate;
	}

	/**
	 * Sets the createDate value for this AddressSoap.
	 * 
	 * @param createDate
	 */
	public void setCreateDate(java.util.Calendar createDate) {
		this.createDate = createDate;
	}

	/**
	 * Gets the mailing value for this AddressSoap.
	 * 
	 * @return mailing
	 */
	public boolean isMailing() {
		return mailing;
	}

	/**
	 * Sets the mailing value for this AddressSoap.
	 * 
	 * @param mailing
	 */
	public void setMailing(boolean mailing) {
		this.mailing = mailing;
	}

	/**
	 * Gets the modifiedDate value for this AddressSoap.
	 * 
	 * @return modifiedDate
	 */
	public java.util.Calendar getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * Sets the modifiedDate value for this AddressSoap.
	 * 
	 * @param modifiedDate
	 */
	public void setModifiedDate(java.util.Calendar modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * Gets the primary value for this AddressSoap.
	 * 
	 * @return primary
	 */
	public boolean isPrimary() {
		return primary;
	}

	/**
	 * Sets the primary value for this AddressSoap.
	 * 
	 * @param primary
	 */
	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

	/**
	 * Gets the primaryKey value for this AddressSoap.
	 * 
	 * @return primaryKey
	 */
	public long getPrimaryKey() {
		return primaryKey;
	}

	/**
	 * Sets the primaryKey value for this AddressSoap.
	 * 
	 * @param primaryKey
	 */
	public void setPrimaryKey(long primaryKey) {
		this.primaryKey = primaryKey;
	}

	/**
	 * Gets the regionId value for this AddressSoap.
	 * 
	 * @return regionId
	 */
	public long getRegionId() {
		return regionId;
	}

	/**
	 * Sets the regionId value for this AddressSoap.
	 * 
	 * @param regionId
	 */
	public void setRegionId(long regionId) {
		this.regionId = regionId;
	}

	/**
	 * Gets the street1 value for this AddressSoap.
	 * 
	 * @return street1
	 */
	public java.lang.String getStreet1() {
		return street1;
	}

	/**
	 * Sets the street1 value for this AddressSoap.
	 * 
	 * @param street1
	 */
	public void setStreet1(java.lang.String street1) {
		this.street1 = street1;
	}

	/**
	 * Gets the street2 value for this AddressSoap.
	 * 
	 * @return street2
	 */
	public java.lang.String getStreet2() {
		return street2;
	}

	/**
	 * Sets the street2 value for this AddressSoap.
	 * 
	 * @param street2
	 */
	public void setStreet2(java.lang.String street2) {
		this.street2 = street2;
	}

	/**
	 * Gets the street3 value for this AddressSoap.
	 * 
	 * @return street3
	 */
	public java.lang.String getStreet3() {
		return street3;
	}

	/**
	 * Sets the street3 value for this AddressSoap.
	 * 
	 * @param street3
	 */
	public void setStreet3(java.lang.String street3) {
		this.street3 = street3;
	}

	/**
	 * Gets the typeId value for this AddressSoap.
	 * 
	 * @return typeId
	 */
	public int getTypeId() {
		return typeId;
	}

	/**
	 * Sets the typeId value for this AddressSoap.
	 * 
	 * @param typeId
	 */
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	/**
	 * Gets the userId value for this AddressSoap.
	 * 
	 * @return userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * Sets the userId value for this AddressSoap.
	 * 
	 * @param userId
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * Gets the userName value for this AddressSoap.
	 * 
	 * @return userName
	 */
	public java.lang.String getUserName() {
		return userName;
	}

	/**
	 * Sets the userName value for this AddressSoap.
	 * 
	 * @param userName
	 */
	public void setUserName(java.lang.String userName) {
		this.userName = userName;
	}

	/**
	 * Gets the uuid value for this AddressSoap.
	 * 
	 * @return uuid
	 */
	public java.lang.String getUuid() {
		return uuid;
	}

	/**
	 * Sets the uuid value for this AddressSoap.
	 * 
	 * @param uuid
	 */
	public void setUuid(java.lang.String uuid) {
		this.uuid = uuid;
	}

	/**
	 * Gets the zip value for this AddressSoap.
	 * 
	 * @return zip
	 */
	public java.lang.String getZip() {
		return zip;
	}

	/**
	 * Sets the zip value for this AddressSoap.
	 * 
	 * @param zip
	 */
	public void setZip(java.lang.String zip) {
		this.zip = zip;
	}

}
