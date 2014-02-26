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
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;

/**
 * This class allows to manage group from Liferay portal.
 */
public class UserGroupService extends ServiceAccess<UserGroup> {

	public UserGroupService() {
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
				UserGroupPool.getInstance().addGroup(userGroup);
				return userGroup;
			}

		}
		return null;
	}

	/**
	 * Add a list of users to a group. For testing use only.
	 * 
	 * @param users
	 * @param group
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws AuthenticationRequired
	 */
	public void addUsersToGroup(List<User> users, UserGroup group) throws ClientProtocolException, IOException,
			NotConnectedToWebServiceException, AuthenticationRequired {
		if (users != null && users.size() > 0 && group != null) {
			checkConnection();
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			String usersId = "";
			if (users.size() > 0) {
				usersId = "[";
			}
			for (int i = 0; i < users.size(); i++) {
				usersId += users.get(i).getUserId();
				if (i < users.size() - 1) {
					usersId += ",";
				}
			}
			if (usersId.length() > 0) {
				usersId += "]";
			}
			params.add(new BasicNameValuePair("userIds", usersId));
			params.add(new BasicNameValuePair("userGroupId", group.getUserGroupId() + ""));

			getHttpResponse("user/add-user-group-users", params);
			LiferayClientLogger.info(this.getClass().getName(),
					"Users ids " + usersId + " added to group '" + group.getName() + "'");

			for (User user : users) {
				UserGroupsPool.getInstance().addUserGroup(user, group);
			}
		}
	}

	/**
	 * Add a user to a group. For testing use only.
	 * 
	 * @param users
	 * @param group
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws AuthenticationRequired
	 */
	public void addUserToGroup(User user, UserGroup group) throws NotConnectedToWebServiceException,
			ClientProtocolException, IOException, AuthenticationRequired {
		List<User> users = new ArrayList<User>();
		users.add(user);
		addUsersToGroup(users, group);
	}

	@Override
	public List<UserGroup> decodeListFromJson(String json, Class<UserGroup> objectClass) throws JsonParseException,
			JsonMappingException, IOException {
		List<UserGroup> myObjects = new ObjectMapper().readValue(json, new TypeReference<List<UserGroup>>() {
		});

		return myObjects;
	}

	public void deleteUserFromUserGroup(User user, UserGroup userGroup) throws NotConnectedToWebServiceException,
			ClientProtocolException, IOException, AuthenticationRequired {
		if (user != null && userGroup != null) {
			checkConnection();

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userGroupId", Long.toString(userGroup.getUserGroupId())));
			params.add(new BasicNameValuePair("userIds", Long.toString(user.getUserId())));

			getHttpResponse("user/unset-user-group-users", params);
			UserGroupsPool.getInstance().removeUserGroups(user);

			LiferayClientLogger.info(this.getClass().getName(), "User '" + user.getScreenName() + "' unset from '"
					+ userGroup.getName() + "'.");

		}
	}

	/**
	 * Removes a group from Liferay portal. For testing use only.
	 * 
	 * @param userGroupId
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws AuthenticationRequired
	 */
	public void deleteUserGroup(long userGroupId) throws NotConnectedToWebServiceException, ClientProtocolException,
			IOException, AuthenticationRequired {
		if (userGroupId > 0) {
			checkConnection();

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userGroupId", Long.toString(userGroupId)));

			getHttpResponse("usergroup/delete-user-group", params);
			UserGroupPool.getInstance().removeGroup(userGroupId);
			UserGroupsPool.getInstance().removeUserGroup(userGroupId);

			LiferayClientLogger.info(this.getClass().getName(), "Group with id '" + userGroupId + "' deleted.");

		}
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
			UserGroupPool.getInstance().removeGroup(userGroup.getUserGroupId());
			UserGroupsPool.getInstance().removeUserGroup(userGroup);

			LiferayClientLogger.info(this.getClass().getName(), "Group '" + userGroup.getName() + "' deleted.");

		}
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
			UserGroup group = UserGroupPool.getInstance().getGroup(userGroupId);
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
				UserGroupPool.getInstance().addGroup(userGroup);
				return userGroup;
			}

			throw new UserGroupDoesNotExistException("Group with id '" + userGroupId + "' does not exists.");
		}
		return null;
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
			UserGroup group = UserGroupPool.getInstance().getGroup(name);
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
				UserGroupPool.getInstance().addGroup(userGroup);
				return userGroup;
			}

			throw new UserGroupDoesNotExistException("Group '" + name + "' does not exists.");
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
			List<UserGroup> usergroups = UserGroupsPool.getInstance().getGroupsByUser(user);
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
				UserGroupsPool.getInstance().addUserGroups(user, groups);
				return groups;
			}
		}
		return groups;
	}

}
