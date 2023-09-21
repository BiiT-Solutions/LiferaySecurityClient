package com.biit.liferay.access;

import com.biit.liferay.access.exceptions.DuplicatedLiferayElement;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.RoleNotDeletedException;
import com.biit.liferay.access.exceptions.WebServiceAccessError;
import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.entity.IRole;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.security.exceptions.AuthenticationRequired;
import com.biit.usermanager.security.exceptions.RoleDoesNotExistsException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Role;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IRoleService extends IServiceAccess {

    /**
     * Creates a new RoleSoap on Liferay. For testing use only.
     *
     * @param name name of the new RoleSoap.
     * @return
     * @throws NotConnectedToWebServiceException
     * @throws IOException
     * @throws AuthenticationRequired
     * @throws WebServiceAccessError
     * @throws DuplicatedLiferayElement
     */
    IRole<Long> addRole(String name, int type, Map<String, String> titleMap, Map<String, String> descriptionMap)
            throws NotConnectedToWebServiceException, IOException, AuthenticationRequired,
            WebServiceAccessError, DuplicatedLiferayElement;

    IRole<Long> addRole(Role role) throws NotConnectedToWebServiceException, IOException,
            AuthenticationRequired, WebServiceAccessError, DuplicatedLiferayElement;

    /**
     * Add a role to a group. For testing only.
     *
     * @param role
     * @param userGroup
     * @throws NotConnectedToWebServiceException
     * @throws IOException
     * @throws AuthenticationRequired
     */
    void addRoleGroup(IRole<Long> role, IGroup<Long> userGroup)
            throws NotConnectedToWebServiceException, IOException, AuthenticationRequired;

    /**
     * Add a role to a list of groups. For testing only.
     *
     * @param role
     * @param userGroups
     * @throws NotConnectedToWebServiceException
     * @throws IOException
     * @throws AuthenticationRequired
     */
    void addRoleGroups(IRole<Long> role, List<IGroup<Long>> userGroups)
            throws NotConnectedToWebServiceException, IOException, AuthenticationRequired;

    void addRoleOrganization(IRole<Long> role, IGroup<Long> organization) throws
            NotConnectedToWebServiceException, IOException, AuthenticationRequired, WebServiceAccessError;

    /**
     * Add a role to a list of organizations. For testing only.
     *
     * @param role
     * @param organizations
     * @throws NotConnectedToWebServiceException
     * @throws IOException
     * @throws AuthenticationRequired
     * @throws WebServiceAccessError
     */
    void addRoleOrganizations(IRole<Long> role, List<IGroup<Long>> organizations)
            throws NotConnectedToWebServiceException, IOException, AuthenticationRequired,
            WebServiceAccessError;

    void addRoleUser(IUser<Long> user, IRole<Long> role)
            throws NotConnectedToWebServiceException, IOException, AuthenticationRequired;

    /**
     * Add a list of users to a role
     *
     * @param users
     * @param role
     * @throws NotConnectedToWebServiceException
     * @throws IOException
     * @throws AuthenticationRequired
     */
    void addRoleUsers(List<IUser<Long>> users, IRole<Long> role)
            throws NotConnectedToWebServiceException, IOException, AuthenticationRequired;

    /**
     * Add a role from a user group to a user. For testing use only.
     *
     * @param user
     * @param role
     * @throws NotConnectedToWebServiceException
     * @throws IOException
     * @throws AuthenticationRequired
     */
    void addUserGroupRole(IUser<Long> user, IGroup<Long> userGroup, IRole<Long> role)
            throws NotConnectedToWebServiceException, IOException, AuthenticationRequired;

    /**
     * Add a list of roles from a user group to a user. For testing use only.
     *
     * @param user
     * @param roles
     * @throws NotConnectedToWebServiceException
     * @throws IOException
     * @throws AuthenticationRequired
     */
    void addUserGroupRoles(IUser<Long> user, IGroup<Long> userGroup, Set<IRole<Long>> roles)
            throws NotConnectedToWebServiceException, IOException, AuthenticationRequired;

    /**
     * Add a list of roles to a user. For testing use only.
     *
     * @param userId
     * @param roles
     * @throws NotConnectedToWebServiceException
     * @throws IOException
     * @throws AuthenticationRequired
     */
    void addUserGroupRoles(Long userId, Long groupId, Set<IRole<Long>> roles)
            throws NotConnectedToWebServiceException, IOException, AuthenticationRequired;

    /**
     * Add a role from a organization to a user. For testing use only.
     *
     * @param user
     * @param role
     * @throws NotConnectedToWebServiceException
     * @throws IOException
     * @throws AuthenticationRequired
     * @throws WebServiceAccessError
     */
    void addUserOrganizationRole(IUser<Long> user, IGroup<Long> organization, IRole<Long> role)
            throws NotConnectedToWebServiceException, IOException, AuthenticationRequired,
            WebServiceAccessError;

    /**
     * Add a list of roles from a organization to a user. For testing use only.
     *
     * @param user
     * @param roles
     * @throws NotConnectedToWebServiceException
     * @throws IOException
     * @throws AuthenticationRequired
     * @throws WebServiceAccessError
     */
    void addUserOrganizationRoles(IUser<Long> user, IGroup<Long> organization, Set<IRole<Long>> roles)
            throws NotConnectedToWebServiceException, IOException, AuthenticationRequired,
            WebServiceAccessError;

    /**
     * Add a role to a user. For testing use only.
     *
     * @param user
     * @param role
     * @throws NotConnectedToWebServiceException
     * @throws IOException
     * @throws AuthenticationRequired
     */
    void addUserRole(IUser<Long> user, IRole<Long> role)
            throws NotConnectedToWebServiceException, IOException, AuthenticationRequired;

    /**
     * Add a list of roles to a user. For testing use only.
     *
     * @param user
     * @param roles
     * @throws NotConnectedToWebServiceException
     * @throws IOException
     * @throws AuthenticationRequired
     */
    void addUserRoles(IUser<Long> user, List<IRole<Long>> roles)
            throws NotConnectedToWebServiceException, IOException, AuthenticationRequired;

    void authorizedServerConnection(String address, String protocol, int port, String proxyPrefix, String webservicesPath,
                                    String authenticationToken, String loginUser, String password);

    Set<IRole<Long>> decodeListFromJson(String json, Class<Role> objectClass)
            throws JsonParseException, JsonMappingException, IOException;

    /**
     * Removes a RoleSoap from Liferay portal. For testing use only.
     *
     * @param role
     * @throws NotConnectedToWebServiceException
     * @throws IOException
     * @throws AuthenticationRequired
     */
    void deleteRole(IRole<Long> role)
            throws NotConnectedToWebServiceException, IOException, AuthenticationRequired;

    /**
     * Removes the RoleSoap from the user. For testing use only.
     *
     * @param role
     * @param user
     * @throws NotConnectedToWebServiceException
     * @throws IOException
     * @throws AuthenticationRequired
     */
    void deleteRole(IUser<Long> user, IRole<Long> role) throws NotConnectedToWebServiceException,
            IOException, AuthenticationRequired, RoleNotDeletedException;

    /**
     * Get a list of roles of a group.
     *
     * @param group
     * @return
     * @throws NotConnectedToWebServiceException
     * @throws IOException
     * @throws AuthenticationRequired
     */
    Set<IRole<Long>> getGroupRoles(IGroup<Long> group)
            throws NotConnectedToWebServiceException, IOException, AuthenticationRequired;

    /**
     * Get a list of roles from an organization.
     *
     * @param organization
     * @return
     * @throws NotConnectedToWebServiceException
     * @throws IOException
     * @throws AuthenticationRequired
     * @throws WebServiceAccessError
     */
    Set<IRole<Long>> getOrganizationRoles(IGroup<Long> organization) throws NotConnectedToWebServiceException,
            IOException, AuthenticationRequired, WebServiceAccessError;

    /**
     * Gets the Group Id related to an organization.
     *
     * @param organization
     * @return
     * @throws NotConnectedToWebServiceException
     * @throws IOException
     * @throws AuthenticationRequired
     * @throws WebServiceAccessError
     */
    long getOrganizationGroupId(IGroup<Long> organization) throws
            NotConnectedToWebServiceException, IOException, AuthenticationRequired, WebServiceAccessError;

    /**
     * Creates a new role on Liferay. For testing use only.
     *
     * @param roleId
     * @return
     * @throws NotConnectedToWebServiceException
     * @throws IOException
     * @throws AuthenticationRequired
     * @throws WebServiceAccessError
     * @throws RoleDoesNotExistsException
     */
    IRole<Long> getRole(long roleId) throws NotConnectedToWebServiceException, IOException,
            AuthenticationRequired, WebServiceAccessError, RoleDoesNotExistsException;

    /**
     * Creates a new RoleSoap on Liferay. For testing use only.
     *
     * @param roleName name of the new RoleSoap.
     * @return
     * @throws NotConnectedToWebServiceException
     * @throws IOException
     * @throws AuthenticationRequired
     * @throws WebServiceAccessError
     * @throws RoleDoesNotExistsException
     */
    IRole<Long> getRole(String roleName, long companyId) throws NotConnectedToWebServiceException,
            IOException, AuthenticationRequired, WebServiceAccessError, RoleDoesNotExistsException;

    /**
     * Get the list of roles for a user.
     *
     * @param user
     * @return
     * @throws NotConnectedToWebServiceException
     * @throws IOException
     * @throws AuthenticationRequired
     */
    Set<IRole<Long>> getUserRoles(IUser<Long> user)
            throws NotConnectedToWebServiceException, IOException, AuthenticationRequired;

    /**
     * Gets the roles of a user in a group.
     *
     * @param user
     * @param group
     * @return
     * @throws NotConnectedToWebServiceException
     * @throws IOException
     * @throws AuthenticationRequired
     */
    Set<IRole<Long>> getUserRolesOfGroup(IUser<Long> user, Group group)
            throws NotConnectedToWebServiceException, IOException, AuthenticationRequired;

    /**
     * Gets the roles of a user in a group.
     *
     * @param userId
     * @param groupId
     * @return
     * @throws NotConnectedToWebServiceException
     * @throws IOException
     * @throws AuthenticationRequired
     */
    Set<IRole<Long>> getUserRolesOfGroup(Long userId, Long groupId)
            throws NotConnectedToWebServiceException, IOException, AuthenticationRequired;

    /**
     * Gets all roles of a user for an organization. Needs the use of GroupService
     *
     * @param user
     * @param organization
     * @return
     * @throws AuthenticationRequired
     * @throws IOException
     * @throws NotConnectedToWebServiceException
     * @throws WebServiceAccessError
     */
    Set<IRole<Long>> getUserRolesOfOrganization(IUser<Long> user, IGroup<Long> organization)
            throws NotConnectedToWebServiceException, IOException, AuthenticationRequired,
            WebServiceAccessError;

    /**
     * Gets all users that have a specific role in an organization.
     *
     * @throws WebServiceAccessError
     * @throws AuthenticationRequired
     * @throws NotConnectedToWebServiceException
     * @throws IOException
     */
    Set<IUser<Long>> getUsers(IRole<Long> role, IGroup<Long> organization) throws IOException,
            NotConnectedToWebServiceException, AuthenticationRequired, WebServiceAccessError;

    /**
     * Unset a role from a group but does not deletes it.
     *
     * @param role
     * @param groups
     * @throws AuthenticationRequired
     * @throws NotConnectedToWebServiceException
     * @throws IOException
     */
    void unsetRoleFromGroups(IRole<Long> role, List<Group> groups)
            throws IOException, NotConnectedToWebServiceException, AuthenticationRequired;

    /**
     * Unset a role from a group but does not deletes it.
     *
     * @param role
     * @param organizations
     * @throws AuthenticationRequired
     * @throws NotConnectedToWebServiceException
     * @throws IOException
     * @throws WebServiceAccessError
     */
    void unsetRoleFromOrganization(IRole<Long> role, List<IGroup<Long>> organizations) throws
            IOException, NotConnectedToWebServiceException, AuthenticationRequired, WebServiceAccessError;

}
