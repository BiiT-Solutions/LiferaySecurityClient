package com.biit.liferay.access;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.UserGroupDoesNotExistException;
import com.biit.liferay.access.exceptions.WebServiceAccessError;
import com.biit.liferay.log.LiferayClientLogger;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;

/**
 * This class allows to manage group from Liferay portal.
 */
public class UserGroupService extends ServiceAccess<UserGroup> {
	private final static UserGroupService instance = new UserGroupService();
	private UserGroupsPool userGroupsPool;
	private GroupPool groupPool;

	private UserGroupService() {
		userGroupsPool = new UserGroupsPool();
		groupPool = new GroupPool();
	}

	public static UserGroupService getInstance() {
		return instance;
	}

	@Override
	public List<UserGroup> decodeListFromJson(String json, Class<UserGroup> objectClass) throws JsonParseException,
			JsonMappingException, IOException {
		List<UserGroup> myObjects = new ObjectMapper().readValue(json, new TypeReference<List<UserGroup>>() {
		});

		return myObjects;
	}

	/**
	 * Get group information using the group's primary key.
	 * 
	 * @param userGroupId
	 *            group's primary key.
	 * @return a group.
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws AuthenticationRequired
	 * @throws WebServiceAccessError
	 */
	public UserGroup getUserGroup(long userGroupId) throws NotConnectedToWebServiceException,
			UserGroupDoesNotExistException, ClientProtocolException, IOException, AuthenticationRequired,
			WebServiceAccessError {
		if (userGroupId >= 0) {
			// Look up UserSoap in the pool.
			UserGroup group = groupPool.getGroup(userGroupId);
			if (group != null) {
				return group;
			}

			// Read from Liferay.
			checkConnection();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userGroupId", Long.toString(userGroupId)));

			String result = getHttpResponse("usergroup/get-user-group", params);
			if (result != null) {
				// A Simple JSON Response Read
				UserGroup userGroup = decodeFromJson(result, UserGroup.class);
				groupPool.addGroup(userGroup);
				return userGroup;
			}

			throw new UserGroupDoesNotExistException("Group with id '" + userGroupId + "' does not exists.");
		}
		return null;
	}

	/**
	 * Add a RoleSoap to a list of groups. For testing only.
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

		}
	}

	/**
	 * Add a RoleSoap to a group. For testing only.
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
	 * Get group information using the group's name.
	 * 
	 * @param name
	 *            name of the group
	 * @return group information
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws UserGroupDoesNotExistException
	 * @throws AuthenticationRequired
	 * @throws WebServiceAccessError
	 */
	public UserGroup getUserGroup(String name) throws NotConnectedToWebServiceException, ClientProtocolException,
			IOException, UserGroupDoesNotExistException, AuthenticationRequired, WebServiceAccessError {
		if (name != null && name.length() > 0) {
			// Look up UserSoap in the pool.
			UserGroup group = groupPool.getGroup(name);
			if (group != null) {
				return group;
			}

			// Read from Liferay.
			checkConnection();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("name", name));

			String result = getHttpResponse("usergroup/get-user-group", params);
			if (result != null) {
				// A Simple JSON Response Read
				UserGroup userGroup = decodeFromJson(result, UserGroup.class);
				groupPool.addGroup(userGroup);
				return userGroup;
			}

			throw new UserGroupDoesNotExistException("Group '" + name + "' does not exists.");
		}
		return null;
	}

	/**
	 * Creates a new group on Liferay. For testing use only.
	 * 
	 * @param name
	 *            name of the new group.
	 * @param description
	 *            description of the new group.
	 * @return
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws AuthenticationRequired
	 * @throws WebServiceAccessError
	 */
	public UserGroup addUserGroup(String name, String description) throws NotConnectedToWebServiceException,
			ClientProtocolException, IOException, AuthenticationRequired, WebServiceAccessError {
		if (name != null && name.length() > 0) {
			checkConnection();

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("name", name));
			params.add(new BasicNameValuePair("description", description));

			String result = getHttpResponse("usergroup/add-user-group", params);
			UserGroup userGroup = null;
			if (result != null) {
				// A Simple JSON Response Read
				userGroup = decodeFromJson(result, UserGroup.class);
				groupPool.addGroup(userGroup);
				return userGroup;
			}

		}
		return null;
	}

	/**
	 * Get a list of groups where the UserSoap belongs to.
	 * 
	 * @param user
	 * @return group information
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws AuthenticationRequired
	 */
	public List<UserGroup> getUserUserGroups(User user) throws NotConnectedToWebServiceException,
			ClientProtocolException, IOException, AuthenticationRequired {
		List<UserGroup> groups = new ArrayList<UserGroup>();

		// Look up UserSoap in the pool.
		if (user != null) {
			List<UserGroup> usergroups = userGroupsPool.getGroupByUser(user);
			if (usergroups != null) {
				return usergroups;
			}
			checkConnection();

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userId", Long.toString(user.getUserId())));

			String result = getHttpResponse("usergroup/get-user-user-groups", params);

			if (result != null) {
				// A Simple JSON Response Read
				groups = decodeListFromJson(result, UserGroup.class);
				userGroupsPool.addUserGroups(user, groups);
				return groups;
			}
		}
		return groups;
	}

	/**
	 * Removes a group from Liferay portal. For testing use only.
	 * 
	 * @param userGroup
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws AuthenticationRequired
	 */
	public void deleteUserGroup(UserGroup userGroup) throws NotConnectedToWebServiceException, ClientProtocolException,
			IOException, AuthenticationRequired {
		if (userGroup != null) {
			checkConnection();

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userGroupId", Long.toString(userGroup.getUserGroupId())));

			getHttpResponse("usergroup/delete-user-group", params);
			groupPool.removeGroup(userGroup.getUserGroupId());
			userGroupsPool.removeUserGroup(userGroup);

			LiferayClientLogger.info(this.getClass().getName(), "Group '" + userGroup.getName() + "' deleted.");

		}
	}

	public void deleteUserFromUserGroup(User user, UserGroup userGroup) throws NotConnectedToWebServiceException,
			ClientProtocolException, IOException, AuthenticationRequired {
		if (user != null && userGroup != null) {
			checkConnection();

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userGroupId", Long.toString(userGroup.getUserGroupId())));
			params.add(new BasicNameValuePair("userIds", Long.toString(user.getUserId())));

			getHttpResponse("user/unset-user-group-users", params);
			userGroupsPool.removeUserGroups(user);

			LiferayClientLogger.info(this.getClass().getName(), "User '" + user.getScreenName() + "' unset from '"
					+ userGroup.getName() + "'.");

		}
	}
}
