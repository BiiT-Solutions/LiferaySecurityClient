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
import com.biit.liferay.log.LiferayClientLogger;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.User;

/**
 * Manage all Organization Services. As some organization's properties are
 * defined as a group, also manage some group services.
 * 
 */
public class OrganizationService extends ServiceAccess<Organization> {
	private final static long DEFAULT_PARENT_ORGANIZATION_ID = 0;
	private final static long DEFAULT_REGION_ID = 0;
	private final static long DEFAULT_COUNTRY_ID = 0;
	private final static String DEFAULT_TYPE = "regular-organization";
	private final static boolean DEFAULT_CREATE_SITE = false;
	private final static int DEFAUL_START_GROUP = -1;
	private final static int DEFAUL_END_GROUP = -1;
	private Integer organizationStatus = null;
	private ListTypeService listTypeService;
	private CompanyService companyService;

	public OrganizationService() {
	}

	/**
	 * Creates a new organization.
	 * 
	 * @param parentOrganizationId
	 * @param name
	 * @param type
	 * @param regionId
	 * @param countryId
	 * @param statusId
	 * @param comments
	 * @param site
	 * @return
	 * @throws NotConnectedToWebServiceException
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws AuthenticationRequired
	 * @throws WebServiceAccessError
	 */
	public Organization addOrganization(Company company, Long parentOrganizationId, String name, String type,
			Long regionId, Long countryId, int statusId, String comments, boolean site)
			throws NotConnectedToWebServiceException, ClientProtocolException, IOException, AuthenticationRequired,
			WebServiceAccessError {
		// Look up user in the liferay.
		checkConnection();

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("parentOrganizationId", parentOrganizationId + ""));
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("type", type));
		params.add(new BasicNameValuePair("regionId", regionId + ""));
		params.add(new BasicNameValuePair("countryId", countryId + ""));
		params.add(new BasicNameValuePair("statusId", statusId + ""));
		params.add(new BasicNameValuePair("comments", comments));
		params.add(new BasicNameValuePair("site", Boolean.toString(site)));

		String result = getHttpResponse("organization/add-organization", params);
		Organization organization = null;
		if (result != null) {
			// A Simple JSON Response Read
			organization = decodeFromJson(result, Organization.class);
			OrganizationPool.getInstance().addOrganization(company, organization);
			LiferayClientLogger.info(this.getClass().getName(), "Organization '" + organization.getName() + "' added.");
			return organization;
		}

		return organization;
	}

	/**
	 * Creates a new organization. Requires the use of ListTypeService.
	 * 
	 * @param name
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws NotConnectedToWebServiceException
	 * @throws AuthenticationRequired
	 * @throws WebServiceAccessError
	 */
	public Organization addOrganization(Company company, String name) throws ClientProtocolException, IOException,
			NotConnectedToWebServiceException, AuthenticationRequired, WebServiceAccessError {
		return addOrganization(company, DEFAULT_PARENT_ORGANIZATION_ID, name, DEFAULT_TYPE, DEFAULT_REGION_ID,
				DEFAULT_COUNTRY_ID, getOrganizationStatus(), "", DEFAULT_CREATE_SITE);
	}

	@Override
	public List<Organization> decodeListFromJson(String json, Class<Organization> objectClass)
			throws JsonParseException, JsonMappingException, IOException {
		List<Organization> myObjects = new ObjectMapper().readValue(json, new TypeReference<List<Organization>>() {
		});

		return myObjects;
	}

	public List<Group> decodeGroupListFromJson(String json, Class<Group> objectClass) throws JsonParseException,
			JsonMappingException, IOException {
		List<Group> myObjects = new ObjectMapper().readValue(json, new TypeReference<List<Group>>() {
		});
		return myObjects;
	}

	/**
	 * Deletes an organization in Liferay database.
	 * 
	 * @param organization
	 * @throws NotConnectedToWebServiceException
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws AuthenticationRequired
	 */
	public void deleteOrganization(Company company, Organization organization)
			throws NotConnectedToWebServiceException, ClientProtocolException, IOException, AuthenticationRequired {
		if (organization != null) {
			checkConnection();

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("organizationId", organization.getOrganizationId() + ""));

			getHttpResponse("organization/delete-organization", params);

			OrganizationPool.getInstance().removeOrganization(company, organization);
			LiferayClientLogger.info(this.getClass().getName(), "Organization '" + organization.getName()
					+ "' deleted.");
		}
	}

	/**
	 * Gets all organizations of a company.
	 * 
	 * @param company
	 * @return
	 * @throws NotConnectedToWebServiceException
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws AuthenticationRequired
	 */
	public List<Organization> getOrganizations(Company company) throws NotConnectedToWebServiceException,
			ClientProtocolException, IOException, AuthenticationRequired {
		// Look up user in the pool.
		List<Organization> organizations = new ArrayList<Organization>();
		if (company != null) {
			organizations = OrganizationPool.getInstance().getOrganizations(company);
			if (organizations != null) {
				return organizations;
			}

			// Look up user in the liferay.
			checkConnection();

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("companyId", company.getCompanyId() + ""));
			params.add(new BasicNameValuePair("parentOrganizationId", DEFAULT_PARENT_ORGANIZATION_ID + ""));

			String result = getHttpResponse("organization/get-organizations", params);
			if (result != null) {
				// A Simple JSON Response Read
				organizations = decodeListFromJson(result, Organization.class);
				OrganizationPool.getInstance().addOrganizations(company, organizations);
			}
		}

		return organizations;
	}

	/**
	 * Obtains the default status from the database using a webservice. Requires
	 * the use of ListTypeService.
	 * 
	 * @return
	 * @throws ClientProtocolException
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws AuthenticationRequired
	 * @throws WebServiceAccessError
	 */
	private int getOrganizationStatus() throws ClientProtocolException, NotConnectedToWebServiceException, IOException,
			AuthenticationRequired, WebServiceAccessError {
		if (organizationStatus == null) {
			try {
				organizationStatus = listTypeService.getFullMemberStatus();
			} catch (AuthenticationRequired e) {
				throw new AuthenticationRequired(
						"Cannot connect to inner service 'ListTypeService'. Authentication Required. ");
			}
		}
		return organizationStatus;
	}

	/**
	 * Gets all organizations where the user pertains to.
	 * 
	 * @param company
	 * @param user
	 * @return
	 * @throws ClientProtocolException
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws AuthenticationRequired
	 * @throws WebServiceAccessError
	 */
	public List<Organization> getUserOrganizations(Company companyOfUser, User user) throws ClientProtocolException,
			NotConnectedToWebServiceException, IOException, AuthenticationRequired, WebServiceAccessError {
		List<Organization> organizationsOfUser = new ArrayList<Organization>();

		List<Group> usergroups = getUserOrganizationGroups(user.getUserId());

		List<Organization> allOrganizations = getOrganizations(companyOfUser);

		for (Group group : usergroups) {
			// classPK key of group references the Id of the organization.
			for (Organization organization : allOrganizations) {
				if (group.getClassPK() == organization.getOrganizationId()) {
					organizationsOfUser.add(organization);
				}
			}
		}
		return organizationsOfUser;
	}

	/**
	 * Gets all organizations where the user pertains to. Needs the inner
	 * service CompanyService.
	 * 
	 * @param user
	 * @return
	 * @throws ClientProtocolException
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws AuthenticationRequired
	 * @throws WebServiceAccessError
	 */
	public List<Organization> getUserOrganizations(User user) throws ClientProtocolException,
			NotConnectedToWebServiceException, IOException, AuthenticationRequired, WebServiceAccessError {
		Company companyOfUser = companyService.getCompanyById(user.getCompanyId());

		return getUserOrganizations(companyOfUser, user);
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
			groups = OrganizationPool.getInstance().getOrganizationGroups(userId);
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
			if (result != null) {
				// A Simple JSON Response Read
				groups = decodeGroupListFromJson(result, Group.class);
				OrganizationPool.getInstance().addOrganizationGroups(userId, groups);
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

	public List<User> getOrganizationUsers(Organization organization) throws NotConnectedToWebServiceException,
			ClientProtocolException, IOException, AuthenticationRequired {
		if (organization != null) {

			List<User> users = new ArrayList<User>();
			// Look up users in the pool.
			users = OrganizationPool.getInstance().getOrganizationUsers(organization.getOrganizationId());
			if (users != null) {
				return users;
			}

			// Look up user in the liferay.
			checkConnection();

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("organizationId", organization.getOrganizationId() + ""));

			String result = getHttpResponse("/user/get-organization-users", params);
			if (result != null) {
				// A Simple JSON Response Read
				users = (new UserService()).decodeListFromJson(result, User.class);
				OrganizationPool.getInstance().addOrganizationUsers(organization.getOrganizationId(), users);
				return users;
			}
		}
		return null;
	}

	/**
	 * Assign a user to an organization.
	 * 
	 * @param user
	 * @param organization
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws NotConnectedToWebServiceException
	 * @throws AuthenticationRequired
	 */
	public void addUserToOrganization(User user, Organization organization) throws ClientProtocolException,
			IOException, NotConnectedToWebServiceException, AuthenticationRequired {
		List<User> users = new ArrayList<User>();
		users.add(user);
		addUsersToOrganization(users, organization);
	}

	/**
	 * Assign a list of users to an organization.
	 * 
	 * @param users
	 * @param organization
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws NotConnectedToWebServiceException
	 * @throws AuthenticationRequired
	 */
	public void addUsersToOrganization(List<User> users, Organization organization) throws ClientProtocolException,
			IOException, NotConnectedToWebServiceException, AuthenticationRequired {
		if (users != null && organization != null && !users.isEmpty()) {
			// Look up user in the liferay.
			checkConnection();

			String usersIds = "";
			if (users.size() > 0) {
				usersIds = "[";
			}
			for (int i = 0; i < users.size(); i++) {
				usersIds += users.get(i).getUserId();
				if (i < users.size() - 1) {
					usersIds += ",";
				}
			}
			if (usersIds.length() > 0) {
				usersIds += "]";
			}

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("organizationId", organization.getOrganizationId() + ""));
			params.add(new BasicNameValuePair("userIds", usersIds));

			getHttpResponse("user/add-organization-users", params);

			// Reset the pool of groups to calculate again the user's
			// organization groups.
			for (User user : users) {
				OrganizationPool.getInstance().removeOrganizationGroups(user);
			}

			LiferayClientLogger.info(this.getClass().getName(), "Users " + usersIds + " added to organization '"
					+ organization.getName() + "'.");
		}
	}

	/**
	 * Remove a list of users to an organization.
	 * 
	 * @param users
	 * @param organization
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws NotConnectedToWebServiceException
	 * @throws AuthenticationRequired
	 */
	public void removeUserFromOrganization(User user, Organization organization) throws ClientProtocolException,
			IOException, NotConnectedToWebServiceException, AuthenticationRequired {
		if (user != null && organization != null) {
			// Look up user in the liferay.
			List<User> users = new ArrayList<User>();
			users.add(user);
			removeUsersFromOrganization(users, organization);

			// Reset the pool of groups to calculate again the user's
			// organization groups.
			OrganizationPool.getInstance().removeOrganizationGroups(user);
		}
	}

	/**
	 * Remove a list of users to an organization.
	 * 
	 * @param users
	 * @param organization
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws NotConnectedToWebServiceException
	 * @throws AuthenticationRequired
	 */
	public void removeUsersFromOrganization(List<User> users, Organization organization)
			throws ClientProtocolException, IOException, NotConnectedToWebServiceException, AuthenticationRequired {
		if (users != null && organization != null && !users.isEmpty()) {
			// Look up user in the liferay.
			checkConnection();

			String usersIds = "";
			if (users.size() > 0) {
				usersIds = "[";
			}
			for (int i = 0; i < users.size(); i++) {
				usersIds += users.get(i).getUserId();
				if (i < users.size() - 1) {
					usersIds += ",";
				}
			}
			if (usersIds.length() > 0) {
				usersIds += "]";
			}

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("organizationId", organization.getOrganizationId() + ""));
			params.add(new BasicNameValuePair("userIds", usersIds));

			getHttpResponse("user/unset-organization-users", params);

			// Reset the pool of groups to calculate again the user's
			// organization groups.
			for (User user : users) {
				OrganizationPool.getInstance().removeOrganizationGroups(user);
			}

			LiferayClientLogger.info(this.getClass().getName(), "Users " + usersIds + " removed from organization '"
					+ organization.getName() + "'.");
		}
	}

	@Override
	public void authorizedServerConnection(String address, String protocol, int port, String webservicesPath,
			String authenticationToken, String loginUser, String password) {
		// Standard behavior.
		super.authorizedServerConnection(address, protocol, port, webservicesPath, authenticationToken, loginUser,
				password);
		// Disconnect previous connection
		try {
			listTypeService.disconnect();
		} catch (Exception e) {

		}
		// Some user information is in the contact object.
		listTypeService = new ListTypeService();
		listTypeService.authorizedServerConnection(address, protocol, port, webservicesPath, authenticationToken,
				loginUser, password);

		try {
			companyService.disconnect();
		} catch (Exception e) {

		}
		companyService = new CompanyService();
		companyService.authorizedServerConnection(address, protocol, port, webservicesPath, authenticationToken,
				loginUser, password);
	}

	@Override
	public void disconnect() {
		super.disconnect();
		listTypeService.disconnect();
		companyService.disconnect();
	}
}
