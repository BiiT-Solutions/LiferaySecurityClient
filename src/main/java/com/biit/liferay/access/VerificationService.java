package com.biit.liferay.access;

import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.configuration.LiferayConfigurationReader;
import com.biit.usermanager.entity.IGroup;
import com.biit.usermanager.entity.IUser;
import com.biit.usermanager.security.exceptions.AuthenticationRequired;
import com.liferay.portal.log.SecurityLogger;
import com.liferay.portal.model.User;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class VerificationService extends ServiceAccess<IUser<Long>, User> {
    private static final String JSON_AUTHENTICATION_REQUIRED_STRING = "Authenticated access required";
    private static final VerificationService INSTANCE = new VerificationService();

    private VerificationService() {
    }

    public static VerificationService getInstance() {
        return INSTANCE;
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
    public Set<IUser<Long>> decodeListFromJson(String json, Class<User> objectClass) {
        return null;
    }

    public void testConnection(IGroup<Long> company, String emailAddress, String password)
            throws IOException, NotConnectedToWebServiceException, AuthenticationRequired {

        if (isNotConnected()) {
            serverConnection();
        }

        // Credentials
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(new AuthScope(getTargetHost().getHostName(), getTargetHost().getPort()),
                new UsernamePasswordCredentials(emailAddress, password));

        // Client
        final CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider)
                .build();

        // Create AuthCache instance
        final AuthCache authCache = new BasicAuthCache();
        // Generate BASIC scheme object and add it to the local auth cache
        final BasicScheme basicScheme = new BasicScheme();
        authCache.put(getTargetHost(), basicScheme);
        // Add AuthCache to the execution context
        final BasicHttpContext httpContext = new BasicHttpContext();
        httpContext.setAttribute(HttpClientContext.AUTH_CACHE, authCache);

        // Set
        final List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("companyId", company.getUniqueId() + ""));
        params.add(new BasicNameValuePair("emailAddress", emailAddress));

        // Set authentication param if defined.
        setAuthParam(params);

        final HttpPost post = new HttpPost("/" + parseProxyPrefix(LiferayConfigurationReader.getInstance().getProxyPrefix())
                + LiferayConfigurationReader.getInstance().getWebServicesPath() + "user/get-user-by-email-address");
        final UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
        post.setEntity(entity);
        final HttpResponse response = httpClient.execute(getTargetHost(), post, httpContext);

        SecurityLogger.debug(this.getClass().getName(), "Liferay response for user verification '" + response + "'.");

        // Process answer.
        if (response.getEntity() != null) {
            // A Simple JSON Response Read
            final String result = EntityUtils.toString(response.getEntity());
            if (result.contains(JSON_AUTHENTICATION_REQUIRED_STRING)) {
                closeClient(httpClient);
                throw new AuthenticationRequired("Authenticated access required.");
            } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
                closeClient(httpClient);
                throw new NotConnectedToWebServiceException("Invalid request to '"
                        + parseProxyPrefix(LiferayConfigurationReader.getInstance().getProxyPrefix())
                        + LiferayConfigurationReader.getInstance().getWebServicesPath()
                        + "user/get-user-by-email-address\"'.");
            }
            closeClient(httpClient);
        } else {
            closeClient(httpClient);
            throw new AuthenticationRequired("Authenticated access required.");
        }
    }

    @Override
    public void reset() {
    }

}
