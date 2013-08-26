/**
 * UserGroupRoleServiceSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.liferay.portal.service.http;

public interface UserGroupRoleServiceSoap extends java.rmi.Remote {
    public void addUserGroupRoles(java.lang.Long[] userIds, long groupId, long roleId) throws java.rmi.RemoteException;
    public void addUserGroupRoles(long userId, long groupId, java.lang.Long[] roleIds) throws java.rmi.RemoteException;
    public void deleteUserGroupRoles(java.lang.Long[] userIds, long groupId, long roleId) throws java.rmi.RemoteException;
    public void deleteUserGroupRoles(long userId, long groupId, java.lang.Long[] roleIds) throws java.rmi.RemoteException;
}
