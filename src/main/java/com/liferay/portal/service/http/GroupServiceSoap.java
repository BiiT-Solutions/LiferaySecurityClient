/**
 * GroupServiceSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.liferay.portal.service.http;

public interface GroupServiceSoap extends java.rmi.Remote {
    public com.liferay.portal.model.Group addGroup(long parentGroupId, long liveGroupId, java.lang.String name, java.lang.String description, int type, boolean manualMembership, int membershipRestriction, java.lang.String friendlyURL, boolean site, boolean active, com.liferay.portal.service.ServiceContext serviceContext) throws java.rmi.RemoteException;
    public com.liferay.portal.model.Group addGroup(long parentGroupId, java.lang.String name, java.lang.String description, int type, java.lang.String friendlyURL, boolean site, boolean active, com.liferay.portal.service.ServiceContext serviceContext) throws java.rmi.RemoteException;
    public com.liferay.portal.model.Group addGroup(java.lang.String name, java.lang.String description, int type, java.lang.String friendlyURL, boolean site, boolean active, com.liferay.portal.service.ServiceContext serviceContext) throws java.rmi.RemoteException;
    public void addRoleGroups(long roleId, long[] groupIds) throws java.rmi.RemoteException;
    public void checkRemoteStagingGroup(long groupId) throws java.rmi.RemoteException;
    public void deleteGroup(long groupId) throws java.rmi.RemoteException;
    public void disableStaging(long groupId) throws java.rmi.RemoteException;
    public void enableStaging(long groupId) throws java.rmi.RemoteException;
    public com.liferay.portal.model.Group getCompanyGroup(long companyId) throws java.rmi.RemoteException;
    public com.liferay.portal.model.Group getGroup(long groupId) throws java.rmi.RemoteException;
    public com.liferay.portal.model.Group getGroup(long companyId, java.lang.String name) throws java.rmi.RemoteException;
    public com.liferay.portal.model.Group[] getGroups(long companyId, long parentGroupId, boolean site) throws java.rmi.RemoteException;
    public com.liferay.portal.model.Group[] getManageableSiteGroups(java.lang.Object[] portlets, int max) throws java.rmi.RemoteException;
    public com.liferay.portal.model.Group[] getManageableSites(java.lang.Object[] portlets, int max) throws java.rmi.RemoteException;
    public com.liferay.portal.model.Group[] getOrganizationsGroups(com.liferay.portal.model.Organization[] organizations) throws java.rmi.RemoteException;
    public com.liferay.portal.model.Group getUserGroup(long companyId, long userId) throws java.rmi.RemoteException;
    public com.liferay.portal.model.Group[] getUserGroupsGroups(com.liferay.portal.model.UserGroup[] userGroups) throws java.rmi.RemoteException;
    public com.liferay.portal.model.Group[] getUserOrganizationsGroups(long userId, int start, int end) throws java.rmi.RemoteException;
    public int getUserPlacesCount() throws java.rmi.RemoteException;
    public com.liferay.portal.model.Group[] getUserPlaces(java.lang.String[] classNames, int max) throws java.rmi.RemoteException;
    public com.liferay.portal.model.Group[] getUserPlaces(long userId, java.lang.String[] classNames, boolean includeControlPanel, int max) throws java.rmi.RemoteException;
    public com.liferay.portal.model.Group[] getUserPlaces(long userId, java.lang.String[] classNames, int max) throws java.rmi.RemoteException;
    public int getUserSitesGroupsCount() throws java.rmi.RemoteException;
    public com.liferay.portal.model.Group[] getUserSitesGroups() throws java.rmi.RemoteException;
    public com.liferay.portal.model.Group[] getUserSitesGroups(java.lang.String[] classNames, int max) throws java.rmi.RemoteException;
    public com.liferay.portal.model.Group[] getUserSitesGroups(long userId, java.lang.String[] classNames, boolean includeControlPanel, int max) throws java.rmi.RemoteException;
    public com.liferay.portal.model.Group[] getUserSitesGroups(long userId, java.lang.String[] classNames, int max) throws java.rmi.RemoteException;
    public com.liferay.portal.model.Group[] getUserSites() throws java.rmi.RemoteException;
    public boolean hasUserGroup(long userId, long groupId) throws java.rmi.RemoteException;
    public int searchCount(long companyId, java.lang.String name, java.lang.String description, java.lang.String[] params) throws java.rmi.RemoteException;
    public com.liferay.portal.model.Group[] search(long companyId, java.lang.String name, java.lang.String description, java.lang.String[] params, int start, int end) throws java.rmi.RemoteException;
    public void setRoleGroups(long roleId, long[] groupIds) throws java.rmi.RemoteException;
    public void unsetRoleGroups(long roleId, long[] groupIds) throws java.rmi.RemoteException;
    public com.liferay.portal.model.Group updateFriendlyURL(long groupId, java.lang.String friendlyURL) throws java.rmi.RemoteException;
    public com.liferay.portal.model.Group updateGroup(long groupId, long parentGroupId, java.lang.String name, java.lang.String description, int type, boolean manualMembership, int membershipRestriction, java.lang.String friendlyURL, boolean active, com.liferay.portal.service.ServiceContext serviceContext) throws java.rmi.RemoteException;
    public com.liferay.portal.model.Group updateGroup(long groupId, java.lang.String typeSettings) throws java.rmi.RemoteException;
}
