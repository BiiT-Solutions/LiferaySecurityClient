package com.liferay.portal.service.http;

public class UserServiceSoapProxy implements com.liferay.portal.service.http.UserServiceSoap {
  private String _endpoint = null;
  private com.liferay.portal.service.http.UserServiceSoap userServiceSoap = null;
  
  public UserServiceSoapProxy() {
    _initUserServiceSoapProxy();
  }
  
  public UserServiceSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initUserServiceSoapProxy();
  }
  
  private void _initUserServiceSoapProxy() {
    try {
      userServiceSoap = (new com.liferay.portal.service.http.UserServiceSoapServiceLocator()).getPortal_UserService();
      if (userServiceSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)userServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)userServiceSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (userServiceSoap != null)
      ((javax.xml.rpc.Stub)userServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.liferay.portal.service.http.UserServiceSoap getUserServiceSoap() {
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    return userServiceSoap;
  }
  
  public void addGroupUsers(long groupId, long[] userIds, com.liferay.portal.service.ServiceContext serviceContext) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    userServiceSoap.addGroupUsers(groupId, userIds, serviceContext);
  }
  
  public void addOrganizationUsers(long organizationId, long[] userIds) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    userServiceSoap.addOrganizationUsers(organizationId, userIds);
  }
  
  public void addPasswordPolicyUsers(long passwordPolicyId, long[] userIds) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    userServiceSoap.addPasswordPolicyUsers(passwordPolicyId, userIds);
  }
  
  public void addRoleUsers(long roleId, long[] userIds) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    userServiceSoap.addRoleUsers(roleId, userIds);
  }
  
  public void addTeamUsers(long teamId, long[] userIds) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    userServiceSoap.addTeamUsers(teamId, userIds);
  }
  
  public void addUserGroupUsers(long userGroupId, long[] userIds) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    userServiceSoap.addUserGroupUsers(userGroupId, userIds);
  }
  
  public com.liferay.portal.model.UserSoap addUserWithWorkflow(long companyId, boolean autoPassword, java.lang.String password1, java.lang.String password2, boolean autoScreenName, java.lang.String screenName, java.lang.String emailAddress, long facebookId, java.lang.String openId, java.lang.String locale, java.lang.String firstName, java.lang.String middleName, java.lang.String lastName, int prefixId, int suffixId, boolean male, int birthdayMonth, int birthdayDay, int birthdayYear, java.lang.String jobTitle, long[] groupIds, long[] organizationIds, long[] roleIds, long[] userGroupIds, com.liferay.portal.model.AddressSoap[] addresses, com.liferay.portal.model.EmailAddressSoap[] emailAddresses, com.liferay.portal.model.PhoneSoap[] phones, com.liferay.portal.model.WebsiteSoap[] websites, com.liferay.portlet.announcements.model.AnnouncementsDeliverySoap[] announcementsDelivers, boolean sendEmail, com.liferay.portal.service.ServiceContext serviceContext) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    return userServiceSoap.addUserWithWorkflow(companyId, autoPassword, password1, password2, autoScreenName, screenName, emailAddress, facebookId, openId, locale, firstName, middleName, lastName, prefixId, suffixId, male, birthdayMonth, birthdayDay, birthdayYear, jobTitle, groupIds, organizationIds, roleIds, userGroupIds, addresses, emailAddresses, phones, websites, announcementsDelivers, sendEmail, serviceContext);
  }
  
  public com.liferay.portal.model.UserSoap addUserWithWorkflow(long companyId, boolean autoPassword, java.lang.String password1, java.lang.String password2, boolean autoScreenName, java.lang.String screenName, java.lang.String emailAddress, long facebookId, java.lang.String openId, java.lang.String locale, java.lang.String firstName, java.lang.String middleName, java.lang.String lastName, int prefixId, int suffixId, boolean male, int birthdayMonth, int birthdayDay, int birthdayYear, java.lang.String jobTitle, long[] groupIds, long[] organizationIds, long[] roleIds, long[] userGroupIds, boolean sendEmail, com.liferay.portal.service.ServiceContext serviceContext) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    return userServiceSoap.addUserWithWorkflow(companyId, autoPassword, password1, password2, autoScreenName, screenName, emailAddress, facebookId, openId, locale, firstName, middleName, lastName, prefixId, suffixId, male, birthdayMonth, birthdayDay, birthdayYear, jobTitle, groupIds, organizationIds, roleIds, userGroupIds, sendEmail, serviceContext);
  }
  
  public com.liferay.portal.model.UserSoap addUser(long companyId, boolean autoPassword, java.lang.String password1, java.lang.String password2, boolean autoScreenName, java.lang.String screenName, java.lang.String emailAddress, long facebookId, java.lang.String openId, java.lang.String locale, java.lang.String firstName, java.lang.String middleName, java.lang.String lastName, int prefixId, int suffixId, boolean male, int birthdayMonth, int birthdayDay, int birthdayYear, java.lang.String jobTitle, long[] groupIds, long[] organizationIds, long[] roleIds, long[] userGroupIds, com.liferay.portal.model.AddressSoap[] addresses, com.liferay.portal.model.EmailAddressSoap[] emailAddresses, com.liferay.portal.model.PhoneSoap[] phones, com.liferay.portal.model.WebsiteSoap[] websites, com.liferay.portlet.announcements.model.AnnouncementsDeliverySoap[] announcementsDelivers, boolean sendEmail, com.liferay.portal.service.ServiceContext serviceContext) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    return userServiceSoap.addUser(companyId, autoPassword, password1, password2, autoScreenName, screenName, emailAddress, facebookId, openId, locale, firstName, middleName, lastName, prefixId, suffixId, male, birthdayMonth, birthdayDay, birthdayYear, jobTitle, groupIds, organizationIds, roleIds, userGroupIds, addresses, emailAddresses, phones, websites, announcementsDelivers, sendEmail, serviceContext);
  }
  
  public com.liferay.portal.model.UserSoap addUser(long companyId, boolean autoPassword, java.lang.String password1, java.lang.String password2, boolean autoScreenName, java.lang.String screenName, java.lang.String emailAddress, long facebookId, java.lang.String openId, java.lang.String locale, java.lang.String firstName, java.lang.String middleName, java.lang.String lastName, int prefixId, int suffixId, boolean male, int birthdayMonth, int birthdayDay, int birthdayYear, java.lang.String jobTitle, long[] groupIds, long[] organizationIds, long[] roleIds, long[] userGroupIds, boolean sendEmail, com.liferay.portal.service.ServiceContext serviceContext) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    return userServiceSoap.addUser(companyId, autoPassword, password1, password2, autoScreenName, screenName, emailAddress, facebookId, openId, locale, firstName, middleName, lastName, prefixId, suffixId, male, birthdayMonth, birthdayDay, birthdayYear, jobTitle, groupIds, organizationIds, roleIds, userGroupIds, sendEmail, serviceContext);
  }
  
  public void deletePortrait(long userId) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    userServiceSoap.deletePortrait(userId);
  }
  
  public void deleteRoleUser(long roleId, long userId) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    userServiceSoap.deleteRoleUser(roleId, userId);
  }
  
  public void deleteUser(long userId) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    userServiceSoap.deleteUser(userId);
  }
  
  public int getCompanyUsersCount(long companyId) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    return userServiceSoap.getCompanyUsersCount(companyId);
  }
  
  public com.liferay.portal.model.UserSoap[] getCompanyUsers(long companyId, int start, int end) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    return userServiceSoap.getCompanyUsers(companyId, start, end);
  }
  
  public long getDefaultUserId(long companyId) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    return userServiceSoap.getDefaultUserId(companyId);
  }
  
  public long[] getGroupUserIds(long groupId) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    return userServiceSoap.getGroupUserIds(groupId);
  }
  
  public com.liferay.portal.model.UserSoap[] getGroupUsers(long groupId) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    return userServiceSoap.getGroupUsers(groupId);
  }
  
  public long[] getOrganizationUserIds(long organizationId) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    return userServiceSoap.getOrganizationUserIds(organizationId);
  }
  
  public com.liferay.portal.model.UserSoap[] getOrganizationUsers(long organizationId) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    return userServiceSoap.getOrganizationUsers(organizationId);
  }
  
  public long[] getRoleUserIds(long roleId) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    return userServiceSoap.getRoleUserIds(roleId);
  }
  
  public com.liferay.portal.model.UserSoap getUserByEmailAddress(long companyId, java.lang.String emailAddress) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    return userServiceSoap.getUserByEmailAddress(companyId, emailAddress);
  }
  
  public com.liferay.portal.model.UserSoap getUserById(long userId) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    return userServiceSoap.getUserById(userId);
  }
  
  public com.liferay.portal.model.UserSoap getUserByScreenName(long companyId, java.lang.String screenName) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    return userServiceSoap.getUserByScreenName(companyId, screenName);
  }
  
  public com.liferay.portal.model.UserSoap[] getUserGroupUsers(long userGroupId) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    return userServiceSoap.getUserGroupUsers(userGroupId);
  }
  
  public long getUserIdByEmailAddress(long companyId, java.lang.String emailAddress) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    return userServiceSoap.getUserIdByEmailAddress(companyId, emailAddress);
  }
  
  public long getUserIdByScreenName(long companyId, java.lang.String screenName) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    return userServiceSoap.getUserIdByScreenName(companyId, screenName);
  }
  
  public boolean hasGroupUser(long groupId, long userId) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    return userServiceSoap.hasGroupUser(groupId, userId);
  }
  
  public boolean hasRoleUser(long roleId, long userId) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    return userServiceSoap.hasRoleUser(roleId, userId);
  }
  
  public boolean hasRoleUser(long companyId, java.lang.String name, long userId, boolean inherited) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    return userServiceSoap.hasRoleUser(companyId, name, userId, inherited);
  }
  
  public void setRoleUsers(long roleId, long[] userIds) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    userServiceSoap.setRoleUsers(roleId, userIds);
  }
  
  public void setUserGroupUsers(long userGroupId, long[] userIds) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    userServiceSoap.setUserGroupUsers(userGroupId, userIds);
  }
  
  public void unsetGroupTeamsUsers(long groupId, long[] userIds) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    userServiceSoap.unsetGroupTeamsUsers(groupId, userIds);
  }
  
  public void unsetGroupUsers(long groupId, long[] userIds, com.liferay.portal.service.ServiceContext serviceContext) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    userServiceSoap.unsetGroupUsers(groupId, userIds, serviceContext);
  }
  
  public void unsetOrganizationUsers(long organizationId, long[] userIds) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    userServiceSoap.unsetOrganizationUsers(organizationId, userIds);
  }
  
  public void unsetPasswordPolicyUsers(long passwordPolicyId, long[] userIds) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    userServiceSoap.unsetPasswordPolicyUsers(passwordPolicyId, userIds);
  }
  
  public void unsetRoleUsers(long roleId, long[] userIds) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    userServiceSoap.unsetRoleUsers(roleId, userIds);
  }
  
  public void unsetTeamUsers(long teamId, long[] userIds) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    userServiceSoap.unsetTeamUsers(teamId, userIds);
  }
  
  public void unsetUserGroupUsers(long userGroupId, long[] userIds) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    userServiceSoap.unsetUserGroupUsers(userGroupId, userIds);
  }
  
  public com.liferay.portal.model.UserSoap updateAgreedToTermsOfUse(long userId, boolean agreedToTermsOfUse) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    return userServiceSoap.updateAgreedToTermsOfUse(userId, agreedToTermsOfUse);
  }
  
  public com.liferay.portal.model.UserSoap updateEmailAddress(long userId, java.lang.String password, java.lang.String emailAddress1, java.lang.String emailAddress2, com.liferay.portal.service.ServiceContext serviceContext) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    return userServiceSoap.updateEmailAddress(userId, password, emailAddress1, emailAddress2, serviceContext);
  }
  
  public com.liferay.portal.model.UserSoap updateIncompleteUser(long companyId, boolean autoPassword, java.lang.String password1, java.lang.String password2, boolean autoScreenName, java.lang.String screenName, java.lang.String emailAddress, long facebookId, java.lang.String openId, java.lang.String locale, java.lang.String firstName, java.lang.String middleName, java.lang.String lastName, int prefixId, int suffixId, boolean male, int birthdayMonth, int birthdayDay, int birthdayYear, java.lang.String jobTitle, boolean updateUserInformation, boolean sendEmail, com.liferay.portal.service.ServiceContext serviceContext) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    return userServiceSoap.updateIncompleteUser(companyId, autoPassword, password1, password2, autoScreenName, screenName, emailAddress, facebookId, openId, locale, firstName, middleName, lastName, prefixId, suffixId, male, birthdayMonth, birthdayDay, birthdayYear, jobTitle, updateUserInformation, sendEmail, serviceContext);
  }
  
  public com.liferay.portal.model.UserSoap updateLockoutById(long userId, boolean lockout) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    return userServiceSoap.updateLockoutById(userId, lockout);
  }
  
  public com.liferay.portal.model.UserSoap updateOpenId(long userId, java.lang.String openId) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    return userServiceSoap.updateOpenId(userId, openId);
  }
  
  public void updateOrganizations(long userId, long[] organizationIds, com.liferay.portal.service.ServiceContext serviceContext) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    userServiceSoap.updateOrganizations(userId, organizationIds, serviceContext);
  }
  
  public com.liferay.portal.model.UserSoap updatePassword(long userId, java.lang.String password1, java.lang.String password2, boolean passwordReset) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    return userServiceSoap.updatePassword(userId, password1, password2, passwordReset);
  }
  
  public com.liferay.portal.model.UserSoap updatePortrait(long userId, byte[] bytes) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    return userServiceSoap.updatePortrait(userId, bytes);
  }
  
  public com.liferay.portal.model.UserSoap updateReminderQuery(long userId, java.lang.String question, java.lang.String answer) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    return userServiceSoap.updateReminderQuery(userId, question, answer);
  }
  
  public com.liferay.portal.model.UserSoap updateScreenName(long userId, java.lang.String screenName) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    return userServiceSoap.updateScreenName(userId, screenName);
  }
  
  public com.liferay.portal.model.UserSoap updateStatus(long userId, int status) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    return userServiceSoap.updateStatus(userId, status);
  }
  
  public com.liferay.portal.model.UserSoap updateUser(long userId, java.lang.String oldPassword, java.lang.String newPassword1, java.lang.String newPassword2, boolean passwordReset, java.lang.String reminderQueryQuestion, java.lang.String reminderQueryAnswer, java.lang.String screenName, java.lang.String emailAddress, long facebookId, java.lang.String openId, java.lang.String languageId, java.lang.String timeZoneId, java.lang.String greeting, java.lang.String comments, java.lang.String firstName, java.lang.String middleName, java.lang.String lastName, int prefixId, int suffixId, boolean male, int birthdayMonth, int birthdayDay, int birthdayYear, java.lang.String smsSn, java.lang.String aimSn, java.lang.String facebookSn, java.lang.String icqSn, java.lang.String jabberSn, java.lang.String msnSn, java.lang.String mySpaceSn, java.lang.String skypeSn, java.lang.String twitterSn, java.lang.String ymSn, java.lang.String jobTitle, long[] groupIds, long[] organizationIds, long[] roleIds, com.liferay.portal.model.UserGroupRoleSoap[] userGroupRoles, long[] userGroupIds, com.liferay.portal.model.AddressSoap[] addresses, com.liferay.portal.model.EmailAddressSoap[] emailAddresses, com.liferay.portal.model.PhoneSoap[] phones, com.liferay.portal.model.WebsiteSoap[] websites, com.liferay.portlet.announcements.model.AnnouncementsDeliverySoap[] announcementsDelivers, com.liferay.portal.service.ServiceContext serviceContext) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    return userServiceSoap.updateUser(userId, oldPassword, newPassword1, newPassword2, passwordReset, reminderQueryQuestion, reminderQueryAnswer, screenName, emailAddress, facebookId, openId, languageId, timeZoneId, greeting, comments, firstName, middleName, lastName, prefixId, suffixId, male, birthdayMonth, birthdayDay, birthdayYear, smsSn, aimSn, facebookSn, icqSn, jabberSn, msnSn, mySpaceSn, skypeSn, twitterSn, ymSn, jobTitle, groupIds, organizationIds, roleIds, userGroupRoles, userGroupIds, addresses, emailAddresses, phones, websites, announcementsDelivers, serviceContext);
  }
  
  public com.liferay.portal.model.UserSoap updateUser(long userId, java.lang.String oldPassword, java.lang.String newPassword1, java.lang.String newPassword2, boolean passwordReset, java.lang.String reminderQueryQuestion, java.lang.String reminderQueryAnswer, java.lang.String screenName, java.lang.String emailAddress, long facebookId, java.lang.String openId, java.lang.String languageId, java.lang.String timeZoneId, java.lang.String greeting, java.lang.String comments, java.lang.String firstName, java.lang.String middleName, java.lang.String lastName, int prefixId, int suffixId, boolean male, int birthdayMonth, int birthdayDay, int birthdayYear, java.lang.String smsSn, java.lang.String aimSn, java.lang.String facebookSn, java.lang.String icqSn, java.lang.String jabberSn, java.lang.String msnSn, java.lang.String mySpaceSn, java.lang.String skypeSn, java.lang.String twitterSn, java.lang.String ymSn, java.lang.String jobTitle, long[] groupIds, long[] organizationIds, long[] roleIds, com.liferay.portal.model.UserGroupRoleSoap[] userGroupRoles, long[] userGroupIds, com.liferay.portal.service.ServiceContext serviceContext) throws java.rmi.RemoteException{
    if (userServiceSoap == null)
      _initUserServiceSoapProxy();
    return userServiceSoap.updateUser(userId, oldPassword, newPassword1, newPassword2, passwordReset, reminderQueryQuestion, reminderQueryAnswer, screenName, emailAddress, facebookId, openId, languageId, timeZoneId, greeting, comments, firstName, middleName, lastName, prefixId, suffixId, male, birthdayMonth, birthdayDay, birthdayYear, smsSn, aimSn, facebookSn, icqSn, jabberSn, msnSn, mySpaceSn, skypeSn, twitterSn, ymSn, jobTitle, groupIds, organizationIds, roleIds, userGroupRoles, userGroupIds, serviceContext);
  }
  
  
}