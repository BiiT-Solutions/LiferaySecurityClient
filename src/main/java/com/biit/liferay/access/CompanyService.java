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
import com.liferay.portal.model.Company;

/**
 * This class allows to obtain a liferay portal instance.
 */
public class CompanyService extends ServiceAccess<Company> {
	private final static CompanyService instance = new CompanyService();
	private CompanyPool companyPool;

	public static CompanyService getInstance() {
		return instance;
	}

	private CompanyService() {
		companyPool = new CompanyPool();
	}

	@Override
	public List<Company> decodeListFromJson(String json, Class<Company> objectClass) throws JsonParseException,
			JsonMappingException, IOException {
		List<Company> myObjects = new ObjectMapper().readValue(json, new TypeReference<List<Company>>() {
		});

		return myObjects;
	}

	/**
	 * Returns the CompanySoap with the virtual host name.
	 * 
	 * @param companyId
	 *            the primary key of the CompanySoap
	 * @return Returns the CompanySoap with the virtual host name.
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws AuthenticationRequired
	 * @throws WebServiceAccessError
	 */
	public Company getCompanyById(long companyId) throws NotConnectedToWebServiceException, ClientProtocolException,
			IOException, AuthenticationRequired, WebServiceAccessError {

		Company company = companyPool.getCompanyById(companyId);
		if (company != null) {
			return company;
		}

		checkConnection();

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("companyId", companyId + ""));

		String result = getHttpResponse("company/get-company-by-id", params);
		if (result != null) {
			// A Simple JSON Response Read
			company = decodeFromJson(result, Company.class);
			companyPool.addCompany(company);
			return company;
		}

		return null;
	}

	/**
	 * Returns the CompanySoap with the virtual host name.
	 * 
	 * @param virtualHost
	 *            the CompanySoap's virtual host name.
	 * @return Returns the CompanySoap with the virtual host name.
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 * @throws AuthenticationRequired
	 * @throws WebServiceAccessError
	 * 
	 */
	public Company getCompanyByVirtualHost(String virtualHost) throws NotConnectedToWebServiceException,
			JsonParseException, JsonMappingException, IOException, AuthenticationRequired, WebServiceAccessError {

		Company company = companyPool.getCompanyByVirtualHostId(virtualHost);
		if (company != null) {
			return company;
		}

		checkConnection();

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("virtualHost", virtualHost));

		String result = getHttpResponse("company/get-company-by-virtual-host", params);
		if (result != null) {
			// A Simple JSON Response Read
			company = decodeFromJson(result, Company.class);
			companyPool.addCompany(company, virtualHost);
			return company;
		}

		return null;
	}

	/**
	 * Returns the CompanySoap with the web domain.
	 * 
	 * @param webId
	 *            The CompanySoap's web domain
	 * @return Returns the CompanySoap with the virtual host name.
	 * @throws NotConnectedToWebServiceException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws AuthenticationRequired
	 * @throws WebServiceAccessError
	 */
	public Company getCompanyByWebId(String webId) throws NotConnectedToWebServiceException, ClientProtocolException,
			IOException, AuthenticationRequired, WebServiceAccessError {

		Company company = companyPool.getCompanyByWebId(webId);
		if (company != null) {
			return company;
		}

		checkConnection();

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("webId ", webId));

		String result = getHttpResponse("company/get-company-by-web-id", params);
		if (result != null) {
			// A Simple JSON Response Read
			company = decodeFromJson(result, Company.class);
			companyPool.addCompany(company);
			return company;
		}

		return null;
	}

}
