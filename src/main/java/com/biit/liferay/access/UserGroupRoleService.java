package com.biit.liferay.access;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.log.LiferayClientLogger;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liferay.portal.model.Role;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;
import com.liferay.portal.model.UserGroupRole;

public class UserGroupRoleService extends ServiceAccess<UserGroupRole> {
	private final static UserGroupRoleService instance = new UserGroupRoleService();

	private UserGroupRoleService() {

	}

	public static UserGroupRoleService getInstance() {
		return instance;
	}

	@Override
	public List<UserGroupRole> decodeListFromJson(String json, Class<UserGroupRole> objectClass)
			throws JsonParseException, JsonMappingException, IOException {
		List<UserGroupRole> myObjects = new ObjectMapper().readValue(json, new TypeReference<List<UserGroupRole>>() {
		});

		return myObjects;
	}

	/**
	 * Add a list of roles to a UserSoap. For testing use only.
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
		if (userGroup != null && roles != null && roles.size() > 0) {
			checkConnection();

			String rolesIds = "";
			if (roles.size() > 0) {
				rolesIds = "[";
			}
			for (int i = 0; i < roles.size(); i++) {
				rolesIds += roles.get(i).getUserId();
				if (i < roles.size() - 1) {
					rolesIds += ",";
				}
			}
			if (rolesIds.length() > 0) {
				rolesIds += "]";
			}

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userId", user.getUserId() + ""));
			params.add(new BasicNameValuePair("groupId", userGroup.getUserGroupId() + ""));
			params.add(new BasicNameValuePair("roleIds", rolesIds));

			getHttpResponse("usergrouprole/add-user-group-roles", params);
			LiferayClientLogger.info(this.getClass().getName(), "Roles ids " + rolesIds + " added to group '"
					+ userGroup.getName() + "' and user '" + user.getUserId() + "'");

		}
	}

	/**
	 * Add a RoleSoap to a UserSoap. For testing use only.
	 * 
	 * @param UserSoap
	 * @param RoleSoap
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws AuthenticationRequired
	 */
	public void addUserGroupRole(User UserSoap, UserGroup UserGroupSoap, Role RoleSoap)
			throws NotConnectedToWebServiceException, ClientProtocolException, IOException, AuthenticationRequired {
		List<Role> roles = new ArrayList<Role>();
		roles.add(RoleSoap);
		addUserGroupRoles(UserSoap, UserGroupSoap, roles);
	}

}
