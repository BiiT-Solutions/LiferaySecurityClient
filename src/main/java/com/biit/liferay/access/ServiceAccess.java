package com.biit.liferay.access;

import java.rmi.Remote;

import javax.xml.rpc.ServiceException;

import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.configuration.ConfigurationReader;

/**
 * Common methods for accessing to a Liferay web service.
 */
public abstract class ServiceAccess implements LiferayService {
	private Remote serviceSoap = null;

	public Remote getServiceSoap() {
		return serviceSoap;
	}

	public void setServiceSoap(Remote serviceSoap) {
		this.serviceSoap = serviceSoap;
	}

	/**
	 * Gets a string that identify the name of the service exposed by the
	 * Liferay web services.
	 * 
	 * @return
	 */
	public abstract String getServiceName();

	@Override
	public boolean isNotConnected() {
		return getServiceSoap() == null;
	}

	@Override
	public void disconnect() {
		serviceSoap = null;
	}

	@Override
	public void connectToWebService() throws ServiceException {
		// Read user and password.
		String loginUser = ConfigurationReader.getInstance().getUser();
		String password = ConfigurationReader.getInstance().getPassword();
		// Locate the Role service.
		connectToWebService(loginUser, password);
	}

	@Override
	public void checkConnection() throws NotConnectedToWebServiceException {
		if (isNotConnected()) {
			throw new NotConnectedToWebServiceException(
					"user credentials are needed to use Liferay webservice. Use the 'connectToWebService' method for this.");
		}
	}

}
