/**
 * PortletPreferencesIds.java
 * <p>
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.liferay.portal.model;

/*-
 * #%L
 * Liferay Authentication Client with Web Services
 * %%
 * Copyright (C) 2013 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

public class PortletPreferencesIds implements java.io.Serializable {
    private static final long serialVersionUID = 5502573990974190354L;

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
     * Gets the ownerId value for this PortletPreferencesIds.
     *
     * @return ownerId
     */
    public long getOwnerId() {
        return ownerId;
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
     * Gets the plid value for this PortletPreferencesIds.
     *
     * @return plid
     */
    public long getPlid() {
        return plid;
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
     * Sets the companyId value for this PortletPreferencesIds.
     *
     * @param companyId
     */
    public void setCompanyId(long companyId) {
        this.companyId = companyId;
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
     * Sets the ownerType value for this PortletPreferencesIds.
     *
     * @param ownerType
     */
    public void setOwnerType(int ownerType) {
        this.ownerType = ownerType;
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
     * Sets the portletId value for this PortletPreferencesIds.
     *
     * @param portletId
     */
    public void setPortletId(java.lang.String portletId) {
        this.portletId = portletId;
    }

}
