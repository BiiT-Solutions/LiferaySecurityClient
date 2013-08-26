package com.liferay.portal.service.http;

public class UserGroupRoleServiceSoapProxy implements com.liferay.portal.service.http.UserGroupRoleServiceSoap {
  private String _endpoint = null;
  private com.liferay.portal.service.http.UserGroupRoleServiceSoap userGroupRoleServiceSoap = null;
  
  public UserGroupRoleServiceSoapProxy() {
    _initUserGroupRoleServiceSoapProxy();
  }
  
  public UserGroupRoleServiceSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initUserGroupRoleServiceSoapProxy();
  }
  
  private void _initUserGroupRoleServiceSoapProxy() {
    try {
      userGroupRoleServiceSoap = (new com.liferay.portal.service.http.UserGroupRoleServiceSoapServiceLocator()).getPortal_UserGroupRoleService();
      if (userGroupRoleServiceSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)userGroupRoleServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)userGroupRoleServiceSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (userGroupRoleServiceSoap != null)
      ((javax.xml.rpc.Stub)userGroupRoleServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.liferay.portal.service.http.UserGroupRoleServiceSoap getUserGroupRoleServiceSoap() {
    if (userGroupRoleServiceSoap == null)
      _initUserGroupRoleServiceSoapProxy();
    return userGroupRoleServiceSoap;
  }
  
  public void addUserGroupRoles(java.lang.Long[] userIds, long groupId, long roleId) throws java.rmi.RemoteException{
    if (userGroupRoleServiceSoap == null)
      _initUserGroupRoleServiceSoapProxy();
    userGroupRoleServiceSoap.addUserGroupRoles(userIds, groupId, roleId);
  }
  
  public void addUserGroupRoles(long userId, long groupId, java.lang.Long[] roleIds) throws java.rmi.RemoteException{
    if (userGroupRoleServiceSoap == null)
      _initUserGroupRoleServiceSoapProxy();
    userGroupRoleServiceSoap.addUserGroupRoles(userId, groupId, roleIds);
  }
  
  public void deleteUserGroupRoles(java.lang.Long[] userIds, long groupId, long roleId) throws java.rmi.RemoteException{
    if (userGroupRoleServiceSoap == null)
      _initUserGroupRoleServiceSoapProxy();
    userGroupRoleServiceSoap.deleteUserGroupRoles(userIds, groupId, roleId);
  }
  
  public void deleteUserGroupRoles(long userId, long groupId, java.lang.Long[] roleIds) throws java.rmi.RemoteException{
    if (userGroupRoleServiceSoap == null)
      _initUserGroupRoleServiceSoapProxy();
    userGroupRoleServiceSoap.deleteUserGroupRoles(userId, groupId, roleIds);
  }
  
  
}