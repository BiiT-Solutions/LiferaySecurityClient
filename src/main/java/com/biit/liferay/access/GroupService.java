package com.biit.liferay.access;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.access.exceptions.WebServiceAccessError;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.User;

public class GroupService extends ServiceAccess<Group> {
	private final static int DEFAUL_START_GROUP = -1;
	private final static int DEFAUL_END_GROUP = -1;
	private final static GroupService instance = new GroupService();

	public static GroupService getInstance() {
		return instance;
	}

	private OrganizationPool groupPool;

	private GroupService() {
		groupPool = new OrganizationPool();
	}

	@Override
	public List<Group> decodeListFromJson(String json, Class<Group> objectClass) throws JsonParseException,
			JsonMappingException, IOException {
		List<Group> myObjects = new ObjectMapper().readValue(json, new TypeReference<List<Group>>() {
		});
		return myObjects;
	}

	/**
	 * Gets all organizations of a user.
	 * 
	 * @param user
	 * @return
	 * @throws NotConnectedToWebServiceException
	 * @throws AuthenticationRequired
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public List<Group> getUserOrganizationGroups(Long userId) throws NotConnectedToWebServiceException,
			ClientProtocolException, IOException, AuthenticationRequired {
		if (userId != null) {
			List<Group> groups = new ArrayList<Group>();
			// Look up group in the pool.
			groups = groupPool.getOrganizationGroups(userId);
			System.out.println("groups " + groups);
			if (groups != null) {
				return groups;
			}

			// Look up user in the liferay.
			checkConnection();

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userId", userId + ""));
			params.add(new BasicNameValuePair("start", DEFAUL_START_GROUP + ""));
			params.add(new BasicNameValuePair("end", DEFAUL_END_GROUP + ""));

			String result = getHttpResponse("group/get-user-organizations-groups", params);
			System.out.println("result -> " + result);
			if (result != null) {
				// A Simple JSON Response Read
				groups = decodeListFromJson(result, Group.class);
				groupPool.addOrganizationGroups(userId, groups);
				return groups;
			}
		}
		return null;
	}

	/**
	 * Gets all organizations of a user.
	 * 
	 * @param user
	 * @return
	 * @throws NotConnectedToWebServiceException
	 * @throws AuthenticationRequired
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public List<Group> getUserOrganizationGroups(User user) throws NotConnectedToWebServiceException,
			ClientProtocolException, IOException, AuthenticationRequired {
		if (user != null) {
			return getUserOrganizationGroups(user.getUserId());
		}
		return null;
	}

	public Group getGroup(Long companyId, String groupName) throws NotConnectedToWebServiceException,
			ClientProtocolException, IOException, AuthenticationRequired, WebServiceAccessError {
		if (companyId != null && groupName != null) {
			// Look up user in the liferay.
			checkConnection();

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("companyId", companyId + ""));
			params.add(new BasicNameValuePair("name", groupName));

			String result = getHttpResponse("group/get-group", params);
			if (result != null) {
				// A Simple JSON Response Read
				Group group = decodeFromJson(result, Group.class);
				return group;
			}
		}
		return null;
	}

}
