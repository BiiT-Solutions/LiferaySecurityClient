package com.biit.liferay.access;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import com.biit.liferay.access.exceptions.DuplicatedLiferayElement;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.RoleNotDeletedException;
import com.biit.liferay.access.exceptions.WebServiceAccessError;
import com.biit.liferay.log.LiferayClientLogger;
import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.entity.IRole;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.entity.pool.RolePool;
import com.biit.usermanager.security.exceptions.AuthenticationRequired;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.Role;

/**
 * This class allows to manage roles from Liferay portal.
 */
public class RoleService extends ServiceAccess<IRole<Long>, Role> {
	private GroupService groupService;
	private OrganizationService organizationService;
	// Relationship between organization and groups.
	private HashMap<Long, Long> organizationGroups;
	private RolePool<Long, Long, Long> rolePool;

	public RoleService() {
		organizationGroups = new HashMap<Long, Long>();
		rolePool = new RolePool<Long, Long, Long>();
	}

	/**
	 * Creates a new RoleSoap on Liferay. For testing use only.
	 * 
	 * @param name
	 *            name of the new RoleSoap.
	 * @return
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws AuthenticationRequired
	 * @throws WebServiceAccessError
	 * @throws DuplicatedLiferayElement
	 */
	public IRole<Long> addRole(String name, int type, Map<String, String> titleMap, Map<String, String> descriptionMap)
			throws NotConnectedToWebServiceException, ClientProtocolException, IOException, AuthenticationRequired, WebServiceAccessError,
			DuplicatedLiferayElement {
		if (name != null && name.length() > 0) {
			checkConnection();

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("name", name));
			params.add(new BasicNameValuePair("titleMap", encodeMapToJson(titleMap)));
			params.add(new BasicNameValuePair("descriptionMap", encodeMapToJson(descriptionMap)));
			params.add(new BasicNameValuePair("type", Integer.toString(type)));

			String result = getHttpResponse("role/add-role", params);
			IRole<Long> role = null;
			if (result != null) {
				// Check some errors
				if (result.contains("DuplicateRoleException")) {
					throw new DuplicatedLiferayElement("Already exists a role with name '" + name + "'.");
				}

				// A Simple JSON Response Read
				role = decodeFromJson(result, Role.class);
				LiferayClientLogger.info(this.getClass().getName(), "Role '" + role.getUniqueName() + "' added.");
				return role;
			}
		}
		return null;
	}

	/**
	 * Add a role to a group. For testing only.
	 * 
	 * @param role
	 * @param userGroup
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws AuthenticationRequired
	 */
	public void addRoleGroup(IRole<Long> role, IGroup<Long> userGroup) throws NotConnectedToWebServiceException, ClientProtocolException, IOException,
			AuthenticationRequired {
		List<IGroup<Long>> groups = new ArrayList<IGroup<Long>>();
		groups.add(userGroup);
		addRoleGroups(role, groups);
	}

	/**
	 * Add a role to a list of groups. For testing only.
	 * 
	 * @param role
	 * @param userGroups
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws AuthenticationRequired
	 */
	public void addRoleGroups(IRole<Long> role, List<IGroup<Long>> userGroups) throws NotConnectedToWebServiceException, ClientProtocolException, IOException,
			AuthenticationRequired {
		if (userGroups != null && role != null && userGroups.size() > 0) {
			checkConnection();
			String groupIds = "";
			if (userGroups.size() > 0) {
				groupIds = "[";
			}
			for (int i = 0; i < userGroups.size(); i++) {
				groupIds += userGroups.get(i).getId();
				if (i < userGroups.size() - 1) {
					groupIds += ",";
				}
			}
			if (groupIds.length() > 0) {
				groupIds += "]";
			}

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("roleId", Long.toString(role.getId())));
			params.add(new BasicNameValuePair("groupIds", groupIds));

			getHttpResponse("group/add-role-groups", params);
			LiferayClientLogger.info(this.getClass().getName(), "Groups ids " + groupIds + " added to role '" + role.getUniqueName() + "'");
			for (IGroup<Long> group : userGroups) {
				rolePool.addGroupRole(group, role);
			}
		}
	}

	public void addRoleOrganization(IRole<Long> role, IGroup<Long> organization) throws ClientProtocolException, NotConnectedToWebServiceException,
			IOException, AuthenticationRequired, WebServiceAccessError {
		List<IGroup<Long>> organizations = new ArrayList<IGroup<Long>>();
		organizations.add(organization);
		addRoleOrganizations(role, organizations);
	}

	/**
	 * Add a role to a list of organizations. For testing only.
	 * 
	 * @param role
	 * @param userGroups
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws AuthenticationRequired
	 * @throws WebServiceAccessError
	 */
	public void addRoleOrganizations(IRole<Long> role, List<IGroup<Long>> organizations) throws NotConnectedToWebServiceException, ClientProtocolException,
			IOException, AuthenticationRequired, WebServiceAccessError {
		if (organizations != null && role != null && organizations.size() > 0) {
			checkConnection();
			String groupIds = "";
			if (organizations.size() > 0) {
				groupIds = "[";
			}
			for (int i = 0; i < organizations.size(); i++) {
				groupIds += getOrganizationGroupId(organizations.get(i));
				if (i < organizations.size() - 1) {
					groupIds += ",";
				}
			}
			if (groupIds.length() > 0) {
				groupIds += "]";
			}

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("roleId", Long.toString(role.getId())));
			params.add(new BasicNameValuePair("groupIds", groupIds));

			getHttpResponse("group/add-role-groups", params);
			LiferayClientLogger.info(this.getClass().getName(), "Groups ids " + groupIds + " added to role '" + role.getUniqueName() + "'");
		}
	}

	public void addRoleUser(IUser<Long> user, IRole<Long> role) throws NotConnectedToWebServiceException, ClientProtocolException, IOException,
			AuthenticationRequired {
		List<IUser<Long>> users = new ArrayList<IUser<Long>>();
		users.add(user);
		addRoleUsers(users, role);
	}

	/**
	 * Add a list of users to a role
	 * 
	 * @param users
	 * @param role
	 * @throws NotConnectedToWebServiceException
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws AuthenticationRequired
	 */
	public void addRoleUsers(List<IUser<Long>> users, IRole<Long> role) throws NotConnectedToWebServiceException, ClientProtocolException, IOException,
			AuthenticationRequired {
		if (users != null && users.size() > 0) {
			checkConnection();

			String userIds = "";
			if (users.size() > 0) {
				userIds = "[";
			}
			for (int i = 0; i < users.size(); i++) {
				userIds += users.get(i).getId();
				if (i < users.size() - 1) {
					userIds += ",";
				}
			}
			if (userIds.length() > 0) {
				userIds += "]";
			}

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("roleId", Long.toString(role.getId())));
			params.add(new BasicNameValuePair("userIds", userIds));

			getHttpResponse("user/add-role-users", params);

			for (IUser<Long> user : users) {
				rolePool.addUserRole(user, role);
			}

			LiferayClientLogger.info(this.getClass().getName(), "IUser<Long> ids " + userIds + " added to role '" + role.getUniqueName() + "'");
		}
	}

	/**
	 * Add a role from a user group to a user. For testing use only.
	 * 
	 * @param user
	 * @param role
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws AuthenticationRequired
	 */
	public void addUserGroupRole(IUser<Long> user, IGroup<Long> userGroup, IRole<Long> role) throws NotConnectedToWebServiceException, ClientProtocolException,
			IOException, AuthenticationRequired {
		Set<IRole<Long>> roles = new HashSet<IRole<Long>>();
		roles.add(role);
		addUserGroupRoles(user, userGroup, roles);
	}

	/**
	 * Add a list of roles from a user group to a user. For testing use only.
	 * 
	 * @param user
	 * @param roles
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws AuthenticationRequired
	 */
	public void addUserGroupRoles(IUser<Long> user, IGroup<Long> userGroup, Set<IRole<Long>> roles) throws NotConnectedToWebServiceException,
			ClientProtocolException, IOException, AuthenticationRequired {
		if (user != null && userGroup != null && roles != null && !roles.isEmpty()) {
			addUserGroupRoles(user.getId(), userGroup.getId(), roles);
		}
	}

	/**
	 * Add a list of roles to a user. For testing use only.
	 * 
	 * @param user
	 * @param roles
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws AuthenticationRequired
	 */
	public void addUserGroupRoles(Long userId, Long groupId, Set<IRole<Long>> roles) throws NotConnectedToWebServiceException, ClientProtocolException,
			IOException, AuthenticationRequired {
		if (userId != null && groupId != null && roles != null && !roles.isEmpty()) {
			checkConnection();

			String rolesIds = "";
			if (roles.size() > 0) {
				rolesIds = "[";
			}
			Iterator<IRole<Long>> iterator = roles.iterator();
			while (iterator != null && iterator.hasNext()) {
				rolesIds += iterator.next().getId();
				if (iterator.hasNext()) {
					rolesIds += ",";
				}

			}
			if (rolesIds.length() > 0) {
				rolesIds += "]";
			}

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userId", userId + ""));
			params.add(new BasicNameValuePair("groupId", groupId + ""));
			params.add(new BasicNameValuePair("roleIds", rolesIds));

			getHttpResponse("usergrouprole/add-user-group-roles", params);

			rolePool.addUserRolesOfGroup(userId, groupId, roles);

			LiferayClientLogger.info(this.getClass().getName(), "Roles ids " + rolesIds + " added to group '" + groupId + "' and user '" + userId + "'");

		}
	}

	/**
	 * Add a role from a organization to a user. For testing use only.
	 * 
	 * @param user
	 * @param role
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws AuthenticationRequired
	 * @throws WebServiceAccessError
	 */
	public void addUserOrganizationRole(IUser<Long> user, IGroup<Long> organization, IRole<Long> role) throws NotConnectedToWebServiceException,
			ClientProtocolException, IOException, AuthenticationRequired, WebServiceAccessError {
		if (user != null && organization != null && role != null) {
			Set<IRole<Long>> roles = new HashSet<IRole<Long>>();
			roles.add(role);
			addUserOrganizationRoles(user, organization, roles);
		}
	}

	/**
	 * Add a list of roles from a organization to a user. For testing use only.
	 * 
	 * @param user
	 * @param roles
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws AuthenticationRequired
	 * @throws WebServiceAccessError
	 */
	public void addUserOrganizationRoles(IUser<Long> user, IGroup<Long> organization, Set<IRole<Long>> roles) throws NotConnectedToWebServiceException,
			ClientProtocolException, IOException, AuthenticationRequired, WebServiceAccessError {
		if (user != null && organization != null && roles != null && !roles.isEmpty()) {
			long organizationGroupId = getOrganizationGroupId(organization);
			addUserGroupRoles(user.getId(), organizationGroupId, roles);
		}
	}

	/**
	 * Add a role to a user. For testing use only.
	 * 
	 * @param user
	 * @param role
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws AuthenticationRequired
	 */
	public void addUserRole(IUser<Long> user, IRole<Long> role) throws NotConnectedToWebServiceException, ClientProtocolException, IOException,
			AuthenticationRequired {
		List<IRole<Long>> roles = new ArrayList<IRole<Long>>();
		roles.add(role);
		addUserRoles(user, roles);
	}

	/**
	 * Add a list of roles to a user. For testing use only.
	 * 
	 * @param user
	 * @param roles
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws AuthenticationRequired
	 */
	public void addUserRoles(IUser<Long> user, List<IRole<Long>> roles) throws NotConnectedToWebServiceException, ClientProtocolException, IOException,
			AuthenticationRequired {
		if (user != null && roles != null && roles.size() > 0) {
			checkConnection();

			String rolesIds = "";
			if (roles.size() > 0) {
				rolesIds = "[";
			}
			for (int i = 0; i < roles.size(); i++) {
				rolesIds += roles.get(i).getId();
				if (i < roles.size() - 1) {
					rolesIds += ",";
				}
			}
			if (rolesIds.length() > 0) {
				rolesIds += "]";
			}

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userId", Long.toString(user.getId())));
			params.add(new BasicNameValuePair("roleIds", rolesIds));

			getHttpResponse("role/add-user-roles", params);

			for (IRole<Long> role : roles) {
				rolePool.addUserRole(user, role);
			}

			LiferayClientLogger.info(this.getClass().getName(), "Roles ids " + rolesIds + " added to user '" + user.getUniqueName() + "'");
		}
	}

	@Override
	public void authorizedServerConnection(String address, String protocol, int port, String webservicesPath, String authenticationToken, String loginUser,
			String password) {
		// Standard behavior.
		super.authorizedServerConnection(address, protocol, port, webservicesPath, authenticationToken, loginUser, password);
		// Disconnect previous connections.
		try {
			groupService.disconnect();
			organizationService.disconnect();
		} catch (Exception e) {

		}
		// Some user information is in the contact object.
		groupService = new GroupService();
		groupService.authorizedServerConnection(address, protocol, port, webservicesPath, authenticationToken, loginUser, password);

		organizationService = new OrganizationService();
		organizationService.authorizedServerConnection(address, protocol, port, webservicesPath, authenticationToken, loginUser, password);
	}

	@Override
	public Set<IRole<Long>> decodeListFromJson(String json, Class<Role> objectClass) throws JsonParseException, JsonMappingException, IOException {
		Set<IRole<Long>> myObjects = new ObjectMapper().readValue(json, new TypeReference<Set<Role>>() {
		});

		return myObjects;
	}

	/**
	 * Removes a RoleSoap from Liferay portal. For testing use only.
	 * 
	 * @param role
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws AuthenticationRequired
	 */
	public void deleteRole(IRole<Long> role) throws NotConnectedToWebServiceException, ClientProtocolException, IOException, AuthenticationRequired {
		if (role != null) {
			checkConnection();

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("roleId", role.getId() + ""));

			getHttpResponse("role/delete-role", params);

			rolePool.removeRole(role);
			LiferayClientLogger.info(this.getClass().getName(), "IRole<Long> '" + role.getUniqueName() + "' deleted.");

		}
	}

	/**
	 * Removes the RoleSoap from the user. For testing use only.
	 * 
	 * @param role
	 * @param user
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws AuthenticationRequired
	 */
	public void deleteRole(IUser<Long> user, IRole<Long> role) throws NotConnectedToWebServiceException, ClientProtocolException, IOException,
			AuthenticationRequired, RoleNotDeletedException {
		checkConnection();

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userId", user.getId() + ""));
		params.add(new BasicNameValuePair("roleId", role.getId() + ""));

		String result = getHttpResponse("user/delete-role-user", params);

		if (result == null || result.length() < 3) {
			rolePool.removeUserRole(user, role);
			LiferayClientLogger.info(this.getClass().getName(), "IRole<Long> '" + role.getUniqueName() + "' of user '" + user.getUniqueName() + "' deleted.");
		} else {
			throw new RoleNotDeletedException("IRole<Long> '" + role.getUniqueName() + "' (id:" + role.getId() + ") not deleted correctly. ");
		}
	}

	@Override
	public void disconnect() {
		super.disconnect();
		groupService.disconnect();
		organizationService.disconnect();
	}

	/**
	 * Get a list of roles of a group.
	 * 
	 * @param group
	 * @return
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws AuthenticationRequired
	 */
	private Set<IRole<Long>> getGroupRoles(Long groupId) throws NotConnectedToWebServiceException, ClientProtocolException, IOException, AuthenticationRequired {
		Set<IRole<Long>> roles = new HashSet<IRole<Long>>();
		if (groupId != null) {
			Set<IRole<Long>> groupRoles = rolePool.getGroupRoles(groupId);
			if (groupRoles != null) {
				return groupRoles;
			}
			checkConnection();

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("groupId", groupId + ""));

			String result = getHttpResponse("role/get-group-roles", params);

			if (result != null) {
				// A Simple JSON Response Read
				roles = decodeListFromJson(result, Role.class);
				rolePool.addGroupRoles(groupId, roles);
				return roles;
			}

			return null;
		}
		return roles;
	}

	/**
	 * Get a list of roles of a group.
	 * 
	 * @param group
	 * @return
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws AuthenticationRequired
	 */
	public Set<IRole<Long>> getGroupRoles(IGroup<Long> group) throws NotConnectedToWebServiceException, ClientProtocolException, IOException,
			AuthenticationRequired {
		Set<IRole<Long>> roles = new HashSet<IRole<Long>>();
		if (group != null) {
			return getGroupRoles(group.getId());
		}
		return roles;
	}

	/**
	 * Get a list of roles from an organization.
	 * 
	 * @param group
	 * @return
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws AuthenticationRequired
	 * @throws WebServiceAccessError
	 */
	public Set<IRole<Long>> getOrganizationRoles(IGroup<Long> organization) throws NotConnectedToWebServiceException, ClientProtocolException, IOException,
			AuthenticationRequired, WebServiceAccessError {
		Set<IRole<Long>> roles = new HashSet<IRole<Long>>();
		if (organization != null) {
			return getGroupRoles(getOrganizationGroupId(organization));
		}
		return roles;
	}

	/**
	 * Gets the Group Id related to an organization.
	 * 
	 * @param organization
	 * @return
	 * @throws ClientProtocolException
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws AuthenticationRequired
	 * @throws WebServiceAccessError
	 */
	public long getOrganizationGroupId(IGroup<Long> organization) throws ClientProtocolException, NotConnectedToWebServiceException, IOException,
			AuthenticationRequired, WebServiceAccessError {

		if (organizationGroups.get(organization.getId()) != null) {
			return organizationGroups.get(organization.getId());
		}

		try {
			IGroup<Long> group = groupService.getGroup(((Organization) organization).getCompanyId(), organization.getUniqueName()
					+ LIFERAY_ORGANIZATION_GROUP_SUFIX);
			if (group != null) {
				organizationGroups.put(organization.getId(), group.getId());
				return group.getId();
			}
		} catch (AuthenticationRequired e) {
			throw new AuthenticationRequired("Cannot connect to inner service 'GroupService'. Authentication Required. ");
		}

		return -1;
	}

	/**
	 * Creates a new role on Liferay. For testing use only.
	 * 
	 * @param name
	 *            name of the new RoleSoap.
	 * @return
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws AuthenticationRequired
	 * @throws WebServiceAccessError
	 */
	public IRole<Long> getRole(long roleId) throws NotConnectedToWebServiceException, ClientProtocolException, IOException, AuthenticationRequired,
			WebServiceAccessError {
		checkConnection();

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("roleId", Long.toString(roleId)));

		String result = getHttpResponse("role/get-role", params);
		IRole<Long> role = null;
		if (result != null) {
			// A Simple JSON Response Read
			role = decodeFromJson(result, Role.class);
			return role;
		}

		return null;
	}

	/**
	 * Creates a new RoleSoap on Liferay. For testing use only.
	 * 
	 * @param name
	 *            name of the new RoleSoap.
	 * @return
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws AuthenticationRequired
	 * @throws WebServiceAccessError
	 */
	public IRole<Long> getRole(String roleName, long companyId) throws NotConnectedToWebServiceException, ClientProtocolException, IOException,
			AuthenticationRequired, WebServiceAccessError {
		checkConnection();

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("companyId", Long.toString(companyId)));
		params.add(new BasicNameValuePair("name", roleName));

		String result = getHttpResponse("role/get-role", params);
		IRole<Long> role = null;
		if (result != null) {
			// A Simple JSON Response Read
			role = decodeFromJson(result, Role.class);
			return role;
		}

		return null;
	}

	/**
	 * Get the list of roles for a user.
	 * 
	 * @param user
	 * @return
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws AuthenticationRequired
	 */
	public Set<IRole<Long>> getUserRoles(IUser<Long> user) throws NotConnectedToWebServiceException, ClientProtocolException, IOException,
			AuthenticationRequired {
		Set<IRole<Long>> roles = new HashSet<IRole<Long>>();
		if (user != null) {
			Set<IRole<Long>> userRoles = rolePool.getUserRoles(user);
			if (userRoles != null) {
				return userRoles;
			}
			checkConnection();

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userId", user.getId() + ""));

			String result = getHttpResponse("role/get-user-roles", params);
			if (result != null) {
				// A Simple JSON Response Read
				roles = decodeListFromJson(result, Role.class);
				rolePool.addUserRoles(user, roles);
				return roles;
			}

			return null;
		}
		return roles;
	}

	/**
	 * Gets the roles of a user in a group.
	 * 
	 * @param user
	 * @param group
	 * @return
	 * @throws NotConnectedToWebServiceException
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws AuthenticationRequired
	 */
	public Set<IRole<Long>> getUserRolesOfGroup(IUser<Long> user, Group group) throws NotConnectedToWebServiceException, ClientProtocolException, IOException,
			AuthenticationRequired {
		if (group != null && user != null) {
			return getUserRolesOfGroup(user.getId(), group.getGroupId());
		}
		return new HashSet<IRole<Long>>();
	}

	/**
	 * Gets the roles of a user in a group.
	 * 
	 * @param userId
	 * @param groupId
	 * @return
	 * @throws NotConnectedToWebServiceException
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws AuthenticationRequired
	 */
	public Set<IRole<Long>> getUserRolesOfGroup(Long userId, Long groupId) throws NotConnectedToWebServiceException, ClientProtocolException, IOException,
			AuthenticationRequired {
		Set<IRole<Long>> roles = new HashSet<IRole<Long>>();

		if (groupId != null && userId != null) {
			Set<IRole<Long>> groupRoles = rolePool.getUserRolesOfGroup(userId, groupId);

			if (groupRoles != null) {
				return groupRoles;
			}
			checkConnection();

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("groupId", groupId + ""));
			params.add(new BasicNameValuePair("userId", userId + ""));

			String result = getHttpResponse("role/get-user-group-roles", params);
			if (result != null) {
				// A Simple JSON Response Read
				roles = decodeListFromJson(result, Role.class);
				rolePool.addUserRolesOfGroup(userId, groupId, roles);
				return roles;
			}

			return null;
		}
		return roles;
	}

	/**
	 * Gets all roles of a user for an organization. Needs the use of
	 * GroupService
	 * 
	 * @param userId
	 * @param organizationId
	 * @return
	 * @throws AuthenticationRequired
	 * @throws IOException
	 * @throws NotConnectedToWebServiceException
	 * @throws ClientProtocolException
	 * @throws WebServiceAccessError
	 */
	public Set<IRole<Long>> getUserRolesOfOrganization(IUser<Long> user, IGroup<Long> organization) throws ClientProtocolException,
			NotConnectedToWebServiceException, IOException, AuthenticationRequired, WebServiceAccessError {
		if (user != null && organization != null) {
			// Get the group of the organization.
			Long groupId = getOrganizationGroupId(organization);
			return getUserRolesOfGroup(user.getId(), groupId);
		}
		return new HashSet<IRole<Long>>();
	}

	/**
	 * Gets all users that have a specific role in an organization.
	 * 
	 * @throws WebServiceAccessError
	 * @throws AuthenticationRequired
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public Set<IUser<Long>> getUsers(IRole<Long> role, IGroup<Long> organization) throws ClientProtocolException, IOException,
			NotConnectedToWebServiceException, AuthenticationRequired, WebServiceAccessError {
		Set<IUser<Long>> usersOfOrganizationWithRole = new HashSet<IUser<Long>>();
		Set<IUser<Long>> usersOfOrganization = organizationService.getOrganizationUsers(organization);

		for (IUser<Long> user : usersOfOrganization) {
			Set<IRole<Long>> roles = getUserRolesOfOrganization(user, organization);
			if (roles.contains(role)) {
				usersOfOrganizationWithRole.add(user);
			}
		}

		return usersOfOrganizationWithRole;
	}

	public void reset() {
		rolePool.reset();
	}

	/**
	 * Unset a role from a group but does not deletes it.
	 * 
	 * @param role
	 * @param group
	 * @throws AuthenticationRequired
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public void unsetRoleFromGroups(IRole<Long> role, List<Group> groups) throws ClientProtocolException, IOException, NotConnectedToWebServiceException,
			AuthenticationRequired {
		if (role != null && groups != null && !groups.isEmpty()) {
			checkConnection();

			String groupIds = "";
			if (groups.size() > 0) {
				groupIds = "[";
			}
			for (int i = 0; i < groups.size(); i++) {
				groupIds += groups.get(i).getGroupId();
				if (i < groups.size() - 1) {
					groupIds += ",";
				}
			}
			if (groupIds.length() > 0) {
				groupIds += "]";
			}

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("roleId", role.getId() + ""));
			params.add(new BasicNameValuePair("groupIds", groupIds));

			getHttpResponse("group/unset-role-groups", params);

			for (Group group : groups) {
				rolePool.removeGroupRole(role, group);
			}

			LiferayClientLogger.info(this.getClass().getName(), "IRole<Long> '" + role.getUniqueName() + "' unsetted from groups " + groupIds);
		}
	}

	/**
	 * Unset a role from a group but does not deletes it.
	 * 
	 * @param role
	 * @param group
	 * @throws AuthenticationRequired
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws WebServiceAccessError
	 */
	public void unsetRoleFromOrganization(IRole<Long> role, List<IGroup<Long>> organizations) throws ClientProtocolException, IOException,
			NotConnectedToWebServiceException, AuthenticationRequired, WebServiceAccessError {
		if (role != null && organizations != null && !organizations.isEmpty()) {
			checkConnection();

			String groupIds = "";
			if (organizations.size() > 0) {
				groupIds = "[";
			}
			for (int i = 0; i < organizations.size(); i++) {
				groupIds += getOrganizationGroupId(organizations.get(i));
				if (i < organizations.size() - 1) {
					groupIds += ",";
				}
			}
			if (groupIds.length() > 0) {
				groupIds += "]";
			}

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("roleId", role.getId() + ""));
			params.add(new BasicNameValuePair("groupIds", groupIds));

			getHttpResponse("group/unset-role-groups", params);

			for (IGroup<Long> organization : organizations) {
				rolePool.removeGroupRole(role, organization);
			}

			LiferayClientLogger.info(this.getClass().getName(), "IRole<Long> '" + role.getUniqueName() + "' unsetted from organizations " + groupIds);
		}
	}
}
