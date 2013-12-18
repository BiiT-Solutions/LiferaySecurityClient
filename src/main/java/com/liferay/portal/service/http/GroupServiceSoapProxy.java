package com.liferay.portal.service.http;

public class GroupServiceSoapProxy implements com.liferay.portal.service.http.GroupServiceSoap {
  private String _endpoint = null;
  private com.liferay.portal.service.http.GroupServiceSoap groupServiceSoap = null;
  
  public GroupServiceSoapProxy() {
    _initGroupServiceSoapProxy();
  }
  
  public GroupServiceSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initGroupServiceSoapProxy();
  }
  
  private void _initGroupServiceSoapProxy() {
    try {
      groupServiceSoap = (new com.liferay.portal.service.http.GroupServiceSoapServiceLocator()).getPortal_GroupService();
      if (groupServiceSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)groupServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)groupServiceSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (groupServiceSoap != null)
      ((javax.xml.rpc.Stub)groupServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.liferay.portal.service.http.GroupServiceSoap getGroupServiceSoap() {
    if (groupServiceSoap == null)
      _initGroupServiceSoapProxy();
    return groupServiceSoap;
  }
  
  public com.liferay.portal.model.Group addGroup(long parentGroupId, long liveGroupId, java.lang.String name, java.lang.String description, int type, boolean manualMembership, int membershipRestriction, java.lang.String friendlyURL, boolean site, boolean active, com.liferay.portal.service.ServiceContext serviceContext) throws java.rmi.RemoteException{
    if (groupServiceSoap == null)
      _initGroupServiceSoapProxy();
    return groupServiceSoap.addGroup(parentGroupId, liveGroupId, name, description, type, manualMembership, membershipRestriction, friendlyURL, site, active, serviceContext);
  }
  
  public com.liferay.portal.model.Group addGroup(long parentGroupId, java.lang.String name, java.lang.String description, int type, java.lang.String friendlyURL, boolean site, boolean active, com.liferay.portal.service.ServiceContext serviceContext) throws java.rmi.RemoteException{
    if (groupServiceSoap == null)
      _initGroupServiceSoapProxy();
    return groupServiceSoap.addGroup(parentGroupId, name, description, type, friendlyURL, site, active, serviceContext);
  }
  
  public com.liferay.portal.model.Group addGroup(java.lang.String name, java.lang.String description, int type, java.lang.String friendlyURL, boolean site, boolean active, com.liferay.portal.service.ServiceContext serviceContext) throws java.rmi.RemoteException{
    if (groupServiceSoap == null)
      _initGroupServiceSoapProxy();
    return groupServiceSoap.addGroup(name, description, type, friendlyURL, site, active, serviceContext);
  }
  
  public void addRoleGroups(long roleId, long[] groupIds) throws java.rmi.RemoteException{
    if (groupServiceSoap == null)
      _initGroupServiceSoapProxy();
    groupServiceSoap.addRoleGroups(roleId, groupIds);
  }
  
  public void checkRemoteStagingGroup(long groupId) throws java.rmi.RemoteException{
    if (groupServiceSoap == null)
      _initGroupServiceSoapProxy();
    groupServiceSoap.checkRemoteStagingGroup(groupId);
  }
  
  public void deleteGroup(long groupId) throws java.rmi.RemoteException{
    if (groupServiceSoap == null)
      _initGroupServiceSoapProxy();
    groupServiceSoap.deleteGroup(groupId);
  }
  
  public void disableStaging(long groupId) throws java.rmi.RemoteException{
    if (groupServiceSoap == null)
      _initGroupServiceSoapProxy();
    groupServiceSoap.disableStaging(groupId);
  }
  
  public void enableStaging(long groupId) throws java.rmi.RemoteException{
    if (groupServiceSoap == null)
      _initGroupServiceSoapProxy();
    groupServiceSoap.enableStaging(groupId);
  }
  
  public com.liferay.portal.model.Group getCompanyGroup(long companyId) throws java.rmi.RemoteException{
    if (groupServiceSoap == null)
      _initGroupServiceSoapProxy();
    return groupServiceSoap.getCompanyGroup(companyId);
  }
  
  public com.liferay.portal.model.Group getGroup(long groupId) throws java.rmi.RemoteException{
    if (groupServiceSoap == null)
      _initGroupServiceSoapProxy();
    return groupServiceSoap.getGroup(groupId);
  }
  
  public com.liferay.portal.model.Group getGroup(long companyId, java.lang.String name) throws java.rmi.RemoteException{
    if (groupServiceSoap == null)
      _initGroupServiceSoapProxy();
    return groupServiceSoap.getGroup(companyId, name);
  }
  
  public com.liferay.portal.model.Group[] getGroups(long companyId, long parentGroupId, boolean site) throws java.rmi.RemoteException{
    if (groupServiceSoap == null)
      _initGroupServiceSoapProxy();
    return groupServiceSoap.getGroups(companyId, parentGroupId, site);
  }
  
  public com.liferay.portal.model.Group[] getManageableSiteGroups(java.lang.Object[] portlets, int max) throws java.rmi.RemoteException{
    if (groupServiceSoap == null)
      _initGroupServiceSoapProxy();
    return groupServiceSoap.getManageableSiteGroups(portlets, max);
  }
  
  public com.liferay.portal.model.Group[] getManageableSites(java.lang.Object[] portlets, int max) throws java.rmi.RemoteException{
    if (groupServiceSoap == null)
      _initGroupServiceSoapProxy();
    return groupServiceSoap.getManageableSites(portlets, max);
  }
  
  public com.liferay.portal.model.Group[] getOrganizationsGroups(com.liferay.portal.model.Organization[] organizations) throws java.rmi.RemoteException{
    if (groupServiceSoap == null)
      _initGroupServiceSoapProxy();
    return groupServiceSoap.getOrganizationsGroups(organizations);
  }
  
  public com.liferay.portal.model.Group getUserGroup(long companyId, long userId) throws java.rmi.RemoteException{
    if (groupServiceSoap == null)
      _initGroupServiceSoapProxy();
    return groupServiceSoap.getUserGroup(companyId, userId);
  }
  
  public com.liferay.portal.model.Group[] getUserGroupsGroups(com.liferay.portal.model.UserGroup[] userGroups) throws java.rmi.RemoteException{
    if (groupServiceSoap == null)
      _initGroupServiceSoapProxy();
    return groupServiceSoap.getUserGroupsGroups(userGroups);
  }
  
  public com.liferay.portal.model.Group[] getUserOrganizationsGroups(long userId, int start, int end) throws java.rmi.RemoteException{
    if (groupServiceSoap == null)
      _initGroupServiceSoapProxy();
    return groupServiceSoap.getUserOrganizationsGroups(userId, start, end);
  }
  
  public int getUserPlacesCount() throws java.rmi.RemoteException{
    if (groupServiceSoap == null)
      _initGroupServiceSoapProxy();
    return groupServiceSoap.getUserPlacesCount();
  }
  
  public com.liferay.portal.model.Group[] getUserPlaces(java.lang.String[] classNames, int max) throws java.rmi.RemoteException{
    if (groupServiceSoap == null)
      _initGroupServiceSoapProxy();
    return groupServiceSoap.getUserPlaces(classNames, max);
  }
  
  public com.liferay.portal.model.Group[] getUserPlaces(long userId, java.lang.String[] classNames, boolean includeControlPanel, int max) throws java.rmi.RemoteException{
    if (groupServiceSoap == null)
      _initGroupServiceSoapProxy();
    return groupServiceSoap.getUserPlaces(userId, classNames, includeControlPanel, max);
  }
  
  public com.liferay.portal.model.Group[] getUserPlaces(long userId, java.lang.String[] classNames, int max) throws java.rmi.RemoteException{
    if (groupServiceSoap == null)
      _initGroupServiceSoapProxy();
    return groupServiceSoap.getUserPlaces(userId, classNames, max);
  }
  
  public int getUserSitesGroupsCount() throws java.rmi.RemoteException{
    if (groupServiceSoap == null)
      _initGroupServiceSoapProxy();
    return groupServiceSoap.getUserSitesGroupsCount();
  }
  
  public com.liferay.portal.model.Group[] getUserSitesGroups() throws java.rmi.RemoteException{
    if (groupServiceSoap == null)
      _initGroupServiceSoapProxy();
    return groupServiceSoap.getUserSitesGroups();
  }
  
  public com.liferay.portal.model.Group[] getUserSitesGroups(java.lang.String[] classNames, int max) throws java.rmi.RemoteException{
    if (groupServiceSoap == null)
      _initGroupServiceSoapProxy();
    return groupServiceSoap.getUserSitesGroups(classNames, max);
  }
  
  public com.liferay.portal.model.Group[] getUserSitesGroups(long userId, java.lang.String[] classNames, boolean includeControlPanel, int max) throws java.rmi.RemoteException{
    if (groupServiceSoap == null)
      _initGroupServiceSoapProxy();
    return groupServiceSoap.getUserSitesGroups(userId, classNames, includeControlPanel, max);
  }
  
  public com.liferay.portal.model.Group[] getUserSitesGroups(long userId, java.lang.String[] classNames, int max) throws java.rmi.RemoteException{
    if (groupServiceSoap == null)
      _initGroupServiceSoapProxy();
    return groupServiceSoap.getUserSitesGroups(userId, classNames, max);
  }
  
  public com.liferay.portal.model.Group[] getUserSites() throws java.rmi.RemoteException{
    if (groupServiceSoap == null)
      _initGroupServiceSoapProxy();
    return groupServiceSoap.getUserSites();
  }
  
  public boolean hasUserGroup(long userId, long groupId) throws java.rmi.RemoteException{
    if (groupServiceSoap == null)
      _initGroupServiceSoapProxy();
    return groupServiceSoap.hasUserGroup(userId, groupId);
  }
  
  public int searchCount(long companyId, java.lang.String name, java.lang.String description, java.lang.String[] params) throws java.rmi.RemoteException{
    if (groupServiceSoap == null)
      _initGroupServiceSoapProxy();
    return groupServiceSoap.searchCount(companyId, name, description, params);
  }
  
  public com.liferay.portal.model.Group[] search(long companyId, java.lang.String name, java.lang.String description, java.lang.String[] params, int start, int end) throws java.rmi.RemoteException{
    if (groupServiceSoap == null)
      _initGroupServiceSoapProxy();
    return groupServiceSoap.search(companyId, name, description, params, start, end);
  }
  
  public void setRoleGroups(long roleId, long[] groupIds) throws java.rmi.RemoteException{
    if (groupServiceSoap == null)
      _initGroupServiceSoapProxy();
    groupServiceSoap.setRoleGroups(roleId, groupIds);
  }
  
  public void unsetRoleGroups(long roleId, long[] groupIds) throws java.rmi.RemoteException{
    if (groupServiceSoap == null)
      _initGroupServiceSoapProxy();
    groupServiceSoap.unsetRoleGroups(roleId, groupIds);
  }
  
  public com.liferay.portal.model.Group updateFriendlyURL(long groupId, java.lang.String friendlyURL) throws java.rmi.RemoteException{
    if (groupServiceSoap == null)
      _initGroupServiceSoapProxy();
    return groupServiceSoap.updateFriendlyURL(groupId, friendlyURL);
  }
  
  public com.liferay.portal.model.Group updateGroup(long groupId, long parentGroupId, java.lang.String name, java.lang.String description, int type, boolean manualMembership, int membershipRestriction, java.lang.String friendlyURL, boolean active, com.liferay.portal.service.ServiceContext serviceContext) throws java.rmi.RemoteException{
    if (groupServiceSoap == null)
      _initGroupServiceSoapProxy();
    return groupServiceSoap.updateGroup(groupId, parentGroupId, name, description, type, manualMembership, membershipRestriction, friendlyURL, active, serviceContext);
  }
  
  public com.liferay.portal.model.Group updateGroup(long groupId, java.lang.String typeSettings) throws java.rmi.RemoteException{
    if (groupServiceSoap == null)
      _initGroupServiceSoapProxy();
    return groupServiceSoap.updateGroup(groupId, typeSettings);
  }
  
  
}