package com.biit.liferay.access;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import com.biit.liferay.access.exceptions.NotConnectedToWebServiceException;
import com.biit.liferay.configuration.ConfigurationReader;
import com.liferay.portal.model.Company;
import com.liferay.portal.service.http.CompanyServiceSoap;
import com.liferay.portal.service.http.CompanyServiceSoapServiceLocator;

/**
 * This class allows to obtain a liferay portal instance.
 */
public class CompanyService implements LiferayService {
	private final static String SERVICE_COMPANY_NAME = "Portal_CompanyService";
	private CompanyServiceSoap companyServiceSoap = null;
	private final static CompanyService instance = new CompanyService();

	private CompanyService() {

	}

	@Override
	public void connectToWebService(String loginUser, String password) throws ServiceException {
		// Locate the User service
		CompanyServiceSoapServiceLocator locatorCompany = new CompanyServiceSoapServiceLocator();
		companyServiceSoap = locatorCompany.getPortal_CompanyService(AccessUtils.getLiferayUrl(loginUser, password,
				SERVICE_COMPANY_NAME));
	}

	@Override
	public void connectToWebService() throws ServiceException {
		// Read user and password.
		String loginUser = ConfigurationReader.getInstance().getUser();
		String password = ConfigurationReader.getInstance().getPassword();
		// Locate the User service.
		connectToWebService(loginUser, password);
	}

	@Override
	public boolean isNotConnected() {
		return companyServiceSoap == null;
	}

	@Override
	public void checkConnection() throws NotConnectedToWebServiceException {
		if (isNotConnected()) {
			throw new NotConnectedToWebServiceException(
					"user credentials are needed to use Liferay webservice. Use the connect method for this.");
		}
	}

	public static CompanyService getInstance() {
		return instance;
	}

	/**
	 * Returns the company with the virtual host name.
	 * 
	 * @param virtualHost
	 *            the company's virtual host name.
	 * @return Returns the company with the virtual host name.
	 * @throws RemoteException
	 *             if there is a communication problem
	 * 
	 */
	public Company getCompanyByVirtualHost(String virtualHost) throws RemoteException {
		return companyServiceSoap.getCompanyByVirtualHost(virtualHost);
	}

	/**
	 * Returns the company with the virtual host name.
	 * 
	 * @param companyId
	 *            the primary key of the company
	 * @return Returns the company with the virtual host name.
	 * @throws RemoteException
	 *             if there is a communication problem
	 */
	public Company getCompanyById(long companyId) throws RemoteException {
		return companyServiceSoap.getCompanyById(companyId);
	}

	/**
	 * Returns the company with the web domain.
	 * 
	 * @param webId
	 *            The company's web domain
	 * @return Returns the company with the virtual host name.
	 * @throws RemoteException
	 *             if there is a communication problem
	 */
	public Company getCompanyByWebId(String webId) throws RemoteException {
		return companyServiceSoap.getCompanyByWebId(webId);
	}

}
