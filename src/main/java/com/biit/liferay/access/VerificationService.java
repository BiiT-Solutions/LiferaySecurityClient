package com.biit.liferay.access;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;

import com.biit.liferay.access.exceptions.AuthenticationRequired;
import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.configuration.ConfigurationReader;
import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.entity.IUser;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.liferay.portal.model.User;

public class VerificationService extends ServiceAccess<IUser<Long>, User> {
	private final static String JSON_AUTHENTICATION_REQUIRED_STRING = "Authenticated access required";
	private final static VerificationService instance = new VerificationService();

	private VerificationService() {
	}

	public static VerificationService getInstance() {
		return instance;
	}

	private void closeClient(CloseableHttpClient httpClient) {
		// Close client.
		try {
			httpClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Set<IUser<Long>> decodeListFromJson(String json, Class<User> objectClass) throws JsonParseException,
			JsonMappingException, IOException {
		return null;
	}

	public boolean testConnection(IGroup<Long> company, String emailAddress, String password)
			throws ClientProtocolException, IOException, NotConnectedToWebServiceException, AuthenticationRequired {

		if (isNotConnected()) {
			serverConnection();
		}

		// Credentials
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(new AuthScope(getTargetHost().getHostName(), getTargetHost().getPort()),
				new UsernamePasswordCredentials(emailAddress, password));

		// Client
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider)
				.build();

		// Create AuthCache instance
		AuthCache authCache = new BasicAuthCache();
		// Generate BASIC scheme object and add it to the local auth cache
		BasicScheme basicScheme = new BasicScheme();
		authCache.put(getTargetHost(), basicScheme);
		// Add AuthCache to the execution context
		BasicHttpContext httpContext = new BasicHttpContext();
		httpContext.setAttribute(HttpClientContext.AUTH_CACHE, authCache);

		// Set
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("companyId", company.getId() + ""));
		params.add(new BasicNameValuePair("emailAddress", emailAddress));

		// Set authentication param if defined.
		setAuthParam(params);

		HttpPost post = new HttpPost("/" + ConfigurationReader.getInstance().getWebServicesPath()
				+ "user/get-user-by-email-address");
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
		post.setEntity(entity);
		HttpResponse response = httpClient.execute(getTargetHost(), post, httpContext);

		// Process answer.
		if (response.getEntity() != null) {
			// A Simple JSON Response Read
			String result = EntityUtils.toString(response.getEntity());
			if (result.contains(JSON_AUTHENTICATION_REQUIRED_STRING)) {
				closeClient(httpClient);
				throw new AuthenticationRequired("Authenticated access required.");
			}
			closeClient(httpClient);
			return true;
		}
		closeClient(httpClient);
		return false;

	}

}
