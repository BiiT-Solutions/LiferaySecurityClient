package com.biit.liferay.access;

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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.biit.liferay.security.AuthorizationService;
import com.biit.usermanager.security.exceptions.UserManagementException;
import org.apache.http.client.ClientProtocolException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import com.biit.liferay.access.exceptions.DuplicatedLiferayElement;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.OrganizationNotDeletedException;
import com.biit.liferay.access.exceptions.RoleNotDeletedException;
import com.biit.liferay.access.exceptions.WebServiceAccessError;
import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.entity.IRole;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.security.exceptions.AuthenticationRequired;
import com.biit.usermanager.security.exceptions.RoleDoesNotExistsException;
import com.biit.usermanager.security.exceptions.UserDoesNotExistException;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.RoleType;

public class AccessTest {
    private static final String LOGIN_USER = "webservices@test.com";
    private static final String LOGIN_PASSWORD = "my-pass";
    private static final String HOST = "testing.test.com";

    private final static String LIFERAY_PROTOCOL = "https";
    private final static int PORT = 443;
    private final static String PROXY_PREFIX = "liferay/";
    private final static String WEBSERVICES_PATH = "api/jsonws/";
    private final static String AUTHENTICATION_TOKEN = "11111111";
    private final static String COMPANY_VIRTUALHOST = "testing.test.com";

    private final static String TEST_USER = "AnotherTestUser";
    private final static String TEST_USER_MAIL = TEST_USER + "@dummyemail.com";
    private final static String TEST_USER_PASSWORD = "asd123";
    private final static int TEST_USER_BIRTHDAY_DAY = 10;
    private final static int TEST_USER_BIRTHDAY_MONTH = 10;
    private final static int TEST_USER_BIRTHDAY_YEAR = 1975;

    private final static String TEST_ROLE = "TestUserRole";
    private final static String TEST_GROUP_ROLE = "TestingUserGroupRole";
    private final static String TEST_GROUP = "TestGroupSecurity";

    private final static String TEST_ORGANIZATION_1 = "TestOrganization1";
    private final static String TEST_ORGANIZATION_2 = "TestOrganization2";
    private final static String TEST_ORGANIZATION_ROLE = "TestUserOrganizationRole";

    private IGroup<Long> company;
    private IUser<Long> user;
    private IRole<Long> role, groupRole, organizationRole;
    private IGroup<Long> group;
    private IGroup<Long> organization1, organization2;

    private CompanyService companyService = new CompanyService();
    private UserService userService = new UserService();
    private UserGroupService userGroupService = new UserGroupService();
    private RoleService roleService = new RoleService();
    private GroupService groupService = new GroupService();
    private OrganizationService organizationService = new OrganizationService();
    private AuthorizationService authorizationService = new AuthorizationService();

    @Test(groups = {"connection"})
    public void authorized()
            throws NotConnectedToWebServiceException, IOException, AuthenticationRequired, WebServiceAccessError {
        companyService.authorizedServerConnection(HOST, LIFERAY_PROTOCOL, PORT, PROXY_PREFIX, WEBSERVICES_PATH, AUTHENTICATION_TOKEN, LOGIN_USER, LOGIN_PASSWORD);
        companyService.getCompanyByVirtualHost(COMPANY_VIRTUALHOST);
    }

    @Test(groups = {"companyAccess"}, dependsOnGroups = {"connection"})
    public void companyAccess()
            throws NotConnectedToWebServiceException, IOException, AuthenticationRequired, WebServiceAccessError {
        company = companyService.getCompanyByVirtualHost(COMPANY_VIRTUALHOST);
        Assert.assertNotNull(company);
    }

    @Test(groups = {"userAccess"})
    public void connectToUserWebService() {
        userService.authorizedServerConnection(HOST, LIFERAY_PROTOCOL, PORT, PROXY_PREFIX, WEBSERVICES_PATH, AUTHENTICATION_TOKEN, LOGIN_USER, LOGIN_PASSWORD);
        Assert.assertFalse(userService.isNotConnected());
    }

    @Test(groups = {"userAccess"}, dependsOnMethods = {"connectToUserWebService"})
    public void userAdd() throws NotConnectedToWebServiceException, IOException, AuthenticationRequired, WebServiceAccessError,
            DuplicatedLiferayElement {
        userService.addUser(company, TEST_USER_PASSWORD, TEST_USER, TEST_USER_MAIL, 0l, "", "es_ES", TEST_USER, TEST_USER, TEST_USER, 0, 0, true,
                TEST_USER_BIRTHDAY_DAY, TEST_USER_BIRTHDAY_MONTH, TEST_USER_BIRTHDAY_YEAR, "Miner", new long[0], new long[0], new long[0], new long[0], false);
    }

    @Test(groups = {"userAccess"}, dependsOnGroups = {"companyAccess"}, dependsOnMethods = {"userAdd"})
    public void userAccess() throws NotConnectedToWebServiceException, IOException, AuthenticationRequired, WebServiceAccessError,
            UserDoesNotExistException {
        try {
            user = userService.getUserByEmailAddress(company, TEST_USER_MAIL);
            Assert.assertNotNull(user);
            IUser<Long> user2 = userService.getUserById(user.getUniqueId());
            Assert.assertEquals(user.getUniqueId(), user2.getUniqueId());
        } catch (WebServiceAccessError e) {
            throw new UserDoesNotExistException("User for testing does not exists. Create user for testing: " + TEST_USER_MAIL);
        }
    }

    @Test(groups = {"groupAccess"})
    public void connectToGroupWebService() {
        userGroupService.authorizedServerConnection(HOST, LIFERAY_PROTOCOL, PORT, PROXY_PREFIX, WEBSERVICES_PATH, AUTHENTICATION_TOKEN, LOGIN_USER, LOGIN_PASSWORD);
        Assert.assertFalse(userGroupService.isNotConnected());
    }

    @Test(groups = {"groupAccess"}, dependsOnMethods = {"connectToGroupWebService"})
    public void groupAdd() throws NotConnectedToWebServiceException, IOException, AuthenticationRequired, WebServiceAccessError,
            DuplicatedLiferayElement {
        group = userGroupService.addUserGroup(TEST_GROUP, "");
        Assert.assertNotNull(group);
    }

    @Test(groups = {"groupAccess"}, dependsOnMethods = {"userAccess", "groupAdd"})
    public void groupUserAdd() throws NotConnectedToWebServiceException, IOException, AuthenticationRequired {
        int prevGroups = userGroupService.getUserUserGroups(user).size();
        userGroupService.addUserToGroup(user, group);
        Assert.assertTrue(userGroupService.getUserUserGroups(user).size() == prevGroups + 1);
    }

    @Test(groups = {"roleAccess"})
    public void connectToRoleWebService() {
        roleService.authorizedServerConnection(HOST, LIFERAY_PROTOCOL, PORT, PROXY_PREFIX, WEBSERVICES_PATH, AUTHENTICATION_TOKEN, LOGIN_USER, LOGIN_PASSWORD);
        groupService.authorizedServerConnection(HOST, LIFERAY_PROTOCOL, PORT, PROXY_PREFIX, WEBSERVICES_PATH, AUTHENTICATION_TOKEN, LOGIN_USER, LOGIN_PASSWORD);
        Assert.assertFalse(roleService.isNotConnected());
    }

    @Test(groups = {"roleAccess"}, dependsOnMethods = {"userAccess", "connectToRoleWebService"})
    public void roleAdd() throws NotConnectedToWebServiceException, IOException, AuthenticationRequired, WebServiceAccessError,
            DuplicatedLiferayElement, RoleDoesNotExistsException {
        Map<String, String> titleMap = new HashMap<String, String>();
        Map<String, String> descriptionMap = new HashMap<String, String>();
        titleMap.put("es", "testRole");
        descriptionMap.put("es", "testRoleDescription");
        role = roleService.addRole(TEST_ROLE, RoleType.STANDARD.getLiferayCode(), titleMap, descriptionMap);
        Assert.assertNotNull(role);
        Assert.assertNotNull(roleService.getRole(role.getUniqueId()));
        Assert.assertEquals(role.getUniqueName(), TEST_ROLE);
    }

    @Test(groups = {"groupAccess"}, dependsOnMethods = {"roleAdd"}, expectedExceptions = {DuplicatedLiferayElement.class})
    public void roleDuplicated() throws NotConnectedToWebServiceException, IOException, AuthenticationRequired, WebServiceAccessError,
            DuplicatedLiferayElement {
        Map<String, String> titleMap = new HashMap<String, String>();
        Map<String, String> descriptionMap = new HashMap<String, String>();
        titleMap.put("es", "testRole");
        descriptionMap.put("es", "testRoleDescription");
        role = roleService.addRole(TEST_ROLE, RoleType.STANDARD.getLiferayCode(), titleMap, descriptionMap);
    }

    @Test(groups = {"roleAccess"}, dependsOnMethods = {"userAccess", "connectToRoleWebService"})
    public void roleGroupAdd() throws NotConnectedToWebServiceException, IOException, AuthenticationRequired, WebServiceAccessError,
            DuplicatedLiferayElement {
        Map<String, String> titleMap = new HashMap<String, String>();
        Map<String, String> descriptionMap = new HashMap<String, String>();
        titleMap.put("es", "testRole");
        descriptionMap.put("es", "testRoleDescription");
        groupRole = roleService.addRole(TEST_GROUP_ROLE, RoleType.STANDARD.getLiferayCode(), titleMap, descriptionMap);
        Assert.assertNotNull(groupRole);
    }

    @Test(groups = {"roleAccess"}, dependsOnMethods = {"roleAdd"})
    public void addRoleToUser() throws NotConnectedToWebServiceException, IOException, AuthenticationRequired, WebServiceAccessError,
            RoleDoesNotExistsException {
        Assert.assertNotNull(roleService.getRole(role.getUniqueId()));
        int prevRoles = roleService.getUserRoles(user).size();
        // roleService.addUserRole(user, role);
        roleService.addRoleUser(user, role);
        Assert.assertEquals(prevRoles + 1, roleService.getUserRoles(user).size());
    }

    @Test(groups = {"roleAccess"}, dependsOnMethods = {"roleGroupAdd", "groupUserAdd"})
    public void addRoleToGroup() throws NotConnectedToWebServiceException, IOException, AuthenticationRequired {
        int previousRoles = roleService.getGroupRoles(group).size();
        roleService.addRoleGroup(groupRole, group);
        Assert.assertEquals(previousRoles + 1, roleService.getGroupRoles(group).size());
    }

    @Test(groups = {"organizationAccess"})
    public void connectToOrganizationWebService() {
        organizationService.authorizedServerConnection(HOST, LIFERAY_PROTOCOL, PORT, PROXY_PREFIX, WEBSERVICES_PATH, AUTHENTICATION_TOKEN, LOGIN_USER, LOGIN_PASSWORD);
        Assert.assertFalse(organizationService.isNotConnected());
    }

    @Test(groups = {"organizationAccess"}, dependsOnGroups = {"companyAccess"}, dependsOnMethods = {"connectToOrganizationWebService"})
    public void addOrganization() throws NotConnectedToWebServiceException, IOException, AuthenticationRequired, WebServiceAccessError,
            DuplicatedLiferayElement {
        // Check previous organization.
        int previousOrganizations = organizationService.getOrganizations(company).size();
        // Create two organizations.
        organization1 = organizationService.addOrganization(company, TEST_ORGANIZATION_1);
        organization2 = organizationService.addOrganization(company, TEST_ORGANIZATION_2);
        Assert.assertNotNull(organization1);
        Assert.assertNotNull(organization2);
        // Test the number has increased.
        Assert.assertTrue(organizationService.getOrganizations(company).size() == previousOrganizations + 2);
    }

    @Test(groups = {"organizationAccess"}, dependsOnGroups = {"companyAccess"}, dependsOnMethods = {"addOrganization"})
    public void getOrganizationByName() throws NotConnectedToWebServiceException, IOException, AuthenticationRequired,
            WebServiceAccessError, UserManagementException {
        IGroup<Long> organizationGroup = groupService.getGroup(company.getUniqueId(),
                organization1.getUniqueName() + ServiceAccess.LIFERAY_ORGANIZATION_GROUP_SUFIX);
        Assert.assertNotNull(organizationGroup);

        // Organization id is the previous of the organization group.
        IGroup<Long> organization = organizationService.getOrganization(organizationGroup.getUniqueId() - 1);
        Assert.assertNotNull(organization);

        Assert.assertEquals(organization.getUniqueName(), organization1.getUniqueName());

        //Check by authorization service.
        authorizationService.authorizedServerConnection(HOST, LIFERAY_PROTOCOL, PORT, PROXY_PREFIX, WEBSERVICES_PATH, AUTHENTICATION_TOKEN, LOGIN_USER, LOGIN_PASSWORD);
        IGroup<Long> organizationByAuthorization = authorizationService.getOrganization(company.getUniqueId(), organization1.getUniqueName());
        Assert.assertEquals(organizationByAuthorization.getUniqueName(), organization1.getUniqueName());
    }

    @Test(groups = {"organizationAccess"}, dependsOnGroups = {"companyAccess"}, dependsOnMethods = {"connectToRoleWebService"})
    public void addOrganizationRoles() throws NotConnectedToWebServiceException, IOException, AuthenticationRequired,
            WebServiceAccessError, DuplicatedLiferayElement {
        // Create role.
        Map<String, String> titleMap = new HashMap<String, String>();
        Map<String, String> descriptionMap = new HashMap<String, String>();
        titleMap.put("es", "testTitle");
        descriptionMap.put("es", "testRoleDescription");
        organizationRole = roleService.addRole(TEST_ORGANIZATION_ROLE, RoleType.ORGANIZATION.getLiferayCode(), titleMap, descriptionMap);
        Assert.assertNotNull(organizationRole);
    }

    @Test(groups = {"organizationAccess"}, dependsOnMethods = {"addOrganizationRoles", "addOrganization"})
    public void assignRoleToOrganization1()
            throws NotConnectedToWebServiceException, IOException, AuthenticationRequired, WebServiceAccessError {
        roleService.addRoleOrganization(organizationRole, organization1);
    }

    @Test(groups = {"organizationAccess"}, dependsOnMethods = {"addOrganization", "userAccess"})
    public void assignUsersToOrganization1()
            throws NotConnectedToWebServiceException, IOException, AuthenticationRequired {
        organizationService.addUserToOrganization(user, organization1);
        Assert.assertEquals(1, organizationService.getUserOrganizationGroups(user).size());
        organizationService.addUserToOrganization(user, organization2);
        Assert.assertEquals(2, organizationService.getUserOrganizationGroups(user).size());
    }

    @Test(groups = {"organizationAccess"}, dependsOnMethods = {"assignRoleToOrganization1", "assignUsersToOrganization1", "addOrganization"})
    public void assignOrganizationRoleToUser()
            throws NotConnectedToWebServiceException, IOException, AuthenticationRequired, WebServiceAccessError {
        roleService.addUserOrganizationRole(user, organization1, organizationRole);
    }

    @Test(groups = {"organizationAccess"}, dependsOnGroups = {"companyAccess"}, dependsOnMethods = {"connectToOrganizationWebService",
            "assignOrganizationRoleToUser"})
    public void getUserOrganizationRoles()
            throws NotConnectedToWebServiceException, IOException, AuthenticationRequired, WebServiceAccessError {
        // Only has roles in organization 1.
        Assert.assertEquals(1, roleService.getUserRolesOfOrganization(user, organization1).size());
        Assert.assertEquals(0, roleService.getUserRolesOfOrganization(user, organization2).size());
    }

    @Test(groups = {"organizationAccess"}, dependsOnGroups = {"companyAccess"}, dependsOnMethods = {"connectToOrganizationWebService",
            "assignOrganizationRoleToUser"})
    public void getUsersWithRoles()
            throws IOException, NotConnectedToWebServiceException, AuthenticationRequired, WebServiceAccessError {
        Assert.assertEquals(1, roleService.getUsers(organizationRole, organization1).size());
    }

    @Test(groups = {"pool"}, dependsOnGroups = {"roleAccess", "userAccess"})
    public void userRoleAccessPool() throws NotConnectedToWebServiceException, IOException, AuthenticationRequired {
        // Make a connection for populating the pool.
        Set<IRole<Long>> roles = roleService.getUserRoles(user);
        // Checks the use of the pool. Disconnect the web service.
        roleService.disconnect();
        // I can still get the RoleSoap (is stored previously in the pool)
        roles = roleService.getUserRoles(user);
        Assert.assertNotNull(roles);
        Assert.assertFalse(roles.isEmpty());
        // Connect again.
        connectToRoleWebService();
    }

    @Test(groups = {"pool"}, dependsOnMethods = {"addRoleToGroup"})
    public void groupRoleAccessPool() throws NotConnectedToWebServiceException, IOException, AuthenticationRequired {
        // Make a connection for populating the pool.
        Set<IRole<Long>> roles = roleService.getGroupRoles(group);
        // Checks the use of the pool. Disconnect the web service.
        roleService.disconnect();
        // I can still get the Role (is stored previously in the pool)
        roles = roleService.getGroupRoles(group);
        Assert.assertNotNull(roles);
        Assert.assertFalse(roles.isEmpty());
        // Connect again.
        connectToRoleWebService();
    }

    @Test(alwaysRun = true, groups = {"clearData"}, dependsOnGroups = {"groupAccess", "roleAccess", "userAccess", "pool"}, dependsOnMethods = {
            "groupAdd"})
    public void groupRoleDelete() throws NotConnectedToWebServiceException, IOException, AuthenticationRequired {
        // Group Role
        int prevRoles = roleService.getGroupRoles(group).size();
        roleService.deleteRole(groupRole);
        Assert.assertEquals(prevRoles - 1, roleService.getGroupRoles(group).size());
    }

    @Test(alwaysRun = true, groups = {"clearData"}, dependsOnGroups = {"roleAccess", "userAccess", "groupAccess", "pool"}, dependsOnMethods = {"roleAdd",
            "groupDelete", "getUserOrganizationRoles", "getUsersWithRoles"})
    public void userRoleDelete() throws NotConnectedToWebServiceException, IOException, AuthenticationRequired, WebServiceAccessError,
            RoleNotDeletedException {
        // User Role.
        int prevRoles = roleService.getUserRoles(user).size();
        roleService.deleteRole(role);
        Assert.assertEquals(prevRoles - 1, roleService.getUserRoles(user).size());
    }

    @Test(alwaysRun = true, groups = {"clearData"}, dependsOnGroups = {"organizationAccess", "pool"}, dependsOnMethods = {"userRoleDelete",
            "addOrganization"})
    public void organizationDeleteRoles()
            throws NotConnectedToWebServiceException, IOException, AuthenticationRequired, WebServiceAccessError {
        // Organization Role
        int prevRoles = roleService.getOrganizationRoles(organization1).size();
        roleService.deleteRole(organizationRole);
        Assert.assertEquals(prevRoles - 1, roleService.getOrganizationRoles(organization1).size());
    }

    @Test(alwaysRun = true, groups = {"clearData"}, dependsOnGroups = {"groupAccess", "userAccess"}, dependsOnMethods = {"groupAdd", "groupRoleDelete"})
    public void groupDelete() throws NotConnectedToWebServiceException, IOException, AuthenticationRequired {
        int prevGroups = userGroupService.getUserUserGroups(user).size();
        userGroupService.deleteUserFromUserGroup(user, group);
        userGroupService.deleteUserGroup(group);
        Assert.assertEquals(prevGroups - 1, userGroupService.getUserUserGroups(user).size());
    }

    @Test(alwaysRun = true, groups = {"clearData"}, dependsOnGroups = {"groupAccess", "userAccess"}, dependsOnMethods = {"addOrganization",
            "organizationDeleteRoles", "getUserOrganizationRoles", "getUsersWithRoles"})
    public void unsetUserFromOrganization() throws NotConnectedToWebServiceException, IOException, AuthenticationRequired {
        int usersInOrg1 = organizationService.getOrganizationUsers(organization1).size();
        int usersInOrg2 = organizationService.getOrganizationUsers(organization2).size();
        organizationService.removeUserFromOrganization(user, organization1);
        organizationService.removeUserFromOrganization(user, organization2);
        Assert.assertEquals(usersInOrg1 - 1, organizationService.getOrganizationUsers(organization1).size());
        Assert.assertEquals(usersInOrg2 - 1, organizationService.getOrganizationUsers(organization2).size());
    }

    @Test(alwaysRun = true, groups = {"clearData"}, dependsOnGroups = {"userAccess", "groupAccess", "pool"}, dependsOnMethods = {"userAccess",
            "groupDelete", "unsetUserFromOrganization"}, expectedExceptions = UserDoesNotExistException.class)
    public void userDelete() throws NotConnectedToWebServiceException, UserDoesNotExistException, IOException, AuthenticationRequired,
            WebServiceAccessError {
        userService.deleteUser(user);
        userService.getUserById(user.getUniqueId());
    }

    @Test(alwaysRun = true, groups = {"clearData"}, dependsOnGroups = {"organizationAccess"}, dependsOnMethods = {"unsetUserFromOrganization"})
    public void organizationDelete()
            throws NotConnectedToWebServiceException, IOException, AuthenticationRequired, OrganizationNotDeletedException {
        int prevOrgs = organizationService.getOrganizations(company).size();
        organizationService.deleteOrganization(company, organization1);
        organizationService.deleteOrganization(company, organization2);
        Assert.assertEquals(prevOrgs - 2, organizationService.getOrganizations(company).size());
    }

    @AfterClass
    public void closeConnections() {
        organizationService.disconnect();
        userService.disconnect();
        roleService.disconnect();
        userGroupService.disconnect();
        groupService.disconnect();
        companyService.disconnect();
    }
}
