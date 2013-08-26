/**
 * GroupServiceSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.liferay.portal.service.http;

public interface GroupServiceSoap extends java.rmi.Remote {
    public com.liferay.portal.model.GroupSoap addGroup(long liveGroupId, java.lang.String name, java.lang.String description, int type, java.lang.String friendlyURL, boolean site, boolean active, com.liferay.portal.service.ServiceContext serviceContext) throws java.rmi.RemoteException;
    public com.liferay.portal.model.GroupSoap addGroup(java.lang.String name, java.lang.String description, int type, java.lang.String friendlyURL, boolean site, boolean active, com.liferay.portal.service.ServiceContext serviceContext) throws java.rmi.RemoteException;
    public void addRoleGroups(long roleId, long[] groupIds) throws java.rmi.RemoteException;
    public void deleteGroup(long groupId) throws java.rmi.RemoteException;
    public com.liferay.portal.model.GroupSoap getGroup(long groupId) throws java.rmi.RemoteException;
    public com.liferay.portal.model.GroupSoap getGroup(long companyId, java.lang.String name) throws java.rmi.RemoteException;
    public com.liferay.portal.model.GroupSoap[] getManageableSites(java.lang.Object[] portlets, int max) throws java.rmi.RemoteException;
    public com.liferay.portal.model.GroupSoap[] getOrganizationsGroups(com.liferay.portal.model.OrganizationSoap[] organizations) throws java.rmi.RemoteException;
    public com.liferay.portal.model.GroupSoap getUserGroup(long companyId, long userId) throws java.rmi.RemoteException;
    public com.liferay.portal.model.GroupSoap[] getUserGroupsGroups(com.liferay.portal.model.UserGroupSoap[] userGroups) throws java.rmi.RemoteException;
    public com.liferay.portal.model.GroupSoap[] getUserOrganizationsGroups(long userId, int start, int end) throws java.rmi.RemoteException;
    public com.liferay.portal.model.GroupSoap[] getUserPlaces(java.lang.String[] classNames, int max) throws java.rmi.RemoteException;
    public com.liferay.portal.model.GroupSoap[] getUserPlaces(long userId, java.lang.String[] classNames, boolean includeControlPanel, int max) throws java.rmi.RemoteException;
    public com.liferay.portal.model.GroupSoap[] getUserPlaces(long userId, java.lang.String[] classNames, int max) throws java.rmi.RemoteException;
    public com.liferay.portal.model.GroupSoap[] getUserSites() throws java.rmi.RemoteException;
    public boolean hasUserGroup(long userId, long groupId) throws java.rmi.RemoteException;
    public int searchCount(long companyId, java.lang.String name, java.lang.String description, java.lang.String[] params) throws java.rmi.RemoteException;
    public com.liferay.portal.model.GroupSoap[] search(long companyId, java.lang.String name, java.lang.String description, java.lang.String[] params, int start, int end) throws java.rmi.RemoteException;
    public void setRoleGroups(long roleId, long[] groupIds) throws java.rmi.RemoteException;
    public void unsetRoleGroups(long roleId, long[] groupIds) throws java.rmi.RemoteException;
    public com.liferay.portal.model.GroupSoap updateFriendlyURL(long groupId, java.lang.String friendlyURL) throws java.rmi.RemoteException;
    public com.liferay.portal.model.GroupSoap updateGroup(long groupId, java.lang.String typeSettings) throws java.rmi.RemoteException;
    public com.liferay.portal.model.GroupSoap updateGroup(long groupId, java.lang.String name, java.lang.String description, int type, java.lang.String friendlyURL, boolean active, com.liferay.portal.service.ServiceContext serviceContext) throws java.rmi.RemoteException;
}
