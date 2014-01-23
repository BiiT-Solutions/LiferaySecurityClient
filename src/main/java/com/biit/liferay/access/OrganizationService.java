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
import com.liferay.portal.model.Organization;

public class OrganizationService extends ServiceAccess<Organization> {
	private final static long DEFAULT_PARENT_ORGANIZATION_ID = 0;
	private final static long DEFAULT_REGION_ID = 0;
	private final static long DEFAULT_COUNTRY_ID = 0;
	private final static String DEFAULT_TYPE = "regular-organization";
	private final static boolean DEFAULT_CREATE_SITE = false;
	private final static OrganizationService instance = new OrganizationService();
	private OrganizationPool organizationPool;
	private Integer organizationStatus = null;

	// Connection information to connects to secondary services.
	private String address;
	private String protocol;
	private int port;
	private String webservicesPath;
	private String authenticationToken;
	private String loginUser;
	private String password;

	private OrganizationService() {
		organizationPool = new OrganizationPool();
	}

	public static OrganizationService getInstance() {
		return instance;
	}

	@Override
	public void serverConnection(String address, String protocol, int port, String webservicesPath,
			String authenticationToken, String loginUser, String password) {
		this.address = address;
		this.protocol = protocol;
		this.port = port;
		this.webservicesPath = webservicesPath;
		this.authenticationToken = authenticationToken;
		this.loginUser = loginUser;
		this.password = password;
		super.serverConnection(address, protocol, port, webservicesPath, authenticationToken, loginUser, password);
	}

	@Override
	public List<Organization> decodeListFromJson(String json, Class<Organization> objectClass)
			throws JsonParseException, JsonMappingException, IOException {
		List<Organization> myObjects = new ObjectMapper().readValue(json, new TypeReference<List<Organization>>() {
		});

		return myObjects;
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
		organizations = organizationPool.getOrganizations(company);
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
			organizationPool.addOrganizations(company, organizations);
		}

		return organizations;
	}

	/**
	 * Creates a new organization.
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
			organizationPool.addOrganization(company, organization);
			LiferayClientLogger.info(this.getClass().getName(), "Organization '" + organization.getName() + "' added.");
			return organization;
		}

		return organization;
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

			organizationPool.removeOrganization(company, organization);
			LiferayClientLogger.info(this.getClass().getName(), "Organization '" + organization.getName()
					+ "' deleted.");
		}
	}

	/**
	 * Obtains the default status from the database using a webservice.
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
		if (organizationStatus == null && !isNotConnected()) {
			ListTypeService.getInstance().serverConnection(address, protocol, port, webservicesPath,
					authenticationToken, loginUser, password);
			organizationStatus = ListTypeService.getInstance().getFullMemberStatus();
		}
		return organizationStatus;
	}
}
