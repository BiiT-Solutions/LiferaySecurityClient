package com.liferay.portal.service.http;

public class UserGroupServiceSoapProxy implements com.liferay.portal.service.http.UserGroupServiceSoap {
  private String _endpoint = null;
  private com.liferay.portal.service.http.UserGroupServiceSoap userGroupServiceSoap = null;
  
  public UserGroupServiceSoapProxy() {
    _initUserGroupServiceSoapProxy();
  }
  
  public UserGroupServiceSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initUserGroupServiceSoapProxy();
  }
  
  private void _initUserGroupServiceSoapProxy() {
    try {
      userGroupServiceSoap = (new com.liferay.portal.service.http.UserGroupServiceSoapServiceLocator()).getPortal_UserGroupService();
      if (userGroupServiceSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)userGroupServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)userGroupServiceSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (userGroupServiceSoap != null)
      ((javax.xml.rpc.Stub)userGroupServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.liferay.portal.service.http.UserGroupServiceSoap getUserGroupServiceSoap() {
    if (userGroupServiceSoap == null)
      _initUserGroupServiceSoapProxy();
    return userGroupServiceSoap;
  }
  
  public void addGroupUserGroups(long groupId, long[] userGroupIds) throws java.rmi.RemoteException{
    if (userGroupServiceSoap == null)
      _initUserGroupServiceSoapProxy();
    userGroupServiceSoap.addGroupUserGroups(groupId, userGroupIds);
  }
  
  public void addTeamUserGroups(long teamId, long[] userGroupIds) throws java.rmi.RemoteException{
    if (userGroupServiceSoap == null)
      _initUserGroupServiceSoapProxy();
    userGroupServiceSoap.addTeamUserGroups(teamId, userGroupIds);
  }
  
  public com.liferay.portal.model.UserGroup addUserGroup(java.lang.String name, java.lang.String description) throws java.rmi.RemoteException{
    if (userGroupServiceSoap == null)
      _initUserGroupServiceSoapProxy();
    return userGroupServiceSoap.addUserGroup(name, description);
  }
  
  public void deleteUserGroup(long userGroupId) throws java.rmi.RemoteException{
    if (userGroupServiceSoap == null)
      _initUserGroupServiceSoapProxy();
    userGroupServiceSoap.deleteUserGroup(userGroupId);
  }
  
  public com.liferay.portal.model.UserGroup getUserGroup(long userGroupId) throws java.rmi.RemoteException{
    if (userGroupServiceSoap == null)
      _initUserGroupServiceSoapProxy();
    return userGroupServiceSoap.getUserGroup(userGroupId);
  }
  
  public com.liferay.portal.model.UserGroup getUserGroup(java.lang.String name) throws java.rmi.RemoteException{
    if (userGroupServiceSoap == null)
      _initUserGroupServiceSoapProxy();
    return userGroupServiceSoap.getUserGroup(name);
  }
  
  public com.liferay.portal.model.UserGroup[] getUserUserGroups(long userId) throws java.rmi.RemoteException{
    if (userGroupServiceSoap == null)
      _initUserGroupServiceSoapProxy();
    return userGroupServiceSoap.getUserUserGroups(userId);
  }
  
  public void unsetGroupUserGroups(long groupId, long[] userGroupIds) throws java.rmi.RemoteException{
    if (userGroupServiceSoap == null)
      _initUserGroupServiceSoapProxy();
    userGroupServiceSoap.unsetGroupUserGroups(groupId, userGroupIds);
  }
  
  public void unsetTeamUserGroups(long teamId, long[] userGroupIds) throws java.rmi.RemoteException{
    if (userGroupServiceSoap == null)
      _initUserGroupServiceSoapProxy();
    userGroupServiceSoap.unsetTeamUserGroups(teamId, userGroupIds);
  }
  
  public com.liferay.portal.model.UserGroup updateUserGroup(long userGroupId, java.lang.String name, java.lang.String description) throws java.rmi.RemoteException{
    if (userGroupServiceSoap == null)
      _initUserGroupServiceSoapProxy();
    return userGroupServiceSoap.updateUserGroup(userGroupId, name, description);
  }
  
  
}