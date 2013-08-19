package com.liferay.portal.service.http;

public class RoleServiceSoapProxy implements com.liferay.portal.service.http.RoleServiceSoap {
  private String _endpoint = null;
  private com.liferay.portal.service.http.RoleServiceSoap roleServiceSoap = null;
  
  public RoleServiceSoapProxy() {
    _initRoleServiceSoapProxy();
  }
  
  public RoleServiceSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initRoleServiceSoapProxy();
  }
  
  private void _initRoleServiceSoapProxy() {
    try {
      roleServiceSoap = (new com.liferay.portal.service.http.RoleServiceSoapServiceLocator()).getPortal_RoleService();
      if (roleServiceSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)roleServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)roleServiceSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (roleServiceSoap != null)
      ((javax.xml.rpc.Stub)roleServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.liferay.portal.service.http.RoleServiceSoap getRoleServiceSoap() {
    if (roleServiceSoap == null)
      _initRoleServiceSoapProxy();
    return roleServiceSoap;
  }
  
  public com.liferay.portal.model.Role addRole(java.lang.String name, java.lang.String[] titleMapLanguageIds, java.lang.String[] titleMapValues, java.lang.String[] descriptionMapLanguageIds, java.lang.String[] descriptionMapValues, int type) throws java.rmi.RemoteException{
    if (roleServiceSoap == null)
      _initRoleServiceSoapProxy();
    return roleServiceSoap.addRole(name, titleMapLanguageIds, titleMapValues, descriptionMapLanguageIds, descriptionMapValues, type);
  }
  
  public void addUserRoles(long userId, long[] roleIds) throws java.rmi.RemoteException{
    if (roleServiceSoap == null)
      _initRoleServiceSoapProxy();
    roleServiceSoap.addUserRoles(userId, roleIds);
  }
  
  public void deleteRole(long roleId) throws java.rmi.RemoteException{
    if (roleServiceSoap == null)
      _initRoleServiceSoapProxy();
    roleServiceSoap.deleteRole(roleId);
  }
  
  public com.liferay.portal.model.Role[] getGroupRoles(long groupId) throws java.rmi.RemoteException{
    if (roleServiceSoap == null)
      _initRoleServiceSoapProxy();
    return roleServiceSoap.getGroupRoles(groupId);
  }
  
  public com.liferay.portal.model.Role getRole(long roleId) throws java.rmi.RemoteException{
    if (roleServiceSoap == null)
      _initRoleServiceSoapProxy();
    return roleServiceSoap.getRole(roleId);
  }
  
  public com.liferay.portal.model.Role getRole(long companyId, java.lang.String name) throws java.rmi.RemoteException{
    if (roleServiceSoap == null)
      _initRoleServiceSoapProxy();
    return roleServiceSoap.getRole(companyId, name);
  }
  
  public com.liferay.portal.model.Role[] getUserGroupGroupRoles(long userId, long groupId) throws java.rmi.RemoteException{
    if (roleServiceSoap == null)
      _initRoleServiceSoapProxy();
    return roleServiceSoap.getUserGroupGroupRoles(userId, groupId);
  }
  
  public com.liferay.portal.model.Role[] getUserGroupRoles(long userId, long groupId) throws java.rmi.RemoteException{
    if (roleServiceSoap == null)
      _initRoleServiceSoapProxy();
    return roleServiceSoap.getUserGroupRoles(userId, groupId);
  }
  
  public com.liferay.portal.model.Role[] getUserRelatedRoles(long userId, com.liferay.portal.model.GroupSoap[] groups) throws java.rmi.RemoteException{
    if (roleServiceSoap == null)
      _initRoleServiceSoapProxy();
    return roleServiceSoap.getUserRelatedRoles(userId, groups);
  }
  
  public com.liferay.portal.model.Role[] getUserRoles(long userId) throws java.rmi.RemoteException{
    if (roleServiceSoap == null)
      _initRoleServiceSoapProxy();
    return roleServiceSoap.getUserRoles(userId);
  }
  
  public boolean hasUserRole(long userId, long companyId, java.lang.String name, boolean inherited) throws java.rmi.RemoteException{
    if (roleServiceSoap == null)
      _initRoleServiceSoapProxy();
    return roleServiceSoap.hasUserRole(userId, companyId, name, inherited);
  }
  
  public boolean hasUserRoles(long userId, long companyId, java.lang.String[] names, boolean inherited) throws java.rmi.RemoteException{
    if (roleServiceSoap == null)
      _initRoleServiceSoapProxy();
    return roleServiceSoap.hasUserRoles(userId, companyId, names, inherited);
  }
  
  public void unsetUserRoles(long userId, long[] roleIds) throws java.rmi.RemoteException{
    if (roleServiceSoap == null)
      _initRoleServiceSoapProxy();
    roleServiceSoap.unsetUserRoles(userId, roleIds);
  }
  
  public com.liferay.portal.model.Role updateRole(long roleId, java.lang.String name, java.lang.String[] titleMapLanguageIds, java.lang.String[] titleMapValues, java.lang.String[] descriptionMapLanguageIds, java.lang.String[] descriptionMapValues, java.lang.String subtype) throws java.rmi.RemoteException{
    if (roleServiceSoap == null)
      _initRoleServiceSoapProxy();
    return roleServiceSoap.updateRole(roleId, name, titleMapLanguageIds, titleMapValues, descriptionMapLanguageIds, descriptionMapValues, subtype);
  }
  
  
}