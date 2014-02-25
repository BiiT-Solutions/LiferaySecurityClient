package com.biit.liferay.access;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.WebServiceAccessError;
import com.biit.liferay.log.LiferayClientLogger;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;

/**
 * This class allows to manage roles from Liferay portal.
 */
public class RoleService extends ServiceAccess<Role> {
	private static final String LIFERAY_ORGANIZATION_GROUP_PREFIX = " LFR_ORGANIZATION";
	private final static RoleService instance = new RoleService();
	private RolesPool rolePool;
	// Relationship between organization and groups.
	private HashMap<Long, Long> organizationGroups;

	private RoleService() {
		rolePool = new RolesPool();
		organizationGroups = new HashMap<Long, Long>();
	}

	public static RoleService getInstance() {
		return instance;
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
	public Role addRole(String name, int type, Map<String, String> titleMap, Map<String, String> descriptionMap)
			throws NotConnectedToWebServiceException, ClientProtocolException, IOException, AuthenticationRequired,
			WebServiceAccessError {
		if (name != null && name.length() > 0) {
			checkConnection();

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("name", name));
			params.add(new BasicNameValuePair("titleMap", convertMapToJson(titleMap)));
			params.add(new BasicNameValuePair("descriptionMap", convertMapToJson(descriptionMap)));
			params.add(new BasicNameValuePair("type", Integer.toString(type)));

			String result = getHttpResponse("role/add-role", params);
			Role role = null;
			if (result != null) {
				// A Simple JSON Response Read
				role = decodeFromJson(result, Role.class);
				LiferayClientLogger.info(this.getClass().getName(), "Role '" + role.getName() + "' added.");
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
	public void addRoleGroup(Role role, UserGroup userGroup) throws NotConnectedToWebServiceException,
			ClientProtocolException, IOException, AuthenticationRequired {
		List<UserGroup> groups = new ArrayList<UserGroup>();
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
	public void addRoleGroups(Role role, List<UserGroup> userGroups) throws NotConnectedToWebServiceException,
			ClientProtocolException, IOException, AuthenticationRequired {
		if (userGroups != null && role != null && userGroups.size() > 0) {
			checkConnection();
			String groupIds = "";
			if (userGroups.size() > 0) {
				groupIds = "[";
			}
			for (int i = 0; i < userGroups.size(); i++) {
				groupIds += userGroups.get(i).getUserGroupId();
				if (i < userGroups.size() - 1) {
					groupIds += ",";
				}
			}
			if (groupIds.length() > 0) {
				groupIds += "]";
			}

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("roleId", Long.toString(role.getRoleId())));
			params.add(new BasicNameValuePair("groupIds", groupIds));

			getHttpResponse("group/add-role-groups", params);
			LiferayClientLogger.info(this.getClass().getName(),
					"Groups ids " + groupIds + " added to role '" + role.getName() + "'");
			for (UserGroup group : userGroups) {
				rolePool.addUserGroupRole(group, role);
			}
		}
	}

	public void addRoleOrganization(Role role, Organization organization) throws ClientProtocolException,
			NotConnectedToWebServiceException, IOException, AuthenticationRequired, WebServiceAccessError {
		List<Organization> organizations = new ArrayList<Organization>();
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
	public void addRoleOrganizations(Role role, List<Organization> organizations)
			throws NotConnectedToWebServiceException, ClientProtocolException, IOException, AuthenticationRequired,
			WebServiceAccessError {
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
			params.add(new BasicNameValuePair("roleId", Long.toString(role.getRoleId())));
			params.add(new BasicNameValuePair("groupIds", groupIds));

			getHttpResponse("group/add-role-groups", params);
			LiferayClientLogger.info(this.getClass().getName(),
					"Groups ids " + groupIds + " added to role '" + role.getName() + "'");
		}
	}

	public void addRoleUser(User user, Role role) throws NotConnectedToWebServiceException, ClientProtocolException,
			IOException, AuthenticationRequired {
		List<User> users = new ArrayList<User>();
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
	public void addRoleUsers(List<User> users, Role role) throws NotConnectedToWebServiceException,
			ClientProtocolException, IOException, AuthenticationRequired {
		if (users != null && users != null && users.size() > 0) {
			checkConnection();

			String userIds = "";
			if (users.size() > 0) {
				userIds = "[";
			}
			for (int i = 0; i < users.size(); i++) {
				userIds += users.get(i).getUserId();
				if (i < users.size() - 1) {
					userIds += ",";
				}
			}
			if (userIds.length() > 0) {
				userIds += "]";
			}

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("roleId", Long.toString(role.getRoleId())));
			params.add(new BasicNameValuePair("userIds", userIds));

			getHttpResponse("user/add-role-users", params);

			for (User user : users) {
				rolePool.addUserRole(user, role);
			}

			LiferayClientLogger.info(this.getClass().getName(),
					"User ids " + userIds + " added to role '" + role.getName() + "'");
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
	public void addUserRole(User user, Role role) throws NotConnectedToWebServiceException, ClientProtocolException,
			IOException, AuthenticationRequired {
		List<Role> roles = new ArrayList<Role>();
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
	public void addUserRoles(User user, List<Role> roles) throws NotConnectedToWebServiceException,
			ClientProtocolException, IOException, AuthenticationRequired {
		if (user != null && roles != null && roles.size() > 0) {
			checkConnection();

			String rolesIds = "";
			if (roles.size() > 0) {
				rolesIds = "[";
			}
			for (int i = 0; i < roles.size(); i++) {
				rolesIds += roles.get(i).getRoleId();
				if (i < roles.size() - 1) {
					rolesIds += ",";
				}
			}
			if (rolesIds.length() > 0) {
				rolesIds += "]";
			}

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userId", Long.toString(user.getUserId())));
			params.add(new BasicNameValuePair("roleIds", rolesIds));

			getHttpResponse("role/add-user-roles", params);

			for (Role role : roles) {
				rolePool.addUserRole(user, role);
			}

			LiferayClientLogger.info(this.getClass().getName(),
					"Roles ids " + rolesIds + " added to user '" + user.getScreenName() + "'");
		}
	}

	@Override
	public List<Role> decodeListFromJson(String json, Class<Role> objectClass) throws JsonParseException,
			JsonMappingException, IOException {
		List<Role> myObjects = new ObjectMapper().readValue(json, new TypeReference<List<Role>>() {
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
	public void deleteRole(Role role) throws NotConnectedToWebServiceException, ClientProtocolException, IOException,
			AuthenticationRequired {
		if (role != null) {
			checkConnection();

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("roleId", role.getRoleId() + ""));

			getHttpResponse("role/delete-role", params);

			rolePool.removeRole(role);
			LiferayClientLogger.info(this.getClass().getName(), "Role '" + role.getName() + "' deleted.");

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
	public void deleteRole(User user, Role role) throws NotConnectedToWebServiceException, ClientProtocolException,
			IOException, AuthenticationRequired {
		checkConnection();

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userId", user.getUserId() + ""));
		params.add(new BasicNameValuePair("roleId", role.getRoleId() + ""));

		getHttpResponse("user/delete-role-user", params);

		rolePool.removeUserRole(user, role);

		LiferayClientLogger.info(this.getClass().getName(),
				"Role '" + role.getName() + "' of user '" + user.getScreenName() + "' deleted.");
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
	public List<Role> getGroupRoles(UserGroup group) throws NotConnectedToWebServiceException, ClientProtocolException,
			IOException, AuthenticationRequired {
		List<Role> roles = new ArrayList<Role>();
		if (group != null) {
			List<Role> groupRoles = rolePool.getGroupRoles(group);
			if (groupRoles != null) {
				return groupRoles;
			}
			checkConnection();

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("groupId", group.getUserGroupId() + ""));

			String result = getHttpResponse("role/get-group-roles", params);

			if (result != null) {
				// A Simple JSON Response Read
				roles = decodeListFromJson(result, Role.class);
				rolePool.addUserGroupRoles(group, roles);
				return roles;
			}

			return null;
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
	public List<Role> getOrganizationRoles(Organization organization) throws NotConnectedToWebServiceException,
			ClientProtocolException, IOException, AuthenticationRequired, WebServiceAccessError {
		List<Role> roles = new ArrayList<Role>();
		if (organization != null) {
			List<Role> groupRoles = rolePool.getOrganizationRoles(organization);
			if (groupRoles != null) {
				return groupRoles;
			}
			checkConnection();

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("groupId", getOrganizationGroupId(organization) + ""));

			String result = getHttpResponse("role/get-group-roles", params);

			if (result != null) {
				// A Simple JSON Response Read
				roles = decodeListFromJson(result, Role.class);
				rolePool.addOrganizationRoles(organization, roles);
				return roles;
			}

			return null;
		}
		return roles;
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
	public Role getRole(long roleId) throws NotConnectedToWebServiceException, ClientProtocolException, IOException,
			AuthenticationRequired, WebServiceAccessError {
		checkConnection();

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("roleId", Long.toString(roleId)));

		String result = getHttpResponse("role/get-role", params);
		Role role = null;
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
	public List<Role> getUserRoles(User user) throws NotConnectedToWebServiceException, ClientProtocolException,
			IOException, AuthenticationRequired {
		List<Role> roles = new ArrayList<Role>();
		if (user != null) {
			List<Role> userRoles = rolePool.getUserRoles(user);
			if (userRoles != null) {
				return userRoles;
			}
			checkConnection();

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userId", user.getUserId() + ""));

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
	 * @param userId
	 * @param groupId
	 * @return
	 * @throws NotConnectedToWebServiceException
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws AuthenticationRequired
	 */
	public List<Role> getUserRolesOfGroup(Long userId, Long groupId) throws NotConnectedToWebServiceException,
			ClientProtocolException, IOException, AuthenticationRequired {
		List<Role> roles = new ArrayList<Role>();

		if (groupId != null && userId != null) {
			List<Role> groupRoles = rolePool.getUserRolesOfGroup(userId, groupId);

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
	public List<Role> getUserRolesOfGroup(User user, Group group) throws NotConnectedToWebServiceException,
			ClientProtocolException, IOException, AuthenticationRequired {
		if (group != null && user != null) {
			return getUserRolesOfGroup(user.getUserId(), group.getGroupId());
		}
		return new ArrayList<Role>();
	}

	/**
	 * Gets all roles of a user for an organization. Needs the use of GroupService
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
	public List<Role> getUserRolesOfOrganization(User user, Organization organization) throws ClientProtocolException,
			NotConnectedToWebServiceException, IOException, AuthenticationRequired, WebServiceAccessError {
		if (user != null && organization != null) {
			// Get the group of the organization.
			Long groupId = getOrganizationGroupId(organization);
			return getUserRolesOfGroup(user.getUserId(), groupId);
		}
		return new ArrayList<Role>();
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
	public void unsetRoleFromGroups(Role role, List<Group> groups) throws ClientProtocolException, IOException,
			NotConnectedToWebServiceException, AuthenticationRequired {
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
			params.add(new BasicNameValuePair("roleId", role.getRoleId() + ""));
			params.add(new BasicNameValuePair("groupIds", groupIds));

			getHttpResponse("group/unset-role-groups", params);

			for (Group group : groups) {
				rolePool.removeGroupRole(role, group);
			}

			LiferayClientLogger.info(this.getClass().getName(), "Role '" + role.getName() + "' unsetted from groups "
					+ groupIds);
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
	public void unsetRoleFromOrganization(Role role, List<Organization> organizations) throws ClientProtocolException,
			IOException, NotConnectedToWebServiceException, AuthenticationRequired, WebServiceAccessError {
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
			params.add(new BasicNameValuePair("roleId", role.getRoleId() + ""));
			params.add(new BasicNameValuePair("groupIds", groupIds));

			getHttpResponse("group/unset-role-groups", params);

			for (Organization organization : organizations) {
				rolePool.removeOrganizationRole(role, organization);
			}

			LiferayClientLogger.info(this.getClass().getName(), "Role '" + role.getName()
					+ "' unsetted from organizations " + groupIds);
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
	public void addUserGroupRoles(Long userId, Long groupId, List<Role> roles)
			throws NotConnectedToWebServiceException, ClientProtocolException, IOException, AuthenticationRequired {
		if (userId != null && groupId != null && roles != null && !roles.isEmpty()) {
			checkConnection();

			String rolesIds = "";
			if (roles.size() > 0) {
				rolesIds = "[";
			}
			for (int i = 0; i < roles.size(); i++) {
				rolesIds += roles.get(i).getRoleId();
				if (i < roles.size() - 1) {
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

			LiferayClientLogger.info(this.getClass().getName(), "Roles ids " + rolesIds + " added to group '" + groupId
					+ "' and user '" + userId + "'");

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
	public void addUserOrganizationRoles(User user, Organization organization, List<Role> roles)
			throws NotConnectedToWebServiceException, ClientProtocolException, IOException, AuthenticationRequired,
			WebServiceAccessError {
		if (user != null && organization != null && roles != null && !roles.isEmpty()) {
			long organizationGroupId = getOrganizationGroupId(organization);
			addUserGroupRoles(user.getUserId(), organizationGroupId, roles);
		}
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
	public void addUserGroupRoles(User user, UserGroup userGroup, List<Role> roles)
			throws NotConnectedToWebServiceException, ClientProtocolException, IOException, AuthenticationRequired {
		if (user != null && userGroup != null && roles != null && !roles.isEmpty()) {
			addUserGroupRoles(user.getUserId(), userGroup.getUserGroupId(), roles);
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
	public void addUserOrganizationRole(User user, Organization organization, Role role)
			throws NotConnectedToWebServiceException, ClientProtocolException, IOException, AuthenticationRequired,
			WebServiceAccessError {
		if (user != null && organization != null && role != null) {
			List<Role> roles = new ArrayList<Role>();
			roles.add(role);
			addUserOrganizationRoles(user, organization, roles);
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
	public void addUserGroupRole(User user, UserGroup userGroup, Role role) throws NotConnectedToWebServiceException,
			ClientProtocolException, IOException, AuthenticationRequired {
		List<Role> roles = new ArrayList<Role>();
		roles.add(role);
		addUserGroupRoles(user, userGroup, roles);
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
	public long getOrganizationGroupId(Organization organization) throws ClientProtocolException,
			NotConnectedToWebServiceException, IOException, AuthenticationRequired, WebServiceAccessError {

		if (organizationGroups.get(organization.getOrganizationId()) != null) {
			return organizationGroups.get(organization.getOrganizationId());
		}

		try {
			Group group = GroupService.getInstance().getGroup(organization.getCompanyId(),
					organization.getName() + LIFERAY_ORGANIZATION_GROUP_PREFIX);
			if (group != null) {
				organizationGroups.put(organization.getOrganizationId(), group.getGroupId());
				return group.getGroupId();
			}
		} catch (AuthenticationRequired e) {
			throw new AuthenticationRequired(
					"Cannot connect to inner service 'GroupService'. Authentication Required. ");
		}

		return -1;
	}
}
